package com.readforce.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.question.dto.QuestionTestResponseDto;
import com.readforce.question.dto.QuestionTestResultDto;
import com.readforce.test.dto.TestSubmitRequestDto;
import com.readforce.test.service.TestService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Validated
public class TestController {

	private final TestService testService;
	
	@GetMapping("/start")
	public ResponseEntity<QuestionTestResponseDto> start(
			@RequestParam("language")
			@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
			LanguageEnum language
	){
		
		QuestionTestResponseDto startQuestion = testService.getTestQuestion(language, CategoryEnum.VOCABULARY, 6);
		
		return ResponseEntity.status(HttpStatus.OK).body(startQuestion);
		
	}
	
	@PostMapping("/submit-vocabulary-result")
	public ResponseEntity<QuestionTestResponseDto> submitVocabularyResult(
			@Valid @RequestBody TestSubmitRequestDto submitRequestDto
	){
		QuestionTestResponseDto nextQuestion = testService.submitVocabularyResult(submitRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(nextQuestion);
		
	}
	
	@PostMapping("/submit-factual-result")
	public ResponseEntity<?> submitFactualResult(
			@Valid @RequestBody TestSubmitRequestDto submitRequestDto
	){
		
		Object result = testService.submitFactualResult(submitRequestDto);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
		
	}
	
	
	@PostMapping("/submit-inferential-result")
	public ResponseEntity<QuestionTestResultDto> submitInferentialResult(
			@Valid @RequestBody TestSubmitRequestDto submitRequestDto
	){
		QuestionTestResultDto result = testService.submitInferentialResult(submitRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
		
	}

	
}
