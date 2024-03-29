package com.alioth.server.domain.contract.repository;

import com.alioth.server.domain.contract.domain.Renewal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenewalRepository extends JpaRepository<Renewal, Long> {
}
