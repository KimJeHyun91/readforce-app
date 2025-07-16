package com.readforce.result.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resultNo;
	
	@Builder.Default
	@Column(nullable = false)
	private Integer learningStreak = 0;
	
	@Builder.Default
	@Column(nullable = false)
	private Double overallCorrectAnswerRate = 0.0;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_no", nullable = false)
	private Member member;
	
	@Builder.Default
	@OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ResultMetric> resultMetricList = new ArrayList<>();

	public void updateLearningStreak(boolean attendedYesterday) {
		
		if(attendedYesterday) {
			
			this.learningStreak++;
			
		} else {
			
			this.learningStreak = 1;
			
		}
		
	}
	
	public void resetLearningStreak() {
		
		this.learningStreak = 0;
		
	}
	
	
	public void updateOverallCorrectAnswerRate(Double overallCorrectAnswerRate) {
		
		this.overallCorrectAnswerRate = overallCorrectAnswerRate;
		
	}
	
	public void modifyInformation(Integer learningStreak, Double overallCorrectAnswerRate) {
		
		if(learningStreak != null) {
			
			this.learningStreak = learningStreak;
			
		}
		
		if(overallCorrectAnswerRate != null) {
			
			this.overallCorrectAnswerRate = overallCorrectAnswerRate;
			
		}
		
		
	}
	
}
