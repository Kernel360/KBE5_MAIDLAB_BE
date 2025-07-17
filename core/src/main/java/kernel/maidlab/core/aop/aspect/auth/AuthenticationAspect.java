package kernel.maidlab.core.aop.aspect.auth;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import kernel.maidlab.core.exception.custom.AuthException;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.core.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AuthenticationAspect {

	// @AuthRequired 어노테이션 권한 확인
	@Around("@annotation(authRequired)")
	public Object authenticateUser(ProceedingJoinPoint joinPoint, AuthRequired authRequired) throws Throwable {

		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();

		log.debug("인증 체크 시작 - {}#{}", className, methodName);

		try {
			if (!authRequired.requireToken()) {
				log.debug("토큰 검증 불필요 - {}#{}", className, methodName);
				return joinPoint.proceed();
			}

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated()) {
				log.warn("인증되지 않은 사용자 접근 시도 - {}#{}", className, methodName);
				throw AuthException.unauthorized("인증되지 않은 사용자의 " + methodName + " 접근 시도");
			}

			if (authentication.getPrincipal().equals("anonymousUser")) {
				log.warn("익명 사용자 접근 시도 - {}#{}", className, methodName);
				throw AuthException.unauthorized("익명 사용자의 " + methodName + " 접근 시도");
			}

			if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
				log.error("사용자 인증: Principal 객체가 CustomUserDetails 타입이 아닙니다. - {}#{}", className, methodName);
				throw AuthException.unauthorized("잘못된 사용자 인증 객체 타입: " + methodName);
			}

			if (authRequired.roles().length > 0) {
				boolean hasRole = userDetails.hasAnyRole(authRequired.roles());

				if (!hasRole) {
					log.warn("권한 부족 - 사용자: {}, 필요 권한: {}, 실제 권한: {} - {}#{}",
						userDetails.getUsername(),
						Arrays.toString(authRequired.roles()),
						userDetails.getUserType(),
						className, methodName);

					throw AuthException.forbidden(userDetails.getUserKey(), methodName);
				}
			}

			log.debug("인증 체크 성공 - 사용자: {}, 권한: {} - {}#{}",
				userDetails.getUsername(),
				userDetails.getUserType(),
				className, methodName);

			return joinPoint.proceed();

		} catch (AuthException e) {
			throw e;
		} catch (EntityNotFoundException | AccessDeniedException e) {
			log.debug("사용자 비즈니스 예외 발생 - {}#{}: {}", className, methodName, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("인증 체크 중 예상치 못한 오류 발생 - {}#{}", className, methodName, e);
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED, "인증 체크 중 예상치 못한 오류 발생: " + methodName, e);
		}
	}

	@Around("@annotation(kernel.maidlab.core.aop.annotation.auth.AdminRequired)")
	public Object authorizeAdmin(ProceedingJoinPoint joinPoint) throws Throwable {

		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();

		log.debug("관리자 권한 체크 시작 - {}#{}", className, methodName);

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated()) {
				log.warn("인증되지 않은 사용자의 관리자 권한 접근 시도 - {}#{}", className, methodName);
				throw AuthException.unauthorized("인증되지 않은 사용자의 관리자 권한 접근 시도: " + methodName);
			}

			if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
				log.error("관리자 인증: Principal 객체가 CustomUserDetails 타입이 아닙니다. - {}#{}", className, methodName);
				throw AuthException.unauthorized("잘못된 관리자 인증 객체 타입: " + methodName);
			}

			if (!userDetails.isAdmin()) {
				log.warn("관리자 권한 부족 - 사용자: {}, 권한: {} - {}#{}",
					userDetails.getUsername(),
					userDetails.getUserType(),
					className, methodName);

				throw AuthException.forbidden(userDetails.getUserKey(), methodName);
			}

			log.debug("관리자 권한 체크 성공 - 사용자: {} - {}#{}",
				userDetails.getUsername(),
				className, methodName);

			return joinPoint.proceed();

		} catch (AuthException e) {
			throw e;
		} catch (EntityNotFoundException | AccessDeniedException e) {
			log.debug("관리자 비즈니스 예외 발생 - {}#{}: {}", className, methodName, e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("관리자 권한 체크 중 예상치 못한 오류 발생 - {}#{}", className, methodName, e);
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED, "관리자 권한 체크 중 예상치 못한 오류 발생: " + methodName, e);
		}
	}

	// 인증된 사용자 정보 반환
	public static CustomUserDetails getCurrentUser() {
		return AuthenticationHelper.getCurrentUser();
	}

	// 관리자 여부 확인
	public static boolean isAdmin() {
		return AuthenticationHelper.isAdmin();
	}
}