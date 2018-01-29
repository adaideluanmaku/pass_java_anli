package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  DoctorPriv </li>
 * <li>类描述：   医生越权用药的用户配置表自定义表</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月21日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class DoctorPrivCustom implements java.io.Serializable{

	private String hiscode;
	
	private String druguniquecode;
	
	private String doctorcode;
	
	private int slcode;
	
	private String severity;
	
	private String warning;

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

	public String getDoctorcode() {
		return doctorcode;
	}

	public void setDoctorcode(String doctorcode) {
		this.doctorcode = doctorcode;
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
