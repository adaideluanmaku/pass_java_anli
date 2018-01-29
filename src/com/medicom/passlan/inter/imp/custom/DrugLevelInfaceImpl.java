package com.medicom.passlan.inter.imp.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.custom.DrugLevelCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  DrugLevelInfaceImpl </li>
 * <li>类描述：   体外配伍浓度的更新redis的方式</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月30日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class DrugLevelInfaceImpl extends CommonInface<DrugLevelCustom>{
	
	private Logger logger = Logger.getLogger(DrugLevelInfaceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DRUGLEVEL_TITLE = "user_druglevel-";
	
	
	public String getDRUGLEVEL_TITLE() {
		return DRUGLEVEL_TITLE;
	}


	private final String SEARCH_DRUGLEVEL = "select pkid,hiscode,drug_unique_code1,"
			+ "drugname1,doseunit1,drug_unique_code2,"
			+ "drugname2,doseunit2,routecode,druglevel_low,druglevel_high,min_druglevel,"
			+ "max_druglevel,warning from mc_user_druglevel ";

	public boolean addOrUpdate(DrugLevelCustom t) {
		try{
			String key = DRUGLEVEL_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()
			+"-"+t.getDruguniquecodetwo().trim()+"-"+t.getDoseunittwo()+"-"+t.getRoutecode();	
			return this.addOrUpdateCommon(t,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述： 更新体外配伍弄得redsi的方法</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月30日 </li>
	 * </ul> 
	 * @param pkid 传入的数据的数组是t.getHiscode()+"-"+t.getDruguniquecode()+"-"+t.getDoseunit()
			+"-"+t.getDruguniquecodetwo()+"-"+t.getDoseunittwo()+"-"+t.getRoutecode();组成的
	 * @return
	 */
	public boolean initDateByCon(int pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb  = new StringBuffer(SEARCH_DRUGLEVEL);
				sb.append(" where pkid = ? order by hiscode, drug_unique_code1,drug_unique_code2,doseunit1,doseunit2,routecode");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					DrugLevelCustom t = copyFromMap(list.get(0));
					String key = DRUGLEVEL_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()
					+"-"+t.getDruguniquecodetwo().trim()+"-"+t.getDoseunittwo()+"-"+t.getRoutecode();	
					return this.addOrUpdate(t, key);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return true;

	}
	
	
	public DrugLevelCustom copyFromMap(Map<String,Object> map){
		DrugLevelCustom custom = new DrugLevelCustom();
		//pkid,hiscode,drug_unique_code1,drugname1,doseunit1,drug_unique_code2,"
		//"drugname2,doseunit2,routecode,druglevel_low,druglevel_high,min_druglevel,"
		//"max_druglevel,warning 
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"), 0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code1"),""));
		custom.setDrugname(FieldUtils.getNullStr(map.get("drugname1"),""));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit1"),""));
		custom.setDruguniquecodetwo(FieldUtils.getNullStr(map.get("drug_unique_code2"),""));
		custom.setDrugnametwo(FieldUtils.getNullStr(map.get("drugname2"),""));
		custom.setDoseunittwo(FieldUtils.getNullStr(map.get("doseunit2"),""));
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
		custom.setDruglevellow(FieldUtils.getDefaultF(map.get("druglevel_low"), 0));
		custom.setDruglevelhigh(FieldUtils.getDefaultF(map.get("druglevel_high"), 0));
		custom.setMindruglevel(FieldUtils.getDefaultF(map.get("min_druglevel"), 0));
		custom.setMaxdruglevel(FieldUtils.getDefaultF(map.get("max_druglevel"), 0));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	
	public boolean deleteId(Integer pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb  = new StringBuffer(SEARCH_DRUGLEVEL);
				sb.append(" where pkid = ? order by hiscode, drug_unique_code1,drug_unique_code2,doseunit1,doseunit2,routecode");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					DrugLevelCustom t = copyFromMap(list.get(0));
					String key = DRUGLEVEL_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()
					+"-"+t.getDruguniquecodetwo().trim()+"-"+t.getDoseunittwo()+"-"+t.getRoutecode();	
					return this.delete(key);
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
	
	
	public boolean deleteAll(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)){
				StringBuffer sb  = new StringBuffer(SEARCH_DRUGLEVEL);
				sb.append(" where hiscode = ? and  drug_unique_code1=? order by hiscode, drug_unique_code1,drug_unique_code2,doseunit1,doseunit2,routecode");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						DrugLevelCustom t = copyFromMap(list.get(0));
						String key = DRUGLEVEL_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()
						+"-"+t.getDruguniquecodetwo().trim()+"-"+t.getDoseunittwo()+"-"+t.getRoutecode();	
						 this.delete(key);
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
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述：初始化所有的redis的数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月11日 </li>
	 * </ul> 
	 * @return
	 */
	public boolean initAll(){
		try{
			List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DRUGLEVEL);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					DrugLevelCustom t = copyFromMap(list.get(i));
					this.addOrUpdate(t);
				}
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
}
