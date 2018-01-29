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
import com.medicom.passlan.redis.custom.DosageCustom;
import com.medicom.passlan.util.FieldUtils;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  DosageInfaceImpl </li>
 * <li>类描述：   剂量范围自定义的数据</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class DosageInfaceImpl extends CommonInface<DosageCustom> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_DOSAGE="select pkid,hiscode,drug_unique_code,"
			+ "drugname,doseunit,routecode,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,agedesc,"
			+ "calculate_label,dose_each_low,dose_each_low_unit"
			+ ",dose_each_high,dose_each_high_unit,maxdose_each,maxdose_each_unit"
			+ ",frequency_low,frequency_low_day,frequency_high,frequency_high_day"
			+ ",dose_day_low,dose_day_low_unit,dose_day_high,dose_day_high_unit,"
			+ "maxdose_day,maxdose_day_unit,duration_low,duration_high,duration_max,treatment_dose,treatment_dose_unit from mc_user_dosage ";
	
	private final String DOSAGE_TITLE = "user_dosage-";
	
	
	public String getDOSAGE_TITLE() {
		return DOSAGE_TITLE;
	}

	private Logger logger = Logger.getLogger(DosageInfaceImpl.class);

	public boolean addOrUpdate(DosageCustom t) {
		try{
			String key = DOSAGE_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()+"-"+t.getRoutecode();
			List<DosageCustom> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}


	public boolean findbyList(List<DosageCustom> list, DosageCustom t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DosageCustom temp = list.get(i);
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
	 * <li>功能描述： </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param druguniquecode
	 * @param doseunit
	 * @param routecode
	 * @return
	 */
	public boolean initDateByCon(String hiscode,String druguniquecode,
			String doseunit,String routecode){
		try{
			if(StringUtils.isNotBlank(hiscode)){
				StringBuffer sb = new StringBuffer(SEARCH_DOSAGE);
				sb.append(" where hiscode=? and drug_unique_code=? and doseunit=? and routecode=?  ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode.trim());
				listvalue.add(druguniquecode.trim());
				listvalue.add(doseunit.trim());
				listvalue.add(routecode.trim());
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<DosageCustom> listobject = new ArrayList<DosageCustom>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						DosageCustom custom = copyFromMap(list.get(i));
						key = DOSAGE_TITLE+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim()+"-"+custom.getDoseunit()+"-"+custom.getRoutecode();
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
	
	public DosageCustom copyFromMap(Map<String,Object> map){
		DosageCustom custom = new DosageCustom();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"),0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		custom.setDrugname(FieldUtils.getNullStr(map.get("drugname"),""));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit"),""));
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
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
		custom.setMaxdoseeach(FieldUtils.getDefaultF(map.get("maxdose_each"), 0));
		custom.setMaxdoseeachunit(FieldUtils.getNullStr(map.get("maxdose_each_unit"),""));
		custom.setFrequencylow(FieldUtils.getDefaultInt(map.get("frequency_low"),0));
		custom.setFrequencylowday(FieldUtils.getDefaultInt(map.get("frequency_low_day"),0));
		custom.setFrequencyhigh(FieldUtils.getDefaultInt(map.get("frequency_high"),0));
		custom.setFrequencyhighday(FieldUtils.getDefaultInt(map.get("frequency_high_day"),0));
		custom.setDosedaylow(FieldUtils.getDefaultF(map.get("dose_day_low"), 0));
		custom.setDosedaylowunit(FieldUtils.getNullStr(map.get("dose_day_low_unit"),""));
		custom.setDosedayhigh(FieldUtils.getDefaultF(map.get("dose_day_high"), 0));
		custom.setDosedayhighunit(FieldUtils.getNullStr(map.get("dose_day_high_unit"), ""));
		custom.setMaxdoseday(FieldUtils.getDefaultF(map.get("maxdose_day"), 0));
		custom.setMaxdosedayunit(FieldUtils.getNullStr(map.get("maxdose_day_unit"), ""));
		custom.setDurationLow(FieldUtils.getDefaultInt(map.get("duration_low"),0));
		custom.setDurationHigh(FieldUtils.getDefaultInt(map.get("duration_high"),0));
		custom.setDurationMax(FieldUtils.getDefaultInt(map.get("duration_max"),0));
		custom.setTreatmentDose(FieldUtils.getDefultDou(map.get("treatment_dose"),0));
		custom.setTreatmentDoseUnit(FieldUtils.getNullStr(map.get("treatment_dose_unit"), ""));
		return custom;
	}
	
	
	
	public boolean deleteObject(DosageCustom t){
		String key = DOSAGE_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()+"-"+t.getRoutecode();
		List<DosageCustom> list = this.get(key);
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
				StringBuffer sb  = new StringBuffer(SEARCH_DOSAGE);
				sb.append(" where pkid=?  ");
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), pkid);
				if(list!=null&&list.size()>0){
					DosageCustom t = copyFromMap(list.get(0));
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
				StringBuffer sb  = new StringBuffer(SEARCH_DOSAGE);
				sb.append(" where hiscode=? and drug_unique_code=?  ");
				List<Object> listvalue = new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(druguniquecode);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						DosageCustom t = copyFromMap(list.get(i));
						String key = DOSAGE_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()+"-"+t.getRoutecode();
						this.delete(key);
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
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述：可以单独更新数据也可以更新全部数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月12日 </li>
	 * </ul> 
	 * @param hiscode
	 */
	public void initAll(String hiscode){
		List<Object> listvalue = new ArrayList<Object>();
		StringBuffer sb  = new StringBuffer(SEARCH_DOSAGE);
		if(StringUtils.isNotBlank(hiscode)){
			sb.append(" where hiscode=?   ");
			listvalue.add(hiscode);
		}
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(),listvalue.toArray());
		if(list!=null&&list.size()>0){
			Map<String,List<DosageCustom>> maps  = new HashMap<String, List<DosageCustom>>();
			for(int i=0;i<list.size();i++){
				DosageCustom t = copyFromMap(list.get(i));
				String key =DOSAGE_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim()+"-"+t.getDoseunit()+"-"+t.getRoutecode();
				if(maps.containsKey(key)){
					maps.get(key).add(t);
				 }else{
				   List z = new ArrayList();
				   z.add(t);
				   maps.put(key,z);
				}
			}
			this.addMapinListCommon(maps);
		}
	}
	

}
