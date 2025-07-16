package com.readforce.result.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.StatusEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Member;
import com.readforce.result.entity.Result;
import com.readforce.result.repository.ResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {
	
	private final ResultRepository resultRepository;
	
	@Transactional
	public Result create(Member member) {
		
		Result result = Result.builder()
				.member(member)
				.build();
		
		return resultRepository.save(result);
		
	}

	@Transactional(readOnly = true)
	public Result getActiveMemberResultByEmail(String email) {

		return resultRepository.findByMember_EmailAndMember_Status(email, StatusEnum.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_RESULT_NOT_FOUND));
	
	}

	@Transactional(readOnly = true)
	public Integer getLearningStreak(String email) {
		
		Result result = getActiveMemberResultByEmail(email);
		
		return result.getLearningStreak();
		
	}

	@Transactional(readOnly = true)
	public Double getOverallCorrectAnswerRate(String email) {

		return resultRepository.findOverallAnswerCorrectRateByMemberEmailAndMemberStatus(email, StatusEnum.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.OVERALL_CORRECT_ANSWER_RATE_NOT_FOUND));

	}

	@Transactional(readOnly = true)
	public Optional<Result> getActiveMemberResultByEmailWithOptional(String email) {

		return resultRepository.findByMember_EmailAndMember_Status(email, StatusEnum.ACTIVE);
		
	}
	
	@Transactional(readOnly = true)
	public Result getResultByEmail(String email) {

		return resultRepository.findByMember_Email(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.RESULT_NOT_FOUND));

	}

	@Transactional
	public void modifyResult(Long resultNo, Integer learningStreak, Double overallCorrectAnswerRate) {
		
		Result result = getResultByResultNo(resultNo);
		
		result.modifyInformation(learningStreak, overallCorrectAnswerRate);
		
	}

	@Transactional
	public Result getResultByResultNo(Long resultNo) {

		return resultRepository.findById(resultNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.RESULT_NOT_FOUND));
		
	}
	
}