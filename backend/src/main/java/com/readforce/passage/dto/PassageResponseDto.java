package com.readforce.passage.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.readforce.common.enums.ClassificationEnum;
import com.readforce.passage.entity.Passage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PassageResponseDto {

	private Long passageNo;

	private String title;

	private String content;

	private String author;

	private LocalDate publicationDate;

	private LocalDateTime createdAt;

	private String category;

	private String type;

	private Integer level;
	
	private String language;

	private ClassificationEnum classification;
	
	public PassageResponseDto(Passage passage) {
		
		this.passageNo = passage.getPassageNo();
		this.title = passage.getTitle();
		this.content = passage.getContent();
		this.author = passage.getAuthor();
		this.publicationDate = passage.getPublicationDate();
		this.createdAt = passage.getCreatedAt();
		this.category = passage.getCategory().getCategoryName().name();
	    this.type = passage.getType() != null ? passage.getType().getTypeName().name() : null;
		this.level = passage.getLevel().getLevelNumber();
		this.language = passage.getLanguage().getLanguageName().name();	
	    this.classification = passage.getClassification().getClassificationName();

	}

}
