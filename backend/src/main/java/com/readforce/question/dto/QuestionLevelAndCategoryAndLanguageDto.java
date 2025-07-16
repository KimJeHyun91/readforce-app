package com.readforce.question.dto;

import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestionLevelAndCategoryAndLanguageDto {

	private CategoryEnum category;
	
	private LanguageEnum language;
	
	private Integer level;
	
}
