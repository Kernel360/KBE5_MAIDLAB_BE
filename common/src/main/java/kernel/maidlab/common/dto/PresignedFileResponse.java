package kernel.maidlab.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedFileResponse {
	private String key;
	private String url;
}
