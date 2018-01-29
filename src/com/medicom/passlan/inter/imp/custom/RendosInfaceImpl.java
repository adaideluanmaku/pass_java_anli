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
import com.medicom.passlan.redis.custom.RendosCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RendosInfaceImpl </li>
 * <li>类描述：   肾损害更新redis的方法</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class RendosInfaceImpl extends CommonInface<RendosCustom>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_HEPDOS="select pkid,hiscode,drug_unique_code,drugname,"
			+ "doseunit,routecode,ren_label,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,agedesc,"
			+ "calculate_label,dose_each_low,dose_each_low_unit"
			+ ",dose_each_high,dose_each_high_unit,"
			+ "dose_day_low,dose_day_low_unit,dose_day_high,dose_day_high_unit,"
			+ "frequency_low,frequency_low_day,frequency_high,frequency_high_day"
		    +" from mc_user_rendos ";
	
	private final String RENDOS_TITLE = "user_rendos-";
	
	private Logger logger = Logger.getLogger(RendosInfaceImpl.class);
	
	
	
	public boolean addOrUpdate(RendosCustom t) {
		try{
			String key =RENDOS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()
			+"-"+t.getDoseunit()+"-"+t.getRoutecode()+"-"+t.getRenlabel();
			List<RendosCustom> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}


	public boolean findbyList(List<RendosCustom> list, RendosCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				RendosCustom temp = list.get(i);
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
			String routecode,String renlabel){
		try{
			if(StringUtils.isNotBlank(hiscode)){
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append(" where hiscode=? and druguniquecode=? and doseunit=? and  routecode=? and ren_label=?   order by pkid ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				listvalue.add(doseunit);
				listvalue.add(routecode);
				listvalue.add(renlabel);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<RendosCustom> listobject = new ArrayList<RendosCustom>();
				String key  = RENDOS_TITLE+hiscode.trim()+"-"+druguniquecode.trim()
				+"-"+doseunit+"-"+routecode+"-"+renlabel;
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						RendosCustom custom = copyFromMap(list.get(i));
						listobject.add(custom);
					}
				}else{
					return false;
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
	
	public RendosCustom copyFromMap(Map<String,Object> map){
		RendosCustom custom = new RendosCustom();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"),0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit"),""));
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
		custom.setRenlabel(FieldUtils.getDefaultInt(map.get("ren_label"),0));
		custom.setAgelow(FieldUtils.getNullStr(map.get("agelow"),""));
		custom.setUnequallow(FieldUtils.getDefaultInt(map.get("unequal_low"),0));
		custom.setAgelowunit(FieldUtils.getNullStr(map.get("agelow_unit"),""));
		custom.setAgehigh(FieldUtils.getNullStr(map.get("agehigh"),""));
		custom.setUnequalhigh(FieldUtils.getDefaultInt(map.get("unequal_high"),0));
		custom.setAgehighunit(FieldUtils.getNullStr(map.get("agehigh_unit"),""));
		custom.setAgedesc(FieldUtils.getNullStr(map.get("agedesc"),""));
		custom.setCalculatelabel(FieldUtils.getDefaultInt(map.get("calculate_label"),0));
		custom.setDoseeachlow(FieldUtils.getDefaultF(map.get("dose_each_low"), 0));
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
	
	
	public boolean deleteObject(RendosCustom t){
		String key = RENDOS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()
		+"-"+t.getDoseunit()+"-"+t.getRoutecode()+"-"+t.getRenlabel();
		List<RendosCustom> list = this.get(key);
		this.findbyList(list,t);
		if(list!=null&&list.size()==0){
			return this.delete(key);
		}else if(list!=null){
			return this.addOrUpdateList(list,key);
		}else{
			return false;
		}
	}
	
	public boolean deleteId(Integer pkid){
		try{
			if(pkid>0){
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append("where pkid=? order by pkid ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					RendosCustom t = copyFromMap(list.get(0));
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
	
	public boolean deleteAll(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append("where hiscode=? and drug_unique_code=?  order by pkid ");
				List<Object> listvalue = new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						RendosCustom t = copyFromMap(list.get(i));
						String key = RENDOS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()
						+"-"+t.getDoseunit()+"-"+t.getRoutecode()+"-"+t.getRenlabel();
						delete(key);
					}
					return true;
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
	
	
	public boolean initAllDate(){
		try{
				Map<String,List<RendosCustom>> map = new HashMap<String,List<RendosCustom>>();
				StringBuffer sb = new StringBuffer(SEARCH_HEPDOS);
				sb.append(" order by pkid ");
				List<Object> listvalue =new ArrayList<Object>();
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						RendosCustom custom = copyFromMap(list.get(i));
						String key  = RENDOS_TITLE+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim()
						+"-"+custom.getDoseunit()+"-"+custom.getRoutecode()+"-"+custom.getRenlabel();
						if(map.get(key)!=null){
							map.get(key).add(custom);
						}else{
							List<RendosCustom> listobject = new ArrayList<RendosCustom>();
							listobject.add(custom);
							map.put(key, listobject);
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
