package com.readforce.result.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LearningMultipleChoiceRequestDto {
	
	@NotNull(message = MessageCode.SELECTED_INDEX_NOT_NULL)
	private Integer selectedIndex;
	
	@NotNull(message = MessageCode.QUESTION_SOLVING_TIME_NOT_NULL)
	@Min(value = 10)
	private Long questionSolvingTime;
	
	@NotNull(message = MessageCode.QUESTION_NO_NOT_NULL)
	private Long questionNo;

}
