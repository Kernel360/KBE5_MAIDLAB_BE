package kernel.maidlab.admin.event.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends Base {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = false)
	private Admin admin;

	@Column(name = "title")
	private String title;

	@Column(name = "main_image_url")
	private String mainImageUrl;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@CreationTimestamp
	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	@UpdateTimestamp
	@Column(name = "update_at", nullable = false)
	private LocalDateTime updateAt;

	private Event(Admin admin, String title,String mainImageUrl, String imageUrl, String content) {
		this.admin = admin;
		this.title = title;
		this.mainImageUrl = mainImageUrl;
		this.imageUrl = imageUrl;
		this.content = content;
	}

	public static Event createEvent(Admin admin, String title, String mainImageUrl, String imageUrl, String content) {
		return new Event(admin, title, mainImageUrl, imageUrl, content);
	}

	public void updateEvent(String title,String mainImageUrl, String imageUrl, String content) {
		this.title = title;
		this.mainImageUrl = mainImageUrl;
		this.imageUrl = imageUrl;
		this.content = content;
	}
}
