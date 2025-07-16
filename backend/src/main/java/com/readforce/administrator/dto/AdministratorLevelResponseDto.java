package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.passage.entity.Level;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorLevelResponseDto {

	private Long levelNo;
	
	private Integer level;
	
	private Integer paragraphCount;
	
	private String vocabularyLevel;
	
	private String sentenceStructure;
	
	private String questionType;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastModifiedAt;
	
	public AdministratorLevelResponseDto(Level level) {
		
		this.levelNo = level.getLevelNo();
		this.level = level.getLevelNumber();
		this.paragraphCount = level.getParagraphCount();
		this.vocabularyLevel = level.getVocabularyLevel();
		this.sentenceStructure = level.getSentenceStructure();
		this.questionType = level.getQuestionType();
		this.createdAt = level.getCreatedAt();
		this.lastModifiedAt = level.getLastModifiedAt();
		
	}
	
}
