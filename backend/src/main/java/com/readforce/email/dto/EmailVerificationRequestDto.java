package com.readforce.email.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequestDto {

	@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	private String email;
	
}
