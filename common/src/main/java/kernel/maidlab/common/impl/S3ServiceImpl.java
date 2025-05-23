package kernel.maidlab.common.impl;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kernel.maidlab.common.dto.PresignedFileResponse;
import kernel.maidlab.common.service.S3Service;
import lombok.RequiredArgsConstructor;
// import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
	// private final S3Client s3Client; 백엔드에서 파일 전송할 때 필요

	private final S3Presigner s3Presigner;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	// 백엔드에서 직접 파일 보내는 함수
	// public String uploadFile(String key, MultipartFile file) throws IOException {
	// 	String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	//
	// 	PutObjectRequest request = PutObjectRequest.builder()
	// 		.bucket(bucketName)
	// 		.key(key + filename)
	// 		//.contentType(file.getContentType())
	// 		.build();
	//
	// 	s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
	//
	// 	return filename;
	// }

	//presigned url 이용하여 전송하는 함수
	@Override
	public List<PresignedFileResponse> uploadFile(List<String> filenames, String prefix) {
		return filenames.stream().map(filename -> {
			String key = prefix + UUID.randomUUID() + "_" + filename;

			PutObjectRequest objectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.contentType("application/octet-stream")
				.build();

			PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
				.putObjectRequest(objectRequest)
				.signatureDuration(Duration.ofMinutes(10))
				.build();

			PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

			return new PresignedFileResponse(key, presignedRequest.url().toString());
		}).toList();
	}
}
