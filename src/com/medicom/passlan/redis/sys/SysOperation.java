package com.medicom.passlan.redis.sys;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  SysOperation </li>
 * <li>类描述：   手术信息</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class SysOperation implements java.io.Serializable{
	
	private int matchscheme;
	
	private String operationCode;
	
	private String operationName;
	
	private int useanti;
	
	private int drugTime;
	
	private double premomentLow;
	
	private double premomentHigh;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
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

	public int getUseanti() {
		return useanti;
	}

	public void setUseanti(int useanti) {
		this.useanti = useanti;
	}

	public int getDrugTime() {
		return drugTime;
	}

	public void setDrugTime(int drugTime) {
		this.drugTime = drugTime;
	}

	public double getPremomentLow() {
		return premomentLow;
	}

	public void setPremomentLow(double premomentLow) {
		this.premomentLow = premomentLow;
	}

	public double getPremomentHigh() {
		return premomentHigh;
	}

	public void setPremomentHigh(double premomentHigh) {
		this.premomentHigh = premomentHigh;
	}
	
}
