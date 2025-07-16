package kernel.maidlab.domain.consumer.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import kernel.maidlab.common.entity.UserBase;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileUpdateRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consumer", indexes = {
	@Index(name = "idx_consumer_uuid", columnList = "uuid", unique = true),
	@Index(name = "idx_consumer_phone_number", columnList = "phone_number", unique = true)})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consumer extends UserBase {

	@Column(name = "address")
	private String address;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "point", nullable = false)
	private Integer point;

	@Column(name = "average_rate")
	private Float averageRate;

	@Column(name = "total_reviewed_cnt")
	private Long totalReviewedCnt;

	private Consumer(String phoneNumber, String password, String name, Gender gender, LocalDate birth) {
		super(phoneNumber, password, name, gender, birth);
		this.point = 0;
		this.averageRate = 0.0F;
		this.totalReviewedCnt = 0L;
	}

	public static Consumer createConsumer(String phoneNumber, String password, String name, Gender gender,
		LocalDate birth) {
		return new Consumer(phoneNumber, password, name, gender, birth);
	}

	public static Consumer createSocialConsumer(String phoneNumber, String name, Gender gender,
		LocalDate birth, SocialType socialType) {
		Consumer consumer = new Consumer(phoneNumber, null, name, gender, birth);
		return createSocialConsumerWithSocialType(phoneNumber, name, gender, birth, socialType);
	}

	private static Consumer createSocialConsumerWithSocialType(String phoneNumber, String name, Gender gender,
		LocalDate birth, SocialType socialType) {
		Consumer consumer = new Consumer(phoneNumber, name, gender, birth, socialType);
		return consumer;
	}

	private Consumer(String phoneNumber, String name, Gender gender, LocalDate birth, SocialType socialType) {
		super(phoneNumber, name, gender, birth, socialType);
		this.point = 0;
		this.averageRate = 0.0F;
		this.totalReviewedCnt = 0L;
	}

	public void updateAverageRate(Float averageRate) {
		this.averageRate = averageRate;
		this.totalReviewedCnt += 1;
	}

	public void createProfile(ConsumerProfileRequestDto consumerProfileRequestDto) {
		super.updateProfileImage(consumerProfileRequestDto.getProfileImage());
		this.address = consumerProfileRequestDto.getAddress();
		this.detailAddress = consumerProfileRequestDto.getDetailAddress();
	}

	public void updateProfile(ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto) {
		super.updateProfileImage(consumerProfileUpdateRequestDto.getProfileImage());
		super.updateBasicInfo(
			consumerProfileUpdateRequestDto.getName(),
			consumerProfileUpdateRequestDto.getBirth(),
			consumerProfileUpdateRequestDto.getGender()
		);
		this.address = consumerProfileUpdateRequestDto.getAddress();
		this.detailAddress = consumerProfileUpdateRequestDto.getDetailAddress();
	}

	public boolean hasCompleteProfile() {
		return this.address != null && !this.address.trim().isEmpty();
	}
}
