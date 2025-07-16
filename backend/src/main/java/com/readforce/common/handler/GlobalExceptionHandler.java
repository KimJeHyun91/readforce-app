package com.readforce.common.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.readforce.ai.exception.ApiException;
import com.readforce.authentication.exception.AuthenticationException;
import com.readforce.authentication.exception.JwtException;
import com.readforce.common.MessageCode;
import com.readforce.common.exception.DuplicationException;
import com.readforce.common.exception.JsonException;
import com.readforce.common.exception.RateLimitExceededException;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.file.exception.FileException;
import com.readforce.file.exception.ProfileImageException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ProfileImageException.class)
	public ResponseEntity<Map<String, String>> handerProfileImageException(ProfileImageException exception){
		
		log.warn("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Map<String, String>> handlerApiException(ApiException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Map<String, String>> handlerCustomAuthenticationException(AuthenticationException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<Map<String, String>> handlerJwtException(JwtException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(DuplicationException.class)
	public ResponseEntity<Map<String, String>> handlerDuplicationException(DuplicationException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(JsonException.class)
	public ResponseEntity<Map<String, String>> handlerJsonException(JsonException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(RateLimitExceededException.class)
	public ResponseEntity<Map<String, String>> handlerRateLimitExceededException(RateLimitExceededException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlerResourceNotFoundException(ResourceNotFoundException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<Map<String, String>> handlerFileException(FileException exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handlerException(Exception exception){
		
		log.error("[{}] 발생: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MessageCode.MESSAGE_CODE, exception.getMessage()));
		
	}
	
	
}
