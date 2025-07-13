package kernel.maidlab.api.matching.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.api.reservation.repository.ReservationRepository;
import kernel.maidlab.common.dto.consumer.response.LikedManagerResponseDto;
import kernel.maidlab.common.entity.base.UserBase;
import kernel.maidlab.common.entity.consumer.Consumer;
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
import kernel.maidlab.api.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {
	private final MatchingRepository matchingRepository;
	private final ManagerService managerService;
	private final ReservationRepository reservationRepository;
	private final ConsumerService consumerService;
	private final UserValidator userValidator;

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
		Matching savedMatching = matchingRepository.save(matching);
		log.info("매칭 생성 완료 - 매칭 ID: {}, 예약 ID: {}", savedMatching.getId(), dto.getReservationId());
	}

	@Transactional
	@Override
	public void changeStatus(Long reservationId, Status status) {
		Matching matching = matchingRepository.findByReservationId(reservationId);
		Status previousStatus = matching.getMatchingStatus();
		matching.setMatchingStatus(status);
		log.info("매칭 상태 변경 완료 - 예약 ID: {}, 이전 상태: {} -> 새 상태: {}", reservationId, previousStatus, status);
	}

	@Override
	public List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size) {

		String userId = AuthenticationHelper.getCurrentUserId();
		UserType userType = AuthenticationHelper.getCurrentUserType();
		UserBase me = userValidator.findByUuid(userId, userType);
		Manager manager = (Manager)me;

		Pageable pageable = PageRequest.of(page, size);

		return matchingRepository.findByManagerIdAndMatchingStatus(manager.getId(), Status.PENDING,
			pageable).stream().map(matching -> {
					Long reservationId = matching.getReservationId();
					Reservation reservation = reservationRepository.findById(reservationId)
						.orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + reservationId));
					return new RequestMatchingListResponseDto(reservation);
			}).toList();
	}

	@Override
	public List<LikedManagerResponseDto> preferenceManager(HttpServletRequest request) {
		return consumerService.getLikedManagerList();
	}

	@Override
	public List<AvailableManagerResponseDto> previousManager(Consumer consumer) {
		return managerService.previousManagers(consumer);
	}

	@Scheduled(fixedRate = 60000) // 1분마다 실행
	@Transactional
	public void rejectExpiredPendingMatching() {
		LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
		int updatedCount = matchingRepository.bulkExpirePendingMatching(
			Status.REJECTED,
			Status.PENDING,
			expiredTime
		);

		// Expired matching status updates are handled silently
	}

	private String extractGuFromAddress(String address) {
		// "구" 단위 추출 (예: "서울시 강남구 역삼동" -> "강남구")
		// 단위를 바꾸고 싶을때는 filter의 endsWith 만 바꾸면 됨
		if (address.startsWith("서"))
			return Arrays.stream(address.split(" "))
				.filter(s -> s.endsWith("구"))
				.findFirst()
				.orElseThrow(() -> new BaseException(ResponseType.WRONG_ADDRESS));
			//서울시가 아닌경우 시 단위로 나누게 함
		else
			return Arrays.stream(address.split(" "))
				.filter(s -> s.endsWith("시"))
				.findFirst()
				.orElseThrow(() -> new BaseException(ResponseType.WRONG_ADDRESS));
	}

}
