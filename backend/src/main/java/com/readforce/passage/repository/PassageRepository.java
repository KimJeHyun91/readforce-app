package com.readforce.passage.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.ClassificationEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;
import com.readforce.passage.entity.Passage;



@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {

	@Query("""
			SELECT p 
			FROM Passage p 
			JOIN FETCH p.category c 
			LEFT JOIN FETCH p.type t 
			JOIN FETCH p.level l
			JOIN FETCH p.language lang
			JOIN FETCH p.classification cl
			WHERE lang.languageName = :language
			AND cl.classificationName = :classification
			AND c.categoryName = :category
	""")

	List<Passage> findByLanguageAndCategory(
			@Param("language") LanguageEnum language, 
			@Param("classification") ClassificationEnum classification, 
			@Param("category") CategoryEnum category,

			Sort sort
	);

	@Query("""
			SELECT p 
			FROM Passage p 
			JOIN FETCH p.category c 
			JOIN FETCH p.type t 
			JOIN FETCH p.level l
			JOIN FETCH p.language lang
			JOIN FETCH p.classification cl
			WHERE lang.languageName = :language
			AND cl.classificationName = :classification
			AND c.categoryName = :category
			AND t.typeName = :type
	""")

	List<Passage> findByLanguageAndClassificationAndCategoryAndType(
			@Param("language") LanguageEnum language, 
			@Param("classification") ClassificationEnum classification, 
			@Param("category") CategoryEnum category,
			@Param("type") TypeEnum type, 
			Sort sort
	);

	@Query("""
			SELECT p 
			FROM Passage p 
			JOIN FETCH p.category c 
			JOIN FETCH p.type t 
			JOIN FETCH p.level l
			JOIN FETCH p.language lang
			JOIN FETCH p.classification cl
			WHERE lang.languageName = :language
			AND cl.classificationName = :classification
			AND c.categoryName = :category
			AND t.typeName = :type
			AND l.levelNumber = :level
	""")
	List<Passage> findByLanguageAndClassificationAndCategoryAndTypeAndLevel(
			@Param("language") LanguageEnum language, 
			@Param("classification") ClassificationEnum classification, 
			@Param("category") CategoryEnum category,
			@Param("type") TypeEnum type,
			@Param("level") Integer level,
			Sort sort
	);

	@Query("""
			SELECT p.passageNo 
			FROM Passage p
			JOIN p.category c
			JOIN p.type t
			JOIN p.level l
			JOIN p.language lang
			JOIN p.classification cl

			WHERE lang.languageName = :language 
			AND cl.classificationName = :classification
			AND c.categoryName = :category
			AND t.typeName = :type
			AND l.levelNumber = :level
	""")
	List<Long> findPassageNoByLanguageAndClassificationAndCategoryAndTypeAndLevel(
			@Param("language") LanguageEnum language, 
			@Param("classification") ClassificationEnum classification, 
			@Param("category") CategoryEnum category,
			@Param("type") TypeEnum type,
			@Param("level") Integer level
	);
	
	@Query("""
			SELECT p
			FROM Passage p 
			LEFT JOIN FETCH p.category
			LEFT JOIN FETCH p.type
			LEFT JOIN FETCH p.level
			LEFT JOIN FETCH p.classification
			LEFT JOIN Question q ON p.passageNo = q.passage.passageNo
			WHERE q.passage.passageNo IS NULL
	""")
	List<Passage> findNoQuestionPassageList();

	long countByLanguage_LanguageNameAndCategory_CategoryNameAndLevel_LevelNumberAndClassification_ClassificationName(
			LanguageEnum language, 
			CategoryEnum category, 
			Integer level, 
			ClassificationEnum test
	);

	@Query("""
			SELECT p
			FROM Passage p
			WHERE p.language.languageName = :language
			AND p.category.categoryName = :category
			AND p.level.levelNumber = :level
			AND p.classification.classificationName = :classification					
	""")
	Page<Passage> findByLanguageAndCategoryAndLevelAndClassification(
			@Param("language") LanguageEnum language, 
			@Param("category") CategoryEnum category,
			@Param("level") Integer level, 
			@Param("classification") ClassificationEnum classification, 
			Pageable pageable
	);

	
	@Query("""
			SELECT p.passageNo
			FROM Passage p
			JOIN p.category c
			JOIN p.type t
			JOIN p.level l
			JOIN p.language lang
			JOIN p.classification cl
			WHERE lang.languageName = :language
			AND cl.classificationName = :classification
			AND c.categoryName = :category
			AND l.levelNumber = :level	
	""")
	List<Long> findPassageNoByLanguageAndCategoryAndLevelAndClassification(
			@Param("language") LanguageEnum language, 
			@Param("category") CategoryEnum category,
			@Param("level") Integer level, 
			@Param("classification") ClassificationEnum classification
	);

	@Query("""
			SELECT p 
			FROM Passage p 
			JOIN FETCH p.category c 
			LEFT JOIN FETCH p.type t 
			JOIN FETCH p.level l
			JOIN FETCH p.language lang
			JOIN FETCH p.classification cl
			WHERE lang.languageName = :language
			AND cl.classificationName = :classification
			AND c.categoryName = :category
			AND l.levelNumber = :level
	""")
	List<Passage> findByLanguageAndCategoryAndLevel(
			@Param("language") LanguageEnum language, 
			@Param("classification") ClassificationEnum classification,
			@Param("category") CategoryEnum category, 
			@Param("level") Integer level, 
			Sort sort
	);

	Page<Passage> findAllByClassification_ClassificationName(ClassificationEnum classificationName, Pageable pageable);
	
	
	

}