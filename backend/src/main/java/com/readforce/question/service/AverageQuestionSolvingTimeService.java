package com.readforce.question.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.administrator.dto.AdministratorAverageQuestionSolvingTimeRequestDto;
import com.readforce.member.entity.AgeGroup;
import com.readforce.member.service.AgeGroupService;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;
import com.readforce.passage.service.CategoryService;
import com.readforce.passage.service.LanguageService;
import com.readforce.passage.service.TypeService;
import com.readforce.question.entity.AverageQuestionSolvingTime;
import com.readforce.question.repository.AverageQuestionSolvingTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AverageQuestionSolvingTimeService {

	private final AverageQuestionSolvingTimeRepository averageQuestionSolvingTimeRepository;
	private final AgeGroupService ageGroupService;
	private final CategoryService categoryService;
	private final TypeService typeService;
	private final LanguageService languageService;
	
	
	@Transactional(readOnly = true)
	public Optional<Long> getAverageTime(AgeGroup ageGroup, Category category, Type type, Level level, Language language){
		
		return averageQuestionSolvingTimeRepository
				.findByAgeGroupAndCategoryAndTypeAndLevelAndLanguage(ageGroup, category, type, level, language)
				.map(AverageQuestionSolvingTime::getAverageQuestionSolvingTime);				
		
	}
	
	@Transactional(readOnly = true)
	public List<AverageQuestionSolvingTime> getAllAverageQuestionSolvingTimeList(){
		
		return averageQuestionSolvingTimeRepository.findAll();
		
	}

	@Transactional
	public void createAverageQuestionSolvingTime(AdministratorAverageQuestionSolvingTimeRequestDto requestDto, Level level) {
		
		AgeGroup ageGroup = ageGroupService.getAgeGroupByAgeGroup(requestDto.getAgeGroup());
		
		Category category = categoryService.getCategoryByCategory(requestDto.getCategory());
		
		Type type = typeService.getTypeByType(requestDto.getType());
		
		Language language = languageService.getLangeageByLanguage(requestDto.getLanguage());
		
		AverageQuestionSolvingTime averageQuestionSolvingTime = AverageQuestionSolvingTime.builder()
				.averageQuestionSolvingTime(requestDto.getAverageQuestionSolvingTime())
				.ageGroup(ageGroup)
				.category(category)
				.type(type)
				.level(level)
				.language(language)
				.build();
		
		averageQuestionSolvingTimeRepository.save(averageQuestionSolvingTime);
		
	}

	@Transactional
	public void deleteAverageQuestionSolvingTime(Long averageQuestionSolvingTimeNo) {
		
		averageQuestionSolvingTimeRepository.deleteById(averageQuestionSolvingTimeNo);
		
	}
	
	
	
}
