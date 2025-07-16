package com.readforce.administrator.controller.member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.administrator.dto.AdministratorAddMemberRequestDto;
import com.readforce.administrator.dto.AdministratorAttendanceRequestDto;
import com.readforce.administrator.dto.AdministratorMemberResponseDto;
import com.readforce.administrator.dto.AdministratorModifyRequestDto;
import com.readforce.administrator.dto.AdministratorResultMetricModifyRequestDto;
import com.readforce.administrator.dto.AdministratorResultMetricRequestDto;
import com.readforce.administrator.dto.AdministratorResultMetricResponseDto;
import com.readforce.administrator.dto.AdministratorResultModifyRequestDto;
import com.readforce.administrator.dto.AdministratorResultRequestDto;
import com.readforce.administrator.dto.AdministratorResultResponseDto;
import com.readforce.administrator.dto.AdministratorScoreModifyRequestDto;
import com.readforce.administrator.dto.AdministratorScoreRequestDto;
import com.readforce.administrator.dto.AdministratorScoreResponseDto;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.RoleEnum;
import com.readforce.member.dto.MemberAttendanceResponseDto;
import com.readforce.member.entity.Member;
import com.readforce.member.service.AttendanceService;
import com.readforce.member.service.MemberService;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.service.CategoryService;
import com.readforce.passage.service.LanguageService;
import com.readforce.question.dto.QuestionSummaryResponseDto;
import com.readforce.result.entity.Result;
import com.readforce.result.service.LearningService;
import com.readforce.result.service.ResultMetricService;
import com.readforce.result.service.ResultService;
import com.readforce.result.service.ScoreService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/member")
@RequiredArgsConstructor
@Validated
public class AdministratorMemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final LearningService learningService;
	private final AttendanceService attendanceService;
	private final ScoreService scoreService;
	private final CategoryService categoryService;
	private final LanguageService languageService;
	private final ResultService resultService;
	private final ResultMetricService resultMetricService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-member-list")
	public ResponseEntity<List<AdministratorMemberResponseDto>> getAllMemberList(){
		
		List<AdministratorMemberResponseDto> memberList = memberService.getAllMemberList();
		
		return ResponseEntity.status(HttpStatus.OK).body(memberList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-member")
	public ResponseEntity<Map<String, String>> addMember(
			@Valid @RequestBody AdministratorAddMemberRequestDto requestDto
	){
		
		memberService.saveMember(Member.builder()
				.email(requestDto.getEmail())
				.password(passwordEncoder.encode(requestDto.getPassword()))
				.nickname(requestDto.getNickname())
				.birthday(requestDto.getBirthday())
				.role(requestDto.getRole() != null ? requestDto.getRole() : RoleEnum.USER)
				.build()	
		);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_MEMBER_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify")
	public ResponseEntity<Map<String, String>> modify(
			@Valid @RequestBody AdministratorModifyRequestDto requestDto			
	){
		
		memberService.modifyByAdmin(requestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MEMBER_MODIFY_SUCCESS
		));		
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, String>> delete(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email
	){
		
		memberService.deleteMemberByAdmin(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_MEMBER_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-member")
	public ResponseEntity<AdministratorMemberResponseDto> getMember(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email	
	){
		
		Member member = memberService.getActiveMemberByEmail(email);
		
		AdministratorMemberResponseDto memberDto = AdministratorMemberResponseDto.builder()
				.email(member.getEmail())
				.nickname(member.getNickname())
				.birthday(member.getBirthday())
				.profileImagePath(member.getProfileImagePath())
				.status(member.getStatus().name())
				.role(member.getRole().name())
				.socialProvider(member.getSocialProvider())
				.createdAt(member.getCreatedAt())
				.lastModifiedAt(member.getLastModifiedAt())
				.withdrawAt(member.getWithdrawAt())
				.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(memberDto);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-total-learning")
	public ResponseEntity<List<QuestionSummaryResponseDto>> getTotalLearning(
			@RequestParam("email")
			@NotNull(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email
	){
		
		List<QuestionSummaryResponseDto> totalLearningList = learningService.getTotalLearning(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(totalLearningList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-attendance-list-by-email")
	public ResponseEntity<List<MemberAttendanceResponseDto>> getAttendanceListByEmail(
			@RequestParam("email")
			@NotNull(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email
	){
		
		List<MemberAttendanceResponseDto> attendanceList = attendanceService.getAttendanceListByEmail(email).stream()
				.map(MemberAttendanceResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-attendance-by-email")
	public ResponseEntity<Map<String, String>> addAttendanceByEmail(
			@Valid @RequestBody AdministratorAttendanceRequestDto requestDto
	){
		
		attendanceService.addAttendance(requestDto.getEmail(), requestDto.getAttendanceDate());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.ADD_ATTENDANCE_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-attendance-by-email")
	public ResponseEntity<Map<String, String>> deleteAttendanceByEmail(
		@RequestParam("attendanceNo")
		@NotNull(message = MessageCode.ATTENDANCE_NO_NOT_NULL)
		Long attendanceNo
	){
		
		attendanceService.deleteAttendance(attendanceNo);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.DELETE_ATTENDANCE_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-score-list-by-email")
	public ResponseEntity<List<AdministratorScoreResponseDto>> getScoreListByEmail(
			@RequestParam("email")
			@NotNull(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email
	){
		
		List<AdministratorScoreResponseDto> scoreList = scoreService.getScoreListByEmail(email).stream()
				.map(AdministratorScoreResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(scoreList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-score-by-email")
	public ResponseEntity<Map<String, String>> createScoreByEmail(
			@Valid @RequestBody AdministratorScoreRequestDto requestDto
	){
		
		Member member = memberService.getActiveMemberByEmail(requestDto.getEmail());
		
		Category category = categoryService.getCategoryByCategory(requestDto.getCategory());
		
		Language language = languageService.getLangeageByLanguage(requestDto.getLanguage());
		
		scoreService.createScore(member, requestDto.getScore(), category, language);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_SCORE_SUCCESS
		));
				
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify-score-by-email")
	public ResponseEntity<Map<String, String>> modifyScoreByEmail(
			@Valid @RequestBody AdministratorScoreModifyRequestDto requestDto
	){
		
		if(requestDto.getScore() < 0) {
			
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					MessageCode.MESSAGE_CODE, MessageCode.SCORE_INVALID
			));	
			
		}
		
		Member member = memberService.getActiveMemberByEmail(requestDto.getEmail());
		
		Category category = categoryService.getCategoryByCategory(requestDto.getCategory());
		
		Language language = languageService.getLangeageByLanguage(requestDto.getLanguage());
				
		
		scoreService.modifyScoreByEmail(
				member, 
				requestDto.getScoreNo(), 
				requestDto.getScore(),
				category,
				language);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_SCORE_SUCCESS
		));
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-result-by-email")
	public ResponseEntity<AdministratorResultResponseDto> getResultByEmail(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email
	){
		
		Result result = resultService.getResultByEmail(email);
		
		AdministratorResultResponseDto resultDto = AdministratorResultResponseDto.builder()
				.resultNo(result.getResultNo())
				.learningStreak(result.getLearningStreak())
				.overallCorrectAnswerRate(result.getOverallCorrectAnswerRate())
				.createdAt(result.getCreatedAt())
				.lastModifiedAt(result.getLastModifiedAt())
				.email(result.getMember().getEmail())
				.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(resultDto);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-result-by-email")
	public ResponseEntity<Map<String, String>> createResultByEmail(
			@Valid @RequestBody AdministratorResultRequestDto requestDto
	){
		
		Member member = memberService.getActiveMemberByEmail(requestDto.getEmail());
		
		resultService.create(member);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_RESULT_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify-result-by-email")
	public ResponseEntity<Map<String, String>> mdoifyResultByEmail(
			@Valid @RequestBody AdministratorResultModifyRequestDto requestDto
	){
		
		resultService.modifyResult(requestDto.getResultNo(), requestDto.getLearningStreak(), requestDto.getOverallCorrectAnswerRate());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_RESULT_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-result-metric-list-by-email")
	public ResponseEntity<List<AdministratorResultMetricResponseDto>> getResultMetricListByEmail(
			@RequestParam("email")
			@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
			@Email
			String email
	){
		
		Result result = resultService.getResultByEmail(email);
		
		List<AdministratorResultMetricResponseDto> resultMetricList = 
				resultMetricService.getAllByResult(result).stream()
				.map(AdministratorResultMetricResponseDto::new)
				.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(resultMetricList);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-missing-result-metric-by-email")
	public ResponseEntity<Map<String, String>> createMissingResultMetricByEmail(
			@Valid @RequestBody AdministratorResultMetricRequestDto requestDto	
	){
				
		memberService.createMissingResultMetricByEmail(requestDto.getEmail());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.CREATE_MISSING_RESULT_METRIC_SUCCESS
		));
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/modify-result-metric")
	public ResponseEntity<Map<String, String>> modifyResultMetric(
			@Valid @RequestBody AdministratorResultMetricModifyRequestDto requestDto
	){
		
		resultMetricService.modifyResultMetric(
				requestDto.getResultMetricNo(),
				requestDto.getCorrectAnswerRate(),
				requestDto.getQuestionSolvingTimeAverage());
		
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MODIFY_RESULT_METRIC_SUCCESS
		));
		
	}
	
}