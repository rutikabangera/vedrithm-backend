package com.vedrithm.repository;

import com.vedrithm.model.HomeBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeBenefitRepository extends JpaRepository<HomeBenefit, Long> {
    List<HomeBenefit> findByIsActiveTrueOrderByDisplayOrderAsc();
}
