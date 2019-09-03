package com.servicehouse.energy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servicehouse.energy.common.Constants;
import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.exceptions.MyResourceNotFoundException;
import com.servicehouse.energy.model.FractionInfo;
import com.servicehouse.energy.model.MeterReadingInfo;
import com.servicehouse.energy.model.MeterReadingInfoStatus;
import com.servicehouse.energy.repositories.MeterReadingRepository;

@Service
public class MeterReadingServiceImpl implements MeterReadingService {

	private static final Logger LOG = LoggerFactory.getLogger(FileInputServiceImpl.class);
	
	@Autowired
	MeterReadingRepository meterReadingRepository;
	
	@Autowired
	FractionService fractionService;

	@Override
	public MeterReadingInfoDto saveAll(MeterReadingInfoDto meterReadingInfoRequest) {
		List<MeterReadingInfo> list = meterReadingInfoRequest.getMeterReadingInfoList();
		markAsRejectedInvalidData(list);
		
		List<MeterReadingInfo> saveAll = meterReadingRepository.saveAll(list);
	
		return new MeterReadingInfoDto(saveAll);
	}
	
	private void markAsRejectedInvalidData(List<MeterReadingInfo> list) {
		meterReadingShouldGrowByProfile(list);
		fractionAndMeterReadingExistanceCheck(list);
		setConsumptions(list);
	}

	private void setConsumptions(List<MeterReadingInfo> list) {
		Map<String, Double> map = list.stream().filter(value-> value.getMonth().equals(Constants.LAST_MONTH))
											   .collect(Collectors.toMap(MeterReadingInfo::getProfile, MeterReadingInfo::getMeterReading));
		
		Map<String, Double> previousMap = new HashMap<>();
		for (MeterReadingInfo meterReadingInfo : list) {
			if(MeterReadingInfoStatus.REJECTED.equals(meterReadingInfo.getStatus())) {
				continue;
			}
			String month = meterReadingInfo.getMonth();
			String profile = meterReadingInfo.getProfile();
			if(!map.containsKey(profile)) {
				meterReadingInfo.setStatus(MeterReadingInfoStatus.REJECTED);
				continue;
			}
			double meterReadingAmount = meterReadingInfo.getMeterReading();
			double yearAmount = map.get(profile);
			
			FractionInfo fractionInfo = getFractionInfo(month, profile);
			double profileMonthFraction = fractionInfo.getFraction();
			
			double estimation = profileMonthFraction*yearAmount;
			double previousAmount = 0;
			if(previousMap.containsKey(profile)) {
				previousAmount = previousMap.get(profile);
			}
			double consumption = meterReadingAmount - previousAmount;
			if(consumption< estimation*3/4 || consumption> estimation*5/4) {
				LOG.error(meterReadingInfo.getMeterId()+" REJECTED : Consumption is not in estimation range"
						+"Consumption :" + consumption+ "Estimation range: "+estimation*3/4 +"-"+ estimation*5/4);
				
				meterReadingInfo.setStatus(MeterReadingInfoStatus.REJECTED);
				continue;
			}
			meterReadingInfo.setConsumption(consumption);
			previousMap.put(profile, meterReadingAmount);
		}
	}

	private FractionInfo getFractionInfo(String month, String profile) {
		Optional<FractionInfo> fractionalInfoOpt = fractionService.findByProfileAndMonth(profile, month);
		if(!fractionalInfoOpt.isPresent()) {
			throw new MyResourceNotFoundException();
		}
		FractionInfo fractionInfo = fractionalInfoOpt.get();
		
		return fractionInfo;
	}

	private void fractionAndMeterReadingExistanceCheck(List<MeterReadingInfo> list) {
		for (MeterReadingInfo meterReadingInfo : list) {
			if(MeterReadingInfoStatus.REJECTED.equals(meterReadingInfo.getStatus())) {
				continue;
			}
			String profile = meterReadingInfo.getProfile();
			String month = meterReadingInfo.getMonth();
			boolean fractionExists = fractionService.isExists(profile, month);
			if(!fractionExists) {
				LOG.error(meterReadingInfo.getMeterId()+" REJECTED : This profile and month fraction is not exists!"+ " Profile: "+meterReadingInfo.getProfile()+
																			" Month: "+ meterReadingInfo.getMonth());
				
				meterReadingInfo.setStatus(MeterReadingInfoStatus.REJECTED);
			}
			boolean meterExists = isProfileAndMonthExists(profile, month);
			if(meterExists) {
				LOG.error(meterReadingInfo.getMeterId()+" REJECTED : This profile and month meterReading is already exists!"+ " Profile: "+meterReadingInfo.getProfile()+
						" Month: "+ meterReadingInfo.getMonth());


				meterReadingInfo.setStatus(MeterReadingInfoStatus.REJECTED);
			}
		}
	}

	private void meterReadingShouldGrowByProfile(List<MeterReadingInfo> list) {
		Map<String, List<Double>> map = new HashMap<>();
		for (MeterReadingInfo meterReadingInfo : list) {
			String profileKey = meterReadingInfo.getProfile();
			double meterReadingAmount = meterReadingInfo.getMeterReading();
			if(map.containsKey(profileKey)) {
				List<Double> subList = map.get(profileKey);
				Optional<Double> invalidAmountOptional = subList.stream()
																.filter(element-> meterReadingAmount < element)
																.findAny();
				if(invalidAmountOptional.isPresent()) {
					LOG.error(meterReadingInfo.getMeterId()+" REJECTED: meterReadingAmount can t be smaller than previous amounts!");
					meterReadingInfo.setStatus(MeterReadingInfoStatus.REJECTED);
				}
				subList.add(meterReadingAmount);
				map.put(profileKey,subList);
			}
			else {
				List<Double> meterAmountList = new ArrayList<>();
				meterAmountList.add(meterReadingAmount);
				map.put(profileKey,meterAmountList);
			}
		}
		
	}

	public boolean isMeterIdExists(String meterId) {
		Optional<MeterReadingInfo> optionalMeterReading = meterReadingRepository.findByMeterId(meterId);

		return optionalMeterReading.isPresent();
	}
	public Optional<MeterReadingInfo> getByProfileAndMonth(String profile,String month) {
		return  meterReadingRepository.findByProfileAndMonthAndStatus(profile, month, MeterReadingInfoStatus.ACCEPTED);
	}

	public Iterable<MeterReadingInfo> findAll() {
		return meterReadingRepository.findAll();
	}
	
	public MeterReadingInfo save(MeterReadingInfo p) {
		return meterReadingRepository.save(p);
	}

	@Override
	public Optional<MeterReadingInfo> findById(Long id) {
		return meterReadingRepository.findById(id);
	}

	@Override
	public Double retrieveConsumption(String profile, String month) {
		Optional<MeterReadingInfo> optionalMeter = getByProfileAndMonth(profile, month);
		if (!optionalMeter.isPresent()) {
			throw new MyResourceNotFoundException();
		}
		return optionalMeter.get().getConsumption();
	}
	
	public boolean isProfileAndMonthExists(String profile, String month) {
		Optional<MeterReadingInfo> optionalMeter = getByProfileAndMonth(profile, month);
		
		return optionalMeter.isPresent();
	}

}
