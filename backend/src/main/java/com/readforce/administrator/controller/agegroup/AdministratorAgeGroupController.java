package com.readforce.administrator.controller.agegroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.administrator.dto.AdministratorAgeGroupRequestDto;
import com.readforce.administrator.dto.AdministratorAgeGroupResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.member.entity.AgeGroup;
import com.readforce.member.service.AgeGroupService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/age-group")
@RequiredArgsConstructor
@Validated
public class AdministratorAgeGroupController {

	private final AgeGroupService ageGroupService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-list")
	public ResponseEntity<List<AdministratorAgeGroupResponseDto>> getAllList(){
		
		List<AdministratorAgeGroupResponseDto> ageGroupList = ageGroupService.getAllList().stream()
				.map(AdministratorAgeGroupResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(ageGroupList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> create(
			@Valid @RequestBody AdministratorAgeGroupRequestDto requestDto
	){
		
		ageGroupService.createAgeGroup(AgeGroup.builder().ageGroup(requestDto.getAgeGroup()).build());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_AGE_GROUP_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("ageGroupNo")
			@NotNull(message = MessageCode.AGE_GROUP_NO_NOT_NULL)
			Long ageGroupNo
	){
		
		ageGroupService.deleteAgeGroup(ageGroupNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_AGE_GROUP_SUCCESS
		));
		
	}

}
