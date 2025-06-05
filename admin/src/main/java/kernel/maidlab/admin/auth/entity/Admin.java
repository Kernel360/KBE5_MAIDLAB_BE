package kernel.maidlab.admin.auth.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends Base {

	@Column(name = "admin_key", unique = true)
	private String adminKey;

	@Column(name = "password")
	private String password;

	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
