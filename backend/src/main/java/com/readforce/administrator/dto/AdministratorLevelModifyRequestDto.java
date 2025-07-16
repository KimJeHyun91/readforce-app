package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorLevelModifyRequestDto {
	
	@NotNull(message = MessageCode.LEVEL_NO_NOT_NULL)
	private Long levelNo;

	private Integer level;
	
	private Integer paragraphCount;
	
	private String vocabularyLevel;
	
	private String sentenceStructure;
	
	private String questionType;
	
}
