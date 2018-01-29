package com.medicom.passlan.redis.custom;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  UserPiCustom </li>
 * <li>类描述：   用户自定义说明书</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年5月12日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class UserPiCustom {
	
	private String hiscode;
	
	private String druguniquecode;
	
	private Integer seqnum;
	
	private String phname;
	
	private String content;

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

	public Integer getSeqnum() {
		return seqnum;
	}

	public void setSeqnum(Integer seqnum) {
		this.seqnum = seqnum;
	}

	public String getPhname() {
		return phname;
	}

	public void setPhname(String phname) {
		this.phname = phname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
