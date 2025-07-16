package com.readforce.member.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.readforce.common.MessageCode;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = BirthdayValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthday {

	String message() default MessageCode.BIRTHDAY_RANGE_INVALID;
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}
