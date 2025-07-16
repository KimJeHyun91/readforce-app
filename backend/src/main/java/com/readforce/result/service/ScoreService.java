package com.readforce.result.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.MessageCode;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.exception.DuplicationException;
import com.readforce.common.exception.ResourceNotFoundException;
import com.readforce.member.entity.Member;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.ranking.dto.RankingResponseDto;
import com.readforce.result.entity.Score;
import com.readforce.result.repository.ScoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService {
	
	private final ScoreRepository scoreRepository;
	
	@Transactional(readOnly = true)
	public Score getScoreByMemberAndCategoryAndLanguage(Member member, Category category, Language language){
		
		return scoreRepository.findByMemberAndCategoryAndLanguage(member, category, language)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.SCORE_NOT_FOUND));
		
	}
	
	@Transactional
	public void createScore(Member member, Double totalScore, Category category, Language language) {
		
		if(scoreRepository.findByMemberAndCategoryAndLanguage(member, category, language).isPresent()) {
			
			throw new DuplicationException(MessageCode.SCORE_ALREADY_EXIST);
			
		}
		
		Score score = Score.builder()
				.score(totalScore)
				.member(member)
				.category(category)
				.language(language)
				.build();
		
		scoreRepository.save(score);
		
	}
	
	@Transactional(readOnly = true)
	public List<RankingResponseDto> getTop50ByCategory(CategoryEnum category, LanguageEnum language){
		
		Pageable pageable = PageRequest.of(0, 50);
		
		List<Score> scoreList = scoreRepository.findTopScoreListByCategoryAndLanguage(category, language, pageable);

		return scoreList.stream()
				.map(RankingResponseDto::new)
				.collect(Collectors.toList());
		
	}

	@Transactional
	public void deleteAll() {

		scoreRepository.deleteAllInBatch();
		
	}

	@Transactional(readOnly = true)
	public List<Score> getScoreListByEmail(String email) {
		
		return scoreRepository.findByMember_Email(email);

	}

	@Transactional
	public void modifyScoreByEmail(
			Member member, 
			Long scoreNo, 
			Double score, 
			Category category,
			Language language
	) {
		
		Score scoreEntity = getScoreByScoreNo(scoreNo);
		
		scoreEntity.modifyInfo(member, score, category, language);
		
	}

	@Transactional(readOnly = true)
	private Score getScoreByScoreNo(Long scoreNo) {

		return scoreRepository.findById(scoreNo)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.SCORE_NOT_FOUND));
		
	}

	@Transactional(readOnly = true)
	public Optional<Score> findByMemberAndCategoryAndLanguageWithOptional(Member member, Category category, Language language) {

		return scoreRepository.findByMemberAndCategoryAndLanguage(member, category, language);

	}

	@Transactional
	public void updateScoreForChallenge(Score score, double totalScore) {
		
		score.updateScoreForChallenge(totalScore);
		
		scoreRepository.save(score);
		
	}

}