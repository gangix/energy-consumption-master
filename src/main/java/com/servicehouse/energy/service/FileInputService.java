package com.servicehouse.energy.service;

import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.dto.MeterReadingInfoDto;

public interface FileInputService {

	public MeterReadingInfoDto saveAllMeterReadings();
	public FractionInfoDto saveAllFractions();
}
