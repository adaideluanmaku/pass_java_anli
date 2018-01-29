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
import com.medicom.passlan.redis.custom.AdultCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  AdultInfaceImpl </li>
 * <li>类描述：   成人用药的数据定义成接口是为了尝试是否能用分布式的方式做处理</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月24日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class AdultInfaceImpl  extends CommonInface<AdultCustom>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_ADULT="select pkid,hiscode,drug_unique_code,drugname,agelow,agelow_unit,"
			+ "unequal_low,agehigh,agehigh_unit,unequal_high,agedesc,slcode,severity,warning from mc_user_adult where drug_unique_code <> '' and drug_unique_code is not null ";
	
	//查询成人用的数据
	private final String SEARCH_ADULTB="select pkid,hiscode,drug_unique_code,drugname,agelow,agelow_unit,"
				+ "unequal_low,agehigh,agehigh_unit,unequal_high,agedesc,slcode,severity,warning from mc_user_adult where drug_unique_code <> '' and drug_unique_code is not null and hiscode=:hiscode and  drug_unique_code=:druguniquecode  order by drug_unique_code";
	
	private final String ADULT_TITLE = "user_adult-";
	
	private Logger logger = Logger.getLogger(AdultInfaceImpl.class);
	
	
	public String getADULT_TITLE() {
		return ADULT_TITLE;
	}


	/**
	 * 成人用药的单个数据新增的方法
	 */
	public boolean addOrUpdate(AdultCustom t) {
		try{
			String key = ADULT_TITLE+t.getHiscode()+"-"+t.getDruguniquecode();
			List<AdultCustom> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
	
	
	

	/**
	 * 新增后者修改redis的数据
	 */
	public boolean addList(List<AdultCustom> list) {
		try{
			if(list!=null&&list.size()>0){
				AdultCustom t = list.get(0);
				String key = ADULT_TITLE+t.getHiscode()+"-"+t.getDruguniquecode();
				return this.addListCommon(list, key);
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}	
	}

	/**
	 * 去掉成人用药找到的当前的对象
	 */
	public boolean findbyList(List<AdultCustom> list, AdultCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AdultCustom temp = list.get(i);
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
	 * <li>功能描述：通过给定的条件初始化redis的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月27日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb  = new StringBuffer(SEARCH_ADULT);
				sb.append(" and hiscode=? and  drug_unique_code=?  order by drug_unique_code ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<AdultCustom> listobject = new ArrayList<AdultCustom>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						AdultCustom custom = copyFromMap(list.get(i));
						key = ADULT_TITLE+custom.getHiscode()+"-"+custom.getDruguniquecode();
						listobject.add(custom);
					}
				}
				if(key!=""){
					//进入保存redis的方法
					return this.addListCommon(listobject, key);
				}else{
					return false;
				}
			}else{
				return false;
			}
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
	public AdultCustom copyFromMap(Map<String,Object> map){
		AdultCustom adult = new AdultCustom();
		adult.setPkid(FieldUtils.getDefaultInt(map.get("pkid").toString(),0));
		adult.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		adult.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		adult.setDrugname(FieldUtils.getNullStr(map.get("drugname"),""));
		adult.setAgelow(FieldUtils.getNullStr(map.get("agelow"),""));
		adult.setAgelowunit(FieldUtils.getNullStr(map.get("agelow_unit"),""));
		adult.setUnequallow(FieldUtils.getDefaultInt(map.get("unequal_low"),0));
		adult.setAgehigh(FieldUtils.getNullStr(map.get("agehigh"),""));
		adult.setAgehighunit(FieldUtils.getNullStr(map.get("agehigh_unit"),""));
		adult.setUnequalhigh(FieldUtils.getDefaultInt(map.get("unequal_high"),0));
		adult.setAgedesc(FieldUtils.getNullStr(map.get("agedesc"),""));
		adult.setSlcode(Integer.parseInt(map.get("slcode").toString()));
		adult.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		adult.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return adult;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDataBean </li>
	 * <li>功能描述：初始化对应的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param druguniquecode
	 * @return
	 */
//	public boolean initDataBean(String hiscode,String druguniquecode){
//		try{
////			if(StringUtils.isNotBlank(hiscode)
////					&&StringUtils.isNotBlank(druguniquecode)){
////				AdultCustom custom = new AdultCustom();
////				custom.setHiscode(hiscode);
////				custom.setDruguniquecode(druguniquecode);
////				String key = ADULTTITLE+custom.getHiscode()+"-"+custom.getDruguniquecode();
////				List<AdultCustom> list = this.initDateList(custom, SEARCH_ADULTB);
////				return this.addListCommon(list, key);
////			}else{
////				return false;
////			}
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
//	}
	public boolean deleteObject(AdultCustom t){
		String key = ADULT_TITLE+t.getHiscode()+"-"+t.getDruguniquecode();
		List<AdultCustom> list = this.get(key);
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
				StringBuffer sb  = new StringBuffer(SEARCH_ADULT);
				sb.append(" and pkid=? order by drug_unique_code ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					AdultCustom t = copyFromMap(list.get(0));
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
	
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAllDate </li>
	 * <li>功能描述： 初始化所有自定义的数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月12日 </li>
	 * </ul> 
	 * @return
	 */
	public boolean initAllDate(){
		try{
			List<Map<String,Object>> list =  jdbcTemplate.queryForList(SEARCH_ADULT);
			Map<String,List<AdultCustom>> map = new HashMap<String,List<AdultCustom>>();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					AdultCustom custom = copyFromMap(list.get(i));
					String key = ADULT_TITLE+custom.getHiscode()+"-"+custom.getDruguniquecode();
					if(map.get(key)!=null){
						map.get(key).add(custom);
					}else{
						List<AdultCustom> listcustom  = new ArrayList<AdultCustom>();
						listcustom.add(custom);
						map.put(key, listcustom);
					}
				}
				this.addMapinListCommon(map);
				return true;
			}
		}catch(Exception e){
			
		}
		return true;
	}
	
	

}
