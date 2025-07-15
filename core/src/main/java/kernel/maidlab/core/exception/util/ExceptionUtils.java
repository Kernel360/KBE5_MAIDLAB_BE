package kernel.maidlab.core.exception.util;

import java.time.format.DateTimeFormatter;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.exception.BaseException;
import kernel.maidlab.core.exception.custom.AuthException;
import kernel.maidlab.core.exception.custom.PointException;
import kernel.maidlab.core.exception.custom.ReservationException;
public final class ExceptionUtils {

	private ExceptionUtils() {
		// 유틸리티 클래스이므로 인스턴스 생성 방지
	}

	// exception -> base exception
	public static BaseException convertToBaseException(Exception e, ResponseType defaultType) {
		if (e instanceof BaseException) {
			return (BaseException) e;
		}
		
		return new BaseException(defaultType, e.getMessage(), e);
	}

	// 예외 로그 변환
	public static String formatExceptionForLog(BaseException e) {
		StringBuilder sb = new StringBuilder();
		sb.append("Exception: ").append(e.getClass().getSimpleName())
		  .append(" | Type: ").append(e.getResponseType().name())
		  .append(" | Message: ").append(e.getDisplayMessage())
		  .append(" | Timestamp: ").append(e.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

		switch (e) {
			case AuthException authEx -> sb.append(" | UserId: ").append(authEx.getUserId())
			  .append(" | Action: ").append(authEx.getAttemptedAction())
			  .append(" | IP: ").append(authEx.getClientIp());
			case PointException pointEx -> sb.append(" | UserId: ").append(pointEx.getUserId())
			  .append(" | Requested: ").append(pointEx.getRequestedPoints())
			  .append(" | Available: ").append(pointEx.getAvailablePoints())
			  .append(" | Operation: ").append(pointEx.getOperation());
			case ReservationException reservationEx -> sb.append(" | ReservationId: ").append(reservationEx.getReservationId())
			  .append(" | UserId: ").append(reservationEx.getUserId())
			  .append(" | ServiceType: ").append(reservationEx.getServiceType())
			  .append(" | ConflictReason: ").append(reservationEx.getConflictReason());
			default -> {
				// 기본 BaseException인 경우 추가 정보 없음
			}
		}

		return sb.toString();
	}

	// 에러메세지 분리
	public static String getEnvironmentAwareMessage(BaseException e, boolean isProduction) {
		if (isProduction) {
			// 운영 환경에서는 민감한 정보 제외
			return e.getResponseType().getMessage();
		} else {
			// 개발 환경에서는 상세 정보 포함
			return formatExceptionForLog(e);
		}
	}

	// 예외 체인 분석
	public static String analyzeExceptionChain(Throwable e) {
		StringBuilder sb = new StringBuilder();
		Throwable current = e;
		int depth = 0;

		while (current != null && depth < 10) { // 무한 루프 방지
			sb.append("  ".repeat(depth))
			  .append(current.getClass().getSimpleName())
			  .append(": ")
			  .append(current.getMessage())
			  .append("\n");
			
			current = current.getCause();
			depth++;
		}

		return sb.toString();
	}
}