package com.readforce.member.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.exception.DuplicationException;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Attendance;
import com.readforce.member.entity.Member;
import com.readforce.member.repository.AttendanceRepository;
import com.readforce.result.entity.Result;
import com.readforce.result.service.ResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
	private final ResultService resultService;
	private final MemberService memberService;
	
	@Transactional
	public void recordAttendance(String email) {

		if(attendanceRepository.findByMember_EmailAndAttendanceDate(email, LocalDate.now()).isPresent()) {
			
			return;
			
		}
		
		Member member = memberService.getActiveMemberByEmail(email);
		
		Attendance attendance = Attendance.builder()
				.member(member)
				.attendanceDate(LocalDate.now())
				.build();
		
		attendanceRepository.save(attendance);
		
		updateLearningStreak(email);
		
	}

	private void updateLearningStreak(String email) {
		
		Result result = resultService.getActiveMemberResultByEmail(email);
		
		LocalDate yesterday = LocalDate.now().minusDays(1);
		
		boolean attendedYesterday = attendanceRepository
				.findByMember_EmailAndAttendanceDate(email, yesterday)
				.isPresent();
		
		result.updateLearningStreak(attendedYesterday);
		
	}

	@Transactional(readOnly = true)
	public List<LocalDate> getAttendanceDateList(String email) {
		
		List<Attendance> attendanceList = attendanceRepository.findAllByMember_Email(email);
		
		if(attendanceList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.ATTENDANCE_NOT_FOUND);
			
		}
		
		return attendanceList
				.stream()
				.map(attendance -> attendance.getAttendanceDate())
				.collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public List<Attendance> getAttendanceListByEmail(String email) {

		return attendanceRepository.findAllByMember_Email(email);

	}

	@Transactional
	public void addAttendance(String email, LocalDate attendanceDate) {

		if(attendanceRepository.findByMember_EmailAndAttendanceDate(email, attendanceDate).isPresent()) {
			
			throw new DuplicationException(MessageCode.TODAY_ALREADY_ATTENDANCE);
			
		}
		
		Member member = memberService.getActiveMemberByEmail(email);
		
		Attendance attendance = Attendance.builder()
				.member(member)
				.attendanceDate(attendanceDate)
				.build();
		
		attendanceRepository.save(attendance);
		
		recalculateLearningStreak(email);
		
	}

	@Transactional
	private void recalculateLearningStreak(String email) {

		Result result = resultService.getActiveMemberResultByEmail(email);
		
		List<LocalDate> attendanceDateList = getAttendanceDateList(email);
		
		if(attendanceDateList.isEmpty()) {
			
			result.resetLearningStreak();
			
			return;
			
		}
		
		Collections.sort(attendanceDateList);
		
		int currentStreak = 0;
		
		if(!attendanceDateList.isEmpty()) {
			
			LocalDate today = LocalDate.now();
			
			LocalDate lastAttendance = attendanceDateList.get(attendanceDateList.size() - 1);
			
			if(lastAttendance.isEqual(today) || lastAttendance.isEqual(today.minusDays(1))) {
				
				currentStreak = 1;
				
				for(int i = attendanceDateList.size() - 2; i >= 0; i--) {
					
					if(attendanceDateList.get(i).plusDays(1).isEqual(attendanceDateList.get(i + 1))) {
						
						currentStreak++;
						
					} else {
						
						break;
						
					}
					
				}
				
			}
			
		}
		
		result.modifyInformation(currentStreak, null);
		
	}
	
	@Transactional(readOnly = true)
	public Attendance getAttendanceByAttendanceNo(Long attendanceNo) {
		
		return attendanceRepository.findById(attendanceNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.ATTENDANCE_NOT_FOUND));
		
	}

	@Transactional
	public void deleteAttendance(Long attendanceNo) {
		
		Attendance attendance = getAttendanceByAttendanceNo(attendanceNo);
		
		attendanceRepository.delete(attendance);

	}


	

}