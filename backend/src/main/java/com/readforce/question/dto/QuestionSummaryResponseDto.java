package com.readforce.question.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.readforce.result.entity.Learning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestionSummaryResponseDto {
	
	private final Long passageNo;

	private final Long questionNo;
	
	private final String title;
	
	private final LocalDateTime createdAt;
	
	private final String author;
	
	private final String language;
	
	private final String category;
	
	private String content;
	
	@JsonProperty("isCorrect")
	private final boolean isCorrect;
	
	public QuestionSummaryResponseDto(Learning learning) {
		
		this.passageNo = learning.getQuestion().getPassage().getPassageNo();
		this.questionNo = learning.getQuestion().getQuestionNo();
		this.title = learning.getQuestion().getPassage().getTitle();
		this.createdAt = learning.getCreatedAt();
		this.isCorrect = learning.getIsCorrect();
	    this.author = learning.getQuestion().getPassage().getAuthor();
	    this.language = learning.getQuestion().getPassage().getLanguage().getLanguageName().name();
	    this.category = learning.getQuestion().getPassage().getCategory().getCategoryName().name();
	    this.content = learning.getQuestion().getPassage().getContent();
	}
	
}
