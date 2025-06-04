package kernel.maidlab.api.manager.dto.object;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentListItem {

	@NotBlank
	private String fileType;

	@NotBlank
	private String fileName;

	@NotBlank
	private String uploadedFileUrl;

}
