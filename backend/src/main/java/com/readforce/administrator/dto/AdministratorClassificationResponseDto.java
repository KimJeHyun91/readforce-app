package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.passage.entity.Classification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorClassificationResponseDto {

	private Long classificationNo;
	
	private String classification;
	
	private LocalDateTime createdAt;
	
	public AdministratorClassificationResponseDto(Classification classification) {
		
		this.classificationNo = classification.getClassificationNo();
		this.classification = classification.getClassificationName().name();
		this.createdAt = classification.getCreatedAt();
		
	}
	
}
