package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  ProDrugReason </li>
 * <li>类描述：   围术期用药信息的数据</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年5月3日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class ProDrugReason implements java.io.Serializable{
	
	private int pkid;
	
	private String hiscode;
	
	private String hisname;
	
	private int patStatus;
	
	private String inhospno;
	
	private String 	caseid;
	
	private String visitCode;
	
	private String patCode;
	
	private String recipno;
	
	private String orderDeptCode;
	
	private String orderDoctorCode;
	
	private String orderStartTime;
	
	private String orderEndTime;
	
	private String orderExecuteTime;
	
	private String drugUniqueCode;
	
	private String doseunit;
	
	private String doseperTime;
	
	private String frequency;
	
	private int istempDrug;
	
	private int moduleId;
	
	private String moduleName;
	
	private int slcode;
	
	private String severity;
	
	private String warning;
	
	private String moduleItems;
	
	private String otherInfo;
	
	private int isForstatic;
	
	private String reason;
	
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

	public String getHisname() {
		return hisname;
	}

	public void setHisname(String hisname) {
		this.hisname = hisname;
	}

	public int getPatStatus() {
		return patStatus;
	}

	public void setPatStatus(int patStatus) {
		this.patStatus = patStatus;
	}

	public String getInhospno() {
		return inhospno;
	}

	public void setInhospno(String inhospno) {
		this.inhospno = inhospno;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getVisitCode() {
		return visitCode;
	}

	public void setVisitCode(String visitCode) {
		this.visitCode = visitCode;
	}

	public String getPatCode() {
		return patCode;
	}

	public void setPatCode(String patCode) {
		this.patCode = patCode;
	}

	public String getRecipno() {
		return recipno;
	}

	public void setRecipno(String recipno) {
		this.recipno = recipno;
	}

	public String getOrderDeptCode() {
		return orderDeptCode;
	}

	public void setOrderDeptCode(String orderDeptCode) {
		this.orderDeptCode = orderDeptCode;
	}

	public String getOrderDoctorCode() {
		return orderDoctorCode;
	}

	public void setOrderDoctorCode(String orderDoctorCode) {
		this.orderDoctorCode = orderDoctorCode;
	}

	public String getOrderStartTime() {
		return orderStartTime;
	}

	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}

	public String getOrderEndTime() {
		return orderEndTime;
	}

	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}

	public String getOrderExecuteTime() {
		return orderExecuteTime;
	}

	public void setOrderExecuteTime(String orderExecuteTime) {
		this.orderExecuteTime = orderExecuteTime;
	}

	public String getDrugUniqueCode() {
		return drugUniqueCode;
	}

	public void setDrugUniqueCode(String drugUniqueCode) {
		this.drugUniqueCode = drugUniqueCode;
	}

	public String getDoseunit() {
		return doseunit;
	}

	public void setDoseunit(String doseunit) {
		this.doseunit = doseunit;
	}

	public String getDoseperTime() {
		return doseperTime;
	}

	public void setDoseperTime(String doseperTime) {
		this.doseperTime = doseperTime;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getIstempDrug() {
		return istempDrug;
	}

	public void setIstempDrug(int istempDrug) {
		this.istempDrug = istempDrug;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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

	public String getModuleItems() {
		return moduleItems;
	}

	public void setModuleItems(String moduleItems) {
		this.moduleItems = moduleItems;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public int getIsForstatic() {
		return isForstatic;
	}

	public void setIsForstatic(int isForstatic) {
		this.isForstatic = isForstatic;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
