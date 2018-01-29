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
import com.medicom.passlan.redis.sys.DisDictionary;
import com.medicom.passlan.util.FieldUtils;

/**
 * 初始化感染/慢性疾病配对数据
 * @author Administrator
 *
 */
@Service
public class SysDisDisctionImpl extends CommonInface<DisDictionary>{
	
	private final String SEARCH_DISDICTIONARY = "select distinct match_scheme,discode, disname, Dis_type from mc_dict_disease";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DISDICTIONARY_TITLE = "dict_disdiction-";
	
	private Logger logger = Logger.getLogger(SysDisDisctionImpl.class);
	
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public boolean addOrUpdate(DisDictionary t) {
		try{
			String key = DISDICTIONARY_TITLE+t.getMatchscheme()+"-"+t.getDiscode();
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
	public DisDictionary copyFromMap(Map<String,Object> map){
		DisDictionary custom = new DisDictionary();
		//match_scheme,discode, disname, Dis_type
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setDiscode(FieldUtils.getNullStr(map.get("discode"),""));
		custom.setDisname(FieldUtils.getNullStr(map.get("disname"),""));
		custom.setDistype(FieldUtils.getDefaultInt(map.get("dis_type"),0));
		return custom;
	}
	
	
	/**
	 * 初始化所有的数据
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DISDICTIONARY);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				DisDictionary t = copyFromMap(list.get(i));
				String key =DISDICTIONARY_TITLE+t.getMatchscheme()+"-"+t.getDiscode();
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
	public boolean deletetDiscode(String disode,Integer matchscheme){
		return this.delete(DISDICTIONARY_TITLE+matchscheme+"-"+disode);
	}
	
	/**
	 * 初始化数据的方法
	 * @param matchshceme
	 * @param discode
	 * @return
	 */
	public boolean initDateByCon(int matchshceme,String discode){
		try{
			if(StringUtils.isNotBlank(discode)){
				StringBuffer sb = new StringBuffer(SEARCH_DISDICTIONARY);
				sb.append(" where match_scheme=? and discode =?  ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchshceme);
				listvalue.add(discode);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String,Object> object = list.get(i);
						//String key = DICTIONARY_TITLE+matchshceme+"-"+routecode;
						DisDictionary t = copyFromMap(object);
						return addOrUpdate(t);
					}
				}else{
					if(discode!=null&&StringUtils.isNotBlank(discode)){
						String key  = DISDICTIONARY_TITLE+matchshceme+"-"+discode;
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
