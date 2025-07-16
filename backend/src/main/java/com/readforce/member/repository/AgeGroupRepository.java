package com.readforce.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.member.entity.AgeGroup;

@Repository
public interface AgeGroupRepository extends JpaRepository<AgeGroup, Long> {

	Optional<AgeGroup> findByAgeGroup(int ageGroupValue);
}
