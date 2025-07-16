package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.common.enums.LanguageEnum;
import com.readforce.passage.entity.Language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorLanguageResponseDto {

	private Long languageNo;
	
	private LanguageEnum languageName;
	
	private LocalDateTime createdAt;
	
	public AdministratorLanguageResponseDto(Language language) {
		
		this.languageNo = language.getLanguageNo();
		this.languageName = language.getLanguageName();
		this.createdAt = language.getCreatedAt();
		
	}
	
}
