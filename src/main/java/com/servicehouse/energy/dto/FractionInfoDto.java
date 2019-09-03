package com.servicehouse.energy.dto;

import java.util.ArrayList;
import java.util.List;

import com.servicehouse.energy.model.FractionInfo;

public class FractionInfoDto {
	private List<FractionInfo> fractionList;

	public FractionInfoDto(List<FractionInfo> fractionList) {
		this.fractionList = fractionList;
	}

	public FractionInfoDto() {
	}

	public List<FractionInfo> getFractionList() {
		return new ArrayList<FractionInfo>(this.fractionList);
	}

	public void setFractionList(List<FractionInfo> fractionList) {
		this.fractionList = fractionList;
	}
}
