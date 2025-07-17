package com.readforce.question.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.ClassificationEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Member;
import com.readforce.passage.entity.Passage;
import com.readforce.passage.service.PassageService;
import com.readforce.question.dto.ChoiceDto;
import com.readforce.question.dto.MultipleChoiceResponseDto;
import com.readforce.question.dto.QuestionCheckResultDto;
import com.readforce.question.entity.Choice;
import com.readforce.question.entity.MultipleChoice;
import com.readforce.question.repository.MultipleChoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MultipleChoiceService {
	
	private final MultipleChoiceRepository multipleChoiceRepository;
	private final PassageService passageService;
	
	@Transactional(readOnly = true)
	public List<MultipleChoiceResponseDto> getMultipleChoiceQuestionListByPassageNo(Long passageNo){
		
		List<MultipleChoice> questionList = multipleChoiceRepository.findByPassage_PassageNo(passageNo);
		
		if(questionList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.QUESTION_NOT_FOUND);
			
		}
		
		return questionList.stream()
				.map(MultipleChoiceResponseDto::new)
				.collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public QuestionCheckResultDto checkResult(Long questionNo, Integer selectedIndex) {

		MultipleChoice multipleChoice = multipleChoiceRepository.findById(questionNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.QUESTION_NOT_FOUND));
		
		boolean isCorrect = multipleChoice.getChoiceList().stream()
				.filter(choice -> choice.getChoiceIndex().equals(selectedIndex))
				.anyMatch(Choice::getIsCorrect);
		
		return new QuestionCheckResultDto(isCorrect, multipleChoice);

	}

	@Transactional(readOnly = true)
	public MultipleChoiceResponseDto getUnsolvedMultipleChoiceQuestion(Member member, LanguageEnum language, CategoryEnum category, TypeEnum type, Integer level, List<Long> solvedQuestionNos) {

	    List<Long> passageNoList = passageService.getPassageNoListByLanguageAndClassificationAndCategoryAndTypeAndLevel(
	            language,
	            ClassificationEnum.NORMAL,
	            category,
	            type,
	            level
	    );
	    
	    List<MultipleChoice> allQuestions = passageNoList.stream()
	            .flatMap(passageNo -> multipleChoiceRepository.findByPassage_PassageNo(passageNo).stream())
	            .collect(Collectors.toList());

	    List<MultipleChoice> unsolvedQuestions = allQuestions.stream()
	            .filter(q -> !solvedQuestionNos.contains(q.getQuestionNo()))
	            .collect(Collectors.toList());


	    if (unsolvedQuestions.isEmpty()) {

	    	throw new ResourceNotFoundException(MessageCode.QUESTION_NOT_FOUND);

	    }

	    Collections.shuffle(unsolvedQuestions);
	    
	    MultipleChoice selectedQuestion = unsolvedQuestions.get(0);

	    Passage passage = selectedQuestion.getPassage();

	    return MultipleChoiceResponseDto.builder()
	            .passageNo(passage.getPassageNo())
	            .title(passage.getTitle())
	            .content(passage.getContent())
	            .author(passage.getAuthor())
	            .publicationDate(passage.getPublicationDate())
	            .category(passage.getCategory().getCategoryName().name())
	            .level(passage.getLevel().getLevelNumber())
	            .questionNo(selectedQuestion.getQuestionNo())
	            .question(selectedQuestion.getQuestion())
	            .choiceList(selectedQuestion.getChoiceList().stream()
	                    .map(ChoiceDto::new)
	                    .collect(Collectors.toList()))
	            .build();
	    
	}



	@Transactional
	public void saveMultipleChoice(MultipleChoice multipleChoice) {

		multipleChoiceRepository.save(multipleChoice);
		
	}

	@Transactional(readOnly = true)
	public MultipleChoice getMultipleChoiceByQuestionNo(Long questionNo) {

		return multipleChoiceRepository.findById(questionNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MULTIPLE_CHOICE_NOT_FOUND));

	}
	
	@Transactional
	public void deleteQuestionByQuestionNo(Long questionNo) {
	    boolean exists = multipleChoiceRepository.existsById(questionNo);
	    if (!exists) {
	        throw new ResourceNotFoundException("존재하지 않는 문제 번호입니다: " + questionNo);
	    }
	    multipleChoiceRepository.deleteById(questionNo);
	}

}
