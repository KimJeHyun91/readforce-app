package com.readforce.common.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.challenge.service.ChallengeService;
import com.readforce.result.service.ScoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
	
	private final ScoreService scoreService;
	private final ChallengeService challengeService;
	
	@Scheduled(cron = "0 0 23 * * SUN")
	@Transactional
	public void weeklyReset() {
		
		log.info("주간 점수 초기화 및 챌린지 문제 생성을 시작합니다.");
		
		scoreService.deleteAll();
		
		log.info("모든 점수 데이터가 초기화되었습니다.");
		
		challengeService.resetWeeklyChallenge();
		
		log.info("챌린지 문제가 성공적으로 재설정되었습니다.");
		
		log.info("주간 점수 초기화 및 챌린지 초기화가 완료되었습니다.");
		
	}
	
}
