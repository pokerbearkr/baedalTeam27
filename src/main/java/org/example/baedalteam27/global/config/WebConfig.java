package org.example.baedalteam27.global.config;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.global.jwt.JwtAuthFilter;
import org.example.baedalteam27.global.jwt.LoginUserArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginUserArgumentResolver loginUserArgumentResolver;
	private final JwtAuthFilter jwtAuthFilter;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserArgumentResolver);
	}

	@Bean
	public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
		FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(jwtAuthFilter);
		registrationBean.addUrlPatterns("/api/*"); // 로그인 인증이 필요한 경로에만 필터 적용
		return registrationBean;
	}
}
