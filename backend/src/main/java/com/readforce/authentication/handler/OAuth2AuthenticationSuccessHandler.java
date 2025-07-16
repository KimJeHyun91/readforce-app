package com.readforce.authentication.handler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readforce.authentication.dto.OAuth2UserDto;
import com.readforce.authentication.service.AuthenticationService;
import com.readforce.authentication.util.JwtUtil;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.NameEnum;
import com.readforce.common.enums.PrefixEnum;
import com.readforce.common.exception.JsonException;
import com.readforce.member.entity.Member;
import com.readforce.member.service.AttendanceService;
import com.readforce.member.service.MemberService;
import com.readforce.result.entity.Result;
import com.readforce.result.service.ResultMetricEventService;
import com.readforce.result.service.ResultService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final AuthenticationService authenticationService;
	private final MemberService memberService;
	private final AttendanceService attendanceService;
	private final StringRedisTemplate redisTemplate;
	private final ResultService resultService;
	private final ResultMetricEventService resultMetricEventService;
	
	@Value("${custom.fronted.social-login-success.exist-member-url}")
	private String socialLoginSuccessExistMemberUrl;
	
	@Value("${custom.fronted.social-login-success.new-member-url}")
	private String socialLoginSuccessNewMemberUrl;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest httpServletRequest, 
			HttpServletResponse httpServletResponse,
			Authentication authentication
	) throws IOException, ServletException {
				
		OAuth2UserDto oAuth2UserDto = (OAuth2UserDto) authentication.getPrincipal();
		
		boolean isNewUser = oAuth2UserDto.isNewUser();
		String email = oAuth2UserDto.getEmail();
		
		String existingRefreshToken = PrefixEnum.REFRESH.getContent() + email;
		
		if(redisTemplate.hasKey(existingRefreshToken)) {
			
			redisTemplate.delete(existingRefreshToken);
			
		}
		
		String targetUrl;
		
		if(isNewUser) {
				
			String temporalToken = UUID.randomUUID().toString();
			
			Map<String, String> socialInfo = Map.of(
					"email", email,
					"socialProvider", oAuth2UserDto.getRegistrationId(),
					"socialId", oAuth2UserDto.getName()					
			);
			
			try {
				
				String socialInfoJson = new ObjectMapper().writeValueAsString(socialInfo);
				
				redisTemplate.opsForValue().set(
						PrefixEnum.SOCIAL_SIGN_UP.getContent() + temporalToken,
						socialInfoJson,
						Duration.ofMinutes(10)
				);
				
			} catch(JsonProcessingException exception) {
				
				throw new JsonException(MessageCode.JSON_PROCESSING_FAIL);
				
			}
			
			targetUrl = UriComponentsBuilder.fromUriString(socialLoginSuccessNewMemberUrl)
					.queryParam(NameEnum.TEMPORAL_TOKEN.toString(), temporalToken)
					.build()
					.toUriString();
						
		} else {		
			
			final UserDetails userDetails = authenticationService.loadUserByUsername(email);
			
			final String accessToken = jwtUtil.generateAccessToken(userDetails);
			
			final String refreshToken = jwtUtil.generateRefreshToken(userDetails);
			
			Member member = memberService.getActiveMemberByEmail(email);
			
			Optional<Result> memberResult = resultService.getActiveMemberResultByEmailWithOptional(email);
			
			if(memberResult.isEmpty()) {
				
				Result newResult = resultService.create(member);
				
				resultMetricEventService.createResultMetricsForMember(newResult.getResultNo());
				
			}
			
			String temporalToken = UUID.randomUUID().toString();
			
			Map<String, String> tokenMap = Map.of(
					NameEnum.ACCESS_TOKEN.toString(), accessToken,
					NameEnum.REFRESH_TOKEN.toString(), refreshToken,
					NameEnum.NICKNAME.toString(), member.getNickname(),
					NameEnum.SOCIAL_PROVIDER.toString(), member.getSocialProvider()
			);
			
			redisTemplate.opsForValue().set(
					PrefixEnum.TEMPORAL.getContent() + temporalToken,
					new ObjectMapper().writeValueAsString(tokenMap),
					Duration.ofMinutes(3)
			);
						
			authenticationService.storeRefreshToken(email, refreshToken);
			
			attendanceService.recordAttendance(email);
			
			targetUrl = UriComponentsBuilder.fromUriString(socialLoginSuccessExistMemberUrl)
					.queryParam(NameEnum.TEMPORAL_TOKEN.toString(), temporalToken)
					.build()
					.toUriString();
			
			
		}

		httpServletResponse.sendRedirect(targetUrl);
			
	}
	
	
	
	
	
}
