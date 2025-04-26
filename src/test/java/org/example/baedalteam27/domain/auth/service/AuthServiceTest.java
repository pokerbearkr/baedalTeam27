package org.example.baedalteam27.domain.auth.service;

import org.example.baedalteam27.domain.auth.dto.*;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.example.baedalteam27.global.config.PasswordEncoder;
import org.example.baedalteam27.global.exception.CustomException;
import org.example.baedalteam27.global.exception.ErrorCode;
import org.example.baedalteam27.global.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup() {
        SignupRequestDto request = new SignupRequestDto("test@example.com", "Password1!", UserRole.USER);

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");

        authService.signup(request);

        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    void login_성공() {
        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");
        User fakeUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", 1L);

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(fakeUser));
        given(passwordEncoder.matches(request.getPassword(), fakeUser.getPassword())).willReturn(true);
        given(jwtProvider.createAccessToken(fakeUser.getId(), fakeUser.getRole().name())).willReturn("mocked-access-token");

        String token = authService.login(request);

        assertNotNull(token);
        assertEquals("mocked-access-token", token);
    }

    @Test
    void login_실패_비밀번호불일치() {
        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");
        User fakeUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", 1L);

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(fakeUser));
        given(passwordEncoder.matches(request.getPassword(), fakeUser.getPassword())).willReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(request));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    void login_실패_이미탈퇴한유저() {
        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");
        User fakeUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", 1L);
        ReflectionTestUtils.setField(fakeUser, "isDeleted", true);

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(fakeUser));

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(request));
        assertEquals(ErrorCode.ALREADY_WITHDRAWN, exception.getErrorCode());
    }

    @Test
    void login_실패_이메일불일치() {
        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(request));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void withdraw_성공() {
        Long userId = 1L;
        String rawPassword = "Password1!";
        User fakeUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", userId);

        given(userRepository.getUserByUserId(userId)).willReturn(fakeUser);
        given(passwordEncoder.matches(rawPassword, fakeUser.getPassword())).willReturn(true);

        authService.withdraw(userId, rawPassword);

        assertTrue(fakeUser.getIsDeleted());
    }

    @Test
    void withdraw_실패_비밀번호불일치() {
        Long userId = 1L;
        String rawPassword = "WrongPassword";
        User fakeUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", userId);

        given(userRepository.getUserByUserId(userId)).willReturn(fakeUser);
        given(passwordEncoder.matches(rawPassword, fakeUser.getPassword())).willReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.withdraw(userId, rawPassword));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    void mailCheck_성공_중복X() {
        MailCheckRequestDto request = new MailCheckRequestDto("test@example.com");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);

        MailCheckResponseDto response = authService.mailCheck(request);

        assertTrue(response.isAvailable());
        assertEquals("사용 가능한 이메일입니다.", response.getMessage());
    }

    @Test
    void passwordChange_성공() {
        Long userId = 1L;
        String nowPassword = "Password1!";
        String newPassword = "NewPassword1!";

        User fakeUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", userId);

        given(userRepository.getUserByUserId(userId)).willReturn(fakeUser);
        given(passwordEncoder.matches(nowPassword, fakeUser.getPassword())).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn("newEncodedPassword");

        PasswordChangeRequestDto requestDto = new PasswordChangeRequestDto(nowPassword, newPassword);

        PasswordChangeResponseDto response = authService.passwordChange(userId, requestDto);

        assertEquals("비밀번호가 성공적으로 변경되었습니다.", response.getMessage());
    }

    @Test
    void passwordChange_실패_현재비밀번호불일치() {
        Long userId = 1L;
        PasswordChangeRequestDto requestDto = new PasswordChangeRequestDto("WrongNowPassword", "NewPassword1!");

        User fakeUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", userId);

        given(userRepository.getUserByUserId(userId)).willReturn(fakeUser);
        given(passwordEncoder.matches(requestDto.getNowPassword(), fakeUser.getPassword())).willReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.passwordChange(userId, requestDto));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    void logout_성공() {
        String token = "access-token";
        Long userId = 1L;

        given(redisTemplate.hasKey(token)).willReturn(false);
        given(jwtProvider.getExpiration(token)).willReturn(60000L);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        authService.logout(token, userId);

        then(redisTemplate).should(times(1)).opsForValue();
        then(valueOperations).should(times(1)).set(token, "logout", Duration.ofMillis(60000L));
    }

    @Test
    void logout_실패_이미로그아웃() {
        String token = "access-token";
        Long userId = 1L;

        given(redisTemplate.hasKey(token)).willReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.logout(token, userId));
        assertEquals(ErrorCode.ALREADY_LOGOUT, exception.getErrorCode());
    }

    @Test
    void reissueToken_성공() {
        String refreshToken = "refresh-token";
        Long userId = 1L;

        User fakeUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", userId);

        given(jwtProvider.validateToken(refreshToken)).willReturn(true);
        given(userRepository.findById(userId)).willReturn(Optional.of(fakeUser));
        given(jwtProvider.createAccessToken(userId, fakeUser.getRole().name())).willReturn("new-access-token");
        given(jwtProvider.createRefreshToken()).willReturn("new-refresh-token");

        Map<String, String> tokens = authService.reissueToken(refreshToken, userId);

        assertEquals("new-access-token", tokens.get("accessToken"));
        assertEquals("new-refresh-token", tokens.get("refreshToken"));
    }

    @Test
    void reissueToken_실패_유효하지않은리프레시토큰() {
        String refreshToken = "invalid-refresh-token";
        Long userId = 1L;

        given(jwtProvider.validateToken(refreshToken)).willReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.reissueToken(refreshToken, userId));
        assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, exception.getErrorCode());
    }
}
