package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  AdultCustom </li>
 * <li>绫绘弿杩帮細 鎴愪汉鐢ㄨ嵂 </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�5鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */

public class AdultCustom implements java.io.Serializable{
	

	private int pkid;

	private String hiscode;

	private String druguniquecode;

	private String drugname;
	
	private String agelow;
	
	private String agelowunit;

	private int unequallow;

	private String agehigh;

	private String agehighunit;

	private int unequalhigh;

	private String agedesc;

	private int slcode;

	private String severity;
	
	private String warning;

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

	public String getAgelow() {
		return agelow;
	}

	public void setAgelow(String agelow) {
		this.agelow = agelow;
	}

	public String getAgelowunit() {
		return agelowunit;
	}

	public void setAgelowunit(String agelowunit) {
		this.agelowunit = agelowunit;
	}

	public int getUnequallow() {
		return unequallow;
	}

	public void setUnequallow(int unequallow) {
		this.unequallow = unequallow;
	}

	public String getAgehigh() {
		return agehigh;
	}

	public void setAgehigh(String agehigh) {
		this.agehigh = agehigh;
	}

	public String getAgehighunit() {
		return agehighunit;
	}

	public void setAgehighunit(String agehighunit) {
		this.agehighunit = agehighunit;
	}

	public int getUnequalhigh() {
		return unequalhigh;
	}

	public void setUnequalhigh(int unequalhigh) {
		this.unequalhigh = unequalhigh;
	}

	public String getAgedesc() {
		return agedesc;
	}

	public void setAgedesc(String agedesc) {
		this.agedesc = agedesc;
	}

	public int getSlcode() {
		return slcode;
	}

	public void setSlcode(int slcode) {
		this.slcode = slcode;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	
	
	
	
	

}
