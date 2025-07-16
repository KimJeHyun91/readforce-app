package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.result.entity.Score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorScoreResponseDto {

	private Long scoreNo;
	
	private Double score;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastModifiedAt;
	
	private String category;
	
	private String email;
	
	private String language;
	
	public AdministratorScoreResponseDto(Score score) {
		
		this.scoreNo = score.getScoreNo();
		this.score = score.getScore();
		this.createdAt = score.getCreatedAt();
		this.lastModifiedAt = score.getLastModifiedAt();
		this.category = score.getCategory().getCategoryName().name();
		this.email = score.getMember().getEmail();
		this.language = score.getLanguage().getLanguageName().name();
		
	}
	
}
