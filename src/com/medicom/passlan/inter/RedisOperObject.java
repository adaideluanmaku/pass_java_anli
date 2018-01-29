package com.medicom.passlan.inter;

import java.util.List;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RedisOperObject </li>
 * <li>类描述：   没有对象的操作接口 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月27日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public interface RedisOperObject {
	
	boolean add(String key,Object t);
	
	boolean addOrUpdate(String key,Object t);
	
	boolean delete(String key);
	
	boolean isExiter(String key);
	
	boolean addList(String key,List<Object> list);
	
	<T>T  get(Object key);
}
