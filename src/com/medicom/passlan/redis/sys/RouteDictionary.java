package com.medicom.passlan.redis.sys;

/**
 * 
 * <ul>
 * <li>项目名称：pass_lanservice </li>
 * <li>类名称：  RouteDictionary </li>
 * <li>类描述：   给药途径字典表</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class RouteDictionary implements java.io.Serializable{
	
	private int matchscheme;
	
	private String routecode;
	
	private String routename;
	
	private int routetype;
	
	private int isskintest;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
	}

	public String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(String routecode) {
		this.routecode = routecode;
	}

	public String getRoutename() {
		return routename;
	}

	public void setRoutename(String routename) {
		this.routename = routename;
	}

	public int getRoutetype() {
		return routetype;
	}

	public void setRoutetype(int routetype) {
		this.routetype = routetype;
	}

	public int getIsskintest() {
		return isskintest;
	}

	public void setIsskintest(int isskintest) {
		this.isskintest = isskintest;
	}
	
	

}
