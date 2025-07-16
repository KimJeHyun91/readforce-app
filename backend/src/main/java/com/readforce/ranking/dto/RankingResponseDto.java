package com.readforce.ranking.dto;

import com.readforce.result.entity.Score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RankingResponseDto {

	private final String nickname;
	
	private final String email;
	
	private final Double score;
	
	private final String category;
	
	private final String language;
	
	public RankingResponseDto(Score score) {
		
		this.nickname = score.getMember().getNickname();
		this.email = score.getMember().getEmail();
		this.score = score.getScore();
		this.category = score.getCategory().getCategoryName().name();
		this.language = score.getLanguage().getLanguageName().name();
		
	}
		
}
