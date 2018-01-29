package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  OperationPrivCustom </li>
 * <li>类描述：   围术期用药审查信息</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class OperationPrivCustom implements java.io.Serializable{
	
	private Integer pkid;
	
	private String hiscode;
	
	private String operationCode;
	
	private String operationName;
	
	private String drugUniqueCode;
	
	private Integer slcode ;
	
	private String severity;
	
	private String warning;
	

	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(Integer pkid) {
		this.pkid = pkid;
	}

	public String getHiscode() {
		return hiscode;
	}

	public void setHiscode(String hiscode) {
		this.hiscode = hiscode;
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

	public Integer getSlcode() {
		return slcode;
	}

	public void setSlcode(Integer slcode) {
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

	public String getDrugUniqueCode() {
		return drugUniqueCode;
	}

	public void setDrugUniqueCode(String drugUniqueCode) {
		this.drugUniqueCode = drugUniqueCode;
	}
	
	
	

}
