/**
 * 
 */
package com.servicehouse.energy.service;

import java.util.Optional;

import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.model.FractionInfo;


public interface FractionService {

	FractionInfoDto saveAll(FractionInfoDto fractionInputRequest);

	FractionInfo save(FractionInfo p);

	Optional<FractionInfo> findById(Long bookId);

	Optional<FractionInfo> findByProfileAndMonth(String profile, String month);

	boolean isExists(String profile, String month);
}
