package com.servicehouse.energy.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.model.MeterReadingInfo;
import com.servicehouse.energy.model.MeterReadingInfoStatus;
import com.servicehouse.energy.repositories.MeterReadingRepository;

@RunWith(MockitoJUnitRunner.class)
public class MeterReadingServiceMock {
	@InjectMocks
	MeterReadingServiceImpl meterReadingServiceImpl;
	@Mock
	MeterReadingRepository meterReadingRepository;
	
	@Mock
	FractionService fractionService;

	@SuppressWarnings("deprecation")
	@Test
	public void saveAll_GivenMissedList_SavedRejected() {
		List<MeterReadingInfo> meterList = new ArrayList<>();
		MeterReadingInfo meter1 = new MeterReadingInfo("001","JAN","A",15);
		MeterReadingInfo meter2 = new MeterReadingInfo("001","JAN","B",25);
		meterList.add(meter1);
		meterList.add(meter2);
		
		when(fractionService.isExists(anyString(), anyString())).thenReturn(true);
		
		when(meterReadingRepository.findByProfileAndMonthAndStatus(
				anyString(), anyString(),(MeterReadingInfoStatus)anyObject()))
						.thenReturn(Optional.empty());
		
		when(meterReadingRepository.saveAll(meterList)).thenReturn(meterList);
		
		MeterReadingInfoDto dto = new MeterReadingInfoDto(meterList);
		
		MeterReadingInfoDto dtoReturn = meterReadingServiceImpl.saveAll(dto);
		
		Assert.assertNotNull(dtoReturn);
		Assert.assertEquals(2, dtoReturn.getMeterReadingInfoList().size());
	}
}
