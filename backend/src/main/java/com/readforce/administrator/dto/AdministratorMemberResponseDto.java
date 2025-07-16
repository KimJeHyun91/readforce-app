package com.readforce.administrator.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.readforce.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdministratorMemberResponseDto {

	private String email;
	
	private String nickname;
	
	private LocalDate birthday;
	
	private String profileImagePath;
	
	private String status;
	
	private String role;
	
	private String socialProvider;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastModifiedAt;
	
	private LocalDateTime withdrawAt;
	
	public AdministratorMemberResponseDto(Member member) {
		
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.birthday = member.getBirthday();
		this.profileImagePath = member.getProfileImagePath();
		this.status = member.getStatus().name();
		this.role = member.getRole().name();
		this.socialProvider = member.getSocialProvider();
		this.createdAt = member.getCreatedAt();
		this.lastModifiedAt = member.getLastModifiedAt();
		this.withdrawAt = member.getWithdrawAt();		
		
	}
	
	
}
