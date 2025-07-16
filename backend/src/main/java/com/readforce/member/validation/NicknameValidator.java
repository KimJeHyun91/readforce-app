package com.readforce.member.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<ValidNickname, String> {

	private static final String NICKNAME_PATTERN = "^[a-zA-Z가-힣0-9]+$";
	
	@Override
	public boolean isValid(String nickname, ConstraintValidatorContext context) {

		if(nickname == null || nickname.isBlank()) {
			
			return true;
			
		}
		
		return Pattern.matches(NICKNAME_PATTERN, nickname);
		
	}

}
