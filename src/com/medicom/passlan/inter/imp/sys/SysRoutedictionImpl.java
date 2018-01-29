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
import com.medicom.passlan.redis.sys.RouteDictionary;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class SysRoutedictionImpl extends CommonInface<RouteDictionary>{
	
	private final String SEARCH_DICTIONARY = "select distinct match_scheme,routecode, routename, route_type,isskintest from mc_dict_route where (route_type is not null and route_type <> '') ";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DICTIONARY_TITLE = "dict_routediction-";
	
	private Logger logger = Logger.getLogger(SysRoutedictionImpl.class);
	
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public boolean addOrUpdate(RouteDictionary t) {
		try{
			String key = DICTIONARY_TITLE+t.getMatchscheme()+"-"+t.getRoutecode();
			return this.addOrUpdateCommon(t, key);
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
	public RouteDictionary copyFromMap(Map<String,Object> map){
		RouteDictionary custom = new RouteDictionary();
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setRoutename(FieldUtils.getNullStr(map.get("routename"),""));
		custom.setIsskintest(FieldUtils.getDefaultInt(map.get("isskintest"),0));
		custom.setRoutetype(FieldUtils.getDefaultInt(map.get("route_type"),0));
		return custom;
	}
	
	
	/**
	 * 初始化所有的数据
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DICTIONARY);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				RouteDictionary t = copyFromMap(list.get(i));
				String key =DICTIONARY_TITLE+t.getMatchscheme()+"-"+t.getRoutecode();
				this.addOrUpdate(t,key);
			}
		}
	}
	
	
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  deletetHosptial </li>
	 * <li>功能描述： 删除数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年10月18日 </li>
	 * </ul> 
	 * @param hiscode_user
	 * @return
	 */
	public boolean deletetRouted(String routecode,Integer matchscheme){
		return this.delete(DICTIONARY_TITLE+matchscheme+"-"+routecode);
	}
	
	
	public boolean initDateByCon(int matchshceme,String routecode){
		try{
			if(StringUtils.isNotBlank(routecode)){
				StringBuffer sb = new StringBuffer(SEARCH_DICTIONARY);
				sb.append(" and match_scheme=? and routecode =?  ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchshceme);
				listvalue.add(routecode);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String,Object> object = list.get(i);
						//String key = DICTIONARY_TITLE+matchshceme+"-"+routecode;
						RouteDictionary t = copyFromMap(object);
						return addOrUpdate(t);
					}
				}else{
					if(routecode!=null&&StringUtils.isNotBlank(routecode)){
						String key  = DICTIONARY_TITLE+matchshceme+"-"+routecode;
						return this.delete(key);
					}else{
						return false;
					}
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

}
