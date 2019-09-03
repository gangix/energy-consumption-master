package com.servicehouse.energy.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fraction_info")
public class FractionInfo implements Serializable {

	private static final long serialVersionUID = -5241781253380015253L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String month;
	private String profile;
	private double fraction;

	public FractionInfo(String month, String profile, double fraction) {
		super();
		this.fraction = fraction;
		this.profile = profile;
		this.month = month;
	}

	public FractionInfo() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public double getFraction() {
		return fraction;
	}

	public void setFraction(double fraction) {
		this.fraction = fraction;
	}
	
	
}
