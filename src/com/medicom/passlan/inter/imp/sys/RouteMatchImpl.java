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
import com.medicom.passlan.redis.sys.SysRouteMatch;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RouteMatchImpl </li>
 * <li>类描述：  给药途径的工作 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月4日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class RouteMatchImpl extends CommonInface<SysRouteMatch>{
	
	private final String SEARCH_MATCH = " select a.match_scheme,a.routecode as u_code, a.routename as u_name," 
			+"b.routeid, b.st_routeid, b.route_name,b.routelabel from mc_dict_route a, mc_route_name_view b "
			+" where a.pass_routeid is not null and a.pass_routeid > 0 and a.pass_routeid = b.routeid ";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String MATCH_TITLE = "route_pass-";
	
	private Logger logger = Logger.getLogger(SysMatchRelationImpl.class);
	
	
	public boolean addOrUpdate(SysRouteMatch t) {
		try{
			String key = MATCH_TITLE+t.getMatchscheme()+"-"+ StringUtils.trim(t.getUcode());
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
	public boolean initDateByCon(Integer matchscheme,String routecode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_MATCH);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(routecode)
					&&StringUtils.isNotBlank(String.valueOf(matchscheme)) ){
				sb.append(" and a.match_scheme=?  and a.routecode=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(routecode);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysRouteMatch priv = copyFromMap(list.get(0));
					 this.addOrUpdate(priv);
				}
				return true;
			}else{
				String key = MATCH_TITLE+matchscheme+"-"+ StringUtils.trim(routecode);
				return delete(key);
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
	public SysRouteMatch copyFromMap(Map<String,Object> map){
		SysRouteMatch custom = new SysRouteMatch();
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setUcode(FieldUtils.getNullStr(map.get("u_code"),""));
		custom.setUname(FieldUtils.getNullStr(map.get("u_name"),""));
		custom.setRouteId(FieldUtils.getDefaultInt(map.get("routeid"),0));
		custom.setStrouteId(FieldUtils.getNullStr(map.get("st_routeid"),""));
		custom.setRouteName(FieldUtils.getNullStr(map.get("route_name"),""));
		custom.setRouteLabel(FieldUtils.getNullStr(map.get("routelabel"),""));
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
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_MATCH);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysRouteMatch t = copyFromMap(list.get(i));
				String key = MATCH_TITLE+t.getMatchscheme()+"-"+ StringUtils.trim(t.getUcode());
				this.addOrUpdate(t,key);
			}
		}
	}

}
