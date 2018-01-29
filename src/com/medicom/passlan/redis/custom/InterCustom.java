package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

//@Entity
//@Table(name="mc_user_inter")
public class InterCustom implements java.io.Serializable{

//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="drug_unique_code1")
	private String druguniquecodeone;
	
//	@Column(name="drug_unique_code2")
	private String druguniquecodetwo;
	
//	@Column(name="drugname1")
	private String drugnameone;
	
//	@Column(name="drugname2")
	private String drugnametwo;
	
//	@Column(name="severity")
	private String severity;
	
//	@Column(name="warning")
	private String warning;
	
//	@Column(name="slcode")
	private int slcode;

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

	public String getDruguniquecodeone() {
		return druguniquecodeone;
	}

	public void setDruguniquecodeone(String druguniquecodeone) {
		this.druguniquecodeone = druguniquecodeone;
	}

	public String getDruguniquecodetwo() {
		return druguniquecodetwo;
	}

	public void setDruguniquecodetwo(String druguniquecodetwo) {
		this.druguniquecodetwo = druguniquecodetwo;
	}

	public String getDrugnameone() {
		return drugnameone;
	}

	public void setDrugnameone(String drugnameone) {
		this.drugnameone = drugnameone;
	}

	public String getDrugnametwo() {
		return drugnametwo;
	}

	public void setDrugnametwo(String drugnametwo) {
		this.drugnametwo = drugnametwo;
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

	public int getSlcode() {
		return slcode;
	}

	public void setSlcode(int slcode) {
		this.slcode = slcode;
	}
	
}
