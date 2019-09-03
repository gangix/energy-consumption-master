package com.servicehouse.energy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicehouse.energy.model.MeterReadingInfo;
import com.servicehouse.energy.model.MeterReadingInfoStatus;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReadingInfo, Long> {

	Optional<MeterReadingInfo> findByMeterId(String meterId);
	Optional<MeterReadingInfo> findByProfileAndMonthAndStatus(String profile, String month, MeterReadingInfoStatus status);

}
