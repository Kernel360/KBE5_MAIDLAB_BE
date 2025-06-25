package kernel.maidlab.common.dto.consumer.response;

import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminConsumerProfileResponseDto {

    private String profileImage;
    private String phoneNumber;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String address;
    private String detailAddress;


    public static AdminConsumerProfileResponseDto getInstance(Consumer consumer){

        return new AdminConsumerProfileResponseDto(
                consumer.getProfileImage(),
                consumer.getPhoneNumber(),
                consumer.getName(),
                consumer.getBirth(),
                consumer.getGender(),
                consumer.getAddress(),
                consumer.getDetailAddress()
        );
    }
}
