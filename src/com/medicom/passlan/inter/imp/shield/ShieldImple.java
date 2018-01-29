package com.medicom.passlan.inter.imp.shield;

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
import com.medicom.passlan.redis.shield.SheildEnum;
import com.medicom.passlan.redis.shield.ShieldData;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  ShieldImple </li>
 * <li>类描述：   屏蔽用的初始化的数据</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class ShieldImple extends CommonInface<ShieldData>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_SHIELD="select pkid,hiscode,moduleid,modulename,drug_unique_code1,"
			+ "drug_unique_code2,doseunit,routecode,routetype,discode,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,shielddesc,warning FROM mc_user_shielddata ";
	
	private final String SHIELD_TITLE = "user_shielddata-";
	
	private Logger logger = Logger.getLogger(ShieldImple.class);
	

	/**
	 * 成人用药的单个数据新增的方法
	 */
	public boolean addOrUpdate(ShieldData t) {
		try{
			String key = this.key(t.getModuleid(), t);
			List<ShieldData> list = this.get(key);
			this.findbyList(list,t);
			if(list==null){
				list = new ArrayList<ShieldData>();
			}
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
	public boolean addList(List<ShieldData> list) {
		try{
			if(list!=null&&list.size()>0){
				ShieldData t = list.get(0);
				String key =  this.key(t.getModuleid(), t);
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
	public boolean findbyList(List<ShieldData> list, ShieldData t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				ShieldData temp = list.get(i);
				if(temp.getPkid()==t.getPkid()){
					//这里去掉重复找到的成人用药的对象
					list.remove(temp);
					isSure =  true;
				}
			}
		}else{
			list = new ArrayList<ShieldData>();
		}
		return isSure;
	}
	
	
	public boolean deleteObject(ShieldData t){
		String key  = this.key(t.getModuleid(), t);
		List<ShieldData> list = this.get(key);
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
	public boolean initDateByCon(int pkid,int moduleid){	
		try{
			if(moduleid<=17&&moduleid>0){
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(returnSql(moduleid,pkid,2), listvalue.toArray());
				List<ShieldData> listobject = new ArrayList<ShieldData>();
				String key  = "";
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						ShieldData custom = copyFromMap(list.get(i));
						listobject.add(custom);
						key = this.key(custom.getModuleid(), custom);
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
	public ShieldData copyFromMap(Map<String,Object> map){
		ShieldData custom = new ShieldData();
		custom.setPkid(FieldUtils.getDefaultInt(map.get("pkid"),0));
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setModuleid(FieldUtils.getDefaultInt(map.get("moduleid"),0));
		custom.setDruguniquecodeone(FieldUtils.getNullStr(map.get("drug_unique_code1"),""));
		custom.setDruguniquecodetwo(FieldUtils.getNullStr(map.get("drug_unique_code2"),""));
		custom.setDoseunit(FieldUtils.getNullStr(map.get("doseunit"),""));
		custom.setRoutecode(FieldUtils.getNullStr(map.get("routecode"),""));
		custom.setRoutetype(FieldUtils.getDefaultInt(map.get("routetype"),0));
		custom.setDiscode(FieldUtils.getNullStr(map.get("discode"),""));
		custom.setAgelow(FieldUtils.getNullStr(map.get("agelow"),""));
		custom.setAgelowunit(FieldUtils.getNullStr(map.get("agelow_unit"),""));
		custom.setUnequallow(FieldUtils.getDefaultInt(map.get("unequal_low"),0));
		custom.setAgehigh(FieldUtils.getNullStr(map.get("agehigh"),""));
		custom.setAgehighunit(FieldUtils.getNullStr(map.get("agehigh_unit"),""));
		custom.setUnequalhigh(FieldUtils.getDefaultInt(map.get("unequal_high"),0));
		custom.setShielddesc(FieldUtils.getNullStr(map.get("shielddesc"),""));
		custom.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return custom;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  returnSql </li>
	 * <li>功能描述：返回执行屏蔽那些要初始化的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年4月12日 </li>
	 * </ul> 
	 * @param modelid
	 * @return
	 */
	private String returnSql(Integer modelid,int pkid,int typestart){
		StringBuffer sb = new StringBuffer(SEARCH_SHIELD);
		if(modelid==SheildEnum.PASS_DOSAGE.getCustomindex()){
			//剂量范围
			sb.append(" where moduleid = 1  and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> ' ' and doseunit is not null and doseunit <> ' '");
			if(pkid>0){
				sb.append(" and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by hiscode, drug_unique_code1, routecode, doseunit ");
		}else if(modelid==SheildEnum.PASS_HEPDOSAGE.getCustomindex()){
			//����
			sb.append(" where moduleid = 2  and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> ' ' and doseunit is not null and doseunit <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by drug_unique_code1, routecode, doseunit");
		}else if(modelid==SheildEnum.PASS_RENDOSAGE.getCustomindex()){
			//����
			sb.append(" where moduleid = 3 and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> ' ' and doseunit is not null and doseunit <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by drug_unique_code1, routecode, doseunit");
		}else if(modelid==SheildEnum.PASS_DRUGINTER.getCustomindex()){
			//�໥����
			sb.append(" where moduleid = 4 and drug_unique_code1 is not null  and drug_unique_code1 <> '' and drug_unique_code2 is not null and drug_unique_code2 <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by hiscode, drug_unique_code1, drug_unique_code2");
		}else if(modelid==SheildEnum.PASS_IV.getCustomindex()){
			//��������
			sb.append(" where moduleid = 5 and drug_unique_code1 is not null and drug_unique_code1 <> '' and drug_unique_code2 is not null and drug_unique_code2 <> ' ' and routetype is not null and routetype > 0 ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by hiscode,drug_unique_code1,drug_unique_code2, routetype ");
		}else if(modelid==SheildEnum.PASS_DRUGLEVEL.getCustomindex()){
			//����Ũ��
			sb.append(" where moduleid = 6 and drug_unique_code1 is not null and drug_unique_code1 <> ''and drug_unique_code2 is not null and drug_unique_code2 <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by hiscode, drug_unique_code1, drug_unique_code2 ");
		}else if(modelid==SheildEnum.PASS_CONTRAIND.getCustomindex()){
			//����Ũ��
			sb.append(" where moduleid = 7 and drug_unique_code1 is not null and drug_unique_code1 <> '' and discode is not null and discode <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by hiscode, drug_unique_code1, discode ");
		}else if(modelid==SheildEnum.PASS_ADR.getCustomindex()){
			//������Ӧ����
			sb.append(" where moduleid = 8 and drug_unique_code1 is not null and drug_unique_code1 <> '' and discode is not null and discode <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by hiscode, drug_unique_code1, discode ");
		}else if(modelid==SheildEnum.PASS_INDICATION.getCustomindex()){
			//����Ӧ����
			sb.append(" where moduleid = 9 and drug_unique_code1 is not null and drug_unique_code1 <> '' and discode is not null and discode <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by drug_unique_code1, discode ");
		}else if(modelid==SheildEnum.PASS_PEDIATRIC.getCustomindex()){
			//����Ӧ����
			sb.append(" where moduleid = 10 and drug_unique_code1 is not null and drug_unique_code1 <> '' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by drug_unique_code1 ");
		}else if(modelid==SheildEnum.PASS_ADULT.getCustomindex()){
			//����Ӧ����
			sb.append(" where moduleid =11 and drug_unique_code1 is not null and drug_unique_code1 <> '' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by drug_unique_code1 ");
		}else if(modelid==SheildEnum.PASS_GERIATRIC.getCustomindex()){
			//������ҩ
			sb.append(" where moduleid=12 and drug_unique_code1 is not null and drug_unique_code1 <> '' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_PREGNANCY.getCustomindex()){
			//������ҩ����
			sb.append(" where moduleid=13 and drug_unique_code1 is not null and drug_unique_code1 <> '' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_LACTATION.getCustomindex()){
			//������ҩ����
			sb.append(" where moduleid=14 ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_SEX.getCustomindex()){
			//�Ա���ҩ����
			sb.append(" where moduleid=15 and drug_unique_code1 is not null and drug_unique_code1 <> ' ' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append(" order by drug_unique_code1  ");
		}else if(modelid==SheildEnum.PASS_DRUGALLERGEN.getCustomindex()){
			//�Ա���ҩ����
			sb.append(" where moduleid=16 ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_ROUTE.getCustomindex()){
			//�Ա���ҩ����
			sb.append(" where moduleid = 17 and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> '' ");
			if(pkid>0){
				sb.append("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sb.append("  order by hiscode,drug_unique_code1, routecode  ");
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * <ul>
	 * <li>������  key </li>
	 * <li>���������� ��������keyֵ </li>
	 * <li>�����ˣ�  ��Ӧǿ </li>
	 * <li>����ʱ�䣺2016��3��11�� </li>
	 * </ul> 
	 * @param modelid
	 * @param shieldData
	 * @return
	 */
	private String key(Integer modelid,ShieldData shieldData){
		String key = SHIELD_TITLE+modelid;
		if(modelid==SheildEnum.PASS_DOSAGE.getCustomindex()){
			/**< 剂量范围 */
			key = key+"-"+shieldData.getHiscode().trim()
					+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_HEPDOSAGE.getCustomindex()){
			 /**< 肝损害剂量 */
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_RENDOSAGE.getCustomindex()){
			  /**< 相互作用 */
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_DRUGINTER.getCustomindex()){
			//�໥����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDruguniquecodetwo();
		}else if(modelid==SheildEnum.PASS_IV.getCustomindex()){
			//��������
			if(compStrOrder(shieldData.getDruguniquecodeone(),shieldData.getDruguniquecodetwo())>0){
				key = key+"-"+shieldData.getHiscode().trim()
				+"-"+shieldData.getDruguniquecodetwo().trim()+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getRoutetype();
			}else{
				key = key+"-"+shieldData.getHiscode().trim()
				+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDruguniquecodetwo().trim()+"-"+shieldData.getRoutetype();
			}
			
		}else if(modelid==SheildEnum.PASS_DRUGLEVEL.getCustomindex()){
			//����Ũ��
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDruguniquecodetwo().trim();
		}else if(modelid==SheildEnum.PASS_CONTRAIND.getCustomindex()){
			//����Ũ��
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDiscode();
		}else if(modelid==SheildEnum.PASS_ADR.getCustomindex()){
			//������Ӧ����
			key = key+"-"+ StringUtils.trim(shieldData.getHiscode())
			+"-"+StringUtils.trim(shieldData.getDruguniquecodeone())+"-"+StringUtils.trim(shieldData.getDiscode());
		}else if(modelid==SheildEnum.PASS_INDICATION.getCustomindex()){
			//����Ӧ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDiscode();
		}else if(modelid==SheildEnum.PASS_PEDIATRIC.getCustomindex()){
			//����Ӧ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_ADULT.getCustomindex()){
			//����Ӧ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_GERIATRIC.getCustomindex()){
			//������ҩ
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_PREGNANCY.getCustomindex()){
			//������ҩ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_LACTATION.getCustomindex()){
			//������ҩ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_SEX.getCustomindex()){
			//�Ա���ҩ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_DRUGALLERGEN.getCustomindex()){
			//�Ա���ҩ����
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_ROUTE.getCustomindex()){
			// ��ҩ;������
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+StringUtils.trim(shieldData.getDruguniquecodeone()) +"-"+ StringUtils.trim(shieldData.getRoutecode());
		}
		return key;
	}
	
	
	public static int compStrOrder(String str1,String str2){
		int len1 = str1.length();
		int len2 = str2.length();
		for(int i = 0;i < 15 - len1;i++){
			str1 = " "+str1;
		}
		for(int i = 0;i < 15 - len2;i++){
			str2 = " "+str2;
		}
		return str1.compareTo(str2);
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  deleteId </li>
	 * <li>功能描述： </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年8月17日 </li>
	 * </ul> 
	 * @param pkid 主键的id
	 * @param moduleid 
	 * @return
	 */
	public boolean deleteId(Integer pkid,int moduleid){
		try{
			if(moduleid<=17&&moduleid>0){
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(returnSql(moduleid,pkid,1), listvalue.toArray());
				if(list!=null&&list.size()>0){
					ShieldData custom = copyFromMap(list.get(0));
					return this.deleteObject(custom);
					//return this.delete(this.key(moduleid, custom));
				}else{
					return false;
				}
			}else{
				throw new Exception("执行参数异常");
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	
	public boolean initAllDate(int moduleid){	
		try{
			if(moduleid<=17&&moduleid>0){
				Map<String,List<ShieldData>> map = new HashMap<String,List<ShieldData>>();
				List<Map<String,Object>> list =  jdbcTemplate.queryForList(returnSql(moduleid,0,1));
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						ShieldData custom = copyFromMap(list.get(i));
						String key = this.key(custom.getModuleid(), custom);
						if(map.get(key)!=null){
							map.get(key).add(custom);
						}else{
							List<ShieldData> listobject = new ArrayList<ShieldData>();
							listobject.add(custom);
							map.put(key, listobject);
						}
					}
					this.addMapinListCommon(map);
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
