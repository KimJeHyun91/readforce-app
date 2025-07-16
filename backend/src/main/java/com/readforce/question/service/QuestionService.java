package com.readforce.question.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.ClassificationEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.OrderByEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.passage.dto.PassageResponseDto;
import com.readforce.passage.service.PassageService;
import com.readforce.question.dto.QuestionLevelAndCategoryAndLanguageDto;
import com.readforce.question.entity.Question;
import com.readforce.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	private final PassageService passageService;

	@Transactional(readOnly = true)
	public Question getQuestion(Long questionNo) {

		return questionRepository.findById(questionNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.QUESTION_NOT_FOUND));

	}
	
	@Transactional(readOnly = true)
	private Question getQuestionWithPassage(Long questionNo) {
		
		return questionRepository.findByIdWithPassage(questionNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.QUESTION_NOT_FOUND));
		
	}

	@Transactional(readOnly = true)
	public QuestionLevelAndCategoryAndLanguageDto getQuestionLevelAndCategoryAndLanguage(Long questionNo) {

		Question question = getQuestionWithPassage(questionNo);
		
		return QuestionLevelAndCategoryAndLanguageDto.builder()
				.language(question.getPassage().getLanguage().getLanguageName())
				.category(question.getPassage().getCategory().getCategoryName())
				.level(question.getPassage().getLevel().getLevelNumber())
				.build();

	}
	
	@Transactional(readOnly = true)
	public List<PassageResponseDto> getUnusedVocabularyPassageList(LanguageEnum language, ClassificationEnum classification){
		
		List<PassageResponseDto> allPassageDtoList = passageService.getPassageListByLanguageAndCategory(OrderByEnum.ASC, language, classification, CategoryEnum.VOCABULARY);

		List<Long> usedPassageNoList = questionRepository.findAllUsedPassageNo();
		
		return allPassageDtoList.stream()
				.filter(passageDto -> !usedPassageNoList.contains(passageDto.getPassageNo()))
				.collect(Collectors.toList());
		
	}

	@Transactional
	public void deleteQuestion(Long questionNo) {

		Question question = getQuestion(questionNo);
		
		questionRepository.delete(question);
		
	}
	



}
