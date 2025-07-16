package com.readforce.result.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.member.entity.Member;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.result.entity.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

	@Query("""
			SELECT s
			FROM Score s
			JOIN FETCH s.member m
			JOIN FETCH s.category c
			JOIN FETCH s.language l
			WHERE c.categoryName = :category AND l.languageName = :language
			ORDER BY s.score DESC			
			""")
	List<Score> findTopScoreListByCategoryAndLanguage(
			@Param("category") CategoryEnum category,
			@Param("language") LanguageEnum language,
			Pageable pageable			
	);

	List<Score> findByMember_Email(String email);
	
	Optional<Score> findByMemberAndCategoryAndLanguage(Member member, Category category, Language language);
	
}