package kernel.maidlab.admin.reservation.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.common.dto.reservation.response.AdminReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;

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
}
