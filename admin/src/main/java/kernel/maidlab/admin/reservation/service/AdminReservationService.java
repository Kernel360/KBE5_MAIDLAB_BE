package kernel.maidlab.admin.reservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.domain.reservation.dto.response.AdminReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.domain.reservation.dto.response.SettlementGraphDataDto;
import kernel.maidlab.domain.reservation.dto.response.SettlementResponseDto;

public interface AdminReservationService {
	List<ReservationResponseDto> adminReservations(HttpServletRequest request, int page, int size);

	AdminReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request);

	List<ReservationResponseDto> dailyReservations(LocalDate date, int page, int size);

	AdminWeeklySettlementResponseDto getAdminWeeklySettlements(LocalDate startDate, int page, int size);

	@Transactional
	void settlementApprove(Long settlementId);

	@Transactional
	void settlementReject(Long settlementId);

	SettlementResponseDto getSettlementDetail(Long settlementId, HttpServletRequest request);

	Long getTodayReservation(HttpServletRequest request);

	List<ReservationResponseDto> getConsumerReservation(HttpServletRequest request, Long id, int page, int size);

	List<ReservationResponseDto> getManagerReservation(HttpServletRequest request, Long id, int page, int size);

	Long getCountByConsumerId(HttpServletRequest request, Long ConsumerId);

	BigDecimal getTotalPaidMoney(HttpServletRequest request, Long consumerId);

	BigDecimal getReviewedPercent(HttpServletRequest request, Long consumerId);

	Long getActiveReservationCountByManagerId(HttpServletRequest request, Long managerId);

	BigDecimal getTotalSettlementAmountByManagerId(HttpServletRequest request, Long managerId);

	BigDecimal getManagerReviewedPercent(HttpServletRequest request, Long managerId);

	SettlementGraphDataDto getSettlementGraphData(HttpServletRequest request, LocalDate startDate, LocalDate endDate,
		String period);
}
