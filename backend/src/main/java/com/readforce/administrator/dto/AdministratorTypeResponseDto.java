package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.passage.entity.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorTypeResponseDto {

	private Long typeNo;
	
	private String typeName;
	
	private LocalDateTime createdAt;
	
	public AdministratorTypeResponseDto(Type type) {
		
		this.typeNo = type.getTypeNo();
		this.typeName = type.getTypeName().name();
		this.createdAt = type.getCreatedAt();
		
	}
	
}
