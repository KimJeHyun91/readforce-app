package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.question.entity.AverageQuestionSolvingTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorAverageQuetionSolvingTimeResponseDto {

	private final Long averageQuestionSolvingTimeNo;
	
	private final Long averageQuestionSolvingTime;
	
	private final LocalDateTime createdAt;
	
	private final LocalDateTime lastModifiedAt;
	
	private final Integer ageGroup;
	
	private final String category;
	
	private final String type;
	
	private final Integer level;
	
	private final String language;
	
	public AdministratorAverageQuetionSolvingTimeResponseDto(AverageQuestionSolvingTime averageQuestionSolvingTime) {
		
		this.averageQuestionSolvingTimeNo = averageQuestionSolvingTime.getAverageQuestionSolvingTimeNo();
		this.averageQuestionSolvingTime = averageQuestionSolvingTime.getAverageQuestionSolvingTime();
		this.createdAt = averageQuestionSolvingTime.getCreatedAt();
		this.lastModifiedAt = averageQuestionSolvingTime.getLastModifiedAt();
		this.ageGroup = averageQuestionSolvingTime.getAgeGroup().getAgeGroup();
		this.category = averageQuestionSolvingTime.getCategory().getCategoryName().name();
		this.type = averageQuestionSolvingTime.getType().getTypeName().name();
		this.level = averageQuestionSolvingTime.getLevel().getLevelNumber();
		this.language = averageQuestionSolvingTime.getLanguage().getLanguageName().name();
				
	}
	
	
}
