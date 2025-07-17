package kernel.maidlab.domain.consumer.dto;

import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.domain.consumer.entity.Consumer;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerMyPageDto {
    private String name;
    private int point;
    private String profileImage;
    private SocialType socialType;

    public static ConsumerMyPageDto from(Consumer consumer) {

        return new ConsumerMyPageDto(
                consumer.getName(),
                consumer.getPoint(),
                consumer.getProfileImage(),
                consumer.getSocialType()
        );
    }
}
