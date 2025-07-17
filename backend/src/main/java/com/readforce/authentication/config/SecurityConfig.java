package com.readforce.authentication.config;

import java.util.Arrays;
import java.util.Collections;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    // 👇 1. CORS 설정을 위한 Bean을 새로 등록합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ⚠️ 실제 프론트엔드 배포 주소를 여기에 추가해야 합니다.
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://readforce-frontend-production.up.railway.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }
    
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
    
    @Bean 
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        
        httpSecurity
                // 👇 2. .cors(withDefaults()) 대신, 위에서 만든 Bean을 사용하도록 변경합니다.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(
                                "/",
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
                                "/passage/**"
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
