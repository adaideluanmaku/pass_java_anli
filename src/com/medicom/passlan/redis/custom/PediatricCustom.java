package com.medicom.passlan.redis.custom;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  Pediatric </li>
 * <li>绫绘弿杩帮細  鍎跨鐢ㄨ嵂鑷畾涔夊疄浣撶被 </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
//@Entity
//@Table(name="mc_user_pediatric")
public class PediatricCustom implements java.io.Serializable{
	
//	@Id
//	@Column(name="pkid")
	private int pkid;
	
	  /**<  鍖婚櫌缂栫爜 */
//	@Column(name="hiscode")
	private String hiscode;
		  
	/**<  鍖婚櫌鑽搧缂栫爜 */
//	@Column(name="drug_unique_code")
	private String drugUniqueCode;
		  
    /**<  鍖婚櫌鑽搧鍚嶇О */
//	@Column(name="drugname")
	private String drugName;
		  
	/**< 瀹℃煡淇℃伅琛�骞撮緞浣庡� */
//	@Column(name="agelow")
	private String ageLow;
		  
    /**< 涓嶇瓑浜庡勾榫勪綆鍊�0-绛変簬 1-涓嶇瓑浜�*/
//	@Column(name="unequal_low")
	private int unequalLow;
		  
    /**< 瀹℃煡淇℃伅琛�骞撮緞浣庡�鍗曚綅 */
//	@Column(name="agelow_unit")
	private String ageLowUnit;
		  
		  /**< 瀹℃煡淇℃伅琛�骞撮緞楂樺� */
//	@Column(name="agehigh")
	private String ageHigh;
		  
	/**< 涓嶇瓑浜庡勾榫勯珮鍊�0-绛変簬 1-涓嶇瓑浜�*/
//	@Column(name="unequal_high")
	private int unequalHigh;
		  
	/**< 瀹℃煡淇℃伅琛�骞撮緞楂樺�鍗曚綅 */
//	@Column(name="agehigh_unit")
	private String ageHighUnit; 
		  
	/**< 瀹℃煡淇℃伅琛�骞撮緞娈垫弿杩�*/
//	@Column(name="agedesc")
	private String ageDesc;  
		  
	/**< 涓ラ噸绋嬪害鏍囪 */
//	@Column(name="slcode")
	private int slcode;   
		  
	/**< 涓ラ噸绋嬪害 */
//	@Column(name="severity")
	private String severity; 
		  
	/**< 绠�璀︾ず */
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

	public String getDrugUniqueCode() {
		return drugUniqueCode;
	}

	public void setDrugUniqueCode(String drugUniqueCode) {
		this.drugUniqueCode = drugUniqueCode;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getAgeLow() {
		return ageLow;
	}

	public void setAgeLow(String ageLow) {
		this.ageLow = ageLow;
	}

	public int getUnequalLow() {
		return unequalLow;
	}

	public void setUnequalLow(int unequalLow) {
		this.unequalLow = unequalLow;
	}

	public String getAgeLowUnit() {
		return ageLowUnit;
	}

	public void setAgeLowUnit(String ageLowUnit) {
		this.ageLowUnit = ageLowUnit;
	}

	public String getAgeHigh() {
		return ageHigh;
	}

	public void setAgeHigh(String ageHigh) {
		this.ageHigh = ageHigh;
	}

	public int getUnequalHigh() {
		return unequalHigh;
	}

	public void setUnequalHigh(int unequalHigh) {
		this.unequalHigh = unequalHigh;
	}

	public String getAgeHighUnit() {
		return ageHighUnit;
	}

	public void setAgeHighUnit(String ageHighUnit) {
		this.ageHighUnit = ageHighUnit;
	}

	public String getAgeDesc() {
		return ageDesc;
	}

	public void setAgeDesc(String ageDesc) {
		this.ageDesc = ageDesc;
	}

	public int getSlcode() {
		return slcode;
	}

	public void setSlcode(int slcode) {
		this.slcode = slcode;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
}

