package com.servicehouse.energy.dto;

import java.util.ArrayList;
import java.util.List;

import com.servicehouse.energy.model.MeterReadingInfo;

public class MeterReadingInfoDto {
	private List<MeterReadingInfo> meterReadingInfoList;

	public MeterReadingInfoDto(List<MeterReadingInfo> meterReadingInfoList) {
		this.meterReadingInfoList = meterReadingInfoList;
	}

	public MeterReadingInfoDto() {
	}

	public List<MeterReadingInfo> getMeterReadingInfoList() {
		return new ArrayList<MeterReadingInfo>(this.meterReadingInfoList);
	}

	public void setMeterReadingInfoList(List<MeterReadingInfo> meterReadingInfoList) {
		this.meterReadingInfoList = meterReadingInfoList;
	}
}
