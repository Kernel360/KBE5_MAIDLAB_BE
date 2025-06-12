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
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.matching.dto.response.RequestMatchingListResponseDto;
import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.api.reservation.repository.ReservationRepository;
import kernel.maidlab.api.util.AuthUtil;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.api.matching.entity.Matching;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;

@Service
public class MatchingServiceImpl implements MatchingService {
	private final ReservationRepository reservationRepository;
	private final ManagerRepository managerRepository;
	private final MatchingRepository matchingRepository;
	private final AuthUtil authUtil;

	public MatchingServiceImpl(ReservationRepository reservationRepository, ManagerRepository managerRepository,
		MatchingRepository matchingRepository, AuthUtil authUtil) {
		this.reservationRepository = reservationRepository;
		this.managerRepository = managerRepository;
		this.matchingRepository = matchingRepository;
		this.authUtil = authUtil;
	}

	@Override
	public List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto) {
		LocalDateTime StartTime = LocalDateTime.parse(dto.getStartTime());
		LocalDateTime EndTime = LocalDateTime.parse(dto.getEndTime());
		String gu = extractGuFromAddress(dto.getAddress());

		return managerRepository.findAvailableManagers(gu, StartTime, EndTime);
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
	public void changeManager(Long reservationId, Long managerId) {
		Matching matching = matchingRepository.findByReservationId(reservationId);
		matching.setManagerId(managerId);
		matching.setMatchingStatus(Status.PENDING);
	}

	@Override
	public List<MatchingResponseDto> allMatching(HttpServletRequest request, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Matching> matchings = matchingRepository.findAll(pageable);
		return matchings.stream()
			.map(matching -> MatchingResponseDto.builder()
				.reservationId(matching.getReservationId())
				.managerId(matching.getManagerId())
				.matchingStatus(matching.getMatchingStatus())
				.build())
			.toList();
	}

	@Override
	public List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size) {
		Manager me = authUtil.getManager(request);
		Pageable pageable = PageRequest.of(page, size);

		Page<Matching> matchings = matchingRepository.findByManagerId(me.getId(), pageable);

		return matchings.stream()
			.filter(
				matching -> matching.getMatchingStatus() != null && matching.getMatchingStatus().equals(Status.PENDING))
			.map(matching -> {
				Long reservationId = matching.getReservationId();
				Reservation reservation = reservationRepository.findById(reservationId)
					.orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + reservationId));

				return new RequestMatchingListResponseDto(reservation);
			})
			.toList();
	}

	@Override
	public List<MatchingResponseDto> statusMatching(Status status, int page, int size) {
		Page<Matching> matchings;
		Pageable pageable = PageRequest.of(page, size);
		matchings = matchingRepository.findAllByMatchingStatus(status, pageable);
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
