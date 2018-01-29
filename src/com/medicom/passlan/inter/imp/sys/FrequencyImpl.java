package com.medicom.passlan.inter.imp.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.sys.SysFrequency;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  FrequencyImpl </li>
 * <li>类描述：   给药频次的</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月4日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class FrequencyImpl extends CommonInface<SysFrequency>{
	
	
	private final String SEARCH_FREQUENCY = " select distinct match_scheme,frequency, times,days from mc_dict_frequency where times is not null and times > 0";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String FREQUENCY_TITLE = "dict_frequency-";
	
	private Logger logger = Logger.getLogger(FrequencyImpl.class);
	
	
	public boolean addOrUpdate(SysFrequency t) {
		try{
			String key = FREQUENCY_TITLE+t.getMatchscheme()+"-"+ t.getFrequency().trim();
			return this.addOrUpdateCommon(t, key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
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
	 * @return
	 */
	public boolean initDateByCon(Integer matchscheme,String frequency){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_FREQUENCY);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(frequency)
					&&StringUtils.isNotBlank(String.valueOf(matchscheme)) ){
				sb.append(" and match_scheme=?  and frequency=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(frequency);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysFrequency priv = copyFromMap(list.get(0));
					 this.addOrUpdate(priv);
				}
				return true;
			}else{
				String key  = FREQUENCY_TITLE+matchscheme+"-"+ frequency.trim();
				this.delete(key);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
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
	public SysFrequency copyFromMap(Map<String,Object> map){
		SysFrequency custom = new SysFrequency();
		//match_scheme,frequency, times,days
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setFrequency(FieldUtils.getNullStr(map.get("frequency"),""));
		custom.setTimes(FieldUtils.getDefaultInt(map.get("times"),0));
		custom.setDays(FieldUtils.getDefaultInt(map.get("days"),0));
		return custom;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述：初始化所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月5日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_FREQUENCY);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysFrequency t = copyFromMap(list.get(i));
				String key = FREQUENCY_TITLE+t.getMatchscheme()+"-"+ t.getFrequency().trim();
				this.addOrUpdate(t,key);
			}
		}
	}
	
	

}
