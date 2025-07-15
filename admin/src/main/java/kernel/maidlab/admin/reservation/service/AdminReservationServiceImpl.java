package kernel.maidlab.admin.reservation.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.consumer.repository.AdminConsumerRepository;
import kernel.maidlab.admin.manager.repository.AdminManagerRepository;
import kernel.maidlab.admin.reservation.repository.AdminReservationRepository;
import kernel.maidlab.admin.reservation.repository.AdminReviewRepository;
import kernel.maidlab.admin.reservation.repository.AdminServiceDetailTypeRepository;
import kernel.maidlab.admin.reservation.repository.AdminSettlementRepository;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.core.exception.custom.ReservationException;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.reservation.dto.response.AdminReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.AdminSettlementResponseDto;
import kernel.maidlab.domain.reservation.dto.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.domain.reservation.dto.response.SettlementGraphDataDto;
import kernel.maidlab.domain.reservation.dto.response.SettlementResponseDto;
import kernel.maidlab.domain.reservation.entity.Reservation;
import kernel.maidlab.domain.reservation.entity.ServiceDetailType;
import kernel.maidlab.domain.reservation.entity.Settlement;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminReservationServiceImpl implements AdminReservationService {

	private final AdminReservationRepository adminReservationRepository;
	private final AdminManagerRepository adminManagerRepository;
	private final AdminSettlementRepository adminSettlementRepository;
	private final AdminServiceDetailTypeRepository adminServiceDetailTypeRepository;
	private final AdminConsumerRepository adminConsumerRepository;
	private final AdminReviewRepository adminReviewRepository;

	@Override
	public List<ReservationResponseDto> adminReservations(HttpServletRequest request, int page, int size) {
		Page<Reservation> reservations;
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		reservations = adminReservationRepository.findAll(pageable);
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
	public AdminReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request) {
		Reservation reservation = adminReservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Optional<Manager> manager = adminManagerRepository.findById(reservation.getManagerId());

		Optional<Consumer> consumer = adminConsumerRepository.findById(reservation.getConsumerId());

		return AdminReservationDetailResponseDto.getInstance(reservationId, reservation, manager.get(), consumer.get());
	}

	@Override
	public List<ReservationResponseDto> dailyReservations(LocalDate date, int page, int size) {
		Page<Reservation> reservations;
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		Pageable pageable = PageRequest.of(page, size);
		reservations = adminReservationRepository.findAllByReservationDateBetween(start, end, pageable);

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
	public AdminWeeklySettlementResponseDto getAdminWeeklySettlements(LocalDate startDate, int page, int size) {
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = start.plusDays(7).with(LocalTime.MIN);

		// 페이지 정렬 : pending 먼저, 최신순
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("status"), Sort.Order.desc("createdAt")));

		// 페이징된 settlement 조회
		Page<Settlement> settlementPage = adminSettlementRepository.findByCreatedAtBetween(start, end, pageable);

		List<Settlement> allSettlementsInWeek = adminSettlementRepository.findAllByCreatedAtBetween(start, end);
		BigDecimal totalAmount = allSettlementsInWeek.stream()
			.map(Settlement::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		Page<AdminSettlementResponseDto> dtoPage = settlementPage.map(settlement -> {
			ServiceDetailType detailType = adminServiceDetailTypeRepository.findById(
					settlement.getServiceDetailTypeId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
			Manager manager = adminManagerRepository.findById(settlement.getManagerId())
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

	@Transactional
	@Override
	public void settlementApprove(Long settlementId) {
		Optional<Settlement> settlement = adminSettlementRepository.findById(settlementId);
		settlement.get().approve();
	}

	@Transactional
	@Override
	public void settlementReject(Long settlementId) {
		Optional<Settlement> settlement = adminSettlementRepository.findById(settlementId);
		settlement.get().reject();
	}

	@Override
	public SettlementResponseDto getSettlementDetail(Long settlementId, HttpServletRequest request) {
		Optional<Settlement> settlement = adminSettlementRepository.findById(settlementId);

		return new SettlementResponseDto(
			settlement.get().getId(),
			settlement.get().getReservationId(),
			settlement.get().getServiceType(),
			adminServiceDetailTypeRepository.findById(settlement.get().getServiceDetailTypeId())
				.get()
				.getServiceDetailType(),
			settlement.get().getStatus(),
			settlement.get().getPlatformFee(),
			settlement.get().getAmount()
		);
	}

	@Override
	public Long getTodayReservation(HttpServletRequest request) {
		LocalDate today = LocalDate.now();
		return adminReservationRepository.countByReservationDate(today);
	}

	@Override
	public List<ReservationResponseDto> getConsumerReservation(HttpServletRequest request, Long consumerId, int page,
		int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

		return adminReservationRepository.findAllByConsumerId(consumerId, pageable).stream()
			.map(reservation -> ReservationResponseDto.builder()
				.reservationId(reservation.getId())
				.serviceType(reservation.getServiceDetailType().getServiceType().toString())
				.detailServiceType(reservation.getServiceDetailType().getServiceDetailType())
				.reservationDate(reservation.getReservationDate().toLocalDate().toString())
				.startTime(reservation.getStartTime().toLocalTime().toString().substring(0, 5))
				.status(reservation.getStatus())
				.endTime(reservation.getEndTime().toLocalTime().toString().substring(0, 5))
				.totalPrice(reservation.getTotalPrice())
				.build())
			.toList();
	}

	@Override
	public List<ReservationResponseDto> getManagerReservation(HttpServletRequest request, Long managerId, int page,
		int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

		return adminReservationRepository.findAllByManagerId(managerId, pageable).stream()
			.map(reservation -> ReservationResponseDto.builder()
				.reservationId(reservation.getId())
				.serviceType(reservation.getServiceDetailType().getServiceType().toString())
				.detailServiceType(reservation.getServiceDetailType().getServiceDetailType())
				.reservationDate(reservation.getReservationDate().toLocalDate().toString())
				.startTime(reservation.getStartTime().toLocalTime().toString().substring(0, 5))
				.status(reservation.getStatus())
				.endTime(reservation.getEndTime().toLocalTime().toString().substring(0, 5))
				.totalPrice(reservation.getTotalPrice())
				.build())
			.toList();
	}

	@Override
	public Long getCountByConsumerId(HttpServletRequest request, Long consumerId) {
		return adminReservationRepository.countByConsumerId(consumerId);
	}

	@Override
	public BigDecimal getTotalPaidMoney(HttpServletRequest request, Long consumerId) {
		BigDecimal total = adminReservationRepository.sumTotalPrice(consumerId);
		if (total != null)
			return total;
		else
			return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getReviewedPercent(HttpServletRequest request, Long consumerId) {
		Long countReview = adminReviewRepository.countByConsumerIdAndIsConsumerToManager(consumerId, true);
		Long completed = adminReservationRepository.countByConsumerIdAndStatus(consumerId, Status.COMPLETED);
		if (completed == 0 || countReview == 0) {
			return BigDecimal.ZERO;
		}

		return BigDecimal.valueOf(completed / countReview).multiply(BigDecimal.valueOf(100));
	}

	@Override
	public BigDecimal getManagerReviewedPercent(HttpServletRequest request, Long managerId) {
		Long countReview = adminReviewRepository.countByManagerIdAndIsConsumerToManager(managerId, false);
		Long completed = adminReservationRepository.countByManagerIdAndStatus(managerId, Status.COMPLETED);
		if (completed == 0 || countReview == 0) {
			return BigDecimal.ZERO;
		}

		return BigDecimal.valueOf(countReview)
			.divide(BigDecimal.valueOf(completed), 2, RoundingMode.HALF_UP)
			.multiply(BigDecimal.valueOf(100));
	}

	@Override
	public Long getActiveReservationCountByManagerId(HttpServletRequest request, Long managerId) {
		Set<Status> activeStatuses = Set.of(Status.MATCHED, Status.WORKING, Status.COMPLETED);
		return adminReservationRepository.countByManagerIdAndStatusIn(managerId, activeStatuses);
	}

	@Override
	public BigDecimal getTotalSettlementAmountByManagerId(HttpServletRequest request, Long managerId) {
		return adminSettlementRepository.sumAmountByManagerId(managerId);
	}

	@Override
	public SettlementGraphDataDto getSettlementGraphData(HttpServletRequest request, LocalDate startDate,
		LocalDate endDate, String period) {
		if (startDate == null) {
			startDate = LocalDate.now().minusDays(30);
		}
		if (endDate == null) {
			endDate = LocalDate.now();
		}

		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		List<Settlement> settlements = adminSettlementRepository.findAllByCreatedAtBetween(startDateTime, endDateTime);

		List<SettlementGraphDataDto.DailySettlementData> dailyData = getDailyData(settlements);
		List<SettlementGraphDataDto.WeeklySettlementData> weeklyData = getWeeklyData(settlements);
		List<SettlementGraphDataDto.MonthlySettlementData> monthlyData = getMonthlyData(settlements);

		List<SettlementGraphDataDto.ServiceTypeData> serviceTypeData = settlements.stream()
			.collect(Collectors.groupingBy(settlement -> settlement.getServiceType().toString()))
			.entrySet().stream()
			.map(entry -> {
				String serviceType = entry.getKey();
				List<Settlement> serviceSettlements = entry.getValue();
				BigDecimal totalAmount = serviceSettlements.stream()
					.map(Settlement::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				return new SettlementGraphDataDto.ServiceTypeData(serviceType, totalAmount,
					(long)serviceSettlements.size());
			})
			.collect(Collectors.toList());

		List<SettlementGraphDataDto.StatusData> statusData = settlements.stream()
			.collect(Collectors.groupingBy(settlement -> settlement.getStatus().toString()))
			.entrySet().stream()
			.map(entry -> {
				String status = entry.getKey();
				List<Settlement> statusSettlements = entry.getValue();
				BigDecimal totalAmount = statusSettlements.stream()
					.map(Settlement::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				return new SettlementGraphDataDto.StatusData(status, totalAmount, (long)statusSettlements.size());
			})
			.collect(Collectors.toList());

		BigDecimal totalAmount = settlements.stream()
			.map(Settlement::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalPlatformFee = settlements.stream()
			.map(Settlement::getPlatformFee)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		Long totalCount = (long)settlements.size();

		return new SettlementGraphDataDto(dailyData, weeklyData, monthlyData, serviceTypeData, statusData, totalAmount,
			totalPlatformFee, totalCount);
	}

	private List<SettlementGraphDataDto.DailySettlementData> getDailyData(List<Settlement> settlements) {
		return settlements.stream()
			.collect(Collectors.groupingBy(settlement -> settlement.getCreatedAt().toLocalDate()))
			.entrySet().stream()
			.map(entry -> {
				LocalDate date = entry.getKey();
				List<Settlement> dailySettlements = entry.getValue();
				BigDecimal totalAmount = dailySettlements.stream()
					.map(Settlement::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal totalPlatformFee = dailySettlements.stream()
					.map(Settlement::getPlatformFee)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				return new SettlementGraphDataDto.DailySettlementData(date, totalAmount, totalPlatformFee,
					(long)dailySettlements.size());
			})
			.sorted((a, b) -> a.getDate().compareTo(b.getDate()))
			.collect(Collectors.toList());
	}

	private List<SettlementGraphDataDto.WeeklySettlementData> getWeeklyData(List<Settlement> settlements) {
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		return settlements.stream()
			.collect(Collectors.groupingBy(settlement -> {
				LocalDate date = settlement.getCreatedAt().toLocalDate();
				return date.with(weekFields.dayOfWeek(), 1);
			}))
			.entrySet().stream()
			.map(entry -> {
				LocalDate weekStart = entry.getKey();
				LocalDate weekEnd = weekStart.plusDays(6);
				List<Settlement> weeklySettlements = entry.getValue();
				BigDecimal totalAmount = weeklySettlements.stream()
					.map(Settlement::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal totalPlatformFee = weeklySettlements.stream()
					.map(Settlement::getPlatformFee)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				return new SettlementGraphDataDto.WeeklySettlementData(weekStart, weekEnd, totalAmount,
					totalPlatformFee, (long)weeklySettlements.size());
			})
			.sorted((a, b) -> a.getWeekStart().compareTo(b.getWeekStart()))
			.collect(Collectors.toList());
	}

	private List<SettlementGraphDataDto.MonthlySettlementData> getMonthlyData(List<Settlement> settlements) {
		return settlements.stream()
			.collect(Collectors.groupingBy(settlement -> {
				LocalDate date = settlement.getCreatedAt().toLocalDate();
				return date.getYear() * 100 + date.getMonthValue();
			}))
			.entrySet().stream()
			.map(entry -> {
				int yearMonth = entry.getKey();
				int year = yearMonth / 100;
				int month = yearMonth % 100;
				List<Settlement> monthlySettlements = entry.getValue();
				BigDecimal totalAmount = monthlySettlements.stream()
					.map(Settlement::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal totalPlatformFee = monthlySettlements.stream()
					.map(Settlement::getPlatformFee)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				return new SettlementGraphDataDto.MonthlySettlementData(year, month, totalAmount, totalPlatformFee,
					(long)monthlySettlements.size());
			})
			.sorted((a, b) -> {
				if (a.getYear() != b.getYear()) {
					return Integer.compare(a.getYear(), b.getYear());
				}
				return Integer.compare(a.getMonth(), b.getMonth());
			})
			.collect(Collectors.toList());
	}
}
