package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

//@Entity
//@Table(name="mc_user_hepdos")
public class HepdosCustom implements java.io.Serializable{
	
//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="drug_unique_code")
	private String druguniquecode;
	
//	@Column(name="doseunit")
	private String doseunit;
	
//	@Column(name="routecode")
	private String routecode;
	
//	@Column(name="hep_label")
	private int heplabel;
	
//	@Column(name="agelow")
	private String agelow;
	
//	@Column(name="unequal_low")
	private int unequallow;
	
//	@Column(name="agelow_unit")
	private String agelowunit;
	
//	@Column(name="agehigh")
	private String agehigh;
	
//	@Column(name="unequal_high")
	private int unequalhigh;
	
//	@Column(name="agehigh_unit")
	private String agehighunit;
	
//	@Column(name="agedesc")
	private String agedesc;
	
//	@Column(name="calculate_label")
	private int calculatelabel;
	
//	@Column(name="dose_each_low")
	private float dose_eachlow;
	
//	@Column(name="dose_each_low_unit")
	private String doseeachlowunit;
	
//	@Column(name="dose_each_high")
	private float doseeachhigh;
	
//	@Column(name="dose_each_high_unit")
	private String doseeachhighunit;
	
//	@Column(name="dose_day_low")
	private float dosedaylow;
	
//	@Column(name="dose_day_low_unit")
	private String dosedaylowunit;
	
//	@Column(name="dose_day_high")
	private float dosedayhigh;
	
//	@Column(name="dose_day_high_unit")
	private String dosedayhighunit; 
	
//	@Column(name="frequency_low")
	private int frequencylow;
	
//	@Column(name="frequency_low_day")
	private int frequencylowday;
	
//	@Column(name="frequency_high")
	private int frequencyhigh;
	
//	@Column(name="frequency_high_day")
	private int frequencyhighday;

	public int getPkid() {
		return pkid;
	}

	public void setPkid(int pkid) {
		this.pkid = pkid;
	}

	public String getHiscode() {
		return hiscode;
	}

	public void setHiscode(String hiscode) {
		this.hiscode = hiscode;
	}

	public String getDruguniquecode() {
		return druguniquecode;
	}

	public void setDruguniquecode(String druguniquecode) {
		this.druguniquecode = druguniquecode;
	}

	public String getDoseunit() {
		return doseunit;
	}

	public void setDoseunit(String doseunit) {
		this.doseunit = doseunit;
	}

	public String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(String routecode) {
		this.routecode = routecode;
	}

	public int getHeplabel() {
		return heplabel;
	}

	public void setHeplabel(int heplabel) {
		this.heplabel = heplabel;
	}

	public String getAgelow() {
		return agelow;
	}

	public void setAgelow(String agelow) {
		this.agelow = agelow;
	}

	public int getUnequallow() {
		return unequallow;
	}

	public void setUnequallow(int unequallow) {
		this.unequallow = unequallow;
	}

	public String getAgelowunit() {
		return agelowunit;
	}

	public void setAgelowunit(String agelowunit) {
		this.agelowunit = agelowunit;
	}

	public String getAgehigh() {
		return agehigh;
	}

	public void setAgehigh(String agehigh) {
		this.agehigh = agehigh;
	}

	public int getUnequalhigh() {
		return unequalhigh;
	}

	public void setUnequalhigh(int unequalhigh) {
		this.unequalhigh = unequalhigh;
	}

	public String getAgehighunit() {
		return agehighunit;
	}

	public void setAgehighunit(String agehighunit) {
		this.agehighunit = agehighunit;
	}

	public String getAgedesc() {
		return agedesc;
	}

	public void setAgedesc(String agedesc) {
		this.agedesc = agedesc;
	}

	public int getCalculatelabel() {
		return calculatelabel;
	}

	public void setCalculatelabel(int calculatelabel) {
		this.calculatelabel = calculatelabel;
	}

	public float getDose_eachlow() {
		return dose_eachlow;
	}

	public void setDose_eachlow(float dose_eachlow) {
		this.dose_eachlow = dose_eachlow;
	}

	public String getDoseeachlowunit() {
		return doseeachlowunit;
	}

	public void setDoseeachlowunit(String doseeachlowunit) {
		this.doseeachlowunit = doseeachlowunit;
	}

	public float getDoseeachhigh() {
		return doseeachhigh;
	}

	public void setDoseeachhigh(float doseeachhigh) {
		this.doseeachhigh = doseeachhigh;
	}

	public String getDoseeachhighunit() {
		return doseeachhighunit;
	}

	public void setDoseeachhighunit(String doseeachhighunit) {
		this.doseeachhighunit = doseeachhighunit;
	}

	public float getDosedaylow() {
		return dosedaylow;
	}

	public void setDosedaylow(float dosedaylow) {
		this.dosedaylow = dosedaylow;
	}

	public String getDosedaylowunit() {
		return dosedaylowunit;
	}

	public void setDosedaylowunit(String dosedaylowunit) {
		this.dosedaylowunit = dosedaylowunit;
	}

	public float getDosedayhigh() {
		return dosedayhigh;
	}

	public void setDosedayhigh(float dosedayhigh) {
		this.dosedayhigh = dosedayhigh;
	}

	public String getDosedayhighunit() {
		return dosedayhighunit;
	}

	public void setDosedayhighunit(String dosedayhighunit) {
		this.dosedayhighunit = dosedayhighunit;
	}

	public int getFrequencylow() {
		return frequencylow;
	}

	public void setFrequencylow(int frequencylow) {
		this.frequencylow = frequencylow;
	}

	public int getFrequencylowday() {
		return frequencylowday;
	}

	public void setFrequencylowday(int frequencylowday) {
		this.frequencylowday = frequencylowday;
	}

	public int getFrequencyhigh() {
		return frequencyhigh;
	}

	public void setFrequencyhigh(int frequencyhigh) {
		this.frequencyhigh = frequencyhigh;
	}

	public int getFrequencyhighday() {
		return frequencyhighday;
	}

	public void setFrequencyhighday(int frequencyhighday) {
		this.frequencyhighday = frequencyhighday;
	}
	
}
