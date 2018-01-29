package com.medicom.passlan.redis.sys;

//import javax.persistence.Column;
//import javax.persistence.Entity;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  SysDrugMatch </li>
 * <li>绫绘弿杩帮細  鑽搧閰嶅琛ㄧ殑鏁版嵁 </li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�9鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
//@Entity
public class SysDrugMatch implements java.io.Serializable{

	
//	@Column(name="match_scheme")
	private int drugmatchscheme;
	
	
//	@Column(name="u_unique_code")
	private String uuniqueCode;
	
//	@Column(name="u_code")
	private String ucode;
	
//	@Column(name="u_name")
	private String uname;
	
//	@Column(name="u_doseunit")
	private String udoseunit;
	
//	@Column(name="u_form")
	private String uform;
	
//	@Column(name="u_strength")
	private String ustrength;
	
//	@Column(name="u_comp_name")
	private String ucompname;
	
//	@Column(name="u_approvalcode")
	private String uapprovalcode;
	
//	@Column(name="pass_dividend")
	private Double passdividend;
	
//	@Column(name="pass_divisor")
	private Double passdivisor;
	
//	@Column(name="doseunit")
	private String doseunit;
	
	
//	@Column(name="drugcode")
	private String drugcode;
//	
//	@Column(name="drugname")
//	private String drugname;
//	
//	@Column(name="st_cbrand")
//	private String stcbrand;
//	
//	@Column(name="genid")
//	private String genid; 
//	
//	@Column(name="st_formid")
//	private String stformid; 
//	
//	@Column(name="form_name")
//	private String formname; 
//	
//	@Column(name="st_strength")
//	private String ststrength; 
//	
//	@Column(name="st_compid")
//	private Integer stcompid; 
//	
//	@Column(name="st_comp_name")
//	private String stcompname; 
//	
//	@Column(name="approvalcode")
//	private String approvalcode; 
//	
//	@Column(name="doseunit")
//	private String doseunit;
//	
//	@Column(name="mdid")
//	private Integer mdid;
//	
//	@Column(name="is_solvent")
//	private Integer issolvent;
//	
//	@Column(name="split_sfdanum")
//	private String splitsfdanum; 
//	
//	
//	@Column(name="matchdesc")
//	private String matchdesc; 
//	
//	@Column(name="menulabel")
//	private String menulabel;
//	
//	@Column(name="tpn")
//	private Integer tpn;
//	
//	@Column(name="pi_link")
//	private Integer pilink;
//	
//	@Column(name="pi_count")
//	private Integer picount;
//	
//	@Column(name="pe_count")
//	private Integer pecount; 
//	
//	@Column(name="chp_count")
//	private Integer chpcount;
//	
//	@Column(name="is_inject")
//	private Integer isinject;
//	
//	@Column(name="modulelabel")
//	private String modulelabel;

	public int getDrugmatchscheme() {
		return drugmatchscheme;
	}

	public void setDrugmatchscheme(int drugmatchscheme) {
		this.drugmatchscheme = drugmatchscheme;
	}

	public String getUuniqueCode() {
		return uuniqueCode;
	}

	public void setUuniqueCode(String uuniqueCode) {
		this.uuniqueCode = uuniqueCode;
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

	public String getUdoseunit() {
		return udoseunit;
	}

	public void setUdoseunit(String udoseunit) {
		this.udoseunit = udoseunit;
	}

	public String getUform() {
		return uform;
	}

	public void setUform(String uform) {
		this.uform = uform;
	}

	public String getUstrength() {
		return ustrength;
	}

	public void setUstrength(String ustrength) {
		this.ustrength = ustrength;
	}

	public String getUcompname() {
		return ucompname;
	}

	public void setUcompname(String ucompname) {
		this.ucompname = ucompname;
	}

	public String getUapprovalcode() {
		return uapprovalcode;
	}

	public void setUapprovalcode(String uapprovalcode) {
		this.uapprovalcode = uapprovalcode;
	}

	public Double getPassdividend() {
		return passdividend;
	}

	public void setPassdividend(Double passdividend) {
		this.passdividend = passdividend;
	}

	public Double getPassdivisor() {
		return passdivisor;
	}

	public void setPassdivisor(Double passdivisor) {
		this.passdivisor = passdivisor;
	}

	public String getDrugcode() {
		return drugcode;
	}

	public void setDrugcode(String drugcode) {
		this.drugcode = drugcode;
	}
//	public String getDrugname() {
//		return drugname;
//	}
//
//	public void setDrugname(String drugname) {
//		this.drugname = drugname;
//	}
//
//	
//
//	public String getGenid() {
//		return genid;
//	}
//
//	public void setGenid(String genid) {
//		this.genid = genid;
//	}
//
//	
//	public String getApprovalcode() {
//		return approvalcode;
//	}
//
//	public void setApprovalcode(String approvalcode) {
//		this.approvalcode = approvalcode;
//	}

	public String getDoseunit() {
		return doseunit;
	}

	public void setDoseunit(String doseunit) {
		this.doseunit = doseunit;
	}

	

	

//	public String getMatchdesc() {
//		return matchdesc;
//	}
//
//	public void setMatchdesc(String matchdesc) {
//		this.matchdesc = matchdesc;
//	}
//
//	public String getMenulabel() {
//		return menulabel;
//	}
//
//	public void setMenulabel(String menulabel) {
//		this.menulabel = menulabel;
//	}
//
//	
//	
//	public String getStcbrand() {
//		return stcbrand;
//	}
//
//	public void setStcbrand(String stcbrand) {
//		this.stcbrand = stcbrand;
//	}
//
//	public String getStformid() {
//		return stformid;
//	}
//
//	public void setStformid(String stformid) {
//		this.stformid = stformid;
//	}
//
//	public String getFormname() {
//		return formname;
//	}
//
//	public void setFormname(String formname) {
//		this.formname = formname;
//	}
//
//	public String getStstrength() {
//		return ststrength;
//	}
//
//	public void setStstrength(String ststrength) {
//		this.ststrength = ststrength;
//	}
//
//	
//
//	public String getStcompname() {
//		return stcompname;
//	}
//
//	public void setStcompname(String stcompname) {
//		this.stcompname = stcompname;
//	}
//
//	
//
//	public String getSplitsfdanum() {
//		return splitsfdanum;
//	}
//
//	public void setSplitsfdanum(String splitsfdanum) {
//		this.splitsfdanum = splitsfdanum;
//	}
//
//	public Integer getStcompid() {
//		return stcompid;
//	}
//
//	public void setStcompid(Integer stcompid) {
//		this.stcompid = stcompid;
//	}
//
//	public Integer getMdid() {
//		return mdid;
//	}
//
//	public void setMdid(Integer mdid) {
//		this.mdid = mdid;
//	}
//
//	public Integer getIssolvent() {
//		return issolvent;
//	}
//
//	public void setIssolvent(Integer issolvent) {
//		this.issolvent = issolvent;
//	}
//
//	public Integer getTpn() {
//		return tpn;
//	}
//
//	public void setTpn(Integer tpn) {
//		this.tpn = tpn;
//	}
//
//	public Integer getPilink() {
//		return pilink;
//	}
//
//	public void setPilink(Integer pilink) {
//		this.pilink = pilink;
//	}
//
//	public Integer getPicount() {
//		return picount;
//	}
//
//	public void setPicount(Integer picount) {
//		this.picount = picount;
//	}
//
//	public Integer getPecount() {
//		return pecount;
//	}
//
//	public void setPecount(Integer pecount) {
//		this.pecount = pecount;
//	}
//
//	public Integer getChpcount() {
//		return chpcount;
//	}
//
//	public void setChpcount(Integer chpcount) {
//		this.chpcount = chpcount;
//	}
//
//	public Integer getIsinject() {
//		return isinject;
//	}
//
//	public void setIsinject(Integer isinject) {
//		this.isinject = isinject;
//	}
//
//	public String getModulelabel() {
//		return modulelabel;
//	}
//
//	public void setModulelabel(String modulelabel) {
//		this.modulelabel = modulelabel;
//	} 
	
	
	
	
}
