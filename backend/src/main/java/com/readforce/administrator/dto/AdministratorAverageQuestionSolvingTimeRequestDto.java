package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorAverageQuestionSolvingTimeRequestDto {

	@NotNull(message = MessageCode.AVERAGE_QUESTION_SOLVING_TIME_NOT_NULL)
	private Long averageQuestionSolvingTime;
	
	@NotNull(message = MessageCode.AGE_GROUP_NOT_NULL)
	private Integer ageGroup;
	
	@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
	private CategoryEnum category;
	
	@NotNull(message = MessageCode.TYPE_NO_NOT_NULL)
	private TypeEnum type;
	
	@NotNull(message = MessageCode.LEVEL_NOT_NULL)
	private Integer level;
	
	@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
	private LanguageEnum language;
	
}
