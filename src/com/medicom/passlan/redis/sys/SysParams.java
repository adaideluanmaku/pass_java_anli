package com.medicom.passlan.redis.sys;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  SysParams </li>
 * <li>类描述：  系统设置的数据 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年3月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class SysParams implements java.io.Serializable{
	
	private int pkid;
	
	private String checkModel;
	
	private String checkModeName;
	
	private String scrfilters;
	
	private int issave;
	
	//rh_5.0版本没有这几个字段
//	private Integer isscroutDrug;
//	
//	private Integer isscrExactlying;
//	
//	private Integer isscrDupuniquecode;
//	
//	private Integer isUseim;

	public int getPkid() {
		return pkid;
	}

	public void setPkid(int pkid) {
		this.pkid = pkid;
	}

	public String getCheckModel() {
		return checkModel;
	}

	public void setCheckModel(String checkModel) {
		this.checkModel = checkModel;
	}

	public String getCheckModeName() {
		return checkModeName;
	}

	public void setCheckModeName(String checkModeName) {
		this.checkModeName = checkModeName;
	}

	public String getScrfilters() {
		return scrfilters;
	}

	public void setScrfilters(String scrfilters) {
		this.scrfilters = scrfilters;
	}

	public int getIssave() {
		return issave;
	}

	public void setIssave(int issave) {
		this.issave = issave;
	}

//	public Integer getIsscroutDrug() {
//		return isscroutDrug;
//	}
//
//	public void setIsscroutDrug(Integer isscroutDrug) {
//		this.isscroutDrug = isscroutDrug;
//	}
//
//	public Integer getIsscrExactlying() {
//		return isscrExactlying;
//	}
//
//	public void setIsscrExactlying(Integer isscrExactlying) {
//		this.isscrExactlying = isscrExactlying;
//	}
//
//	public Integer getIsscrDupuniquecode() {
//		return isscrDupuniquecode;
//	}
//
//	public void setIsscrDupuniquecode(Integer isscrDupuniquecode) {
//		this.isscrDupuniquecode = isscrDupuniquecode;
//	}
//
//	public Integer getIsUseim() {
//		return isUseim;
//	}
//
//	public void setIsUseim(Integer isUseim) {
//		this.isUseim = isUseim;
//	}
}
