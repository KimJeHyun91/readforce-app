package com.readforce.result.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.StatusEnum;
import com.readforce.result.entity.Result;
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

	Optional<Result> findByMember_EmailAndMember_Status(String email, StatusEnum active);

	@Query("SELECT r.overallCorrectAnswerRate FROM Result r WHERE r.member.email = :email AND r.member.status = :status")
	Optional<Double> findOverallAnswerCorrectRateByMemberEmailAndMemberStatus(
			@Param("email") String email, 
			@Param("status") StatusEnum status
	);

	Optional<Result> findByMember_Email(String email);
	
	
}
