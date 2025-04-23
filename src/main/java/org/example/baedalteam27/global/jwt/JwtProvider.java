package org.example.baedalteam27.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

	private Key key;
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

	@PostConstruct
	public void init() {
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	// 토큰 생성
	public String createToken(Long userId, String role) {
		return Jwts.builder()
			.setSubject(userId.toString())
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
			.signWith(key)
			.compact();
	}

	// 토큰에서 userId 꺼내기
	public Long getUserIdFromToken(String token) {
		return Long.parseLong(parseClaims(token).getBody().getSubject());
	}

	// 토큰에서 role 꺼내기
	public String getRoleFromToken(String token) {
		return parseClaims(token).getBody().get("role", String.class);
	}

	// 공통 Claims 파서
	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token.replace("Bearer ", ""));
	}
}
