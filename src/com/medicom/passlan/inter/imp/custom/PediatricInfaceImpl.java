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
import com.medicom.passlan.redis.custom.PediatricCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  PediatricInfaceImpl </li>
 * <li>类描述：  儿童模块更新redis的数据 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月30日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class PediatricInfaceImpl extends CommonInface<PediatricCustom>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_PEDIATRIC="select pkid,hiscode,drug_unique_code,drugname, "
			+ " agelow,unequal_low,agelow_unit,agehigh,"
			+ "unequal_high,agehigh_unit,agedesc,slcode,severity,warning from mc_user_pediatric "
			+ "where drug_unique_code <> '' ";
	
	
	private final String PEDIATRIC_TTITLE = "user_pediatric-";
	
	private Logger logger = Logger.getLogger(PediatricInfaceImpl.class);
	

	/**
	 * 成人用药的单个数据新增的方法
	 */
	public boolean addOrUpdate(PediatricCustom t) {
		try{
			String key = PEDIATRIC_TTITLE+t.getHiscode().trim()+"-"+t.getDrugUniqueCode().trim();
			List<PediatricCustom> list = this.get(key);
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
	public boolean addList(List<PediatricCustom> list) {
		try{
			if(list!=null&&list.size()>0){
				PediatricCustom t = list.get(0);
				String key = PEDIATRIC_TTITLE+t.getHiscode().trim()+"-"+t.getDrugUniqueCode().trim();
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
	 * @param hiscode
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(SEARCH_PEDIATRIC);
				sb.append(" and hiscode=? and drug_unique_code=? and drug_unique_code is not null order by drug_unique_code ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode.trim());
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<PediatricCustom> listobject = new ArrayList<PediatricCustom>();
				String key  = PEDIATRIC_TTITLE+hiscode.trim()+"-"+druguniquecode.trim();
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						PediatricCustom custom = copyFromMap(list.get(i));
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
	public PediatricCustom copyFromMap(Map<String,Object> map){
		PediatricCustom custom = new PediatricCustom();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"),0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDrugUniqueCode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDrugName(FieldUtils.getNullStr(map.get("drugname"),""));
		custom.setAgeLow(FieldUtils.getNullStr(map.get("agelow"),""));
		custom.setUnequalLow(FieldUtils.getDefaultInt(map.get("unequal_low"),0));
		custom.setAgeLowUnit(FieldUtils.getNullStr(map.get("agelow_unit"),""));
		custom.setAgeHigh(FieldUtils.getNullStr(map.get("agehigh"),""));
		custom.setAgeHighUnit(FieldUtils.getNullStr(map.get("agehigh_unit"),""));
		custom.setUnequalHigh(FieldUtils.getDefaultInt(map.get("unequal_high"),0));
		custom.setAgeDesc(FieldUtils.getNullStr(map.get("agedesc"),""));
		custom.setSlcode(Integer.parseInt(map.get("slcode").toString()));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	
	public boolean deleteObject(PediatricCustom t){
		String key = PEDIATRIC_TTITLE+t.getHiscode().trim()+"-"+t.getDrugUniqueCode().trim();
		List<PediatricCustom> list = this.get(key);
		this.findbyList(list,t);
		if(list!=null&&list.size()==0){
			return this.delete(key);
		}else if(list!=null){
			return this.addOrUpdateList(list,key);
		}else{
			return false;
		}
	}
	
	public boolean deleteId(Integer pkid){
		try{
			if(pkid>0){
				StringBuffer sb = new StringBuffer(SEARCH_PEDIATRIC);
				sb.append(" and pkid=? and drug_unique_code is not null order by drug_unique_code ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					PediatricCustom t = copyFromMap(list.get(0));
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
	
	
	public boolean deleteAll(String hiscode,String drug_unique_code1){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(drug_unique_code1)){
				StringBuffer sb = new StringBuffer(SEARCH_PEDIATRIC);
				sb.append(" and hiscode=? and drug_unique_code=? and drug_unique_code is not null order by drug_unique_code ");
				List<Object> listvalue = new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(drug_unique_code1);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						PediatricCustom t = copyFromMap(list.get(i));
						String key = PEDIATRIC_TTITLE+t.getHiscode().trim()+"-"+t.getDrugUniqueCode().trim();
						delete(key);
					}
					return true;
					
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
	
	
	
	public boolean initAllDate(){
		try{
			Map<String,List<PediatricCustom>> map = new HashMap<String,List<PediatricCustom>>();
			StringBuffer sb = new StringBuffer(SEARCH_PEDIATRIC);
			sb.append("  and drug_unique_code is not null order by drug_unique_code ");
			List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					PediatricCustom custom = copyFromMap(list.get(i));
					String key  = PEDIATRIC_TTITLE+custom.getHiscode().trim()+"-"+custom.getDrugUniqueCode().trim();
					if(map.get(key)!=null){
						map.get(key).add(custom);
					}else{
						List<PediatricCustom> listobject = new ArrayList<PediatricCustom>();
						listobject.add(custom);
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
