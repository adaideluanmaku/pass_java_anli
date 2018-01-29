package ch.com.PAandPassModule_Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ListOperations;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.Oracleconn;
import com.ch.jdbc.PassMysqlconn;
import com.ch.jdbc.Sqlserverconn;

import ch.com.sys.Passservice;
import freemarker.template.utility.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
/**
 * 1.调用PASS审查，结果数据PASS写入redis
 * 2.通过统计分析临时表的数据制作分表数据
 * 3.调用PA审查，结果数据PA保存在PA审查结果表里面
 * @author 陈辉
 *
 */
public class Test1 {
	public static Jedis jedis;
	public static final String PA_SCREENRESULTS = "PA_SCREENRESULT_LIST";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		Sqlserverconn sqlserver=new Sqlserverconn();
		Connection sqlserverconn=sqlserver.getConn();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
		
		sql="select * from mc_dict_drug where match_scheme=0";
		st=sqlserverconn.createStatement();
		rs=st.executeQuery(sql);
		list=sqlserver.getlist(rs);
		
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			sql="update mc_dict_drug set druggroupcode='"+(i+1)+"',druggroupname='"
			+map.get("drugname")+"' where match_scheme=0 and drugcode='"+map.get("drugcode")+"'";
			st=sqlserverconn.createStatement();
			st.executeUpdate(sql);
			
			System.out.println(list.size()+"----->"+i);
		}
		
		rs.close();
		st.close();
		passmysqlconn.close();
		sqlserverconn.close();
	}
}
