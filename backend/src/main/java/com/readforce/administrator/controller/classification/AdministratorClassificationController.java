package com.readforce.administrator.controller.classification;

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

import com.readforce.administrator.dto.AdministratorClassificationModifyRequestDto;
import com.readforce.administrator.dto.AdministratorClassificationRequestDto;
import com.readforce.administrator.dto.AdministratorClassificationResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.passage.entity.Classification;
import com.readforce.passage.service.ClassificationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/classification")
@RequiredArgsConstructor
@Validated
public class AdministratorClassificationController {

	private final ClassificationService classificationService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-list")
	public ResponseEntity<List<AdministratorClassificationResponseDto>> getAllList(){
		
		List<AdministratorClassificationResponseDto> classificationList =
				classificationService.getAllClassificationList().stream()
				.map(AdministratorClassificationResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(classificationList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> create(
			@Valid @RequestBody AdministratorClassificationRequestDto requestDto
	){
		
		classificationService.saveClassification(Classification.builder().classificationName(requestDto.getClassification()).build());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_CLASSIFICATION_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(
			@Valid @RequestBody AdministratorClassificationModifyRequestDto requestDto
	){
		
		classificationService.modifyClassification(requestDto.getClassificationNo(), requestDto.getClassification());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_CLASSIFICATION_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("classificationNo")
			@NotNull(message = MessageCode.CLASSIFICATION_NO_NOT_NULL)
			Long classificationNo
	){
		
		classificationService.deleteByClassificationNo(classificationNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_CLASSIFICATION_SUCCESS
		));
		
	}
}
