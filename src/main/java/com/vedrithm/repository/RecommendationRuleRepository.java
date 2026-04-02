package com.vedrithm.repository;

import com.vedrithm.model.RecommendationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRuleRepository extends JpaRepository<RecommendationRule, Long> {
    Optional<RecommendationRule> findByConcernKeyAndIsActiveTrue(String concernKey);
    List<RecommendationRule> findByIsActiveTrueOrderByConcernKeyAsc();
}
