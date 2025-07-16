package com.readforce.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.question.entity.Choice;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {

}
