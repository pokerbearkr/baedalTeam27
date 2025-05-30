package org.example.baedalteam27.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private Key key;

	@Value("${jwt.secret}")
	private String secret;

	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
	private final long REFRESH_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

	@PostConstruct
	public void init() {
		// properties에서 불러온 시크릿 키로 고정 키 생성
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(Long userId, String role) {
		return Jwts.builder()
			.setSubject(userId.toString())
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
			.signWith(key)
			.compact();
	}

	public String createRefreshToken() {
		return Jwts.builder()
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
			.signWith(key)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Long extractUserIdAllowExpired(String token) {
		try {
			return Long.parseLong(Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token.replace("Bearer ", ""))
				.getBody()
				.getSubject());
		} catch (ExpiredJwtException e) {
			return Long.parseLong(e.getClaims().getSubject()); // 만료된 토큰이라도 claims 접근 가능
		}
	}


	public Long getUserIdFromToken(String token) {
		return Long.parseLong(parseClaims(token).getBody().getSubject());
	}

	public String getRoleFromToken(String token) {
		return parseClaims(token).getBody().get("role", String.class);
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token.replace("Bearer ", ""));
	}

	public long getExpiration(String token) {
		return parseClaims(token).getBody().getExpiration().getTime() - System.currentTimeMillis();
	}
}
