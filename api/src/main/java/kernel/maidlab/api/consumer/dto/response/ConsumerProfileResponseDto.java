package kernel.maidlab.api.consumer.dto.response;

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
    private String phoneNumber;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String address;
    private String detailAddress;

}
