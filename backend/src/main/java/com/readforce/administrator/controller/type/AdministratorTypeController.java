package com.readforce.administrator.controller.type;

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

import com.readforce.administrator.dto.AdministratorTypeModifyRequestDto;
import com.readforce.administrator.dto.AdministratorTypeRequestDto;
import com.readforce.administrator.dto.AdministratorTypeResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.passage.entity.Type;
import com.readforce.passage.service.TypeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/type")
@RequiredArgsConstructor
@Validated
public class AdministratorTypeController {

	private final TypeService typeService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-list")
	public ResponseEntity<List<AdministratorTypeResponseDto>> getAllList(){
		
		List<AdministratorTypeResponseDto> typeList = typeService.getAllTypeList().stream()
				.map(AdministratorTypeResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(typeList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> create(
		@Valid @RequestBody AdministratorTypeRequestDto requestDto	
	){
		
		typeService.saveType(Type.builder().typeName(requestDto.getType()).build());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_TYPE_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(
			@Valid @RequestBody AdministratorTypeModifyRequestDto requestDto
	){
		
		typeService.modifyType(requestDto.getTypeNo(), requestDto.getType());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_TYPE_SUCCESS
		));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("typeNo")
			@NotNull(message = MessageCode.TYPE_NO_NOT_NULL)
			Long typeNo
	){
		
		typeService.deleteTypeByTypeNo(typeNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_TYPE_SUCCESS
		));
		
	}
	
}
