package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.ClassificationEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorClassificationModifyRequestDto {

	@NotNull(message = MessageCode.CLASSIFICATION_NO_NOT_NULL)
	private Long classificationNo;
	
	@NotNull(message = MessageCode.CLASSIFICATION_NOT_NULL)
	private ClassificationEnum classification;	
	
}
