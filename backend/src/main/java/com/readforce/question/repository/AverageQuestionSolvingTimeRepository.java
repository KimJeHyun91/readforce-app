package com.readforce.question.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.member.entity.AgeGroup;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;
import com.readforce.question.entity.AverageQuestionSolvingTime;

@Repository
public interface AverageQuestionSolvingTimeRepository extends JpaRepository<AverageQuestionSolvingTime, Long> {

	Optional<AverageQuestionSolvingTime> findByAgeGroupAndCategoryAndTypeAndLevelAndLanguage(
			AgeGroup ageGroup,
			Category category,
			Type type, 
			Level level,
			Language language
	);

}
