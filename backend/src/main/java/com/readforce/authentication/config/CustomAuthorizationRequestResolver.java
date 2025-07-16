package com.readforce.authentication.config;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final OAuth2AuthorizationRequestResolver defaultResolver;
	
	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository, String authorizationRequestBaseUrl) {
		
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, authorizationRequestBaseUrl);
		
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest httpServletRequest) {

		OAuth2AuthorizationRequest oAuth2AuthorizationRequest = defaultResolver.resolve(httpServletRequest);
		
		if(oAuth2AuthorizationRequest != null) {
			
			oAuth2AuthorizationRequest = customizeAuthorizationRequest(oAuth2AuthorizationRequest, httpServletRequest);
			
		}
		
		return oAuth2AuthorizationRequest;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest httpServletRequest, String clientRegistrationId) {

		OAuth2AuthorizationRequest oAuth2AuthorizationRequest = defaultResolver.resolve(httpServletRequest, clientRegistrationId);
		
		if(oAuth2AuthorizationRequest != null) {
			
			oAuth2AuthorizationRequest = customizeAuthorizationRequest(oAuth2AuthorizationRequest, httpServletRequest);
			
		}
		
		return oAuth2AuthorizationRequest;

	}
	
	private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest oAuth2AuthorizationRequest, HttpServletRequest httpServletRequest) {
		
		String state = httpServletRequest.getParameter("state");
		
		if(state != null) {
			
			return OAuth2AuthorizationRequest
					.from(oAuth2AuthorizationRequest)
					.state(state)
					.build();
			
		}
		
		return oAuth2AuthorizationRequest;
		
	}

	
	
}
