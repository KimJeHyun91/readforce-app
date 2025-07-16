package com.readforce.passage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.LanguageEnum;
import com.readforce.passage.entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

	Optional<Language> findByLanguageName(LanguageEnum language);

}
