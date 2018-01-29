package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

/**
 * 
 * <ul>
 * <li>锟斤拷目锟斤拷疲锟絉edisInit </li>
 * <li>锟斤拷锟斤拷疲锟� Unagesp </li>
 * <li>锟斤拷锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷锟斤拷药锟斤拷锟斤拷  </li>
 * <li>锟斤拷锟斤拷锟剿ｏ拷锟斤拷应强 </li>
 * <li>锟斤拷锟斤拷时锟戒：2016锟斤拷3锟斤拷10锟斤拷 </li>
 * <li>锟睫改憋拷注锟斤拷</li>
 * </ul>
 */
//@Entity
//@Table(name="mc_user_unage_sp")
public class UnagespCustom implements java.io.Serializable{
	
//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//    @Column(name="hiscode")
	private String hiscode;
	
//    @Column(name="moduleid")
	private int moduleid;
	
//    @Column(name="drug_unique_code")
	private String druguniquecode;
	
//    @Column(name="drugname")
	private String drugname;
	
//    @Column(name="slcode")
	private int slcode;
	
//    @Column(name="severity")
	private String severity;
	
//    @Column(name="warning")
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

	public int getModuleid() {
		return moduleid;
	}

	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
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
