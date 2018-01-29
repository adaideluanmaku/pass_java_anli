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
import com.medicom.passlan.redis.sys.SysHospital;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  SysHospitalImpl </li>
 * <li>类描述：   查询</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年9月27日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class SysHospitalImpl extends CommonInface<SysHospital>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//查询成人用的数据
	private final String SEARCH_HOSPITAL="select hiscode,hiscode_user,stru_type,hospname,hiscode_p from mc_hospital ";
	
	private final String HOSPITAL_TITLE = "hospital-";
	
	private Logger logger = Logger.getLogger(SysHospitalImpl.class);
	
	
	
	public String getHOSPITAL_TITLE() {
		return HOSPITAL_TITLE;
	}


	/**
	 * 
	 * <ul>
	 * <li>方法名：  addOrUpdate </li>
	 * <li>功能描述： </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月27日 </li>
	 * </ul> 
	 * @param t
	 * @return
	 */
	public boolean addOrUpdate(SysHospital t) {
		try{
			if(StringUtils.isNotBlank(t.getHiscode_user())){
				String key = HOSPITAL_TITLE+t.getHiscode_user().trim();
				return this.addOrUpdateCommon(t, key);
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
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述：通过给定的条件初始化redis的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月27日 </li>
	 * </ul> 
	 * @param hiscode
	 * @return
	 */
	public boolean initDateByCon(String hiscode_user){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_HOSPITAL);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(hiscode_user)){
				sb.append(" where hiscode_user=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode_user);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysHospital priv = copyFromMap(list.get(0));
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
	 * <li>功能描述：医院机构的所有所有数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月27日 </li>
	 * </ul> 
	 * @param map
	 * @return
	 */
	public SysHospital copyFromMap(Map<String,Object> map){
		SysHospital custom = new SysHospital();
		custom.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		custom.setHiscode_p(FieldUtils.getNullStr(map.get("hiscode_p"),""));
		custom.setHiscode_user(FieldUtils.getNullStr(map.get("hiscode_user"),""));
		custom.setHospname(FieldUtils.getNullStr(map.get("hospname"),""));
		custom.setStru_type(FieldUtils.getNullStr(map.get("stru_type"),""));
		return custom;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述： 初始化所有的医院数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年9月27日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_HOSPITAL);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysHospital t = copyFromMap(list.get(i));
				if(StringUtils.isNotBlank(t.getHiscode_user())){
					String key =HOSPITAL_TITLE+t.getHiscode_user().trim();
					this.addOrUpdate(t,key);
				}
				
			}
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  deletetHosptial </li>
	 * <li>功能描述： 删除数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年10月18日 </li>
	 * </ul> 
	 * @param hiscode_user
	 * @return
	 */
	public boolean deletetHosptial(String hiscode_user){
		return this.delete(HOSPITAL_TITLE+hiscode_user.trim());
	}
	
	
	
	
	
	
	

}
