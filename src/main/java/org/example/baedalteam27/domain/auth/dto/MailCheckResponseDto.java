package org.example.baedalteam27.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailCheckResponseDto {
	private boolean available;
	private String message;
}
