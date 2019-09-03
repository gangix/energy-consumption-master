/**
 * 
 */
package com.servicehouse.energy.service;

import java.util.Optional;

import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.model.MeterReadingInfo;

public interface MeterReadingService {

	public MeterReadingInfoDto saveAll(MeterReadingInfoDto meterReadingInfoRequest);

	public MeterReadingInfo save(MeterReadingInfo p);

	public Optional<MeterReadingInfo> findById(Long bookId);
	
	public Double retrieveConsumption(String profile,String month);

}
