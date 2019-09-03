package com.servicehouse.energy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.service.MeterReadingService;

@RestController
public class MeterReadingController {

	@Autowired
	MeterReadingService meterReadingService;

	@PostMapping(path = "/api/meter/readings")
	public ResponseEntity<MeterReadingInfoDto> saveMeterReadings(@RequestBody MeterReadingInfoDto meterReadingInfoRequest) {
		
		return ResponseEntity.ok(meterReadingService.saveAll(meterReadingInfoRequest));
	}
	
	@GetMapping(path = "/api/meter/consumption")
	public ResponseEntity<Double> getConsumption(@RequestBody Map<String, String> params) {
		String profile = params.get("profile");
		String month = params.get("month");
		
		return ResponseEntity.ok(meterReadingService.retrieveConsumption(profile, month));
	}

}
