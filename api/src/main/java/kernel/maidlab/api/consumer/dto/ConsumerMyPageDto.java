package kernel.maidlab.api.consumer.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerMyPageDto {
    private String name;
    private int point;
    private String profileImage;

    @Builder
    public ConsumerMyPageDto(String name, int point, String profileImage) {
        this.name = name;
        this.point = point;
        this.profileImage = profileImage;
    }
}