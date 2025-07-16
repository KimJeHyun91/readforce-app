package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorScoreRequestDto {

	@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	private String email;
	
	@NotNull(message = MessageCode.SCORE_NOT_NULL)
	private Double score;
	
	@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
	private CategoryEnum category;
	
	@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
	private LanguageEnum language;
	
}
