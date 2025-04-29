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
    CATEGORY_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CATEGORY_001", "이미 존재하는 카테고리 이름입니다."),
    STORE_LIMIT_EXCEPTION(HttpStatus.BAD_REQUEST, "STORE_001", "가게는 최대 3개까지만 등록할 수 있습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "REQ_001", "카테고리 또는 가게명 중 하나만 입력! 또는 둘 다 입력X"),
    NULL_STORE_ID(HttpStatus.BAD_REQUEST, "REQ_002", "가게 번호는 필수!"),

    // 401 Unauthorized
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "USER_005", "인증되지 않은 사용자입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "유효하지 않은 리프레시 토큰입니다."),

    // 403 Forbidden
    NOT_ADMIN(HttpStatus.FORBIDDEN, "AUTH_002", "가게를 등록할 권한이 없습니다."),
    NOT_OWNER(HttpStatus.FORBIDDEN, "AUTH_003", "가게를 등록할 권한이 없습니다."),
    NOT_STORE_OWNER_MODIFY(HttpStatus.FORBIDDEN, "AUTH_004", "해당 가게에 대한 수정 권한이 없습니다."),
    NOT_STORE_OWNER_DELETE(HttpStatus.FORBIDDEN, "AUTH_005", "해당 가게에 대한 삭제 권한이 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_006", "사용자를 찾을 수 없습니다."),

    // 409 Conflict
    ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "USER_007", "이미 탈퇴한 사용자입니다."),
    ALREADY_LOGOUT(HttpStatus.CONFLICT, "USER_008", "이미 로그아웃된 사용자입니다."),
    ALREADY_STORE_DELETED(HttpStatus.CONFLICT, "STORE_002", "이미 폐업한 가게입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
