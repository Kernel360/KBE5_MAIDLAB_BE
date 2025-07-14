package kernel.maidlab.api.manager.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.service.JwtTokenService;
import kernel.maidlab.api.manager.repository.ManagerDocumentRepository;
import kernel.maidlab.api.manager.repository.ManagerRegionRepository;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.manager.repository.ManagerScheduleRepository;
import kernel.maidlab.api.manager.repository.ManagerServiceTypeRepository;
import kernel.maidlab.api.manager.repository.RegionRepository;
import kernel.maidlab.api.reservation.repository.ReviewRepository;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.manager.object.DocumentListItem;
import kernel.maidlab.common.dto.manager.object.RegionListItem;
import kernel.maidlab.common.dto.manager.object.ReviewListItem;
import kernel.maidlab.common.dto.manager.object.ScheduleListItem;
import kernel.maidlab.common.dto.manager.object.ServiceListItem;
import kernel.maidlab.common.dto.manager.request.ProfileRequestDto;
import kernel.maidlab.common.dto.manager.request.ProfileUpdateRequestDto;
import kernel.maidlab.common.dto.manager.response.MypageResponseDto;
import kernel.maidlab.common.dto.manager.response.ProfileResponseDto;
import kernel.maidlab.common.dto.manager.response.ReviewListResponseDto;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.entity.manager.ManagerDocument;
import kernel.maidlab.common.entity.manager.ManagerRegion;
import kernel.maidlab.common.entity.manager.ManagerSchedule;
import kernel.maidlab.common.entity.manager.ManagerServiceType;
import kernel.maidlab.common.entity.manager.Region;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.core.security.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {

	private final ManagerRepository managerRepository;
	private final ManagerServiceTypeRepository managerServiceTypeRepository;
	private final ManagerRegionRepository managerRegionRepository;
	private final ManagerScheduleRepository managerScheduleRepository;
	private final ManagerDocumentRepository managerDocumentRepository;
	private final RegionRepository regionRepository;
	private final ReviewRepository reviewRepository;
	private final JwtTokenService jwtTokenService;

	private Manager getCurrentManager() {
		String userUuid = AuthenticationHelper.getCurrentUserId();
		return managerRepository.findByUuid(userUuid)
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));
	}

	public Manager getManager(String userId) {
		return managerRepository.findByUuid(userId)
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));
	}

	// 최초 기본 프로필 생성
	@Override
	public ResponseEntity<ResponseDto<Void>> createProfile(ProfileRequestDto req, HttpServletRequest httpReq) {
		Manager manager = getCurrentManager();

		if (req.getProfileImage() != null) {
			manager.updateProfileImage(req.getProfileImage());
		}

		if (req.getIntroduceText() != null) {
			manager.updateIntroduceText(req.getIntroduceText());
		}

		if (req.getServiceTypes() != null && !req.getServiceTypes().isEmpty()) {
			for (ServiceListItem serviceItem : req.getServiceTypes()) {
				try {
					ServiceType serviceTypeEnum = ServiceType.valueOf(serviceItem.getServiceType());
					ManagerServiceType managerServiceType = ManagerServiceType.managerServiceType(manager,
						serviceTypeEnum);
					managerServiceTypeRepository.save(managerServiceType);
				} catch (IllegalArgumentException e) {
					log.warn("잘못된 서비스 타입 - 매니저 ID: {}, 서비스 타입: {}", manager.getId(), serviceItem.getServiceType());
					throw new BaseException(ResponseType.VALIDATION_FAILED);
				}
			}
			log.info("서비스 타입 등록 완료 - 매니저 ID: {}, 서비스 갯수: {}", manager.getId(), req.getServiceTypes().size());
		}

		if (req.getRegions() != null && !req.getRegions().isEmpty()) {
			for (RegionListItem regionItem : req.getRegions()) {
				Region region = regionRepository.findByRegionName(regionItem.getRegion())
					.orElseThrow(() -> new BaseException(ResponseType.VALIDATION_FAILED));

				ManagerRegion managerRegion = ManagerRegion.managerRegion(manager, region);
				managerRegionRepository.save(managerRegion);
			}
			log.info("지역 정보 등록 완료 - 매니저 ID: {}, 지역 갯수: {}", manager.getId(), req.getRegions().size());
		}

		if (req.getAvailableTimes() != null && !req.getAvailableTimes().isEmpty()) {
			for (ScheduleListItem scheduleItem : req.getAvailableTimes()) {
				ManagerSchedule schedule = ManagerSchedule.managerSchedule(
					manager,
					scheduleItem.getDay(),
					scheduleItem.getStartTime(),
					scheduleItem.getEndTime()
				);
				managerScheduleRepository.save(schedule);
			}
			log.info("스케줄 정보 등록 완료 - 매니저 ID: {}, 스케줄 갯수: {}", manager.getId(), req.getAvailableTimes().size());
		}

		if (req.getDocuments() != null && !req.getDocuments().isEmpty()) {
			for (DocumentListItem documentItem : req.getDocuments()) {
				ManagerDocument document = ManagerDocument.managerDocument(
					manager,
					documentItem.getFileType(),
					documentItem.getFileName(),
					documentItem.getUploadedFileUrl()
				);
				managerDocumentRepository.save(document);
			}
			log.info("문서 등록 완료 - 매니저 ID: {}, 문서 갯수: {}", manager.getId(), req.getDocuments().size());
		}

		managerRepository.save(manager);
		log.info("매니저 프로필 생성 완료 - 매니저 ID: {}", manager.getId());

		return ResponseDto.success();
	}

	// 마이페이지 조회
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req) {

		Manager manager = getCurrentManager();

		Boolean isVerified = Status.APPROVED.equals(manager.getIsVerified());

		MypageResponseDto responseDto = new MypageResponseDto(
			manager.getId(),
			UserType.MANAGER,
			manager.getProfileImage(),
			manager.getName(),
			isVerified,
			manager.getSocialType()
		);

		return ResponseDto.success(responseDto);
	}

	// 프로필 조회
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req) {
		Manager manager = getCurrentManager();

		List<ServiceType> services = managerServiceTypeRepository.findServiceTypesByManagerId(manager.getId());

		List<String> regionNames = managerRegionRepository.findRegionNamesByManagerId(manager.getId());
		List<RegionListItem> regions = regionNames.stream()
			.map(RegionListItem::new)
			.collect(Collectors.toList());

		List<ManagerSchedule> managerSchedules = managerScheduleRepository.findByManagerId(manager.getId());
		List<ScheduleListItem> schedules = managerSchedules.stream()
			.map(ms -> new ScheduleListItem(
				ms.getAvailableDay(),
				ms.getAvailableStartTime(),
				ms.getAvailableEndTime()
			))
			.collect(Collectors.toList());

		boolean isVerified = manager.getIsVerified() != null && manager.getIsVerified().name().equals("APPROVED");

		ProfileResponseDto responseDto = new ProfileResponseDto(
			manager.getId(),
			UserType.MANAGER,
			isVerified,
			manager.getProfileImage(),
			manager.getName(),
			manager.getBirth(),
			manager.getGender(),
			regions,
			schedules,
			services,
			manager.getIntroduceText()
		);

		return ResponseDto.success(responseDto);
	}

	// 프로필 수정
	@Override
	public ResponseEntity<ResponseDto<Void>> updateProfile(ProfileUpdateRequestDto req, HttpServletRequest httpReq) {
		Manager manager = getCurrentManager();

		manager.updateBasicInfo(req.getName(), req.getBirth(), req.getGender());

		if (req.getProfileImage() != null) {
			manager.updateProfileImage(req.getProfileImage());
		}

		if (req.getIntroduceText() != null) {
			manager.updateIntroduceText(req.getIntroduceText());
		}

		managerServiceTypeRepository.deleteByManagerId(manager.getId());
		managerRegionRepository.deleteByManagerId(manager.getId());
		managerScheduleRepository.deleteByManagerId(manager.getId());
		log.info("기존 매니저 서비스, 지역, 스케줄 정보 삭제 완료 - 매니저 ID: {}", manager.getId());

		if (req.getServiceTypes() != null && !req.getServiceTypes().isEmpty()) {
			for (ServiceListItem serviceItem : req.getServiceTypes()) {
				try {
					ServiceType serviceTypeEnum = ServiceType.valueOf(serviceItem.getServiceType());
					ManagerServiceType managerServiceType = ManagerServiceType.managerServiceType(manager,
						serviceTypeEnum);
					managerServiceTypeRepository.save(managerServiceType);
				} catch (IllegalArgumentException e) {
					log.warn("잘못된 서비스 타입 수정 시도 - 매니저 ID: {}, 서비스 타입: {}", manager.getId(),
						serviceItem.getServiceType());
					throw new BaseException(ResponseType.VALIDATION_FAILED);
				}
			}
			log.info("서비스 타입 수정 완료 - 매니저 ID: {}, 새 서비스 갯수: {}", manager.getId(), req.getServiceTypes().size());
		}

		if (req.getRegions() != null && !req.getRegions().isEmpty()) {
			for (RegionListItem regionItem : req.getRegions()) {
				Region region = regionRepository.findByRegionName(regionItem.getRegion())
					.orElseThrow(() -> new BaseException(ResponseType.VALIDATION_FAILED));

				ManagerRegion managerRegion = ManagerRegion.managerRegion(manager, region);
				managerRegionRepository.save(managerRegion);
			}
			log.info("지역 정보 수정 완료 - 매니저 ID: {}, 새 지역 갯수: {}", manager.getId(), req.getRegions().size());
		}

		if (req.getAvailableTimes() != null && !req.getAvailableTimes().isEmpty()) {
			for (ScheduleListItem scheduleItem : req.getAvailableTimes()) {
				ManagerSchedule schedule = ManagerSchedule.managerSchedule(
					manager,
					scheduleItem.getDay(),
					scheduleItem.getStartTime(),
					scheduleItem.getEndTime()
				);
				managerScheduleRepository.save(schedule);
			}
			log.info("스케줄 정보 수정 완료 - 매니저 ID: {}, 새 스케줄 갯수: {}", manager.getId(), req.getAvailableTimes().size());
		}

		managerRepository.save(manager);
		log.info("매니저 프로필 수정 완료 - 매니저 ID: {}", manager.getId());

		return ResponseDto.success();
	}

	// 리뷰 목록 조회
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req) {
		Manager manager = getCurrentManager();

		List<Object[]> reviewData = reviewRepository.findManagerReviewDetails(manager.getId());

		List<ReviewListItem> reviewItems = reviewData.stream()
			.map(data -> new ReviewListItem(
				String.valueOf(data[0]), // reviewId
				BigDecimal.valueOf(((Number)data[1]).doubleValue()), // rating
				(String)data[2], // consumerName
				(String)data[3], // comment
				(String)data[4], // serviceType
				(String)data[5]  // serviceDetailType
			))
			.collect(Collectors.toList());

		ReviewListResponseDto responseDto = new ReviewListResponseDto(
			reviewItems
		);

		return ResponseDto.success(responseDto);
	}

	@Override
	public List<AvailableManagerResponseDto> findAvailableManagers(String gu, LocalDateTime StartTime,
		LocalDateTime EndTime) {
		return managerRepository.findAvailableManagers(gu, StartTime, EndTime);
	}

	@Override
	public List<AvailableManagerResponseDto> previousManagers(Consumer consumer) {
		return managerRepository.previousManagers(consumer);
	}

	@Override
	public Manager findById(Long managerId) {
		return managerRepository.findById(managerId).orElse(null);
	}

}
