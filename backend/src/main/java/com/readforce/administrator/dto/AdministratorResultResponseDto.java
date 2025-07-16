package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorResultResponseDto {

	private Long resultNo;
	
	private Integer learningStreak;
	
	private Double overallCorrectAnswerRate;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastModifiedAt;
	
	private String email;
		
}
