package com.servicehouse.energy.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.exceptions.MyForbiddenException;
import com.servicehouse.energy.model.FractionInfo;
import com.servicehouse.energy.repositories.FractionRepository;

@Service
public class FractionServiceImpl implements FractionService {

	private static final Logger LOG = LoggerFactory.getLogger(FractionServiceImpl.class);
	
	@Autowired
	FractionRepository fractionRepository;

	@Override
	public FractionInfoDto saveAll(FractionInfoDto fractionInputRequest) {
		List<FractionInfo> list = fractionInputRequest.getFractionList();
		validation(list);
		
		List<FractionInfo> saveAll = fractionRepository.saveAll(list);
		
		return new FractionInfoDto(saveAll);
	}

	private void validation(List<FractionInfo> list) {
		if (list.size() % 12 != 0) {
			LOG.error("Fraction list size should be 12 for every profile!");
			throw new MyForbiddenException();
		}
		Optional<FractionInfo> alreadyExistsFractionOpt = list.stream().
								filter(value-> isExists(value.getProfile(), value.getMonth())).findAny();
		if (alreadyExistsFractionOpt.isPresent()) {
			LOG.error("Fraction month and profile should be uniuqe!");
			throw new MyForbiddenException();
		}
		
		Map<String, Double> profileFractionMap = list.stream()
				.collect(groupingBy(FractionInfo::getProfile, summingDouble(FractionInfo::getFraction)));

		Optional<Entry<String, Double>> fractionSumNotOne = profileFractionMap.entrySet().stream()
				.filter(entry -> entry.getValue() != 1).findAny();
		
		if (fractionSumNotOne.isPresent()) {
			LOG.error("Sum of fractions for every profile shoulde be 1!");
			throw new MyForbiddenException();
		}
	}

	public FractionInfo save(FractionInfo p) {
		return fractionRepository.save(p);
	}

	@Override
	public Optional<FractionInfo> findById(Long fractionId) {
		return fractionRepository.findById(fractionId);
	}

	@Override
	public Optional<FractionInfo> findByProfileAndMonth(String profile, String month) {
		return fractionRepository.findByProfileAndMonth(profile, month);
	}
	
	@Override
	public boolean isExists(String profile, String month) {
		Optional<FractionInfo> fractionInfoOpt = fractionRepository.findByProfileAndMonth(profile, month);
		
		return fractionInfoOpt.isPresent();
	}

}
