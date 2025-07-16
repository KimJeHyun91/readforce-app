package com.readforce.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.NameEnum;
import com.readforce.member.dto.MemberModifyDto;
import com.readforce.member.dto.MemberPasswordResetDto;
import com.readforce.member.dto.MemberPasswordResetFromLinkDto;
import com.readforce.member.dto.MemberSignUpDto;
import com.readforce.member.dto.MemberSocialSignUpDto;
import com.readforce.member.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/email-check")
	public ResponseEntity<Map<String, String>> emailCheck(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
			String email
	){
		
		memberService.emailCheck(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.EMAIL_CAN_USE));
		
	}
	
	@GetMapping("/nickname-check")
	public ResponseEntity<Map<String, String>> nicknameCheck(
			@RequestParam("nickname")
    		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
    		@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
    		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
    		String nickname
	){
		
		memberService.nicknameCheck(nickname);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.NICKNAME_CAN_USE));
		
	}
	
	@PostMapping("/sign-up")
	public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto){
		
		memberService.signUp(memberSignUpDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS));
		
	}
	
	@PatchMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(@Valid @RequestBody MemberModifyDto memberModifyDto, @AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		
		String nickname = memberService.modify(email, memberModifyDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MEMBER_MODIFY_SUCCESS,
				NameEnum.NICKNAME.name(), nickname				
		));
		
	}
	
	@PatchMapping("/withdraw")
	public ResponseEntity<Map<String, String>> withdraw(@AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		
		memberService.withdraw(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MEMBER_WITHDRAW_SUCCESS
		));
		
	}
	
	@PatchMapping("/password-reset-from-link")
	public ResponseEntity<Map<String, String>> passwordResetFromLink(@Valid @RequestBody MemberPasswordResetFromLinkDto memberPasswordResetFromLinkDto){
		
		memberService.passwordResetFromLink(memberPasswordResetFromLinkDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PASSWORD_RESET_SUCCESS));
		
	}
	
	@PatchMapping("/password-reset")
	public ResponseEntity<Map<String, String>> passwordReset(
		@Valid @RequestBody MemberPasswordResetDto memberPasswordResetDto,
		@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		memberService.passwordReset(email, memberPasswordResetDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PASSWORD_RESET_SUCCESS));
		
	}
	
	@PostMapping("/social-sign-up")
	public ResponseEntity<Map<String, String>> socialSignUp(@Valid @RequestBody MemberSocialSignUpDto memberSocialSignUpDto){
		
		memberService.socialSignUp(memberSocialSignUpDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS));
		
	}
	
}
