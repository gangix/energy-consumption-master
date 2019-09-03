package com.servicehouse.energy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.servicehouse.energy.common.Constants;
import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.dto.MeterReadingInfoDto;
import com.servicehouse.energy.model.FractionInfo;
import com.servicehouse.energy.model.MeterReadingInfo;

@Service
public class FileInputServiceImpl implements FileInputService {

	private static final Logger LOG = LoggerFactory.getLogger(FileInputServiceImpl.class);

	@Autowired
	MeterReadingService meterReadingService;

	@Autowired
	FractionService fractionService;

	@Value("${path.fractions}")
	private String fractionsPath;

	@Value("${path.meters}")
	private String metersPath;

	@Override
	public MeterReadingInfoDto saveAllMeterReadings() {
		MeterReadingInfoDto dto = new MeterReadingInfoDto();
		List<MeterReadingInfo> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(metersPath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.trim().split(Constants.COMMA_DELIMITER);
				String meterId = values[0];
				String profile = values[1];
				String month = values[2];
				double meterReading = Double.valueOf(values[3]);
				MeterReadingInfo info = new MeterReadingInfo(meterId, month, profile, meterReading);

				list.add(info);
			}
			dto.setMeterReadingInfoList(list);
			MeterReadingInfoDto returnedDto = meterReadingService.saveAll(dto);
			deleteFile(metersPath);
			
			return returnedDto;
		}
		catch (RuntimeException e) {
			LOG.error(e.getMessage(), e);
			writeErrorLogFile(fractionsPath, e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			writeErrorLogFile(fractionsPath, e);
		}
		return new MeterReadingInfoDto();
	}

	@Override
	public FractionInfoDto saveAllFractions() {
		FractionInfoDto dto = new FractionInfoDto();
		List<FractionInfo> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fractionsPath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.trim().split(Constants.COMMA_DELIMITER);
				String month = values[0];
				String profile = values[1];
				double fraction = Double.valueOf(values[2]);
				FractionInfo info = new FractionInfo(month, profile, fraction);

				list.add(info);
			}
			dto.setFractionList(list);
			FractionInfoDto returnedDto = fractionService.saveAll(dto);
			deleteFile(fractionsPath);
			
			return returnedDto;
		} catch (RuntimeException e) {
			LOG.error(e.getMessage(), e);
			writeErrorLogFile(fractionsPath, e);
			throw e;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			writeErrorLogFile(fractionsPath, e);
		}
		return new FractionInfoDto();
	}

	private void deleteFile(String path) {
		File file = new File(path); 
		file.delete();
	}

	private void writeErrorLogFile(String path, Exception ex) {
		try {
			String absoluteFilePath = path.substring(0, path.length() - 4) +UUID.randomUUID().toString()+ "_error.txt";
			File file = new File(absoluteFilePath);
			file.createNewFile();
			ex.printStackTrace(new PrintStream(file));			
		} catch (Exception e) {
			LOG.error("Exception occured in logging" + e);
		}

	}

}
