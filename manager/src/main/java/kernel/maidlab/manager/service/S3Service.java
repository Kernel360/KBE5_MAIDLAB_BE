package kernel.maidlab.manager.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	public String uploadFile(MultipartFile file) throws IOException {
		String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(filename)
			.contentType(file.getContentType())
			.build();

		s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

		return filename;
	}
}
