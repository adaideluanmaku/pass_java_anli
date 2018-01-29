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
import com.medicom.passlan.redis.custom.InterCustom;
import com.medicom.passlan.redis.sys.SysAllerGen;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  InterInfaceImpl </li>
 * <li>类描述：   相互作用的实现类</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月30日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class InterInfaceImpl extends CommonInface<InterCustom>{
	
private Logger logger = Logger.getLogger(InterInfaceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String INTER_TITLE = "user_inter-";
	
	private final String SEARCH_INTER = "select pkid,hiscode,drug_unique_code1,drug_unique_code2,drugname1,"
			+ "drugname2,severity,warning,slcode from mc_user_inter";

	public boolean addOrUpdate(InterCustom t) {
		try{
			String key = INTER_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecodeone().trim()+"-"+t.getDruguniquecodetwo().trim();
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
	 * @param pkid  主键ID
	 * @return
	 */
	public boolean initDateByCon(int pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				StringBuffer sb = new StringBuffer(SEARCH_INTER);
				sb.append(" where pkid=? ");
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					InterCustom t = copyFromMap(list.get(0));
					String key=null;
					if(t.getDruguniquecodeone().trim().compareTo(t.getDruguniquecodetwo().trim()) > 0){
						key = StringUtils.trimToEmpty(t.getDruguniquecodetwo().trim()) + "-" + StringUtils.trimToEmpty(t.getDruguniquecodeone().trim());
					}else{
						key = StringUtils.trimToEmpty(t.getDruguniquecodeone().trim()) + "-" + StringUtils.trimToEmpty(t.getDruguniquecodetwo().trim());
					}
					key=INTER_TITLE+t.getHiscode().trim()+"-"+key;
//					String key = INTER_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecodeone().trim()+"-"+t.getDruguniquecodetwo().trim();
					return this.addOrUpdate(t, key);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return true;

	}
	
	
	public InterCustom copyFromMap(Map<String,Object> map){
		InterCustom custom = new InterCustom();
		//select pkid,hiscode,drug_unique_code1,drug_unique_code2,drugname1,"
		//+ "drugname2,severity,warning,slcode from mc_user_inter
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid").toString(), 0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecodeone(FieldUtils.getNullStr(map.get("drug_unique_code1"),""));
		custom.setDruguniquecodetwo(FieldUtils.getNullStr(map.get("drug_unique_code2"),""));
		custom.setDrugnameone(FieldUtils.getNullStr(map.get("drugname1"),""));
		custom.setDrugnametwo(FieldUtils.getNullStr(map.get("drugname2"),""));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		custom.setSlcode(FieldUtils.getDefaultInt(map.get("slcode"), 0));
		return custom;
	}
	
	
	public boolean deleteId(Integer pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb = new StringBuffer(SEARCH_INTER);
				sb.append(" where pkid=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					InterCustom t = copyFromMap(list.get(0));
					String key = INTER_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecodeone().trim()+"-"+t.getDruguniquecodetwo().trim();
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
	
	public boolean deleteAll(String hiscode,String druguniquecode1){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode1)){
				StringBuffer sb = new StringBuffer(SEARCH_INTER);
				sb.append(" where hiscode=? and drug_unique_code1=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode1);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						InterCustom t = copyFromMap(list.get(i));
						String key = INTER_TITLE+t.getHiscode()+"-"+t.getDruguniquecodeone()+"-"+t.getDruguniquecodetwo();
						this.delete(key);
					}
					
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
	 * <li>功能描述： 初始化</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月11日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_INTER);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				InterCustom t = copyFromMap(list.get(i));
				String key = INTER_TITLE+t.getHiscode()+"-"+t.getDruguniquecodeone()+"-"+t.getDruguniquecodetwo();
				this.addOrUpdate(t,key);
			}
		}
	}
	
	
	

}
