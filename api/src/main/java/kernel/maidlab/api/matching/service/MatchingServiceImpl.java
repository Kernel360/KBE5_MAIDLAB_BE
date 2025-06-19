package kernel.maidlab.api.matching.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.jwt.JwtFilter;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.api.reservation.service.ReservationService;
import kernel.maidlab.common.entity.base.UserBase;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.dto.matching.response.RequestMatchingListResponseDto;
import kernel.maidlab.common.entity.reservation.Reservation;
import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.dto.matching.response.MatchingResponseDto;
import kernel.maidlab.common.dto.matching.request.MatchingRequestDto;
import kernel.maidlab.common.entity.matching.Matching;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;

@Service
public class MatchingServiceImpl implements MatchingService {
	private final MatchingRepository matchingRepository;
	private final ManagerService managerService;
	private final ReservationService reservationService;

	public MatchingServiceImpl(MatchingRepository matchingRepository, ManagerService managerService,
		ReservationService reservationService) {
		this.matchingRepository = matchingRepository;
		this.managerService = managerService;
		this.reservationService = reservationService;
	}

	@Override
	public List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto) {
		LocalDateTime StartTime = LocalDateTime.parse(dto.getStartTime());
		LocalDateTime EndTime = LocalDateTime.parse(dto.getEndTime());
		String gu = extractGuFromAddress(dto.getAddress());

		return managerService.findAvailableManagers(gu, StartTime, EndTime);
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





	@Override
	public List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size) {

		UserBase me = (UserBase)request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		Manager manager = (Manager)me;

		Pageable pageable = PageRequest.of(page, size);

		Page<Matching> matchings = matchingRepository.findByManagerId(manager.getId(), pageable);

		return matchings.stream()
			.filter(
				matching -> matching.getMatchingStatus() != null && matching.getMatchingStatus().equals(Status.PENDING))
			.map(matching -> {
				Long reservationId = matching.getReservationId();
				Reservation reservation = reservationService.findById(reservationId);
				return new RequestMatchingListResponseDto(reservation);
			})
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
