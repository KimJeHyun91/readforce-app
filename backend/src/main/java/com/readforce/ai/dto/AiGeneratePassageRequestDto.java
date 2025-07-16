package com.readforce.ai.dto;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.ClassificationEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiGeneratePassageRequestDto {

	@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
	private LanguageEnum language;
	
	@NotNull(message = MessageCode.CLASSIFICATION_NOT_NULL)
	private ClassificationEnum classification;
	
	@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
	private CategoryEnum category;
	
	@NotNull(message = MessageCode.TYPE_NOT_NULL)
	private TypeEnum type;
	
	@NotNull(message = MessageCode.LEVEL_NOT_NULL)
	private Integer level;
		
	private Integer count = 1; 

}
