package kernel.maidlab.domain.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedFileResponseDto {
	private String key;
	private String url;
}
