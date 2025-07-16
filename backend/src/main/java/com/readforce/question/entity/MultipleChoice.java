package com.readforce.question.entity;

import java.util.ArrayList;
import java.util.List;

import com.readforce.passage.entity.Passage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoice extends Question {

	@Column(name = "question", nullable = false, columnDefinition = "TEXT")
	private String question;

	@OneToMany(mappedBy = "multipleChoice", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Choice> choiceList = new ArrayList<>();
	
	@Builder
	public MultipleChoice(Passage passage, String question, List<Choice> choiceList) {
		
		super(passage);
		this.question = question;
		if(choiceList != null) {
			for(Choice choice : choiceList) {
				this.addChoice(choice);
			}
		}
		
	}
	
	private void addChoice(Choice choice) {
		
		this.choiceList.add(choice);
		choice.setMultipleChoice(this);
	
	}
	
	public List<Choice> getChoiceList(){
		
		return java.util.Collections.unmodifiableList(this.choiceList);
		
	}
	
}
