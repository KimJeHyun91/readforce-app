package com.readforce.administrator.controller.level;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.administrator.dto.AdministratorLevelModifyRequestDto;
import com.readforce.administrator.dto.AdministratorLevelRequestDto;
import com.readforce.administrator.dto.AdministratorLevelResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.passage.entity.Level;
import com.readforce.passage.service.LevelService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/level")
@RequiredArgsConstructor
@Validated
public class AdministratorLevelController {

	private final LevelService levelService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-list")
	public ResponseEntity<List<AdministratorLevelResponseDto>> getAllList(){
		
		List<AdministratorLevelResponseDto> levelList = levelService.getAllLevelList().stream()
				.map(AdministratorLevelResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(levelList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> create(
		@Valid @RequestBody AdministratorLevelRequestDto requestDto
	){
		
		levelService.saveLevel(Level.builder()
				.levelNumber(requestDto.getLevel())
				.paragraphCount(requestDto.getParagraphCount())
				.vocabularyLevel(requestDto.getVocabularyLevel())
				.sentenceStructure(requestDto.getSentenceStructure())
				.questionType(requestDto.getQuestionType())
				.build());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_LEVEL_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(
			@Valid @RequestBody AdministratorLevelModifyRequestDto requestDto
	){
		
		levelService.modifyLevel(
				requestDto.getLevelNo(),
				requestDto.getLevel(),
				requestDto.getParagraphCount(),
				requestDto.getVocabularyLevel(),
				requestDto.getSentenceStructure(),
				requestDto.getQuestionType()				
		);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_LEVEL_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("levelNo")
			@NotNull(message = MessageCode.LEVEL_NO_NOT_NULL)
			Long levelNo
	){
		
		levelService.deleteLevelByLevelNo(levelNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LEVEL_SUCCESS	
		));
		
	}
	
	
}
