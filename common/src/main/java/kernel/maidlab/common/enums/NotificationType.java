package kernel.maidlab.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    
    // Manager용 알림
    MATCHING_REQUEST("매칭 신청", "새로운 매칭 신청이 들어왔습니다."),
    RESERVATION_REMINDER("예약 알림", "예약된 서비스 시간이 다가왔습니다."),
    RESERVATION_CANCELLED("예약 취소", "예약이 취소되었습니다."),
    PAYMENT_CONFIRMED("결제 확인", "매칭된 예약의 결제가 확인되었습니다."),

    // Consumer용 알림
    MATCHING_APPROVED("매칭 승인", "매칭 신청이 승인되었습니다."),
    MATCHING_REJECTED("매칭 거절", "매칭 신청이 거절되었습니다."),
    MATCHING_EXPIRED("매칭 만료", "매칭 신청이 만료되었습니다."),
    PAYMENT_REMINDER("결제 알림", "결제가 필요한 예약이 있습니다."),
    SERVICE_CHECKIN("체크인", "매칭 된 매니저가 작업을 시작했습니다."),
    SERVICE_COMPLETED("서비스 완료", "서비스가 완료되었습니다."),
    
    // 공통 알림
    REVIEW_REQUEST("리뷰 요청", "서비스에 대한 리뷰를 남겨주세요."),
    SYSTEM_NOTICE("시스템 공지", "시스템 공지사항이 있습니다."),
    POINT_EARNED("포인트 적립", "포인트가 적립되었습니다."),
    POINT_USED("포인트 사용", "포인트가 사용되었습니다."),
    
    // 관리자용 알림
    ADMIN_NOTICE("관리자 공지", "관리자 공지사항이 있습니다.");

    
    private final String title;
    private final String defaultMessage;
    
    public boolean isForManager() {
        return this == MATCHING_REQUEST || this == RESERVATION_REMINDER || this == RESERVATION_CANCELLED;
    }
    
    public boolean isForConsumer() {
        return this == MATCHING_APPROVED || this == MATCHING_REJECTED || this == MATCHING_EXPIRED 
               || this == PAYMENT_REMINDER || this == SERVICE_COMPLETED || this == SERVICE_CHECKIN;
    }
    
    public boolean isForBoth() {
        return this == REVIEW_REQUEST || this == SYSTEM_NOTICE || this == POINT_EARNED 
               || this == POINT_USED || this == ADMIN_NOTICE;
    }
}
