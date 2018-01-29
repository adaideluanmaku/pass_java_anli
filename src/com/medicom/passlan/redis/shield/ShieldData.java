package com.medicom.passlan.redis.shield;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  ShieldData </li>
 * <li>绫绘弿杩帮細  灞忚斀瀵硅薄 </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�2鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
//@Entity
//@Table(name="mc_user_shielddata")
public class ShieldData implements java.io.Serializable{

//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="moduleid")
	private int moduleid;
	
//	@Column(name="modulename")
	private String modulename;
	
//	@Column(name="drug_unique_code1")
	private String druguniquecodeone;
	
//	@Column(name="drug_unique_code2")
	private String druguniquecodetwo;
	
//	@Column(name="doseunit")
	private String doseunit;
	
//	@Column(name="routecode")
	private String routecode;
	
//	@Column(name="routetype")
	private int routetype;
	
//	@Column(name="discode")
	private String discode;
	
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
	
//	@Column(name="shielddesc")
	private String shielddesc;
	
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

	public int getModuleid() {
		return moduleid;
	}

	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
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

	public int getRoutetype() {
		return routetype;
	}

	public void setRoutetype(int routetype) {
		this.routetype = routetype;
	}

	public String getDiscode() {
		return discode;
	}

	public void setDiscode(String discode) {
		this.discode = discode;
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

	public String getShielddesc() {
		return shielddesc;
	}

	public void setShielddesc(String shielddesc) {
		this.shielddesc = shielddesc;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}
	
}

