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
import com.medicom.passlan.redis.sys.SysDictDrug;
import com.medicom.passlan.util.FieldUtils;

import redis.clients.jedis.ShardedJedis;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  DistDrugImpl </li>
 * <li>类描述： 药品基本信息的缓存redis文件  </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年7月1日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
@Service	
public class DistDrugImpl extends CommonInface<SysDictDrug>{
	
	private final String SEARCH_DISTDRUG = "SELECT a.match_scheme, a.drugcode, a.drugform,"
			+ " a.is_basedrug, a.is_basedrug_p"
+",a.is_anti,a.antitype,a.antilevel,a.drugtype,a.drugformtype " 
+" ,a.socialsecurity_ratio,a.is_socialsecurity "
+",a.socialsecurity_desc,a.jdmtype,a.is_stimulant "
+",a.stimulantingred,a.is_solvent,a.is_srpreparations "
+",a.is_needskintest,a.is_dearmed,a.is_poison,a.is_bloodmed "
+",a.is_sugarmed,a.otctype,a.is_bisection_use,a.high_alert_level FROM mc_dict_drug a ";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String DISTDRUG_TITLE = "dict_drug-single-";
	
	private Logger logger = Logger.getLogger(DistDrugImpl.class);
	
	
	public boolean addOrUpdate(SysDictDrug t) {
		try{
			String key = DISTDRUG_TITLE+t.getMatchscheme()+"-"+t.getDrugcode().trim();
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
	public boolean initDateByCon(int matchscheme,String drugcode){
		try{
			StringBuffer sb = new StringBuffer(SEARCH_DISTDRUG);
			List<Map<String,Object>> list = null;
			if(StringUtils.isNotBlank(drugcode)&&StringUtils.isNotBlank(String.valueOf(matchscheme))){
				sb.append(" where a.match_scheme=? and  a.drugcode=? ");
				List<Object> listvalue =new ArrayList<Object>();
				listvalue.add(matchscheme);
				listvalue.add(drugcode);
				list = jdbcTemplate.queryForList(sb.toString(), listvalue.toArray());
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						SysDictDrug priv = copyFromMap(list.get(0));
						 this.addOrUpdate(priv);
					}
					return true;
				}else{
					return false;
				}
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
	public SysDictDrug copyFromMap(Map<String,Object> map){
		SysDictDrug custom = new SysDictDrug();
		custom.setMatchscheme(Integer.parseInt(map.get("match_scheme").toString()));
		custom.setDrugcode(FieldUtils.getNullStr(map.get("drugcode"),""));
		custom.setDrugform(FieldUtils.getNullStr(map.get("drugform"),""));
		custom.setIsbasedrug(FieldUtils.getDefaultInt(map.get("is_basedrug"),0));
		custom.setIsbbasedrugp(FieldUtils.getDefaultInt(map.get("is_basedrug_p"),0));
		custom.setIsanti(FieldUtils.getDefaultInt(map.get("is_anti"),0));
		custom.setAntitype(FieldUtils.getDefaultInt(map.get("antitype"),0));
		custom.setAntilevel(FieldUtils.getDefaultInt(map.get("antilevel"),0));
		custom.setDrugtype(FieldUtils.getDefaultInt(map.get("drugtype"),0));
		custom.setDrugformtype(FieldUtils.getDefaultInt(map.get("drugformtype"),0));
		custom.setSocialsecurityRatio(FieldUtils.getNullStr(map.get("socialsecurity_ratio"),""));
		custom.setIssocialSecurity(FieldUtils.getDefaultInt(map.get("is_socialsecurity"),0));
		custom.setSocialsecurityDesc(FieldUtils.getNullStr(map.get("socialsecurity_desc"),""));
		custom.setJdmtype(FieldUtils.getDefaultInt(map.get("jdmtype"),0));
		custom.setIsstimulant(FieldUtils.getDefaultInt(map.get("is_stimulant"),0));
		custom.setStimulantingred(FieldUtils.getNullStr(map.get("stimulantingred"),""));
		custom.setIssolvent(FieldUtils.getDefaultInt(map.get("is_solvent"),0));
		custom.setIssrpreparations(FieldUtils.getDefaultInt(map.get("is_srpreparations"),0));
		custom.setIsneedskintest(FieldUtils.getDefaultInt(map.get("is_needskintest"),0));
		custom.setIsdearmed(FieldUtils.getDefaultInt(map.get("is_dearmed"),0));
		custom.setIspoison(FieldUtils.getDefaultInt(map.get("is_poison"),0));
		custom.setIsbloodmed(FieldUtils.getDefaultInt(map.get("is_bloodmed"),0));
		custom.setIssugarmed(FieldUtils.getDefaultInt(map.get("is_sugarmed"),0));
		custom.setOtctype(FieldUtils.getDefaultInt(map.get("otctype"),0));
		custom.setIsbisectionUse(FieldUtils.getDefaultInt(map.get("is_bisection_use"),0));
		custom.setHighAlertLevel(FieldUtils.getDefaultInt(map.get("high_alert_level"),0));
		return custom;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  initAll </li>
	 * <li>功能描述： 执行更新所有的数据</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul>
	 */
	public void initAll(){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(SEARCH_DISTDRUG);
		if(list!=null&&list.size()>0){
			//ShardedJedis jedis = this.getShardedJedisPool().getResource();
			for(int i=0;i<list.size();i++){
				SysDictDrug custom = copyFromMap(list.get(i));
				String key = DISTDRUG_TITLE+custom.getMatchscheme()+"-"+custom.getDrugcode().trim();
				this.addOrUpdate(custom,key);
			}
		}
	}


}
