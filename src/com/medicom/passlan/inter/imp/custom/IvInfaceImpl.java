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
import com.medicom.passlan.redis.custom.IvCustom;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歅assLanManage </li>
 * <li>绫诲悕绉帮細  InterInfaceImpl </li>
 * <li>绫绘弿杩帮細   鐩镐簰浣滅敤鐨勫疄鐜扮被</li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�0鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
@Service	
public class IvInfaceImpl extends CommonInface<IvCustom>{
	
	private Logger logger = Logger.getLogger(IvInfaceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String IV_TITLE = "user_iv-";
	
	private final String SEARCH_IV = "select pkid,hiscode,drug_unique_codes,drugnames,drugcodes,"
			+ "routetype,routedesc,slcode,severity,warning from mc_user_iv ";

	public boolean addOrUpdate(IvCustom t) {
		try{
			String key = IV_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutetype();
			return this.addOrUpdateCommon(t,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  initDateByCon </li>
	 * <li>鍔熻兘鎻忚堪锛�鏇存柊浣撳閰嶄紞寮勫緱redsi鐨勬柟娉�/li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�0鏃�</li>
	 * </ul> 
	 * @param pkid  涓婚敭ID
	 * @return
	 */
	public boolean initDateByCon(int pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb = new StringBuffer(SEARCH_IV);
				sb.append(" where pkid=? and  drug_unique_codes <> ' ' order by hiscode, drug_unique_codes ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					IvCustom t = copyFromMap(list.get(0));
					String key = IV_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutetype();
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
	
	
	public IvCustom copyFromMap(Map<String,Object> map){
		IvCustom custom = new IvCustom();
		// pkid,hiscode,drug_unique_codes,drugnames,drugcodes,"
		//		+ "routetype,routedesc,slcode,severity,warning 
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"), 0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		//ch修改
		custom.setDrugnames(FieldUtils.getNullStr(map.get("drugnames"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_codes"),""));
		custom.setDrugcodes(FieldUtils.getNullStr(map.get("drugcodes"),""));
		custom.setRoutetype(FieldUtils.getDefaultInt(map.get("routetype"),0));
		custom.setRoutedesc(FieldUtils.getNullStr(map.get("routedesc"),""));
		custom.setSlcode(FieldUtils.getDefaultInt(map.get("slcode"), 0));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	
	public boolean deleteId(Integer pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb = new StringBuffer(SEARCH_IV);
				sb.append(" where pkid=? and drug_unique_codes <> ' ' order by hiscode, drug_unique_codes ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					IvCustom t = copyFromMap(list.get(0));
					String key = IV_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutetype();
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
	
	public boolean deleteAll(String hiscode,String druguniquecodes){
		try{
			if(StringUtils.isNotBlank(hiscode)&&
					StringUtils.isNotBlank(druguniquecodes)){
				StringBuffer sb = new StringBuffer(SEARCH_IV);
				sb.append(" where hiscode=? and drug_unique_codes=? and drug_unique_codes <> ' ' order by hiscode, drug_unique_codes ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecodes);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						IvCustom t = copyFromMap(list.get(i));
						String key = IV_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutetype();
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
	
	
	public void initAll(){
		StringBuffer sb = new StringBuffer(SEARCH_IV);
		sb.append(" where drug_unique_codes <> ' ' order by hiscode, drug_unique_codes ");
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				IvCustom t = copyFromMap(list.get(i));
				String key = IV_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutetype();
				this.addOrUpdate(t,key);
			}
		}
	}
	
	
	

}
