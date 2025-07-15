package kernel.maidlab.api.util;

import kernel.maidlab.api.consumer.entity.Consumer;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.manager.entity.Manager;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

	private final ConsumerRepository consumerRepository;
	private final ManagerRepository managerRepository;
	private final PasswordEncoder passwordEncoder;

	// 휴대폰 중복검사
	public void validatePhoneNumberDuplication(String phoneNumber, UserType userType) {
		boolean isDuplicate = switch (userType) {
			case CONSUMER -> consumerRepository.findByPhoneNumber(phoneNumber).isPresent();
			case MANAGER -> managerRepository.findByPhoneNumber(phoneNumber).isPresent();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입: " + userType);
		};

		if (isDuplicate) {
			log.warn("{} 휴대폰 번호 중복: {}", userType.getName(), phoneNumber);
			throw new BaseException(ResponseType.DUPLICATE_TEL_NUMBER);
		}
	}

	// 로그인 검증
	public <T> T validateLoginCredentials(String phoneNumber, String password, UserType userType) {
		return switch (userType) {
			case CONSUMER -> {
				Consumer consumer = consumerRepository.findByPhoneNumber(phoneNumber)
					.orElseThrow(() -> new BaseException(ResponseType.LOGIN_FAILED));

				validateUserStatus(consumer, userType);
				validatePassword(password, consumer.getPassword(), consumer.getId(), userType);

				yield (T)consumer;
			}
			case MANAGER -> {
				Manager manager = managerRepository.findByPhoneNumber(phoneNumber)
					.orElseThrow(() -> new BaseException(ResponseType.LOGIN_FAILED));

				validateUserStatus(manager, userType);
				validatePassword(password, manager.getPassword(), manager.getId(), userType);

				yield (T)manager;
			}
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입: " + userType);
		};
	}

	// 소셜 사용자 조회
	public <T> Optional<T> findBySocialId(String socialId, UserType userType) {
		return switch (userType) {
			case CONSUMER -> consumerRepository.findByPhoneNumber(socialId).map(consumer -> (T)consumer);
			case MANAGER -> managerRepository.findByPhoneNumber(socialId).map(manager -> (T)manager);
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입: " + userType);
		};
	}

	// uuid 사용자 조회 (Consumer, Manager만 지원)
	public <T> T findByUuid(String uuid, UserType userType) {
		return switch (userType) {
			case CONSUMER -> {
				Consumer consumer = consumerRepository.findByUuid(uuid)
					.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));
				yield (T)consumer;
			}
			case MANAGER -> {
				Manager manager = managerRepository.findByUuid(uuid)
					.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));
				yield (T)manager;
			}
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입: " + userType);
		};
	}

	// 탈퇴 여부
	private void validateUserStatus(Object user, UserType userType) {
		boolean isDeleted = switch (user) {
			case Consumer consumer -> consumer.getIsDeleted();
			case Manager manager -> manager.getIsDeleted();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 객체 타입");
		};

		if (isDeleted) {
			Long userId = switch (user) {
				case Consumer consumer -> consumer.getId();
				case Manager manager -> manager.getId();
				default -> null;
			};
			log.warn("탈퇴한 {} 계정 로그인 시도 - ID: {}", userType.getName(), userId);
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}
	}

	// 비밀번호 검증
	private void validatePassword(String inputPassword, String storedPassword, Long userId, UserType userType) {
		if (!passwordEncoder.matches(inputPassword, storedPassword)) {
			log.warn("{} 로그인 실패 - 잘못된 비밀번호, ID: {}", userType.getName(), userId);
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}
	}

	// 소셜 비밀번호 검증
	public void validateSocialAccountPasswordChange(Object user, UserType userType) {
		Object socialType = switch (user) {
			case Consumer consumer -> consumer.getSocialType();
			case Manager manager -> manager.getSocialType();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 객체 타입");
		};

		if (socialType != null) {
			Long userId = switch (user) {
				case Consumer consumer -> consumer.getId();
				case Manager manager -> manager.getId();
				default -> null;
			};
			log.warn("소셜 계정 비밀번호 변경 시도 - ID: {}, 소셜 타입: {}", userId, socialType);
			throw new BaseException(ResponseType.VALIDATION_FAILED);
		}
	}

	// 프로필 완성 여부
	public boolean hasCompleteProfile(Object user) {
		return switch (user) {
			case Consumer consumer -> consumer.hasCompleteProfile();
			case Manager manager -> manager.hasCompleteProfile();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 객체 타입");
		};
	}

	// 사용자 uuid
	public String getUserUuid(Object user) {
		return switch (user) {
			case Consumer consumer -> consumer.getUuid();
			case Manager manager -> manager.getUuid();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 객체 타입");
		};
	}

	// 사용자 pk 추출
	public Long getUserId(Object user) {
		return switch (user) {
			case Consumer consumer -> consumer.getId();
			case Manager manager -> manager.getId();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 객체 타입");
		};
	}

	// 사용자 이름 추출
	public String getUserName(Object user) {
		return switch (user) {
			case Consumer consumer -> consumer.getName();
			case Manager manager -> manager.getName();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 객체 타입");
		};
	}
}