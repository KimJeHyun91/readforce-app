package com.readforce.passage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.TypeEnum;
import com.readforce.passage.entity.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

	Optional<Type> findByTypeName(TypeEnum type);

}
