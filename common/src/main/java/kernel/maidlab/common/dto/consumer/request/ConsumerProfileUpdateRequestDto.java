package kernel.maidlab.common.dto.consumer.request;

import kernel.maidlab.common.enums.Gender;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ConsumerProfileUpdateRequestDto {

    private String profileImage;
    private String name;
    private Gender gender;
    private LocalDate birth;
    private String address;
    private String detailAddress;

}
