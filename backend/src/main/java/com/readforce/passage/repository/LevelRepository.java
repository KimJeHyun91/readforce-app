package com.readforce.passage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.passage.entity.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

	Optional<Level> findByLevelNumber(Integer level);

}
