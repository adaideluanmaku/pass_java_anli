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
import com.medicom.passlan.redis.custom.DoctorPrivCustom;
import com.medicom.passlan.util.FieldUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  DoctorprivInfaceImpl </li>
 * <li>类描述：  </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月11日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service
public class DoctorprivInfaceImpl extends CommonInface<DoctorPrivCustom> {
	
	private final String DOCTOR_PRIV = "select distinct hiscode, doctorcode, drug_unique_code, slcode, severity, warning from mc_user_doctor_priv ";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String PRIV_TITLE = "user_doctor_priv-";
	
	private Logger logger = Logger.getLogger(DoctorprivInfaceImpl.class);
	

	public String getDOCTOR_PRIV() {
		return DOCTOR_PRIV;
	}

	public boolean addOrUpdate(DoctorPrivCustom t) {
		try{
//			String key = PRIV_TITLE+t.getHiscode()+"-"+t.getDoctorcode()+"-"+t.getDruguniquecode();
//			List<DoctorPrivCustom> list = this.get(key);
//			this.findbyList(list,t);
//			list.add(t);
//			return this.addOrUpdateList(list,key);
			return false;
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
	 * @param druguniquecode
	 * @return
	 */
	public boolean initDateByCon(String hiscode,String doctorcode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(doctorcode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(DOCTOR_PRIV);
				sb.append(" where  hiscode =? and doctorcode=? and drug_unique_code=?  order by hiscode, doctorcode,drug_unique_code ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode.trim());
				listvalue.add(doctorcode.trim());
				listvalue.add(druguniquecode.trim());
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
		
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						DoctorPrivCustom t = copyFromMap(list.get(i));
						String key = PRIV_TITLE+t.getHiscode().trim()+"-"+t.getDoctorcode()+"-"+t.getDruguniquecode().trim();
						return this.addOrUpdateCommon(t, key);
					}
				}
			}else{
				return false;
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
	public DoctorPrivCustom copyFromMap(Map<String,Object> map){
		DoctorPrivCustom priv = new DoctorPrivCustom();
		//hiscode, doctorcode, drug_unique_code, slcode, severity, warning
		priv.setHiscode(FieldUtils.getNullStr(map.get("hiscode"),""));
		priv.setDoctorcode(FieldUtils.getNullStr(map.get("doctorcode"),""));
		priv.setDruguniquecode(FieldUtils.getNullStr(map.get("drug_unique_code"),""));
		priv.setSlcode(Integer.parseInt(map.get("slcode").toString()));
		priv.setSeverity(FieldUtils.getNullStr(map.get("severity"),""));
		priv.setWarning(FieldUtils.getNullStr(map.get("warning"),""));
		return priv;
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  deleteAll </li>
	 * <li>功能描述：删除所有相关的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月13日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param druguniquecode
	 * @return
	 */
	public  boolean deleteAll(String hiscode,String druguniquecode){
		try{
			if(StringUtils.isNotBlank(hiscode)
					&&StringUtils.isNotBlank(druguniquecode)){
				StringBuffer sb = new StringBuffer(DOCTOR_PRIV);
				sb.append(" where  hiscode =? and drug_unique_code=?  order by hiscode, doctorcode,drug_unique_code ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(hiscode.trim());
				listvalue.add(druguniquecode.trim());
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						DoctorPrivCustom t = copyFromMap(list.get(i));
						String key = PRIV_TITLE+t.getHiscode().trim()+"-"+t.getDoctorcode()+"-"+t.getDruguniquecode().trim();
						this.delete(key);
					}
					return true;
				}
			}else{
				return false;
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
	 * <li>方法名：  initDateByCon </li>
	 * <li>功能描述：通过给定的条件初始化redis的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月27日 </li>
	 * </ul> 
	 * @param hiscode
	 * @param druguniquecode
	 * @return
	 */
	public boolean initAllDate(String hiscode){
		try{
//			List<Object> listvalue =new ArrayList<Object>();
			StringBuffer sb = new StringBuffer(DOCTOR_PRIV);
//			if(StringUtils.isNotBlank(hiscode)){
//				sb.append(" where  hiscode =?   order by hiscode, doctorcode,drug_unique_code ");
//				listvalue.add(hiscode.trim());
//			}else{
				sb.append("order by hiscode, doctorcode,drug_unique_code ");
//			}
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sb.toString());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					DoctorPrivCustom t = copyFromMap(list.get(i));
					String key = PRIV_TITLE+t.getHiscode().trim()+"-"+t.getDoctorcode()+"-"+t.getDruguniquecode().trim();
					this.addOrUpdateCommon(t, key);
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
}
