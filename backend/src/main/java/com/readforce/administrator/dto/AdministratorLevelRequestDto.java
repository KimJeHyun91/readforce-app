package com.readforce.administrator.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorLevelRequestDto {

	@NotNull(message = MessageCode.LEVEL_NOT_NULL)
	private Integer level;
	
	@NotNull(message = MessageCode.PARAGRAPH_COUNT_NOT_NULL)
	private Integer paragraphCount;
	
	@NotBlank(message = MessageCode.VOCABULARY_LEVEL_NOT_BLANK)
	private String vocabularyLevel;
	
	@NotBlank(message = MessageCode.SENTENCE_STRUCTURE_NOT_BLANK)
	private String sentenceStructure;
	
	@NotBlank(message = MessageCode.QUESTION_TYPE_NOT_BLANK)
	private String questionType;
	
}
