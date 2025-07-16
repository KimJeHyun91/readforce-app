package com.readforce.administrator.controller.category;

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

import com.readforce.administrator.dto.AdministratorCategoryModifyRequestDto;
import com.readforce.administrator.dto.AdministratorCategoryRequestDto;
import com.readforce.administrator.dto.AdministratorCategoryResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.passage.entity.Category;
import com.readforce.passage.service.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/category")
@RequiredArgsConstructor
@Validated
public class AdministratorCategoryController {
	
	private final CategoryService categoryService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-list")
	public ResponseEntity<List<AdministratorCategoryResponseDto>> getAllList(){
		
		List<AdministratorCategoryResponseDto> categoryList = categoryService.getAllCategoryList().stream()
				.map(AdministratorCategoryResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(categoryList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> create(
			@Valid @RequestBody AdministratorCategoryRequestDto requestDto
	){
		
		categoryService.saveCategory(Category.builder().categoryName(requestDto.getCategory()).build());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_CATEGORY_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(
			@Valid @RequestBody AdministratorCategoryModifyRequestDto requestDto
	){
		
		categoryService.modifyCategory(requestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_CATEGORY_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("categoryNo")
			@NotNull(message = MessageCode.CATEGORY_NO_NOT_NULL)
			Long categoryNo
	){
		
		categoryService.deleteCategory(categoryNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_CATEGORY_SUCCESS
		));
		
	}

}
