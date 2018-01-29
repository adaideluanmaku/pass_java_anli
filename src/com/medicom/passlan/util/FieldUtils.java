package com.medicom.passlan.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  FieldUtils </li>
 * <li>类描述：   处理数据库的特殊字符串的</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class FieldUtils {
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  getNullStr </li>
	 * <li>功能描述：对字符串进行处理 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param str
	 * @param deafaultStr
	 * @return
	 */
	public static String getNullStr(Object str,String deafaultStr){
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			return str.toString();
		}else{
			if(deafaultStr!=null){
				return deafaultStr;
			}else{
				//这里设置默认值返回空串
				return "";
			}
		}
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  getDefaultInt </li>
	 * <li>功能描述： </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param str
	 * @param defaultInt
	 * @return
	 */
	public static int getDefaultInt(Object str,int defaultInt){
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			return Integer.parseInt(str.toString());
		}else{
			return defaultInt;
		}
	}
	
	public static Integer getDefaultInteger(Object str,Integer defaultInteger){
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			return Integer.parseInt(str.toString());
		}else{
			return defaultInteger;
		}
	}
	
	public static Long getDefaultLong(Object str,Long defalutlong){
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			return Long.parseLong(str.toString());
		}else{
			return defalutlong;
		}
	}
	/**
	 * 
	 * <ul>
	 * <li>方法名：  getDefultDou </li>
	 * <li>功能描述：获取Double的数据 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月29日 </li>
	 * </ul> 
	 * @param str 字符串的数据
	 * @param defaultDou 缺省的double值
	 * @return double
	 */
	public static double getDefultDou(Object str,double defaultDou){
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			return Double.parseDouble(str.toString());
		}else{
			return defaultDou;
		}
	}
	
	public static float getDefaultF(Object str,float deaultf){
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			return Float.parseFloat(str.toString());
		}else{
			return deaultf;
		}
	}
	
	public static void main(String[] args){
		System.out.println(FieldUtils.getDefaultLong(1,0L));;
		Object str = 1;
		System.out.println("str != null="+ (str != null) );
		System.out.println("str.toString()="+str.toString());
		System.out.println("....="+StringUtils.isNotBlank(str.toString()));
		System.out.println(Long.parseLong("1"));
		if (str != null && StringUtils.isNotBlank(str.toString())) {
			System.out.println(Long.getLong(str.toString()));
		}else{
			System.out.println("....");
		}
		
		
	}
	
	
	
	
	
}
