package com.readforce.member.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.AgeGroup;
import com.readforce.member.entity.Member;
import com.readforce.member.repository.AgeGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgeGroupService {

	private final AgeGroupRepository ageGroupRepository;

	@Transactional
	public AgeGroup getAgeGroupForMember(Member member) {
		
		int age = Period.between(member.getBirthday(), LocalDate.now()).getYears();
		
		int ageGroupValue = (age / 10) * 10;
		
		return getAgeGroupByAgeGroup(ageGroupValue);
		
	}

	@Transactional(readOnly = true)
	public AgeGroup getAgeGroupByAgeGroup(Integer ageGroup) {
		
		return ageGroupRepository.findByAgeGroup(ageGroup)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.AGE_GROUP_NOT_FOUND));

	}

	@Transactional(readOnly = true)
	public List<AgeGroup> getAllList() {

		return ageGroupRepository.findAll();

	}

	@Transactional
	public void createAgeGroup(AgeGroup ageGroup) {

		ageGroupRepository.save(ageGroup);
		
	}

	@Transactional
	public void deleteAgeGroup(Long ageGroupNo) {

		AgeGroup ageGroup = getAgeGroupByAgeGroupNo(ageGroupNo);
		
		ageGroupRepository.delete(ageGroup);
		
	}

	@Transactional(readOnly = true)
	private AgeGroup getAgeGroupByAgeGroupNo(Long ageGroupNo) {

		return ageGroupRepository.findById(ageGroupNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.AGE_GROUP_NOT_FOUND));

	}
	
}
