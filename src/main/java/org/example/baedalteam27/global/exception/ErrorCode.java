package org.example.baedalteam27.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USER_001", "올바른 이메일 형식이 아닙니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "USER_002", "비밀번호 형식이 유효하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_003", "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_004", "비밀번호가 일치하지 않습니다."),

    // 401 Unauthorized
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "USER_005", "인증되지 않은 사용자입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "유효하지 않은 리프레시 토큰입니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_006", "사용자를 찾을 수 없습니다."),

    // 409 Conflict
    ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "USER_007", "이미 탈퇴한 사용자입니다."),
    ALREADY_LOGOUT(HttpStatus.CONFLICT, "USER_008", "이미 로그아웃된 사용자입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
