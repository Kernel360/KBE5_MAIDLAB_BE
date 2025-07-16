package kernel.maidlab.admin.manager.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.manager.repository.AdminManagerRepository;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.manager.dto.ManagerListResponseDto;
import kernel.maidlab.domain.manager.dto.response.AdminManagerResponseDto;
import kernel.maidlab.domain.manager.entity.Manager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminManagerServiceImpl implements AdminManagerService {

	private final AdminManagerRepository adminManagerRepository;

	@Override
	@ExceptionHandler(
		value = {NullPointerException.class},
		responseType = ResponseType.THIS_RESOURCE_DOES_NOT_EXIST,
		message = "매니저를 찾을 수 없습니다",
		logLevel = LogLevel.WARN
	)
	public AdminManagerResponseDto getManager(Long id) {
		Manager manager = adminManagerRepository.findById(id)
			.orElseThrow(() -> new NullPointerException("매니저를 찾을 수 없습니다. ID: " + id));
		return AdminManagerResponseDto.getInstance(manager);
	}

	@Transactional
	@Override
	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.DataAccessException.class}
	)
	@ExceptionHandler(
		value = {NullPointerException.class, RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "매니저 거절 처리 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR
	)
	public void rejectManager(Long managerId) {
		Manager manager = adminManagerRepository.findById(managerId)
			.orElseThrow(() -> new NullPointerException("매니저를 찾을 수 없습니다. ID: " + managerId));
		manager.reject();
	}

	@Transactional
	@Override
	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.DataAccessException.class}
	)
	@ExceptionHandler(
		value = {NullPointerException.class, RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "매니저 승인 처리 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR
	)
	public void approveManager(Long managerId) {
		Manager manager = adminManagerRepository.findById(managerId)
			.orElseThrow(() -> new NullPointerException("매니저를 찾을 수 없습니다. ID: " + managerId));
		manager.approve();
	}

	@Override
	public Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status,
		boolean sortByRating, Boolean isDescending) {
		Pageable pageable;

		if (sortByRating) {
			if (isDescending)
				pageable = PageRequest.of(page, size, Sort.by("averageRate").descending());
			else
				pageable = PageRequest.of(page, size, Sort.by("averageRate").ascending());
		} else {
			pageable = PageRequest.of(page, size);
		}
		return adminManagerRepository.findAllByIsVerified(status, pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

	@Override
	public Page<ManagerListResponseDto> getManagerBypage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return adminManagerRepository.findAll(pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

	@Override
	public Long managerCount(HttpServletRequest request) {
		return adminManagerRepository.countByIsDeletedFalseAndIsVerified(Status.APPROVED);
	}

	@Override
	public Long newManagerCount(HttpServletRequest request) {
		return adminManagerRepository.countByIsDeletedFalseAndIsVerified(Status.PENDING);
	}

}
