package kernel.maidlab.admin.reservation.service;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.manager.repository.AdminManagerRegionRepository;
import kernel.maidlab.admin.manager.repository.AdminManagerRepository;
import kernel.maidlab.admin.manager.repository.AdminRegionRepository;
import kernel.maidlab.admin.reservation.repository.AdminReservationRepository;
import kernel.maidlab.admin.reservation.repository.AdminServiceDetailTypeRepository;
import kernel.maidlab.admin.reservation.repository.AdminSettlementRepository;
import kernel.maidlab.common.dto.reservation.response.AdminSettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.entity.manager.ManagerRegion;
import kernel.maidlab.common.entity.reservation.Reservation;
import kernel.maidlab.common.entity.reservation.ServiceDetailType;
import kernel.maidlab.common.entity.reservation.Settlement;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.exception.custom.ReservationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminReservationServiceImpl implements AdminReservationService {

	private final AdminReservationRepository adminReservationRepository;
	private final AdminManagerRepository adminManagerRepository;
	private final AdminManagerRegionRepository adminManagerRegionRepository;
	private final AdminRegionRepository adminRegionRepository;
	private final AdminSettlementRepository adminSettlementRepository;
	private final AdminServiceDetailTypeRepository adminServiceDetailTypeRepository;

	@Override
	public List<ReservationResponseDto> adminReservations(HttpServletRequest request, int page, int size) {
		Page<Reservation> reservations;
		Pageable pageable = PageRequest.of(page, size);
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
	public ReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request) {
		Reservation reservation = adminReservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		Manager manager = adminManagerRepository.findById(reservation.getManagerId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		String mangerUuid = manager.getUuid();
		Long managerId = manager.getId();
		List<ManagerRegion> managerRegions = adminManagerRegionRepository.findByManagerId(manager.getId());
		List<String> regionNames = managerRegions.stream()
			.map(mr -> adminRegionRepository.findById(mr.getRegionId().getId())
				.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR))
				.getRegionName())
			.collect(toList());

		return ReservationDetailResponseDto.builder()
			.status(reservation.getStatus())
			.serviceType(reservation.getServiceDetailType().getServiceType().toString())
			.serviceDetailType(reservation.getServiceDetailType().getServiceDetailType())
			.address(reservation.getAddress())
			.addressDetail(reservation.getAddressDetail())
			.managerUuId(mangerUuid)
			.managerName(manager.getName())
			.managerProfileImageUrl(manager.getProfileImage())
			.managerAverageRate(manager.getAverageRate())
			.managerRegion(regionNames)
			.managerPhoneNumber(manager.getPhoneNumber())
			.housingType(reservation.getHousingType())
			.roomSize(reservation.getRoomSize())
			.housingInformation(reservation.getHousingInformation())
			.reservationDate(reservation.getReservationDate())
			.startTime(reservation.getStartTime())
			.endTime(reservation.getEndTime())
			.serviceAdd(reservation.getServiceAdd())
			.pet(reservation.getPet())
			.specialRequest(reservation.getSpecialRequest())
			.totalPrice(reservation.getTotalPrice())
			.build();
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
			ServiceDetailType detailType = adminServiceDetailTypeRepository.findById(settlement.getServiceDetailTypeId())
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
		Optional<Settlement> settlement= adminSettlementRepository.findById(settlementId);

		return new SettlementResponseDto(
			settlement.get().getId(),
			settlement.get().getReservationId(),
			settlement.get().getServiceType(),
			adminServiceDetailTypeRepository.findById(settlement.get().getServiceDetailTypeId()).get().getServiceDetailType(),
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
}
