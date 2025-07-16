package com.readforce.passage.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.administrator.dto.AdministratorCategoryModifyRequestDto;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Member;
import com.readforce.passage.entity.Category;
import com.readforce.passage.repository.CategoryRepository;
import com.readforce.result.entity.Result;
import com.readforce.result.entity.ResultMetric;
import com.readforce.result.service.ResultMetricService;
import com.readforce.result.service.ResultService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final ResultMetricService resultMetricService;
	private final ResultService resultService;

	@Transactional(readOnly = true)
	public List<Category> getAllCategoryList() {

		return categoryRepository.findAll();

	}

	@Transactional(readOnly = true)
	public CategoryEnum findWeakCategory(Member member, LanguageEnum language) {

		Result result = resultService.getActiveMemberResultByEmail(member.getEmail());
		
		List<ResultMetric> metricList = resultMetricService.getAllByResultAndLanguage_Language(result, language);
		
		for(double threshold = 0.65; threshold <= 1.0; threshold += 0.05) {
			
			final double currentThreshold = threshold;
			
			Optional<Category> weakCategory = metricList.stream()
					.filter(metric -> metric.getCategory() != null && metric.getCorrectAnswerRate() <= currentThreshold)
					.min(Comparator.comparing(ResultMetric::getCorrectAnswerRate))
					.map(ResultMetric::getCategory);
			
			if(weakCategory.isPresent()) {
				
				return weakCategory.get().getCategoryName();
				
			}
			
		}
		
		throw new ResourceNotFoundException(MessageCode.WEAK_CATEGORY_NOT_FOUND);
		
	}

	@Transactional(readOnly = true)
	public Category getCategoryByCategory(CategoryEnum category) {
		
		return categoryRepository.findByCategoryName(category)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.CATEGORY_NOT_FOUND));
		
	}

	@Transactional
	public void saveCategory(Category category) {
		
		categoryRepository.save(category);
		
	}
	
	@Transactional
	public Category getCategoryByCategoryNo(Long categoryNo) {
		
		return categoryRepository.findById(categoryNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.CATEGORY_NOT_FOUND));
		
	}

	@Transactional
	public void modifyCategory(@Valid AdministratorCategoryModifyRequestDto requestDto) {

		Category category = getCategoryByCategoryNo(requestDto.getCategoryNo());
		
		category.changeCategoryName(requestDto.getCategory());
		
		saveCategory(category);
		
	}

	public void deleteCategory(Long categoryNo) {

		categoryRepository.deleteById(categoryNo);		
		
	}
	
}
