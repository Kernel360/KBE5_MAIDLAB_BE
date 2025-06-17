package kernel.maidlab.common.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerListResponseDto {
	private String name;
	private String uuid;
	private Long id;
}
