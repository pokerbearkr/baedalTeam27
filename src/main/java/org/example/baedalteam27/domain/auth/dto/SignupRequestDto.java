package org.example.baedalteam27.domain.auth.dto;

import lombok.Getter;
import org.example.baedalteam27.domain.user.UserRole;

@Getter
public class SignupRequestDto {
	private String email;
	private String password;
	private UserRole role;
}
