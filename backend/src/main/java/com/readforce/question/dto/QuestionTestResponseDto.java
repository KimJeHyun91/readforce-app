package com.readforce.question.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestionTestResponseDto {

	private final String testerId;

	private final Long passageNo;
	
	private final String title;

	private final String content;
	
	private final String author;
	
	private final LocalDate publicationDate;
	
	private final String category;
	
	private final Integer level;
	
	private final Long questionNo;
	
	private final Long multipleChoiceNo;
	
	private final String question;
	
	private final List<ChoiceDto> choiceList;	
	
}
