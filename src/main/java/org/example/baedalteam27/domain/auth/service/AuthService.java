package org.example.baedalteam27.domain.auth.service;

import lombok.RequiredArgsConstructor;

import org.example.baedalteam27.domain.auth.dto.LoginRequestDto;
import org.example.baedalteam27.domain.auth.dto.MailCheckRequestDto;
import org.example.baedalteam27.domain.auth.dto.MailCheckResponseDto;
import org.example.baedalteam27.domain.auth.dto.PasswordChangeRequestDto;
import org.example.baedalteam27.domain.auth.dto.PasswordChangeResponseDto;
import org.example.baedalteam27.domain.auth.dto.SignupRequestDto;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.example.baedalteam27.global.config.PasswordEncoder;
import org.example.baedalteam27.global.jwt.JwtProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, String> redisTemplate;


	public void signup(SignupRequestDto dto) {
		if (!isValidEmail(dto.getEmail()) || !isValidPassword(dto.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호 형식이 유효하지 않습니다.");
		}

		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(dto.getRole());

		userRepository.save(user);
	}

	public String login(LoginRequestDto dto) {
		User user = userRepository.findByEmail(dto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

		if (Boolean.TRUE.equals(user.getIsDeleted())) {
			throw new IllegalArgumentException("이미 탈퇴한 사용자입니다.");
		}

		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}

		return jwtProvider.createToken(user.getId(), user.getRole().name());
	}


	public void withdraw(Long userId, String rawPassword) {
		User user = userRepository.getUserByUserId(userId);

		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		user.setIsDeleted(true);
	}

	public MailCheckResponseDto mailCheck(MailCheckRequestDto dto) {
		if (!isValidEmail(dto.getEmail())) {
			throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
		}

		boolean exists = userRepository.existsByEmail(dto.getEmail());
		if (exists) {
			return new MailCheckResponseDto(false, "이미 존재하는 이메일입니다.");
		}

		return new MailCheckResponseDto(true, "사용 가능한 이메일입니다.");
	}
	// Redis 임시저장

	// 비밀번호 변경
	public PasswordChangeResponseDto passwordChange(Long userId, PasswordChangeRequestDto dto) {
		User user = userRepository.getUserByUserId(userId);

		// 현재 비밀번호 일치 여부 확인
		if (!passwordEncoder.matches(dto.getNowPassword(), user.getPassword())) {
			throw new IllegalArgumentException("현재 비밀번호와 일치하지 않습니다.");
		}

		// 새 비밀번호 형식 확인
		if (!isValidPassword(dto.getNewPassword())) {
			throw new IllegalArgumentException("새 비밀번호 형식이 유효하지 않습니다.");
		}

		// 새 비밀번호 저장
		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

		return new PasswordChangeResponseDto("비밀번호가 성공적으로 변경되었습니다.");
	}

	public void logout(String token, Long userId) {
		// 이미 Redis에 해당 토큰이 저장되어 있으면 재로그아웃 방지
		if (Boolean.TRUE.equals(redisTemplate.hasKey(token))) {
			throw new IllegalStateException("이미 로그아웃된 사용자입니다.");
		}

		// 토큰의 남은 유효 시간 계산
		long expiration = jwtProvider.getExpiration(token);

		// Redis에 저장 (블랙리스트로 간주)
		redisTemplate.opsForValue().set(token, "logout", Duration.ofMillis(expiration));
	}




	private boolean isValidEmail(String email) {
		return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
	}

	private boolean isValidPassword(String pw) {
		return pw.length() >= 8 &&
			pw.matches(".*[A-Z].*") &&
			pw.matches(".*[a-z].*") &&
			pw.matches(".*[0-9].*") &&
			pw.matches(".*[!@#$%^&*()].*");
	}
}
