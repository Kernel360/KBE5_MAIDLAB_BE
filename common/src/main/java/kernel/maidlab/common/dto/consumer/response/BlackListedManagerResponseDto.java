package kernel.maidlab.common.dto.consumer.response;

import kernel.maidlab.common.entity.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BlackListedManagerResponseDto {

    private String managerUuid;
    private String name;
    private String profileImage;
    private float  averageRate;
    private String introduceText;

    public static List<BlackListedManagerResponseDto> getManagerResponseDtoList(List<Manager> BlacklistedManagerList){
        return BlacklistedManagerList.stream()
                .map(m -> new BlackListedManagerResponseDto(
                        m.getUuid(),
                        m.getName(),
                        m.getProfileImage(),
                        m.getAverageRate(),
                        m.getIntroduceText()
                ))
                .toList();
    }


}
