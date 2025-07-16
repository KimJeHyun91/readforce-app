package com.readforce.question.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.member.entity.AgeGroup;
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
public class AverageQuestionSolvingTime {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long averageQuestionSolvingTimeNo;
	
	@Column(nullable = false)
	private Long averageQuestionSolvingTime;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "age_group_no", nullable = false)
	private AgeGroup ageGroup;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_no", nullable = false)
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_no", nullable = false)
	private Type type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_no", nullable = false)
	private Level level;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_no", nullable = false)
	private Language language;

}
