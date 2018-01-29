package com.medicom.passlan.inter.imp.sys;

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
 * <li>类名称：  SysConfigImpl </li>
 * <li>类描述：  系统配置的redis的更新 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class SysConfigImpl extends ObjectInfaceImpl{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String COFING_TITLE = "mc_config-";
	
	private final String SEARCH_COFING = "select paramname,paramvalue from mc_config  ";
	
	
	private Logger logger = Logger.getLogger(SysConfigImpl.class);
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述： 初始化单个对象的</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param paraname  key 
	 * @param paramvalue value
	 * @return
	 */
	public boolean initDateByCon(String paraname,String paramvalue){
		try{
			if(StringUtils.isNotBlank(paraname)&&StringUtils.isNotBlank(paramvalue)){
				String key  = 	COFING_TITLE+paraname.trim();
				return this.addOrUpdate(key, paramvalue);
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
	 * <li>功能描述：初始化配置文件所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_COFING);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> map = list.get(i);
				String key =COFING_TITLE+map.get("paramname").toString();
				this.addOrUpdate(key, map.get("paramvalue").toString());
			}
		}
		
	}

}
