package com.medicom.passlan.redis.sys;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  Doctor </li>
 * <li>类描述：   医生基本信息表</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月21日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class SysDoctor implements java.io.Serializable{
	
	private int matchscheme;
	
	private String doctorcode;
	
	private String doctorname;
	
	private int prespriv;
	
	private int ilevel;
	
	private int antilevel;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
	}

	public String getDoctorcode() {
		return doctorcode;
	}

	public void setDoctorcode(String doctorcode) {
		this.doctorcode = doctorcode;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public int getPrespriv() {
		return prespriv;
	}

	public void setPrespriv(int prespriv) {
		this.prespriv = prespriv;
	}

	public int getIlevel() {
		return ilevel;
	}

	public void setIlevel(int ilevel) {
		this.ilevel = ilevel;
	}

	public int getAntilevel() {
		return antilevel;
	}

	public void setAntilevel(int antilevel) {
		this.antilevel = antilevel;
	}
	

}
