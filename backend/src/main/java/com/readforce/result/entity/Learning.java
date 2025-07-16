package com.readforce.result.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.member.entity.Member;
import com.readforce.question.entity.Question;

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
public class Learning {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long learningNo;
	
	@Builder.Default
	@Column(nullable = false)
	private Boolean isFavorit = false;
	
	@Column(nullable = false)
	private Boolean isCorrect;
	
	@Builder.Default
	@Column(nullable = false)
	private Long questionSolvingTime = 0L;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_no", nullable = false)
	private Question question;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_no", nullable = false)
	private Member member;
	
	public void changeFavoritState(Boolean isFavorit) {
		
		this.isFavorit = isFavorit;
		
	}

}
