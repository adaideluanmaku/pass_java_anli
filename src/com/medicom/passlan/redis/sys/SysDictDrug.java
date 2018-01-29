package com.medicom.passlan.redis.sys;

/**
 * 
 * <ul>
 * <li>项目名称：RedisInit </li>
 * <li>类名称：  SysDictDrug </li>
 * <li>类描述：  用户药品基本信息表的数据 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年4月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class SysDictDrug implements java.io.Serializable{
	
	private int matchscheme;
	
	private String drugcode; 
	
	private String drugform;
	
	private Integer isbasedrug; 
	
	private Integer isbbasedrugp;
	
	private Integer isanti;
	
	private Integer antitype;
	
	private Integer antilevel;
	
	private Integer drugtype;
	
	private Integer drugformtype; 
	
	private String socialsecurityRatio;
	
	private Integer issocialSecurity;
	
	private String socialsecurityDesc;
	
	private Integer jdmtype;
	
	private Integer isstimulant;
	
	private String stimulantingred;
	
	private Integer issolvent;
	
	private Integer issrpreparations;
	
	private Integer isneedskintest;
	
	private Integer isdearmed;
	
	private Integer ispoison;
	
	private Integer isbloodmed;
	
	private Integer issugarmed;
	
	private Integer otctype;
	
	private Integer isbisectionUse;
	
	private Integer highAlertLevel;

	public int getMatchscheme() {
		return matchscheme;
	}

	public void setMatchscheme(int matchscheme) {
		this.matchscheme = matchscheme;
	}

	public String getDrugcode() {
		return drugcode;
	}

	public void setDrugcode(String drugcode) {
		this.drugcode = drugcode;
	}

	public String getDrugform() {
		return drugform;
	}

	public void setDrugform(String drugform) {
		this.drugform = drugform;
	}

	public Integer getIsbasedrug() {
		return isbasedrug;
	}

	public void setIsbasedrug(Integer isbasedrug) {
		this.isbasedrug = isbasedrug;
	}

	public Integer getIsbbasedrugp() {
		return isbbasedrugp;
	}

	public void setIsbbasedrugp(Integer isbbasedrugp) {
		this.isbbasedrugp = isbbasedrugp;
	}

	public Integer getIsanti() {
		return isanti;
	}

	public void setIsanti(Integer isanti) {
		this.isanti = isanti;
	}

	public Integer getAntitype() {
		return antitype;
	}

	public void setAntitype(Integer antitype) {
		this.antitype = antitype;
	}

	public Integer getAntilevel() {
		return antilevel;
	}

	public void setAntilevel(Integer antilevel) {
		this.antilevel = antilevel;
	}

	public Integer getDrugtype() {
		return drugtype;
	}

	public void setDrugtype(Integer drugtype) {
		this.drugtype = drugtype;
	}

	public Integer getDrugformtype() {
		return drugformtype;
	}

	public void setDrugformtype(Integer drugformtype) {
		this.drugformtype = drugformtype;
	}

	public String getSocialsecurityRatio() {
		return socialsecurityRatio;
	}

	public void setSocialsecurityRatio(String socialsecurityRatio) {
		this.socialsecurityRatio = socialsecurityRatio;
	}

	public Integer getIssocialSecurity() {
		return issocialSecurity;
	}

	public void setIssocialSecurity(Integer issocialSecurity) {
		this.issocialSecurity = issocialSecurity;
	}

	public String getSocialsecurityDesc() {
		return socialsecurityDesc;
	}

	public void setSocialsecurityDesc(String socialsecurityDesc) {
		this.socialsecurityDesc = socialsecurityDesc;
	}

	public Integer getJdmtype() {
		return jdmtype;
	}

	public void setJdmtype(Integer jdmtype) {
		this.jdmtype = jdmtype;
	}

	public Integer getIsstimulant() {
		return isstimulant;
	}

	public void setIsstimulant(Integer isstimulant) {
		this.isstimulant = isstimulant;
	}

	public String getStimulantingred() {
		return stimulantingred;
	}

	public void setStimulantingred(String stimulantingred) {
		this.stimulantingred = stimulantingred;
	}

	public Integer getIssolvent() {
		return issolvent;
	}

	public void setIssolvent(Integer issolvent) {
		this.issolvent = issolvent;
	}

	public Integer getIssrpreparations() {
		return issrpreparations;
	}

	public void setIssrpreparations(Integer issrpreparations) {
		this.issrpreparations = issrpreparations;
	}

	public Integer getIsneedskintest() {
		return isneedskintest;
	}

	public void setIsneedskintest(Integer isneedskintest) {
		this.isneedskintest = isneedskintest;
	}

	public Integer getIsdearmed() {
		return isdearmed;
	}

	public void setIsdearmed(Integer isdearmed) {
		this.isdearmed = isdearmed;
	}

	public Integer getIspoison() {
		return ispoison;
	}

	public void setIspoison(Integer ispoison) {
		this.ispoison = ispoison;
	}

	public Integer getIsbloodmed() {
		return isbloodmed;
	}

	public void setIsbloodmed(Integer isbloodmed) {
		this.isbloodmed = isbloodmed;
	}

	public Integer getIssugarmed() {
		return issugarmed;
	}

	public void setIssugarmed(Integer issugarmed) {
		this.issugarmed = issugarmed;
	}

	public Integer getOtctype() {
		return otctype;
	}

	public void setOtctype(Integer otctype) {
		this.otctype = otctype;
	}

	public Integer getIsbisectionUse() {
		return isbisectionUse;
	}

	public void setIsbisectionUse(Integer isbisectionUse) {
		this.isbisectionUse = isbisectionUse;
	}

	public Integer getHighAlertLevel() {
		return highAlertLevel;
	}

	public void setHighAlertLevel(Integer highAlertLevel) {
		this.highAlertLevel = highAlertLevel;
	}
	
}
