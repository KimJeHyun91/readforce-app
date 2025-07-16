package com.readforce.passage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.passage.entity.Language;
import com.readforce.passage.repository.LanguageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {
	
	private final LanguageRepository languageRepository;

	@Transactional(readOnly = true)
	public List<Language> getAllLanguageList() {
		
		return languageRepository.findAll();
		
	}

	@Transactional(readOnly = true)
	public Language getLangeageByLanguage(LanguageEnum language) {

		return languageRepository.findByLanguageName(language)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.LANGUAGE_NOT_FOUND));

	}

	@Transactional
	public void createLanguage(Language language) {

		languageRepository.save(language);
		
	}

	@Transactional
	public Language getLanguageByLanguageNo(Long languageNo) {
		
		return languageRepository.findById(languageNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.LANGUAGE_NOT_FOUND));
		
	}
	
	@Transactional
	public void modifyLanguage(Long languageNo, LanguageEnum languageName) {

		Language language = getLanguageByLanguageNo(languageNo);
		
		language.chageLanguageName(languageName);
		
	}

	@Transactional
	public void deleteLanguageByLanguageNo(Long languageNo) {

		languageRepository.deleteById(languageNo);		
		
	}

}
