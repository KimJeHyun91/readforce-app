package com.readforce.administrator.controller.passage;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.administrator.dto.AdministratorUploadPassageRequestDto;
import com.readforce.common.MessageCode;
import com.readforce.passage.service.PassageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/passage")
@RequiredArgsConstructor
@Validated
public class AdministratorPassageController {

	private final PassageService passageService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/upload-passage")
	public ResponseEntity<Map<String, String>> uploadPassage(
			@Valid @RequestBody AdministratorUploadPassageRequestDto requestDto	
	){
		
		passageService.uploadPassage(requestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.UPLOAD_PASSAGE_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
		@RequestParam("passageNo")
		@NotNull(message = MessageCode.PASSAGE_NO_NOT_NULL)
		Long passageNo
	){
		
		passageService.deletePassage(passageNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_PASSAGE_SUCCESS
		));		
		
	}
	
	
}