package kernel.maidlab.domain.point.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
public class PointRecordRequestDto {

    private Integer monthOffset = 0; // 오늘 기준 월 오프셋
    private String pointType; // "ALL", "PAYMENT", "EVENT"
    private PageableRequest pageable;

    @Getter
    @NoArgsConstructor
    public static class PageableRequest {
        private Integer page = 0;
        private Integer size = 20;
        private List<SortRequest> sort = List.of();

        @Getter
        @NoArgsConstructor
        public static class SortRequest {
            private String property = "createdAt";
            private Sort.Direction direction = Sort.Direction.DESC;
        }
    }
}
