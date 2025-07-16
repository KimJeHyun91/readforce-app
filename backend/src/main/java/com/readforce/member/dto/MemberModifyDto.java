package com.readforce.member.dto;

import java.time.LocalDate;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.RoleEnum;
import com.readforce.common.enums.StatusEnum;
import com.readforce.member.validation.ValidBirthday;
import com.readforce.member.validation.ValidNickname;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberModifyDto {

	@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
	@ValidNickname
	private String nickname;
	
	@ValidBirthday
	private LocalDate birthday;
	
	private StatusEnum status;
	
	private RoleEnum role;
	
}
