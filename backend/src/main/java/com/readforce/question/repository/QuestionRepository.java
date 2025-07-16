package com.readforce.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.question.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	Optional<Question> findByPassage_PassageNo(Long passageNo);

	@Query("SELECT q FROM Question q JOIN FETCH q.passage WHERE q.questionNo = :questionNo")
	Optional<Question> findByIdWithPassage(
			@Param("questionNo") Long questionNo
	);

	@Query("SELECT DISTINCT q.passage.passageNo FROM Question q")
	List<Long> findAllUsedPassageNo();
	
}
