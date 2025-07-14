package kernel.maidlab.api.aws.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FileNamesRequestDto {
	private List<String> filenames;
}
