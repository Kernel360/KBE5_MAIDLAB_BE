package kernel.maidlab.api.aws.controller;

import kernel.maidlab.api.aws.dto.FileNamesRequestDto;
import kernel.maidlab.api.aws.dto.PresignedFileResponseDto;
import kernel.maidlab.api.aws.service.S3Service;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class S3Controller {

	private final S3Service s3Service;

	@PostMapping("/presigned-urls")
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<List<PresignedFileResponseDto>>> getPresignedUrls(
		@RequestBody FileNamesRequestDto request) {
		List<PresignedFileResponseDto> presignedUrls = s3Service.uploadFile(
			request.getFilenames(),
			"uploads"
		);
		return ResponseDto.success(presignedUrls);
	}
}
