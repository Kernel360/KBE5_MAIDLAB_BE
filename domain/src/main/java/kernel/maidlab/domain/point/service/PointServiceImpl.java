package kernel.maidlab.domain.point.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.point.dto.request.PointChargeRequestDto;
import kernel.maidlab.domain.point.dto.request.PointRecordRequestDto;
import kernel.maidlab.domain.point.dto.response.PageResponseDto;
import kernel.maidlab.domain.point.dto.response.PointRecordResponseDto;
import kernel.maidlab.domain.point.dto.response.PointResponseDto;
import kernel.maidlab.domain.point.entity.Point;
import kernel.maidlab.domain.point.repository.PointRepository;
import kernel.maidlab.domain.util.UserValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointRepository pointRepository;
	private final UserValidator userValidator;

	@Override
	public PointResponseDto getPoint(HttpServletRequest request) {

		String userId = AuthenticationHelper.getCurrentUserKey();

		Consumer consumer = (Consumer)userValidator.findByUuid(userId, UserType.CONSUMER);
		Long totalPointsByConsumerId = pointRepository.getTotalPointsByConsumerId(consumer.getId());
		return PointResponseDto.from(totalPointsByConsumerId);
	}

	@Override
	public PageResponseDto<PointRecordResponseDto> getPointRecordList(
		HttpServletRequest request,
		PointRecordRequestDto requestDto) {
		Integer page = requestDto.getPageable().getPage();
		Integer size = requestDto.getPageable().getSize();
		List<PointRecordRequestDto.PageableRequest.SortRequest> sort = requestDto.getPageable().getSort();

		Pageable pageable = PageRequest.of(
			page,
			size,
			Sort.by(
				sort.stream()
					.map(sortRequest ->
						new Sort.Order(
							sortRequest.getDirection(),
							sortRequest.getProperty()
						)
					)
					.toList()
			)
		);

		String userId = AuthenticationHelper.getCurrentUserKey();

		Consumer consumer = (Consumer)userValidator.findByUuid(userId, UserType.CONSUMER);

		LocalDate startOfMonth = LocalDate.now().plusMonths(requestDto.getMonthOffset()).withDayOfMonth(1);
		LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

		Page<PointRecordResponseDto> pointRecordsPage = pointRepository.findPointRecords(
			consumer.getId(),
			requestDto.getMonthOffset(),
			requestDto.getPointType(),
			pageable
		);

		return PageResponseDto.<PointRecordResponseDto>builder()
			.content(pointRecordsPage.getContent())
			.hasNext(pointRecordsPage.hasNext())
			.build();
	}

	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.DataIntegrityViolationException.class}
	)
	@ExceptionHandler(
		value = {Exception.class},
		responseType = ResponseType.PAYMENT_FAILED,
		message = "포인트 충전 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR,
		enableNotification = true
	)
	public void chargePoint(
		HttpServletRequest request,
		PointChargeRequestDto pointChargeRequestDto
	) {
		String userId = AuthenticationHelper.getCurrentUserKey();

		Consumer consumer = userValidator.findByUuid(userId, UserType.CONSUMER);

		if (pointChargeRequestDto.getChargeAmount() <= 0) {
			throw new IllegalArgumentException("충전 금액이 유효하지 않습니다: " + pointChargeRequestDto.getChargeAmount());
		}

		Point chargedPoint = Point.createChargePoint(
			consumer,
			pointChargeRequestDto.getChargeAmount());

		pointRepository.save(chargedPoint);
	}

}
