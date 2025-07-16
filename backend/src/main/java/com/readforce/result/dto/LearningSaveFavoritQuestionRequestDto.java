package com.readforce.result.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LearningSaveFavoritQuestionRequestDto {

	@NotNull(message = MessageCode.QUESTION_NO_NOT_NULL)
	private Long questionNo;
	
}
