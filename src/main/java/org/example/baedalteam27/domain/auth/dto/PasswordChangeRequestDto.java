package org.example.baedalteam27.domain.auth.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {
	private String nowPassword;
	private String newPassword;
}
