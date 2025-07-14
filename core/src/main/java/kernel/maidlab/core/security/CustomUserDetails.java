package kernel.maidlab.core.security;

import kernel.maidlab.common.enums.UserType;
import lombok.Builder;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {

	private final String userId; // 사용자 고유 ID (consumerUuid, managerUuid, adminKey)
	private final UserType userType; // 사용자 타입 (권한 구분용)

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + userType.name()));
	}

	// JWT 방식에서는 비밀번호 사용 안함
	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userId; // username 으로 userId 사용
	}

	// JWT 기반에서는 토큰 자체가 유효성을 보장하므로 모두 true
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// 실제 사용하는 권한 검증 메서드들
	public boolean hasRole(UserType requiredType) {
		return this.userType == requiredType;
	}

	public boolean hasAnyRole(UserType... requiredTypes) {
		if (requiredTypes == null || requiredTypes.length == 0) {
			return true;
		}

		for (UserType type : requiredTypes) {
			if (this.userType == type) {
				return true;
			}
		}
		return false;
	}

	public boolean isAdmin() {
		return this.userType == UserType.ADMIN;
	}
}