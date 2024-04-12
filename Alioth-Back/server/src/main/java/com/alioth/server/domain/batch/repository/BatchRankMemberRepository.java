package com.alioth.server.domain.batch.repository;

import com.alioth.server.domain.batch.BatchRankMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRankMemberRepository extends JpaRepository<BatchRankMember, Long> {
}
