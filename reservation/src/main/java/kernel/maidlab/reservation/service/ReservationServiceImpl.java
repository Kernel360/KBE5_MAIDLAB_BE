package kernel.maidlab.reservation.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import kernel.maidlab.reservation.dto.request.PaymentRequestDto;
import kernel.maidlab.reservation.entity.Reservation;
import kernel.maidlab.reservation.entity.ServiceDetailType;
import kernel.maidlab.reservation.enums.ReservationStatus;
import kernel.maidlab.reservation.repository.ReservationRepository;
import kernel.maidlab.reservation.repository.ServiceDetailTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final ServiceDetailTypeRepository serviceDetailTypeRepository;

	@Override
	public void createReservation(PaymentRequestDto dto) {
		ServiceDetailType detailType = serviceDetailTypeRepository.findById(dto.getServiceDetailTypeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지않은 서비스 상세 타입입니다."));

		// 서버에서 총 결제 금액 재계산
		Long basePrice = detailType.getServicePrice();
		Long additionalPrice = calculateAdditionalServicePrice(dto);
		Long totalCalculatedPrice = basePrice + additionalPrice;
		Long clientPrice = dto.getTotalPrice();

		// 금액 비교
		if (!totalCalculatedPrice.equals(clientPrice)) {
			log.warn("결제 금액 불일치: client={}, server={}, detailTypeId={}", clientPrice, totalCalculatedPrice,
				dto.getServiceDetailTypeId());
			throw new IllegalArgumentException("총 결제 금액이 일치하지 않습니다.");
		}

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
			// .helper(dto.getHelper())
			.pet(dto.getPet())
			.specialRequest(dto.getSpecialRequest())
			.totalPrice(clientPrice)
			.status(ReservationStatus.PENDING)
			.createdAt(LocalDateTime.now())
			.build();
		reservationRepository.save(reservation);
	}

	private Long calculateAdditionalServicePrice(PaymentRequestDto dto) {
		long additional = 0L;

		if (dto.getServiceAdd() != null && dto.getServiceAdd().equals("COOK")) {
			additional += 10_000;
		}
		return additional;
	}
}
