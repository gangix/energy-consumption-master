package com.servicehouse.energy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicehouse.energy.model.FractionInfo;

@Repository
public interface FractionRepository extends JpaRepository<FractionInfo, Long> {
	Optional<FractionInfo> findByProfileAndMonth(String profile, String month);
}
