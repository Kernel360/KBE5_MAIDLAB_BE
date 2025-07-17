package kernel.maidlab.domain.manager.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.UserBase;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.Region;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "manager", indexes = {
        @Index(name = "idx_manager_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_manager_phone_number", columnList = "phone_number", unique = true)})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends UserBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "introduce_text")
    private String introduceText;

    @Column(name = "average_rate")
    private Float averageRate;

    @Column(name = "total_reviewed_cnt")
    private Long totalReviewedCnt;

    @Column(name = "bank")
    private String bank;

    @Column(name = "is_verified", nullable = false)
    private Status isVerified;

    @ElementCollection(targetClass = Region.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "manager_region",
            joinColumns = @JoinColumn(name = "manager_id")
    )
    @Column(name = "region_id", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private List<Region> regions = new ArrayList<>();

    private Manager(String phoneNumber, String password, String name, Gender gender, LocalDate birth) {
        super(phoneNumber, password, name, gender, birth);
        this.averageRate = 0.0F;
        this.totalReviewedCnt = 0L;
        this.isVerified = Status.PENDING;
    }

    public static Manager createManager(String phoneNumber, String password, String name, Gender gender,
                                        LocalDate birth) {
        return new Manager(phoneNumber, password, name, gender, birth);
    }

    public static Manager createSocialManager(String phoneNumber, String name, Gender gender,
                                              LocalDate birth, SocialType socialType) {
        Manager manager = new Manager(phoneNumber, name, gender, birth, socialType);
        return manager;
    }

    private Manager(String phoneNumber, String name, Gender gender, LocalDate birth, SocialType socialType) {
        super(phoneNumber, name, gender, birth, socialType);
        this.averageRate = 0.0F;
        this.totalReviewedCnt = 0L;
        this.isVerified = Status.PENDING;
    }

    public void updateAverageRate(Float averageRate) {
        this.averageRate = averageRate;
        this.totalReviewedCnt += 1;
    }

    public void updateIntroduceText(String introduceText) {
        this.introduceText = introduceText;
    }

    public void updateBasicInfo(String name, LocalDate birth, Gender gender) {
        super.updateBasicInfo(name, birth, gender);
    }

    public boolean hasCompleteProfile() {
        return super.getProfileImage() != null && !super.getProfileImage().trim().isEmpty() && this.regions != null
                && !this.regions.isEmpty();
    }

    public void approve() {
        this.isVerified = Status.APPROVED;
    }

    public void reject() {
        this.isVerified = Status.REJECTED;
    }
}

