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
import com.medicom.passlan.redis.sys.SysAllerGen;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  AllerGenImpl </li>
 * <li>类描述：  更新过敏原的数据 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月4日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class AllerGenImpl extends CommonInface<SysAllerGen>{
	
	private final String SEARCH_ALLERGEN =  "select a.match_scheme,a.allercode as u_code, a.allername as u_name, b.allerid,"
			+ " b.allername, b.allertype from mc_dict_allergen a, mc_allergen_view b "
			+ "where a.pass_allerid is not null and a.pass_allerid > 0 "
			+ "and a.pass_allerid = b.allerid";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String ALLERGEN_TITLE = "dict_allergen-";
	
	private Logger logger = Logger.getLogger(AllerGenImpl.class);
	
	
	public boolean addOrUpdate(SysAllerGen t) {
		try{
			String key = ALLERGEN_TITLE+t.getMatchscheme()+"-"+ t.getUcode().trim();
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
	public boolean initDateByCon(Integer matchscheme,String allercode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_ALLERGEN);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(allercode)
					&&StringUtils.isNotBlank(String.valueOf(matchscheme)) ){
				sb.append(" and a.match_scheme=?  and a.allercode=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(allercode);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysAllerGen priv = copyFromMap(list.get(0));
					 this.addOrUpdate(priv);
				}
				return true;
			}else{
				String key = ALLERGEN_TITLE+matchscheme+"-"+ allercode;
				return this.delete(key);
			}
		}catch(Exception e){
			e.printStackTrace();
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
	public SysAllerGen copyFromMap(Map<String,Object> map){
		SysAllerGen custom = new SysAllerGen();
		//a.match_scheme,a.allercode as u_code, a.allername as u_name, b.allerid,"
		//+ " b.allername, b.allertype
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setUcode(FieldUtils.getNullStr(map.get("u_code"),""));
		custom.setUname(FieldUtils.getNullStr(map.get("u_name"),""));
		custom.setAllerid(FieldUtils.getNullStr(map.get("allerid"),""));
		custom.setAllername(FieldUtils.getNullStr(map.get("allername"),""));
		custom.setAllertype(FieldUtils.getDefaultInt(map.get("allertype"),0));
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
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_ALLERGEN);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysAllerGen t = copyFromMap(list.get(i));
				String key = ALLERGEN_TITLE+t.getMatchscheme()+"-"+ t.getUcode().trim();
				this.addOrUpdate(t,key);
			}
		}
	}
	
	
	

}
