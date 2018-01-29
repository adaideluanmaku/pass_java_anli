package com.medicom.passlan.redis.sys;

//import javax.persistence.Entity;
//import javax.persistence.Table;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  SysFrequency </li>
 * <li>绫绘弿杩帮細 缁欒嵂棰戞閰嶅琛�/li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�1鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
public class SysFrequency implements java.io.Serializable{
	
	private int matchscheme;
	
	private String frequency;
	
	private Integer times;
	
	private Integer days;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	

}
