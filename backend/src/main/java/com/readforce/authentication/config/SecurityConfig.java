package com.readforce.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.readforce.authentication.filter.JwtRequestFilter;
import com.readforce.authentication.handler.OAuth2AuthenticationSuccessHandler;
import com.readforce.authentication.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtRequestFilter jwtRequestFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final ClientRegistrationRepository clientRegistrationRepository;
	
	@Bean
	public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
		
		return new CustomAuthorizationRequestResolver(
				this.clientRegistrationRepository, "/oauth2/authorization"
		);
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();		
		
	}
	
	@Bean SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
			.authorizeHttpRequests(
					auth -> auth.requestMatchers(
                                                "/**",
                                                "/authentication/sign-in",
                                                "/authentication/reissue-refresh-token",
                                                "/authentication/get-tokens",
				            "/member/sign-up",
				            "/member/social-sign-up",
				            "/member/email-check",
				            "/member/nickname-check",
				            "/member/password-reset-from-link",
				            "/email/send-verification-code-for-sign-up",
				            "/email/verify-verification-code-for-sign-up",
				            "/email/send-password-reset-link",
				            "/ranking/get-ranking-list",
				            "/learning/get-most-incorrect-passages",
				            "/test/**",
				            "/oauth2/**",
				            "/passage/**",
                                            "/administrator/**",
                                            "/authentication/**",
                                            "/challenge/**",
                                            "/email/**",
                                            "/file/**",
                                            "/member/**",
                                            "/attendance/**",
                                            "/question/**",
                                            "/multiple_choice/**",
                                            "/recommend/**",
                                            "/learning/**",
                                            "/result/**",
                                            "/result_metric/**"
					)
					.permitAll()
					.requestMatchers(
							"/administrator/**",
							"/ai/**"							
					).hasRole("ADMIN")
					.anyRequest()
					.authenticated()
			
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.oauth2Login(oauth2 -> oauth2
					.authorizationEndpoint(auth -> auth.authorizationRequestResolver(customAuthorizationRequestResolver()))
					.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
					.successHandler(oAuth2AuthenticationSuccessHandler)
			);
		
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		return httpSecurity.build();		
		
	}
		
}
