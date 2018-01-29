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
import com.medicom.passlan.redis.custom.OperationPrivCustom;
import com.medicom.passlan.util.FieldUtils;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  OperprivInfaceImpl </li>
 * <li>类描述：   围术期自定义的更新redis的方法</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月30日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class OperprivInfaceImpl extends CommonInface<OperationPrivCustom>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_ADULT="select pkid,hiscode,operationcode, drug_unique_code "
			+ "from mc_user_operation_priv ";
	
	private final String OPERCUSTOM_TITLE = "user_operation_priv-";
	
	private Logger logger = Logger.getLogger(OperprivInfaceImpl.class);
	

	/**
	 * 成人用药的单个数据新增的方法
	 */
	public boolean addOrUpdate(OperationPrivCustom t) {
		try{
			String key = OPERCUSTOM_TITLE+t.getHiscode().trim()+"-"+t.getOperationCode();
			List<OperationPrivCustom> list = this.get(key);
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
	public boolean addList(List<OperationPrivCustom> list) {
		try{
			if(list!=null&&list.size()>0){
				OperationPrivCustom t = list.get(0);
				String key = OPERCUSTOM_TITLE+t.getHiscode().trim()+"-"+t.getOperationCode();
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
	public boolean findbyList(List<OperationPrivCustom> list, OperationPrivCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				OperationPrivCustom temp = list.get(i);
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
	 * @param hiscode
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(String hiscode,String operationcode){
		try{
			if(StringUtils.isNotBlank(hiscode)){
				StringBuffer sb = new StringBuffer(SEARCH_ADULT);
				sb.append(" where hiscode=? and operationcode=?  order by hiscode,operationcode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(operationcode);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<OperationPrivCustom> listobject = new ArrayList<OperationPrivCustom>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						OperationPrivCustom t = copyFromMap(list.get(i));
						key = OPERCUSTOM_TITLE+t.getHiscode().trim()+"-"+t.getOperationCode();
						listobject.add(t);
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
	public OperationPrivCustom copyFromMap(Map<String,Object> map){
		OperationPrivCustom cutsom = new OperationPrivCustom();
		//pkid,hiscode,operationcode, drug_unique_code
		cutsom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"),0));
		cutsom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		cutsom.setOperationCode(FieldUtils.getNullStr(map.get("operationcode"),""));
		cutsom.setDrugUniqueCode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		cutsom.setSlcode(0);
		cutsom.setSeverity("");
		cutsom.setWarning("");
		return cutsom;
	}
	
	public boolean deleteObject(OperationPrivCustom t){
		String key = OPERCUSTOM_TITLE+t.getHiscode().trim()+"-"+t.getOperationCode();
		List<OperationPrivCustom> list = this.get(key);
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
				StringBuffer sb = new StringBuffer(SEARCH_ADULT);
				sb.append(" where pkid=? order by hiscode,operationcode ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					OperationPrivCustom t = copyFromMap(list.get(0));
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
	
	
	public boolean deleteAll(String hiscode,String operationcode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(operationcode)){
				StringBuffer sb = new StringBuffer(SEARCH_ADULT);
				sb.append("where hiscode=? and operationcode=?  order by hiscode,operationcode ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						OperationPrivCustom t = copyFromMap(list.get(i));
						String key = OPERCUSTOM_TITLE+t.getHiscode().trim()+"-"+t.getOperationCode();
						delete(key);
					}
				}
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
	public boolean initAllDate(){
		try{
			Map<String,List<OperationPrivCustom>> map = new HashMap<String,List<OperationPrivCustom>>();
			StringBuffer sb = new StringBuffer(SEARCH_ADULT);
			List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					OperationPrivCustom t = copyFromMap(list.get(i));
					String key = OPERCUSTOM_TITLE+t.getHiscode().trim()+"-"+t.getOperationCode();
					if(map.get(key)!=null){
						map.get(key).add(t);
					}else{
						List<OperationPrivCustom> listobject = new ArrayList<OperationPrivCustom>();
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
