package kernel.maidlab.api.reservation.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.api.reservation.entity.ServiceDetailType;
import kernel.maidlab.api.reservation.enums.ReservationStatus;
import kernel.maidlab.api.reservation.repository.ReservationRepository;
import kernel.maidlab.api.reservation.repository.ServiceDetailTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final ServiceDetailTypeRepository serviceDetailTypeRepository;

	@Override
	public void checkTotalPrice(ReservationRequestDto dto) {
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지않은 서비스 상세 타입입니다."));
		Long serverCalculatedPrice = calculateTotalPrice(dto, detailType.getServicePrice());
		if (!serverCalculatedPrice.equals(dto.getTotalPrice())) {
			log.warn("금액 불일치 - client={}, server={}", dto.getTotalPrice(), serverCalculatedPrice);
			throw new IllegalArgumentException("총 결제 금액이 일치하지 않습니다.");
		}
	}

	@Transactional
	@Override
	public void createReservation(ReservationRequestDto dto) {
		// 결제 검증 로직(애플리케이션 상용 전 true 고정)
		boolean payValid = true;
		if (!payValid) {
			throw new IllegalArgumentException("결제 검증 실패");
		}

		// 금액 재검증
		checkTotalPrice(dto);

		// 예약 저장
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지않은 서비스 상세 타입입니다."));

		Reservation reservation = Reservation.builder()
			.serviceDetailType(detailType)
			.address(dto.getAddress())
			.addressDetail(dto.getAddressDetail())
			.MatchManagerId(dto.getMatchManagerId())
			.consumerId(dto.getConsumerId())
			.housingType(dto.getHousingType())
			.roomSize(dto.getRoomSize())
			.housingInformation(dto.getHousingInformation())
			.reservationDate(dto.getReservationDate())
			.startTime(dto.getStartTime())
			.endTime(dto.getEndTime())
			.serviceAdd(dto.getServiceAdd())

			.pet(dto.getPet())
			.specialRequest(dto.getSpecialRequest())
			.totalPrice(dto.getTotalPrice())
			.status(ReservationStatus.PENDING)
			.createdAt(LocalDateTime.now())
			.build();
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

