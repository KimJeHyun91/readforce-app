package com.readforce.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.common.enums.StatusEnum;
import com.readforce.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
	
	Optional<Member> findByEmailAndStatus(String email, StatusEnum status);

	Optional<Member> findBySocialProviderAndSocialId(String socialProvider, String socialId);

	Optional<Member> findByNickname(String nickname);

	List<Member> findAllByStatusAndWithdrawAtBefore(StatusEnum pendingDeletion, LocalDateTime thirtyDaysAgo);

}