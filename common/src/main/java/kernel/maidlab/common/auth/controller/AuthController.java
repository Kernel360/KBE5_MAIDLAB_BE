package kernel.maidlab.common.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import kernel.maidlab.common.auth.dto.request.LoginRequestDto;
import kernel.maidlab.common.auth.dto.request.SignupRequestDto;
import kernel.maidlab.common.auth.dto.response.TokenResponseDto;
import kernel.maidlab.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController{

    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입", description = "신규 회원 가입 API")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request) {

        return ResponseEntity.ok("회원가입 완료");
    }

    @Operation(summary = "로그인", description = "이메일/비밀번호 기반 로그인 API")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        // 실제 구현에선 DB 조회 필요, 테스트용으로 하드코딩
        if ("010-1111-1111".equals(request.getPhoneNumber()) && "1234".equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getPhoneNumber());
            return ResponseEntity.ok(new TokenResponseDto(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 처리 API")
    @PostMapping("/socialLogin")
    public void socialLogin() {}

    @Operation(summary = "토큰 갱신", description = "AccessToken 재발급 API")
    @PostMapping("/refresh")
    public void refresh() {}

    @Operation(summary = "로그아웃", description = "로그아웃 처리 api", security = @SecurityRequirement(name = "JWT"))
    @PostMapping("/logout")
    public void logout() {}
}
