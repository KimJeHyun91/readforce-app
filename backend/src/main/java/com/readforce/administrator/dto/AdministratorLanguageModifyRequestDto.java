package com.readforce.administrator.dto;

import com.readforce.common.enums.LanguageEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorLanguageModifyRequestDto {

	private Long languageNo;
	
	private LanguageEnum language;
	
}
