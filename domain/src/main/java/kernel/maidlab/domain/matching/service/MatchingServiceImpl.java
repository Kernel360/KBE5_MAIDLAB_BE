package kernel.maidlab.domain.matching.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Fallback;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.core.exception.BaseException;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.domain.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.consumer.service.ConsumerService;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.manager.repository.ManagerRepository;
import kernel.maidlab.domain.manager.service.ManagerService;
import kernel.maidlab.domain.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.domain.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.domain.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.domain.matching.dto.response.RequestMatchingListResponseDto;
import kernel.maidlab.domain.matching.entity.Matching;
import kernel.maidlab.domain.matching.repository.MatchingRepository;
import kernel.maidlab.domain.notification.dto.NotificationDto;
import kernel.maidlab.domain.notification.service.NotificationService;
import kernel.maidlab.domain.reservation.entity.Reservation;
import kernel.maidlab.domain.reservation.repository.ReservationRepository;
import kernel.maidlab.domain.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {
    private final MatchingRepository matchingRepository;
    private final ManagerService managerService;
    private final ReservationRepository reservationRepository;
    private final ConsumerService consumerService;
    private final NotificationService notificationService;
    private final ManagerRepository managerRepository;
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
        log.info("test");

        Matching savedMatching = matchingRepository.save(matching);
        log.info("매칭 생성 완료 - 매칭 ID: {}, 예약 ID: {}", savedMatching.getId(), dto.getReservationId());

        // 매니저에게 매칭 신청 알림 전송
        sendMatchingNotification(savedMatching);

    }

    @Transactional
    @Override
    public void changeStatus(Long reservationId, Status status) {
        Matching matching = matchingRepository.findByReservationId(reservationId);
        Status previousStatus = matching.getMatchingStatus();
        matching.setMatchingStatus(status);
        sendStatusNotification(matching, status);
        log.info("매칭 상태 변경 완료 - 예약 ID: {}, 이전 상태: {} -> 새 상태: {}", reservationId, previousStatus, status);
    }

    @Override
    public List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size) {

        String userId = AuthenticationHelper.getCurrentUserKey();
        UserType userType = AuthenticationHelper.getCurrentUserType();
        Manager me = userValidator.findByUuid(userId, userType);

        Pageable pageable = PageRequest.of(page, size);

        return matchingRepository.findByManagerIdAndMatchingStatus(me.getId(), Status.PENDING,
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

        // Find expired pending matchings first
        List<Matching> expiredMatchings = matchingRepository.findByMatchingStatusAndUpdatedAtBefore(Status.PENDING,
                expiredTime);

        // Update each matching individually to increment count and change status
        for (Matching matching : expiredMatchings) {
            Integer currentCount = matching.getMatchingCount();
            matching.setMatchingCount(currentCount != null ? currentCount + 1 : 1);
            matching.setMatchingStatus(Status.REJECTED);
            matchingRepository.save(matching);
            log.info("매칭 만료 처리 - 예약 ID: {}, 매칭 시도 횟수: {}", matching.getReservationId(), matching.getMatchingCount());
        }

        // Handle matchings that have reached maximum count (4 attempts)
        List<Matching> maxCountMatchings = matchingRepository.findByMatchingCountGreaterThanEqualOrderByUpdatedAtDesc(
                4);
        for (Matching matching : maxCountMatchings) {
            // Cancel the reservation
            Reservation reservation = reservationRepository.findById(matching.getReservationId())
                    .orElse(null);
            if (reservation != null) {
                reservation.cancel(LocalDateTime.now());
                reservationRepository.save(reservation);
                log.info("예약 취소 완료 - 예약 ID: {}, 매칭 시도 횟수: {}", matching.getReservationId(),
                        matching.getMatchingCount());
            }

            // Delete the matching
            matchingRepository.delete(matching);
            log.info("매칭 삭제 완료 - 매칭 ID: {}, 예약 ID: {}", matching.getId(), matching.getReservationId());
        }
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

    //매칭 신청 알림 전송
    @Fallback(
            method = "logMatchingNotificationFailure",
            exceptions = {Exception.class}
    )
    @ExceptionHandler(
            value = {Exception.class},
            responseType = ResponseType.INTERNAL_SERVER_ERROR,
            message = "매칭 알림 전송에 실패했습니다",
            logLevel = LogLevel.ERROR,
            enableNotification = true
    )
    private void sendMatchingNotification(Matching matching) {
        // 예약 정보 조회
        Reservation reservation = reservationRepository.findById(matching.getReservationId())
                .orElseThrow(
                        () -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + matching.getReservationId()));

        // 소비자 정보 조회
        Consumer consumer = consumerService.findById(reservation.getConsumerId());

        // 서비스 타입 정보
        String serviceType = reservation.getServiceDetailType().getServiceDetailType();

        // 알림생성
        NotificationDto notification = notificationService.createMatchingNotification(
                matching.getManagerId(),
                reservation.getId(),
                consumer.getName(),
                serviceType
        );
        log.info("매칭 알림 생성 완료 - 매니저 ID: {}, 예약 ID: {}",
                notification.getReceiverId(), notification.getRelatedId());

        // DB 저장 및 실시간 전송
        notificationService.sendNotification(notification);
    }

    //매칭 승인/거절 알림 전송
    @Fallback(
            method = "logStatusNotificationFailure",
            exceptions = {Exception.class}
    )
    @ExceptionHandler(
            value = {Exception.class},
            responseType = ResponseType.INTERNAL_SERVER_ERROR,
            message = "상태 알림 전송에 실패했습니다",
            logLevel = LogLevel.ERROR,
            enableNotification = true
    )
    private void sendStatusNotification(Matching matching, Status status) {
        // 예약 정보 조회
        Reservation reservation = reservationRepository.findById(matching.getReservationId())
                .orElseThrow(
                        () -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. ID: " + matching.getReservationId()));

        // 소비자, 매니저 정보 조회
        Consumer consumer = consumerService.findById(reservation.getConsumerId());
        Manager manager = managerService.findById(reservation.getManagerId());
        // 서비스 타입 정보
        String serviceType = reservation.getServiceDetailType().getServiceDetailType();

        // 새로운 알림 시스템 사용
        NotificationDto notification = notificationService.createMatchingStatusNotification(
                consumer.getId(),
                matching.getId(),
                manager.getName(),
                status
        );

        // DB 저장 및 실시간 전송
        notificationService.sendNotification(notification);
    }

    private void logMatchingNotificationFailure(Matching matching) {
        log.error("매칭 알림 전송 실패로 인한 대체 처리 - 매칭 ID: {}", matching.getId());
    }

    private void logStatusNotificationFailure(Matching matching, Status status) {
        log.error("상태 알림 전송 실패로 인한 대체 처리 - 매칭 ID: {}", matching.getId());
    }

}
