package com.medicom.passlan.inter.imp.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.sys.SysOperation;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  OperationImpl </li>
 * <li>类描述：  系统手术信息更新redis</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月4日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class OperationImpl extends CommonInface<SysOperation>{
	
	
	private final String SEARCH_OPERATION = "select match_scheme,operationcode, operationname, "
						            + " useanti, drugtime, " 
						            +" (case when premoment_low is null then -1.0 " 
						            +" else premoment_low end) as premoment_low, "
						            +" (case when premoment_high is null then -1.0 "
						            +" else premoment_high end) as premoment_high " 
						            +" from mc_dict_operation "
						            +" where (useanti>-1 or premoment_low>0 or premoment_high>0 or drugtime>0) ";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String OPERATION_TITLE = "dict_operation-";
	
	private Logger logger = Logger.getLogger(FrequencyImpl.class);
	
	
	public boolean addOrUpdate(SysOperation t) {
		try{
			String key = OPERATION_TITLE+t.getMatchscheme()+"-"+ t.getOperationCode().trim();
			return this.addOrUpdateCommon(t, key);
		}catch(Exception e){
			logger.error(e.getMessage());
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
	 * @return
	 */
	public boolean initDateByCon(Integer matchscheme,String operationcode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_OPERATION);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(operationcode)
					&&StringUtils.isNotBlank(String.valueOf(matchscheme)) ){
				sb.append(" and match_scheme=?  and operationcode=? ");
				sb.append("  order by operationcode ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(operationcode);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysOperation priv = copyFromMap(list.get(0));
					 this.addOrUpdate(priv);
				}
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
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
	public SysOperation copyFromMap(Map<String,Object> map){
		SysOperation custom = new SysOperation();
		//match_scheme,frequency, times,days
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		custom.setOperationCode(FieldUtils.getNullStr(map.get("operationcode"),"")); 				
		custom.setOperationName(FieldUtils.getNullStr(map.get("operationname"),""));
		custom.setUseanti(FieldUtils.getDefaultInt(map.get("useanti"),0));
		custom.setDrugTime(FieldUtils.getDefaultInt(map.get("drugtime"),-1));
		custom.setPremomentLow(FieldUtils.getDefultDou(map.get("premoment_low"),-1.0));
		custom.setPremomentHigh(FieldUtils.getDefultDou(map.get("premoment_high"),-1.0));
		return custom;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述：初始化所有的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月5日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_OPERATION);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysOperation t = copyFromMap(list.get(i));
				String key = OPERATION_TITLE+t.getMatchscheme()+"-"+ t.getOperationCode().trim();
				this.addOrUpdate(t,key);
			}
		}
	}
	
	

}
