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
import com.medicom.passlan.redis.custom.UnagespCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  UnagespInfaceImpl </li>
 * <li>类描述：   </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class UnagespInfaceImpl extends CommonInface<UnagespCustom>{
	
private Logger logger = Logger.getLogger(UnagespInfaceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String UNAGESP_TITLE = "user_unagesp-";
	
	private final String SEARCH_UNAGESP = "select pkid,hiscode,moduleid,drug_unique_code,drugname,slcode,severity,warning from mc_user_unage_sp ";

	public boolean addOrUpdate(UnagespCustom t) {
		try{
			String key = UNAGESP_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
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
	public boolean initDateByCon(int pkid,int moduleid){
		try{
			StringBuffer sb  = new StringBuffer(SEARCH_UNAGESP);
			if(moduleid==12){
				sb.append("  where moduleid=12 and drug_unique_code is not null and drug_unique_code <> '' and pkid =? ");
			}else if(moduleid==13){
				sb.append("  where moduleid=13 and drug_unique_code is not null and drug_unique_code <> '' and pkid =? order by hiscode, drug_unique_code");
			}else if(moduleid==14){
				sb.append("  where moduleid=14 and pkid =? ");
			}else{
				return false;
			}
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					UnagespCustom t = copyFromMap(list.get(0));
					String key =  UNAGESP_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
					return this.addOrUpdate(t, key);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	
	public UnagespCustom copyFromMap(Map<String,Object> map){
		UnagespCustom custom = new UnagespCustom();
		//pkid,hiscode,moduleid,drug_unique_code,drugname,slcode,severity,warning
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"), 0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setModuleid(FieldUtils.getDefaultInt(map.get("moduleid"), 0));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDrugname(FieldUtils.getNullStr(map.get("drugname"),""));
		custom.setSlcode(FieldUtils.getDefaultInt(map.get("slcode"), 0));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	
	public boolean deleteId(Integer pkid,int moduleid){
		try{
			StringBuffer sb  = new StringBuffer(SEARCH_UNAGESP);
			if(moduleid==12){
				sb.append("  where moduleid=12 and drug_unique_code is not null and drug_unique_code <> '' and pkid =? ");
			}else if(moduleid==13){
				sb.append("  where moduleid=13 and drug_unique_code is not null and drug_unique_code <> '' and pkid =? order by hiscode, drug_unique_code");
			}else if(moduleid==14){
				sb.append("  where moduleid=14 and pkid =? ");
			}else{
				return false;
			}
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					UnagespCustom t = copyFromMap(list.get(0));
					String key =  UNAGESP_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
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
	
	public boolean deleteAll(String hiscode,String druguniquecode,String distype){
		try{
			StringBuffer sb  = new StringBuffer(SEARCH_UNAGESP);
			if(distype.equals("12")){
				sb.append("  where moduleid=12 and drug_unique_code is not null and drug_unique_code <> '' and hiscode=? and drug_unique_code =? ");
			}else if(distype.equals("13")){
				sb.append("  where moduleid=13 and drug_unique_code is not null and drug_unique_code <> '' and hiscode=? and drug_unique_code =? order by hiscode, drug_unique_code");
			}else if(distype.equals("14")){
				sb.append("  where moduleid=14 and hiscode=? and drug_unique_code =? ");
			}else{
				return false;
			}
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					UnagespCustom t = copyFromMap(list.get(0));
					String key =  UNAGESP_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
					this.delete(key);
				}
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	
	public boolean initAllDate(int moduleid){
		try{
			StringBuffer sb  = new StringBuffer(SEARCH_UNAGESP);
			if(moduleid==12){
				sb.append("  where moduleid=12 and drug_unique_code is not null and drug_unique_code <> ''  ");
			}else if(moduleid==13){
				sb.append("  where moduleid=13 and drug_unique_code is not null and drug_unique_code <> '' order by hiscode, drug_unique_code");
			}else if(moduleid==14){
				sb.append("  where moduleid=14  ");
			}else{
				return false;
			}
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					UnagespCustom t = copyFromMap(list.get(i));
					String key =  UNAGESP_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
					this.addOrUpdate(t, key);
				}
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
