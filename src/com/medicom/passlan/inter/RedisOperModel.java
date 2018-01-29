package com.medicom.passlan.inter;

import java.util.List;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RedisOper </li>
 * <li>类描述：  主要是对数据的增删改的接口(带泛型的数据) </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月23日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public interface RedisOperModel<T> {
	
	boolean addOrUpdateListCommon(T t,final String key);
	
	boolean addOrUpdateList(List<T> list,String key);
	
	boolean addOrUpdateCommon(T t,final String key);
	
	boolean deleteCommon(final String key);
	
	boolean isExiterCommon(final String key);
	
	boolean addListCommon(List<T> list,final String key);
		
	<T>T  get(Object key);

}
