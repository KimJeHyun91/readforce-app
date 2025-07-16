package com.readforce.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberSocialProviderDto {

	private String socialProvider;
	
}
