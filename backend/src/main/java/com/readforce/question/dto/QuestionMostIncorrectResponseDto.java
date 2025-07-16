package com.readforce.question.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestionMostIncorrectResponseDto {
	
	private final Long passageNo;

	private final Long questionNo;
	
	private final String title;
	
	private final LocalDateTime createdAt;
	
	private final boolean isCorrect;
	
	private final Double correctAnswerRate;
	
}
