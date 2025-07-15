package kernel.maidlab.api.consumer.dto;

import kernel.maidlab.api.consumer.entity.Consumer;
import kernel.maidlab.common.enums.SocialType;
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

    public static ConsumerMyPageDto from(Consumer consumer){

        return new ConsumerMyPageDto(
                consumer.getName(),
                consumer.getPoint(),
                consumer.getProfileImage(),
                consumer.getSocialType()
        );
    }
}
