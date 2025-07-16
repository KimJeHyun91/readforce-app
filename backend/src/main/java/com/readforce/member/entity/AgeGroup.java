package com.readforce.member.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.question.entity.AverageQuestionSolvingTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class AgeGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ageGroupNo;
	
	@Column(nullable = false)
	private Integer ageGroup;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt;
	
	@Builder.Default
	@OneToMany(mappedBy = "ageGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AverageQuestionSolvingTime> averageQuestionSolvingTimeList = new ArrayList<>();
	
}
