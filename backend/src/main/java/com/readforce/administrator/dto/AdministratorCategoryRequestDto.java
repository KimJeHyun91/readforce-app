package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorCategoryRequestDto {

	@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
	private CategoryEnum category;
	
}
