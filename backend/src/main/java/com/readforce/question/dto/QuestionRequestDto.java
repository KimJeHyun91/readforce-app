package com.readforce.question.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionRequestDto {

	@NotNull(message = MessageCode.PASSAGE_NO_NOT_NULL)
	private Long passageNo;
	
}
