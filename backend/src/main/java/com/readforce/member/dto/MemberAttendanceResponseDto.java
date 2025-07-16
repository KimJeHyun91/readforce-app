package com.readforce.member.dto;

import java.time.LocalDate;

import com.readforce.member.entity.Attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberAttendanceResponseDto {

	private Long attendanceNo;
	
	private LocalDate attendanceDate;
	
	public MemberAttendanceResponseDto(Attendance attendance) {
		
		this.attendanceNo = attendance.getAttendanceNo();
		this.attendanceDate = attendance.getAttendanceDate();
		
	}
	
}