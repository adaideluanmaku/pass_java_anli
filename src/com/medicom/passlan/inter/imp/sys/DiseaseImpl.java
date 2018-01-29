package com.medicom.passlan.inter.imp.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.medicom.modules.utils.SqlUtils;
import com.medicom.passlan.inter.imp.CommonInface;
import com.medicom.passlan.redis.sys.SysDisease;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  DiseaseImpl </li>
 * <li>类描述：   疾病的</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月4日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class DiseaseImpl extends CommonInface<SysDisease>{
	
	
	private final String SEARCH_DISEASE = " select a.match_scheme, a.discode as u_code, a.disname as u_name," 
			+"b.icd_code,b.renal, b.hepatic, b.cardio, b.pulm, b.neur, b.commonindex, "
			+"b.endo, b.seqnum, b.preg_label, b.lact_label, b.hep_label, b.ren_label "
			+"from mc_dict_disease a, mc_icd_view b  "
			+"where a.pass_icd_code = b.icd_code and a.pass_icd_code is not null ";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DISEASE_TITLE = "dict_disease-";
	
	public String getDISEASE_TITLE() {
		return DISEASE_TITLE;
	}


	private Logger logger = Logger.getLogger(DiseaseImpl.class);
	
	
	public boolean addOrUpdate(SysDisease t) {
		try{
			String key = DISEASE_TITLE+t.getMatchscheme()+"-"+ t.getUcode().trim();
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
	public boolean initDateByCon(Integer matchscheme,String discode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_DISEASE);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(discode)
					&&StringUtils.isNotBlank(String.valueOf(matchscheme)) ){
				sb.append(" and a.match_scheme=?  and a.discode=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(discode);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysDisease priv = copyFromMap(list.get(0));
					 this.addOrUpdate(priv);
				}
				return true;
			}else{
				String key = DISEASE_TITLE+matchscheme+"-"+ discode.trim();
				return this.delete(key);
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
	public SysDisease copyFromMap(Map<String,Object> map){
		SysDisease custom = new SysDisease();
		//select a.match_scheme, a.discode as u_code, a.disname as u_name," 
		//+"b.icd_code,b.renal, b.hepatic, b.cardio, b.pulm, b.neur, b.commonindex, "
		//+"b.endo, b.seqnum, b.preg_label, b.lact_label, b.hep_label, b.ren_label "
		//+"from mc_dict_disease a, mc_icd_view b  "
		//+"where a.pass_icd_code = b.icd_code and a.pass_icd_code is not null
		
		custom.setMatchscheme(FieldUtils.getDefaultInt(map.get("match_scheme") ,0));
		custom.setUcode(FieldUtils.getNullStr(map.get("u_code") ,""));
		custom.setUname(FieldUtils.getNullStr(map.get("u_name") ,""));
		custom.setIcdCode(FieldUtils.getNullStr(map.get("icd_code") ,""));
		custom.setRenal(FieldUtils.getDefaultInt(map.get("renal") ,0));
		custom.setHepatic(FieldUtils.getDefaultInt(map.get("hepatic") ,0));
		custom.setCardio(FieldUtils.getDefaultInt(map.get("cardio") ,0));
		custom.setPulm(FieldUtils.getDefaultInt(map.get("pulm") ,0));
		custom.setNeur(FieldUtils.getDefaultInt(map.get("neur") ,0));
		custom.setCommonIndex(FieldUtils.getDefaultInt(map.get("commonindex") ,0));
		custom.setEndo(FieldUtils.getDefaultInt(map.get("endo") ,0));
		custom.setSeqNum(FieldUtils.getDefaultInt(map.get("seqnum") ,0));
		custom.setPregLabel(FieldUtils.getDefaultInt(map.get("preg_label") ,0));
		custom.setLactLabel(FieldUtils.getDefaultInt(map.get("lact_label") ,0));
		custom.setHepLabel(FieldUtils.getDefaultInt(map.get("hep_label") ,0));
		custom.setRenLabel(FieldUtils.getDefaultInt(map.get("ren_label") ,0));
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
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DISEASE);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysDisease t = copyFromMap(list.get(i));
				String key =DISEASE_TITLE+t.getMatchscheme()+"-"+ t.getUcode().trim();
				this.addOrUpdate(t,key);
			}
		}
	}

}
