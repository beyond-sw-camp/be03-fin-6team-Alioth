package com.alioth.statistics.domain.dummy.repository;

import com.alioth.statistics.domain.dummy.domain.ContractMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractMembersRepository extends JpaRepository<ContractMembers, Long> {
}
