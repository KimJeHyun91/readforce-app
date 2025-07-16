package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorCategoryModifyRequestDto {

	@NotNull(message = MessageCode.CATEGORY_NO_NOT_NULL)
	private Long categoryNo;
	
	@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
	private CategoryEnum category;
	
}
