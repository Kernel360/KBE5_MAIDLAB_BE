package kernel.maidlab.api.matching.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.matching.dto.ManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;

@Service
public class MatchingServiceImpl implements MatchingService {

	private final ManagerRepository managerRepository;

	public MatchingServiceImpl(ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Override
	public List<ManagerResponseDto> FindAvailableManagers(MatchingRequestDto dto) {
		System.out.println(dto.getStartTime());
		LocalDateTime StartTime = LocalDateTime.parse(dto.getStartTime());
		LocalDateTime EndTime = LocalDateTime.parse(dto.getEndTime());
		System.out.println("test");
		System.out.println(StartTime);
		String gu = extractGuFromAddress(dto.getAddress());
		System.out.println(gu);


		return managerRepository.FindAvailableManagers(gu, StartTime, EndTime);
	}

	private String extractGuFromAddress(String address) {
		// 간단히 "구" 단위 추출 (예: "서울시 강남구 역삼동" -> "강남구")
		return Arrays.stream(address.split(" "))
			.filter(s -> s.endsWith("구"))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("주소에 '구'가 없습니다."));
	}

}
