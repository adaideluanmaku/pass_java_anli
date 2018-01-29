package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  DrugDisCustom </li>
 * <li>绫绘弿杩帮細   瓒呴�搴�/li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�5鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
//@Entity
//@Table(name="mc_user_drug_dis")
public class DrugDisCustom implements java.io.Serializable{
	
//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="drug_unique_code")
	private String druguniquecode;
	
//	@Column(name="moduleid")
	private Integer moduleid;
	
//	@Column(name="drugname")
	private String drugname;
	
//	@Column(name="discode")
	private String discode;
	
//	@Column(name="disname")
	private String disname;
	
//	@Column(name="slcode")
	private int slcode;
	
//	@Column(name="severity")
	private String severity;
	
//	@Column(name="warning")
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

	public Integer getModuleid() {
		return moduleid;
	}

	public void setModuleid(Integer moduleid) {
		this.moduleid = moduleid;
	}

	public String getDrugname() {
		return drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}

	public String getDiscode() {
		return discode;
	}

	public void setDiscode(String discode) {
		this.discode = discode;
	}

	public String getDisname() {
		return disname;
	}

	public void setDisname(String disname) {
		this.disname = disname;
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
