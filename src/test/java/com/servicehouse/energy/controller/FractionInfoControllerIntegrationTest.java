package com.servicehouse.energy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.model.FractionInfo;
import com.servicehouse.energy.repositories.MeterReadingRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FractionInfoControllerIntegrationTest {
	
	private static String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

	@Autowired
	private TestRestTemplate template;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MeterReadingRepository meterReadingRepository;


	@Test
	public void saveFractions_givenFullListAndFractionSumIsOne_Successfull() throws Exception {
		List<FractionInfo> list = new ArrayList<>();
		UUID uuid1 = UUID.randomUUID();
        String profile1UUIDString = uuid1.toString();
        UUID uuid2 = UUID.randomUUID();
        String profile2UUIDString = uuid2.toString();
		for (int i = 0; i < 12; i++) {
				double fraction = 0.1;
				if(i>7) {
					fraction=0.05;
				}
				FractionInfo info1 = new FractionInfo(months[i], profile1UUIDString, fraction);		
				FractionInfo info2 = new FractionInfo(months[i], profile2UUIDString, fraction);		
				
				list.add(info1);
				list.add(info2);
		}
		FractionInfoDto request = new FractionInfoDto();
		request.setFractionList(list);
		String jSon = objectMapper.writeValueAsString(request);
		HttpEntity<Object> memberEntity = getHttpEntity(jSon);
		ResponseEntity<FractionInfoDto> response =
				template.postForEntity("/api/fractions", memberEntity, FractionInfoDto.class);

		
		Assert.assertEquals(200, response.getStatusCode().value());
		Assert.assertEquals(24, response.getBody().getFractionList().size());
	}
	
	@Test
	public void saveFractions_givenFullListButFractionSumIsNotOne_Successfull() throws Exception {
		List<FractionInfo> list = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 2; j++) {
				double fraction = 0.1;
				FractionInfo info = new FractionInfo(months[i], j+"", fraction);		
				list.add(info);
			}
			
		}
		FractionInfoDto request = new FractionInfoDto();
		request.setFractionList(list);
		String jSon = objectMapper.writeValueAsString(request);
		HttpEntity<Object> memberEntity = getHttpEntity(jSon);
		ResponseEntity<FractionInfoDto> response =
				template.postForEntity("/api/fractions", memberEntity, FractionInfoDto.class);

		
		Assert.assertEquals(403, response.getStatusCode().value());
	}
	
	@Test
	public void saveFractions_givenNotFullList_Forbidden() throws Exception {
		List<FractionInfo> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 1; j++) {
				double fraction = 0.1;
				if(i>7) {
					fraction=0.05;
				}
				FractionInfo info = new FractionInfo(months[i], j+"", fraction);		
				list.add(info);
			}
			
		}
		FractionInfoDto request = new FractionInfoDto();
		request.setFractionList(list);
		String jSon = objectMapper.writeValueAsString(request);
		HttpEntity<Object> memberEntity = getHttpEntity(jSon);
		 {}
		ResponseEntity<FractionInfoDto> response =
				template.postForEntity("/api/fractions", memberEntity, FractionInfoDto.class);

		Assert.assertEquals(403, response.getStatusCode().value());
	}


	private HttpEntity<Object> getHttpEntity(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}

}
