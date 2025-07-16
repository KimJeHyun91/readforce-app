package com.readforce.member.dto;

import java.time.LocalDate;

import com.readforce.common.MessageCode;
import com.readforce.member.validation.ValidBirthday;
import com.readforce.member.validation.ValidNickname;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSocialSignUpDto {
	
	@NotBlank(message = MessageCode.TEMPORAL_TOKEN_NOT_BLANK)
	private String temporalToken;
	
	@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
	@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
	@ValidNickname
	private String nickname;
	
	@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
	@ValidBirthday
	private LocalDate birthday;
	
}
