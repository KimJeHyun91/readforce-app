package com.readforce.member.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberSummaryDto {

	private final String email;
	
	private final String nickname;
	
	private final String socialProvider;
	
	private final LocalDate birthday;
	
}
