package com.readforce.passage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.ClassificationEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.passage.entity.Classification;
import com.readforce.passage.repository.ClassificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassificationService {
	
	private final ClassificationRepository classificationRepository;
	
	@Transactional(readOnly = true)
	public Classification getClassificationByClassfication(ClassificationEnum classification) {
		
		return classificationRepository.findByClassificationName(classification)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.CLASSIFICATION_NOT_FOUND));
				
	}

	@Transactional(readOnly = true)
	public List<Classification> getAllClassificationList() {
		
		return classificationRepository.findAll();

	}

	@Transactional
	public void saveClassification(Classification classifcation) {

		classificationRepository.save(classifcation);
		
	}

	@Transactional(readOnly = true)
	public Classification getClassificationByClassificationNo(Long classificationNo) {
		
		return classificationRepository.findById(classificationNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.CLASSIFICATION_NOT_NULL));
		
	}
	
	@Transactional
	public void modifyClassification(Long classificationNo, ClassificationEnum classificationName) {

		Classification classification = getClassificationByClassificationNo(classificationNo);
		
		classification.changeClassificationName(classificationName);
		
		saveClassification(classification);
		
	}

	@Transactional
	public void deleteByClassificationNo(Long classificationNo) {
		
		classificationRepository.deleteById(classificationNo);
		
	}

}
