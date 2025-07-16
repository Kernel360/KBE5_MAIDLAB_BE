package kernel.maidlab.domain.reservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.domain.manager.enums.ServiceOptionType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.domain.util.RoomSizeRuleUtil;
import kernel.maidlab.core.exception.custom.PointException;
import kernel.maidlab.core.exception.custom.ReservationException;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.consumer.entity.ManagerPreference;
import kernel.maidlab.domain.consumer.repository.ConsumerRepository;
import kernel.maidlab.domain.consumer.repository.ManagerPreferenceRepository;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.manager.repository.ManagerRepository;
import kernel.maidlab.domain.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.domain.matching.repository.MatchingRepository;
import kernel.maidlab.domain.matching.service.MatchingService;
import kernel.maidlab.domain.notification.dto.NotificationDto;
import kernel.maidlab.domain.notification.service.NotificationService;
import kernel.maidlab.domain.point.entity.Point;
import kernel.maidlab.domain.point.repository.PointRepository;
import kernel.maidlab.domain.reservation.dto.request.CheckInOutRequestDto;
import kernel.maidlab.domain.reservation.dto.request.PaymentRequestDto;
import kernel.maidlab.domain.reservation.dto.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.domain.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.domain.reservation.dto.request.ReviewRegisterRequestDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.domain.reservation.dto.response.SettlementResponseDto;
import kernel.maidlab.domain.reservation.dto.response.WeeklySettlementResponseDto;
import kernel.maidlab.domain.reservation.entity.Reservation;
import kernel.maidlab.domain.reservation.entity.Review;
import kernel.maidlab.domain.reservation.entity.ReviewKeyword;
import kernel.maidlab.domain.reservation.entity.ServiceDetailType;
import kernel.maidlab.domain.reservation.entity.Settlement;
import kernel.maidlab.domain.reservation.repository.ReservationRepository;
import kernel.maidlab.domain.reservation.repository.ReviewKeywordRepository;
import kernel.maidlab.domain.reservation.repository.ReviewRepository;
import kernel.maidlab.domain.reservation.repository.ServiceDetailTypeRepository;
import kernel.maidlab.domain.reservation.repository.SettlementRepository;
import kernel.maidlab.domain.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final ServiceDetailTypeRepository serviceDetailTypeRepository;
	private final MatchingRepository matchingRepository;
	private final ManagerRepository managerRepository;
	private final MatchingService matchingService;
	private final ManagerPreferenceRepository managerPreferenceRepository;
	private final ConsumerRepository consumerRepository;
	private final ReviewRepository reviewRepository;
	private final SettlementRepository settlementRepository;
	private final ReviewKeywordRepository reviewKeywordRepository;
	private final UserValidator userValidator;
	private final PointRepository pointRepository;
	private final NotificationService notificationService;

	@Transactional
	@Override
	public void registerReview(ReviewRegisterRequestDto dto, HttpServletRequest request) {
		UserType userType = AuthenticationHelper.getCurrentUserType();
		String userId = AuthenticationHelper.getCurrentUserKey();

		Boolean isConsumerToManager = userType == UserType.CONSUMER;

		Reservation reservation = reservationRepository.findById(dto.getReservationId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		if (userType == UserType.CONSUMER) {
			Consumer consumer = (Consumer)userValidator.findByUuid(userId, userType);
			Manager manager = managerRepository.findById(reservation.getManagerId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
			// 매니저 선호도 테이블 관리
			if (dto.getLikes() != null) {
				managerPreferenceRepository.save(new ManagerPreference(consumer, manager, dto.getLikes()));
				log.info("매니저 선호도 등록 - Consumer ID: {}, Manager ID: {}, 선호도: {}", consumer.getId(), manager.getId(),
					dto.getLikes());
			}

			// 매니저 평균 평점(average_rate) 관리
			Long managerTotalReviewedCnt = manager.getTotalReviewedCnt();
			Float averageRate = manager.getAverageRate();
			if (managerTotalReviewedCnt == 0) {
				manager.updateAverageRate(dto.getRating());
			} else {
				Float newAverageRate =
					(managerTotalReviewedCnt * averageRate + dto.getRating()) / (managerTotalReviewedCnt + 1);
				manager.updateAverageRate(newAverageRate);
			}
			managerRepository.save(manager);
			log.info("매니저 평균 평점 업데이트 - Manager ID: {}, 이전 평점: {}, 새 평점: {}", manager.getId(), averageRate,
				manager.getAverageRate());

		} else if (userType == UserType.MANAGER) {
			Consumer consumer = consumerRepository.findById(reservation.getConsumerId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

			// 고객 평균 평점(average_rate) 관리
			Long consumerTotalReviewedCnt = consumer.getTotalReviewedCnt();
			Float averageRate = consumer.getAverageRate();
			if (consumerTotalReviewedCnt == 0) {
				consumer.updateAverageRate(dto.getRating());
			} else {
				Float newAverageRate =
					(consumerTotalReviewedCnt * averageRate + dto.getRating()) / (consumerTotalReviewedCnt + 1);
				consumer.updateAverageRate(newAverageRate);
			}
			consumerRepository.save(consumer);
			log.info("Consumer 평균 평점 업데이트 - Consumer ID: {}, 이전 평점: {}, 새 평점: {}", consumer.getId(), averageRate,
				consumer.getAverageRate());
		} else {
			throw new ReservationException(ResponseType.INVALID_USER_TYPE);
		}

		// 리뷰 등록
		Review review = Review.of(dto, reservation, isConsumerToManager);
		Review savedReview = reviewRepository.save(review);

		// 키워드가 있으면 저장
		if (dto.getKeywords() != null && !dto.getKeywords().isEmpty()) {
			for (String keyword : dto.getKeywords()) {
				ReviewKeyword reviewKeyword = new ReviewKeyword(review, keyword);
				reviewKeywordRepository.save(reviewKeyword);
			}
		}
		log.info("리뷰 등록 완료 - 리뷰 ID: {}, 예약 ID: {}", savedReview.getId(), reservation.getId());
	}

	// 이전 예약 전체 조회 api
	@Override
	public List<ReservationResponseDto> allReservations(HttpServletRequest request) {
		UserType userType = AuthenticationHelper.getCurrentUserType();

		if (userType == UserType.CONSUMER) {
			Long consumerId = getCurrentConsumer().getId();
			return reservationRepository.findAllWithReviewByConsumerId(consumerId);
		} else {
			Long managerId = getCurrentManager().getId();
			return reservationRepository.findAllWithReviewByManagerId(managerId);

		}
	}

	// 고객 맞춤 예약 내역 페이징 및 상태별 필터링
	@Override
	public Page<ReservationResponseDto> getConsumerReservationsWithPaging(String status, int page, int size,
		String sortBy, String sortOrder, HttpServletRequest request) {
		Consumer consumer = getCurrentConsumer();
		Long consumerId = consumer.getId();

		if (size > 50) {
			size = 50;
		}

		Set<String> allowedSortFields = new HashSet<>(
			Arrays.asList("createdAt", "reservationDate", "totalPrice", "completedAt", "startTime"));
		if (!allowedSortFields.contains(sortBy)) {
			sortBy = "createdAt";
		}

		Status statusEnum = null;
		if (status != null && !status.trim().isEmpty()) {
			try {
				statusEnum = Status.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ReservationException(ResponseType.VALIDATION_FAILED);
			}
		}

		Sort.Direction direction = "ASC".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(direction, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		return reservationRepository.findConsumerReservationsWithPaging(consumerId, statusEnum, pageable);
	}

	@Override
	public Page<ReservationResponseDto> getManagerReservationsWithPaging(String status, int page, int size,
		String sortOrder, HttpServletRequest request) {
		Manager manager = getCurrentManager();
		Long managerId = manager.getId();

		if (size > 50) {
			size = 50;
		}

		Sort.Direction direction = "ASC".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(direction, "reservationDate");
		Pageable pageable = PageRequest.of(page, size, sort);

		return reservationRepository.getManagerReservationsWithPaging(managerId, status, pageable);
	}

	@Override
	public ReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request) {
		Long userId = getCurrentUserEntityId();
		UserType userType = AuthenticationHelper.getCurrentUserType();

		return reservationRepository.findDetailReservationByIdAndUser(reservationId, userId, userType);
	}

	@Transactional
	@Override
	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.OptimisticLockingFailureException.class}
	)
	@ExceptionHandler(
		value = {Exception.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "예약 생성 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR,
		enableNotification = true
	)
	public Long createReservation(ReservationRequestDto dto, HttpServletRequest request) {
		// 매칭된 매니저 존재 확인
		if (dto.getManagerUuid().isEmpty() || dto.getManagerUuid().isBlank()) {
			throw new ReservationException(ResponseType.AVAILABLE_MANAGER_DOES_NOT_EXIST);
		}

		String consumerUuid = AuthenticationHelper.getCurrentUserKey();
		Consumer consumer = (Consumer)userValidator.findByUuid(consumerUuid, UserType.CONSUMER);
		Long consumerId = consumer.getId();

		// 결제 검증 로직(애플리케이션 상용 전 true 고정)
		boolean payValid = true;
		if (!payValid) {
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}

		// 금액 재검증
		checkTotalPrice(dto);

		// 예약 저장
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new ReservationException(ResponseType.VALIDATION_FAILED));

		// managerUuid → managerId 변환
		Manager manager = managerRepository.findByUuid(dto.getManagerUuid())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		Long managerId = manager.getId();

		Reservation reservation = Reservation.of(dto, consumerId, managerId, detailType);
		Reservation matchingReservation = reservationRepository.save(reservation);
		log.info("예약 생성 완료 - 예약 ID: {}, Consumer ID: {}, Manager ID: {}", matchingReservation.getId(), consumerId,
			managerId);

		// 예약 완료 시 manager 매칭
		MatchingResponseDto match = new MatchingResponseDto(
			matchingReservation.getManagerId(),
			matchingReservation.getId(),
			Status.PENDING
		);
		matchingService.createMatching(match);
		return reservation.getId();
	}

	@Transactional
	@Override
	public void managerResponseToReservation(Long reservationId, ReservationIsApprovedRequestDto dto,
		HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Long managerId = getCurrentManager().getId();
		if (!reservation.getStatus().equals(Status.PENDING)) {
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}
		boolean isApproved = dto.getStatus(); // approved : true, rejected : false
		if (isApproved) {
			reservation.managerRespondApproved(managerId);
			matchingService.changeStatus(reservationId, Status.APPROVED);
			matchingRepository.deleteById(matchingRepository.findByReservationId(reservationId).getId());
		} else {
			reservation.managerRespondRejected(managerId);
			matchingService.changeStatus(reservationId, Status.REJECTED);
		}
		reservationRepository.save(reservation);
	}

	@Transactional
	@Override
	@Retry(
		maxAttempts = 2,
		delay = 500,
		retryFor = {org.springframework.dao.DataIntegrityViolationException.class}
	)
	@ExceptionHandler(
		value = {Exception.class},
		responseType = ResponseType.PAYMENT_FAILED,
		message = "결제 처리 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR,
		enableNotification = true
	)
	public void pay(PaymentRequestDto dto, HttpServletRequest request) {

		String userId = AuthenticationHelper.getCurrentUserKey();
		Consumer consumer = userValidator.findByUuid(userId, UserType.CONSUMER);

		Reservation reservation = reservationRepository.findById(dto.getReservationId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		// 결제시 포인트 사용
		if (dto.isPointUsed()) {

			// 사용 가능한 포인트 검증
			Long useAblePoint = pointRepository.getTotalPointsByConsumerId(consumer.getId());
			if (useAblePoint < dto.getPointToUse()) {
				throw new PointException(ResponseType.INSUFFICIENT_POINT);
			}

			BigDecimal finalTotalPrice = usePoints(dto.getPointToUse(), reservation);
			reservation.applyFinalPaymentPrice(finalTotalPrice);

			// 포인트 차감
			Point usagePoint = Point.createUsagePoint(consumer, reservation, dto.getPointToUse());
			pointRepository.save(usagePoint);
		}

		reservation.pay();
		reservationRepository.save(reservation);

		// 포인트 적립
		Point point = Point.createEarnPointOnPayment(consumer, reservation, reservation.getTotalPrice());
		pointRepository.save(point);

		sendReservationPaidNotification(reservation.getManagerId(), reservation.getId(), consumer.getName());

	}

	private void sendReservationPaidNotification(Long managerId, Long reservationId, String consumerName) {
		NotificationDto notification = notificationService.createReservationPaidNotification(managerId, reservationId,
			consumerName);
		notificationService.sendNotification(notification);
	}

	@Transactional
	@Override
	public void checkin(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Manager manager = getCurrentManager();
		Long managerId = manager.getId();

		if (!reservation.getManagerId().equals(managerId)) {
			throw new ReservationException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
		// 이미 체크인 되어 있는 경우
		if (reservation.getCheckinTime() != null) {
			throw new ReservationException(ResponseType.ALREADY_CHECKED_IN);
		}

		sendReservationCheckInNotification(reservation.getConsumerId(), reservationId, manager.getName());

		reservation.checkin(dto.getCheckTime());
		reservationRepository.save(reservation);
	}

	private void sendReservationCheckInNotification(Long consumerId, Long reservationId, String name) {
		NotificationDto notification = notificationService.createReservationCheckInNotification(consumerId,
			reservationId, name);
		notificationService.sendNotification(notification);
	}

	@Transactional
	@Override
	public void checkout(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Manager manager = getCurrentManager();
		Long managerId = manager.getId();

		if (!reservation.getManagerId().equals(managerId)) {
			throw new ReservationException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
		if (reservation.getCheckinTime() == null) {
			throw new ReservationException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
		if (reservation.getCheckoutTime() != null) {
			throw new ReservationException(ResponseType.ALREADY_CHECKED_OUT);
		}
		reservation.checkout(dto.getCheckTime());
		reservationRepository.save(reservation);

		// 정산 테이블 생성
		settlementRepository.save(Settlement.of(reservation));

		sendReservationCheckOutNotification(reservation.getConsumerId(), reservationId, manager.getName());
	}

	private void sendReservationCheckOutNotification(Long consumerId, Long reservationId, String managerName) {
		NotificationDto notification = notificationService.createReservationCheckOutNotification(consumerId,
			reservationId, managerName);
		notificationService.sendNotification(notification);
	}

	@Transactional
	@Override
	public void cancel(Long reservationId, HttpServletRequest request) {

		Consumer consumer = getCurrentConsumer();
		Long consumerId = consumer.getId();
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		if (!reservation.getConsumerId().equals(consumerId)) {
			throw new ReservationException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}

		if (reservation.getStatus() != Status.PENDING && reservation.getStatus() != Status.MATCHED) {
			throw new ReservationException(ResponseType.ALREADY_WORKING_OR_COMPLETED);
		}

		reservation.cancel(LocalDateTime.now());
		reservationRepository.save(reservation);
		if (matchingRepository.existsByReservationId(reservationId)) {
			matchingRepository.deleteById(matchingRepository.findByReservationId(reservationId).getId());
		}

		sendReservationCanceledNotification(reservation.getManagerId(), reservationId, consumer.getName());

	}

	private void sendReservationCanceledNotification(Long managerId, Long reservationId, String consumerName) {
		NotificationDto notification = notificationService.createReservationCancelNotification(managerId, reservationId,
			consumerName);
		notificationService.sendNotification(notification);
	}

	@Override
	public void checkTotalPrice(ReservationRequestDto dto) {
		BigDecimal serverCalculatedPrice = calculateTotalPrice(dto);
		if (serverCalculatedPrice.compareTo(dto.getTotalPrice()) != 0) {
			log.warn("금액 불일치 - client={}, server={}", dto.getTotalPrice(), serverCalculatedPrice);
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}
	}

	private static final Map<String, BigDecimal> ADDITIONAL_PRICE_MAP = Map.of("cooking", BigDecimal.valueOf(10_000),
		"ironing", BigDecimal.valueOf(10_000)
		// 나중에 "laundry", "cleaning" 등 추가 가능
	);

	private BigDecimal calculateTotalPrice(ReservationRequestDto dto) {
		BigDecimal basePrice = RoomSizeRuleUtil.resolveBasePrice(dto.getLifeCleaningRoomIdx());

		BigDecimal optionPrice = dto.getServiceOptions()
			.stream()
			.filter(opt -> ServiceOptionType.isValid(opt.getId()))
			.map(opt -> {
				ServiceOptionType type = ServiceOptionType.from(opt.getId());
				return type.getPriceForCount(opt.getCount());
			})
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		return basePrice.add(optionPrice);
	}

	@Override
	public WeeklySettlementResponseDto getWeeklySettlements(HttpServletRequest request, LocalDate startDate) {
		Long managerId = getCurrentManager().getId();
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = start.plusDays(7).with(LocalTime.MIN);

		List<Settlement> settlements = settlementRepository.findByManagerIdAndCreatedAtBetween(managerId, start, end);

		BigDecimal totalAmount = BigDecimal.ZERO;
		List<SettlementResponseDto> responseList = new ArrayList<>();

		for (Settlement settlement : settlements) {
			ServiceDetailType detailType = serviceDetailTypeRepository.findById(settlement.getServiceDetailTypeId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

			totalAmount = totalAmount.add(settlement.getAmount());

			responseList.add(new SettlementResponseDto(settlement.getId(), settlement.getReservationId(),
				settlement.getServiceType(),
				detailType.getServiceDetailType(), settlement.getStatus(), settlement.getPlatformFee(),
				settlement.getAmount()));

		}
		return new WeeklySettlementResponseDto(totalAmount, responseList);
	}

	public BigDecimal usePoints(Integer pointToUse, Reservation reservation) {

		if (pointToUse < 0) {
			throw new PointException(ResponseType.VALIDATION_FAILED);
		}
		BigDecimal pointValue = BigDecimal.valueOf(pointToUse);
		BigDecimal newPrice = reservation.getTotalPrice().subtract(pointValue);
		return newPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newPrice;
	}

	// @Override
	// public Reservation findById(Long reservationId) {
	// 	return reservationRepository.findById(reservationId)
	// 		.orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + reservationId));
	// }

	private Long getCurrentUserEntityId() {
		String userUuid = AuthenticationHelper.getCurrentUserKey();
		UserType userType = AuthenticationHelper.getCurrentUserType();

		Object user = userValidator.findByUuid(userUuid, userType);
		return userValidator.getUserId(user);
	}

	private Consumer getCurrentConsumer() {
		String userUuid = AuthenticationHelper.getCurrentUserKey();
		return (Consumer)userValidator.findByUuid(userUuid, UserType.CONSUMER);
	}

	private Manager getCurrentManager() {
		String userUuid = AuthenticationHelper.getCurrentUserKey();
		return (Manager)userValidator.findByUuid(userUuid, UserType.MANAGER);
	}
}


