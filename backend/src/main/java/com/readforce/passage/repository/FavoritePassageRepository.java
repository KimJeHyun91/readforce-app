package com.readforce.passage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.member.entity.Member;
import com.readforce.passage.entity.FavoritePassage;
import com.readforce.passage.entity.Passage;

@Repository
public interface FavoritePassageRepository extends JpaRepository<FavoritePassage, Long>{

	void deleteByMemberAndPassage(Member member, Passage passage);

	List<FavoritePassage> findByMember(Member member);

}
