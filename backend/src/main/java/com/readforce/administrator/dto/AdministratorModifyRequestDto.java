package com.readforce.administrator.dto;

import java.time.LocalDate;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.RoleEnum;
import com.readforce.common.enums.StatusEnum;
import com.readforce.member.validation.ValidBirthday;
import com.readforce.member.validation.ValidNickname;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorModifyRequestDto {
	
	@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	private String email;

	@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
	@ValidNickname
	private String nickname;
	
	@ValidBirthday
	private LocalDate birthday;
	
	private StatusEnum status;
	
	private RoleEnum role;
	
}
