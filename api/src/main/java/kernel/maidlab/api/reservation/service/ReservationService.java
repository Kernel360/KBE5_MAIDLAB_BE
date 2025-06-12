package kernel.maidlab.api.reservation.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.reservation.dto.request.CheckInOutRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReviewRegisterRequestDto;
import kernel.maidlab.api.reservation.dto.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.api.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.api.reservation.dto.response.SettlementResponseDto;
import kernel.maidlab.api.reservation.dto.response.WeeklySettlementResponseDto;

public interface ReservationService {
	void createReservation(ReservationRequestDto dto, HttpServletRequest request);

	List<ReservationResponseDto> allReservations(HttpServletRequest request);

	void checkTotalPrice(ReservationRequestDto dto);

	void managerResponseToReservation(Long reservationId, ReservationIsApprovedRequestDto dto,
		HttpServletRequest request);

	void checkin(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request);

	void checkout(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request);

	void cancel(Long reservationId, HttpServletRequest request);

	ReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request);

	void registerReview(Long reservationId, ReviewRegisterRequestDto dto, HttpServletRequest request);

	List<ReservationResponseDto> dailyReservations(LocalDate date, int page, int size);

	List<ReservationResponseDto> adminReservations(HttpServletRequest request, int page, int size);

	WeeklySettlementResponseDto getWeeklySettlements(HttpServletRequest request, LocalDate startDate);

	AdminWeeklySettlementResponseDto getAdminWeeklySettlements(LocalDate startDate, int page, int size);

	SettlementResponseDto getSettlementDetail(Long settlementId, HttpServletRequest request);

	void settlementApprove(Long settlementId);

	@Transactional
	void settlementReject(Long settlementId);
}
