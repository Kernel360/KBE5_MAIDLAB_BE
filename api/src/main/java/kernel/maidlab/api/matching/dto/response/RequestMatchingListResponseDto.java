package kernel.maidlab.api.matching.dto.response;

import java.math.BigDecimal;

import kernel.maidlab.api.reservation.entity.Reservation;
import lombok.Data;

@Data
public class RequestMatchingListResponseDto {
	private Long reservationId;
	private String serviceType;
	private String detailServiceType;
	private String reservationDate;
	private String startTime;
	private String endTime;
	private String address;
	private String addressDetail;
	private Integer roomSize;
	private String pet;
	private BigDecimal totalPrice;

	public RequestMatchingListResponseDto(Reservation reservation) {

		this.reservationId = reservation.getId();
		this.serviceType = reservation.getServiceDetailType().getServiceType().toString();
		this.detailServiceType = reservation.getServiceDetailType().getServiceDetailType();
		this.reservationDate = reservation.getReservationDate().toLocalDate().toString();
		this.startTime = reservation.getStartTime().toLocalTime().toString();
		this.endTime = reservation.getEndTime().toLocalTime().toString();
		this.address = reservation.getAddress();
		this.addressDetail = reservation.getAddressDetail();
		this.roomSize = reservation.getRoomSize();
		this.pet = reservation.getPet();
		this.totalPrice = reservation.getTotalPrice();
	}
}
