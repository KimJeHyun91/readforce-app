package com.readforce.authentication.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.readforce.authentication.dto.OAuth2UserDto;
import com.readforce.authentication.dto.OAuthAttributeDto;
import com.readforce.common.MessageCode;
import com.readforce.common.enums.PrefixEnum;
import com.readforce.common.enums.RoleEnum;
import com.readforce.common.exception.DuplicationException;
import com.readforce.member.entity.Member;
import com.readforce.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private final StringRedisTemplate redisTemplate;
	private final MemberService memberService;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuthAttributeDto oAuthAttributeDto = OAuthAttributeDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		
		String socialProvider = registrationId;
		String socialId = oAuthAttributeDto.getProviderId();
		String socialEmail = oAuthAttributeDto.getEmail();
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		
		if(servletRequestAttributes == null) {
			
			throw new OAuth2AuthenticationException(MessageCode.REQUEST_ATTRIBUTES_IS_NULL_AND_CANNOT_CHECK_STATE);
			
		}
		
		HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
		
		String state = httpServletRequest.getParameter("state");
		
		if(state != null) {
			
			log.warn(state);
			
			String email = redisTemplate.opsForValue().get(PrefixEnum.SOCIAL_LINK_STATE.getContent() + state);
			
			if(email != null) {
				
				redisTemplate.delete(PrefixEnum.SOCIAL_LINK_STATE.getContent() + state);
				
				try {
					
					memberService.linkSocialAccount(email, socialProvider, socialId, socialEmail);
					
				} catch(DuplicationException exception) {
					
					throw new OAuth2AuthenticationException(exception.getMessage());
					
				}
				
				Member linkedMember = memberService.getActiveMemberByEmail(email);
				
				return createOAuth2UserDto(linkedMember, oAuthAttributeDto, false, registrationId);
				
			}
			
		}
		
		Optional<Member> optionalMember = memberService.getActiveMemberBySocialInfo(socialProvider, socialId);
		
		Member member;
		boolean isNewUser;
		
		if(optionalMember.isPresent()) {
			
			member = optionalMember.get();
			isNewUser = false;
			
		} else {
			
			Optional<Member> optionalMemberByEmail = memberService.getActiveMemberWithOptional(socialEmail);
			
			if(optionalMemberByEmail.isPresent()) {
				
				member = optionalMemberByEmail.get();
				member.changeSocialInfo(socialProvider, socialId);
				
				memberService.saveMember(member);
				
				isNewUser = false;
				
			} else {
				
				member = null;
				
				isNewUser = true;
				
			}
			
		}
		
		return createOAuth2UserDto(member, oAuthAttributeDto, isNewUser, registrationId);
		
	}
	
	private OAuth2UserDto createOAuth2UserDto(Member member, OAuthAttributeDto oAuthAttributeDto, boolean isNewUser, String registrationId) {
		
		String primaryEmail;
		RoleEnum role;
		
		if(isNewUser) {
			
			primaryEmail = oAuthAttributeDto.getEmail();
			role = RoleEnum.USER;			
			
		} else {
			
			primaryEmail = member.getEmail();
			role = member.getRole();
			
		}
		
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(PrefixEnum.ROLE.getContent() + role.name());
		
		return new OAuth2UserDto(
				Collections.singleton(grantedAuthority),
				oAuthAttributeDto.getAttributeMap(),
				oAuthAttributeDto.getNameAttributeKey(),
				primaryEmail,
				isNewUser,
				registrationId
		);

	}
	
}
