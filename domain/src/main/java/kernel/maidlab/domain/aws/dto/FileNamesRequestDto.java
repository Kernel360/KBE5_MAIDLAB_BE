package kernel.maidlab.domain.aws.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileNamesRequestDto {
	private List<String> filenames;
}
