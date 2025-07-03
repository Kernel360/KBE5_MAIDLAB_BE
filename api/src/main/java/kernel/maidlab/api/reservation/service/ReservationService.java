package kernel.maidlab.api.reservation.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.reservation.request.PaymentRequestDto;
import kernel.maidlab.common.dto.reservation.request.CheckInOutRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReviewRegisterRequestDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.WeeklySettlementResponseDto;

public interface ReservationService {
	Long createReservation(ReservationRequestDto dto, HttpServletRequest request);

	List<ReservationResponseDto> allReservations(HttpServletRequest request);

	void checkTotalPrice(ReservationRequestDto dto);

	void managerResponseToReservation(Long reservationId, ReservationIsApprovedRequestDto dto,
		HttpServletRequest request);

	void pay(PaymentRequestDto dto, HttpServletRequest request);

	void checkin(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request);

	void checkout(Long reservationId, CheckInOutRequestDto dto, HttpServletRequest request);

	void cancel(Long reservationId, HttpServletRequest request);

	ReservationDetailResponseDto getReservationDetail(Long reservationId, HttpServletRequest request);

	void registerReview(ReviewRegisterRequestDto dto, HttpServletRequest request);

	WeeklySettlementResponseDto getWeeklySettlements(HttpServletRequest request, LocalDate startDate);
}
