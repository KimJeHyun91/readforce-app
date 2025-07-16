package com.readforce.test.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.exception.DuplicationException;
import com.readforce.passage.entity.Passage;
import com.readforce.passage.service.LevelService;
import com.readforce.passage.service.PassageService;
import com.readforce.question.dto.MultipleChoiceResponseDto;
import com.readforce.question.dto.QuestionLevelAndCategoryAndLanguageDto;
import com.readforce.question.dto.QuestionTestResponseDto;
import com.readforce.question.dto.QuestionTestResultDto;
import com.readforce.question.service.MultipleChoiceService;
import com.readforce.question.service.QuestionService;
import com.readforce.test.dto.TestSubmitRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {

	private final MultipleChoiceService multipleChoiceService;
	private final PassageService passageService;
	private final Map<String, Set<Long>> solvedQuestionCache = new ConcurrentHashMap<>();
	private final QuestionService questionService;
	private final LevelService levelService;
	
	@Transactional
	public QuestionTestResponseDto getTestQuestion(LanguageEnum language, CategoryEnum category, Integer level) {

		Passage passage = passageService.getTestPassage(language, category, level);
		
		List<MultipleChoiceResponseDto> multipleChoiceDtoList = multipleChoiceService.getMultipleChoiceQuestionListByPassageNo(passage.getPassageNo());
		
		MultipleChoiceResponseDto multipleChoiceDto = multipleChoiceDtoList.get(0);
		
		String testerId = UUID.randomUUID().toString();
		
		return QuestionTestResponseDto.builder()
				.passageNo(passage.getPassageNo())
				.title(passage.getTitle())
				.content(passage.getContent())
				.author(passage.getAuthor())
				.publicationDate(passage.getPublicationDate())
				.category(passage.getCategory().getCategoryName().name())
				.level(passage.getLevel().getLevelNumber())
				.questionNo(multipleChoiceDto.getQuestionNo())
				.question(multipleChoiceDto.getQuestion())
				.choiceList(multipleChoiceDto.getChoiceList())
				.testerId(testerId)
				.build();

	}

	@Transactional
	public QuestionTestResponseDto submitVocabularyResult(TestSubmitRequestDto submitRequestDto) {

		QuestionLevelAndCategoryAndLanguageDto questionInfo = questionService.getQuestionLevelAndCategoryAndLanguage(submitRequestDto.getQuestionNo());

		if(isDuplicate(submitRequestDto.getTesterId(), submitRequestDto.getQuestionNo())) {
			
			throw new DuplicationException(MessageCode.TEST_QUESTION_ALREADY_SOLVED);
			
		}
		
		markAsSolved(submitRequestDto.getTesterId(), submitRequestDto.getQuestionNo());
		
		boolean isCorrect = multipleChoiceService.checkResult(submitRequestDto.getQuestionNo(), submitRequestDto.getSelectedIndex()).getIsCorrect();
		
		if(submitRequestDto.getQuestionSolvingTime() > 180) {
			
			isCorrect = false;
			
		}
		
		Integer nextLevel = evaluate(questionInfo.getLevel(), isCorrect);

		if(nextLevel > 0) {
			
			return getTestQuestion(questionInfo.getLanguage(), questionInfo.getCategory(), nextLevel);
			
		} else {
			
			return getTestQuestion(questionInfo.getLanguage(), CategoryEnum.FACTUAL, Math.abs(nextLevel));
			
		}
		
	}
	@Transactional
	public Object submitFactualResult(TestSubmitRequestDto submitRequestDto) {
		
		String testerId = submitRequestDto.getTesterId();
		Long questionNo = submitRequestDto.getQuestionNo();
		Integer selectedIndex = submitRequestDto.getSelectedIndex();
		Long questionSolvingTime = submitRequestDto.getQuestionSolvingTime();

		QuestionLevelAndCategoryAndLanguageDto questionInfo = questionService.getQuestionLevelAndCategoryAndLanguage(questionNo);
		Integer level = questionInfo.getLevel();
		LanguageEnum language = questionInfo.getLanguage();
		
		if(isDuplicate(testerId, questionNo)) {
			
			throw new DuplicationException(MessageCode.TEST_QUESTION_ALREADY_SOLVED);
			
		}
		
		markAsSolved(testerId, questionNo);
		
		boolean isCorrect = multipleChoiceService.checkResult(questionNo, selectedIndex).getIsCorrect();
		
		if(questionSolvingTime > 180) {
			
			isCorrect = false;
			
		}
		
		if(isCorrect) {
			
			return getTestQuestion(language, CategoryEnum.INFERENTIAL, level);
			
		} else {
			
			return getTestResult(language, level, false, false);
			
		}

	}
	
	@Transactional
	public QuestionTestResultDto submitInferentialResult(TestSubmitRequestDto submitRequestDto) {

		QuestionLevelAndCategoryAndLanguageDto questionInfo = questionService.getQuestionLevelAndCategoryAndLanguage(submitRequestDto.getQuestionNo());
		
		if(isDuplicate(submitRequestDto.getTesterId(), submitRequestDto.getQuestionNo())) {
			
			throw new DuplicationException(MessageCode.TEST_QUESTION_ALREADY_SOLVED);
			
		}
		
		markAsSolved(submitRequestDto.getTesterId(), submitRequestDto.getQuestionNo());
		
		boolean isCorrect = multipleChoiceService.checkResult(submitRequestDto.getQuestionNo(), submitRequestDto.getSelectedIndex()).getIsCorrect();
		
		if(submitRequestDto.getQuestionSolvingTime() > 180) {
			
			isCorrect = false;
			
		}
		
		if(isCorrect) {
			
			return getTestResult(questionInfo.getLanguage(), questionInfo.getLevel(), true, true);
			
		} else {
			
			return getTestResult(questionInfo.getLanguage(), questionInfo.getLevel(), true, false);
		}

	}

	private QuestionTestResultDto getTestResult(LanguageEnum language, Integer level, boolean factualResult, boolean inferentialResult) {
			
		String vocabularyLevel = levelService.getVocabularyLevelByLevel(level);
		
		return generateTestComment(language, vocabularyLevel, factualResult, inferentialResult);
				
	}

	private QuestionTestResultDto generateTestComment(LanguageEnum language, String vocabularyLevel, boolean factualResult, boolean inferentialResult) {
				
		String testResultComment = "";
		
		String koreanStartComment = "당신의 한국어의 ";
		String koreanVocabularyComment = "어휘 능력은 " + vocabularyLevel + "수준 이며, ";
		String koreanFactualCorrectComment = "글에 쓰여 있는 내용을 정확하게 찾아내는 능력이 뛰어납니다.";
		String koreanFactualIncorrectComment = "글에 쓰여 있는 내용을 정확하게 찾아내는 능력이 부족합니다.";
		String koreanInferentialCorrectComment = "그리고 글쓴이가 직접 말하지 않은 숨은 의미를 찾아내는 능력이 뛰어납니다.";
		String koreanInferentialIncorrectComment = "그리고 글쓴이가 직접 말하지 않은 숨은 의미를 찾아내는 능력이 부족합니다. ";
		
		switch(language) {

			default:
				if(factualResult) {
					if(inferentialResult) {
						testResultComment = koreanStartComment + koreanVocabularyComment + koreanFactualCorrectComment + koreanInferentialCorrectComment;
					} else {
						testResultComment = koreanStartComment + koreanVocabularyComment + koreanFactualCorrectComment + koreanInferentialIncorrectComment;
					}					
				} else {
					testResultComment = koreanStartComment + koreanVocabularyComment + koreanFactualIncorrectComment +koreanInferentialIncorrectComment;
				}
				
		}		
		
		return QuestionTestResultDto.builder()
				.testResultComment(testResultComment)
				.build();
		
	}

	private int evaluate(Integer level, boolean isCorrect) {

		return switch(level) {
			case 6 -> isCorrect ? 8 : 4;
			case 8 -> isCorrect ? 10 : 7;
			case 10 -> isCorrect ? -10 : 9;
			case 9 -> isCorrect ? -9 : -8;
			case 7 -> isCorrect ? -7 : -6;
			case 4 -> isCorrect ? 5 : 3;
			case 5 -> isCorrect ? -5 : -4;
			case 3 -> isCorrect ? 2 : -2;
			case 2 -> isCorrect ? -2 : 1;
			default -> -1;
		
		};
		
	}

	private void markAsSolved(String testerId, Long questionNo) {
		
		solvedQuestionCache.computeIfAbsent(testerId, key -> new HashSet<>()).add(questionNo);
		
	}
	
	private boolean isDuplicate(String testeId, Long questionNo) {
		
		return solvedQuestionCache.getOrDefault(testeId, Collections.emptySet()).contains(questionNo);
		
	}




	


}
