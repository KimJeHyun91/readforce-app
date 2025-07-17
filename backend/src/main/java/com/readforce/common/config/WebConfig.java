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
    registry.addMapping("/**") // 모든 URL 패턴 허용
            .allowedOrigins("*") // 모든 Origin 허용 (운영 환경에선 제한 권장)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false) // true면 allowedOrigins에 * 사용 못함
            .maxAge(3600);
  }

}
