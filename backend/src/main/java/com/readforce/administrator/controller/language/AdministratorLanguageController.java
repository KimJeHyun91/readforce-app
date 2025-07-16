package com.readforce.administrator.controller.language;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.administrator.dto.AdministratorLanguageModifyRequestDto;
import com.readforce.administrator.dto.AdministratorLanguageRequestDto;
import com.readforce.administrator.dto.AdministratorLanguageResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.passage.entity.Language;
import com.readforce.passage.service.LanguageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/language")
@RequiredArgsConstructor
@Validated
public class AdministratorLanguageController {

	private final LanguageService languageService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-list")
	public ResponseEntity<List<AdministratorLanguageResponseDto>> getAllList(){
		
		List<AdministratorLanguageResponseDto> languageList = languageService.getAllLanguageList().stream()
				.map(AdministratorLanguageResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(languageList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> create(
			@Valid @RequestBody AdministratorLanguageRequestDto requestDto
	){
		
		languageService.createLanguage(Language.builder().languageName(requestDto.getLanguage()).build());

		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_LANGUAGE_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(
			@Valid @RequestBody AdministratorLanguageModifyRequestDto requestDto
	){
		
		languageService.modifyLanguage(requestDto.getLanguageNo(), requestDto.getLanguage());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_LANGUAGE_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("languageNo")
			@NotNull(message = MessageCode.LANGUAGE_NO_NOT_NULL)
			Long languageNo
	){
		
		languageService.deleteLanguageByLanguageNo(languageNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_LANGUAGE_SUCCESS
		));
		
	}
	
}
