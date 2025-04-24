package org.example.baedalteam27.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordChangeRequestDto {
	private final String nowPassword;
	private final String newPassword;
}
