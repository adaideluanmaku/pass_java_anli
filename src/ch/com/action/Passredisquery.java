package ch.com.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ch.redissys.Redis_conf;
import com.ch.redissys.Redis_query;

public class Passredisquery {
	List list=null;
	String str=null;
	public int req;
	public int match_scheme;
	public String allercode;
	public String drugcode;
	public String discode;
	public String frequency;
	public String hiscode_user;
	public String operationcode;
	public String caseid;
	public String recipno;
	public String orderDeptCode;
	public String orderDoctorCode;
	public String moduleId;
	public String routecode;
	public String disode;
	public String Doctorcode;
	public String druguniquecode;
	public String hiscode;
	public String checkmode;
	public String drugspec;
	public String doseunit;
	public String costunit;
	public String paraname;
	public int moduleid;
	public String dupcid;
	public String heplabel;
	public String Druguniquecodeone;
	public String Druguniquecodetwo;
	public String routetype;
	public int tabletype;
	public String doseunitone;
	public String doseunittwo;
	public List iplist;
	public String servername;
	
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public List getIplist() {
		return iplist;
	}

	public String getDoseunitone() {
		return doseunitone;
	}

	public void setDoseunitone(String doseunitone) {
		this.doseunitone = doseunitone;
	}

	public String getDoseunittwo() {
		return doseunittwo;
	}

	public void setDoseunittwo(String doseunittwo) {
		this.doseunittwo = doseunittwo;
	}

	public String getRoutetype() {
		return routetype;
	}

	public void setRoutetype(String routetype) {
		this.routetype = routetype;
	}

	public int getTabletype() {
		return tabletype;
	}

	public void setTabletype(int tabletype) {
		this.tabletype = tabletype;
	}

	public int getModuleid() {
		return moduleid;
	}

	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
	}

	public String getDupcid() {
		return dupcid;
	}

	public void setDupcid(String dupcid) {
		this.dupcid = dupcid;
	}

	public String getHeplabel() {
		return heplabel;
	}

	public void setHeplabel(String heplabel) {
		this.heplabel = heplabel;
	}

	public String getDruguniquecodeone() {
		return Druguniquecodeone;
	}

	public void setDruguniquecodeone(String druguniquecodeone) {
		Druguniquecodeone = druguniquecodeone;
	}

	public String getDruguniquecodetwo() {
		return Druguniquecodetwo;
	}

	public void setDruguniquecodetwo(String druguniquecodetwo) {
		Druguniquecodetwo = druguniquecodetwo;
	}

	public List getList() {
		return list;
	}

	public String getStr() {
		return str;
	}

	public int getReq() {
		return req;
	}

	public void setReq(int req) {
		this.req = req;
	}

	public int getMatch_scheme() {
		return match_scheme;
	}

	public void setMatch_scheme(int match_scheme) {
		this.match_scheme = match_scheme;
	}

	public String getAllercode() {
		return allercode;
	}

	public void setAllercode(String allercode) {
		this.allercode = allercode;
	}

	public String getDrugcode() {
		return drugcode;
	}

	public void setDrugcode(String drugcode) {
		this.drugcode = drugcode;
	}

	public String getDiscode() {
		return discode;
	}

	public void setDiscode(String discode) {
		this.discode = discode;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getHiscode_user() {
		return hiscode_user;
	}

	public void setHiscode_user(String hiscode_user) {
		this.hiscode_user = hiscode_user;
	}

	public String getOperationcode() {
		return operationcode;
	}

	public void setOperationcode(String operationcode) {
		this.operationcode = operationcode;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getRecipno() {
		return recipno;
	}

	public void setRecipno(String recipno) {
		this.recipno = recipno;
	}

	public String getOrderDeptCode() {
		return orderDeptCode;
	}

	public void setOrderDeptCode(String orderDeptCode) {
		this.orderDeptCode = orderDeptCode;
	}

	public String getOrderDoctorCode() {
		return orderDoctorCode;
	}

	public void setOrderDoctorCode(String orderDoctorCode) {
		this.orderDoctorCode = orderDoctorCode;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(String routecode) {
		this.routecode = routecode;
	}

	public String getDisode() {
		return disode;
	}

	public void setDisode(String disode) {
		this.disode = disode;
	}

	public String getDoctorcode() {
		return Doctorcode;
	}

	public void setDoctorcode(String doctorcode) {
		Doctorcode = doctorcode;
	}

	public String getDruguniquecode() {
		return druguniquecode;
	}

	public void setDruguniquecode(String druguniquecode) {
		this.druguniquecode = druguniquecode;
	}

	public String getHiscode() {
		return hiscode;
	}

	public void setHiscode(String hiscode) {
		this.hiscode = hiscode;
	}

	public String getCheckmode() {
		return checkmode;
	}

	public void setCheckmode(String checkmode) {
		this.checkmode = checkmode;
	}

	public String getDrugspec() {
		return drugspec;
	}

	public void setDrugspec(String drugspec) {
		this.drugspec = drugspec;
	}

	public String getDoseunit() {
		return doseunit;
	}

	public void setDoseunit(String doseunit) {
		this.doseunit = doseunit;
	}

	public String getCostunit() {
		return costunit;
	}

	public void setCostunit(String costunit) {
		this.costunit = costunit;
	}

	public String getParaname() {
		return paraname;
	}

	public void setParaname(String paraname) {
		this.paraname = paraname;
	}

	public String execute() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		//��ȡ�����������ַ
		ServerIP ServerIP=new ServerIP();
		ServerIP.getshuju();
		iplist=ServerIP.getList();
		
		if(req==999){
			return "success";
		}
//		Redis_conf Redis_conf=new Redis_conf();
//		Redis_conf.setServername(servername);
//		Redis_conf.redisconnect();
		Redis_query Redis_sys=new Redis_query();
		Redis_sys.setServername(servername);
//		list=new ArrayList();
//		System.out.println(tabletype);
		//�ֵ��
		if(req==1){
			list=Redis_sys.allerGenImpl(match_scheme, allercode);
			System.out.println(list);
		}
		if(req==2){
			list=Redis_sys.distDrugImpl(match_scheme, drugcode);
			System.out.println(list);
		}
		if(req==3){
			list=Redis_sys.diseaseImpl(match_scheme, discode);
			System.out.println(list);
		}
		if(req==4){
			list=Redis_sys.drugDoseUnitImpl(match_scheme, drugcode, drugspec, doseunit, costunit);
			System.out.println(list);
		}
		
		if(req==5){
			list=Redis_sys.frequencyImpl(match_scheme, frequency);
			System.out.println(list);
		}
		if(req==6){
			list=Redis_sys.sysHospitalImpl(hiscode_user);
			System.out.println(list);
		}
		
		if(req==7){
			list=Redis_sys.operationImpl(match_scheme, operationcode);
			System.out.println(list);
		}
		if(req==8){
			list=Redis_sys.proDrugReasonImpl(caseid, recipno, orderDeptCode, orderDoctorCode, druguniquecode, moduleId);
			System.out.println(list);
		}
		if(req==9){
			list=Redis_sys.routeMatchImpl(match_scheme, routecode);
			System.out.println(list);
		}
		
		if(req==10){
			list=Redis_sys.sysConfigImpl(paraname);
			System.out.println(list);
		}
		if(req==11){
			list=Redis_sys.sysDisDisctionImpl(match_scheme, disode);
			System.out.println(list);
		}
		if(req==12){
			list=Redis_sys.sysDoctorInfaceImpl(match_scheme, Doctorcode);
			System.out.println(list);
		}
		if(req==13){
			list=Redis_sys.sysDrugMatchImpl(match_scheme, druguniquecode);
			System.out.println(list);
		}
		if(req==14){
			list=Redis_sys.sysMatchRelationImpl(hiscode);
			System.out.println(list);
		}
		if(req==15){
			list=Redis_sys.sysParamsImpl(checkmode);
			System.out.println(list);
		}
		if(req==16){
			list=Redis_sys.sysRoutedictionImpl(match_scheme, routecode);
			System.out.println(list);
		}
		
		//�Զ����ѯ
		if(req==17){
			list=Redis_sys.adultInfaceImpl(hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==18){
			list=Redis_sys.bacresisInfaceImpl(hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==19){
			list=Redis_sys.brifInfaceImpl(hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==20){
			list=Redis_sys.doctorprivInfaceImpl(hiscode,Doctorcode,druguniquecode);
			System.out.println(list);
		}
		if(req==21){
			list=Redis_sys.dosageInfaceImpl(hiscode,druguniquecode,
					doseunit,routecode);
			System.out.println(list);
		}
		if(req==22){
			list=Redis_sys.drugdisInfaceImpl(moduleid,hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==23){
			list=Redis_sys.drugLevelInfaceImpl(hiscode,Druguniquecodeone,doseunitone,Druguniquecodetwo,
					doseunittwo,routecode);
			System.out.println(list);
		}
		if(req==24){
			list=Redis_sys.duptherapyInfaceImpl(dupcid);
			System.out.println(list);
		}
		if(req==25){
			list=Redis_sys.hepdosInfaceImpl(hiscode,druguniquecode,doseunit,
					routecode,heplabel);
			System.out.println(list);
		}
		if(req==26){
			list=Redis_sys.interInfaceImpl(hiscode,Druguniquecodeone,Druguniquecodetwo);
			System.out.println(list);
		}
		if(req==27){
			list=Redis_sys.ivInfaceImpl(hiscode,druguniquecode,routetype);
			System.out.println(list);
		}
		if(req==28){
			list=Redis_sys.operativeAdviceImpl(caseid);
			System.out.println(list);
		}
		if(req==29){
			list=Redis_sys.operativeOperImpl(caseid,operationcode);
			System.out.println(list);
		}
		if(req==30){
			list=Redis_sys.operativeTimeImpl(caseid,operationcode);
			System.out.println(list);
		}
		if(req==31){
			list=Redis_sys.operprivInfaceImpl(hiscode,operationcode);
			System.out.println(list);
		}
		if(req==32){
			list=Redis_sys.pediatricInfaceImpl(hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==33){
			list=Redis_sys.rendosInfaceImpl(hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==34){
			list=Redis_sys.routeInfaceImpl(hiscode,druguniquecode,routecode);
			System.out.println(list);
		}
		if(req==35){
			list=Redis_sys.sexInfaceImpl(hiscode,druguniquecode);
			System.out.println(list);
		}
		if(req==36){
			list=Redis_sys.unagespInfaceImpl(moduleid,hiscode,druguniquecode);
			System.out.println(list);
		}
		
		//���α��ѯ
		if(req==37){
			list=Redis_sys.shieldImple(moduleid,druguniquecode);
			System.out.println(list);
		}
//		System.out.println("---"+list+":"+str+"---");
		return "success";
	}
}
