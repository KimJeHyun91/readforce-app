package com.readforce.passage.dto;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PassageChangeFavoritStateRequestDto {

	@NotNull(message = MessageCode.PASSAGE_NO_NOT_NULL)
	private Long passageNo;
	
	@NotNull(message = MessageCode.IS_FAVORITE_NOT_NULL)
	private Boolean isFavorite;
	
}
