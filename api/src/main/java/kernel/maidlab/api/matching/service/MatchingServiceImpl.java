package kernel.maidlab.api.matching.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.api.matching.entity.Matching;
import kernel.maidlab.api.matching.repository.MatchingRepository;
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
	public List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto) {
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
	public void createMatching(MatchingResponseDto dto) {
		Matching matching = Matching.of(dto);
		if (matchingRepository.existsByReservationId(matching.getReservationId())) {
			throw new BaseException(ResponseType.DUPLICATE_RESERVATION_ID);
		}
		matchingRepository.save(matching);
	}

	@Transactional
	@Override
	public void changeStatus(Long reservationId, Status status) {
		Matching matching = matchingRepository.findByReservationId(reservationId);
		matching.setMatchingStatus(status);
	}

	@Transactional
	@Override
	public void changeManager(Long reservationId, Manager manager) {
		Matching matching = matchingRepository.findByReservationId(reservationId);
		matching.setManagerId(manager.getId());
	}

	@Override
	public List<MatchingResponseDto> allMatching(HttpServletRequest request) {
		List<Matching> matchings = matchingRepository.findAll();
		return matchings.stream()
			.map(matching -> MatchingResponseDto.builder()
				.reservationId(matching.getReservationId())
				.managerId(matching.getManagerId())
				.matchingStatus(matching.getMatchingStatus())
				.build())
			.toList();
	}

	private String extractGuFromAddress(String address) {
		// "구" 단위 추출 (예: "서울시 강남구 역삼동" -> "강남구")
		// 단위를 바꾸고 싶을때는 filter의 endsWith 만 바꾸면 됨
		return Arrays.stream(address.split(" "))
			.filter(s -> s.endsWith("구"))
			.findFirst()
			.orElseThrow(() -> new BaseException(ResponseType.WRONG_ADDRESS));
	}

}
