package com.servicehouse.energy.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.model.MeterReadingInfo;
import com.servicehouse.energy.model.MeterReadingInfoStatus;
import com.servicehouse.energy.repositories.MeterReadingRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FileInputControllerIntegrationTest {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	MeterReadingRepository meterReadingRepository;

	@Test
	public void saveFractions_givenNotFullList_Forbidden() throws Exception {
		ResponseEntity<FractionInfoDto> response 
		= template.postForEntity("/api/file/fractions", null, FractionInfoDto.class);

		Assert.assertEquals(403, response.getStatusCode().value());
	}
	
	@Test
	public void saveMeters_givenNotFullList_Rejected() throws Exception {
		ResponseEntity<MeterReadingInfoDto> response 
		= template.postForEntity("/api/file/meters", null, MeterReadingInfoDto.class);

		Assert.assertEquals(200, response.getStatusCode().value());
		
		List<MeterReadingInfo> meterReadingInfoList = response.getBody().getMeterReadingInfoList();
		Assert.assertEquals(5, meterReadingInfoList.size());
		Assert.assertEquals(false, meterReadingInfoList.stream().filter(value-> value.getStatus()
								.equals(MeterReadingInfoStatus.ACCEPTED)).findAny().isPresent());
		
		
	}

}
