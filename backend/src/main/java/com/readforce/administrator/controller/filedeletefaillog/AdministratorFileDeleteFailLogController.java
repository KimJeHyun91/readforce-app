package com.readforce.administrator.controller.filedeletefaillog;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.common.MessageCode;
import com.readforce.common.service.FileDeleteFailLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/administrator/file-delete-fail-log")
@RequiredArgsConstructor
@Validated
public class AdministratorFileDeleteFailLogController {
	
	private final FileDeleteFailLogService fileDeleteFailLogService;

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/retry-delete-failed-files")
	public ResponseEntity<Map<String, String>> retryDeleteFailedFiles(){
		
		fileDeleteFailLogService.retryDeleteFailedFiles();
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.RETRY_FILE_DELETE_FAILED_FILE
		));
		
	}
	
}
