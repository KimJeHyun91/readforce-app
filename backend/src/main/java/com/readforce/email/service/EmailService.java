package com.readforce.email.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.readforce.authentication.exception.AuthenticationException;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.ExpireTimeEnum;
import com.readforce.common.enums.NameEnum;
import com.readforce.common.enums.PrefixEnum;
import com.readforce.common.service.RateLimitingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final RateLimitingService rateLimitingService;
	private final JavaMailSender javaMailSender;
	private final StringRedisTemplate redisTemplate;
	private final String SIGN_UP_MESSAGE = "ReadForce에 가입하신 것을 환영합니다.";
	private final String DEFAULT_MESSAGE = "ReadForce를 이용해주셔서 감사합니다.";
	
	@Value("${custom.fronted.password-reset-link-url}")
	private String passwordResetUrl;
	
	private String createVerificationCode() {
		
		return String.valueOf(10000 + new SecureRandom().nextInt(900000));
		
	}
	
	private void sendVerificationCode(
			String email,
			String message,
			String verificationCode,
			long expiredTime,
			String prefix
	){
		
		String subject = "[ReadForce] 이메일 인증 안내";
		
		String text = 
				message + "\n" +
				"인증 번호 : " + verificationCode + "\n" +
				"이 코드는 " + expiredTime + "분간 유효합니다.";
		
		redisTemplate.opsForValue().set(
				prefix + email, 
				verificationCode,
				Duration.ofMinutes(expiredTime)
		);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(email);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		
		javaMailSender.send(simpleMailMessage);
		
	}
	
	private void verifyVerificationCode(String email, String code, String prefix) {
		
		String storedCode = redisTemplate.opsForValue().get(prefix + email);
		
		if(storedCode == null || !storedCode.equals(code)) {
			
			throw new AuthenticationException(MessageCode.VERIFY_VERIFICATION_CODE_FAIL);
			
		}
		
		redisTemplate.delete(prefix + email);		
		
	}
	
	public void sendVerificationCodeForSignUp(String email) {
		
		rateLimitingService.checkAndIncrementVerificationAttempt(email);
		
		sendVerificationCode(
				email,
				SIGN_UP_MESSAGE,
				createVerificationCode(),
				ExpireTimeEnum.DEFAULT.getTime(),
				PrefixEnum.SIGN_UP.getContent()				
		);
		
	}
	
	public void verifyVerificationCodeForSignUp(String email, String code) {
		
		verifyVerificationCode(email, code, PrefixEnum.SIGN_UP.getContent());
		
	}
	
	public void sendPasswordResetLink(String email) {
		
		String temporalToken = UUID.randomUUID().toString();
		
		String subject = "[ReadForce] 비밀번호 재설정 안내";
		String text = 
				DEFAULT_MESSAGE + "\n" +
				"비밀번호를 재설정 하시려면 아래의 링크를 눌러주세요.\n" +
				passwordResetUrl + "?" + "token=" + temporalToken;
		
		redisTemplate.opsForValue().set(
				PrefixEnum.PASSWORD_RESET.getContent() + temporalToken,
				email,
				Duration.ofMinutes(ExpireTimeEnum.DEFAULT.getTime())
		);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(email);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		
		javaMailSender.send(simpleMailMessage);

	}
	
	public void sendPasswordChangeNotification(String email) {
		
		String subject = "[ReadForce] 비밀번호 재설정 안내";
		String text = 
				DEFAULT_MESSAGE + "\n" +
				"비밀번호 변경이 완료되었습니다.\n";
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(email);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		
		javaMailSender.send(simpleMailMessage);
		
	}
	
	public void markEmailAsVerified(String email) {
		
		redisTemplate.opsForValue().set(
				PrefixEnum.EMAIL_VERIFICATION.getContent() + email,
				MessageCode.EMAIL_VERIFICATION_SUCCESS,
				Duration.ofMinutes(ExpireTimeEnum.MARK_EMAIL_AS_VERIFIED.getTime())
		);
		
	}	

}
