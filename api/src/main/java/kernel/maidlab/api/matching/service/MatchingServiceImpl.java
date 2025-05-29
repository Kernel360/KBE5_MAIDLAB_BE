package kernel.maidlab.api.matching.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.repository.ManagerRepository;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.matching.dto.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;
import kernel.maidlab.api.matching.entity.Matching;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;

@Service
public class MatchingServiceImpl implements MatchingService {

	private final ManagerRepository managerRepository;
	private final MatchingRepository matchingRepository;

	public MatchingServiceImpl(ManagerRepository managerRepository, MatchingRepository matchingRepository) {
		this.managerRepository = managerRepository;
		this.matchingRepository = matchingRepository;
	}

	@Override
	public List<AvailableManagerResponseDto> FindAvailableManagers(MatchingRequestDto dto) {
		System.out.println(dto.getStartTime());
		LocalDateTime StartTime = LocalDateTime.parse(dto.getStartTime());
		LocalDateTime EndTime = LocalDateTime.parse(dto.getEndTime());
		System.out.println("test");
		System.out.println(StartTime);
		String gu = extractGuFromAddress(dto.getAddress());
		System.out.println(gu);

		return managerRepository.FindAvailableManagers(gu, StartTime, EndTime);
	}

	@Override
	public void createMatching(MatchingDto dto) {
		Matching matching = Matching.of(dto);
		if (matchingRepository.existsByReservationId(matching.getReservationId())) {
			throw new BaseException(ResponseType.DUPLICATE_RESERVATION_ID);
		}
		matchingRepository.save(matching);
	}

	@Transactional
	@Override
	public void ChangeStatus(Long reservationId, Status status) {
		Matching matching = matchingRepository.findByReservationId(reservationId);
		matching.setMatchingStatus(status);
	}

	private String extractGuFromAddress(String address) {
		// "구" 단위 추출 (예: "서울시 강남구 역삼동" -> "강남구")
		return Arrays.stream(address.split(" "))
			.filter(s -> s.endsWith("구"))
			.findFirst()
			.orElseThrow(() -> new BaseException(ResponseType.WRONG_ADDRESS));
	}




}
