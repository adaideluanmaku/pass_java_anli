package com.ch.redissys;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import com.ch.jdbc.Mysqlconn;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.medicom.passlan.redis.shield.SheildEnum;
import com.medicom.passlan.redis.shield.ShieldData;
import com.medicom.passlan.util.RedisSerialization;

import redis.clients.jedis.Jedis;

public class Redis_conf {
	public static Jedis jedis;
	private RedisSerializer<String> stringSerializer = new StringRedisSerializer();
	private RedisSerializer valueSerializer = null;
	private RedisSerializer<?> defaultSerializer = new JdkSerializationRedisSerializer();
	private boolean defaultUsed = true;
	
	public String servername;
	
	
	public void setServername(String servername) {
		this.servername = servername;
	}

	//	static {
//		InputStream in=Redis_conf.class.getClassLoader().getResourceAsStream("config.properties");
//		Properties prop=new Properties();		
//		try {
//			prop.load(in);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		String redisip=prop.getProperty("redisip");
//		int redisport=Integer.parseInt(prop.getProperty("redisport"));
//		String redispassword=prop.getProperty("redispassword");
//		int redisnum=Integer.parseInt(prop.getProperty("redisnum"));
//		
//		jedis = new Jedis(redisip, redisport);
//		if(!"".equals(redispassword)){
//			jedis.auth(redispassword);
//		}
//		jedis.select(redisnum);
//	}
	//redis连接
	public Jedis redisconnect() throws IOException, ClassNotFoundException, SQLException{
//		if(jedis==null){
//			try{
//				InputStream in=Redis_conf.class.getClassLoader().getResourceAsStream("config.properties");
//				Properties prop=new Properties();		
//				prop.load(in);
//				
//				String redisip=prop.getProperty("redisip");
//				int redisport=Integer.parseInt(prop.getProperty("redisport"));
//				String redispassword=prop.getProperty("redispassword");
//				int redisnum=Integer.parseInt(prop.getProperty("redisnum"));
//				
//				jedis = new Jedis(redisip, redisport);
//				if(!"".equals(redispassword)){
//					jedis.auth(redispassword);
//				}
//				jedis.select(redisnum);
//			}catch(Exception e){
//				System.out.println("redis连接失败");
//			}
//		}
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		System.out.println("redis数据库连接配置："+servername);
		sql="select * from serverip where servername=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, servername);
		rs=pst.executeQuery();
		List list=mysql.getlist(rs);
		
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			if(map.get("redisip")==null || map.get("redisport")==null){
				jedis=null;
				continue;
			}
			if("".equals(map.get("redisip")) || "".equals(map.get("redisport"))){
				jedis=null;
				continue;
			}
			jedis = new Jedis(map.get("redisip").toString(), Integer.parseInt(map.get("redisport").toString()));
			
			if(map.get("redispw")!=null){
				if(!"".equals(map.get("redispw"))){
					jedis.auth(map.get("redispw").toString());
				}
			}
			jedis.select(Integer.parseInt(map.get("redisnum").toString()));
		}
		rs.close();
		pst.close();
		return jedis;
	}
	
	//key值序列化1
	public static <T> T  get(final Object key){
		byte[] keys = JRedisSerializationUtils.serialize(key);
		if(jedis==null){
			return null;
		}
		byte[] values = jedis.get(keys);
			if (values == null) {
			return null;
		}
		return (T) JRedisSerializationUtils.unserialize(values);
	}
	
	//反射技术
	//classaddres=序列化对象的class路径,import地址
	//obj=获取的对象
	public static List passfields(String classaddres,Object obj) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		List list=new ArrayList();
		//反射技术得到对象所有属性和属性值
//		Class c =  Class.forName("com.medicom.passlan.redis.sys.SysMatchRelation"); 
		Class c =  Class.forName(classaddres); 
		Field[] fields = c.getDeclaredFields();
		for(Field f:fields){  
			f.setAccessible(true);  
	    } 
		for(Field f:fields){  
            String field = f.toString().substring(f.toString().lastIndexOf(".")+1);         //取出属性名称  
            System.out.println("getvalues."+field+" --> "+f.get(obj)); 
            Map map=new HashMap();
            map.put(field, f.get(obj));
            list.add(map);
        }
		return list;
	}
	
	
	//redis消息订阅，自定义内容后推送
	public void convertAndSend(Jedis jedis,String channel, Object message) {
		Assert.hasText(channel, "a non-empty channel is required");
//		System.out.println(channel);
//		System.out.println(message);
		final byte[] rawChannel = stringSerializer.serialize(channel);
		final byte[] rawMessage;
		if (valueSerializer == null) {
			valueSerializer = defaultSerializer;
			defaultUsed = true;
		}
		if (valueSerializer == null && message instanceof byte[]) {
			rawMessage=message.toString().getBytes();
		}else{
			rawMessage=valueSerializer.serialize(message);
		}
		System.out.println("推送消息结束");
		jedis.publish(rawChannel, rawMessage);
	}
	
	//屏蔽表需要处理的数据-----------------------------------------------------------------------------
	public String key(Integer modelid,ShieldData shieldData){
		String key = "user_shielddata-"+modelid;
		if(modelid==SheildEnum.PASS_DOSAGE.getCustomindex()){
			/**< 鍓傞噺鑼冨洿 */
			key = key+"-"+shieldData.getHiscode().trim()
					+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_HEPDOSAGE.getCustomindex()){
			 /**< 鑲濇崯瀹冲墏閲�*/
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_RENDOSAGE.getCustomindex()){
			  /**< 鐩镐簰浣滅敤 */
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_DRUGINTER.getCustomindex()){
			//锟洁互锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDruguniquecodetwo();
		}else if(modelid==SheildEnum.PASS_IV.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷锟斤拷
			if(compStrOrder(shieldData.getDruguniquecodeone(),shieldData.getDruguniquecodetwo())>0){
				key = key+"-"+shieldData.getHiscode().trim()
				+"-"+shieldData.getDruguniquecodetwo().trim()+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getRoutetype();
			}else{
				key = key+"-"+shieldData.getHiscode().trim()
				+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDruguniquecodetwo().trim()+"-"+shieldData.getRoutetype();
			}
			
		}else if(modelid==SheildEnum.PASS_DRUGLEVEL.getCustomindex()){
			//锟斤拷锟斤拷浓锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDruguniquecodetwo().trim();
		}else if(modelid==SheildEnum.PASS_CONTRAIND.getCustomindex()){
			//锟斤拷锟斤拷浓锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDiscode();
		}else if(modelid==SheildEnum.PASS_ADR.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷
			key = key+"-"+ StringUtils.trim(shieldData.getHiscode())
			+"-"+StringUtils.trim(shieldData.getDruguniquecodeone())+"-"+StringUtils.trim(shieldData.getDiscode());
		}else if(modelid==SheildEnum.PASS_INDICATION.getCustomindex()){
			//锟斤拷锟斤拷应锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDiscode();
		}else if(modelid==SheildEnum.PASS_PEDIATRIC.getCustomindex()){
			//锟斤拷锟斤拷应锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_ADULT.getCustomindex()){
			//锟斤拷锟斤拷应锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_GERIATRIC.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷药
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_PREGNANCY.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷药锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_LACTATION.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷药锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_SEX.getCustomindex()){
			//锟皆憋拷锟斤拷药锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim();
		}else if(modelid==SheildEnum.PASS_DRUGALLERGEN.getCustomindex()){
			//锟皆憋拷锟斤拷药锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+shieldData.getDruguniquecodeone().trim()+"-"+shieldData.getDoseunit()+"-"+shieldData.getRoutecode();
		}else if(modelid==SheildEnum.PASS_ROUTE.getCustomindex()){
			// 锟斤拷药途锟斤拷锟斤拷锟斤拷
			key = key+"-"+shieldData.getHiscode().trim()
			+"-"+StringUtils.trim(shieldData.getDruguniquecodeone()) +"-"+ StringUtils.trim(shieldData.getRoutecode());
		}
		return key;
	}
	public static int compStrOrder(String str1,String str2){
		int len1 = str1.length();
		int len2 = str2.length();
		for(int i = 0;i < 15 - len1;i++){
			str1 = " "+str1;
		}
		for(int i = 0;i < 15 - len2;i++){
			str2 = " "+str2;
		}
		return str1.compareTo(str2);
	}
	public boolean findbyList(List<ShieldData> list, ShieldData t) {
		boolean isSure = false;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				ShieldData temp = list.get(i);
				if(temp.getPkid()==t.getPkid()){
					//杩欓噷鍘绘帀閲嶅鎵惧埌鐨勬垚浜虹敤鑽殑瀵硅薄
					list.remove(temp);
					isSure =  true;
				}
			}
		}else{
			list = new ArrayList<ShieldData>();
		}
		return isSure;
	}
	public String returnSql(Integer modelid,int pkid,int typestart){
		String sql="select pkid,hiscode,moduleid,modulename,drug_unique_code1,"
			+ "drug_unique_code2,doseunit,routecode,routetype,discode,agelow,unequal_low,agelow_unit,"
			+ "agehigh,unequal_high,agehigh_unit,shielddesc,warning FROM mc_user_shielddata";
		if(modelid==SheildEnum.PASS_DOSAGE.getCustomindex()){
			//鍓傞噺鑼冨洿
			sql=sql+" where moduleid = 1  and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> ' ' and doseunit is not null and doseunit <> ' '";
			if(pkid>0){
				sql=sql+" and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by hiscode, drug_unique_code1, routecode, doseunit ";
		}else if(modelid==SheildEnum.PASS_HEPDOSAGE.getCustomindex()){
			//锟斤拷锟斤拷
			sql=sql+" where moduleid = 2  and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> ' ' and doseunit is not null and doseunit <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by drug_unique_code1, routecode, doseunit";
		}else if(modelid==SheildEnum.PASS_RENDOSAGE.getCustomindex()){
			//锟斤拷锟斤拷
			sql=sql+" where moduleid = 3 and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> ' ' and doseunit is not null and doseunit <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by drug_unique_code1, routecode, doseunit";
		}else if(modelid==SheildEnum.PASS_DRUGINTER.getCustomindex()){
			//锟洁互锟斤拷锟斤拷
			sql=sql+" where moduleid = 4 and drug_unique_code1 is not null  and drug_unique_code1 <> '' and drug_unique_code2 is not null and drug_unique_code2 <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by hiscode, drug_unique_code1, drug_unique_code2";
		}else if(modelid==SheildEnum.PASS_IV.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷锟斤拷
			sql=sql+" where moduleid = 5 and drug_unique_code1 is not null and drug_unique_code1 <> '' and drug_unique_code2 is not null and drug_unique_code2 <> ' ' and routetype is not null and routetype > 0 ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by hiscode,drug_unique_code1,drug_unique_code2, routetype ";
		}else if(modelid==SheildEnum.PASS_DRUGLEVEL.getCustomindex()){
			//锟斤拷锟斤拷浓锟斤拷
			sql=sql+" where moduleid = 6 and drug_unique_code1 is not null and drug_unique_code1 <> ''and drug_unique_code2 is not null and drug_unique_code2 <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by hiscode, drug_unique_code1, drug_unique_code2 ";
		}else if(modelid==SheildEnum.PASS_CONTRAIND.getCustomindex()){
			//锟斤拷锟斤拷浓锟斤拷
			sql=sql+" where moduleid = 7 and drug_unique_code1 is not null and drug_unique_code1 <> '' and discode is not null and discode <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by hiscode, drug_unique_code1, discode ";
		}else if(modelid==SheildEnum.PASS_ADR.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷
			sql=sql+" where moduleid = 8 and drug_unique_code1 is not null and drug_unique_code1 <> '' and discode is not null and discode <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by hiscode, drug_unique_code1, discode ";
		}else if(modelid==SheildEnum.PASS_INDICATION.getCustomindex()){
			//锟斤拷锟斤拷应锟斤拷锟斤拷
			sql=sql+" where moduleid = 9 and drug_unique_code1 is not null and drug_unique_code1 <> '' and discode is not null and discode <> ' ' ";
			if(pkid>0){
				sql=sql+("and pkid=? ");
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by drug_unique_code1, discode ";
		}else if(modelid==SheildEnum.PASS_PEDIATRIC.getCustomindex()){
			//锟斤拷锟斤拷应锟斤拷锟斤拷
			sql=sql+" where moduleid = 10 and drug_unique_code1 is not null and drug_unique_code1 <> '' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by drug_unique_code1 ";
		}else if(modelid==SheildEnum.PASS_ADULT.getCustomindex()){
			//锟斤拷锟斤拷应锟斤拷锟斤拷
			sql=sql+" where moduleid =11 and drug_unique_code1 is not null and drug_unique_code1 <> '' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by drug_unique_code1 ";
		}else if(modelid==SheildEnum.PASS_GERIATRIC.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷药
			sql=sql+" where moduleid=12 and drug_unique_code1 is not null and drug_unique_code1 <> '' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_PREGNANCY.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷药锟斤拷锟斤拷
			sql=sql+" where moduleid=13 and drug_unique_code1 is not null and drug_unique_code1 <> '' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_LACTATION.getCustomindex()){
			//锟斤拷锟斤拷锟斤拷药锟斤拷锟斤拷
			sql=sql+" where moduleid=14 ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_SEX.getCustomindex()){
			//锟皆憋拷锟斤拷药锟斤拷锟斤拷
			sql=sql+" where moduleid=15 and drug_unique_code1 is not null and drug_unique_code1 <> ' ' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+" order by drug_unique_code1  ";
		}else if(modelid==SheildEnum.PASS_DRUGALLERGEN.getCustomindex()){
			//锟皆憋拷锟斤拷药锟斤拷锟斤拷
			sql=sql+" where moduleid=16 ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
		}else if(modelid==SheildEnum.PASS_ROUTE.getCustomindex()){
			//锟皆憋拷锟斤拷药锟斤拷锟斤拷
			sql=sql+" where moduleid = 17 and drug_unique_code1 is not null and drug_unique_code1 <> '' and routecode is not null and routecode <> '' ";
			if(pkid>0){
				sql=sql+"and pkid=? ";
			}
//			if(typestart==2){
//				sb.append("and intstart=2 ");
//			}
			sql=sql+"  order by hiscode,drug_unique_code1, routecode  ";
		}
		return sql;
	}
	//屏蔽表需要处理的数据-----------------------------------------------------------------------------
}
