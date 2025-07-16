package com.readforce.common.filter;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.HeaderEnum;
import com.readforce.common.exception.RateLimitExceededException;
import com.readforce.common.service.RateLimitingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {

	private final RateLimitingService rateLimitingService;
	
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
			
			String email = authentication.getName();
			if(!rateLimitingService.isEmailRequestAllowed(email)) {
				
				throw new RateLimitExceededException(MessageCode.EMAIL_REQUEST_LIMIT_EXCEEDED);
				
			}
			
		} else {
			
			String ipAddress = getClientIp(httpServletRequest);
			if(!rateLimitingService.isIpRequestAllowed(ipAddress)) {
				
				throw new RateLimitExceededException(MessageCode.IP_ADDRESS_REQUEST_LIMIT_EXCEEDED);
				
			}
			
		}
		
		return true;
		
	}
	
	private String getClientIp(HttpServletRequest httpServletRequest) {
		
		String xForwardedForHeader = httpServletRequest.getHeader(HeaderEnum.X_FORWARDED_FOR.getContent());
		
		if(xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
			
			return httpServletRequest.getRemoteAddr();
			
		}
		
		return xForwardedForHeader.split(",")[0];
		
	}
	
	
}
