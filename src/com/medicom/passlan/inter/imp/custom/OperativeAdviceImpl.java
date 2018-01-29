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
import com.medicom.passlan.redis.custom.OperDrugadvice;
import com.medicom.passlan.util.FieldUtils;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  OperativeAdviceImpl </li>
 * <li>类描述：  围术期用药的历史抗菌药物信息 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月30日 </li>
 * <li>修改备注：</li>
 * </ul>
 */

@Service
public class OperativeAdviceImpl extends CommonInface<OperDrugadvice>{
	//这个准备添加一个直接根据给的字符串在查数据库操作
		@Autowired
		private JdbcTemplate jdbcTemplate;
		
		private final String ADVICE_TITLE = "drug_advice-operative-";
		
		private final String SEARCH_ADVICE = "select caseid,orderno, drug_starttime, drug_unique_code, drugname,"
				+ " drug_executetime, drug_endtime from mc_pro_drug_advice where caseid =?";

		private final String SEARCH_ADVICENO = "select caseid,orderno, drug_starttime, drug_unique_code, drugname,"
				+ " drug_executetime, drug_endtime from mc_pro_drug_advice ";
		private Logger logger = Logger.getLogger(BacresisInfaceImpl.class);
		
		public boolean addOrUpdate(OperDrugadvice t) {
			try{
				String key = ADVICE_TITLE+t.getCaseid();
				List<OperDrugadvice> list = this.get(key);
				this.findbyList(list,t);
				list.add(t);
				return this.addOrUpdateList(list,key);
			}catch(Exception e){
				logger.error(e.getMessage());
				return false;
			}
		}

		public boolean addList(List<OperDrugadvice> list) {
			try{
				if(list!=null&&list.size()>0){
					OperDrugadvice t = list.get(0);
					String key = ADVICE_TITLE+t.getCaseid();
					return this.addListCommon(list, key);
				}else{
					return false;
				}
			}catch(Exception e){
				logger.error(e.getMessage());
				return false;
			}
		}

		public boolean findbyList(List<OperDrugadvice> list, OperDrugadvice t) {
			boolean isSure = false;
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					OperDrugadvice temp = list.get(i);
					if(temp.getCaseid().equals(t.getCaseid())){
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
		public boolean initDateByCon(String caseid){
			try{
				if(StringUtils.isNotBlank(caseid)){
					List<Object> listvalue =new ArrayList<Object>();
					listvalue.add(caseid);
					
					List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_ADVICE, listvalue.toArray());
					List<OperDrugadvice> listobject = new ArrayList<OperDrugadvice>();
					String key  = "";
					if(list!=null&&list.size()>0){
						for(int i=0;i<list.size();i++){
							OperDrugadvice t = copyFromMap(list.get(i));
							key = ADVICE_TITLE+t.getCaseid().trim();
							listobject.add(t);
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
		public OperDrugadvice copyFromMap(Map<String,Object> map){
			OperDrugadvice custom = new OperDrugadvice();
			custom.setCaseid(FieldUtils.getNullStr(map.get("caseid"),""));
			custom.setOrderno(FieldUtils.getNullStr(map.get("orderno"),""));
			custom.setDrugStartTime(FieldUtils.getNullStr(map.get("drug_starttime"),""));
			custom.setDrugUniqueCode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
			custom.setDrugName(FieldUtils.getNullStr(map.get("drugname"),""));
			custom.setDrugExecuteTime(FieldUtils.getNullStr(map.get("drug_executetime"),""));
			custom.setDrugEndTime(FieldUtils.getNullStr(map.get("drug_endtime"),""));
			return custom;
		}
		
		
		/**
		 * 
		 * <ul>
		 * <li>方法名：  initAllDate </li>
		 * <li>功能描述： 初始化所有的数据</li>
		 * <li>创建人：  周应强 </li>
		 * <li>创建时间：2016年9月13日 </li>
		 * </ul> 
		 * @return
		 */
		public boolean initAllDate(){
			try{
				Map<String,List<OperDrugadvice>> map = new HashMap<String,List<OperDrugadvice>>();
				List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_ADVICENO);
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						OperDrugadvice t = copyFromMap(list.get(i));
						String key = ADVICE_TITLE+t.getCaseid().trim();
						if(map.get(key)!=null){
							map.get(key).add(t);
						}else{
							List<OperDrugadvice> listdrug = new ArrayList<OperDrugadvice>();
							listdrug.add(t);
							map.put(key, listdrug);
						}
					}
					this.addMapinListCommon(map);
				}
				return true;
			}catch(Exception e){
				logger.error(e.getMessage());
				return false;
			}
		}
}
