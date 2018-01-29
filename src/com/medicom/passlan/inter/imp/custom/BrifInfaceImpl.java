package com.medicom.passlan.inter.imp.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.ObjectInfaceImpl;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  BrifInfaceImpl </li>
 * <li>类描述：  简化信息的初始化方法 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class BrifInfaceImpl extends ObjectInfaceImpl{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String BRIF_TITLE = "user_brief-";
	
	public String getBRIF_TITLE() {
		return BRIF_TITLE;
	}


	private final String SEARCH_BRIF = "SELECT hiscode, drug_unique_code, content FROM mc_user_brief ";
	
	
	private Logger logger = Logger.getLogger(BrifInfaceImpl.class);
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
	public boolean initDateByCon(String hiscode,String drugUniqueCode){
		try{
			if(StringUtils.isNotBlank(String.valueOf(hiscode))
					&&StringUtils.isNotBlank(drugUniqueCode)){
				StringBuffer sb = new StringBuffer(SEARCH_BRIF);
				sb.append(" WHERE hiscode=? AND drug_unique_code=?  ORDER BY drug_unique_code ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode);
				listvalue.add(drugUniqueCode.trim());
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String,Object> object = list.get(i);
						String key = BRIF_TITLE+object.get("hiscode").toString().trim()+"-"+object.get("drug_unique_code").toString().trim();
						return this.addOrUpdate(key, object.get("content").toString());
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
	
	
	public boolean initAllDate(){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_BRIF);
			sb.append(" ORDER BY drug_unique_code ");
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> object = list.get(i);
					String key = BRIF_TITLE+object.get("hiscode").toString().trim()+"-"+object.get("drug_unique_code").toString().trim();
					this.addOrUpdate(key, object.get("content").toString());
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}
	
	
	public boolean deleteId(int pkid){
		try{
			if(pkid>0){
				StringBuffer sb = new StringBuffer(SEARCH_BRIF);
				sb.append(" WHERE pkid=? ORDER BY drug_unique_code ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String,Object> object = list.get(0);
						String key = BRIF_TITLE+object.get("hiscode").toString().trim()+"-"+object.get("drug_unique_code").toString().trim();
						return this.delete(key);
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
	
	
	
	
	

}
