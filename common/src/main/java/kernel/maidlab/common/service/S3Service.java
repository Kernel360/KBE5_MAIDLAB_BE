package kernel.maidlab.common.service;

import java.util.List;

import kernel.maidlab.common.dto.PresignedFileResponseDto;

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
	public List<PresignedFileResponseDto> uploadFile(List<String> filenames, String prefix);

}
