package com.medicom.passlan.inter.imp.custom;

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
import com.medicom.passlan.redis.custom.OperDrugoper;
import com.medicom.passlan.util.FieldUtils;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  OperativeOperImpl </li>
 * <li>类描述：   </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月5日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class OperativeOperImpl extends CommonInface<OperDrugoper>{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String OPER_TITLE = "pro_drug_operation-";
	
	private final String SEARCH_OPER = "select caseid,orderno, drug_unique_code, drugname, drug_starttime,"
			+ "drug_endtime,drug_executetime,operationcode,operationname,"
			+ "operation_starttime,operation_endtime,pkid from mc_pro_drug_operation ";

	private Logger logger = Logger.getLogger(OperativeOperImpl.class);
	
	public boolean addOrUpdate(OperDrugoper t) {
		try{
			String key = SEARCH_OPER+t.getCaseid().trim()+"-"+t.getOperationCode();
			List<OperDrugoper> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean addList(List<OperDrugoper> list) {
		try{
			if(list!=null&&list.size()>0){
				OperDrugoper t = list.get(0);
				String key = OPER_TITLE+t.getCaseid().trim()+"-"+t.getOperationCode();
				return this.addListCommon(list, key);
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean findbyList(List<OperDrugoper> list, OperDrugoper t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				OperDrugoper temp = list.get(i);
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
	 * <li>功能描述： 通过给定的条件初始化redis的数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param hiscode 医院
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(String caseid,String operationcode){
		try{
			if(StringUtils.isNotBlank(caseid)){
				StringBuffer sb = new StringBuffer(SEARCH_OPER);
				sb.append(" where caseid=? and operationcode=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(caseid);
				listvalue.add(operationcode);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<OperDrugoper> listobject = new ArrayList<OperDrugoper>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						OperDrugoper t = copyFromMap(list.get(i));
						key = OPER_TITLE+t.getCaseid().trim()+"-"+t.getOperationCode();
						listobject.add(t);
					}
				}
				if(key!=""){
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
	 * <li>功能描述：把数据库的字段拷贝到对象 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param map
	 * @return
	 */
	public OperDrugoper copyFromMap(Map<String,Object> map){
		OperDrugoper custom = new OperDrugoper();
		custom.setCaseid(FieldUtils.getNullStr(map.get("caseid"),""));
		custom.setOrderno(FieldUtils.getNullStr(map.get("orderno"),""));
		custom.setDrugUniqueCode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDrugName(FieldUtils.getNullStr(map.get("drugname"),""));
		custom.setDrugStartTime(FieldUtils.getNullStr(map.get("drug_starttime"),""));
		custom.setDrugEndTime(FieldUtils.getNullStr(map.get("drug_endtime"),""));
		custom.setDrugExecuteTime(FieldUtils.getNullStr(map.get("drug_executetime"),""));
		custom.setOperationCode(FieldUtils.getNullStr(map.get("operationcode"),""));
		custom.setOperationName(FieldUtils.getNullStr(map.get("operationname"),""));
		custom.setOperationStartTime(FieldUtils.getNullStr(map.get("operation_starttime"),""));
		custom.setOperationEndTime(FieldUtils.getNullStr(map.get("operation_endtime"),""));
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"), 0));
		return custom;
	}
	
	
	public boolean deleteObject(OperDrugoper t){
		String key = SEARCH_OPER+t.getCaseid().trim()+"-"+t.getOperationCode();
		List<OperDrugoper> list = this.get(key);
		this.findbyList(list,t);
		if(list!=null&&list.size()==0){
			return this.delete(key);
		}else if(list!=null)
		{
			return this.addOrUpdateList(list,key);
		}else{
			return false;
		}
	}	
	public boolean deleteId(Integer pkid){
		try{
			if(pkid>0){
				StringBuffer sb = new StringBuffer(SEARCH_OPER);
				sb.append(" where pkid=? ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					OperDrugoper t = copyFromMap(list.get(0));
					return deleteObject(t);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAllDate </li>
	 * <li>功能描述：初始化所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月13日 </li>
	 * </ul> 
	 * @return
	 */
	public boolean initAllDate(){
		try{
			Map<String,List<OperDrugoper>> map = new HashMap<String,List<OperDrugoper>>();
			StringBuffer sb = new StringBuffer(SEARCH_OPER);
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					OperDrugoper t = copyFromMap(list.get(i));
					String key = OPER_TITLE+t.getCaseid().trim()+"-"+t.getOperationCode();
					if(map.get(key)!=null){
						map.get(key).add(t);
					}else{
						List<OperDrugoper> listobject = new ArrayList<OperDrugoper>();
						listobject.add(t);
						map.put(key, listobject);
					}
				}
				this.addMapinListCommon(map);
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

}
