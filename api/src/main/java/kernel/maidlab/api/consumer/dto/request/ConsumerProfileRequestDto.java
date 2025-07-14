package kernel.maidlab.api.consumer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerProfileRequestDto {
    private String profileImage;
    private String address;
    private String detailAddress;
}
