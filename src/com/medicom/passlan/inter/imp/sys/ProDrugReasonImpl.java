package com.medicom.passlan.inter.imp.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.inter.imp.custom.PediatricInfaceImpl;
import com.medicom.passlan.redis.custom.PediatricCustom;
import com.medicom.passlan.redis.custom.ProDrugReason;
import com.medicom.passlan.util.FieldUtils;
@Service	
public class ProDrugReasonImpl extends CommonInface<ProDrugReason>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_PEDIATRIC="select pkid,hiscode,hisname,patstatus,inhospno,caseid,"
	+" visitcode,patcode,recipno,order_deptcode,order_doctorcode,order_starttime,order_endtime,"
	+" order_executetime,drug_unique_code,doseunit,dosepertime,frequency,istempdrug,"
	+" moduleid,modulename,slcode,severity,warning,moduleitems,otherinfo,is_forstatic,reason from mc_pro_drug_reason where reason <> ' ' ";
	
	
	private final String PRODRUGREASON_TTITLE = "pro_drug_reason-priv-";
	
	private Logger logger = Logger.getLogger(PediatricInfaceImpl.class);
	

	/**
	 * 成人用药的单个数据新增的方法
	 */
	public boolean addOrUpdate(ProDrugReason t) {
		try{
			String key = PRODRUGREASON_TTITLE+t.getCaseid().trim()+"-"+
					t.getRecipno()+"-"+t.getOrderDeptCode()+"-"+t.getOrderDoctorCode()
					+"-"+t.getDrugUniqueCode()+"-"+t.getModuleId();
			List<ProDrugReason> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 新增后者修改redis的数据
	 */
	public boolean addList(List<ProDrugReason> list) {
		try{
			if(list!=null&&list.size()>0){
				ProDrugReason t = list.get(0);
				String key = PRODRUGREASON_TTITLE+t.getCaseid().trim()+"-"+
						t.getRecipno()+"-"+t.getOrderDeptCode()+"-"+t.getOrderDoctorCode()
						+"-"+t.getDrugUniqueCode()+"-"+t.getModuleId();
				return this.addListCommon(list, key);
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		
		
	}

	/**
	 * 去掉成人用药找到的当前的对象
	 */
	public boolean findbyList(List<PediatricCustom> list, PediatricCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				PediatricCustom temp = list.get(i);
				if(temp.getPkid()==t.getPkid()){
					//这里去掉重复找到的成人用药的对象
					list.remove(temp);
					isSure =  true;
				}
			}
		}
		return isSure;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述：通过给定的条件初始化redis的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月27日 </li>
	 * </ul> 
	 * @param caseid
	 * @param recipno
	 * @param orderDeptCode
	 * @param orderDoctorCode
	 * @param drugUniqueCode
	 * @param moduleId
	 * @return 是否跟新redis成功
	 */
	public boolean initDateByCon(String caseid,String recipno,String orderDeptCode,
			String orderDoctorCode,String drugUniqueCode,String moduleId){
		try{
			if(StringUtils.isNotBlank(caseid)){
				StringBuffer sb = new StringBuffer(SEARCH_PEDIATRIC);
				sb.append(" and caseid=? and recipno=? and order_deptcode=? and order_doctorcode=? and drug_unique_code=? and moduleid=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(caseid);
				listvalue.add(recipno);
				listvalue.add(orderDeptCode);
				listvalue.add(orderDoctorCode);
				listvalue.add(drugUniqueCode);
				listvalue.add(moduleId);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<ProDrugReason> listobject = new ArrayList<ProDrugReason>();
				String key  = PRODRUGREASON_TTITLE+caseid.trim()+"-"+
						recipno+"-"+orderDeptCode+"-"+orderDoctorCode
						+"-"+drugUniqueCode+"-"+moduleId;
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						ProDrugReason custom = copyFromMap(list.get(i));
						listobject.add(custom);
					}
				}
				if(key!=""){
					//进入保存redis的方法
					return this.addListCommon(listobject, key);
				}else{
					return false;
				}
			}else{
				return false;
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  copyFromMap </li>
	 * <li>功能描述：把从数据库的数据直接返回对象 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param map 数据库中查询出来的对象
	 * @return
	 */
	public ProDrugReason copyFromMap(Map<String,Object> map){
		ProDrugReason custom = new ProDrugReason();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"),0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setHisname(FieldUtils.getNullStr(map.get("hisname"),""));
		custom.setPatStatus(FieldUtils.getDefaultInt(map.get("patstatus"),0));
		custom.setInhospno(FieldUtils.getNullStr(map.get("inhospno"),""));
		custom.setCaseid(FieldUtils.getNullStr(map.get("caseid"),""));
		custom.setVisitCode(FieldUtils.getNullStr(map.get("visitcode"),""));
		custom.setPatCode(FieldUtils.getNullStr(map.get("patcode"),""));
		custom.setRecipno(FieldUtils.getNullStr(map.get("recipno"),""));
		custom.setOrderDeptCode(FieldUtils.getNullStr(map.get("order_deptcode"),""));
		custom.setOrderDoctorCode(FieldUtils.getNullStr(map.get("order_deptcode"),""));
		custom.setOrderStartTime(FieldUtils.getNullStr(map.get("order_starttime"),""));
		custom.setOrderEndTime(FieldUtils.getNullStr(map.get("order_endtime"),""));
		custom.setOrderExecuteTime(FieldUtils.getNullStr(map.get("order_executetime"),""));
		custom.setDrugUniqueCode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit"),""));
		custom.setDoseperTime(FieldUtils.getNullStr(map.get("dosepertime"),""));
		custom.setFrequency(FieldUtils.getNullStr(map.get("frequency"),""));
		custom.setIstempDrug(FieldUtils.getDefaultInt(map.get("istempdrug"),0));
		custom.setModuleId(FieldUtils.getDefaultInt(map.get("moduleid"),0));
		custom.setModuleName(FieldUtils.getNullStr(map.get("modulename"),""));
		custom.setSlcode(Integer.parseInt(map.get("slcode").toString()));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		custom.setModuleItems(FieldUtils.getNullStr(map.get("moduleitems"),""));
		custom.setOtherInfo(FieldUtils.getNullStr(map.get("otherinfo"),""));
		custom.setIsForstatic(FieldUtils.getDefaultInt(map.get("is_forstatic"),0));
		custom.setReason(FieldUtils.getNullStr(map.get("reason"),""));
		return custom;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述：初始化数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月5日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_PEDIATRIC);
		if(list!=null&&list.size()>0){
			Map<String,List<ProDrugReason>> maps  = new HashMap<String, List<ProDrugReason>>();
			for(int i=0;i<list.size();i++){
				ProDrugReason t = copyFromMap(list.get(i));
				String key = PRODRUGREASON_TTITLE+t.getCaseid().trim()+"-"+
						t.getRecipno()+"-"+t.getOrderDeptCode()+"-"+t.getOrderDoctorCode()
						+"-"+t.getDrugUniqueCode()+"-"+t.getModuleId();
				if(maps.containsKey(key)){
					maps.get(key).add(t);
				 }else{
				   List z = new ArrayList();
				   z.add(t);
				   maps.put(key,z);
				}
			}
			this.addMapinListCommon(maps);
		}
	}


}
