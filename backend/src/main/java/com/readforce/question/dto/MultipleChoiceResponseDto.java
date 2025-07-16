package com.readforce.question.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.readforce.question.entity.MultipleChoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MultipleChoiceResponseDto {

	private final Long passageNo;
	
	private final String title;

	private final String content;
	
	private final String author;
	
	private final LocalDate publicationDate;
	
	private final String category;
	
	private final Integer level;
	
	private final Long questionNo;
	
	private final String question;
	
	private final List<ChoiceDto> choiceList;
	
	public MultipleChoiceResponseDto(MultipleChoice multipleChoice) {
		
		this.passageNo = multipleChoice.getPassage().getPassageNo();
		this.title = multipleChoice.getPassage().getTitle();
		this.content = multipleChoice.getPassage().getContent();
		this.author = multipleChoice.getPassage().getAuthor();
		this.publicationDate = multipleChoice.getPassage().getPublicationDate();
		this.category = multipleChoice.getPassage().getCategory().getCategoryName().name();
		this.level = multipleChoice.getPassage().getLevel().getLevelNumber();
		this.questionNo = multipleChoice.getQuestionNo();
		this.question = multipleChoice.getQuestion();
		this.choiceList = multipleChoice.getChoiceList().stream()
				.map(ChoiceDto::new)
				.collect(Collectors.toList());
		
	}

}
