package org.example.baedalteam27.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtProvider jwtProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUser.class) &&
			parameter.getParameterType().equals(Long.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		String token = request.getHeader("Authorization");

		if (token == null || !token.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Authorization 헤더가 없거나 잘못되었습니다.");
		}

		return jwtProvider.getUserIdFromToken(token);
	}
}
