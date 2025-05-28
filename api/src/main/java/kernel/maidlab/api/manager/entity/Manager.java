package kernel.maidlab.api.manager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Manager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @Column(name = "phone_number", length = 20, nullable = false)
	// private String phoneNumber;

	@Column(length = 100, nullable = false)
	private String password;

	@Column(length = 50, nullable = false)
	private String name;

	// @Column(nullable = false)
	// private LocalDateTime birth;
	//
	// @Enumerated(EnumType.STRING)
	// @Column(nullable = false)
	// private Gender gender;
	//
	// @Enumerated(EnumType.STRING)
	// private SocialType socialType;
	//
	// @Lob
	// private String refreshToken;
	//
	// @Lob
	// private String profileImage;
	//
	// @Column(length = 1000)
	// private String introduceText;
	//
	// @Column(name = "average_rate", precision = 2, scale = 1, nullable = false)
	// private BigDecimal averageRate;
	//
	// @Lob
	// private String bank;
	//
	// @Enumerated(EnumType.STRING)
	// @Column(name = "is_verified", nullable = false)
	// private VerificationStatus isVerified = VerificationStatus.PENDING;
	//
	// @Column(name = "created_at", nullable = false)
	// private LocalDateTime createdAt;
	//
	// @Column(name = "updated_at")
	// private LocalDateTime updatedAt;
	//
	// @Column(name = "is_deleted", nullable = false)
	// private Boolean isDeleted = false;
	//
	// public enum Gender {
	// 	MAN, WOMAN
	// }
	//
	// public enum SocialType {
	// 	GOOGLE, KAKAO
	// }
	//
	// public enum VerificationStatus {
	// 	PENDING, APPROVED, REJECTED
	// }
}
