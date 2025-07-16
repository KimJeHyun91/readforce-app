package com.readforce.result.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.member.entity.Member;
import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scoreNo;
	
	@Builder.Default
	@Column(nullable = false)
	private Double score = 0.0;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_no", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_no", nullable = false)
	private Member member;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_no", nullable = false)
	private Language language;
	
	public void modifyInfo(
			Member member, 
			Double score, 
			Category category,
			Language language
	) {
		
		if(member != null) {
			
			this.member = member;
			
		}
		
		if(score != null) {
			
			this.score = score;
			
		}
		
		if(category != null) {
			
			this.category = category;
			
		}
		
		if(language != null) {
			
			this.language = language;
			
		}
		
	}
	
	public void updateScoreForChallenge(Double score) {
		
		this.score += score;
		
	}
	
	
	
}