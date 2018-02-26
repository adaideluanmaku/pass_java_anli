package com.ch.redissys;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.PassMysqlconn;
import com.medicom.passlan.inter.imp.shield.ShieldImple;
import com.medicom.passlan.redis.custom.AdultCustom;
import com.medicom.passlan.redis.custom.Bacresis;
import com.medicom.passlan.redis.custom.DoctorPrivCustom;
import com.medicom.passlan.redis.custom.DosageCustom;
import com.medicom.passlan.redis.custom.DrugDisCustom;
import com.medicom.passlan.redis.custom.DrugLevelCustom;
import com.medicom.passlan.redis.custom.HepdosCustom;
import com.medicom.passlan.redis.custom.InterCustom;
import com.medicom.passlan.redis.custom.IvCustom;
import com.medicom.passlan.redis.custom.OperDrugadvice;
import com.medicom.passlan.redis.custom.OperDrugoper;
import com.medicom.passlan.redis.custom.OperationPrivCustom;
import com.medicom.passlan.redis.custom.PediatricCustom;
import com.medicom.passlan.redis.custom.ProDrugReason;
import com.medicom.passlan.redis.custom.RendosCustom;
import com.medicom.passlan.redis.custom.RouteCustom;
import com.medicom.passlan.redis.custom.SexCustom;
import com.medicom.passlan.redis.custom.UnagespCustom;
import com.medicom.passlan.redis.shield.ShieldData;
import com.medicom.passlan.redis.sys.DisDictionary;
import com.medicom.passlan.redis.sys.RouteDictionary;
import com.medicom.passlan.redis.sys.SysAllerGen;
import com.medicom.passlan.redis.sys.SysDictDrug;
import com.medicom.passlan.redis.sys.SysDisease;
import com.medicom.passlan.redis.sys.SysDoctor;
import com.medicom.passlan.redis.sys.SysDrugMatch;
import com.medicom.passlan.redis.sys.SysFrequency;
import com.medicom.passlan.redis.sys.SysHospital;
import com.medicom.passlan.redis.sys.SysMatchRelation;
import com.medicom.passlan.redis.sys.SysOperation;
import com.medicom.passlan.redis.sys.SysParams;
import com.medicom.passlan.redis.sys.SysRouteMatch;

import redis.clients.jedis.Jedis;


public class Redis_query {
	/**
	 * 
	 * @throws IOException 
	 * */
	public String servername;
	
	public void setServername(String servername) {
		this.servername = servername;
	}

	//mc_dict_Doctor,key=hiscode_user
	public List sysDoctorInfaceImpl(int match_scheme,String Doctorcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_doctor-"+match_scheme+"-"+Doctorcode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysDoctor obj = Redis_conf.get("dict_doctor-"+match_scheme+"-"+Doctorcode);
		if(obj==null){
			list.add("dict_doctor,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysDoctor",obj);
		return list;
	}
	
	public List allerGenImpl(int match_scheme,String allercode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_allergen-"+match_scheme+"-"+allercode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysAllerGen obj = Redis_conf.get("dict_allergen-"+match_scheme+"-"+allercode);
		if(obj==null){
			list.add("dict_allergen,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysAllerGen",obj);
		return list;
	}
	
	public List diseaseImpl(int match_scheme,String discode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_disease-"+match_scheme+"-"+discode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysDisease obj = Redis_conf.get("dict_disease-"+match_scheme+"-"+discode);
		if(obj==null){
			list.add("dict_disease,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysDisease",obj);
		return list;
	}
	
	public List distDrugImpl(int match_scheme,String drugcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_drug-single-"+match_scheme+"-"+drugcode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysDictDrug obj = Redis_conf.get("dict_drug-single-"+match_scheme+"-"+drugcode);
		if(obj==null){
			list.add("dict_drug,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysDictDrug",obj);
		return list;
	}
	
	public List drugDoseUnitImpl(int match_scheme,String drugcode,String drugspec,
			String doseunit,String costunit) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("drug_cost_dose_unit-"+match_scheme+"-"+drugcode.trim()
				+"-"+drugspec+"-"+doseunit+"-"+costunit);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		String str = (String)Redis_conf.get("drug_cost_dose_unit-"+match_scheme+"-"+drugcode.trim()
				+"-"+drugspec+"-"+doseunit+"-"+costunit);
		//System.out.println(str);
//		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysDictDrug",obj);
		if(str==null){
			list.add("drug_cost_dose_unit,redis is null");
			return list;
		}
		if("".equals(str)){
			list.add("drug_cost_dose_unit,redis is null");
			return list;
		}
		list.add("conversion="+str);
		return list;
	}
	
	public List frequencyImpl(int match_scheme,String frequency) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_frequency-"+match_scheme+"-"+frequency);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysFrequency obj = Redis_conf.get("dict_frequency-"+match_scheme+"-"+frequency);
		if(obj==null){
			list.add("dict_frequency,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysFrequency",obj);
		return list;
	}
	
	public List operationImpl(int match_scheme,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_operation-"+match_scheme+"-"+operationcode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysOperation obj = Redis_conf.get("dict_operation-"+match_scheme+"-"+operationcode);
		if(obj==null){
			list.add("dict_operation,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysOperation",obj);
		return list;
	}
	
	public List proDrugReasonImpl(String caseid,String recipno,String orderDeptCode,
			String orderDoctorCode,String drugUniqueCode,String moduleId) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("pro_drug_reason-priv-"+caseid.trim()+"-"+
				recipno+"-"+orderDeptCode+"-"+orderDoctorCode
				+"-"+drugUniqueCode+"-"+moduleId);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		ProDrugReason obj = Redis_conf.get("pro_drug_reason-priv-"+caseid.trim()+"-"+
				recipno+"-"+orderDeptCode+"-"+orderDoctorCode
				+"-"+drugUniqueCode+"-"+moduleId);
		if(obj==null){
			list.add("pro_drug_reason,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.ProDrugReason",obj);
		return list;
	}
	
	public List routeMatchImpl(int match_scheme,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("route_pass-"+match_scheme+"-"+routecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysRouteMatch obj = Redis_conf.get("route_pass-"+match_scheme+"-"+routecode);
		if(obj==null){
			list.add("route_pass,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysRouteMatch",obj);
		return list;
	}
	
	public List sysConfigImpl(String paraname) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("mc_config-"+paraname);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		String str = Redis_conf.get("mc_config-"+paraname);
		if(str==null){
			list.add("mc_config,redis is null");
			return list;
		}
		if("".equals(str)){
			list.add("paramvalue="+str);
			return list;
		}
		list.add("paramvalue="+str);
//		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysRouteMatch",obj);
		return list;
	}
	
	public List sysDrugMatchImpl(int match_scheme,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("drug_pass-"+match_scheme+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List<SysDrugMatch> objlist = Redis_conf.get("drug_pass-"+match_scheme+"-"+druguniquecode);
		if(objlist==null){
			list.add("drug_pass,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			SysDrugMatch obj=objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.sys.SysDrugMatch",obj));
		}
		
		return list;
	}
	
	//mc_hospital_match_relation,key=hiscode
	public List sysMatchRelationImpl(String hiscode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("match_relation-"+hiscode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysMatchRelation obj = (SysMatchRelation)Redis_conf.get("match_relation-"+hiscode);
		if(obj==null){
			list.add("match_relation,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysMatchRelation",obj);
		return list;
	}
	
	public List sysParamsImpl(String checkmode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("mc_params-"+checkmode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysParams obj = Redis_conf.get("mc_params-"+checkmode);
		if(obj==null){
			list.add("mc_params,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysParams",obj);
		return list;
	}
	
	//mc_hospital,key=hiscode_user
	public List sysHospitalImpl(String hiscode_user) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("hospital-"+hiscode_user);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SysHospital obj = Redis_conf.get("hospital-"+hiscode_user);
		if(obj==null){
			list.add("hospital,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.SysHospital",obj);
		return list;
	}
	
	public List sysRoutedictionImpl(int match_scheme,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_routediction-"+match_scheme+"-"+routecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		RouteDictionary obj = Redis_conf.get("dict_routediction-"+match_scheme+"-"+routecode);
		if(obj==null){
			list.add("dict_routediction,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.RouteDictionary",obj);
		return list;
	}
	
	public List sysDisDisctionImpl(int match_scheme,String disode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("dict_disdiction-"+match_scheme+"-"+disode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		DisDictionary obj = Redis_conf.get("dict_disdiction-"+match_scheme+"-"+disode);
		if(obj==null){
			list.add("dict_dis,redis is null");
			return list;
		}
		list=Redis_conf.passfields("com.medicom.passlan.redis.sys.DisDictionary",obj);
		return list;
	}
	
	/**
	 * ��ѯ�Զ�������
	 * @throws IOException 
	 * @throws SQLException 
	 * */
	public List adultInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_adult-"+hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List<AdultCustom> objlist = Redis_conf.get("user_adult-"+hiscode+"-"+druguniquecode);
		if(objlist==null){
			list.add("user_adult,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			AdultCustom obj=objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.AdultCustom",obj));
		}
		return list;
	}
	
	public List bacresisInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_bacresis-"+hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List objlist = (List)Redis_conf.get("user_bacresis-"+hiscode+"-"+druguniquecode);
		if(objlist==null){
			list.add("user_bacresis,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			Bacresis obj=(Bacresis) objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.Bacresis",obj));
		}
		return list;
	}
	
	public List brifInfaceImpl(String hiscode,String drugUniqueCode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_brief-"+hiscode+"-"+drugUniqueCode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		String str = Redis_conf.get("user_brief-"+hiscode+"-"+drugUniqueCode);
		if(str==null){
			list.add("user_brief,redis is null");
			return list;
		}
		list.add("content="+str);
		return list;
	}
	
	public List doctorprivInfaceImpl(String hiscode,String Doctorcode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_doctor_priv-"+hiscode+"-"+Doctorcode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		DoctorPrivCustom obj = Redis_conf.get("user_doctor_priv-"+hiscode+"-"+Doctorcode+"-"+druguniquecode);
		if(obj==null){
			list.add("user_doctor_priv,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.DoctorPrivCustom",obj));
		return list;
	}
	
	public List dosageInfaceImpl(String hiscode,String druguniquecode,
			String doseunit,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_dosage-"+hiscode+"-"+
				druguniquecode+"-"+doseunit+"-"+routecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List<DosageCustom> objlist = Redis_conf.get("user_dosage-"+hiscode+"-"+
				druguniquecode+"-"+doseunit+"-"+routecode);
		if(objlist==null){
			list.add("user_dosage,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			DosageCustom obj=objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.DosageCustom",obj));
		}
		return list;
	}
	
	public List drugdisInfaceImpl(int moduleid,String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_drugdis-"+moduleid+"-"+
				hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List<DrugDisCustom> objlist = Redis_conf.get("user_drugdis-"+moduleid+"-"+
				hiscode+"-"+druguniquecode);
		if(objlist==null){
			list.add("user_drugdis,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			DrugDisCustom obj=objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.DrugDisCustom",obj));
		}
		return list;
	}
	
	public List drugLevelInfaceImpl(String hiscode,String druguniquecode,
			String doseunitone,String Druguniquecodetwo,String doseunittwo,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_druglevel-"+hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		DrugLevelCustom obj = Redis_conf.get("user_druglevel-"+hiscode+"-"+druguniquecode);
		if(obj==null){
			list.add("user_druglevel,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.DrugLevelCustom",obj));
		return list;
	}
	
	public List duptherapyInfaceImpl(String dupcid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_duptherapy-"+dupcid);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		String str = Redis_conf.get("user_duptherapy-"+dupcid);
		if(str==null){
			list.add("user_duptherapy,redis is null");
			return list;
		}
		list.add("user_max="+str);
		return list;
	}
	
	public List hepdosInfaceImpl(String hiscode,String druguniquecode,String doseunit,
			String routecode,String heplabel) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_hepdos-"+hiscode+"-"+druguniquecode
				+"-"+doseunit+"-"+routecode+"-"+heplabel);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		HepdosCustom obj = Redis_conf.get("user_hepdos-"+hiscode+"-"+druguniquecode
				+"-"+doseunit+"-"+routecode+"-"+heplabel);
		if(obj==null){
			list.add("user_hepdos,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.HepdosCustom",obj));
		return list;
	}
	
	public List interInfaceImpl(String hiscode,String Druguniquecodeone,String Druguniquecodetwo) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
//		System.out.println("user_inter-"+hiscode+"-"+Druguniquecodeone+"-"+Druguniquecodetwo);
//		System.out.println("����user_inter-"+hiscode+"-"+Druguniquecodetwo+"-"+Druguniquecodeone);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		InterCustom obj = Redis_conf.get("user_inter-"+hiscode+"-"+Druguniquecodeone+"-"+Druguniquecodetwo);
		InterCustom obj1 = Redis_conf.get("user_inter-"+hiscode+"-"+Druguniquecodetwo+"-"+Druguniquecodeone);
		if(obj==null && obj1==null){
			list.add("user_inter,redis is null");
			return list;
		}
		if(obj!=null){
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.InterCustom",obj));
		}
		if(obj1!=null){
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.InterCustom",obj1));
		}
		
		return list;
	}
	
	public List ivInfaceImpl(String hiscode,String druguniquecodes,String routetype) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_iv-"+hiscode+"-"+druguniquecodes+"-"+routetype);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		IvCustom obj = Redis_conf.get("user_iv-"+hiscode+"-"+druguniquecodes+"-"+routetype);
		if(obj==null){
			list.add("user_iv,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.IvCustom",obj));
		return list;
	}
	
	public List operativeAdviceImpl(String caseid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("drug_advice-operative-"+caseid);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		OperDrugadvice obj = Redis_conf.get("drug_advice-operative-"+caseid);
		if(obj==null){
			list.add("drug_advice-operative,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.OperDrugadvice",obj));
		return list;
	}
	
	public List operativeOperImpl(String caseid,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("pro_drug_operation-"+caseid+"-"+operationcode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		OperDrugoper obj = Redis_conf.get("pro_drug_operation-"+caseid+"-"+operationcode);
		if(obj==null){
			list.add("pro_drug_operation,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.OperDrugoper",obj));
		return list;
	}
	
	public List operativeTimeImpl(String caseid,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("pro_drug_operation-datetime-"+caseid+"-"+operationcode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		String str = Redis_conf.get("pro_drug_operation-datetime-"+caseid+"-"+operationcode);
		if(str==null){
			list.add("pro_drug_operation-datetime,redis is null");
			return list;
		}
		if("".equals(str)){
			list.add("drug_starttime="+str);
			return list;
		}
		return list;
	}
	
	public List operprivInfaceImpl(String hiscode,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_operation_priv-"+hiscode+"-"+operationcode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List<OperationPrivCustom> objlist = Redis_conf.get("user_operation_priv-"+hiscode+"-"+operationcode);
		if(objlist==null){
			list.add("user_operation_priv,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			OperationPrivCustom obj=objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.OperationPrivCustom",obj));
		}
		return list;
	}
	
	public List pediatricInfaceImpl(String hiscode,String drug_unique_code) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_pediatric-"+hiscode+"-"+drug_unique_code);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		List<PediatricCustom> objlist = Redis_conf.get("user_pediatric-"+hiscode+"-"+drug_unique_code);
		if(objlist==null){
			list.add("user_pediatric,redis is null");
			return list;
		}
		for(int i=0;i<objlist.size();i++){
			PediatricCustom obj=objlist.get(i);
			list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.PediatricCustom",obj));
		}
		return list;
	}
	
	public List rendosInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_rendos-"+hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		RendosCustom obj = Redis_conf.get("user_rendos-"+hiscode+"-"+druguniquecode);
		if(obj==null){
			list.add("user_rendos,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.RendosCustom",obj));
		return list;
	}
	
	public List routeInfaceImpl(String hiscode,String druguniquecode,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_drugroute-"+hiscode+"-"+druguniquecode+"-"+routecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		RouteCustom obj = Redis_conf.get("user_drugroute-"+hiscode+"-"+druguniquecode+"-"+routecode);
		if(obj==null){
			list.add("user_drugroute,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.RouteCustom",obj));
		return list;
	}
	
	public List sexInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_sex-"+hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		SexCustom obj = Redis_conf.get("user_sex-"+hiscode+"-"+druguniquecode);
		if(obj==null){
			list.add("user_sex,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.SexCustom",obj));
		return list;
	}
	
	public List unagespInfaceImpl(int moduleid,String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		System.out.println("user_unagesp-"+moduleid+"-"+hiscode+"-"+druguniquecode);
		List list=new ArrayList();
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		UnagespCustom obj = Redis_conf.get("user_unagesp-"+moduleid+"-"+hiscode+"-"+druguniquecode);
		if(obj==null){
			list.add("user_unagesp,redis is null");
			return list;
		}
		list.add(Redis_conf.passfields("com.medicom.passlan.redis.custom.UnagespCustom",obj));
		return list;
	}
	
	/**
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 * */
	public List shieldImple(int moduleid,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, SQLException{
		
		Redis_conf Redis_conf =new Redis_conf(); 
		Redis_conf.setServername(servername);
		Jedis jedis = Redis_conf.redisconnect();
		List list=new ArrayList();
		if(jedis==null){
			list.add("shieldImple,redis is null");
			return list;
		}
		Set<String> keys=jedis.keys("*user_shielddata-*");//KEY
		Iterator<String> keyit=keys.iterator() ;  
		List<ShieldData> listobject = new ArrayList<ShieldData>();
		while(keyit.hasNext()){   
			String key = keyit.next();  
//			System.out.println(key);//dict_doctor-36-630602
			key=key.substring(2);
//			System.out.println(key);
			String[] str=key.split("-");
			for(int i=0;i<str.length;i++){
				if(moduleid==Integer.parseInt(str[1].toString()) && druguniquecode.equals(str[i])){
					listobject= (List<ShieldData>) Redis_conf.get(key);
					if(listobject==null){
						continue;
					}
					for(int i1=0;i1<listobject.size();i1++){
						list.add(Redis_conf.passfields("com.medicom.passlan.redis.shield.ShieldData",listobject.get(i1)));
					}
				}
			}
		}
		if(list.size()==0){
			list.add("shieldImple,redis is null");
		}
		return list;
	}
}
