package com.medicom.passlan.inter.imp.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.sys.SysDoctor;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  SysDoctorInfaceImpl </li>
 * <li>类描述：  系统医生的配置信息 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月5日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class SysDoctorInfaceImpl extends CommonInface<SysDoctor> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	 private Logger logger = Logger.getLogger(SysDoctorInfaceImpl.class);
	
	private final String SEARCHE_DOCTOR= "select distinct match_scheme,doctorcode, doctorname, prespriv, ilevel,antilevel from mc_dict_doctor ";
	
	private String DOCUTOR_TITLE = "dict_doctor-";
	
	
	public boolean addOrUpdate(SysDoctor t) {
		try{
			String key = DOCUTOR_TITLE + t.getMatchscheme()+"-"+t.getDoctorcode().trim();
			return this.addOrUpdateCommon(t,key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * <ul>
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述：初始化医生的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param matchscheme
	 * @param doctorcode
	 * @return
	 */
	public boolean initDateByCon(int matchscheme,String doctorcode){
		try{
			if(StringUtils.isNotBlank(String.valueOf(matchscheme))
					&&StringUtils.isNotBlank(doctorcode)){
				StringBuffer sb = new StringBuffer(SEARCHE_DOCTOR);
				sb.append(" where  match_scheme=? and doctorcode=?  order by match_scheme, doctorcode");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(doctorcode.trim());
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				//List<SysDoctor> listobject = new ArrayList<SysDoctor>();
				String key  = "";
				if(list!=null&&list.size()>0){
					SysDoctor doctor = copyFromMap(list.get(0));
					key = DOCUTOR_TITLE+doctor.getMatchscheme()+"-"+doctor.getDoctorcode().trim();
					return this.addOrUpdate(doctor, key);
				}else{
					return true;
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
	 * <li>功能描述：把从数据库的数据直接返回对象 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月28日 </li>
	 * </ul> 
	 * @param map 数据库中查询出来的对象
	 * @return
	 */
	public SysDoctor copyFromMap(Map<String,Object> map){
		SysDoctor sysdoct = new SysDoctor();
		//match_scheme,doctorcode, doctorname, prespriv, ilevel,antilevel
		sysdoct.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme"),0));
		sysdoct.setDoctorcode(FieldUtils.getNullStr(map.get("doctorcode"),""));
		sysdoct.setDoctorname(FieldUtils.getNullStr(map.get("doctorname"),""));
		sysdoct.setPrespriv(FieldUtils.getDefaultInt(map.get("prespriv"),0));
		sysdoct.setIlevel(FieldUtils.getDefaultInt(map.get("ilevel"),0));
		sysdoct.setAntilevel(FieldUtils.getDefaultInt(map.get("antilevel"),0));
		return sysdoct;
	}
	
	
	
	
	public boolean initAllDate(){
		try{
		
			StringBuffer sb = new StringBuffer(SEARCHE_DOCTOR);
			sb.append("   order by match_scheme, doctorcode");
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			//List<SysDoctor> listobject = new ArrayList<SysDoctor>();
			String key  = "";
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysDoctor doctor = copyFromMap(list.get(i));
					key = DOCUTOR_TITLE+doctor.getMatchscheme()+"-"+doctor.getDoctorcode().trim();
					this.addOrUpdate(doctor, key);
				}
			}
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
}
