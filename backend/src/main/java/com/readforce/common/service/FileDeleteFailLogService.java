package com.readforce.common.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.common.entity.FileDeleteFailLog;
import com.readforce.common.repository.FileDeleteFailLogRepository;
import com.readforce.file.service.FileService;
import com.readforce.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileDeleteFailLogService {

	private final FileDeleteFailLogRepository fileDeleteFailLogRepository;
	private final FileService fileService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void create(Member member, String message) {

		FileDeleteFailLog fileDelteFailLog = FileDeleteFailLog.builder()
				.member(member)
				.filePath(member.getProfileImagePath())
				.reason(message)
				.build();
		
		fileDeleteFailLogRepository.save(fileDelteFailLog);
		
	}
	
	@Transactional(readOnly = true)
	public List<FileDeleteFailLog> getAllList(){
		
		return fileDeleteFailLogRepository.findAll();
		
	}
	
	@Transactional
	public void retryDeleteFailedFiles() {
		
		List<FileDeleteFailLog> failedLogList = getAllList();
		
		if(failedLogList.isEmpty()) {
			
			log.info("삭제에 실패한 파일이 없습니다.");
			
			return;
			
		}
		
		log.info("{}개의 삭제 실패 파일에 대한 재시도를 시작합니다.", failedLogList.size());
		
		for(FileDeleteFailLog fileDeleteFailLog : failedLogList) {
			
			try {
				
				fileService.deleteFile(fileDeleteFailLog.getFilePath(), com.readforce.common.enums.FileCategoryEnum.PROFILE_IMAGE);
				
				fileDeleteFailLogRepository.delete(fileDeleteFailLog);
				
				log.info("파일 삭제 성공 및 로그 제거: {}", fileDeleteFailLog.getFilePath());
				
			} catch(Exception exception) {
				
				log.error("파일 삭제 재시도 실패: {}", fileDeleteFailLog.getFilePath(), exception);
				
			}
			
		}
		
		log.info("파일 삭제 재시도를 완료했습니다.");
		
	}
	
}
