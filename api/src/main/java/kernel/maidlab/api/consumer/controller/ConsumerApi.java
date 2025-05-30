package kernel.maidlab.api.consumer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.consumer.dto.request.ConsumerProfileRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Consumer", description = "Consumer API")
public interface ConsumerApi {

    @Operation(summary = "프로필 생성", description = "프로필 생성 API")
    @ApiResponse(responseCode = "200", description = "프로필 등록 완료!")
    ResponseEntity<?> updateProfile(HttpServletRequest request, @RequestBody ConsumerProfileRequestDto consumerProfileRequestDto);

    @Operation(summary = "프로필 조회", description = "프로필 조회 API")
    ResponseEntity<?> getProfile(HttpServletRequest request);

    @Operation(summary = "마이페이지 조회", description = "마이페이지 조회 API")
    ResponseEntity<?> getMypage(HttpServletRequest request);

    }
