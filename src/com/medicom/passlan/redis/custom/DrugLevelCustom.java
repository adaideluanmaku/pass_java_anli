package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  DrugLevel </li>
 * <li>绫绘弿杩帮細  浣撳閰嶄紞娴撳害 </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
//@Entity
//@Table(name="mc_user_druglevel")
public class DrugLevelCustom implements java.io.Serializable{

//	@Id
//	@Column(name="pkid")
	private int pkid;
	
//	@Column(name="hiscode")
	private String hiscode;
	
//	@Column(name="drug_unique_code1")
	private String druguniquecode;
	
//	@Column(name="drugname1")
	private String drugname;
	
//	@Column(name="doseunit1")
	private String doseunit;
	
//	@Column(name="drug_unique_code2")
	private String druguniquecodetwo;
	
//	@Column(name="drugname2")
	private String drugnametwo;
	
//	@Column(name="doseunit2")
	private String doseunittwo;
	
//	@Column(name="routecode")
	private String routecode;
	
//	@Column(name="druglevel_low")
	private float druglevellow;
	
//	@Column(name="druglevel_high")
	private float druglevelhigh;
	
//	@Column(name="min_druglevel")
	private float mindruglevel;
	
//	@Column(name="max_druglevel")
	private float maxdruglevel;
	
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

	public String getDruguniquecodetwo() {
		return druguniquecodetwo;
	}

	public void setDruguniquecodetwo(String druguniquecodetwo) {
		this.druguniquecodetwo = druguniquecodetwo;
	}

	public String getDrugnametwo() {
		return drugnametwo;
	}

	public void setDrugnametwo(String drugnametwo) {
		this.drugnametwo = drugnametwo;
	}

	public String getDoseunittwo() {
		return doseunittwo;
	}

	public void setDoseunittwo(String doseunittwo) {
		this.doseunittwo = doseunittwo;
	}

	public String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(String routecode) {
		this.routecode = routecode;
	}

	public float getDruglevellow() {
		return druglevellow;
	}

	public void setDruglevellow(float druglevellow) {
		this.druglevellow = druglevellow;
	}

	public float getDruglevelhigh() {
		return druglevelhigh;
	}

	public void setDruglevelhigh(float druglevelhigh) {
		this.druglevelhigh = druglevelhigh;
	}

	public float getMindruglevel() {
		return mindruglevel;
	}

	public void setMindruglevel(float mindruglevel) {
		this.mindruglevel = mindruglevel;
	}

	public float getMaxdruglevel() {
		return maxdruglevel;
	}

	public void setMaxdruglevel(float maxdruglevel) {
		this.maxdruglevel = maxdruglevel;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	
	
	
	
}
