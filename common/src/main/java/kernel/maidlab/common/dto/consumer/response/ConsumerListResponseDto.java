package kernel.maidlab.common.dto.consumer.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConsumerListResponseDto {
	private Long id;
	private String phoneNumber;
	private String name;
	private String uuid;
}
