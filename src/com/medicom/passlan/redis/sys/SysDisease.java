package com.medicom.passlan.redis.sys;

//import javax.persistence.Column;
//import javax.persistence.Id;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  SysDisease </li>
 * <li>绫绘弿杩帮細   鐤剧梾閰嶅琛ㄧ殑鏁版嵁</li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�1鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
public class SysDisease implements java.io.Serializable{

	
//	@Column(name="match_scheme")
	private int matchscheme; //閰嶅鍏崇郴 
	
//	@Column(name="u_code")
	private String ucode;
	
//	@Column(name="u_name")
	private String  uname; 
	
//	@Column(name="icd_code")
	private String icdCode;//icd缂栫爜 
	
//	@Column(name="renal")
	private Integer renal; 
	
//	@Column(name="hepatic")
	private Integer hepatic;
	
//	@Column(name="cardio")
	private Integer cardio; 
	
//	@Column(name="pulm")
	private Integer pulm;
	
//	@Column(name="neur")
	private Integer neur;
	
//	@Column(name="commonindex")
	private Integer commonIndex; 
	
//	@Column(name="endo")
	private Integer endo;
	
//	@Column(name="seqnum")
	private Integer seqNum;
	
//	@Column(name="preg_label")
	private Integer pregLabel; 
	
//	@Column(name="lact_label")
	private Integer lactLabel;
	
//	@Column(name="hep_label")
	private Integer hepLabel;
	
//	@Column(name="ren_label")
	private Integer renLabel;

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

	public Integer getRenal() {
		return renal;
	}

	public void setRenal(Integer renal) {
		this.renal = renal;
	}

	public Integer getHepatic() {
		return hepatic;
	}

	public void setHepatic(Integer hepatic) {
		this.hepatic = hepatic;
	}

	public Integer getCardio() {
		return cardio;
	}

	public void setCardio(Integer cardio) {
		this.cardio = cardio;
	}

	public Integer getPulm() {
		return pulm;
	}

	public void setPulm(Integer pulm) {
		this.pulm = pulm;
	}

	public Integer getNeur() {
		return neur;
	}

	public void setNeur(Integer neur) {
		this.neur = neur;
	}

	public Integer getEndo() {
		return endo;
	}

	public void setEndo(Integer endo) {
		this.endo = endo;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public Integer getCommonIndex() {
		return commonIndex;
	}

	public void setCommonIndex(Integer commonIndex) {
		this.commonIndex = commonIndex;
	}

	public Integer getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public Integer getPregLabel() {
		return pregLabel;
	}

	public void setPregLabel(Integer pregLabel) {
		this.pregLabel = pregLabel;
	}

	public Integer getLactLabel() {
		return lactLabel;
	}

	public void setLactLabel(Integer lactLabel) {
		this.lactLabel = lactLabel;
	}

	public Integer getHepLabel() {
		return hepLabel;
	}

	public void setHepLabel(Integer hepLabel) {
		this.hepLabel = hepLabel;
	}

	public Integer getRenLabel() {
		return renLabel;
	}

	public void setRenLabel(Integer renLabel) {
		this.renLabel = renLabel;
	}
}
