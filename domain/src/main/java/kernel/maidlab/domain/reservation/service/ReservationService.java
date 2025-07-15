package kernel.maidlab.domain.reservation.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.reservation.dto.request.*;
import kernel.maidlab.domain.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.domain.reservation.dto.response.WeeklySettlementResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

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

	Page<ReservationResponseDto> getConsumerReservationsWithPaging(String status, int page, int size, String sortBy,
		String sortOrder, HttpServletRequest request);

	Page<ReservationResponseDto> getManagerReservationsWithPaging(String status, int page, int size, String sortOrder,
		HttpServletRequest request);
}
