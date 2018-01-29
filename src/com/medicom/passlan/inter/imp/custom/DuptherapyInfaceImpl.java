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
 * <li>类名称：  DuptherapyInfaceImpl </li>
 * <li>类描述：   保存重复用药的数据</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月13日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class DuptherapyInfaceImpl extends ObjectInfaceImpl{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DUPTHERAPY_TITLE = "user_duptherapy-";
	
	private final String SEARCH_BRIF = "select distinct dupcid, user_max from mc_user_duptherapy where dupcid is not null ";
	
	private Logger logger = Logger.getLogger(DuptherapyInfaceImpl.class);
	
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
	public boolean initDateByCon(int pkid,Object dupcid){
		try{
			if(StringUtils.isNotBlank(String.valueOf(pkid))){
				StringBuffer sb = new StringBuffer(SEARCH_BRIF);
				sb.append(" and pkid=? and dupcid > 0 and user_max is not null and user_max > 0 order by dupcid, user_max");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(pkid);
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String,Object> object = list.get(i);
						String key = DUPTHERAPY_TITLE+object.get("dupcid").toString().trim();
						return this.addOrUpdate(key, object.get("user_max").toString());
					}
				}else{
					if(dupcid!=null&&StringUtils.isNotBlank(dupcid.toString())){
						String key  = DUPTHERAPY_TITLE+dupcid.toString();
						return this.delete(key);
					}else{
						return false;
					}
				}
			}else{
				return false;
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
	 * <li>方法名：  initAll </li>
	 * <li>功能描述： 初始化所有数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月13日 </li>
	 * </ul> 
	 * @return
	 */
	public boolean initAll(){
		try{
			
			StringBuffer sb = new StringBuffer(SEARCH_BRIF);
			sb.append(" and dupcid > 0 and user_max is not null and user_max > 0 order by dupcid, user_max");
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> object = list.get(i);
					String key = DUPTHERAPY_TITLE+object.get("dupcid").toString().trim();
					this.addOrUpdate(key, object.get("user_max").toString());
				}
				return true;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

}
