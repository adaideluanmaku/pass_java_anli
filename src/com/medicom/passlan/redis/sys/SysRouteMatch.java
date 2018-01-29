package com.medicom.passlan.redis.sys;

//import javax.persistence.Column;
//import javax.persistence.Id;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  SysRouteMatch </li>
 * <li>绫绘弿杩帮細  缁欒嵂閫斿緞閰嶅鏁版嵁 </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�1鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
public class SysRouteMatch implements java.io.Serializable{
	
//	@Column(name="match_scheme")
	private Integer matchscheme;
	
//	@Column(name="u_code")
	private String ucode;
	
//	@Column(name="u_name")
	private String uname;
	
//	@Column(name="routeid")
	private Integer routeId;
	
//	@Column(name="st_routeid")
	private String strouteId;
	
//	@Column(name="route_name")
	private String routeName;
	
//	@Column(name="routelabel")
	private String routeLabel;

	public Integer getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(Integer matchscheme) {
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

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getStrouteId() {
		return strouteId;
	}

	public void setStrouteId(String strouteId) {
		this.strouteId = strouteId;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getRouteLabel() {
		return routeLabel;
	}

	public void setRouteLabel(String routeLabel) {
		this.routeLabel = routeLabel;
	}

	
	
	
	
}
