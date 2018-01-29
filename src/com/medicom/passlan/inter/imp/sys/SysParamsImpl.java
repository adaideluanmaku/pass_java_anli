package com.medicom.passlan.inter.imp.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.sys.SysParams;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  SysParamsImpl </li>
 * <li>类描述：   系统参数配置的更新redis的类</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class SysParamsImpl extends CommonInface<SysParams>{
	
	private final String SEARCH_PARAMS = "select pkid,checkmode,checkmode_name,scr_filters"
					+ ",is_save,is_scroutdrug,is_scr_exactlying,is_scr_dupuniquecode,is_useim from mc_params";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String SYSPARAMS_TITLE = "mc_params-";
	
	private Logger logger = Logger.getLogger(SysParamsImpl.class);

	public boolean addOrUpdate(SysParams t) {
		try{
			String key = SYSPARAMS_TITLE+t.getCheckModel().trim();
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
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(int pkid){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_PARAMS);
			sb.append(" where pkid=? ");
			sb.append("  order by checkmode ");
			List<Object> listvalue =new ArrayList<Object>();
			listvalue.add(pkid);
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			if(list!=null&&list.size()>0){
				SysParams priv = copyFromMap(list.get(0));
				return this.addOrUpdate(priv);
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
	 * <li>方法名：  copyFromMap </li>
	 * <li>功能描述：把从数据库的数据直接返回对象 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param map 数据库中查询出来的对象
	 * @return
	 */
	public SysParams copyFromMap(Map<String,Object> map){
		SysParams custom = new SysParams();
		custom.setPkid(Integer.parseInt(map.get("pkid").toString()));
		custom.setCheckModel(FieldUtils.getNullStr(map.get("checkmode"),""));
		custom.setCheckModeName(FieldUtils.getNullStr(map.get("checkmode_name"),""));
		custom.setScrfilters(FieldUtils.getNullStr(map.get("scr_filters"),""));
		custom.setIssave(FieldUtils.getDefaultInt(map.get("is_save"),0));
//		custom.setIsscroutDrug(FieldUtils.getDefaultInt(map.get("is_scroutdrug"),0));
//		custom.setIsscrExactlying(FieldUtils.getDefaultInt(map.get("is_scr_exactlying"),0));
//		custom.setIsscrDupuniquecode(FieldUtils.getDefaultInt(map.get("is_scr_dupuniquecode"),0));
//		custom.setIsUseim(FieldUtils.getDefaultInt(map.get("is_useim"),0));
		return custom;
	}
	/**
	 * 
	 * <ul>
	 * <li>方法名：  intAll </li>
	 * <li>功能描述：初始化全部数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul>
	 */
	public boolean initAll(){
		try{
			List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_PARAMS);
			if(list!=null&&list.size()>0){
				//ShardedJedis jedis = this.getShardedJedisPool().getResource();
				for(int i=0;i<list.size();i++){
					SysParams t = copyFromMap(list.get(i));
					String key =SYSPARAMS_TITLE+t.getCheckModel().trim();
					this.addOrUpdate(t,key);
				}
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public boolean deleteId(int pkid){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_PARAMS);
			sb.append(" where pkid=? ");
			sb.append("  order by checkmode ");
			List<Object> listvalue =new ArrayList<Object>();
			listvalue.add(pkid);
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			if(list!=null&&list.size()>0){
				SysParams t = copyFromMap(list.get(0));
				String key =SYSPARAMS_TITLE+t.getCheckModel().trim();
				return this.delete(key);
			
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}

}
