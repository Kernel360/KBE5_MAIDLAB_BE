package kernel.maidlab.common.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileNamesRequestDto {
	private List<String> filenames;
}
