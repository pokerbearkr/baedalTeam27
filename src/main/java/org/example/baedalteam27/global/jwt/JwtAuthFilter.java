package org.example.baedalteam27.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final RedisTemplate<String, String> redisTemplate;

	private static final Set<String> PUBLIC_URIS = Set.of(
		"/api/auth/signup",
		"/api/auth/login",
		"/api/auth/mail-check",
		"/api/auth/reissue"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String uri = request.getRequestURI();

		// 인증이 필요 없는 URI는 필터 통과
		if (PUBLIC_URIS.contains(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = resolveToken(request);
		if (token == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Authorization 헤더가 없습니다.");
			return;
		}

		if (Boolean.TRUE.equals(redisTemplate.hasKey(token))) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("로그아웃된 토큰입니다.");
			return;
		}

		if (!jwtProvider.validateToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("유효하지 않은 토큰입니다.");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}
}
