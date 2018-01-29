package com.medicom.passlan.redis.sys;

//感染/慢性疾病字典数据 
public class DisDictionary implements java.io.Serializable{

	private int matchscheme;
	
	private String discode;
	
	private String disname;
	
	private int distype;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
	}

	public String getDiscode() {
		return discode;
	}

	public void setDiscode(String discode) {
		this.discode = discode;
	}

	public String getDisname() {
		return disname;
	}

	public void setDisname(String disname) {
		this.disname = disname;
	}

	public int getDistype() {
		return distype;
	}

	public void setDistype(int distype) {
		this.distype = distype;
	}
	
}
