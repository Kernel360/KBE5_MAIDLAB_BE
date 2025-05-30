package kernel.maidlab.api.auth.entity;

import jakarta.persistence.*;
import kernel.maidlab.api.consumer.entity.ManagerPreference;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.SocialType;
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
@Table(name = "consumer", indexes = {
	@Index(name = "idx_consumer_uuid", columnList = "uuid", unique = true),
	@Index(name = "idx_consumer_phone_number", columnList = "phone_number", unique = true)})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consumer extends Base {

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

	@Column(name = "address")
	private String address;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "point", nullable = false)
	private Integer point;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type")
	private SocialType socialType;

	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;

	@OneToMany
	private List<ManagerPreference> preferences = new ArrayList<>();

	private Consumer(String phoneNumber,String password, String name, Gender gender, LocalDate birth) {
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.point = 0;
		this.isDeleted = false;
	}

	public static Consumer createConsumer(String phoneNumber, String password, String name, Gender gender,
		LocalDate birth) {
		return new Consumer(phoneNumber, password, name, gender, birth);
	}

	public static Consumer createSocialConsumer(String phoneNumber, String password, String name, Gender gender,
		LocalDate birth, SocialType socialType) {
		Consumer consumer = new Consumer(phoneNumber, null, name, gender, birth);
		consumer.socialType = socialType;
		return consumer;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateProfile(String profileImage, String address, String detailAddress){
		this.profileImage = profileImage;
		this.address = address;
		this.detailAddress = detailAddress;
	}

	@PrePersist
	public void generateUuid() {
		if (this.uuid == null) {
			this.uuid = UUID.randomUUID().toString();
		}
	}
}
