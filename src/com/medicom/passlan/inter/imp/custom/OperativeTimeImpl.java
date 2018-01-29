package com.medicom.passlan.inter.imp.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.ObjectInfaceImpl;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  OperativeTimeImpl </li>
 * <li>类描述：   围术期用的方法。</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月5日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class OperativeTimeImpl extends ObjectInfaceImpl{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String TIME_TITLE = "pro_drug_operation-datetime-";
	
	private final String SEARCH_TIME = "select * from (select  caseid,operationcode,drug_starttime from mc_pro_drug_operation order by drug_starttime ) c  where c.caseid=? and c.operationcode=?  group by caseid,operationcode";
	
	private final String SEARCH_TIMEALL = "select * from (select  caseid,operationcode,drug_starttime from mc_pro_drug_operation order by drug_starttime ) c   group by caseid,operationcode";
	
	
	private Logger logger = Logger.getLogger(OperativeTimeImpl.class);
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述： 初始化单个对象的</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param drugUniqueCode
	 * @return
	 */
	public boolean initDateByCon(String caseid,String operationcode){
		try{
			List<Object> listvalue =new ArrayList<Object>();
			listvalue.add(caseid);
			listvalue.add(operationcode.trim());
			List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_TIME, listvalue.toArray());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> object = list.get(i);
					String key = TIME_TITLE+object.get("caseid").toString().trim()+"-"+object.get("operationcode").toString();
					return this.addOrUpdate(key, object.get("drug_starttime").toString());
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAllDate </li>
	 * <li>功能描述：初始化所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月13日 </li>
	 * </ul> 
	 * @return
	 */
	public boolean initAllDate(){
		try{
			List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_TIMEALL);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> object = list.get(i);
					String key = TIME_TITLE+object.get("caseid").toString().trim()+"-"+object.get("operationcode").toString();
					this.addOrUpdate(key, object.get("drug_starttime").toString());
				}
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	

}
