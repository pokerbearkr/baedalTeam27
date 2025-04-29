package org.example.baedalteam27.domain.auth.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.auth.dto.LoginRequestDto;
import org.example.baedalteam27.domain.auth.dto.LoginResponseDto;
import org.example.baedalteam27.domain.auth.dto.MailCheckRequestDto;
import org.example.baedalteam27.domain.auth.dto.MailCheckResponseDto;
import org.example.baedalteam27.domain.auth.dto.PasswordChangeRequestDto;
import org.example.baedalteam27.domain.auth.dto.PasswordChangeResponseDto;
import org.example.baedalteam27.domain.auth.dto.SignupRequestDto;
import org.example.baedalteam27.domain.auth.dto.WithdrawRequestDto;
import org.example.baedalteam27.domain.auth.service.AuthService;
import org.example.baedalteam27.global.jwt.JwtProvider;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final JwtProvider jwtProvider;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto dto) {
		authService.signup(dto);
		return ResponseEntity.ok().build();
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
		String accessToken = authService.login(dto);
		String refreshToken = jwtProvider.createRefreshToken();
		return ResponseEntity.ok(new LoginResponseDto(accessToken, refreshToken));
	}

	// 계정 삭제 (soft delete)
	@DeleteMapping("/withdraw")
	public ResponseEntity<Void> withdraw(@LoginUser Long userId,
		@RequestBody WithdrawRequestDto dto) {
		authService.withdraw(userId, dto.getPassword());
		return ResponseEntity.ok().build();
	}

	// 메일 중복 체크
	@PostMapping("/mail-check")
	public ResponseEntity<MailCheckResponseDto> mailCheck(@Valid @RequestBody MailCheckRequestDto dto) {
		MailCheckResponseDto response = authService.mailCheck(dto);
		return ResponseEntity.ok(response);
	}

	// 비밀번호 변경
	@PatchMapping("/password")
	public ResponseEntity<PasswordChangeResponseDto> passwordChange(
		@LoginUser Long userId,
		@Valid @RequestBody PasswordChangeRequestDto dto
	) {
		PasswordChangeResponseDto response = authService.passwordChange(userId, dto);
		return ResponseEntity.ok(response);
	}

	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
		@LoginUser Long userId,
		HttpServletRequest request
	) {
		String token = request.getHeader("Authorization").replace("Bearer ", "");
		authService.logout(token, userId);
		return ResponseEntity.ok().build();
	}


	@PostMapping("/reissue")
	public ResponseEntity<Map<String, String>> reissue(
		@RequestHeader("Authorization") String accessToken,
		@RequestHeader("Refresh-Token") String refreshToken
	) {
		// "Bearer " 제거 후 유저 ID 수동 추출 (만료된 토큰에서도 Claims 추출은 가능)
		Long userId = jwtProvider.extractUserIdAllowExpired(accessToken);
		Map<String, String> tokens = authService.reissueToken(refreshToken, userId);
		return ResponseEntity.ok(tokens);
	}

}
