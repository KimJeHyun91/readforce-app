package com.readforce.passage.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.question.entity.Question;

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
public class Passage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long passageNo;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;
	
	@Column(nullable = false)
	private String author;
	
	@Column(nullable = false)
	private LocalDate publicationDate;
	
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
	@JoinColumn(name = "type_no")
	private Type type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_no", nullable = false)
	private Level level;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_no", nullable = false)
	private Language language;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classification_no", nullable = false)
	private Classification classification;
	
	@Builder.Default
	@OneToMany(mappedBy = "passage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Question> questionList = new ArrayList<>();
	
	public void changeClassification(Classification classification) {
		
		this.classification = classification;
		
	}
	
}