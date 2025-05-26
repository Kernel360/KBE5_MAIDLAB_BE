package kernel.maidlab.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedFileResponseDto {
	private String key;
	private String url;
}
