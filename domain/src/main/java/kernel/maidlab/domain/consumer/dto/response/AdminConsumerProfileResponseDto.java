package kernel.maidlab.domain.consumer.dto.response;


import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AdminConsumerProfileResponseDto {

    private String profileImage;
    private String phoneNumber;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String address;
    private String detailAddress;
    private Integer point;
    private Boolean isDeleted;
    private SocialType socialType;

    public static AdminConsumerProfileResponseDto from(Consumer consumer){

        return new AdminConsumerProfileResponseDto(
                consumer.getProfileImage(),
                consumer.getPhoneNumber(),
                consumer.getName(),
                consumer.getBirth(),
                consumer.getGender(),
                consumer.getAddress(),
                consumer.getDetailAddress(),
                consumer.getPoint(),
                consumer.getIsDeleted(),
                consumer.getSocialType()
        );
    }
}
