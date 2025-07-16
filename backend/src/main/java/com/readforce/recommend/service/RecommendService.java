package com.readforce.recommend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Member;
import com.readforce.passage.service.CategoryService;
import com.readforce.passage.service.LevelService;
import com.readforce.passage.service.TypeService;
import com.readforce.question.dto.MultipleChoiceResponseDto;
import com.readforce.question.service.MultipleChoiceService;
import com.readforce.result.service.LearningService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
	
	private final CategoryService categoryService;
	private final TypeService typeService;
	private final LevelService levelService;
	private final MultipleChoiceService multipleChoiceService;
	private final LearningService learningService;
	
	public MultipleChoiceResponseDto getRecommendQuestion(Member member, LanguageEnum language) {

	    CategoryEnum weakCategory = categoryService.findWeakCategory(member, language);
	    
	    TypeEnum weakType = typeService.findWeakType(member, language, weakCategory);
	    
	    Integer optimalLevel = levelService.findOptimalLevel(member, language, weakCategory, weakType);
	    
	    List<Long> solvedQuestionNos = learningService.getAllSolvedQuestionNos(member);

	    try {

	    	return multipleChoiceService.getUnsolvedMultipleChoiceQuestion(
	                member, language, weakCategory, weakType, optimalLevel, solvedQuestionNos
	        );
	    	
	    } catch (ResourceNotFoundException e) {
	    	
	        log.warn("추천 문제 탐색 실패 (최적 레벨: {}). 대안 탐색을 시작합니다. 사용자: {}", optimalLevel, member.getEmail());
	        
	    }

	    for (CategoryEnum category : CategoryEnum.values()) {
	    	
	         for (TypeEnum type : TypeEnum.values()) {
	        	 
	            if (category == weakCategory && type == weakType) continue;
	            
	            try {
	            	
	                return multipleChoiceService.getUnsolvedMultipleChoiceQuestion(
	                		
	                        member, language, category, type, optimalLevel, solvedQuestionNos
	                        
	                );
	                
	            } catch (ResourceNotFoundException ignored) {

	            }
	            
	        }
	         
	    }

        int[] levelAdjustments = {-1, 1};
        
        for (int adjustment : levelAdjustments) {
        	
            int adjustedLevel = optimalLevel + adjustment;
            
            if (adjustedLevel >= 1 && adjustedLevel <= 10) { 
            	
                 for (CategoryEnum category : CategoryEnum.values()) {
                	 
                     for (TypeEnum type : TypeEnum.values()) {
                    	 
                        try {
                        	
                            log.warn("대안 탐색 (조정된 레벨: {})", adjustedLevel);
                            
                            return multipleChoiceService.getUnsolvedMultipleChoiceQuestion(
                            		
                                    member, language, category, type, adjustedLevel, solvedQuestionNos
                                    
                            );
                            
                        } catch (ResourceNotFoundException ignored) {

                        }
                        
                    }
                     
                }
                 
            }
            
        }

	    throw new ResourceNotFoundException("추천할 수 있는 문제가 없습니다.");
	    
	}
	
}