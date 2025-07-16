package com.readforce.member.dto;

import java.time.LocalDate;

import com.readforce.common.MessageCode;
import com.readforce.member.validation.ValidBirthday;
import com.readforce.member.validation.ValidNickname;
import com.readforce.member.validation.ValidPassword;

import jakarta.validation.constraints.Email;
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
public class MemberSignUpDto {

	@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	private String email;
	
	@NotBlank(message = MessageCode.PASSWORD_NOT_BLANK)
	@Size(min = 8, max = 20, message = MessageCode.PASSWORD_SIZE_INVALID)
	@ValidPassword
	private String password;
	
	@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
	@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
	@ValidNickname
	private String nickname;
	
	@NotNull(message = MessageCode.BIRTHDAY_NOT_NULL)
	@ValidBirthday
	private LocalDate birthday;
	
}
