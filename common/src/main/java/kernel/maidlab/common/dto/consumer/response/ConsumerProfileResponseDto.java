package kernel.maidlab.common.dto.consumer.response;

import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ConsumerProfileResponseDto {

    private String profileImage;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String address;
    private String detailAddress;


    public static ConsumerProfileResponseDto getInstance(Consumer consumer){

        return new ConsumerProfileResponseDto(
                consumer.getProfileImage(),
                consumer.getName(),
                consumer.getBirth(),
                consumer.getGender(),
                consumer.getAddress(),
                consumer.getDetailAddress()
        );
    }

}
