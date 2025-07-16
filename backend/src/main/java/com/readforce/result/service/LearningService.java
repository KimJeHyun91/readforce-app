package com.readforce.result.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Member;
import com.readforce.member.service.MemberService;
import com.readforce.passage.dto.PassageResponseDto;
import com.readforce.passage.entity.Passage;
import com.readforce.question.dto.QuestionCheckResultDto;
import com.readforce.question.dto.QuestionSummaryResponseDto;
import com.readforce.question.entity.Question;
import com.readforce.question.service.MultipleChoiceService;
import com.readforce.result.dto.LearningMultipleChoiceRequestDto;
import com.readforce.result.entity.Learning;
import com.readforce.result.entity.Result;
import com.readforce.result.entity.ResultMetric;
import com.readforce.result.repository.LearningRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LearningService {
	
	private final LearningRepository learningRepository;
	private final MultipleChoiceService multipleChoiceService;
	private final MemberService memberService;
	private final ResultService resultService;
	private final ResultMetricService resultMetricService;
	
	@Transactional
	public void saveMuiltipleChoice(String email, LearningMultipleChoiceRequestDto learningMultipleChoiceRequestDto) {

	    QuestionCheckResultDto questionCheckResultDto = multipleChoiceService.checkResult(
	            learningMultipleChoiceRequestDto.getQuestionNo(),
	            learningMultipleChoiceRequestDto.getSelectedIndex()
	    );

	    Member member = memberService.getActiveMemberByEmail(email);

	    recordLearning(
	    		member, 
	    		questionCheckResultDto.getMultipleChoice(), 
	    		questionCheckResultDto.getIsCorrect(),
	            learningMultipleChoiceRequestDto.getQuestionSolvingTime()
	    );

	    updateResultAndMetric(member);
	}

	@Transactional
	private void updateResultAndMetric(Member member) {
		
		Result result = resultService.getActiveMemberResultByEmail(member.getEmail());
		
		List<Learning> allLearningList = learningRepository.findAllByMember(member);
		
		if(allLearningList.isEmpty()) {
			
			return;
			
		}
		
		long totalCorrect = allLearningList.stream().filter(Learning::getIsCorrect).count();
		
		double overallCorrectRate = (double) totalCorrect / allLearningList.size();
		
		result.updateOverallCorrectAnswerRate(overallCorrectRate);
		
		List<ResultMetric> metricList = resultMetricService.getAllByResult(result);
		
		for(ResultMetric metric : metricList) {
			
			List<Learning> filteredLearningList = allLearningList.stream()
					.filter(learning -> {
						Passage passage = learning.getQuestion().getPassage();
						boolean languageMatch = passage.getLanguage().equals(metric.getLanguage());
						boolean categoryMatch = passage.getCategory().equals(metric.getCategory());
						boolean typeMatch = (metric.getType() == null) || passage.getType().equals(metric.getType());
						boolean levelMatch = (metric.getLevel() == null) || passage.getLevel().equals(metric.getLevel());
						return languageMatch && categoryMatch && typeMatch && levelMatch;
					})
					.collect(Collectors.toList());
		
			if(!filteredLearningList.isEmpty()) {
				
				long metricCorrect = filteredLearningList.stream().filter(Learning::getIsCorrect).count();
				double metricCorrectRate = (double)metricCorrect / filteredLearningList.size();
				double metricAverageTime = filteredLearningList.stream()
						.mapToLong(Learning::getQuestionSolvingTime)
						.average()
						.orElse(0.0);
				
				metric.updateMetric(metricCorrectRate, (long)metricAverageTime);
				
			}
			
		}
		
	}

	@Transactional
	public void recordLearning(Member member, Question question, Boolean isCorrect, Long solvingTime) {
		
		Learning learning = Learning.builder()
				.isCorrect(isCorrect)
				.questionSolvingTime(solvingTime)
				.question(question)
				.member(member)
				.build();
		
		learningRepository.save(learning);

	}

	@Transactional(readOnly = true)
	public List<Long> getAllByMemberWithPassageNo(Member member) {

		return learningRepository.findAllByMember(member).stream()
				.map(learning -> learning.getQuestion().getPassage().getPassageNo())
				.distinct()
				.collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public Integer getTodaySolvedQuestionCount(String email) {
		
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDateTime.now();

		return learningRepository.countByMember_EmailAndCreatedAtBetween(email, startOfDay, endOfDay);

	}

	@Transactional(readOnly = true)
	public List<QuestionSummaryResponseDto> getTotalLearning(String email) {

		List<Learning> totalLearningList = learningRepository.findAllByMember_Email(email);
		
		if(totalLearningList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LEARNING_NOT_FOUND);
			
		}
		
		return totalLearningList.stream()
				.map(QuestionSummaryResponseDto::new)
				.collect(Collectors.toList());
				
	}

	@Transactional(readOnly = true)
	public List<QuestionSummaryResponseDto> getTotalIncorrectLearning(String email) {

		List<Learning> totalIncorrectLearningList = learningRepository.findIncorrectLearningByMember_Email(email);

		if(totalIncorrectLearningList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LEARNING_NOT_FOUND);
			
		}
		
		return totalIncorrectLearningList.stream()
				.map(QuestionSummaryResponseDto::new)
				.collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public List<QuestionSummaryResponseDto> getTodayLearning(String email) {
		
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDateTime.now();

		List<Learning> todayLearningList = learningRepository.findTodayLearningByMember_Email(email, startOfDay, endOfDay);
		
		if(todayLearningList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LEARNING_NOT_FOUND);
			
		}
		
		return todayLearningList.stream()
				.map(QuestionSummaryResponseDto::new)
				.collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public List<QuestionSummaryResponseDto> getTodayIncorrectLearning(String email) {
		
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDateTime.now();

		List<Learning> todayIncorrectLearningList = learningRepository.findTodayIncorrectLearningByMember_Email(email, startOfDay, endOfDay);
		
		if(todayIncorrectLearningList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LEARNING_NOT_FOUND);
			
		}
		
		return todayIncorrectLearningList.stream()
				.map(QuestionSummaryResponseDto::new)
				.collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public List<QuestionSummaryResponseDto> getFavoritLearning(String email) {
		
		List<Learning> favoritLearningList = learningRepository.findFavoritLearningByMember_Email(email);

		if(favoritLearningList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LEARNING_NOT_FOUND);
			
		}
		
		return favoritLearningList.stream()
				.map(QuestionSummaryResponseDto::new)
				.collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public List<PassageResponseDto> getMostIncorrectPassages(LanguageEnum language, Integer number) {

		List<Long> topIncorrectQuestionIdList = learningRepository.findMostIncorrectQuestionNosByLanguage(language, PageRequest.of(0, 100));
		
		if(topIncorrectQuestionIdList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LEARNING_NOT_FOUND);
			
		}
		
		List<Learning> incorrectLearningList = learningRepository.findAllByQuestionQuestionNoIn(topIncorrectQuestionIdList);
		
		List<Passage> uniquePassageList = incorrectLearningList.stream()
				.map(learning -> learning.getQuestion().getPassage())
				.distinct()
				.limit(number)
				.collect(Collectors.toList());
		
		return uniquePassageList.stream()
				.map(PassageResponseDto::new)
				.collect(Collectors.toList());
		
	}
	
	@Transactional(readOnly = true)
	public List<Long> getAllSolvedQuestionNos(Member member) {
	    return learningRepository.findAllByMember(member).stream()
	            .map(learning -> learning.getQuestion().getQuestionNo())
	            .distinct()
	            .collect(Collectors.toList());
	}

}
