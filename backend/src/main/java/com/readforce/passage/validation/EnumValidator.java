package com.readforce.passage.validation;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

	private Set<String> allowedValueSet;
	
	private boolean ignoreCase;
	
	@Override
	public void initialize(ValidEnum constraintAnnotation) {
		
		this.ignoreCase = constraintAnnotation.ignoreClass();
		this.allowedValueSet = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
				.map(Enum::name)
				.map(value -> ignoreCase ? value.toUpperCase() : value)
				.collect(Collectors.toSet());
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if(value == null || value.isBlank()) {
			
			return true;
			
		}
		
		String valueToCompare = ignoreCase ? value.toUpperCase() : value;
		
		return allowedValueSet.contains(valueToCompare);

	}
	
}
