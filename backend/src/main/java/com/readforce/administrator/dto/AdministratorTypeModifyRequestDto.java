package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.TypeEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorTypeModifyRequestDto {

	@NotNull(message = MessageCode.TYPE_NO_NOT_NULL)
	private Long typeNo;
	
	@NotNull(message = MessageCode.TYPE_NOT_NULL)
	private TypeEnum type;
	
}
