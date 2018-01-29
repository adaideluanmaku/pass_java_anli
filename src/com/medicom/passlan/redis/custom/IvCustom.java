package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

//@Entity
//@Table(name="mc_user_iv")
public class IvCustom implements java.io.Serializable{
	
//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="drug_unique_codes")
	private String druguniquecode;
	
//	@Column(name="drugnames")
	private String drugnames;
	
//	@Column(name="drugcodes")
	private String drugcodes;
	
//	@Column(name="routetype")
	private int routetype;
	
//	@Column(name="routedesc")
	private String routedesc;
	
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

	public String getDrugnames() {
		return drugnames;
	}

	public void setDrugnames(String drugnames) {
		this.drugnames = drugnames;
	}

	public String getDrugcodes() {
		return drugcodes;
	}

	public void setDrugcodes(String drugcodes) {
		this.drugcodes = drugcodes;
	}

	public int getRoutetype() {
		return routetype;
	}

	public void setRoutetype(int routetype) {
		this.routetype = routetype;
	}

	public String getRoutedesc() {
		return routedesc;
	}

	public void setRoutedesc(String routedesc) {
		this.routedesc = routedesc;
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
