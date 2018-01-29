package com.medicom.passlan.inter.imp.sys;

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
import com.medicom.passlan.redis.sys.SysDrugMatch;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  SysDrugMatchImpl </li>
 * <li>类描述：   药品配对的数据</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class SysDrugMatchImpl extends CommonInface<SysDrugMatch>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_DRUGMATCH="SELECT a.match_scheme, a.drug_unique_code as u_unique_code,"
			+ "a.drugcode as u_code, a.drugname as u_name,a.doseunit as u_doseunit," 
			+ "a.drugform as u_form, a.drugspec as u_strength, "
			+ "a.comp_name as u_comp_name,a.approvalcode as u_approvalcode,"
			+ " a.pass_dividend, a.pass_divisor, a.pass_doseunit as doseunit,a.pass_drugcode as drugcode "
			+ " FROM mc_dict_drug_pass a";
	
	private final String DRUGMATCH_TITLE = "drug_pass-";
	
	private Logger logger = Logger.getLogger(SysDrugMatchImpl.class);
	
	
	
	public boolean addOrUpdate(SysDrugMatch t) {
		try{
			String key =DRUGMATCH_TITLE+String.valueOf(t.getDrugmatchscheme())+"-"+String.valueOf(t.getUuniqueCode().trim());
			List<SysDrugMatch> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}


	public boolean findbyList(List<SysDrugMatch> list, SysDrugMatch t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysDrugMatch temp = list.get(i);
				if(String.valueOf(t.getDrugmatchscheme())==String.valueOf(temp.getDrugmatchscheme())
						&&t.getUuniqueCode()==temp.getUuniqueCode()
						&&t.getDoseunit()==temp.getDoseunit()){
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
	public boolean initDateByCon(int drugmatchscheme,String druguniquecode){
		try{
			List<Map<String,Object>> list =  null;
			if(StringUtils.isNotBlank(String.valueOf(drugmatchscheme))){
				StringBuffer sb = new StringBuffer(SEARCH_DRUGMATCH);
				sb.append(" where a.match_scheme=? and  a.drug_unique_code=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(drugmatchscheme);
				listvalue.add(druguniquecode);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<SysDrugMatch> listobject = new ArrayList<SysDrugMatch>();
				String key =DRUGMATCH_TITLE+String.valueOf(drugmatchscheme)+"-"+String.valueOf(druguniquecode.trim());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						SysDrugMatch custom = copyFromMap(list.get(i));
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
	
	public SysDrugMatch copyFromMap(Map<String,Object> map){
		SysDrugMatch custom = new SysDrugMatch();
		custom.setDrugmatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setUuniqueCode(FieldUtils.getNullStr(map.get("u_unique_code"),""));
		custom.setUcode(FieldUtils.getNullStr(map.get("u_code"),""));
		custom.setUname(FieldUtils.getNullStr(map.get("u_name"),""));
		custom.setUdoseunit(FieldUtils.getNullStr(map.get("u_doseunit"),""));
		custom.setUform(FieldUtils.getNullStr(map.get("u_form"),""));
		custom.setUstrength(FieldUtils.getNullStr(map.get("u_strength"),""));
		custom.setUcompname(FieldUtils.getNullStr(map.get("u_comp_name"),""));
		custom.setUapprovalcode(FieldUtils.getNullStr(map.get("u_approvalcode"),""));
		custom.setPassdividend(FieldUtils.getDefultDou(map.get("pass_dividend"),0));
		custom.setPassdivisor(FieldUtils.getDefultDou(map.get("pass_divisor"),0));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit"),""));
		custom.setDrugcode(FieldUtils.getNullStr(map.get("drugcode"),""));
		return custom;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述： 初始化药品数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DRUGMATCH);
		if(list!=null&&list.size()>0){
			Map<String,List<SysDrugMatch>> maps  = new HashMap<String, List<SysDrugMatch>>();
			for(int i=0;i<list.size();i++){
				SysDrugMatch t = copyFromMap(list.get(i));
				String key = DRUGMATCH_TITLE+String.valueOf(t.getDrugmatchscheme())+"-"+String.valueOf(t.getUuniqueCode().trim());
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
