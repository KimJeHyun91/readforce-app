package com.readforce.ai.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.ai.dto.AiGeneratePassageRequestDto;
import com.readforce.ai.service.AiService;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.question.dto.QuestionRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Validated
public class AiController {

    private final AiService aiService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate-test-passage")
    public ResponseEntity<Map<String, String>> generateTestPassage(
            @RequestParam("language")
            @NotNull LanguageEnum language
    ) {
    	
        aiService.generateTestVocabulary(language);
        
        return ResponseEntity.ok(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_TEST_PASSAGE_SUCCESS));
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate-test-question")
    public ResponseEntity<Map<String, String>> generateTestQuestion(
            @RequestParam("language")
            @NotNull LanguageEnum language
    ) {
    	
        aiService.generateTestQuestion(language);
        
        return ResponseEntity.ok(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_TEST_QUESTION_SUCCESS));
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate-passage")
    public ResponseEntity<Map<String, String>> generatePassage(
            @Valid @RequestBody AiGeneratePassageRequestDto aiGeneratePassageRequestDto
    ) {
    	
        aiService.generatePassage(aiGeneratePassageRequestDto);
        
        return ResponseEntity.ok(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_PASSAGE_SUCCESS));
 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate-question")
    public ResponseEntity<Map<String, String>> generateQuestion() {
    	
        aiService.generateQuestion();
        
        return ResponseEntity.ok(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_QUESTION_SUCCESS));
        
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate-question-by-passage-no")
    public ResponseEntity<Map<String, String>> generateQuestionByPassageNo(
    		@Valid @RequestBody QuestionRequestDto requestDto
    ){
    	
    	aiService.generateQuestionByPassageNo(requestDto.getPassageNo());
    	
    	return ResponseEntity.ok(Map.of(MessageCode.MESSAGE_CODE, MessageCode.GENERATE_QUESTION_SUCCESS));
    	
    }
    
}