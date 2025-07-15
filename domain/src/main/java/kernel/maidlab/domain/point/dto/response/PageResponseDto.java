package kernel.maidlab.domain.point.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponseDto<T> {
	private List<T> content;
	private boolean hasNext;
}
