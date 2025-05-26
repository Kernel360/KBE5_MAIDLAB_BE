package kernel.maidlab.reservation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
	private Long serviceDetailTypeId; // ex) 0 : 대청소,가사

	private String address;
	private String addressDetail;

	private Long MatchManagerId;

	private Long consumerId;

	private String housingType;
	private Integer roomSize;
	private String housingInformation;

	private LocalDateTime reservationDate;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private String serviceAdd;
	private Boolean helper;
	private String pet;

	private String specialRequest;

	private Long totalPrice;

}