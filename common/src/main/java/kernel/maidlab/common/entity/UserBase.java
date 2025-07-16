package kernel.maidlab.common.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.SocialType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserBase extends TimeBase {

	@Column(name = "uuid", nullable = false, unique = true)
	private String uuid;

	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	@Column(name = "password")
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "birth", nullable = false)
	private LocalDate birth;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@Column(name = "profile_image")
	private String profileImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type")
	private SocialType socialType;

	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	@Column(name = "emergency_call")
	private String emergencyCall;

	protected UserBase(String phoneNumber, String password, String name, Gender gender, LocalDate birth) {
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.isDeleted = false;
	}

	protected UserBase(String phoneNumber, String name, Gender gender, LocalDate birth, SocialType socialType) {
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.socialType = socialType;
		this.isDeleted = false;
	}

	@PrePersist
	public void generateUuid() {
		if (this.uuid == null) {
			this.uuid = UUID.randomUUID().toString();
		}
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void deleteAccount() {
		this.isDeleted = true;
	}

	public boolean isDeleted() {
		return this.isDeleted;
	}

	public boolean isSocialAccount() {
		return this.socialType != null;
	}

	/**
	 * 기본 정보 업데이트 (이름, 생년월일, 성별)
	 */
	public void updateBasicInfo(String name, LocalDate birth, Gender gender) {
		this.name = name;
		this.birth = birth;
		this.gender = gender;
	}

	/**
	 * 비상연락처 업데이트
	 */
	public void updateEmergencyCall(String emergencyCall) {
		this.emergencyCall = emergencyCall;
	}
}