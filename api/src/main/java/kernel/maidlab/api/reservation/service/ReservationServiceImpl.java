package kernel.maidlab.api.reservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kernel.maidlab.api.reservation.repository.*;
import kernel.maidlab.common.entity.reservation.*;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.jwt.JwtFilter;
import kernel.maidlab.common.entity.base.UserBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.entity.consumer.ManagerPreference;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepository;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.common.dto.matching.response.MatchingResponseDto;
import kernel.maidlab.common.entity.matching.Matching;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.WeeklySettlementResponseDto;
import kernel.maidlab.api.util.AuthUtil;
import kernel.maidlab.common.enums.ServiceOptionType;
import kernel.maidlab.common.exception.custom.ReservationException;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.api.matching.service.MatchingService;
import kernel.maidlab.common.dto.reservation.request.PaymentRequestDto;
import kernel.maidlab.common.dto.reservation.request.CheckInOutRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReviewRegisterRequestDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.util.RoomSizeRuleUtil;
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
	private final AuthUtil authUtil;
	private final MatchingService matchingService;
	private final ManagerPreferenceRepository managerPreferenceRepository;
	private final ConsumerRepository consumerRepository;
	private final ReviewRepository reviewRepository;
	private final SettlementRepository settlementRepository;
	private final ReviewKeywordRepository reviewKeywordRepository;

	@Transactional
	@Override
	public void registerReview(ReviewRegisterRequestDto dto, HttpServletRequest request) {
		UserType userType = (UserType)request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);

		Boolean isConsumerToManager = userType == UserType.CONSUMER;

		Reservation reservation = reservationRepository.findById(dto.getReservationId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		if (userType == UserType.CONSUMER) {
			Consumer consumer = (Consumer)request.getAttribute(JwtFilter.CURRENT_USER_KEY);
			Manager manager = managerRepository.findById(reservation.getManagerId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
			// 매니저 선호도 테이블 관리
			if (dto.getLikes() != null) {
				managerPreferenceRepository.save(new ManagerPreference(consumer, manager, dto.getLikes()));
				log.info("매니저 선호도 등록 - Consumer ID: {}, Manager ID: {}, 선호도: {}", consumer.getId(), manager.getId(), dto.getLikes());
			}

			// 매니저 평균 평점(average_rate) 관리
			Long managerTotalReviewedCnt = manager.getTotalReviewedCnt();
			Float averageRate = manager.getAverageRate();
			if (managerTotalReviewedCnt == 0) {
				manager.updateAverageRate(dto.getRating());
			} else {
				Float newAverageRate = (managerTotalReviewedCnt * averageRate + dto.getRating()) / (managerTotalReviewedCnt + 1);
				manager.updateAverageRate(newAverageRate);
			}
			managerRepository.save(manager);
			log.info("매니저 평균 평점 업데이트 - Manager ID: {}, 이전 평점: {}, 새 평점: {}", manager.getId(), averageRate, manager.getAverageRate());

		} else if (userType == UserType.MANAGER) {
			Consumer consumer = consumerRepository.findById(reservation.getConsumerId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

			// 고객 평균 평점(average_rate) 관리
			Long consumerTotalReviewedCnt = consumer.getTotalReviewedCnt();
			Float averageRate = consumer.getAverageRate();
			if (consumerTotalReviewedCnt == 0) {
				consumer.updateAverageRate(dto.getRating());
			} else {
				Float newAverageRate = (consumerTotalReviewedCnt * averageRate + dto.getRating()) / (consumerTotalReviewedCnt + 1);
				consumer.updateAverageRate(newAverageRate);
			}
			consumerRepository.save(consumer);
			log.info("Consumer 평균 평점 업데이트 - Consumer ID: {}, 이전 평점: {}, 새 평점: {}", consumer.getId(), averageRate, consumer.getAverageRate());
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

	@Override
	public List<ReservationResponseDto> allReservations(HttpServletRequest request) {
		UserType userType = authUtil.getUserType(request);

		if (userType == UserType.CONSUMER) {
			Long consumerId = authUtil.getConsumer(request).getId();
			return reservationRepository.findAllWithReviewByConsumerId(consumerId);
		} else {
			Long managerId = authUtil.getManager(request).getId();
			return reservationRepository.findAllWithReviewByManagerId(managerId);

		}
	}

	@Override
	public ReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request) {

		UserBase user = (UserBase)request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		UserType userType = (UserType)request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
		Long userId = switch (userType) {
			case CONSUMER -> ((Consumer)user).getId();
			case MANAGER -> ((Manager)user).getId();
			default -> throw new ReservationException(ResponseType.THIS_USER_DOES_NOT_EXIST);
		};

		return reservationRepository.findDetailReservationByIdAndUser(reservationId, userId, userType);
	}

	@Transactional
	@Override
	public Long createReservation(ReservationRequestDto dto, HttpServletRequest request) {
		// 매칭된 매니저 존재 확인
		if (dto.getManagerUuId().isEmpty() || dto.getManagerUuId().isBlank()){
			throw new ReservationException(ResponseType.AVAILABLE_MANAGER_DOES_NOT_EXIST);
		}

		Consumer consumer = (Consumer)request.getAttribute(JwtFilter.CURRENT_USER_KEY);
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
		Manager manager = managerRepository.findByUuid(dto.getManagerUuId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		Long managerId = manager.getId();

		Reservation reservation = Reservation.of(dto, consumerId, managerId, detailType);
		Reservation matchingReservation = reservationRepository.save(reservation);
		log.info("예약 생성 완료 - 예약 ID: {}, Consumer ID: {}, Manager ID: {}", matchingReservation.getId(), consumerId, managerId);

		// 예약 완료 시 manager 매칭
		Matching match = Matching.of(MatchingResponseDto.builder()
			.reservationId(matchingReservation.getId())
			.managerId(matchingReservation.getManagerId())
			.matchingStatus(Status.PENDING)
			.build());
		Matching savedMatching = matchingRepository.save(match);
		log.info("매칭 생성 완료 - 매칭 ID: {}, 예약 ID: {}", savedMatching.getId(), matchingReservation.getId());

		return reservation.getId();

	}

	@Transactional
	@Override
	public void managerResponseToReservation(Long reservationId, ReservationIsApprovedRequestDto dto,
		HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Long managerId = authUtil.getManager(request).getId();
		if (!reservation.getStatus().equals(Status.PENDING)) {
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}
		boolean isApproved = dto.getStatus(); // approved : true, rejected : false
		if (isApproved) {
			reservation.managerRespondApproved(managerId);

			matchingRepository.deleteById(matchingRepository.findByReservationId(reservationId).getId());
			// TODO : 수요자에게 알림 보내기 (예약 성공)
		} else {
			reservation.managerRespondRejected(managerId);
			matchingService.changeStatus(reservationId, Status.REJECTED);
		}
		reservationRepository.save(reservation);
	}
	@Transactional
	@Override
	public void pay(PaymentRequestDto dto, HttpServletRequest request){
		Reservation reservation = reservationRepository.findById(dto.getReservationId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		reservation.pay();
		reservationRepository.save(reservation);
	}

	@Transactional
	@Override
	public void checkin(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Long managerId = authUtil.getManager(request).getId();

		if (!reservation.getManagerId().equals(managerId)) {
			throw new ReservationException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
		// 이미 체크인 되어 있는 경우
		if (reservation.getCheckinTime() != null) {
			throw new ReservationException(ResponseType.ALREADY_CHECKED_IN);
		}

		reservation.checkin(dto.getCheckTime());
		reservationRepository.save(reservation);
	}

	@Transactional
	@Override
	public void checkout(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Long managerId = authUtil.getManager(request).getId();

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
	}

	@Transactional
	@Override
	public void cancel(Long reservationId, HttpServletRequest request) {

		Long consumerId = authUtil.getConsumer(request).getId();
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
		if (matchingRepository.existsById(matchingRepository.findByReservationId(reservationId).getId())) {
			matchingRepository.deleteById(matchingRepository.findByReservationId(reservationId).getId());
		}
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
		Long managerId = authUtil.getManager(request).getId();
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = start.plusDays(7).with(LocalTime.MIN);

		List<Settlement> settlements = settlementRepository.findByManagerIdAndCreatedAtBetween(managerId, start, end);

		BigDecimal totalAmount = BigDecimal.ZERO;
		List<SettlementResponseDto> responseList = new ArrayList<>();

		for (Settlement settlement : settlements) {
			ServiceDetailType detailType = serviceDetailTypeRepository.findById(settlement.getServiceDetailTypeId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

			totalAmount = totalAmount.add(settlement.getAmount());

			responseList.add(new SettlementResponseDto(settlement.getId(), settlement.getReservationId(), settlement.getServiceType(),
				detailType.getServiceDetailType(), settlement.getStatus(), settlement.getPlatformFee(),
				settlement.getAmount()));

		}
		return new WeeklySettlementResponseDto(totalAmount, responseList);
	}

	// @Override
	// public Reservation findById(Long reservationId) {
	// 	return reservationRepository.findById(reservationId)
	// 		.orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + reservationId));
	// }
}


