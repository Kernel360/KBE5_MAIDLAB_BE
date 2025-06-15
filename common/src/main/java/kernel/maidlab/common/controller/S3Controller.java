package kernel.maidlab.common.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.common.dto.FileNamesRequestDto;
import kernel.maidlab.common.dto.PresignedFileResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class S3Controller {

	private final S3Service s3Service;

	@PostMapping("/presigned-urls")
	public ResponseEntity<ResponseDto<List<PresignedFileResponseDto>>> getPresignedUrls(
		@RequestBody FileNamesRequestDto request) {
		List<PresignedFileResponseDto> presignedUrls = s3Service.uploadFile(
			request.getFilenames(),
			"uploads"
		);
		return ResponseDto.success(presignedUrls);
	}
}