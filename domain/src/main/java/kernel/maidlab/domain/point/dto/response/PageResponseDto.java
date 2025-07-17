package kernel.maidlab.domain.point.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponseDto<T> {
    private List<T> content;
    private boolean hasNext;
}
