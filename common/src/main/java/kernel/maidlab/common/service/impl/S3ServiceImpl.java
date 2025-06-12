package kernel.maidlab.common.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kernel.maidlab.common.dto.PresignedFileResponseDto;
import kernel.maidlab.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

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
			default -> "application/octet-stream";
		};
	}
}