package com.readforce.authentication.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Getter;

@Getter
public class OAuth2UserDto extends DefaultOAuth2User {

	private static final long serialVersionUID = 1L;
	private final String email;
	private final boolean isNewUser;
	private final String registrationId;

	public OAuth2UserDto(
			Collection<? extends GrantedAuthority> authorities, 
			Map<String, Object> attributeMap,
			String nameAttributeKey,
			String email,
			boolean isNewUser,
			String registrationId
	) {
		
		super(authorities, attributeMap, nameAttributeKey);
		this.email = email;
		this.isNewUser = isNewUser;
		this.registrationId = registrationId;
		
	}

	
	
	
}
