package org.example.baedalteam27.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.example.baedalteam27.domain.user.UserRole;

@Getter
@RequiredArgsConstructor
public class SignupRequestDto {

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수입니다.")
	private final String email;

	@Pattern(
			regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$",
			message = "비밀번호는 8자 이상, 대문자/소문자/숫자/특수문자를 포함해야 합니다."
	)
	@NotBlank(message = "비밀번호는 필수입니다.")
	private final String password;
	private final UserRole role;
}
