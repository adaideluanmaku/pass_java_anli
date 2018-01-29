package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  DosageCustom </li>
 * <li>绫绘弿杩帮細 鍓傞噺鑼冨洿  </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�5鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
//@Entity
//@Table(name="mc_user_dosage")
public class DosageCustom implements java.io.Serializable{
	
//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="drug_unique_code")
	private String druguniquecode;
	
//	@Column(name="drugname")
	private String drugname;
	
//	@Column(name="doseunit")
	private String doseunit;
	
//	@Column(name="routecode")
	private String routecode;
	
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
	private float doseeachlow;
	
//	@Column(name="dose_each_low_unit")
	private String doseeachlowunit;
	
//	@Column(name="dose_each_high")
	private float doseeachhigh;
	
//	@Column(name="dose_each_high_unit")
	private String doseeachhighunit;
	
//	@Column(name="maxdose_each")
	private float maxdoseeach;
	
//	@Column(name="maxdose_each_unit")
	private String maxdoseeachunit;
	
//	@Column(name="frequency_low")
	private int frequencylow;
	
//	@Column(name="frequency_low_day")
	private int frequencylowday;
	
//	@Column(name="frequency_high")
	private int frequencyhigh;
	
//	@Column(name="frequency_high_day")
	private int frequencyhighday;
	
//	@Column(name="dose_day_low")
	private float dosedaylow;
	
//	@Column(name="dose_day_low_unit")
	private String dosedaylowunit;
	
//	@Column(name="dose_day_high")
	private float dosedayhigh;
	
//	@Column(name="dose_day_high_unit")
	private String dosedayhighunit;
	
//	@Column(name="maxdose_day")
	private float maxdoseday;
	
//	@Column(name="maxdose_day_unit")
	private String maxdosedayunit;
	
	// add by 鍒樻槬姊�2016-03-23 娣诲姞鐢ㄨ嵂鎸佺画鏃堕棿鍙婄枟绋嬫�鍓傞噺鑷畾涔夊鏌�
//	@Column(name="duration_low")
    private int durationLow;           /**< 瀹℃煡淇℃伅琛�鐢ㄨ嵂鎸佺画鏃堕棿浣庡� */
	
//	@Column(name="duration_high")
    private int durationHigh;          /**< 瀹℃煡淇℃伅琛�鐢ㄨ嵂鎸佺画鏃堕棿楂樺� */
	
//	@Column(name="duration_max")
    private int durationMax;           /**< 瀹℃煡淇℃伅琛�鏋佸ぇ鐢ㄨ嵂鎸佺画鏃堕棿 */
	
//	@Column(name="treatment_dose")
    private double treatmentDose;         /**< 瀹℃煡淇℃伅琛�鐤楃▼鎬诲墏閲�*/
	
//	@Column(name="treatment_dose_unit")
    private String treatmentDoseUnit;     /**< 瀹℃煡淇℃伅琛�鐤楃▼鎬诲墏閲忓崟浣�*/
	

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

	public String getDrugname() {
		return drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
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

	public float getDoseeachlow() {
		return doseeachlow;
	}

	public void setDoseeachlow(float doseeachlow) {
		this.doseeachlow = doseeachlow;
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

	public float getMaxdoseeach() {
		return maxdoseeach;
	}

	public void setMaxdoseeach(float maxdoseeach) {
		this.maxdoseeach = maxdoseeach;
	}

	public String getMaxdoseeachunit() {
		return maxdoseeachunit;
	}

	public void setMaxdoseeachunit(String maxdoseeachunit) {
		this.maxdoseeachunit = maxdoseeachunit;
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

	public float getMaxdoseday() {
		return maxdoseday;
	}

	public void setMaxdoseday(float maxdoseday) {
		this.maxdoseday = maxdoseday;
	}

	public String getMaxdosedayunit() {
		return maxdosedayunit;
	}

	public void setMaxdosedayunit(String maxdosedayunit) {
		this.maxdosedayunit = maxdosedayunit;
	}

	public int getDurationLow() {
		return durationLow;
	}

	public void setDurationLow(int durationLow) {
		this.durationLow = durationLow;
	}

	public int getDurationHigh() {
		return durationHigh;
	}

	public void setDurationHigh(int durationHigh) {
		this.durationHigh = durationHigh;
	}

	public int getDurationMax() {
		return durationMax;
	}

	public void setDurationMax(int durationMax) {
		this.durationMax = durationMax;
	}

	public double getTreatmentDose() {
		return treatmentDose;
	}

	public void setTreatmentDose(double treatmentDose) {
		this.treatmentDose = treatmentDose;
	}

	public String getTreatmentDoseUnit() {
		return treatmentDoseUnit;
	}

	public void setTreatmentDoseUnit(String treatmentDoseUnit) {
		this.treatmentDoseUnit = treatmentDoseUnit;
	}
	
}
