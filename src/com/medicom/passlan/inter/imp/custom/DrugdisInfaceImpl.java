package com.medicom.passlan.inter.imp.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.custom.DrugDisCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  DrugdisInfaceImpl </li>
 * <li>类描述：   超适应保存到redis的方法</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月30日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class DrugdisInfaceImpl extends CommonInface<DrugDisCustom>{
	
	private Logger logger = Logger.getLogger(DrugdisInfaceImpl.class);
	
	//查询成人用的数据
	private final String SEARCH_DRUGDIS= "select pkid,hiscode,drug_unique_code,"
			+ "moduleid,drugname,discode,disname,slcode,severity,warning from mc_user_drug_dis"
			+ "  where  drug_unique_code is not null and drug_unique_code <> '' and discode is not null and discode <> ' ' ";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DRUGDIS_TITLE = "user_drugdis-";
	
	public String getDRUGDIS_TITLE() {
		return DRUGDIS_TITLE;
	}

	public boolean addOrUpdate(DrugDisCustom t) {
		try{
			String key = DRUGDIS_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
			List<DrugDisCustom> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			return false;
		}
	}

	public boolean addList(List<DrugDisCustom> list) {
		try{
			if(list!=null&&list.size()>0){
				DrugDisCustom t = list.get(0);
				String key = DRUGDIS_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
				return this.addListCommon(list, key);
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	public boolean findbyList(List<DrugDisCustom> list, DrugDisCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DrugDisCustom temp = list.get(i);
				if(temp.getPkid()==t.getPkid()){
					//这里去掉重复找到的成人用药的对象
					list.remove(temp);
					isSure =  true;
				}
			}
		}
		return isSure;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述： 通过给定的条件初始化redis的数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param hiscode 医院
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(int moduleid,String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(SEARCH_DRUGDIS);
				sb.append(" and moduleid =? and hiscode=? and drug_unique_code=? "
			+ " order by hiscode, drug_unique_code, discode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(moduleid);
				listvalue.add(hiscode.trim());
				listvalue.add(druguniquecode.trim());
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<DrugDisCustom> listobject = new ArrayList<DrugDisCustom>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						DrugDisCustom custom = copyFromMap(list.get(i));
						key = DRUGDIS_TITLE+custom.getModuleid()+"-"+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim();
						listobject.add(custom);
					}
				}
				if(key!=""){
					return this.addListCommon(listobject, key);
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  copyFromMap </li>
	 * <li>功能描述：把数据库的字段拷贝到对象 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param map
	 * @return
	 */
	public DrugDisCustom copyFromMap(Map<String,Object> map){
		DrugDisCustom custom = new DrugDisCustom();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"), 0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setModuleid(FieldUtils.getDefaultInt(map.get("moduleid"), 0));
		custom.setDrugname(FieldUtils.getNullStr(map.get("drugname"),""));
		custom.setDiscode(FieldUtils.getNullStr(map.get("discode"),""));
		custom.setDisname(FieldUtils.getNullStr(map.get("disname"),""));
		custom.setSlcode(FieldUtils.getDefaultInt(map.get("slcode"), 0));
		custom.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	
	public boolean deleteObject(DrugDisCustom t){
		String key = DRUGDIS_TITLE+t.getModuleid()+"-"+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
		List<DrugDisCustom> list = this.get(key);
		this.findbyList(list,t);
		if(list!=null&&list.size()==0){
			return this.delete(key);
		}else if(list!=null)
		{
			return this.addOrUpdateList(list,key);
		}else{
			return false;
		}
	}
	
	public boolean deleteId(Integer pkid){
		try{
			if(pkid>0){
				StringBuffer sb  = new StringBuffer(SEARCH_DRUGDIS);
				sb.append(" and pkid=? order by hiscode, drug_unique_code, discode ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				//jdbcTemplate.execute(action)
				if(list!=null&&list.size()>0){
					DrugDisCustom t = copyFromMap(list.get(0));
					return deleteObject(t);
				}
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}
	
	
	public boolean initAllDate(int moduleid){
		try{
			if(moduleid>0){
				
				StringBuffer sb = new StringBuffer(SEARCH_DRUGDIS);
				sb.append(" and moduleid =?  order by hiscode, drug_unique_code, discode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(moduleid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				
				if(list!=null&&list.size()>0){
					Map<String,List<DrugDisCustom>> map = new HashMap<String,List<DrugDisCustom>>();
					for(int i=0;i<list.size();i++){
						DrugDisCustom custom = copyFromMap(list.get(i));
						String key = DRUGDIS_TITLE+custom.getModuleid()+"-"+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim();
						if(map.get(key)!=null){
							map.get(key).add(custom);
						}else{
							List<DrugDisCustom> listobject = new ArrayList<DrugDisCustom>();
							listobject.add(custom);
							map.put(key, listobject);
						}
					}
					this.addMapinListCommon(map);
				}
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
	}
	

}
