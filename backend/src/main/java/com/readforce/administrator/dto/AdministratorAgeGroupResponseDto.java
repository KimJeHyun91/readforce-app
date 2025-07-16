package com.readforce.administrator.dto;

import java.time.LocalDateTime;

import com.readforce.member.entity.AgeGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorAgeGroupResponseDto {

	private Long ageGroupNo;
	
	private Integer ageGroup;
	
	private LocalDateTime createdAt;
	
	public AdministratorAgeGroupResponseDto(AgeGroup ageGroup) {
		
		this.ageGroupNo = ageGroup.getAgeGroupNo();
		this.ageGroup = ageGroup.getAgeGroup();
		this.createdAt = ageGroup.getCreatedAt();		
		
	}
	
}
