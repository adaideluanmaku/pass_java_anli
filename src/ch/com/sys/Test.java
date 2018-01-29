package ch.com.sys;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;

import redis.clients.jedis.Jedis;
import ch.com.screen.Passscreen;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.PassMysqlconn;
import com.ch.redissys.JRedisSerializationUtils;
import com.ch.redissys.Redis_conf;
import com.ch.redissys.Redis_query;
import com.medicom.passlan.redis.custom.DrugDisCustom;
import com.medicom.passlan.redis.shield.ShieldData;

public class Test {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, TimeoutException, DocumentException, IOException, IllegalArgumentException, IllegalAccessException{
//		InputStream in=PassMysqlconn.class.getClassLoader().getResourceAsStream("config.properties");
//		Properties prop=new Properties();		
//		prop.load(in);
//		String redisurl=prop.getProperty("passlanmanage");
//		Passservice Passservice=new Passservice();
//		String result_clean = Passservice.getPassResult("",redisurl+"/redis/cleanRedis") ; 
//		System.out.println("清除redis,"+result_clean);
//    	
//    	//初始化系统缓存
//    	String result_sys = Passservice.getPassResult("",redisurl+"/redis/updateSys") ;
//		System.out.println("更新redics系统缓存，"+result_sys);
//		
//		//初始化系统自定义的数据
//    	String result_custom =Passservice.getPassResult("",redisurl+"/redis/updateCustom") ; 
//    	System.out.println("更新redics自定义的数据，"+result_custom);
//    	
//    	//初始化系统屏蔽数据
//    	String result_shield = Passservice.getPassResult("",redisurl+"/redis/updateShield") ; 
//    	System.out.println("更新redics屏蔽数据，"+result_shield);
		
		Redis_conf Redis_conf=new Redis_conf();
//		Redis_conf.setServername("123");
//		Redis_conf.redisconnect();
		Redis_query Redis_sys=new Redis_query();
		Redis_sys.setServername("zyq");
//		
		//配对方案"match_relation-001001"
//		Redis_query Redis_sys=new Redis_query();
		List list;
		String str;
		
//		list=Redis_sys.sysConfigImpl("年龄范围");//年龄范围
//		System.out.println(list);
		
		
//		list=Redis_sys.sysMatchRelation("001001");
//		System.out.println(list);
//		
//		list=Redis_sys.Hospital("1303");
//		System.out.println(list);
//		
//		list=Redis_sys.sysDoctorInface(1, "+YS0001");
//		System.out.println(list);
		
//		list=Redis_sys.sysDrugMatch(1, "010102017180");
//		System.out.println(list);
		
//		list=Redis_sys.drugDoseUnitImpl(1, "0101011TA0", "375mg", "mg", "mg");
//		System.out.println(list);
		
//		Autoredis Autoredis=new Autoredis();
//		Autoredis.oneupdate("手动不良-反应100","1609");
		
//		Redis_query Redis_sys=new Redis_query();
//		List list;
//		list=Redis_sys.shieldImple("PB167148528");
//		System.out.println(list);
		
//		list=Redis_sys.bacresisInfaceImpl("001001","25932");
//		System.out.println(list);
		
//		List<DrugDisCustom> objlist = Redis_conf.get("user_drugdis-8-001001-2001880");
//		if(objlist==null){
//			System.out.println("user_drugdis,redis中未找到该数据");
//			return;
//		}
//		for(int i=0;i<objlist.size();i++){
//			DrugDisCustom obj=objlist.get(i);
//			System.out.println(obj);
//		}
		
//		Jedis jedis = Redis_conf.redisconnect();
		Redis_sys.shieldImple(0,"");
		
	}
}
