package com.servicehouse.energy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.service.FileInputService;

@RestController
public class FileInputController {

	@Autowired
	FileInputService fileInputService;


	@PostMapping(path = "/api/file/meters")
	public ResponseEntity<MeterReadingInfoDto> saveMeterReadings() {
		
		return ResponseEntity.ok(fileInputService.saveAllMeterReadings());
	}
	
	@PostMapping(path = "/api/file/fractions")
	public ResponseEntity<FractionInfoDto> saveFractions() {
		
		return ResponseEntity.ok(fileInputService.saveAllFractions());
	}
}
