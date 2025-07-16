package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorScoreModifyRequestDto {

	@NotNull(message = MessageCode.SCORE_NO_NOT_NULL)
	private Long scoreNo;
	
	private Double score;
	
	private CategoryEnum category;
	
	private String email;
	
	private LanguageEnum language;
	
}
