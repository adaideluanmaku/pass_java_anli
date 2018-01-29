package com.ch.redissys;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.PassMysqlconn;
import com.medicom.passlan.inter.ComonInfaceImpl;
import com.medicom.passlan.inter.imp.custom.AdultInfaceImpl;
import com.medicom.passlan.inter.imp.custom.BacresisInfaceImpl;
import com.medicom.passlan.inter.imp.custom.BrifInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DoctorprivInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DosageInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DrugLevelInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DrugdisInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DuptherapyInfaceImpl;
import com.medicom.passlan.inter.imp.custom.HepdosInfaceImpl;
import com.medicom.passlan.inter.imp.custom.InterInfaceImpl;
import com.medicom.passlan.inter.imp.custom.IvInfaceImpl;
import com.medicom.passlan.inter.imp.custom.OperativeAdviceImpl;
import com.medicom.passlan.inter.imp.custom.OperativeOperImpl;
import com.medicom.passlan.inter.imp.custom.OperativeTimeImpl;
import com.medicom.passlan.inter.imp.custom.OperprivInfaceImpl;
import com.medicom.passlan.inter.imp.custom.PediatricInfaceImpl;
import com.medicom.passlan.inter.imp.custom.RendosInfaceImpl;
import com.medicom.passlan.inter.imp.custom.RouteInfaceImpl;
import com.medicom.passlan.inter.imp.custom.SexInfaceImpl;
import com.medicom.passlan.inter.imp.custom.SysDoctorInfaceImpl;
import com.medicom.passlan.inter.imp.custom.UnagespInfaceImpl;
import com.medicom.passlan.inter.imp.sys.AllerGenImpl;
import com.medicom.passlan.inter.imp.sys.DiseaseImpl;
import com.medicom.passlan.inter.imp.sys.DistDrugImpl;
import com.medicom.passlan.inter.imp.sys.DrugDoseUnitImpl;
import com.medicom.passlan.inter.imp.sys.FrequencyImpl;
import com.medicom.passlan.inter.imp.sys.OperationImpl;
import com.medicom.passlan.inter.imp.sys.ProDrugReasonImpl;
import com.medicom.passlan.inter.imp.sys.RouteMatchImpl;
import com.medicom.passlan.inter.imp.sys.SysConfigImpl;
import com.medicom.passlan.inter.imp.sys.SysDisDisctionImpl;
import com.medicom.passlan.inter.imp.sys.SysDrugMatchImpl;
import com.medicom.passlan.inter.imp.sys.SysHospitalImpl;
import com.medicom.passlan.inter.imp.sys.SysMatchRelationImpl;
import com.medicom.passlan.inter.imp.sys.SysParamsImpl;
import com.medicom.passlan.inter.imp.sys.SysRoutedictionImpl;
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
import com.medicom.passlan.redis.shield.SheildEnum;
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
import com.medicom.passlan.inter.imp.shield.ShieldImple;
public class Redis_update {
	/**
	 * 更新系统表数据
	 * @throws SQLException 
	 * @throws IOException 
	 * */
	//mc_dict_Doctor,key=hiscode_user
	public boolean sysDoctorInfaceImpl(int match_scheme,String doctorcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct match_scheme,doctorcode, doctorname, prespriv, ilevel,antilevel from mc_dict_doctor where  match_scheme=? and doctorcode=?  order by match_scheme, doctorcode";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, doctorcode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		SysDoctorInfaceImpl SysDoctorInfaceImpl=new SysDoctorInfaceImpl();
		String key  = "";
		if(list!=null&&list.size()>0){
			SysDoctor doctor = SysDoctorInfaceImpl.copyFromMap(list.get(0));
			key = "dict_doctor-"+doctor.getMatchscheme()+"-"+doctor.getDoctorcode().trim();
			return SysDoctorInfaceImpl.addOrUpdate(doctor, key);
		}else{
			return false;
		}
	}
	
	public boolean allerGenImpl(Integer match_scheme,String allercode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select a.match_scheme,a.allercode as u_code, a.allername as u_name, b.allerid,"
			+ " b.allername, b.allertype from mc_dict_allergen a, mc_allergen_view b "
			+ "where a.pass_allerid is not null and a.pass_allerid > 0 "
			+ "and a.pass_allerid = b.allerid  and a.match_scheme=?  and a.allercode=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, allercode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		AllerGenImpl AllerGenImpl=new AllerGenImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysAllerGen priv = AllerGenImpl.copyFromMap(list.get(0));
				AllerGenImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			String key = "dict_allergen-"+match_scheme+"-"+ allercode;
			return AllerGenImpl.delete(key);
		}
	}
	
	public boolean diseaseImpl(Integer match_scheme,String discode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql=" select a.match_scheme, a.discode as u_code, a.disname as u_name," 
			+"b.icd_code,b.renal, b.hepatic, b.cardio, b.pulm, b.neur, b.commonindex, "
			+"b.endo, b.seqnum, b.preg_label, b.lact_label, b.hep_label, b.ren_label "
			+"from mc_dict_disease a, mc_icd_view b  "
			+"where a.pass_icd_code = b.icd_code and a.pass_icd_code is not null  and a.match_scheme=?  and a.discode=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, discode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();

		DiseaseImpl DiseaseImpl=new DiseaseImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysDisease priv = DiseaseImpl.copyFromMap(list.get(0));
				DiseaseImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			String key = "dict_disease-"+match_scheme+"-"+ discode.trim();
			return DiseaseImpl.delete(key);
		}
	}
	
	public boolean distDrugImpl(int match_scheme,String drugcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql=" SELECT a.match_scheme, a.drugcode, a.drugform,"
			+ " a.is_basedrug, a.is_basedrug_p"
			+",a.is_anti,a.antitype,a.antilevel,a.drugtype,a.drugformtype " 
			+" ,a.socialsecurity_ratio,a.is_socialsecurity "
			+",a.socialsecurity_desc,a.jdmtype,a.is_stimulant "
			+",a.stimulantingred,a.is_solvent,a.is_srpreparations "
			+",a.is_needskintest,a.is_dearmed,a.is_poison,a.is_bloodmed "
			+",a.is_sugarmed,a.otctype,a.is_bisection_use,a.high_alert_level FROM mc_dict_drug a "
			+" where a.match_scheme=? and  a.drugcode=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, drugcode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		DistDrugImpl DistDrugImpl=new DistDrugImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysDictDrug priv = DistDrugImpl.copyFromMap(list.get(0));
				DistDrugImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean drugDoseUnitImpl(int match_scheme,String drugcode,String drugspec,
		String doseunit,String costunit, String conversion) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		String key  = 	"drug_cost_dose_unit-"+match_scheme+"-"+drugcode.trim()
				+"-"+drugspec+"-"+doseunit+"-"+costunit;
		
		DrugDoseUnitImpl DrugDoseUnitImpl=new DrugDoseUnitImpl();
		return DrugDoseUnitImpl.addOrUpdate(key, conversion);
	}
	
	public boolean frequencyImpl(Integer match_scheme,String frequency) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct match_scheme,frequency, times,days from mc_dict_frequency "
				+ "where times is not null and times > 0 and match_scheme=?  and frequency=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, frequency);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		FrequencyImpl FrequencyImpl=new FrequencyImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysFrequency priv = FrequencyImpl.copyFromMap(list.get(0));
				FrequencyImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			String key  = "dict_frequency-"+match_scheme+"-"+ frequency.trim();
			FrequencyImpl.delete(key);
			return false;
		}
	}
	
	public boolean operationImpl(Integer match_scheme,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select match_scheme,operationcode, operationname, "
	        + " useanti, drugtime, " 
	        +" (case when premoment_low is null then -1.0 " 
	        +" else premoment_low end) as premoment_low, "
	        +" (case when premoment_high is null then -1.0 "
	        +" else premoment_high end) as premoment_high " 
	        +" from mc_dict_operation "
	        +" where (useanti>-1 or premoment_low>0 or premoment_high>0 or drugtime>0) "
	        + "and match_scheme=?  and operationcode=?  order by operationcode ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, operationcode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		OperationImpl OperationImpl=new OperationImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysOperation priv = OperationImpl.copyFromMap(list.get(0));
				OperationImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean proDrugReasonImpl(String caseid,String recipno,String orderDeptCode,
			String orderDoctorCode,String druguniquecode,String moduleId) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,hiscode,hisname,patstatus,inhospno,caseid,"
			+" visitcode,patcode,recipno,order_deptcode,order_doctorcode,order_starttime,order_endtime,"
			+" order_executetime,drug_unique_code,doseunit,dosepertime,frequency,istempdrug,"
			+" moduleid,modulename,slcode,severity,warning,moduleitems,otherinfo,is_forstatic,reason "
			+ "from mc_pro_drug_reason where reason <> ' '  and caseid=? and recipno=? and order_deptcode=? "
			+ "and order_doctorcode=? and drug_unique_code=? and moduleid=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, caseid);
		pst.setString(2, recipno);
		pst.setString(3, orderDeptCode);
		pst.setString(4, orderDoctorCode);
		pst.setString(5, druguniquecode);
		pst.setString(6, moduleId);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
				
		List<ProDrugReason> listobject = new ArrayList<ProDrugReason>();
		String key  = "pro_drug_reason-priv-"+caseid.trim()+"-"+
				recipno+"-"+orderDeptCode+"-"+orderDoctorCode
				+"-"+druguniquecode+"-"+moduleId;
		
		ProDrugReasonImpl ProDrugReasonImpl=new ProDrugReasonImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				ProDrugReason custom = ProDrugReasonImpl.copyFromMap(list.get(i));
				listobject.add(custom);
			}
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return ProDrugReasonImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean routeMatchImpl(Integer match_scheme,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql=" select a.match_scheme,a.routecode as u_code, a.routename as u_name," 
			+"b.routeid, b.st_routeid, b.route_name,b.routelabel from mc_dict_route a, mc_route_name_view b "
			+" where a.pass_routeid is not null and a.pass_routeid > 0 and a.pass_routeid = b.routeid"
			+ " and a.match_scheme=?  and a.routecode=?  ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, routecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
			
		RouteMatchImpl RouteMatchImpl=new RouteMatchImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysRouteMatch priv = RouteMatchImpl.copyFromMap(list.get(0));
				RouteMatchImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			String key = "route_pass-"+match_scheme+"-"+ StringUtils.trim(routecode);
			return RouteMatchImpl.delete(key);
		}
	}
	
	public boolean sysConfigImpl(String paraname,String paramvalue) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		if(StringUtils.isNotBlank(paraname)&&StringUtils.isNotBlank(paramvalue)){
			String key  = 	"mc_config-"+paraname.trim();
			SysConfigImpl SysConfigImpl=new SysConfigImpl();
			return SysConfigImpl.addOrUpdate(key, paramvalue);
		}else{
			return false;
		}
	}
	
	public boolean sysDrugMatchImpl(int match_scheme,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql=" SELECT a.match_scheme, a.drug_unique_code as u_unique_code,"
			+ "a.drugcode as u_code, a.drugname as u_name,a.doseunit as u_doseunit," 
			+ "a.drugform as u_form, a.drugspec as u_strength, "
			+ "a.comp_name as u_comp_name,a.approvalcode as u_approvalcode,"
			+ " a.pass_dividend, a.pass_divisor, a.pass_doseunit as doseunit,a.pass_drugcode as drugcode "
			+ " FROM mc_dict_drug_pass a  where a.match_scheme=? and  a.drug_unique_code=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		List<SysDrugMatch> listobject = new ArrayList<SysDrugMatch>();
		SysDrugMatchImpl SysDrugMatchImpl=new SysDrugMatchImpl();
		String key ="drug_pass-"+String.valueOf(match_scheme)+"-"+String.valueOf(druguniquecode.trim());
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysDrugMatch custom = SysDrugMatchImpl.copyFromMap(list.get(i));
				listobject.add(custom);
			}
		}else{
			return false;
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return SysDrugMatchImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	//mc_hospital_match_relation,key=hiscode
	public boolean sysMatchRelationImpl(String hiscode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select hiscode,hisname,searchcode,drugmatch_scheme,allermatch_scheme,"
			+ "dismatch_scheme,freqmatch_scheme,routematch_scheme,doctormatch_scheme,"
			+ "oprmatch_scheme,mhiscode,costitemmatch_scheme,deptmatch_scheme,exammatch_scheme,"
			+ "labmatch_scheme,labsubmatch_scheme,doctorgroupmatch_scheme,wardmatch_scheme "
			+ "from mc_hospital_match_relation  where hiscode=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		SysMatchRelationImpl SysMatchRelationImpl=new SysMatchRelationImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysMatchRelation priv = SysMatchRelationImpl.copyFromMap(list.get(0));
				SysMatchRelationImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean sysParamsImpl(String checkmode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,checkmode,checkmode_name,scr_filters"
					+ ",is_save,is_scroutdrug,is_scr_exactlying,is_scr_dupuniquecode,is_useim "
					+ "from mc_params  where checkmode=?  order by checkmode";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, checkmode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		SysParamsImpl SysParamsImpl=new SysParamsImpl();
		if(list!=null&&list.size()>0){
			SysParams priv = SysParamsImpl.copyFromMap(list.get(0));
			return SysParamsImpl.addOrUpdate(priv);
		}else{
			return false;
		}
	}
	
	//mc_hospital,key=hiscode_user
	public boolean sysHospitalImpl(String hiscode_user) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select hiscode,hiscode_user,stru_type,hospname,hiscode_p from mc_hospital "
				+ " where hiscode_user=? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode_user);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		SysHospitalImpl SysHospitalImpl=new SysHospitalImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysHospital priv = SysHospitalImpl.copyFromMap(list.get(0));
				SysHospitalImpl.addOrUpdate(priv);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean sysRoutedictionImpl(int match_scheme,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct match_scheme,routecode, routename, route_type,isskintest "
				+ "from mc_dict_route where (route_type is not null and route_type <> '') "
				+ " and match_scheme=? and routecode =?  ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, routecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		SysRoutedictionImpl SysRoutedictionImpl=new SysRoutedictionImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> object = list.get(i);
				//String key = DICTIONARY_TITLE+matchshceme+"-"+routecode;
				RouteDictionary t = SysRoutedictionImpl.copyFromMap(object);
				return SysRoutedictionImpl.addOrUpdate(t);
			}
		}else{
			if(routecode!=null&&StringUtils.isNotBlank(routecode)){
				String key  = "dict_routediction-"+match_scheme+"-"+routecode;
				return SysRoutedictionImpl.delete(key);
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean sysDisDisctionImpl(int match_scheme,String discode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct match_scheme,discode, disname, Dis_type from mc_dict_disease"
				+ " where match_scheme=? and discode =? ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, match_scheme);
		pst.setString(2, discode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
				
		SysDisDisctionImpl SysDisDisctionImpl=new SysDisDisctionImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> object = list.get(i);
				//String key = DICTIONARY_TITLE+matchshceme+"-"+routecode;
				DisDictionary t = SysDisDisctionImpl.copyFromMap(object);
				return SysDisDisctionImpl.addOrUpdate(t);
			}
		}else{
			if(discode!=null&&StringUtils.isNotBlank(discode)){
				String key  = "dict_disdiction-"+match_scheme+"-"+discode;
				return SysDisDisctionImpl.delete(key);
			}else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 更新自定义数据
	 * @throws IOException 
	 * @throws SQLException 
	 * */
	public boolean adultInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,hiscode,drug_unique_code,drugname,agelow,agelow_unit,"
			+ "unequal_low,agehigh,agehigh_unit,unequal_high,agedesc,slcode,severity,warning "
			+ "from mc_user_adult where drug_unique_code <> '' and drug_unique_code is not null "
			+ " and hiscode=? and  drug_unique_code=?  order by drug_unique_code ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		AdultInfaceImpl AdultInfaceImpl=new AdultInfaceImpl();
		List<AdultCustom> listobject = new ArrayList<AdultCustom>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AdultCustom custom = AdultInfaceImpl.copyFromMap(list.get(i));
				key = "user_adult-"+custom.getHiscode()+"-"+custom.getDruguniquecode();
				listobject.add(custom);
			}
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return AdultInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean bacresisInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct a.hiscode,b.drug_unique_code,b.drugname,a.whodrugcode,a.whodrugname,"
				+ "a.bacterianame,a.resistrate from mc_user_bacresis a, mc_user_match_whonet b  "
				+ "where a.hiscode=b.hiscode and a.whodrugcode=b.whodrugcode "
				+ " and a.hiscode=? and b.drug_unique_code=?  order by a.hiscode, b.drug_unique_code, resistrate desc";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		BacresisInfaceImpl BacresisInfaceImpl=new BacresisInfaceImpl();
		List<Bacresis> listobject = new ArrayList<Bacresis>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Bacresis custom = BacresisInfaceImpl.copyFromMap(list.get(i));
				key = "user_bacresis-"+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim();
				listobject.add(custom);
			}
		}
		if(key!=""){
			return BacresisInfaceImpl.addListCommon(listobject, key);
		}else{
			//杩欓噷鍒犻櫎 key鍊�
			key = "user_bacresis-"+hiscode.trim()+"-"+druguniquecode.trim();
			return BacresisInfaceImpl.delete(key);
			//return false;
		}
	}
	
	public boolean brifInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="SELECT hiscode, drug_unique_code, content FROM mc_user_brief "
				+ " WHERE hiscode=? AND drug_unique_code=?  ORDER BY drug_unique_code";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		BrifInfaceImpl BrifInfaceImpl=new BrifInfaceImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> object = list.get(i);
				String key = "user_brief-"+object.get("hiscode").toString().trim()+"-"+object.get("drug_unique_code").toString().trim();
				return BrifInfaceImpl.addOrUpdate(key, object.get("content").toString());
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean doctorprivInfaceImpl(String hiscode,String doctorcode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct hiscode, doctorcode, drug_unique_code, slcode, severity, warning "
				+ "from mc_user_doctor_priv  where  hiscode =? and doctorcode=? and drug_unique_code=? "
				+ " order by hiscode, doctorcode,drug_unique_code";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, doctorcode);
		pst.setString(3, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		DoctorprivInfaceImpl DoctorprivInfaceImpl=new DoctorprivInfaceImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DoctorPrivCustom t = DoctorprivInfaceImpl.copyFromMap(list.get(i));
				String key = "user_doctor_priv-"+t.getHiscode().trim()+"-"+t.getDoctorcode()+"-"+t.getDruguniquecode().trim();
				return DoctorprivInfaceImpl.addOrUpdateCommon(t, key);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean dosageInfaceImpl(String hiscode,String druguniquecode,
			String doseunit,String routecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,hiscode,drug_unique_code,"
			+ "drugname,doseunit,routecode,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,agedesc,"
			+ "calculate_label,dose_each_low,dose_each_low_unit"
			+ ",dose_each_high,dose_each_high_unit,maxdose_each,maxdose_each_unit"
			+ ",frequency_low,frequency_low_day,frequency_high,frequency_high_day"
			+ ",dose_day_low,dose_day_low_unit,dose_day_high,dose_day_high_unit,"
			+ "maxdose_day,maxdose_day_unit,duration_low,duration_high,duration_max,"
			+ "treatment_dose,treatment_dose_unit from mc_user_dosage "
			+ " where hiscode=? and drug_unique_code=? and doseunit=? and routecode=?  ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		pst.setString(3, doseunit);
		pst.setString(4, routecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		DosageInfaceImpl DosageInfaceImpl=new DosageInfaceImpl();
		List<DosageCustom> listobject = new ArrayList<DosageCustom>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DosageCustom custom = DosageInfaceImpl.copyFromMap(list.get(i));
				key = "user_dosage-"+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim()+"-"+custom.getDoseunit()+"-"+custom.getRoutecode();
				listobject.add(custom);
			}
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return DosageInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean drugdisInfaceImpl(int moduleid,String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,hiscode,drug_unique_code,"
			+ "moduleid,drugname,discode,disname,slcode,severity,warning from mc_user_drug_dis"
			+ "  where  drug_unique_code is not null and drug_unique_code <> '' and discode is "
			+ "not null and discode <> ' ' and moduleid =? and hiscode=? and drug_unique_code=? "
			+ " order by hiscode, drug_unique_code, discode ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, moduleid);
		pst.setString(2, hiscode);
		pst.setString(3, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		DrugdisInfaceImpl DrugdisInfaceImpl=new DrugdisInfaceImpl();
		List<DrugDisCustom> listobject = new ArrayList<DrugDisCustom>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DrugDisCustom custom = DrugdisInfaceImpl.copyFromMap(list.get(i));
				key = "user_drugdis-"+custom.getModuleid()+"-"+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim();
				listobject.add(custom);
			}
		}
		if(key!=""){
			return DrugdisInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean drugLevelInfaceImpl(int pkid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,hiscode,drug_unique_code1,"
			+ "drugname1,doseunit1,drug_unique_code2,"
			+ "drugname2,doseunit2,routecode,druglevel_low,druglevel_high,min_druglevel,"
			+ "max_druglevel,warning from mc_user_druglevel"
			+ "where pkid = ? order by hiscode, drug_unique_code1,drug_unique_code2,doseunit1,doseunit2,routecode";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		DrugLevelInfaceImpl DrugLevelInfaceImpl=new DrugLevelInfaceImpl();
		if(list!=null&&list.size()>0){
			DrugLevelCustom t = DrugLevelInfaceImpl.copyFromMap(list.get(0));
			String key = "user_druglevel-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()
			+"-"+t.getDruguniquecodetwo().trim()+"-"+t.getDoseunittwo()+"-"+t.getRoutecode();	
			return DrugLevelInfaceImpl.addOrUpdate(t, key);
		}else{
			return false;
		}
	}
	
	public boolean duptherapyInfaceImpl(int pkid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select distinct dupcid, user_max from mc_user_duptherapy where dupcid is not null "
				+ " and pkid=? and dupcid > 0 and user_max is not null and user_max > 0 order by dupcid, user_max";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		DuptherapyInfaceImpl DuptherapyInfaceImpl=new DuptherapyInfaceImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> object = list.get(i);
				String key = "user_duptherapy-"+object.get("dupcid").toString().trim();
				return DuptherapyInfaceImpl.addOrUpdate(key, object.get("user_max").toString());
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean hepdosInfaceImpl(String hiscode,String druguniquecode,String doseunit,
			String routecode,String heplabel) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select pkid,hiscode,drug_unique_code,"
			+ "doseunit,routecode,hep_label,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,agedesc,"
			+ "calculate_label,dose_each_low,dose_each_low_unit"
			+ ",dose_each_high,dose_each_high_unit,"
			+ "dose_day_low,dose_day_low_unit,dose_day_high,dose_day_high_unit,"
			+ "frequency_low,frequency_low_day,frequency_high,frequency_high_day"
		    +" from mc_user_hepdos  where hiscode=? and druguniquecode=? and "
		    + "doseunit=? and  routecode=? and hep_label=?   order by pkid ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		pst.setString(3, doseunit);
		pst.setString(4, routecode);
		pst.setString(5, heplabel);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		HepdosInfaceImpl HepdosInfaceImpl=new HepdosInfaceImpl();
		List<HepdosCustom> listobject = new ArrayList<HepdosCustom>();
		String key  = "user_hepdos-"+hiscode.trim()+"-"+druguniquecode.trim()
		+"-"+doseunit+"-"+routecode+"-"+heplabel;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				HepdosCustom custom = HepdosInfaceImpl.copyFromMap(list.get(i));
				listobject.add(custom);
			}
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return HepdosInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean interInfaceImpl(int pkid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,drug_unique_code1,drug_unique_code2,drugname1,"
			+ "drugname2,severity,warning,slcode from mc_user_inter";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		InterInfaceImpl InterInfaceImpl=new InterInfaceImpl();
		if(list!=null&&list.size()>0){
			InterCustom t = InterInfaceImpl.copyFromMap(list.get(0));
			String key=null;
			if(t.getDruguniquecodeone().trim().compareTo(t.getDruguniquecodetwo().trim()) > 0){
				key = StringUtils.trimToEmpty(t.getDruguniquecodetwo().trim()) + "-" + StringUtils.trimToEmpty(t.getDruguniquecodeone().trim());
			}else{
				key = StringUtils.trimToEmpty(t.getDruguniquecodeone().trim()) + "-" + StringUtils.trimToEmpty(t.getDruguniquecodetwo().trim());
			}
			key="user_inter-"+t.getHiscode().trim()+"-"+key;
//			String key = INTER_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecodeone().trim()+"-"+t.getDruguniquecodetwo().trim();
			return InterInfaceImpl.addOrUpdate(t, key);
		}else{
			return false;
		}
	}
	
	public boolean ivInfaceImpl(int pkid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,drug_unique_codes,drugnames,drugcodes,"
			+ "routetype,routedesc,slcode,severity,warning from mc_user_iv"
			+ " where pkid=? and  drug_unique_codes <> ' ' order by hiscode, drug_unique_codes ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		IvInfaceImpl IvInfaceImpl=new IvInfaceImpl();
		if(list!=null&&list.size()>0){
			IvCustom t = IvInfaceImpl.copyFromMap(list.get(0));
			String key = "user_iv-"+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutetype();
			return IvInfaceImpl.addOrUpdate(t, key);
		}else{
			return false;
		}
	}
	
	public boolean operativeAdviceImpl(String caseid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select caseid,orderno, drug_starttime, drug_unique_code, drugname,"
			+ " drug_executetime, drug_endtime from mc_pro_drug_advice where caseid =?";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, caseid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		OperativeAdviceImpl OperativeAdviceImpl=new OperativeAdviceImpl();
		List<OperDrugadvice> listobject = new ArrayList<OperDrugadvice>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				OperDrugadvice t = OperativeAdviceImpl.copyFromMap(list.get(i));
				key = "drug_advice-operative-"+t.getCaseid().trim();
				listobject.add(t);
			}
		}
		if(key!=""){
			return OperativeAdviceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean operativeOperImpl(String caseid,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select caseid,orderno, drug_unique_code, drugname, drug_starttime,"
			+ "drug_endtime,drug_executetime,operationcode,operationname,"
			+ "operation_starttime,operation_endtime,pkid from mc_pro_drug_operation "
			+ " where caseid=? and operationcode=?";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, caseid);
		pst.setString(2, operationcode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		OperativeOperImpl OperativeOperImpl=new OperativeOperImpl();
		List<OperDrugoper> listobject = new ArrayList<OperDrugoper>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				OperDrugoper t = OperativeOperImpl.copyFromMap(list.get(i));
				key = "pro_drug_operation-"+t.getCaseid().trim()+"-"+t.getOperationCode();
				listobject.add(t);
			}
		}
		if(key!=""){
			return OperativeOperImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean operativeTimeImpl(String caseid,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select * from (select  caseid,operationcode,drug_starttime from mc_pro_drug_operation "
				+ "order by drug_starttime ) c  where c.caseid=? and c.operationcode=?  group by "
				+ "caseid,operationcode";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, caseid);
		pst.setString(2, operationcode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		OperativeTimeImpl OperativeTimeImpl=new OperativeTimeImpl();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> object = list.get(i);
				String key = "pro_drug_operation-datetime-"+object.get("caseid").toString().trim()+"-"+object.get("operationcode").toString();
				return OperativeTimeImpl.addOrUpdate(key, object.get("drug_starttime").toString());
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean operprivInfaceImpl(String hiscode,String operationcode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,operationcode, drug_unique_code "
			+ "from mc_user_operation_priv  where hiscode=? and"
			+ " operationcode=?  order by hiscode,operationcode ";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, operationcode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		OperprivInfaceImpl OperprivInfaceImpl=new OperprivInfaceImpl();
		List<OperationPrivCustom> listobject = new ArrayList<OperationPrivCustom>();
		String key  = "";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				OperationPrivCustom t = OperprivInfaceImpl.copyFromMap(list.get(i));
				key = "user_operation_priv-"+t.getHiscode().trim()+"-"+t.getOperationCode();
				listobject.add(t);
			}
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return OperprivInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean pediatricInfaceImpl(String hiscode,String druguniquecode) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,drug_unique_code,drugname, "
			+ " agelow,unequal_low,agelow_unit,agehigh,"
			+ "unequal_high,agehigh_unit,agedesc,slcode,severity,warning from mc_user_pediatric "
			+ "where drug_unique_code <> ''  and hiscode=? and drug_unique_code=? and "
			+ "drug_unique_code is not null order by drug_unique_code";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		PediatricInfaceImpl PediatricInfaceImpl=new PediatricInfaceImpl();
		List<PediatricCustom> listobject = new ArrayList<PediatricCustom>();
		String key  = "user_pediatric-"+hiscode.trim()+"-"+druguniquecode.trim();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				PediatricCustom custom = PediatricInfaceImpl.copyFromMap(list.get(i));
				listobject.add(custom);
			}
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return PediatricInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean rendosInfaceImpl(String hiscode,String druguniquecode,String doseunit,
			String routecode,String renlabel) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,drug_unique_code,drugname,"
			+ "doseunit,routecode,ren_label,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,agedesc,"
			+ "calculate_label,dose_each_low,dose_each_low_unit"
			+ ",dose_each_high,dose_each_high_unit,"
			+ "dose_day_low,dose_day_low_unit,dose_day_high,dose_day_high_unit,"
			+ "frequency_low,frequency_low_day,frequency_high,frequency_high_day"
		    +" from mc_user_rendos  where hiscode=? and druguniquecode=? and "
		    + "doseunit=? and  routecode=? and ren_label=?   order by pkid";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setString(1, hiscode);
		pst.setString(2, druguniquecode);
		pst.setString(3, doseunit);
		pst.setString(4, routecode);
		pst.setString(5, renlabel);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		RendosInfaceImpl RendosInfaceImpl=new RendosInfaceImpl();
		List<RendosCustom> listobject = new ArrayList<RendosCustom>();
		String key  = "user_rendos-"+hiscode.trim()+"-"+druguniquecode.trim()
		+"-"+doseunit+"-"+routecode+"-"+renlabel;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				RendosCustom custom = RendosInfaceImpl.copyFromMap(list.get(i));
				listobject.add(custom);
			}
		}else{
			return false;
		}
		if(key!=""){
			//杩涘叆淇濆瓨redis鐨勬柟娉�
			return RendosInfaceImpl.addListCommon(listobject, key);
		}else{
			return false;
		}
	}
	
	public boolean routeInfaceImpl(int pkid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,drug_unique_code,routecode,drugname,"
			+ "routename,slcode,severity,warning  "
			+ "from mc_user_drugroute  where drug_unique_code <> ''"
			+ " and pkid = ? order by hiscode, drug_unique_code, routecode";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		RouteInfaceImpl RouteInfaceImpl=new RouteInfaceImpl();
		if(list!=null&&list.size()>0){
			RouteCustom t = RouteInfaceImpl.copyFromMap(list.get(0));
			String key = "user_drugroute-"+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutecode();
			return RouteInfaceImpl.addOrUpdate(t, key);
		}else{
			return false;
		}
	}
	
	public boolean sexInfaceImpl(int pkid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		sql="select pkid,hiscode,drug_unique_code,drugname,sexcode,"
			+ "sexdesc,slcode,severity,warning from mc_user_sex where drug_unique_code <> ''"
			+ " and pkid =? order by drug_unique_code, sexcode";
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		SexInfaceImpl SexInfaceImpl=new SexInfaceImpl();
		if(list!=null&&list.size()>0){
			SexCustom t = SexInfaceImpl.copyFromMap(list.get(0));
			String key =  "user_sex-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getSexcode();
			return SexInfaceImpl.addOrUpdate(t, key);
		}else{
			return false;
		}
	}
	
	public boolean unagespInfaceImpl(int pkid,int moduleid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		if(moduleid==12){
			sql="select pkid,hiscode,moduleid,drug_unique_code,drugname,slcode,severity,warning from mc_user_unage_sp  "
					+ "where moduleid=12 and drug_unique_code is not null and drug_unique_code <> '' and pkid =?";
		}else if(moduleid==13){
			sql="select pkid,hiscode,moduleid,drug_unique_code,drugname,slcode,severity,warning from mc_user_unage_sp  "
			+" where moduleid=13 and drug_unique_code is not null and drug_unique_code <> '' and pkid =? order by hiscode, drug_unique_code";
		}else if(moduleid==14){
			sql="select pkid,hiscode,moduleid,drug_unique_code,drugname,slcode,severity,warning from mc_user_unage_sp  "
			+" where moduleid=14 and pkid =? ";
		}else{
			return false;
		}
		pst=passmysqlconn.prepareStatement(sql);
		pst.setInt(1, pkid);
		rs=pst.executeQuery();
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		pst.close();
		passmysqlconn.close();
		
		UnagespInfaceImpl UnagespInfaceImpl=new UnagespInfaceImpl();
		if(list!=null&&list.size()>0){
			UnagespCustom t = UnagespInfaceImpl.copyFromMap(list.get(0));
			String key =  "user_unagesp-"+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
			return UnagespInfaceImpl.addOrUpdate(t, key);
		}else{
			return false;
		}
	}
	
	/**
	 * 更新屏蔽数据
	 * */
	public boolean ShieldImple(int pkid,int moduleid) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SQLException, IOException{
		//接连PASSmysql数据库
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		Redis_conf Redis_conf=new Redis_conf();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs;
		String sql;
		sql=Redis_conf.returnSql(moduleid,pkid,2);
		
		if(pkid>0){
			pst=passmysqlconn.prepareStatement(sql);
			pst.setInt(1, pkid);
			rs=pst.executeQuery();
		}else{
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
		}
		List<Map<String,Object>> list =passmysql.getlist(rs);
		rs.close();
		st.close();
		pst.close();
		passmysqlconn.close();
		
		if(moduleid<=17&&moduleid>0){
			List<ShieldData> listobject = new ArrayList<ShieldData>();
			String key  = "";
			ShieldImple ShieldImple=new ShieldImple();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					ShieldData custom = ShieldImple.copyFromMap(list.get(i));
					listobject.add(custom);
					key = Redis_conf.key(custom.getModuleid(), custom);
				}
			}
			if(key!=""){
				//杩涘叆淇濆瓨redis鐨勬柟娉�
				return ShieldImple.addListCommon(listobject, key);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
