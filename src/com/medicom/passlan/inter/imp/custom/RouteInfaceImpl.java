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
import com.medicom.passlan.redis.custom.RouteCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RouteInfaceImpl </li>
 * <li>类描述：   给药途径自定义更新数据的方法</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class RouteInfaceImpl extends  CommonInface<RouteCustom>{
	
   private Logger logger = Logger.getLogger(RouteInfaceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String ROUTE_TITLE = "user_drugroute-";
	
	private final String SEARCH_ROUTE = "select pkid,hiscode,drug_unique_code,routecode,drugname,"
			+ "routename,slcode,severity,warning  "
			+ "from mc_user_drugroute  where drug_unique_code <> '' ";

	public boolean addOrUpdate(RouteCustom t) {
		try{
			String key = ROUTE_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutecode();
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
	 * <li>功能描述： 更新给药途径弄得redis的方法</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月30日 </li>
	 * </ul> 
	 * @param pkid  主键ID
	 * @return
	 */
	public boolean initDateByCon(int pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb = new StringBuffer(SEARCH_ROUTE);
				sb.append(" and pkid = ? order by hiscode, drug_unique_code, routecode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					RouteCustom t = copyFromMap(list.get(0));
					String key = ROUTE_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutecode();
					 this.addOrUpdate(t, key);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return false;

	}
	
	
	public RouteCustom copyFromMap(Map<String,Object> map){
		RouteCustom custom = new RouteCustom();
		//"select pkid,hiscode,drug_unique_code,routecode,drugname,"
		//+ "routename,slcode,severity,warning  "
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"), 0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
		custom.setDrugname(FieldUtils.getNullStr(map.get("drugname"),""));
		custom.setRoutename(FieldUtils.getNullStr(map.get("routename"),""));
		custom.setSlcode(FieldUtils.getDefaultInt(map.get("slcode"), 0));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	public boolean deleteId(Integer pkid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))&&pkid>0){
				StringBuffer sb = new StringBuffer(SEARCH_ROUTE);
				sb.append(" and pkid = ? order by hiscode, drug_unique_code, routecode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					RouteCustom t = copyFromMap(list.get(0));
					String key = ROUTE_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutecode();
					return this.delete(key);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}
	
	public boolean deleteAll(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(SEARCH_ROUTE);
				sb.append(" and hiscode = ? and drug_unique_code=? order by hiscode, drug_unique_code, routecode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					RouteCustom t = copyFromMap(list.get(0));
					String key = ROUTE_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutecode();
					return this.delete(key);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}
	
	
	public boolean initAllDate(){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_ROUTE);
			sb.append(" order by hiscode, drug_unique_code, routecode ");
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					RouteCustom t = copyFromMap(list.get(i));
					String key = ROUTE_TITLE+t.getHiscode().trim()+"-"+ StringUtils.trim(t.getDruguniquecode()) +"-"+t.getRoutecode();
					this.addOrUpdate(t, key);
				}
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	

}
