package com.readforce.member.dto;

import com.readforce.common.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberKeyInformationDto {

	private final String email;
	
	private final String password;
	
	private final RoleEnum role;
	
}
