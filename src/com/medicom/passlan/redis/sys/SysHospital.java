package com.medicom.passlan.redis.sys;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  SysHospital </li>
 * <li>类描述： 用户的数据 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年9月27日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class SysHospital implements java.io.Serializable{
	
	private String hiscode;
	
	private String hiscode_user;
	
	private String stru_type;
	
	private String hospname;
	
	private String hiscode_p;

	public String getHiscode() {
		return hiscode;
	}

	public void setHiscode(String hiscode) {
		this.hiscode = hiscode;
	}

	public String getHiscode_user() {
		return hiscode_user;
	}

	public void setHiscode_user(String hiscode_user) {
		this.hiscode_user = hiscode_user;
	}

	public String getStru_type() {
		return stru_type;
	}

	public void setStru_type(String stru_type) {
		this.stru_type = stru_type;
	}

	public String getHospname() {
		return hospname;
	}

	public void setHospname(String hospname) {
		this.hospname = hospname;
	}

	public String getHiscode_p() {
		return hiscode_p;
	}

	public void setHiscode_p(String hiscode_p) {
		this.hiscode_p = hiscode_p;
	}
	
	

}
