package com.readforce.authentication.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.readforce.authentication.exception.JwtException;
import com.readforce.common.MessageCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	@Value("${spring.jwt.secret}")
	private String secret;
	
	@Value("${spring.jwt.access-expiration-time}")
	private long accessExpirationTime;
	
	@Value("${spring.jwt.refresh-expiration-time}")
	private long refreshExpirationTime;
	
	@PostConstruct
	public void validateKeyLength() {
		
		log.info("JWT Secret Key를 검증중입니다...");
		
		byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
		
		final int minKeyLengthBytes = 32;
		
		if(keyBytes.length < minKeyLengthBytes) {
			
			log.error("JWT Secret Key가 너무 짧습니다. key는 반드시 최소 {} bytes(256 bits) 길이어야 합니다.", minKeyLengthBytes);
			
			throw new JwtException(MessageCode.JWT_SECRET_KEY_INVALID);
			
		}
		
		log.info("JWT Secret Key 검증에 완료했습니다.");
		
	}
	
	private Key getSigningKey() {
		
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
		
	}
	
	public String extractUsername(String token) {
		
		return extractClaim(token, Claims::getSubject);
		
	}
	
	public Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
		
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
		
	}
	
	public Claims extractAllClaims(String token) {
		
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		
	}
	
	public String generateAccessToken(UserDetails userDetails) {
		
		Map<String, Object> claimMap = new HashMap<>();
		List<String> roleList = userDetails
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		claimMap.put("roleList", roleList);
		return createToken(claimMap, userDetails.getUsername(), accessExpirationTime);
		
	}
	
	public String generateRefreshToken(UserDetails userDetails) {
		
		return createToken(new HashMap<>(), userDetails.getUsername(), refreshExpirationTime);
		
	}
	
	private String createToken(Map<String, Object> claimMap, String subject, long expirationTime) {
		
		return Jwts
				.builder()
				.setClaims(claimMap)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
		
	}
	
	public Boolean isExpiredToken(String token) {
		
		return extractExpiration(token).before(new Date());
		
	}
	
	public Boolean validateToken(String accessToken, UserDetails userDetails) {
		
		final String username = extractUsername(accessToken);
		
		return username.equals(userDetails.getUsername());
		
	}
	
}
