package com.readforce.email.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.email.dto.EmailVerificationRequestDto;
import com.readforce.email.dto.EmailVerificationVerifyDto;
import com.readforce.email.service.EmailService;
import com.readforce.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;
	private final MemberService memberService;
	
	@PostMapping("/send-verification-code-for-sign-up")
	public ResponseEntity<Map<String, String>> sendVerificationCodeForSignUp(@Valid @RequestBody EmailVerificationRequestDto emailVerificationRequestDto){
		
		memberService.emailCheck(emailVerificationRequestDto.getEmail());
		
		emailService.sendVerificationCodeForSignUp(emailVerificationRequestDto.getEmail());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_SEND_SUCCESS));
		
	}
	
	@PostMapping("/verify-verification-code-for-sign-up")
	public ResponseEntity<Map<String, String>> verifyVerificationCodeForSignUp(@Valid @RequestBody EmailVerificationVerifyDto emailVerificationVerifyDto){
		
		emailService.verifyVerificationCodeForSignUp(emailVerificationVerifyDto.getEmail(), emailVerificationVerifyDto.getVerificationCode());

		emailService.markEmailAsVerified(emailVerificationVerifyDto.getEmail());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.VERIFICATION_CODE_VERIFY_SUCCESS));
		
	}

	@PostMapping("/send-password-reset-link")
	public ResponseEntity<Map<String, String>> sendPassowrdResetLink(@Valid @RequestBody EmailVerificationRequestDto emailVerificationRequestDto){
		
		memberService.emailExistCheck(emailVerificationRequestDto.getEmail());
		
		emailService.sendPasswordResetLink(emailVerificationRequestDto.getEmail());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SEND_PASSWORD_RESET_LINK_SUCCESS));
		
	}
	
	
}
