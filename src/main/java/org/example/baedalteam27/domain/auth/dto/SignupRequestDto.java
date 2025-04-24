package org.example.baedalteam27.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.example.baedalteam27.domain.user.UserRole;

@Getter
@RequiredArgsConstructor
public class SignupRequestDto {
	private final String email;
	private final String password;
	private final UserRole role;
}
