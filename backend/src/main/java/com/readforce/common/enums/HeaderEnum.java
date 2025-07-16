package com.readforce.common.enums;

public enum HeaderEnum {

	X_FORWARDED_FOR("X-Forwarded-For"),
	AUTHORIZATION("Authorization")
	;
	
	private final String header;
	
	HeaderEnum(String header){
		
		this.header = header;
		
	}
	
	public String getContent() {
		
		return header;
		
	}
	
}
