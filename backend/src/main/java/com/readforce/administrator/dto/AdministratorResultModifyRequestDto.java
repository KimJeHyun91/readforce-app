package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorResultModifyRequestDto {

	@NotNull(message = MessageCode.RESULT_NO_NOT_NULL)
	private Long resultNo;
	
	private Integer learningStreak;
	
	private Double overallCorrectAnswerRate;
	
	
}
