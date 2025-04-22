package org.example.baedalteam27.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.auth.dto.LoginRequestDto;
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
}
