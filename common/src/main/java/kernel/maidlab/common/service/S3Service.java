package kernel.maidlab.common.service;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kernel.maidlab.common.dto.PresignedFileResponse;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;



public interface S3Service {


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
	public List<PresignedFileResponse> uploadFile(List<String> filenames, String prefix);

}
