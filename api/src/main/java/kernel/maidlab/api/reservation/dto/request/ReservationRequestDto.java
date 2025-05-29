package kernel.maidlab.api.reservation.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {
	private Long serviceDetailTypeId; // ex) 0 : 대청소,가사

	private String address;
	private String addressDetail;

	private Long managerId;

	private String housingType;
	private Integer roomSize;
	private String housingInformation;

	private LocalDateTime reservationDate;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private String serviceAdd;
	private String pet;

	private String specialRequest;

	private Long totalPrice;

}
