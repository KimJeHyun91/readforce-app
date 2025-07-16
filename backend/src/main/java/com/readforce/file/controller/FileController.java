package com.readforce.file.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.readforce.common.MessageCode;
import com.readforce.member.entity.Member;
import com.readforce.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

	private final MemberService memberService;
	
	@PostMapping("/upload-profile-image")
	public ResponseEntity<Map<String, String>> uploadProfileImage(
		@Valid @RequestParam("profileImageFile") MultipartFile profileImageFile,
		@AuthenticationPrincipal UserDetails userDetails			
	){
		
		String email = userDetails.getUsername();
		
		memberService.uploadProfileImage(email, profileImageFile);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PROFILE_IMAGE_UPLOAD_SUCCESS));
		
	}
	
	@DeleteMapping("/delete-profile-image")
	public ResponseEntity<Map<String, String>> deleteProfileImage(@AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		
		Member member = memberService.getActiveMemberByEmail(email);
		
		memberService.deleteProfileImage(member);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PROFILE_IMAGE_DELETE_SUCCESS));
		
	}
	
	@GetMapping("/get-profile-image")
	public ResponseEntity<Resource> getProfileImage(@AuthenticationPrincipal UserDetails userDetails){
		
		String email = userDetails.getUsername();
		
		Resource resource = memberService.getProfileImage(email);
		
		String contentType = null;
		
		try {
			
			String filename = resource.getFilename();
			
			if(filename == null) {
				
				contentType = "application/octet-stream";
				log.warn("파일 이름을 확인할 수 없습니다. 기본 타입으로 설정됩니다.");
				
			} else {
				
				Path filePath = Paths.get(filename);
				contentType = Files.probeContentType(filePath);
				
				if(contentType == null) {
					
					contentType = "application/octet-stream";
					log.warn("이미지 파일 타입을 결정하지 못했습니다. {}, 기본 타입인 application/octet-stream 타입으로 결정되었습니다.", filename);
					
				}
				
			}
			
		} catch(IOException exception) {
			
			contentType = "application/octet-stream";
			log.warn("이미지 파일에 접근하지 못하여 타입을 결정하지 못했습니다. {}, 기본 타입인 application/octet-stream 타입으로 결정되었습니다.", resource.getFilename());
			
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);		
		
	}
	
}
