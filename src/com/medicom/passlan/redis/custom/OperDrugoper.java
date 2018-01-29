package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  OperDrugoper </li>
 * <li>类描述：   围术期用药审查手术药品关系表</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月28日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class OperDrugoper implements java.io.Serializable{
	
	private Integer pkid;

	private String caseid;
	
	private String orderno;
	
	private String drugUniqueCode;
	
	private String drugName;
	
	private String drugStartTime;
	
	private String drugEndTime;
	
	private String drugExecuteTime;
	
	private String operationCode;
	
	private String operationName;
	
	private String operationStartTime;
	
	private String operationEndTime;

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

	public String getDrugStartTime() {
		return drugStartTime;
	}

	public void setDrugStartTime(String drugStartTime) {
		this.drugStartTime = drugStartTime;
	}

	public String getDrugEndTime() {
		return drugEndTime;
	}

	public void setDrugEndTime(String drugEndTime) {
		this.drugEndTime = drugEndTime;
	}

	public String getDrugExecuteTime() {
		return drugExecuteTime;
	}

	public void setDrugExecuteTime(String drugExecuteTime) {
		this.drugExecuteTime = drugExecuteTime;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationStartTime() {
		return operationStartTime;
	}

	public void setOperationStartTime(String operationStartTime) {
		this.operationStartTime = operationStartTime;
	}

	public String getOperationEndTime() {
		return operationEndTime;
	}

	public void setOperationEndTime(String operationEndTime) {
		this.operationEndTime = operationEndTime;
	}

	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(Integer pkid) {
		this.pkid = pkid;
	}

}
