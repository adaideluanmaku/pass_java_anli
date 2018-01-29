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
import com.medicom.passlan.redis.custom.HepdosCustom;
import com.medicom.passlan.util.FieldUtils;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  HepdosInfaceImpl </li>
 * <li>类描述：  肝损害更新redis的方法 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class HepdosInfaceImpl extends CommonInface<HepdosCustom>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_HEPDOS="select pkid,hiscode,drug_unique_code,"
			+ "doseunit,routecode,hep_label,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,agedesc,"
			+ "calculate_label,dose_each_low,dose_each_low_unit"
			+ ",dose_each_high,dose_each_high_unit,"
			+ "dose_day_low,dose_day_low_unit,dose_day_high,dose_day_high_unit,"
			+ "frequency_low,frequency_low_day,frequency_high,frequency_high_day"
		    +" from mc_user_hepdos ";
	
	private final String HEPDOS_TITLE = "user_hepdos-";
	
	private Logger logger = Logger.getLogger(HepdosInfaceImpl.class);
	
	
	
	public boolean addOrUpdate(HepdosCustom t) {
		try{
			String key =HEPDOS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()
			+"-"+t.getDoseunit()+"-"+t.getRoutecode()+"-"+t.getHeplabel();
			List<HepdosCustom> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}


	public boolean findbyList(List<HepdosCustom> list, HepdosCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				HepdosCustom temp = list.get(i);
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
	 * <li>功能描述：根据传入的数据更新表的字段 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月30日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param druguniquecode
	 * @param doseunit
	 * @param routecode
	 * @param heplabel
	 * @return
	 */
	public boolean initDateByCon(String hiscode,String druguniquecode,String doseunit,
			String routecode,String heplabel){
		try{
			if(StringUtils.isNotBlank(hiscode)){
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append(" where hiscode=? and druguniquecode=? and doseunit=? and  routecode=? and hep_label=?   order by pkid ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				listvalue.add(doseunit);
				listvalue.add(routecode);
				listvalue.add(heplabel);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<HepdosCustom> listobject = new ArrayList<HepdosCustom>();
				String key  = HEPDOS_TITLE+hiscode.trim()+"-"+druguniquecode.trim()
				+"-"+doseunit+"-"+routecode+"-"+heplabel;
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						HepdosCustom custom = copyFromMap(list.get(i));
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
			e.printStackTrace();
			return false;
		}
	}
	
	public HepdosCustom copyFromMap(Map<String,Object> map){
		HepdosCustom custom = new HepdosCustom();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid").toString(),0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit"),""));
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
		custom.setHeplabel(FieldUtils.getDefaultInt(map.get("hep_label"),0));
		custom.setAgelow(FieldUtils.getNullStr(map.get("agelow"),""));
		custom.setUnequallow(FieldUtils.getDefaultInt(map.get("unequal_low"),0));
		custom.setAgelowunit(FieldUtils.getNullStr(map.get("agelow_unit"),""));
		custom.setAgehigh(FieldUtils.getNullStr(map.get("agehigh"),""));
		custom.setUnequalhigh(FieldUtils.getDefaultInt(map.get("unequal_high"),0));
		custom.setAgehighunit(FieldUtils.getNullStr(map.get("agehigh_unit"),""));
		custom.setAgedesc(FieldUtils.getNullStr(map.get("agedesc"),""));
		custom.setCalculatelabel(FieldUtils.getDefaultInt(map.get("calculate_label"),0));
		custom.setDose_eachlow(FieldUtils.getDefaultF(map.get("dose_each_low"), 0));
		custom.setDoseeachlowunit(FieldUtils.getNullStr(map.get("dose_each_low_unit"),""));
		custom.setDoseeachhigh(FieldUtils.getDefaultF(map.get("dose_each_high"), 0));
		custom.setDoseeachhighunit(FieldUtils.getNullStr(map.get("dose_each_low_unit"),""));
		custom.setFrequencylow(FieldUtils.getDefaultInt(map.get("frequency_low"),0));
		custom.setFrequencylowday(FieldUtils.getDefaultInt(map.get("frequency_low_day"),0));
		custom.setFrequencyhigh(FieldUtils.getDefaultInt(map.get("frequency_high"),0));
		custom.setFrequencyhighday(FieldUtils.getDefaultInt(map.get("frequency_high_day"),0));
		custom.setDosedaylow(FieldUtils.getDefaultF(map.get("dose_day_low"), 0));
		custom.setDosedaylowunit(FieldUtils.getNullStr(map.get("dose_day_low_unit"),""));
		custom.setDosedayhigh(FieldUtils.getDefaultF(map.get("dose_day_high"), 0));
		custom.setDosedayhighunit(FieldUtils.getNullStr(map.get("dose_day_high_unit"), ""));
		return custom;
	}
	
	
	public boolean deleteObject(HepdosCustom t){
		String key = HEPDOS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()
		+"-"+t.getDoseunit()+"-"+t.getRoutecode()+"-"+t.getHeplabel();
		List<HepdosCustom> list = this.get(key);
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
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append(" and pkid=? order by pkid");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					HepdosCustom t = copyFromMap(list.get(0));
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
	 * <li>方法名：  deleteAll </li>
	 * <li>功能描述：删除所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月11日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param drugunitCode
	 * @return
	 */
	public boolean deleteAll(String hiscode,String drugunitCode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(drugunitCode)){
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append(" and hiscode=?  and  drug_unique_code=? order by pkid");
				List<Object> listvalue = new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(drugunitCode);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						HepdosCustom t = copyFromMap(list.get(i));
						String key =HEPDOS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()
						+"-"+t.getDoseunit()+"-"+t.getRoutecode()+"-"+t.getHeplabel();
						delete(key);
					}
					
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
	
	
	public boolean initAllDate(String hiscode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
			Map<String,List<HepdosCustom>> map = new HashMap<String,List<HepdosCustom>>();
			List<Object> listvalue =new ArrayList<Object>();
			if(StringUtils.isNotBlank(hiscode)){
				sb.append(" where hiscode=?    order by pkid ");
				listvalue.add(hiscode);
			}else{
				sb.append(" order by pkid ");
			}
			List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					HepdosCustom custom = copyFromMap(list.get(i));
					String key = HEPDOS_TITLE+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim()
							+"-"+custom.getDoseunit().trim()+"-"+custom.getRoutecode().trim()+"-"+custom.getHeplabel();
//					if(key.equals("user_hepdos-001002-ZDY-70487+氢溴酸西酞普兰片+片+20mg(西酞普兰)+四川科伦药业股份有限公司-mg-ZDY-122-1")){
//						System.out.println(key);
//					}
					if(map.get(key)!=null){
						map.get(key).add(custom);
					}else{
						List<HepdosCustom> listcustom = new ArrayList<HepdosCustom>();
						listcustom.add(custom);
						map.put(key, listcustom);
					}
				}
				this.addMapinListCommon(map);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
	
	
}
