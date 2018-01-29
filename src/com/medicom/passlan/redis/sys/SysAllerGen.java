package com.medicom.passlan.redis.sys;

public class SysAllerGen implements java.io.Serializable{
	
	private Integer matchscheme;
	
	private String ucode;
	
	private String uname;

	private String allerid; 
	
	private String allername; 
	
	private Integer allertype;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getAllerid() {
		return allerid;
	}

	public void setAllerid(String allerid) {
		this.allerid = allerid;
	}

	public String getAllername() {
		return allername;
	}

	public void setAllername(String allername) {
		this.allername = allername;
	}

	public Integer getAllertype() {
		return allertype;
	}

	public void setAllertype(Integer allertype) {
		this.allertype = allertype;
	}

	public void setMatchscheme(Integer matchscheme) {
		this.matchscheme = matchscheme;
	}

	
	
	

}
