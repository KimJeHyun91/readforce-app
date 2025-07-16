package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorResultMetricRequestDto {

	@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	private String email;
	
}
