package com.readforce.result.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.result.entity.Result;
import com.readforce.result.service.ResultMetricService;
import com.readforce.result.service.ResultService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/result_metric")
@RequiredArgsConstructor
@Validated
public class ResultMetricController {

	private final ResultMetricService resultMetricService;
	private final ResultService resultService;

	@GetMapping("/get-category-correct-answer-rate")
	public ResponseEntity<Map<String, Double>> getCategoryCorrectAnswerRate(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		Result result = resultService.getActiveMemberResultByEmail(email);
		
		Map<String, Double> categoryCorrectAnswerRate = resultMetricService.getCategoryCorrectAnswerRate(result);
		
		return ResponseEntity.status(HttpStatus.OK).body(categoryCorrectAnswerRate);
 		
	}
	
	@GetMapping("/get-type-correct-answer-rate-by-category")
	public ResponseEntity<Map<String, Double>> getTypeCorrectAnswerRateByCategory(
			@RequestParam("category")
			@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
			CategoryEnum category,
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		Result result = resultService.getActiveMemberResultByEmail(email);
		
		Map<String, Double> typeCorrectAnswerRate = resultMetricService.getTypeCorrectAnswerRate(result, category);
		
		return ResponseEntity.status(HttpStatus.OK).body(typeCorrectAnswerRate);
		
	}
	
}
