package kernel.maidlab.domain.manager.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
