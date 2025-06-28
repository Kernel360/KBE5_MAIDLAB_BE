package kernel.maidlab.common.entity.reservation;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.TimeBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_keyword")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewKeyword extends TimeBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 리뷰 연관관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;

	@Column(name = "keyword", nullable = false)
	private String keyword;

	public ReviewKeyword(Review review, String keyword) {
		this.review = review;
		this.keyword = keyword;
	}
}

