package kernel.maidlab.common.dto.consumer;

import kernel.maidlab.common.entity.consumer.Consumer;
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

    public static ConsumerMyPageDto getInstance(Consumer consumer){

        return new ConsumerMyPageDto(
                consumer.getName(),
                consumer.getPoint(),
                consumer.getProfileImage()
        );
    }
}
