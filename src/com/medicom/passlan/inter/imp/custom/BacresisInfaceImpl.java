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
import com.medicom.passlan.redis.custom.Bacresis;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  BacresisInfaceImpl </li>
 * <li>类描述：   细菌耐药率的生成</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月27日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class BacresisInfaceImpl extends CommonInface<Bacresis>{
	
	//这个准备添加一个直接根据给的字符串在查数据库操作
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String BACRESIS_TITLE = "user_bacresis-";
	
	private final String SEARCH_BACRESIS = "select distinct a.hiscode,b.drug_unique_code,b.drugname,a.whodrugcode,a.whodrugname,a.bacterianame,a.resistrate " 
+" from mc_user_bacresis a, mc_user_match_whonet b  where a.hiscode=b.hiscode and a.whodrugcode=b.whodrugcode ";

	private Logger logger = Logger.getLogger(BacresisInfaceImpl.class);
	
	
	public String getBACRESIS_TITLE() {
		return BACRESIS_TITLE;
	}

	public boolean addOrUpdate(Bacresis t) {
		try{
			String key = BACRESIS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
			List<Bacresis> list = this.get(key);
			this.findbyList(list,t);
			list.add(t);
			return this.addOrUpdateList(list,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public boolean deleteObject(Bacresis t){
		String key = BACRESIS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
		List<Bacresis> list = this.get(key);
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
	public boolean addList(List<Bacresis> list) {
		try{
			if(list!=null&&list.size()>0){
				Bacresis t = list.get(0);
				String key = BACRESIS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
				return this.addListCommon(list, key);
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean findbyList(List<Bacresis> list, Bacresis t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Bacresis temp = list.get(i);
				if(temp.getHiscode().equals(t.getHiscode())
						&&temp.getDruguniquecode().equals(t.getDruguniquecode())
						&&temp.getWhodrugcode().equals(t.getWhodrugcode())){
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
	public boolean initDateByCon(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(SEARCH_BACRESIS);
				sb.append(" and a.hiscode=? and b.drug_unique_code=?  order by a.hiscode, b.drug_unique_code, resistrate desc");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode.trim());
				listvalue.add(druguniquecode.trim());
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				List<Bacresis> listobject = new ArrayList<Bacresis>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Bacresis custom = copyFromMap(list.get(i));
						key = BACRESIS_TITLE+custom.getHiscode().trim()+"-"+custom.getDruguniquecode().trim();
						listobject.add(custom);
					}
				}
				if(key!=""){
					return this.addListCommon(listobject, key);
				}else{
					//这里删除 key值
					key = BACRESIS_TITLE+hiscode.trim()+"-"+druguniquecode.trim();
					return this.delete(key);
					//return false;
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
	 * <li>功能描述：把数据库的字段拷贝到对象 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param map
	 * @return
	 */
	public Bacresis copyFromMap(Map<String,Object> map){
		Bacresis bacresis = new Bacresis();
//		a.hiscode,b.drug_unique_code,b.drugname,a.whodrugcode,a.whodrugname,a.bacterianame,a.resistrate " 
//				+" from mc_user_bacresis a, mc_user_match_whonet b
		bacresis.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		bacresis.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		bacresis.setDrugname(FieldUtils.getNullStr(map.get("drugname"),""));
		bacresis.setWhodrugcode(FieldUtils.getNullStr(map.get("whodrugcode"),""));
		bacresis.setWhodrugname(FieldUtils.getNullStr(map.get("whodrugname"),""));
		bacresis.setBacterianame(FieldUtils.getNullStr(map.get("bacterianame"),""));
		bacresis.setResistrate(FieldUtils.getDefultDou(map.get("resistrate"),0));
		return bacresis;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initByHiscode </li>
	 * <li>功能描述： 更新所有的whonet数据还是更新单院的</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月14日 </li>
	 * </ul> 
	 * @param hiscode 
	 * @return
	 */
	public boolean initByHiscode(String hiscode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_BACRESIS);
			List<Object> listvalue =new ArrayList<Object>();
			if(StringUtils.isNotBlank(hiscode)){
				sb.append(" and a.hiscode=? ");
				listvalue.add(hiscode.trim());
			}
			sb.append(" order by a.hiscode, b.drug_unique_code, resistrate desc");
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(),listvalue.toArray());
			if(list!=null&&list.size()>0){
				Map<String,List<Bacresis>> maps  = new HashMap<String, List<Bacresis>>();
				for(int i=0;i<list.size();i++){
					Bacresis t = copyFromMap(list.get(i));
					String key =  BACRESIS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
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
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
	public boolean DeleteByHiscode(String hiscode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_BACRESIS);
			List<Object> listvalue =new ArrayList<Object>();
			if(StringUtils.isNotBlank(hiscode)){
				sb.append(" and a.hiscode=? ");
				listvalue.add(hiscode.trim());
			}
			sb.append(" order by a.hiscode, b.drug_unique_code, resistrate desc");
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(),listvalue.toArray());
			if(list!=null&&list.size()>0){
				Map<String,List<Bacresis>> maps  = new HashMap<String, List<Bacresis>>();
				for(int i=0;i<list.size();i++){
					Bacresis t = copyFromMap(list.get(i));
					String key =  BACRESIS_TITLE+t.getHiscode().trim()+"-"+t.getDruguniquecode().trim();
					if(maps.containsKey(key)){
						maps.get(key).add(t);
					 }else{
					   List z = new ArrayList();
					   z.add(t);
					   maps.put(key,z);
					}
				}
				this.deleteMapinListCommon(maps);
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
	
	

}
