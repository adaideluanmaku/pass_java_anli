package com.medicom.passlan.inter.imp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.medicom.passlan.inter.ComonInfaceImpl;
import com.medicom.passlan.inter.ComonInfaceImplshare;
import com.medicom.passlan.inter.RedisOper;

public class CommonInface<T> extends ComonInfaceImpl<T> implements RedisOper<T> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public boolean delete(String key) {
		return this.deleteCommon(key);
	}

	public boolean isExiter(String key) {
		// TODO Auto-generated method stub
		return this.isExiterCommon(key);
	}

	public boolean addOrUpdate(T t, String key,String servername) throws ClassNotFoundException, SQLException {
		return this.addOrUpdateCommon(t, key,servername);
	}

	public boolean addList(List<T> list, String key) {
		return this.addOrUpdateList(list, key);
	}

	public boolean findbyList(List<T> list, T t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  initdateObject </li>
	 * <li>鍔熻兘鎻忚堪锛氱洿鎺ヨ繑鍥炲璞�</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�9鏃�</li>
	 * </ul> 
	 * @param t 娉涘瀷鐨勫璞�
	 * @param searchsql 鏌ヨ璇彞
	 * @return T
	 */
	public T initdateObject(T t,String searchsql){
		SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
		return (T)namedParameterJdbcTemplate.queryForObject(searchsql, ps, t.getClass());
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  initdateObject </li>
	 * <li>鍔熻兘鎻忚堪锛氱洿鎺ヨ繑鍥炲璞�</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�9鏃�</li>
	 * </ul> 
	 * @param t 娉涘瀷鐨勫璞�
	 * @param searchsql 鏌ヨ璇彞
	 * @return List<T>
	 */
	public List<T> initDateList(T t,String searchsql){
		  SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
	      return namedParameterJdbcTemplate.query(searchsql, ps, new BeanPropertyRowMapper(t.getClass()));
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  getListMap </li>
	 * <li>鍔熻兘鎻忚堪锛氱洿鎺ヨ繑鍥炴煡璇㈢殑sql璇彞 </li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�9鏃�</li>
	 * </ul> 
	 * @param searchsql
	 * @param val
	 * @return
	 */
	public List<Map<String,Object>> getListMap(String searchsql,Object...val){
		return jdbcTemplate.queryForList(searchsql, val); 
	}

	@Override
	public boolean addOrUpdate(T t, String key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	
	

}
