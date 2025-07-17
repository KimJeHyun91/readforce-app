package com.readforce.result.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.passage.dto.PassageResponseDto;
import com.readforce.question.dto.QuestionSummaryResponseDto;
import com.readforce.result.dto.LearningMultipleChoiceRequestDto;
import com.readforce.result.service.LearningService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/learning")
@RequiredArgsConstructor
public class LearningController {
	
	private final LearningService learningService;
	
	// @PostMapping("/save-multiple-choice")
	// public ResponseEntity<Map<String, String>> saveMultipleChoice(
	// 		@Valid @RequestBody LearningMultipleChoiceRequestDto learningMultipleChoiceRequestDto,
	// 		@AuthenticationPrincipal UserDetails userDetails
	// ){
		
	// 	String email = userDetails.getUsername();
		
	// 	learningService.saveMuiltipleChoice(email, learningMultipleChoiceRequestDto);
		
	// 	return ResponseEntity.status(HttpStatus.OK).body(Map.of(
	// 			MessageCode.MESSAGE_CODE, MessageCode.SAVE_MULTIPLE_CHOICE_RESULT_SUCCESS
	// 	));
		
	// }
	
	@GetMapping("/get-total-learning")
	public ResponseEntity<List<QuestionSummaryResponseDto>> getTotalLearning(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		List<QuestionSummaryResponseDto> totalLearningList = learningService.getTotalLearning(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(totalLearningList);
		
	}
	
	@GetMapping("/get-total-incorrect-learning")
	public ResponseEntity<List<QuestionSummaryResponseDto>> getTotalIncorrectLearning(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		List<QuestionSummaryResponseDto> totalIncorrectLearningList = learningService.getTotalIncorrectLearning(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(totalIncorrectLearningList);
		
	}
	
	@GetMapping("/get-today-learning")
	public ResponseEntity<List<QuestionSummaryResponseDto>> getTodayLearning(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		List<QuestionSummaryResponseDto> todayLearningList = learningService.getTodayLearning(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(todayLearningList);
		
	}
	
	@GetMapping("/get-today-incorrect-learning")
	public ResponseEntity<List<QuestionSummaryResponseDto>> getTodayIncorrectLearning(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		List<QuestionSummaryResponseDto> todayIncorrectLearningList = learningService.getTodayIncorrectLearning(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(todayIncorrectLearningList);
		
	}
	
	@GetMapping("/get-favorit-learning")
	public ResponseEntity<List<QuestionSummaryResponseDto>> getFavoritLearning(
			@AuthenticationPrincipal UserDetails userDetails
	){
		
		String email = userDetails.getUsername();
		
		List<QuestionSummaryResponseDto> favoritLearningList = learningService.getFavoritLearning(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(favoritLearningList);
		
	}
	
	@GetMapping("/get-most-incorrect-passages")
	public ResponseEntity<List<PassageResponseDto>> getMostIncorrectPassages(
			@RequestParam("language")
			@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
			LanguageEnum language,
			@RequestParam("number")
			@NotNull(message = MessageCode.NUMBER_NOT_NULL)
			Integer number
	){
		
		List<PassageResponseDto> incorrectPassgeList = learningService.getMostIncorrectPassages(language, number);
		
		return ResponseEntity.status(HttpStatus.OK).body(incorrectPassgeList);
		
	}
		
}
