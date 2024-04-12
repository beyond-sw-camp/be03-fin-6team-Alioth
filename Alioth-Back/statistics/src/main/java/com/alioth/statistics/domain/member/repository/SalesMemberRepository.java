package com.alioth.statistics.domain.member.repository;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Repository
public interface SalesMemberRepository extends JpaRepository<SalesMembers, Long> {

    SalesMembers findFirstByOrderByIdDesc();

    List<SalesMembers> findAllByTeamId(Long teamId);

    Optional<SalesMembers> findBySalesMemberCode(Long salesMemberCode);


    @Query(value = "SELECT m.sales_member_code, m.name, m.performance_review, COUNT(m.name) as totalcount, SUM(c.contract_total_price) as totalprice \n" +
            "                FROM contract as c \n" +
            "                    join sales_members as m \n" +
            "                    on c.sm_id = m.id \n" +
            "                GROUP BY m.sales_member_code, m.performance_review \n" +
            "                ORDER BY m.performance_review, SUM(c.contract_total_price) DESC, COUNT(m.name) DESC", nativeQuery = true)
    List<Map<String, Object>> memberPerformanceReviewRank();

}
