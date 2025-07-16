package com.readforce.ranking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.ranking.dto.RankingResponseDto;
import com.readforce.result.service.ScoreService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
@Validated
public class RankingController {
	
	private final ScoreService scoreService;

	@GetMapping("/get-ranking-list")
	public ResponseEntity<List<RankingResponseDto>> getRankingList(
			@RequestParam("category")
			@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
			CategoryEnum category,
			@RequestParam("language")
			@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
			LanguageEnum language
	){
		
		List<RankingResponseDto> rankingList = scoreService.getTop50ByCategory(category, language);
		
		return ResponseEntity.status(HttpStatus.OK).body(rankingList);
		
	}
	
	
}
