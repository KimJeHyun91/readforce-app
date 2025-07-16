package com.readforce.administrator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorResultMetricModifyRequestDto {

	private Long resultMetricNo;
	
	private Double correctAnswerRate;
	
	private Long questionSolvingTimeAverage;
	
}
