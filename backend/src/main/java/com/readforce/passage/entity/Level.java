package com.readforce.passage.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Level {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long levelNo;
	
	@Column(nullable = false, unique = true)
	private Integer levelNumber;
	
	@Column(nullable = false)
	private Integer paragraphCount;
	
	@Column(nullable = false)
	private String vocabularyLevel;
	
	@Column(nullable = false)
	private String sentenceStructure;
	
	@Column(nullable = false)
	private String questionType;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;
	
	public void changeInfo(
			Integer levelNumber, 
			Integer paragraphCount, 
			String vocabularyLevel,
			String sentenceStructure,
			String questionType
	) {
		
		if(levelNumber != null) {
			
			this.levelNumber = levelNumber;
		}
		
		if(paragraphCount != null) {
			
			this.paragraphCount = paragraphCount;
			
		}
		
		if(!vocabularyLevel.isBlank()) {
			
			this.vocabularyLevel = vocabularyLevel;
			
		}
		
		if(!sentenceStructure.isBlank())
		{
			
			this.sentenceStructure = sentenceStructure;
			
		}
		
		if(!questionType.isBlank()) {
			
			this.questionType = questionType;
			
		}
			
	}
	
	
}
