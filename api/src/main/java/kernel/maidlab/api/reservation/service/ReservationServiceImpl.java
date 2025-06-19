package kernel.maidlab.api.reservation.service;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import kernel.maidlab.common.dto.reservation.response.AdminSettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.WeeklySettlementResponseDto;
import kernel.maidlab.common.entity.reservation.Settlement;
import kernel.maidlab.api.reservation.repository.ReviewRepository;
import kernel.maidlab.api.reservation.repository.SettlementRepository;
import kernel.maidlab.api.util.AuthUtil;
import kernel.maidlab.common.exception.custom.ReservationException;
import kernel.maidlab.common.entity.manager.ManagerRegion;
import kernel.maidlab.api.manager.repository.ManagerRegionRepository;
import kernel.maidlab.api.manager.repository.RegionRepository;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.api.matching.service.MatchingService;
import kernel.maidlab.common.dto.reservation.request.CheckInOutRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReviewRegisterRequestDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.entity.reservation.Reservation;
import kernel.maidlab.common.entity.reservation.Review;
import kernel.maidlab.common.entity.reservation.ServiceDetailType;
import kernel.maidlab.api.reservation.repository.ReservationRepository;
import kernel.maidlab.api.reservation.repository.ServiceDetailTypeRepository;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
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
	private final ManagerRegionRepository managerRegionRepository;
	private final RegionRepository regionRepository;
	private final ConsumerRepository consumerRepository;
	private final ReviewRepository reviewRepository;
	private final SettlementRepository settlementRepository;

	@Transactional
	@Override
	public void registerReview(Long reservationId, ReviewRegisterRequestDto dto, HttpServletRequest request) {
		UserType userType = authUtil.getUserType(request);
		Boolean isConsumerToManager = userType == UserType.CONSUMER;

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		Consumer consumer = consumerRepository.findById(reservation.getConsumerId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		Manager manager = managerRepository.findById(reservation.getManagerId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		if (userType == UserType.CONSUMER) {
			// 매니저 선호도 테이블 관리
			if (dto.getLikes() != null) {
				managerPreferenceRepository.save(new ManagerPreference(consumer, manager, dto.getLikes()));
			}

			// 매니저 평균 평점(average_rate) 관리
			Long totalReviewedCnt = manager.getTotalReviewedCnt();
			Float averageRate = manager.getAverageRate();
			if (totalReviewedCnt == 0) {
				manager.updateAverageRate(dto.getRating());
			} else {
				Float newAverageRate = (totalReviewedCnt * averageRate + dto.getRating()) / (totalReviewedCnt + 1);
				manager.updateAverageRate(newAverageRate);
			}
			managerRepository.save(manager);
		}

		// 리뷰 등록
		Review review = Review.of(dto, reservation, isConsumerToManager);
		reviewRepository.save(review);
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
		UserType userType = (UserType) request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
		UserBase user = (UserBase) request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		Long userId = getUserId(request, userType);

		return reservationRepository.findDetailReservationByIdAndUser(reservationId, userId, userType);
	}




	@Transactional
	@Override
	public void createReservation(ReservationRequestDto dto, HttpServletRequest request) {
		Long consumerId = authUtil.getConsumer(request).getId();

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

		// 예약 완료 시 manager 매칭
		Matching match = Matching.of( MatchingResponseDto.builder()
			.reservationId(matchingReservation.getId())
			.managerId(matchingReservation.getManagerId())
			.matchingStatus(Status.PENDING)
			.build() );
		matchingRepository.save(match);

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
			reservation.managerRespond(managerId);
			reservationRepository.save(reservation);
			matchingRepository.deleteById(matchingRepository.findByReservationId(reservationId).getId());
			// TODO : 수요자에게 알림 보내기 (예약 성공)
		} else {
			matchingService.changeStatus(reservationId, Status.REJECTED);
		}
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
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		BigDecimal serverCalculatedPrice = calculateTotalPrice(dto, detailType.getServicePrice());
		if (!serverCalculatedPrice.equals(dto.getTotalPrice())) {
			log.warn("금액 불일치 - client={}, server={}", dto.getTotalPrice(), serverCalculatedPrice);
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}
	}

	@Override
	public List<ReservationResponseDto> dailyReservations(LocalDate date, int page, int size) {
		Page<Reservation> reservations;
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		Pageable pageable = PageRequest.of(page, size);
		reservations = reservationRepository.findAllByReservationDateBetween(start, end, pageable);

		return reservations.stream()
			.map(reservation -> ReservationResponseDto.builder()
				.reservationId(reservation.getId())
				.serviceType(reservation.getServiceDetailType().getServiceType().toString())
				.detailServiceType(reservation.getServiceDetailType().getServiceDetailType())
				.reservationDate(reservation.getReservationDate().toLocalDate().toString())
				.startTime(reservation.getStartTime().toLocalTime().toString().substring(0, 5))
				.endTime(reservation.getEndTime().toLocalTime().toString().substring(0, 5))
				.totalPrice(reservation.getTotalPrice())
				.build())
			.toList();
	}

	@Override
	public List<ReservationResponseDto> adminReservations(HttpServletRequest request, int page, int size) {
		Page<Reservation> reservations;
		Pageable pageable = PageRequest.of(page, size);
		reservations = reservationRepository.findAll(pageable);
		return reservations.stream()
			.map(reservation -> ReservationResponseDto.builder()
				.reservationId(reservation.getId())
				.serviceType(reservation.getServiceDetailType().getServiceType().toString())
				.detailServiceType(reservation.getServiceDetailType().getServiceDetailType())
				.reservationDate(reservation.getReservationDate().toLocalDate().toString())
				.startTime(reservation.getStartTime().toLocalTime().toString().substring(0, 5))
				.endTime(reservation.getEndTime().toLocalTime().toString().substring(0, 5))
				.totalPrice(reservation.getTotalPrice())
				.build())
			.toList();
	}
	private static final Map<String, BigDecimal> ADDITIONAL_PRICE_MAP = Map.of(
		"cooking", BigDecimal.valueOf(10_000),
		"ironing", BigDecimal.valueOf(10_000)
		// 나중에 "laundry", "cleaning" 등 추가 가능
	);

	private BigDecimal calculateTotalPrice(ReservationRequestDto dto, BigDecimal basePrice) {
		String serviceAdd = dto.getServiceAdd(); // 기본값이 ""라고 가정

		BigDecimal additional = Arrays.stream(serviceAdd.split(","))
			.map(String::trim) // 공백 제거
			.filter(ADDITIONAL_PRICE_MAP::containsKey) // 유효한 서비스만
			.map(ADDITIONAL_PRICE_MAP::get) // 금액으로 변환
			.reduce(BigDecimal.ZERO, BigDecimal::add); // 누적 합산

		return basePrice.add(additional);
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

			responseList.add(new SettlementResponseDto(settlement.getId(), settlement.getServiceType(),
				detailType.getServiceDetailType(), settlement.getStatus(), settlement.getPlatformFee(),
				settlement.getAmount()));

		}
		return new WeeklySettlementResponseDto(totalAmount, responseList);
	}

	@Override
	public AdminWeeklySettlementResponseDto getAdminWeeklySettlements(LocalDate startDate, int page, int size) {
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = start.plusDays(7).with(LocalTime.MIN);

		// 페이지 정렬 : pending 먼저, 최신순
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("status"), Sort.Order.desc("createdAt")));

		// 페이징된 settlement 조회
		Page<Settlement> settlementPage = settlementRepository.findByCreatedAtBetween(start, end, pageable);

		List<Settlement> allSettlementsInWeek = settlementRepository.findAllByCreatedAtBetween(start, end);
		BigDecimal totalAmount = allSettlementsInWeek.stream()
			.map(Settlement::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		Page<AdminSettlementResponseDto> dtoPage = settlementPage.map(settlement -> {
			ServiceDetailType detailType = serviceDetailTypeRepository.findById(settlement.getServiceDetailTypeId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
			Manager manager = managerRepository.findById(settlement.getManagerId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

			return new AdminSettlementResponseDto(
				settlement.getId(),
				manager.getName(),
				settlement.getServiceType(),
				detailType.getServiceDetailType(),
				settlement.getStatus(),
				settlement.getAmount(),
				settlement.getCreatedAt()
			);
		});

		return new AdminWeeklySettlementResponseDto(totalAmount, dtoPage);
	}

	@Override
	public SettlementResponseDto getSettlementDetail(Long settlementId, HttpServletRequest request) {
		Optional<Settlement> settlement= settlementRepository.findById(settlementId);

		return new SettlementResponseDto(
			settlement.get().getId(),
			settlement.get().getServiceType(),
			serviceDetailTypeRepository.findById(settlement.get().getServiceDetailTypeId()).get().getServiceDetailType(),
			settlement.get().getStatus(),
			settlement.get().getPlatformFee(),
			settlement.get().getAmount()
		);
	}

	@Transactional
	@Override
	public void settlementApprove(Long settlementId) {
		Optional<Settlement> settlement = settlementRepository.findById(settlementId);
		settlement.get().approve();
	}

	@Transactional
	@Override
	public void settlementReject(Long settlementId) {
		Optional<Settlement> settlement = settlementRepository.findById(settlementId);
		settlement.get().reject();
	}


	public Long getUserId(HttpServletRequest request, UserType userType) {
		UserBase user = (UserBase) request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		if (userType == UserType.CONSUMER) {
			return ((Consumer) user).getId();
		} else if (userType == UserType.MANAGER) {
			return ((Manager) user).getId();
		} else {
			throw new ReservationException(ResponseType.THIS_USER_DOES_NOT_EXIST);
		}
	}
}


