package kernel.maidlab.admin.auth.util;

import org.springframework.stereotype.Component;

import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserValidator {

	private final AdminRepository adminRepository;

	// adminKey로 관리자 조회
	public Admin findByAdminKey(String adminKey) {
		return adminRepository.findByAdminKey(adminKey)
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));
	}

	// 관리자 탈퇴 여부 검증
	public void validateAdminStatus(Admin admin) {
		if (admin.getIsDeleted()) {
			log.warn("탈퇴한 관리자 계정 접근 시도 - ID: {}", admin.getId());
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}
	}

	// 관리자 키 추출
	public String getAdminKey(Admin admin) {
		return admin.getAdminKey();
	}

	// 관리자 ID 추출
	public Long getAdminId(Admin admin) {
		return admin.getId();
	}

	// 관리자 이름 (항상 "관리자" 반환)
	public String getAdminName(Admin admin) {
		return "관리자";
	}
}