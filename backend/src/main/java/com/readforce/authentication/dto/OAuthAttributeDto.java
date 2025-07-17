package com.readforce.authentication.dto;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import com.readforce.common.MessageCode;
import com.readforce.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthAttributeDto {

	private Map<String, Object> attributeMap;
	private String nameAttributeKey;
	private String email;
	private String providerId;
	
	public static OAuthAttributeDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributeMap) {
		
		if("kakao".equals(registrationId)) {
			
			return ofKakao(userNameAttributeName, attributeMap);
			
		}
		
		if ("google".equals(registrationId)) {
			
	        return ofGoogle(userNameAttributeName, attributeMap);
	        
	    }
		
		throw new OAuth2AuthenticationException(MessageCode.UNSUPPORTED_SOCIAL_LOGIN);
		
	}
	
	private static OAuthAttributeDto ofKakao(String userNameAttributeName, Map<String, Object> attributeMap) {
		
		Map<String, Object> kakao_account = (Map<String, Object>)attributeMap.get("kakao_account");
		
		return OAuthAttributeDto.builder()
				.email((String)kakao_account.get("email"))
				.providerId(attributeMap.get(userNameAttributeName).toString())
				.attributeMap(attributeMap)
				.nameAttributeKey(userNameAttributeName)
				.build();
		
	}
	
	private static OAuthAttributeDto ofGoogle(String userNameAttributeName, Map<String, Object> attributeMap) {
		
		return OAuthAttributeDto.builder()
				.email((String)attributeMap.get("email"))
				.providerId(attributeMap.get(userNameAttributeName).toString())
				.attributeMap(attributeMap)
				.nameAttributeKey(userNameAttributeName)
				.build();
		
	}
	
	public Member toEntry(String nickname, LocalDate birthday) {
		
		return Member.builder()
				.nickname(nickname)
				.birthday(birthday)
				.email(this.email)
				.password(UUID.randomUUID().toString())
				.build();
		
	}
	
	
}
