package com.medicom.passlan.inter.imp.sys;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.ObjectInfaceImpl;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

@Service	
public class DrugDoseUnitImpl extends ObjectInfaceImpl{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DOSEUNIT_TITLE = "drug_cost_dose_unit-";
	
	private final String SEARCH_DOSEUNIT = "select  a.match_scheme,b.drugcode,b.drugspec,b.doseunit,b.costunit, b.conversion " 
+" from mc_dict_drug a left join mc_cost_dose_unit b "
+" on a.drugcode = b.drugcode WHERE b.drugcode IS NOT NULL ";
	
	private Logger logger = Logger.getLogger(DrugDoseUnitImpl.class);
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述： 初始化单个对象的</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param paraname  key 
	 * @param paramvalue value
	 * @return
	 */
	public boolean initDateByCon(int match_scheme,String drugcode,String drugspec,
			String doseunit,String costunit, String conversion){
		try{
			if(StringUtils.isNotBlank(String.valueOf(match_scheme))&&StringUtils.isNotBlank(drugcode)){
				String key  = 	DOSEUNIT_TITLE+match_scheme+"-"+drugcode.trim()
						+"-"+drugspec+"-"+doseunit+"-"+costunit;
				
				return this.addOrUpdate(key, conversion);
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述：初始化配置文件所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DOSEUNIT);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				Map<String,Object> map = list.get(i);
				String key =DOSEUNIT_TITLE+FieldUtils.getDefaultInt(map.get("match_scheme"),0)+"-"+FieldUtils.getNullStr(map.get("drugcode"), "")
						+"-"+FieldUtils.getNullStr(map.get("drugspec"), "")+"-"+FieldUtils.getNullStr(map.get("doseunit"), "")+"-"+FieldUtils.getNullStr(map.get("costunit"), "");
				this.addOrUpdate(key, FieldUtils.getNullStr(map.get("conversion"), ""));
			}
		}
		
	}
}
