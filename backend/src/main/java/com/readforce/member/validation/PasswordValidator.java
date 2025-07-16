package com.readforce.member.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String>{

	private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).*$";

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
	
		if(password == null) {
			
			return false;
			
		}
		
		return Pattern.matches(PASSWORD_PATTERN, password);

	}
	
}
