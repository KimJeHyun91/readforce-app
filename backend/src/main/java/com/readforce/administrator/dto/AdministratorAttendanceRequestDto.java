package com.readforce.administrator.dto;

import java.time.LocalDate;

import com.readforce.common.MessageCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdministratorAttendanceRequestDto {

	@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
	@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
	private String email;
	
	@NotNull(message = MessageCode.ATTENDANCE_DATE_NOT_NULL)
	private LocalDate attendanceDate;

}