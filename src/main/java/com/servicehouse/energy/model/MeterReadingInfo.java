package com.servicehouse.energy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "meter_reading_info")
public class MeterReadingInfo implements Serializable {

	private static final long serialVersionUID = 9045098179799205444L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String meterId;
	private String month;
	private String profile;
	private double meterReading;
	private double consumption;
	
	@Enumerated(EnumType.STRING)
	@Column(name="meter_reading_info_status")
	private MeterReadingInfoStatus status;

	public MeterReadingInfo() {
	}

	public MeterReadingInfo(String meterId, String month, String profile, double meterReading) {
		super();
		this.meterId = meterId;
		this.month = month;
		this.profile = profile;
		this.meterReading = meterReading;
		this.status = MeterReadingInfoStatus.ACCEPTED;
	}

	public Long getId() {
		return id;
	}

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public double getMeterReading() {
		return meterReading;
	}

	public void setMeterReading(double meterReading) {
		this.meterReading = meterReading;
	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	public MeterReadingInfoStatus getStatus() {
		return status;
	}

	public void setStatus(MeterReadingInfoStatus status) {
		this.status = status;
	}

}
