package org.example.baedalteam27.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MailCheckRequestDto {
	private final String email;
}
