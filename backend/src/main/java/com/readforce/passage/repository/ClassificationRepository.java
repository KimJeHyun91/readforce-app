package com.readforce.passage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.ClassificationEnum;
import com.readforce.passage.entity.Classification;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long> {

	Optional<Classification> findByClassificationName(ClassificationEnum classification);


}
