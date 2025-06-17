package kernel.maidlab.common.entity.event;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kernel.maidlab.common.entity.base.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends Base {

	@Column(name = "admin_id")
	private Long admin;

	@Column(name = "title")
	private String title;

	@Column(name = "main_image_url")
	private String mainImageUrl;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	private Event(Long admin, String title,String mainImageUrl, String imageUrl, String content) {
		this.admin = admin;
		this.title = title;
		this.mainImageUrl = mainImageUrl;
		this.imageUrl = imageUrl;
		this.content = content;
	}

	public static Event createEvent(Long adminId, String title, String mainImageUrl, String imageUrl, String content) {
		return new Event(adminId, title, mainImageUrl, imageUrl, content);
	}

	public void updateEvent(String title,String mainImageUrl, String imageUrl, String content) {
		this.title = title;
		this.mainImageUrl = mainImageUrl;
		this.imageUrl = imageUrl;
		this.content = content;
	}
}
