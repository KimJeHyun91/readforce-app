package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.LanguageEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorLanguageRequestDto {

	@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
	private LanguageEnum language;
	
}
