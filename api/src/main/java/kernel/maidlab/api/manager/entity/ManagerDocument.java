package kernel.maidlab.api.manager.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerDocument extends Base {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	@Column(name = "file_type", nullable = false)
	private String fileType;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "uploaded_file_url", nullable = false)
	private String uploadedFileUrl;

	@CreationTimestamp
	@Column(name = "upload_date", nullable = false)
	private LocalDateTime uploadDate;

	private ManagerDocument(Manager manager, String fileType, String fileName, String uploadedFileUrl) {
		this.manager = manager;
		this.fileType = fileType;
		this.fileName = fileName;
		this.uploadedFileUrl = uploadedFileUrl;
	}

	public static ManagerDocument managerDocument(Manager manager, String fileType, String fileName,
		String uploadedFileUrl) {
		return new ManagerDocument(manager, fileType, fileName, uploadedFileUrl);
	}
}
