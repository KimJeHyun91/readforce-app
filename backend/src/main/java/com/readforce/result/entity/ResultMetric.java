package com.readforce.result.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.passage.entity.Category;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Type;

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
public class ResultMetric {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resultMetricNo;
	
	@Builder.Default
	@Column(nullable = false)
	private Double correctAnswerRate = 0.0;
	
	@Builder.Default
	@Column(nullable = false)
	private Long questionSolvingTimeAverage = 0L;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "result_no", nullable = false)
	private Result result;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_no")
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_no")
	private Type type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_no")
	private Level level;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_no")
	private Language language;
	
	public void updateMetric(Double correctAnswerRate, Long questionSolvingTimeAverage) {
		
		this.correctAnswerRate = correctAnswerRate;
		this.questionSolvingTimeAverage = questionSolvingTimeAverage;
		
	}
	
}
