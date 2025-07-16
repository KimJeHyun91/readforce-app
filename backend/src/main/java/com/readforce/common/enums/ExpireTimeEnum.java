package com.readforce.common.enums;

public enum ExpireTimeEnum {
	
	DEFAULT(3),
	SOCIAL_ACCOUNT_LINK(5),
	MARK_EMAIL_AS_VERIFIED(10)
	;
	
	private final long expireTime;
	
	ExpireTimeEnum(long expireTime){
		
		this.expireTime = expireTime;
		
	}
	
	public long getTime() {
		
		return expireTime;
		
	}

}
