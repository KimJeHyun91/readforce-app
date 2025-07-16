package com.readforce.member.dto;

import com.readforce.common.MessageCode;
import com.readforce.member.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberPasswordResetFromLinkDto {

	@NotBlank(message = MessageCode.TEMPORAL_TOKEN_NOT_BLANK)
	private String temporalToken;
	
	@ValidPassword
	private String newPassword;
	
}
