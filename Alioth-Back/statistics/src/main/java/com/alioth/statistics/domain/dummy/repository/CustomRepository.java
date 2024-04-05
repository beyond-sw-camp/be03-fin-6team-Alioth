package com.alioth.statistics.domain.dummy.repository;

import com.alioth.statistics.domain.dummy.domain.Custom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRepository extends JpaRepository<Custom,Long> {

}
