package com.readforce.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.readforce.common.filter.RateLimitingInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@ConditionalOnProperty(name = "rate-limiting.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
	private final RateLimitingInterceptor rateLimitingInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(rateLimitingInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**", "/image/**", "/js/**", "/error/**");
		
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 요청 경로 패턴
                .allowedOrigins(
                    "http://localhost:3000", // 로컬 개발용 프론트엔드 주소
                    "https://your-frontend-domain.up.railway.app" // ⚠️ 실제 배포된 프론트엔드 주소로 변경!
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // ❗ OPTIONS를 반드시 포함해야 합니다.
                .allowedHeaders("*") // 모든 종류의 헤더를 허용합니다.
                .allowCredentials(true) // 쿠키 및 인증 정보를 포함한 요청을 허용합니다.
                .maxAge(3600);
    }

}
