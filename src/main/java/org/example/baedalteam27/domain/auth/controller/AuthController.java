package org.example.baedalteam27.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.auth.dto.LoginRequestDto;
import org.example.baedalteam27.domain.auth.dto.MailCheckRequestDto;
import org.example.baedalteam27.domain.auth.dto.MailCheckResponseDto;
import org.example.baedalteam27.domain.auth.dto.PasswordChangeRequestDto;
import org.example.baedalteam27.domain.auth.dto.PasswordChangeResponseDto;
import org.example.baedalteam27.domain.auth.dto.SignupRequestDto;
import org.example.baedalteam27.domain.auth.dto.WithdrawRequestDto;
import org.example.baedalteam27.domain.auth.service.AuthService;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody SignupRequestDto dto) {
		authService.signup(dto);
		return ResponseEntity.ok().build();
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody LoginRequestDto dto) {
		String token = authService.login(dto);
		return ResponseEntity.ok()
			.header("Authorization", "Bearer " + token)
			.build();
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
	public ResponseEntity<MailCheckResponseDto> mailCheck(@RequestBody MailCheckRequestDto dto) {
		MailCheckResponseDto response = authService.mailCheck(dto);
		return ResponseEntity.ok(response);
	}

	// 비밀번호 변경
	@PatchMapping("/password")
	public ResponseEntity<PasswordChangeResponseDto> passwordChange(
		@LoginUser Long userId,
		@RequestBody PasswordChangeRequestDto dto
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

}
