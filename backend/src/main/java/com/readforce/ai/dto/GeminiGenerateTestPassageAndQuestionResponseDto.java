package com.readforce.ai.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeminiGenerateTestPassageAndQuestionResponseDto {

	private String title;
	
	private String content;
	
	private String question;
	
	private List<String> choiceList;
	
	private String correctAnswerIndex;
	
	private Map<String, String> explanation;
	
}
