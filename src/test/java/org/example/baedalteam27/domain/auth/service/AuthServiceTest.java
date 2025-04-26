package org.example.baedalteam27.domain.auth.service;

import org.example.baedalteam27.domain.auth.dto.*;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.example.baedalteam27.global.config.PasswordEncoder;
import org.example.baedalteam27.global.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

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
        //주어질 Dto 생성
        SignupRequestDto request = new SignupRequestDto("test@example.com", "Password1!", UserRole.USER);

        // 중복된 이메일로 가입된 유저 없는지 확인
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);

        // 암호화된 비밀번호 부여
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");

        // when
        authService.signup(request);

        // then
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    void login_성공() {
        // given
        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");

        User fakeUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword") // DB에 저장된 비밀번호는 암호화된 상태여야 함
                .role(UserRole.USER)
                .build();

        // id가 builder에서 빠져있어서 id 강제주입
        ReflectionTestUtils.setField(fakeUser, "id", 1L);

        // 이메일로 유저를 찾으면 fakeUser를 리턴
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(fakeUser));

        // 입력한 비밀번호와 저장된 비밀번호를 비교했을 때 일치한다고 가정
        given(passwordEncoder.matches(request.getPassword(), fakeUser.getPassword())).willReturn(true);

        // 토큰을 만들어줬다고 가정
        given(jwtProvider.createAccessToken(fakeUser.getId(), fakeUser.getRole().name())).willReturn("mocked-access-token");

        // when
        String token = authService.login(request);

        // then
        assertNotNull(token); // 토큰은 null이 아니어야 한다
        assertEquals("mocked-access-token", token); // 우리가 가짜로 만들어준 토큰과 같아야 한다
    }

    @Test
    void login_실패_비밀번호_불일치() {

        // 위에와 똑같이 회원가입 User 생성
        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");

        User fakeUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();

        ReflectionTestUtils.setField(fakeUser, "id", 1L);

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(fakeUser));

        // 비밀번호 불일치하게 given
        given(passwordEncoder.matches(request.getPassword(), fakeUser.getPassword())).willReturn(false);

        // 비밀번호가 틀리므로 IllegalArgumentException return
        assertThrows(IllegalArgumentException.class, () -> authService.login(request));

    }

    @Test
    void login_실패_이미_탈퇴한_유저() {

        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");

        User fakeUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();

        ReflectionTestUtils.setField(fakeUser, "id", 1L);
        ReflectionTestUtils.setField(fakeUser,"isDeleted", true);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

    @Test
    void login_실패_이메일_불일치() {

        LoginRequestDto request = new LoginRequestDto("test@example.com", "Password1!");

        User fakeUser = User.builder()
                .email("wrong@example.com")
                .password("EncodedPassword")
                .role(UserRole.USER)
                .build();

        ReflectionTestUtils.setField(fakeUser, "id", 1L);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));

    }


    @Test
    void withdraw_성공() {
        // given
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

        // when
        authService.withdraw(userId, rawPassword);

        // then
        assertTrue(fakeUser.getIsDeleted()); // 탈퇴 상태여야 한다
    }


    @Test
    void mailCheck_성공_중복X() {
        // given
        MailCheckRequestDto request = new MailCheckRequestDto("test@example.com");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false); // 이메일 중복 없음

        // when
        MailCheckResponseDto response = authService.mailCheck(request);

        // then
        assertTrue(response.isAvailable()); // 사용 가능해야 함
        assertEquals("사용 가능한 이메일입니다.", response.getMessage());
    }

    @Test
    void passwordChange_성공() {
        // given
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

        // when
        PasswordChangeResponseDto response = authService.passwordChange(userId, requestDto);

        // then
        assertEquals("비밀번호가 성공적으로 변경되었습니다.", response.getMessage());
    }
    @Test
    void passwordChange_실패_현재비밀번호불일치() {
        // given
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

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.passwordChange(userId, requestDto));
    }

    @Test
    void logout_성공() {
        // given
        String token = "access-token";
        Long userId = 1L;

        given(redisTemplate.hasKey(token)).willReturn(false);
        given(jwtProvider.getExpiration(token)).willReturn(60000L);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // when
        authService.logout(token, userId);

        // then
        then(redisTemplate).should(times(1)).opsForValue();
        then(valueOperations).should(times(1)).set(token, "logout", Duration.ofMillis(60000L));
    }
    @Test
    void logout_실패_이미로그아웃() {
        // given
        String token = "access-token";
        Long userId = 1L;

        given(redisTemplate.hasKey(token)).willReturn(true);

        // when & then
        assertThrows(IllegalStateException.class, () -> authService.logout(token, userId));
    }

    @Test
    void reissueToken_성공() {
        // given
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

        // when
        Map<String, String> tokens = authService.reissueToken(refreshToken, userId);

        // then
        assertEquals("new-access-token", tokens.get("accessToken"));
        assertEquals("new-refresh-token", tokens.get("refreshToken"));
    }

    @Test
    void reissueToken_실패_유효하지않은리프레시토큰() {
        // given
        String refreshToken = "invalid-refresh-token";
        Long userId = 1L;

        given(jwtProvider.validateToken(refreshToken)).willReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.reissueToken(refreshToken, userId));
    }

}