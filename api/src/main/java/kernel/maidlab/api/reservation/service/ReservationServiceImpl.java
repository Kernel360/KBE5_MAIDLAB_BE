package kernel.maidlab.api.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.util.AuthUtil;
import kernel.maidlab.api.exception.custom.ReservationException;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.api.matching.service.MatchingService;
import kernel.maidlab.api.reservation.dto.request.CheckInOutRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.api.reservation.entity.ServiceDetailType;
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
	private final AuthUtil authUtil;
	private final MatchingService matchingService;

	@Override
	public List<ReservationResponseDto> allReservations(HttpServletRequest request) {
		UserType userType = authUtil.getUserType(request);
		List<Reservation> reservations;

		if (userType == UserType.CONSUMER) {
			Long consumerId = authUtil.getConsumer(request).getId();
			reservations = reservationRepository.findByConsumerId(consumerId);
		} else {
			Long managerId = authUtil.getManager(request).getId();
			reservations = reservationRepository.findByManagerId(managerId);
		}

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

		Reservation reservation = Reservation.of(dto, consumerId, detailType);
		reservationRepository.save(reservation);
	}

	@Transactional
	@Override
	public void managerResponseToReservation(Long reservationId, ReservationIsApprovedRequestDto dto,
		HttpServletRequest request) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));

		Long managerId = authUtil.getManager(request).getId();
		if (!reservation.getStatus().equals(Status.APPROVED)) {
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
		matchingRepository.deleteById(matchingRepository.findByReservationId(reservationId).getId());
	}

	@Override
	public void checkTotalPrice(ReservationRequestDto dto) {
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new ReservationException(ResponseType.DATABASE_ERROR));
		Long serverCalculatedPrice = calculateTotalPrice(dto, detailType.getServicePrice());
		if (!serverCalculatedPrice.equals(dto.getTotalPrice())) {
			log.warn("금액 불일치 - client={}, server={}", dto.getTotalPrice(), serverCalculatedPrice);
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}
	}

	private Long calculateTotalPrice(ReservationRequestDto dto, Long basePrice) {

		long additional = 0L;

		if (dto.getServiceAdd() != null && dto.getServiceAdd().equals("COOK")) {
			additional += 10_000;
		}
		return basePrice + additional;
	}
}

