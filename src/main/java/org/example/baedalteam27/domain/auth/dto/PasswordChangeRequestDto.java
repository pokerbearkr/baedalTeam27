package org.example.baedalteam27.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordChangeRequestDto {
	@Pattern(
			regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$",
			message = "비밀번호는 8자 이상, 대문자/소문자/숫자/특수문자를 포함해야 합니다."
	)
	@NotBlank(message = "비밀번호는 필수입니다.")
	private final String nowPassword;

	@Pattern(
			regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$",
			message = "비밀번호는 8자 이상, 대문자/소문자/숫자/특수문자를 포함해야 합니다."
	)
	@NotBlank(message = "비밀번호는 필수입니다.")
	private final String newPassword;
}
