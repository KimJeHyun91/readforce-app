package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.passage.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorCategoryResponseDto {

	private Long categoryNo;
	
	private String category;
	
	private LocalDateTime createdAt;
	
	public AdministratorCategoryResponseDto(Category category) {
		
		this.categoryNo = category.getCategoryNo();
		this.category = category.getCategoryName().name();
		this.createdAt = category.getCreatedAt();		
		
	}
	
}
