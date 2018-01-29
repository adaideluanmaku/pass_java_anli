package com.medicom.passlan.inter;

import java.util.List;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RedisOper </li>
 * <li>类描述：  主要是对数据的增删改的接口 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月23日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public interface RedisOper<T> {
	
	boolean addOrUpdate(T t,String key);
	
	
	boolean delete(String key);
	
	boolean isExiter(String key);
	
	boolean addList(List<T> list,String key);
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  findbyList </li>
	 * <li>功能描述：根据泛型 获取到相应的对象是否存在 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月27日 </li>
	 * </ul> 
	 * @param list
	 * @return
	 */
	boolean findbyList(List<T> list,T t);
	
	
	
	
	

}
