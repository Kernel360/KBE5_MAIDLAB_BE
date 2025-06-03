package kernel.maidlab.api.auth.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.Region;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "manager", indexes = {
	@Index(name = "idx_manager_uuid", columnList = "uuid", unique = true),
	@Index(name = "idx_manager_phone_number", columnList = "phone_number", unique = true)})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@Column(name = "social_type")
	private SocialType socialType;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "introduce_text")
	private String introduceText;

	@Column(name = "average_rate")
	private Float averageRate;

	@Column(name = "total_reviewed_cnt")
	private Long totalReviewedCnt;

	@Column(name = "bank")
	private String bank;

	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;

	@Column(name = "is_verified", nullable = false)
	private Status isVerified;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	@ElementCollection(targetClass = Region.class, fetch = FetchType.LAZY)
	@CollectionTable(
		name = "manager_region",
		joinColumns = @JoinColumn(name = "manager_id")
	)
	@Column(name = "region", nullable = false)
	@Enumerated(EnumType.STRING)
	private List<Region> regions = new ArrayList<>();

	private Manager(String phoneNumber, String password, String name, Gender gender, LocalDate birth) {
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.averageRate = 0.0F;
		this.totalReviewedCnt = 0L;
		this.isVerified = Status.PENDING;
		this.isDeleted = false;
	}

	public static Manager createManager(String phoneNumber, String password, String name, Gender gender,
		LocalDate birth) {
		return new Manager(phoneNumber, password, name, gender, birth);
	}

	public static Manager createSocialManager(String phoneNumber, String name, Gender gender,
		LocalDate birth, SocialType socialType) {
		Manager manager = new Manager(phoneNumber, null, name, gender, birth);
		manager.socialType = socialType;
		return manager;
	}

	public void updateAverageRate(Float averageRate) {
		this.averageRate = averageRate;
		this.totalReviewedCnt += 1;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void deleteAccount() {
		this.isDeleted = true;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void updateIntroduceText(String introduceText) {
		this.introduceText = introduceText;
	}

	public void updateBasicInfo(String name, LocalDate birth, Gender gender) {
		this.name = name;
		this.birth = birth;
		this.gender = gender;
	}

	@PrePersist
	public void generateUuid() {
		if (this.uuid == null) {
			this.uuid = UUID.randomUUID().toString();
		}
	}

	@Override
	public Long getId() {
		return this.id;
	}

}

