package org.example.baedalteam27.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MailCheckResponseDto {
	private final boolean available;
	private final String message;
}
