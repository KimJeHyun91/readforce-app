package com.readforce.result.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;
import com.readforce.result.entity.Result;
import com.readforce.result.entity.ResultMetric;
import com.readforce.result.repository.ResultMetricRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultMetricService {
	
	private final ResultMetricRepository resultMetricRepository;
	
	@Transactional(readOnly = true)
	public void createResultMetric(Result result, Language language, Category category, Type type, Level level) {

		Optional<ResultMetric> existingMetric = resultMetricRepository.findMetric(
				result, language, category, type, level
		);
		
		if(existingMetric.isEmpty()) {
			
			ResultMetric resultMetric = ResultMetric.builder()
					.result(result)
					.language(language)
					.category(category)
					.type(type)
					.level(level)
					.build();
			
			resultMetricRepository.save(resultMetric);	
			
		}

	}

	@Transactional(readOnly = true)
	public List<ResultMetric> getAllByResultAndLanguage_Language(Result result, LanguageEnum language) {

		return resultMetricRepository.findAllByResultAndLanguage_LanguageName(result, language);

	}

	@Transactional(readOnly = true)
	public List<ResultMetric> getAllByResult(Result result) {
		
		List<ResultMetric> resultList = resultMetricRepository.findAllByResult(result);
		
		if(resultList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.RESULT_METRIC_NOT_FOUND);
			
		}
		
		return resultList;

	}

	@Transactional(readOnly = true)
	public Map<String, Double> getCategoryCorrectAnswerRate(Result result) {
		
		List<ResultMetric> resultMetricList = resultMetricRepository.findAllByResult(result);
		
		if(resultMetricList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.RESULT_METRIC_NOT_FOUND);
			
		}
		
		return resultMetricList.stream()
				.filter(metric -> metric.getCategory() != null && metric.getType() == null && metric.getLevel() == null)
				.collect(Collectors.toMap(
						metric -> metric.getCategory().getCategoryName().name(),
						ResultMetric::getCorrectAnswerRate,
						(rate1, rate2) -> rate2
				));

	}

	@Transactional(readOnly = true)
	public Map<String, Double> getTypeCorrectAnswerRate(Result result, CategoryEnum category) {

		List<ResultMetric> resultMetricList = resultMetricRepository.findAllByResult(result);
		
		if(resultMetricList.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.RESULT_METRIC_NOT_FOUND);
			
		}
		
		return resultMetricList.stream()
				.filter(metric -> metric.getCategory().getCategoryName().name() != null && metric.getCategory().getCategoryName().name().equals(category.name()))
				.filter(metric -> metric.getType() == null && metric.getLevel() == null)
				.collect(Collectors.toMap(
						metric -> metric.getType().getTypeName().name(),
						ResultMetric::getCorrectAnswerRate,
						(rate1, rate2) -> rate2
				));
		
	}

	@Transactional
	public void deleteAllByResult(Result result) {

		resultMetricRepository.deleteAllByResult(result);		
		
	}

	@Transactional(readOnly = true)
	private ResultMetric getResultMetricByResultMetricNo(Long resultMetricNo) {

		return resultMetricRepository.findById(resultMetricNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.RESULT_METRIC_NOT_FOUND));
		
	}

	public void modifyResultMetric(Long resultMetricNo, Double correctAnswerRate, Long questionSolvingTimeAverage) {

		ResultMetric resultMetric = getResultMetricByResultMetricNo(resultMetricNo);
		
		resultMetric.updateMetric(correctAnswerRate, questionSolvingTimeAverage);
		
	}

}
