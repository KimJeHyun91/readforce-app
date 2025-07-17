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

}
