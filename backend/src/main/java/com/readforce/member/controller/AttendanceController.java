package com.readforce.member.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.member.service.AttendanceService;
import com.readforce.result.service.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	private final ResultService resultService;
	
	@GetMapping("/get-attendance-date-list")
	public ResponseEntity<List<LocalDate>> getAttendanceDateList(@AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		
		List<LocalDate> attendanceDateList = attendanceService.getAttendanceDateList(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(attendanceDateList);
		
	}

	@GetMapping("/get-learning-streak")
	public ResponseEntity<Map<String, Integer>> getLearningStreak(@AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		
		Integer learningStreak = resultService.getLearningStreak(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("learningStreak", learningStreak));
		
	}
	
	
	

}
