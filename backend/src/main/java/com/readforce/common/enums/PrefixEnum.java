package com.readforce.common.enums;

public enum PrefixEnum {

	ROLE("ROLE_"),
	BEARER("Bearer "),
	SOCIAL_SIGN_UP("SocialSignUp_"),
	TEMPORAL("Temporal "),
	REFRESH("Refresh "),
	SOCIAL_LINK_STATE("SocialLinkState_"),
	IP_RATE_LIMIT("IpRateLimit:"),
	EMAIL_RATE_LIMIT("EmailRateLimit:"),
	CHALLENGE_LIMIT("ChallengeLimit_"),
	EMAIL_VERIFICATION("EmailVerification_"),
	SIGN_UP("SignUp_"),
	PASSWORD_RESET("PasswordReset_"),
	EMAIL_VERIFICATION_ATTEMPT("EmailVerificationAttempt_")
	;
	
	private final String prefix;
	
	PrefixEnum(String prefix){
		
		this.prefix = prefix;
		
	}
	
	public String getContent() {
		
		return prefix;
		
	}
	
}
