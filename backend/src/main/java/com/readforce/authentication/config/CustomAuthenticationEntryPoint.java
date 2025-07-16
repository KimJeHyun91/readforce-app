package com.readforce.authentication.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.NameEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
			HttpServletRequest request, 
			HttpServletResponse response,
			AuthenticationException exception
	) throws IOException, ServletException {
		
		String exceptionMessage = (String)request.getAttribute(NameEnum.EXCEPTION.name());
		
		if(exceptionMessage == null) {
			
			exceptionMessage = MessageCode.AUTHENTICATION_FAIL;
			
		}
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		response.setCharacterEncoding("UTF-8");
		
		Map<String, String> body = new HashMap<>();
		
		body.put(MessageCode.MESSAGE_CODE, exceptionMessage);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String jsonBody = objectMapper.writeValueAsString(body);
		
		response.getWriter().write(jsonBody);
		
	}
	
}
