package com.readforce.common.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.PrefixEnum;
import com.readforce.common.exception.RateLimitExceededException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

	@Value("${rate-limiting.ip.max-request}")
	private int ipMaxRequest;
	
	@Value("${rate-limiting.ip.per-minute}")
	private int ipPerMinute;
	
	@Value("${rate-limiting.email.max-request}")
	private int emailMaxRequest;
	
	@Value("${rate-limiting.email.per-minute}")
	private int emailPerMinute;

	@Value("${rate-limiting.email-verification.max-request}")
	private int emailVerificationMaxRequest;
	
	@Value("${rate-limiting.email-verification.per-hour}")
	private int emailVerificationPerHour;
	
	private final StringRedisTemplate redisTemplate;
	
	public boolean isIpRequestAllowed(String ipAddress) {
		
		String key = PrefixEnum.IP_RATE_LIMIT.getContent() + ipAddress;
		
		return isRequestAllowed(key, ipMaxRequest, Duration.ofMinutes(ipPerMinute));
		
	}
	
	public boolean isEmailRequestAllowed(String email) {
		
		String key = PrefixEnum.EMAIL_RATE_LIMIT.getContent() + email;
		
		return isRequestAllowed(key, emailMaxRequest, Duration.ofMinutes(emailPerMinute));
		
	}
	
	public boolean isRequestAllowed(String key, long maxRequest, Duration duration) {
		
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		
		Long currentRequest = valueOperations.increment(key);
		
		if(currentRequest == null) {
			
			return false;
			
		}
		
		if(currentRequest == 1) {
			
			redisTemplate.expire(key, duration);
			
		}
		
		return currentRequest <= maxRequest;
		
	}

	public void checkChallengeAttempt(String email, CategoryEnum category, LanguageEnum language) {
		
		String messageCode = getChallengeMessageCode(category, language);
		
		String key = PrefixEnum.CHALLENGE_LIMIT.getContent() + email + ":" + category.toString() + ":" + language.toString();
		
		if(redisTemplate.opsForValue().get(key) != null) {
			
			throw new RateLimitExceededException(messageCode);
			
		}
		
	}
	
	public void incrementChallengeAttempt(String email, CategoryEnum category, LanguageEnum language) {

		LocalDateTime now = LocalDateTime.now();
		
		LocalDateTime midnignt = now.toLocalDate().plusDays(1).atStartOfDay();
		
		Duration durationUntilMidnight = Duration.between(now, midnignt);
		
		String key = PrefixEnum.CHALLENGE_LIMIT.getContent() + email + ":" + category.toString() + ":" + language.toString();
		
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		
		valueOperations.set(key, "1", durationUntilMidnight);
		
	}

	private String getChallengeMessageCode(CategoryEnum category, LanguageEnum language) {
		
		String messageCode = null;
		 
		switch(category) {

			case NOVEL:
				switch(language) {
				
					case KOREAN:
						messageCode = MessageCode.NOVEL_KOREAN_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
						break;
						
					case ENGLISH:
						messageCode = MessageCode.NOVEL_ENGLISH_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
						break;
						
					case JAPANESE:
						messageCode = MessageCode.NOVEL_JAPANESE_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
						break;
						
					default:
						throw new IllegalArgumentException(MessageCode.NOVEL_LANGUAGE_INVALID);
				
				}
				break;
				
			case FAIRY_TALE:
				switch(language) {
				
					case KOREAN:
						messageCode = MessageCode.FAIRY_TALE_KOREAN_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
						break;
						
					case ENGLISH:
						messageCode = MessageCode.FAIRY_TALE_ENGLISH_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
						break;
						
					case JAPANESE:
						messageCode = MessageCode.FAIRY_TALE_JAPANESE_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
						break;
						
					default:
						throw new IllegalArgumentException(MessageCode.FAIRY_TALE_LANGUAGE_INVALID);
				
				}
				break;
				
			default:
				switch(language) {
				
				case KOREAN:
					messageCode = MessageCode.NEWS_KOREAN_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
					break;
					
				case ENGLISH:
					messageCode = MessageCode.NEWS_ENGLISH_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
					break;
					
				case JAPANESE:
					messageCode = MessageCode.NEWS_JAPANESE_CHALLENGE_ALREDY_ATTEMPTED_TODAY;
					break;
					
				default:
					throw new IllegalArgumentException(MessageCode.NEWS_LANGUAGE_INVALID);
					
			}
			break;
		
		}

		return messageCode;
	}
	
	public void checkAndIncrementVerificationAttempt(String email) {
		
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		
		String key = PrefixEnum.EMAIL_VERIFICATION_ATTEMPT.getContent() + email;
		
		String currentAttemptAsString = valueOperations.get(key);
		
		int currentAttempt = (currentAttemptAsString == null) ? 0 : Integer.parseInt(currentAttemptAsString);
		
		if(currentAttempt >= emailVerificationMaxRequest){
			
			throw new RateLimitExceededException(MessageCode.EMAIL_REQUEST_LIMIT_EXCEEDED);
			
		}
		
		valueOperations.increment(key);
		
		if(currentAttempt == 0) {
			
			redisTemplate.expire(key, emailVerificationPerHour, TimeUnit.HOURS);
			
		}
		
	}
	
}