package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  OperDrugadvice </li>
 * <li>类描述：  围术期用药审查历史抗菌药物信息 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月28日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class OperDrugadvice implements java.io.Serializable {
	
	private String caseid;
	
	private String orderno;
	
	private String drugUniqueCode;
	
	private String drugName;
	
	private String drugExecuteTime;
	
	private String drugEndTime;
	
	private String drugStartTime;

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
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

	public String getDrugExecuteTime() {
		return drugExecuteTime;
	}

	public void setDrugExecuteTime(String drugExecuteTime) {
		this.drugExecuteTime = drugExecuteTime;
	}

	public String getDrugEndTime() {
		return drugEndTime;
	}

	public void setDrugEndTime(String drugEndTime) {
		this.drugEndTime = drugEndTime;
	}

	public String getDrugStartTime() {
		return drugStartTime;
	}

	public void setDrugStartTime(String drugStartTime) {
		this.drugStartTime = drugStartTime;
	}
	
}
