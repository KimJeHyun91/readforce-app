package com.readforce.result.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;
import com.readforce.passage.service.CategoryService;
import com.readforce.passage.service.LanguageService;
import com.readforce.passage.service.LevelService;
import com.readforce.passage.service.TypeService;
import com.readforce.result.entity.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultMetricEventService {
	
	private final ResultService resultService;
	private final ResultMetricService resultMetricService;
	private final LanguageService languageService;
	private final TypeService typeService;
	private final CategoryService categoryService;
	private final LevelService levelService;
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createResultMetricsForMember(Long resultNo) {

		Result result = resultService.getResultByResultNo(resultNo);
		
		List<Language> languageList = languageService.getAllLanguageList();
		List<Category> categoryList = categoryService.getAllCategoryList();
		List<Type> typeList = typeService.getAllTypeList();
		List<Level> levelList = levelService.getAllLevelList();
			
		for(Language language : languageList) {
			
			for(Category category : categoryList) {
				
				resultMetricService.createResultMetric(result, language, category, null, null);
				
				for(Type type : typeList) {
					
					resultMetricService.createResultMetric(result, language, category, type, null);
					
					for(Level level : levelList) {
						
						resultMetricService.createResultMetric(result, language, category, type, level);
						
					}
					
				}				
				
			}
			
		}
			
		
	}

}
