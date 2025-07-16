package com.readforce.challenge.dto;

import java.util.List;
import java.util.Map;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeSubmitResultRequestDto {
	
	@NotNull(message = MessageCode.SELECTED_INDEX_LIST_NOT_NULL)
	private List<Map<Long, Integer>> selecetedIndexList;
	
	@NotNull(message = MessageCode.TOTAL_QUESTION_SOLVING_TIME_NOT_NULL)
	private Long totalQuestionSolvingTime;
	
	@NotNull(message = MessageCode.LANGUAGE_NOT_NULL)
	private LanguageEnum language;
	
	@NotNull(message = MessageCode.CATEGORY_NOT_NULL)
	private CategoryEnum category;

}
