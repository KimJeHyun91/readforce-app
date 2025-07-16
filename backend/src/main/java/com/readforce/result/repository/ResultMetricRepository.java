package com.readforce.result.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.LanguageEnum;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;
import com.readforce.result.entity.Result;
import com.readforce.result.entity.ResultMetric;

@Repository
public interface ResultMetricRepository extends JpaRepository<ResultMetric, Long> {

	List<ResultMetric> findAllByResultAndLanguage_LanguageName(Result result, LanguageEnum language);

	List<ResultMetric> findAllByResult(Result result);

	void deleteAllByResult(Result result);

	@Query("""
			SELECT rm
			FROM ResultMetric rm
			WHERE rm.result = :result
			AND rm.language = :language
			AND rm.category = :category
			AND ((rm.type IS NULL AND :type IS NULL) OR rm.type = :type)
			AND ((rm.level IS NULL AND :level IS NULL) or rm.level = :level)			
	""")
	Optional<ResultMetric> findMetric(
			@Param("result") Result result, 
			@Param("language") Language language, 
			@Param("category") Category category, 
			@Param("type") Type type, 
			@Param("level") Level level
	);
	
}
