package com.readforce.passage.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.readforce.common.MessageCode;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

	String message() default MessageCode.VALUE_INVALID;
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default{};
	
	Class<? extends Enum<?>> enumClass();
	
	boolean ignoreClass() default false;
	
}
