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
import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.model.MeterReadingInfo;
import com.servicehouse.energy.model.MeterReadingInfoStatus;
import com.servicehouse.energy.repositories.MeterReadingRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MeterReadingInfoControllerIntegrationTest {

	private static String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
			"DEC" };

	@Autowired
	private TestRestTemplate template;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MeterReadingRepository meterReadingRepository;

	@Test
	public void saveMeterReadings_GivenFullListWithExistingFractions_Successfull() throws Exception {
		UUID uuid1 = UUID.randomUUID();
		String profile1UUIDString = uuid1.toString();
		UUID uuid2 = UUID.randomUUID();
		String profile2UUIDString = uuid2.toString();
		List<MeterReadingInfo> list = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			MeterReadingInfo info1 =
					new MeterReadingInfo(profile1UUIDString, months[i], profile1UUIDString,	10 + i + 5);
			MeterReadingInfo info2 = 
					new MeterReadingInfo(profile2UUIDString, months[i], profile2UUIDString, 10 + i + 5);

			list.add(info1);
			list.add(info2);
		}
		MeterReadingInfoDto request = new MeterReadingInfoDto();
		request.setMeterReadingInfoList(list);
		
		String jSon = objectMapper.writeValueAsString(request);
		HttpEntity<Object> requestEntity = getHttpEntity(jSon);
		ResponseEntity<MeterReadingInfoDto> response = template.postForEntity("/api/meter/readings", requestEntity,
				MeterReadingInfoDto.class);

		Assert.assertEquals(200, response.getStatusCode().value());

		List<MeterReadingInfo> meterReadingInfoList = response.getBody().getMeterReadingInfoList();
		Assert.assertEquals(24, meterReadingInfoList.size());
	}

	@Test
	public void saveMeterReadings_GivenFullListWithNotExistingFractions_Rejected() throws Exception {
		List<MeterReadingInfo> list = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			for (int j = 2; j < 4; j++) {
				MeterReadingInfo info = new MeterReadingInfo(j + "", months[i], j + "", 10 + i + 5);
				list.add(info);
			}

		}
		MeterReadingInfoDto request = new MeterReadingInfoDto();
		request.setMeterReadingInfoList(list);
		String jSon = objectMapper.writeValueAsString(request);
		HttpEntity<Object> requestEntity = getHttpEntity(jSon);
		ResponseEntity<MeterReadingInfoDto> response = template.postForEntity("/api/meter/readings", requestEntity,
				MeterReadingInfoDto.class);

		Assert.assertEquals(200, response.getStatusCode().value());

		List<MeterReadingInfo> meterReadingInfoList = response.getBody().getMeterReadingInfoList();
		Assert.assertEquals(false, meterReadingInfoList.stream()
				.filter(value -> value.getStatus().equals(MeterReadingInfoStatus.ACCEPTED)).findAny().isPresent());
	}

	@Test
	public void saveMeterReadings_GivenListWithAlreadyExistingMeters_Rejected() throws Exception {
		List<MeterReadingInfo> list = new ArrayList<>();
		MeterReadingInfo info = new MeterReadingInfo("1", months[11], "1", 0.3);
		list.add(info);
		MeterReadingInfoDto request = new MeterReadingInfoDto();
		request.setMeterReadingInfoList(list);

		String jSon = objectMapper.writeValueAsString(request);
		HttpEntity<Object> requestEntity = getHttpEntity(jSon);
		ResponseEntity<MeterReadingInfoDto> response = template.postForEntity("/api/meter/readings", requestEntity,
				MeterReadingInfoDto.class);
	
		Assert.assertEquals(200, response.getStatusCode().value());

		List<MeterReadingInfo> meterReadingInfoList = response.getBody().getMeterReadingInfoList();
		Assert.assertEquals(1, meterReadingInfoList.size());
		Assert.assertTrue(meterReadingInfoList.get(0).getStatus().equals(MeterReadingInfoStatus.REJECTED));
	}

	private HttpEntity<Object> getHttpEntity(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}

}
