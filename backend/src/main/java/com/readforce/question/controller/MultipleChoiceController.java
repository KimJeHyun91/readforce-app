package com.readforce.question.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.question.dto.MultipleChoiceResponseDto;
import com.readforce.question.service.MultipleChoiceService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/multiple_choice")
@RequiredArgsConstructor
@Validated
public class MultipleChoiceController {

	private final MultipleChoiceService multipleChoiceService;
	
	@GetMapping("/get-multiple-choice-question-list")
	public ResponseEntity<List<MultipleChoiceResponseDto>> getMultipleChoiceQuestionList(
			@RequestParam("passageNo")
			@NotNull(message = MessageCode.PASSAGE_NO_NOT_NULL)
			Long passageNo
	){
		
		List<MultipleChoiceResponseDto> multipleChoiceQuestionList = multipleChoiceService.getMultipleChoiceQuestionListByPassageNo(passageNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(multipleChoiceQuestionList);
		
	}
	
	
	
}
