package kernel.maidlab.core.aws.service;

import kernel.maidlab.core.aws.dto.PresignedFileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

	private final S3Presigner s3Presigner;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Override
	public List<PresignedFileResponseDto> uploadFile(List<String> filenames, String prefix) {
		return filenames.stream().map(filename -> {
			String key = prefix + "/" + UUID.randomUUID() + "_" + filename;

			String contentType = getContentType(filename);

			PutObjectRequest objectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.contentType(contentType)
				.build();

			PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
				.putObjectRequest(objectRequest)
				.signatureDuration(Duration.ofMinutes(15)) // 15분으로 증가
				.build();

			PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
			log.info("Presigned URL 생성 완료 - 파일명: {}, URL 길이: {}", filename, presignedRequest.url().toString().length());

			return new PresignedFileResponseDto(key, presignedRequest.url().toString());
		}).toList();
	}

	// 파일 확장자에 따른 Content-Type 결정
	private String getContentType(String filename) {
		String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

		return switch (extension) {
			case "jpg", "jpeg" -> "image/jpeg";
			case "png" -> "image/png";
			case "gif" -> "image/gif";
			case "pdf" -> "application/pdf";
			case "doc" -> "application/msword";
			case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			case "webp" -> "image/webp";
			default -> "application/octet-stream";
		};
	}
}
