package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.result.entity.ResultMetric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorResultMetricResponseDto {

	private Long resultMetricNo;
	
	private Double correctAnswerRate;
	
	private Long questionSolvingTimeAverage;
	
	private LocalDateTime createAt;
	
	private LocalDateTime lastModifiedAt;
	
	private Long resultNo;
	
	private String category;
	
	private String type;
	
	private Integer level;
	
	private String language;
	
	public AdministratorResultMetricResponseDto(ResultMetric resultMetric) {
		
		this.resultMetricNo = resultMetric.getResultMetricNo();
		this.correctAnswerRate = resultMetric.getCorrectAnswerRate();
		this.questionSolvingTimeAverage = resultMetric.getQuestionSolvingTimeAverage();
		this.createAt = resultMetric.getCreatedAt();
		this.lastModifiedAt = resultMetric.getLastModifiedAt();
		this.resultNo = resultMetric.getResult().getResultNo();
		this.category = resultMetric.getCategory().getCategoryName().name();
		this.type = resultMetric.getType().getTypeName().name();
		this.level = resultMetric.getLevel().getLevelNumber();
		this.language = resultMetric.getLanguage().getLanguageName().name();
		
	}
	
}
