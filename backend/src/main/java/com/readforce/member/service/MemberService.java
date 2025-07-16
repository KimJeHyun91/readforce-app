package com.readforce.member.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.administrator.dto.AdministratorMemberResponseDto;
import com.readforce.administrator.dto.AdministratorModifyRequestDto;
import com.readforce.authentication.dto.OAuthAttributeDto;
import com.readforce.authentication.exception.AuthenticationException;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.FileCategoryEnum;
import com.readforce.common.enums.PrefixEnum;
import com.readforce.common.enums.StatusEnum;
import com.readforce.common.exception.DuplicationException;
import com.readforce.common.exception.JsonException;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.common.service.FileDeleteFailLogService;
import com.readforce.email.service.EmailService;
import com.readforce.file.exception.ProfileImageException;
import com.readforce.file.service.FileService;
import com.readforce.member.dto.MemberKeyInformationDto;
import com.readforce.member.dto.MemberModifyDto;
import com.readforce.member.dto.MemberPasswordResetDto;
import com.readforce.member.dto.MemberPasswordResetFromLinkDto;
import com.readforce.member.dto.MemberSignUpDto;
import com.readforce.member.dto.MemberSocialProviderDto;
import com.readforce.member.dto.MemberSocialSignUpDto;
import com.readforce.member.dto.MemberSummaryDto;
import com.readforce.member.entity.Member;
import com.readforce.member.repository.MemberRepository;
import com.readforce.result.entity.Result;
import com.readforce.result.service.ResultMetricEventService;
import com.readforce.result.service.ResultMetricService;
import com.readforce.result.service.ResultService;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final StringRedisTemplate redisTemplate;
	private final PasswordEncoder passwordEncoder;
	private final ResultService resultService;
	private final FileService fileService;
	private final FileDeleteFailLogService fileDeleteFailLogService;
	private final EmailService emailService;
	private final ResultMetricService resultMetricService;
	private final ResultMetricEventService resultMetricEventService;
 
	
	@Transactional(readOnly = true)
	public MemberKeyInformationDto getMemberKeyInformationByEmailAndStatus(String email, StatusEnum status) {
		
		Member member = memberRepository.findByEmailAndStatus(email, status)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		MemberKeyInformationDto memberKeyInformationDto = MemberKeyInformationDto.builder()
				.email(member.getEmail())
				.password(member.getPassword())
				.role(member.getRole())
				.build();
		
		return memberKeyInformationDto;
		
	}

	@Transactional
	public void linkSocialAccount(String email, String socialProvider, String socialId, String socialEmail) {

		Member primaryMember = getActiveMemberByEmail(email);
		
		if(primaryMember.getSocialProvider() != null && !primaryMember.getSocialProvider().isEmpty()) {
			
			throw new AuthenticationException(MessageCode.SOCIAL_ACCOUNT_ALREADY_EXIST_CANNOT_LINK_NEW_ACCOUNT);
			
		}
		
		memberRepository
			.findBySocialProviderAndSocialId(socialProvider, socialId)
			.ifPresent(member -> {
				
				if(!member.getEmail().equals(email)) {
					
					throw new DuplicationException(MessageCode.SOCIAL_EMAIL_ALREADY_CONNECTED);
					
				}
				
			});
		
		memberRepository
			.findByEmail(socialEmail)
			.ifPresent(member -> {
				
				if(!member.getEmail().equals(socialEmail)) {
					
					throw new DuplicationException(MessageCode.SOCIAL_EMAIL_ALREADY_USED);
					
				}
				
			});
			
		Member member = memberRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		member.changeSocialInfo(socialProvider, socialId);
				
	}
	
	@Transactional
	public Optional<Member> getActiveMemberWithOptional(String email) {

		return memberRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE);
				
	}
	
	@Transactional(readOnly = true)
	public Member getActiveMemberByEmail(String email) {

		return memberRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
				
	}
	
	@Transactional(readOnly = true)
	public Member getMemberByEmail(String email) {
		
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
	}
	
	@Transactional
	public Optional<Member> getActiveMemberBySocialInfo(String socialProvider, String socialId) {

		return memberRepository.findBySocialProviderAndSocialId(socialProvider, socialId);

	}

	@Transactional
	public void saveMember(Member member) {

		memberRepository.save(member);
		
	}

	@Transactional
	public MemberSummaryDto getActiveMemberByEmailWithMemberSummaryDto(String email) {

		Member member = memberRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		return MemberSummaryDto.builder()
				.email(member.getEmail())
				.nickname(member.getNickname())
				.socialProvider(member.getSocialProvider())
				.birthday(member.getBirthday())
				.build();
		
	}

	@Transactional
	public MemberSocialProviderDto getActiveMemberByEmailWithMemberSoicalProviderDto(String email) {

		Member member = memberRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

		return MemberSocialProviderDto.builder()
				.socialProvider(member.getSocialProvider())
				.build();
		
	}

	@Transactional
	public void emailCheck(String email) {
		
		if(memberRepository.findByEmail(email).isPresent()) {
			
			throw new DuplicationException(MessageCode.EMAIL_ALREADY_USED);
			
		}
		
	}

	@Transactional
	public void nicknameCheck(String nickname) {

		if(memberRepository.findByNickname(nickname).isPresent()) {
			
			throw new DuplicationException(MessageCode.NICKNAME_ALREADY_USED);
			
		}
		
	}

	@Transactional
	public void signUp(MemberSignUpDto memberSignUpDto) {
		
		String checkEmailVerification = 
				redisTemplate.opsForValue().get(PrefixEnum.EMAIL_VERIFICATION.getContent() + memberSignUpDto.getEmail());
		
		if(checkEmailVerification == null || !checkEmailVerification.equals(MessageCode.EMAIL_VERIFICATION_SUCCESS)) {
			
			throw new AuthenticationException(MessageCode.EMAIL_VERIFICATION_REQUIRED);
			
		}
		
		emailCheck(memberSignUpDto.getEmail());
		
		nicknameCheck(memberSignUpDto.getNickname());
		
		Member member = Member.builder()
				.email(memberSignUpDto.getEmail())
				.password(passwordEncoder.encode(memberSignUpDto.getPassword()))
				.nickname(memberSignUpDto.getNickname())
				.birthday(memberSignUpDto.getBirthday())
				.build();
		
		memberRepository.save(member);
		
		Result result = resultService.create(member);
		
		resultMetricEventService.createResultMetricsForMember(result.getResultNo());
		
		redisTemplate.delete(PrefixEnum.EMAIL_VERIFICATION.getContent() + memberSignUpDto.getEmail());
		
	}

	@Transactional
	public String modify(String email, MemberModifyDto memberModifyDto) {

		Member member = getActiveMemberByEmail(email);
		
		if(memberModifyDto.getNickname() != null && !memberModifyDto.getNickname().isEmpty()) {
			
			nicknameCheck(memberModifyDto.getNickname());
			
		}
		
		member.modifyInformation(memberModifyDto.getNickname(), memberModifyDto.getBirthday(), null);
		
		memberRepository.save(member);
		
		return member.getNickname();
				
	}

	@Transactional
	public void withdraw(String email) {

		Member member = getActiveMemberByEmail(email);
		
		member.deactivate();
		
		redisTemplate.delete(PrefixEnum.REFRESH.getContent() + email);		
		
	}
	
	@Scheduled(cron = "0 0 4 * * ?")
	@Transactional
	public void deleteMember() {
		
		log.info("탈퇴 후 30일이 지난 회원 정보를 삭제합니다.");
		LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
		
		List<Member> memberToDeleteList = memberRepository.findAllByStatusAndWithdrawAtBefore(StatusEnum.PENDING_DELETION, thirtyDaysAgo);
		
		if(memberToDeleteList.isEmpty()) {
			
			log.info("삭제할 회원이 없습니다.");
			return;
			
		}
		
		for(Member member : memberToDeleteList) {
			
			if(member.getProfileImagePath() != null && !member.getProfileImagePath().isEmpty()) {
				
				try {
					
					deleteProfileImage(member);
					
				} catch(Exception exception) {
					
					log.error("프로필 이미지 삭제 실패. Member DB 레코드는 계속 삭제됩니다. Member email: {}, File: {}", member.getEmail(), member.getProfileImagePath(), exception);
	
					fileDeleteFailLogService.create(member, exception.getMessage());
									
				}
				
			}
			
		}
		
		log.info("{}명의 탈퇴 회원 정보를 DB에서 삭제합니다.", memberToDeleteList.size());
		
		memberRepository.deleteAllInBatch(memberToDeleteList);
		
		log.info("탈퇴 회원 정보 삭제를 완료했습니다.");		
		
	}
	
	@Transactional
	public void modifyInformation(String email, String nickname, LocalDate birthday, String profileImagePath) {
		
		Member member = getActiveMemberByEmail(email);
		
		member.modifyInformation(nickname, birthday, profileImagePath);
		
	}

	@Transactional
	public void passwordResetFromLink(MemberPasswordResetFromLinkDto memberPasswordResetFromLinkDto) {
		
		String email = redisTemplate.opsForValue().get(PrefixEnum.PASSWORD_RESET.getContent() + memberPasswordResetFromLinkDto.getTemporalToken());
		
		

		if(email == null) {
			
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
			
		}
		
		Member member = getActiveMemberByEmail(email);
		
		member.resetPassword(passwordEncoder.encode(memberPasswordResetFromLinkDto.getNewPassword()));
		
		redisTemplate.delete(PrefixEnum.PASSWORD_RESET.getContent() + memberPasswordResetFromLinkDto.getTemporalToken());
		
		emailService.sendPasswordChangeNotification(email);
		
	}

	@Transactional
	public void passwordReset(String email, MemberPasswordResetDto memberPasswordResetDto) {

		Member member = getActiveMemberByEmail(email);
		
		if(!passwordEncoder.matches(memberPasswordResetDto.getOldPassword(), member.getPassword())) {
			
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
			
		}
		
		member.resetPassword(passwordEncoder.encode(memberPasswordResetDto.getNewPassword()));
		
	}

	@Transactional
	public void socialSignUp(MemberSocialSignUpDto memberSocialSignUpDto) {

		String socialInfoJson = redisTemplate.opsForValue().get(PrefixEnum.SOCIAL_SIGN_UP.getContent() + memberSocialSignUpDto.getTemporalToken());
		
		if(socialInfoJson == null) {
			
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
			
		}
		
		Map<String, String> socialInfo;
		
		try {
			
			socialInfo = new ObjectMapper().readValue(socialInfoJson, new TypeReference<Map<String, String>>() {});
			
		} catch(Exception exception) {
			
			throw new JsonException(MessageCode.JSON_PROCESSING_FAIL);
			
		}
		
		String email = socialInfo.get("email");
		String socialProvider = socialInfo.get("socialProvider");
		String socialId = socialInfo.get("socialId");
		
		nicknameCheck(memberSocialSignUpDto.getNickname());
		
		OAuthAttributeDto oAuthAttributeDto = OAuthAttributeDto.builder().email(email).build();
		
		Member newMember = oAuthAttributeDto.toEntry(memberSocialSignUpDto.getNickname(), memberSocialSignUpDto.getBirthday());
		
		newMember.resetPassword(passwordEncoder.encode(newMember.getPassword()));
		
		newMember.changeSocialInfo(socialProvider, socialId);
		
		memberRepository.save(newMember);
		
		Result result = resultService.create(newMember);
		
		resultMetricEventService.createResultMetricsForMember(result.getResultNo());
		
		redisTemplate.delete(PrefixEnum.SOCIAL_SIGN_UP.getContent() + memberSocialSignUpDto.getTemporalToken());		
		
	}

	@Transactional
	public void uploadProfileImage(String email, MultipartFile profileImageFile) {

		Member member = getActiveMemberByEmail(email);
		
		String oldProfileImagePath = member.getProfileImagePath();
		
		String newProfileImagePath = fileService.storeFile(profileImageFile, FileCategoryEnum.PROFILE_IMAGE);
		
		member.modifyInformation(null, null, newProfileImagePath);
		
		if(oldProfileImagePath != null && !oldProfileImagePath.isEmpty()) {
			
			fileService.deleteFile(oldProfileImagePath, FileCategoryEnum.PROFILE_IMAGE);
			
		}
		
	}

	@Transactional
	public void deleteProfileImage(Member member) {
		
		String profileImagePath = member.getProfileImagePath();
		
		if(profileImagePath == null || profileImagePath.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.PROFILE_IMAGE_NOT_FOUND);
			
		}
		
		member.modifyInformation(null, null, null);
		
		fileService.deleteFile(profileImagePath, FileCategoryEnum.PROFILE_IMAGE);
		
	}

	@Transactional
	public Resource getProfileImage(String email) {
		
		Member member = getActiveMemberByEmail(email);
		String profileImagePath = member.getProfileImagePath();
		
		 if (profileImagePath == null || profileImagePath.isEmpty()) {
			 
		        throw new ProfileImageException(MessageCode.PROFILE_IMAGE_NOT_FOUND);
		        
		    }
		 
		 return fileService.loadFileAsResource(profileImagePath, FileCategoryEnum.PROFILE_IMAGE);
	}

	@Transactional
	public void emailExistCheck(String email) {

		memberRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE)
		.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
	}

	@Transactional(readOnly = true)
	public List<AdministratorMemberResponseDto> getAllMemberList() {

		return memberRepository.findAll().stream()
				.map(AdministratorMemberResponseDto::new)
				.collect(Collectors.toList());

	}

	@Transactional
	public void modifyByAdmin(AdministratorModifyRequestDto requestDto) {

		Member member = memberRepository.findByEmail(requestDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
		
		member.modifyInformation(requestDto);
				
		memberRepository.save(member);
		
	}

	@Transactional
	public void deleteMemberByAdmin(@Email String email) {
		
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));

		if(member.getProfileImagePath() != null && !member.getProfileImagePath().isEmpty()) {
			
			try {
				
				deleteProfileImage(member);
				
			} catch(Exception exception) {
				
				fileDeleteFailLogService.create(member, exception.getMessage());
				
			}

		}
		
		redisTemplate.delete(PrefixEnum.REFRESH.getContent() + email);
		
		memberRepository.delete(member);		
		
	}

	@Transactional
	public void createMissingResultMetricByEmail(String email) {
		
		Result result = resultService.getActiveMemberResultByEmail(email);
		
		resultMetricEventService.createResultMetricsForMember(result.getResultNo());
		
	}


	
	
	
	
	
	

}