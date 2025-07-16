package com.readforce.passage.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.AgeGroup;
import com.readforce.member.entity.Member;
import com.readforce.member.service.AgeGroupService;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;
import com.readforce.passage.repository.LevelRepository;
import com.readforce.question.service.AverageQuestionSolvingTimeService;
import com.readforce.result.entity.Result;
import com.readforce.result.entity.ResultMetric;
import com.readforce.result.service.ResultMetricService;
import com.readforce.result.service.ResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelService {
	
	private final LevelRepository levelRepository;
	private final ResultService resultService;
	private final ResultMetricService resultMetricService;
	private final AgeGroupService ageGroupService;
	private final AverageQuestionSolvingTimeService averageQuestionSolvingTimeService;
	private final LanguageService languageService;
	private final CategoryService categoryService;
	private final TypeService typeService;
	
	@Transactional(readOnly = true)
	public List<Level> getAllLevelList() {

		return levelRepository.findAll();
		
	}
	
	@Transactional(readOnly = true)
	public Level getLevelByLevel(Integer level) {
		
		return levelRepository.findByLevelNumber(level)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.LEVEL_NOT_FOUND));
		
	}

	@Transactional(readOnly = true)
	public String getVocabularyLevelByLevel(Integer level) {
		
		return getLevelByLevel(level).getVocabularyLevel();
		
	}

	@Transactional(readOnly = true)
	public Integer findOptimalLevel(Member member, LanguageEnum language, CategoryEnum weakCategory, TypeEnum weakType) {
		
		Integer[] optimalLevelRange = findOptimalLevelRange(member.getEmail(), language, weakCategory, weakType);

		return findFinalRecommenedLevel(member, language, optimalLevelRange[0], optimalLevelRange[1], weakCategory, weakType);

	}

	private Integer findFinalRecommenedLevel(Member member, LanguageEnum language, Integer skilledLevel, Integer challengeLevel, CategoryEnum weakCategory, TypeEnum weakType) {
		
		AgeGroup ageGroup = ageGroupService.getAgeGroupForMember(member);
		
		Level skilledLevelEntity = getLevelByLevel(skilledLevel);
		
		Language languageEntity = languageService.getLangeageByLanguage(language);
		
		Category weakCategoryEntity = categoryService.getCategoryByCategory(weakCategory);
		
		Type weakTypeEntity = typeService.getTypeByType(weakType);
		
		long overallAverageSolvingTime = averageQuestionSolvingTimeService
				.getAverageTime(ageGroup, weakCategoryEntity, weakTypeEntity, skilledLevelEntity, languageEntity)
				.orElse(Long.MAX_VALUE);
		
		Result result = resultService.getActiveMemberResultByEmail(member.getEmail());
		
		double memberAverageSolvingTime = resultMetricService.getAllByResultAndLanguage_Language(result, language).stream()
				.filter(metric -> skilledLevelEntity.equals(metric.getLevel()))
				.mapToLong(ResultMetric::getQuestionSolvingTimeAverage)
				.findFirst()
				.orElse(0L);
		
		if(memberAverageSolvingTime <= overallAverageSolvingTime) {
			
			return challengeLevel;
			
		} else {
			
			return skilledLevel;
			
		}

	}

	private Integer[] findOptimalLevelRange(String email, LanguageEnum language, CategoryEnum weakCategory, TypeEnum weakType) {

		Result result = resultService.getActiveMemberResultByEmail(email);
		
	    List<ResultMetric> metricList = resultMetricService.getAllByResultAndLanguage_Language(result, language);

	    Map<Integer, Double> levelCorrectRateMap = metricList.stream()
	        .filter(metric -> metric.getCategory() != null && metric.getCategory().getCategoryName() == weakCategory)
	        .filter(metric -> metric.getType() != null && metric.getType().getTypeName() == weakType)
	        .filter(metric -> metric.getLevel() != null && metric.getCorrectAnswerRate() != null)
	        .collect(Collectors.toMap(
	            metric -> metric.getLevel().getLevelNumber(),
	            ResultMetric::getCorrectAnswerRate,
	            (rate1, rate2) -> rate1
	        ));

	    Integer skilledLevel = null;
	    
	    for (double threshold = 0.80; threshold >= 0.0; threshold -= 0.05) {
	    	
	        final double currentThreshold = threshold;

	        Optional<Integer> skilledLevelOptional = levelCorrectRateMap.entrySet().stream()
	            .filter(entry -> entry.getValue() >= currentThreshold)
	            .map(Map.Entry::getKey)
	            .max(Comparator.naturalOrder());

	        if (skilledLevelOptional.isPresent()) {
	        	
	            skilledLevel = skilledLevelOptional.get();
	            
	            break;
	        }
	        
	    }

	    if (skilledLevel == null) {
	    	
	        skilledLevel = levelCorrectRateMap.keySet().stream()
	            .min(Integer::compareTo)
	            .orElse(1);
	        
	    }

	    Integer challengeLevel = null;
	    
	    for (double threshold = 0.50; threshold >= 0.0; threshold -= 0.05) {
	       
	    	final Integer finalSkilledLevel = skilledLevel;
	    	
	        final double currentThreshold = threshold;

	        Optional<Integer> challengeLevelOptional = levelCorrectRateMap.entrySet().stream()
	            .filter(entry -> entry.getKey() > finalSkilledLevel && entry.getValue() <= currentThreshold)
	            .map(Map.Entry::getKey)
	            .min(Comparator.naturalOrder());

	        if (challengeLevelOptional.isPresent()) {
	        	
	            challengeLevel = challengeLevelOptional.get();
	           
	            break;
	            
	        }
	    }

	    if (challengeLevel == null) {

	        challengeLevel = skilledLevel + 1;
	        
	    }

	    return new Integer[]{skilledLevel, challengeLevel};
	    
	}


	
	
	@Transactional
	public void saveLevel(Level level) {

		levelRepository.save(level);
		
	}
	
	@Transactional(readOnly = true)
	public Level getLevelByLevelNo(Long levelNo) {
		
		return levelRepository.findById(levelNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.LEVEL_NOT_FOUND));
		
	}

	@Transactional
	public void modifyLevel(
			Long levelNo, 
			Integer levelNumber, 
			Integer paragraphCount, 
			String vocabularyLevel,
			String sentenceStructure, 
			String questionType
	) {
		
		Level level = getLevelByLevelNo(levelNo);
		
		level.changeInfo(levelNumber, paragraphCount, vocabularyLevel, sentenceStructure, questionType);
		
		saveLevel(level);
		
	}

	@Transactional
	public void deleteLevelByLevelNo(Long levelNo) {

		levelRepository.deleteById(levelNo);
		
	}

	


}