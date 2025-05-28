package kernel.maidlab.api.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.exception.custom.ReservationException;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.api.reservation.entity.ServiceDetailType;
import kernel.maidlab.api.reservation.repository.ReservationRepository;
import kernel.maidlab.api.reservation.repository.ServiceDetailTypeRepository;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final ServiceDetailTypeRepository serviceDetailTypeRepository;

	@Override
	public List<ReservationResponseDto> allReservations(){
		List<Reservation> reservations = reservationRepository.findAll();
		return reservations.stream()
			.map(reservation -> ReservationResponseDto.builder()
					.serviceType(reservation.getServiceDetailType().getServiceType().toString())
					.detailServiceType(reservation.getServiceDetailType().getServiceDetailType())
					.reservationDate(reservation.getReservationDate().toLocalDate().toString())
					.startTime(reservation.getStartTime().toLocalTime().toString().substring(0,5))
					.endTime(reservation.getEndTime().toLocalTime().toString().substring(0,5))
					.totalPrice(reservation.getTotalPrice())
					.build()
			)
			.toList();
	}

	@Override
	public void checkTotalPrice(ReservationRequestDto dto) {
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지않은 서비스 상세 타입입니다."));
		Long serverCalculatedPrice = calculateTotalPrice(dto, detailType.getServicePrice());
		if (!serverCalculatedPrice.equals(dto.getTotalPrice())) {
			log.warn("금액 불일치 - client={}, server={}", dto.getTotalPrice(), serverCalculatedPrice);
			throw new ReservationException(ResponseType.VALIDATION_FAILED);
		}
	}

	@Transactional
	@Override
	public void createReservation(ReservationRequestDto dto) {
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

		Reservation reservation = Reservation.of(dto, detailType);
		reservationRepository.save(reservation);
	}

	private Long calculateTotalPrice(ReservationRequestDto dto, Long basePrice) {

		long additional = 0L;

		if (dto.getServiceAdd() != null && dto.getServiceAdd().equals("COOK")) {
			additional += 10_000;
		}
		return basePrice + additional;
	}
}

