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
import com.medicom.passlan.redis.sys.SysMatchRelation;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  SysMatchRelationImpl </li>
 * <li>类描述：   医院配对方案更新redis的数据</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class SysMatchRelationImpl extends CommonInface<SysMatchRelation>{
	
	private final String SEARCH_MATCH = "select hiscode,hisname,searchcode,drugmatch_scheme,allermatch_scheme,"
			+ "dismatch_scheme,freqmatch_scheme,routematch_scheme,doctormatch_scheme,oprmatch_scheme,mhiscode,costitemmatch_scheme,deptmatch_scheme,exammatch_scheme,labmatch_scheme,labsubmatch_scheme,doctorgroupmatch_scheme,wardmatch_scheme from mc_hospital_match_relation";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String MATCH_TITLE = "match_relation-";
	
	private Logger logger = Logger.getLogger(SysMatchRelationImpl.class);
	
	
	public boolean addOrUpdate(SysMatchRelation t) {
		try{
			String key = MATCH_TITLE+t.getHiscode().trim();
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
	public boolean initDateByCon(String hisocde){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_MATCH);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(hisocde)){
				sb.append(" where hiscode=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hisocde);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysMatchRelation priv = copyFromMap(list.get(0));
					 this.addOrUpdate(priv);
				}
				return true;
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
	public SysMatchRelation copyFromMap(Map<String,Object> map){
		SysMatchRelation custom = new SysMatchRelation();
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setHisname(FieldUtils.getNullStr(map.get("hisname"),""));
		custom.setSearchcode(FieldUtils.getNullStr(map.get("hisname"),""));
		custom.setDrugmatch(FieldUtils.getDefaultInt(map.get("drugmatch_scheme"),0));
		custom.setAllermatch(FieldUtils.getDefaultInt(map.get("allermatch_scheme"),0));
		custom.setDismatch(FieldUtils.getDefaultInt(map.get("dismatch_scheme"),0));
		custom.setFrematch(FieldUtils.getDefaultInt(map.get("freqmatch_scheme"),0));
		custom.setRoutematch(FieldUtils.getDefaultInt(map.get("routematch_scheme"),0));
		custom.setDoctormatch(FieldUtils.getDefaultInt(map.get("doctormatch_scheme"),0));
		custom.setOprmatch(FieldUtils.getDefaultInt(map.get("oprmatch_scheme"),0));
		custom.setMhiscode(FieldUtils.getDefaultLong(map.get("mhiscode"),0L));
		//costitemmatch_scheme,deptmatch_scheme,exammatch_scheme,labmatch_scheme,labsubmatch_scheme,doctorgroupmatch_scheme,wardmatch_scheme
		custom.setCostitemmatch(FieldUtils.getDefaultInt(map.get("costitemmatch_scheme"),0));
		custom.setDeptmatch(FieldUtils.getDefaultInt(map.get("deptmatch_scheme"),0));
		custom.setExammatch(FieldUtils.getDefaultInt(map.get("exammatch_scheme"),0));
		custom.setLabmatch(FieldUtils.getDefaultInt(map.get("labmatch_scheme"),0));
		custom.setLabsubmatch(FieldUtils.getDefaultInt(map.get("labsubmatch_scheme"),0));
		custom.setDoctorgroupmatch(FieldUtils.getDefaultInt(map.get("doctorgroupmatch_scheme"),0));
		custom.setWardmatch(FieldUtils.getDefaultInt(map.get("wardmatch_scheme"),0));
		return custom;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述： 执行更新所有的数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_MATCH);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysMatchRelation t = copyFromMap(list.get(i));
				String key =MATCH_TITLE+t.getHiscode().trim();
				this.addOrUpdate(t,key);
			}
		}
	}

}
