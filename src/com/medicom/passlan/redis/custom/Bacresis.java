package com.medicom.passlan.redis.custom;
/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  Bacresis </li>
 * <li>类描述：  细菌耐药率的数据 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年5月4日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class Bacresis implements java.io.Serializable{
	
	private String hiscode;
	
	private String druguniquecode;
	
	private String drugname;
	
	private String whodrugcode;
	
	private String whodrugname;
	
	private String bacterianame;
	
	private double resistrate;

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

	public String getWhodrugcode() {
		return whodrugcode;
	}

	public void setWhodrugcode(String whodrugcode) {
		this.whodrugcode = whodrugcode;
	}

	public String getWhodrugname() {
		return whodrugname;
	}

	public void setWhodrugname(String whodrugname) {
		this.whodrugname = whodrugname;
	}

	public String getBacterianame() {
		return bacterianame;
	}

	public void setBacterianame(String bacterianame) {
		this.bacterianame = bacterianame;
	}

	public double getResistrate() {
		return resistrate;
	}

	public void setResistrate(double resistrate) {
		this.resistrate = resistrate;
	}
	
	
}
