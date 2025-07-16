package com.readforce.result.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.enums.NameEnum;
import com.readforce.result.service.LearningService;
import com.readforce.result.service.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {
	
	private final ResultService resultService;
	private final LearningService learningService;

	@GetMapping("/get-overall-correct-answer-rate")
	public ResponseEntity<Map<String, Double>> getOverallCorrectAnswerRate(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		Double overallCorrectAnswerRate = resultService.getOverallCorrectAnswerRate(email);
		
		Double formattedRate = Math.round(overallCorrectAnswerRate * 100) / 100.0;
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				NameEnum.OVERALL_CORRECT_ANSWER_RATE.name(), formattedRate
		));

	}
	
	@GetMapping("/get-today-solved-question-count")
	public ResponseEntity<Map<String, Integer>> getTodaySolvedQuestionCount(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		Integer count = learningService.getTodaySolvedQuestionCount(email);

		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				NameEnum.TODAY_SOLVED_QUESTION_COUNT.name(), count
		));
		
	}
	
}
