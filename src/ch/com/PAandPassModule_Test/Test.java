package ch.com.PAandPassModule_Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.Oracleconn;
import com.ch.jdbc.PassMysqlconn;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
		final int count=1;//一批案例的循环次数
		
		int mz=1;//控制数据制造开关 0关，1开
		int zy=0;
		int cy=0;
		int dict=0;
		
		final int mhiscode=199004;
		final int ienddate=20170101;
		final String enddate="2017-01-01";
		
//		int iid=0;
//		int a=0;
//		int b=0;
		
		//缺少案例 "超多日用量025"
		String[] anliname={"不良反应015","体外配伍068","儿童用药027","剂量范围014","哺乳用药046","围手术期020",
				"妊娠用药012","性别用药031","成人用药018","相互作用022","细菌耐药率015","给药途径036","老人用药021",
				"肝损害剂量027","肾损害剂量066","药物禁忌症032","药物过敏028","超适应症023",
				"越权用药019","配伍浓度020","重复用药031","钾离子浓度069"};
//		String[] anliname={"不良反应015"};
		
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		final List anlilist=new ArrayList();
		
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		//目标数据
		Oracleconn oraclesql=new Oracleconn();
		Connection oraclesqlconn=oraclesql.getConn();
				
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
		
		//还原主键
		//sa_screenresults数据
//		sql="alter table sa_screenresults change chkresid chkresid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_disease数据
//		sql="alter table sa_pat_disease change disid disid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_allergens数据
//		sql="alter table sa_pat_allergens change allerid allerid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_orders数据
//		sql="alter table sa_pat_orders change cid cid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_operation数据
//		sql="alter table sa_pat_operation change oprid oprid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_request数据
//		sql="alter table sa_request change logid logid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		
		//打印字段
//		sql="select * from t_mc_inhosp_patient where rownum = 1 ";//oracle
		sql="select * from mc_review_main limit 1 ";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		Map map=(Map) list.get(0);
		
		String a=map.keySet().toString();
		System.out.println(a);
		String[] b=a.substring(1,a.length()-1).split(",");
		
		System.out.println(b.length);
		String c="";
		int d=0;
		for(int i=0;i<b.length;i++){
			d=d+1;
			//去除没用字段
			if("inserttime11".equals(b[i].trim())){
				d=d-1;
			}else{
				c=c+"?,";
				System.out.println("pst.setString("+(d)+",strisnull.isnull(map.get(\""+b[i].trim()+"\")).toString());//"+b[i].trim());
//				System.out.println("pst.setString("+(d)+",map.get(\""+b[i].trim()+"\").toString());//"+b[i].trim());
//				System.out.println("pst.setString("+(d)+",ScreenDrug.getString(\""+b[i].trim()+"\"));//"+b[i].trim());
//				System.out.println("pst.setString("+(d)+",Patient.getString(\""+b[i].trim()+"\"));//"+b[i].trim());
			}
		}
		System.out.println(c);
		
//		sql="select * from mc_dict_operation where match_scheme=6";
//		st=passmysqlconn.createStatement();
//		rs=st.executeQuery(sql);
//		list=passmysql.getlist(rs);
//		
//		sql="truncate table mc_dict_operation ";
//		st=oraclesqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		sql="insert into mc_dict_operation(premoment_high, searchcode, premoment_low, operationname, is_save, "
//				+ "typename, createdate, match_scheme, useanti, drugtime, operationcode) "
//				+ "values(?,?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
//		pst=oraclesqlconn.prepareStatement(sql);
//		for(int i=0;i<list.size();i++){
//			Map map=(Map)list.get(i);
//			pst.setString(1,map.get("premoment_high").toString());//premoment_high
//			pst.setString(2,map.get("searchcode").toString());//searchcode
//			pst.setString(3,map.get("premoment_low").toString());//premoment_low
//			pst.setString(4,map.get("operationname").toString());//operationname
//			pst.setString(5,map.get("is_save").toString());//is_save
//			if("".equals(map.get("typename")) || map.get("typename")==null){
//				pst.setString(6,"");//typename
//			}else{
//				pst.setString(6,map.get("typename").toString());//typename
//			}
//			System.out.println(map.get("createdate").toString().substring(0,19));
//			pst.setString(7,map.get("createdate").toString().substring(0,19));//createdate
//			pst.setString(8,map.get("match_scheme").toString());//match_scheme
//			pst.setString(9,map.get("useanti").toString());//useanti
//			pst.setString(10,map.get("drugtime").toString());//drugtime
//			pst.setString(11,map.get("operationcode").toString());//operationcode
//			
//			pst.executeUpdate();
//		}
//		rs.close();
//		st.close();
//		passmysqlconn.close();
//		oraclesqlconn.close();
		
//		Date time=new Date();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(sdf.format(time));
//		
//		Date time1=new Date();
//		System.out.println(sdf.format(time1)-sdf.format(time));
		
		
//		String a="104366+注射用左氧氟沙星+粉针剂+500mg+吉林海外制药有限公司";
//		String[] b=a.split("\\+");
//		System.out.println(b[0]);
		
//		sql="select id,gatherbaseinfo from sa_gather_log ";
//		st=passmysqlconn.createStatement();
//		rs=st.executeQuery(sql);
//		list=passmysql.getlist(rs);
//		for(int i=0;i<list.size();i++){
//			Map map=(Map)list.get(i);
//			JSONObject json=JSONObject.fromObject(map.get("gatherbaseinfo"));
//			JSONObject PassClient=json.getJSONObject("PassClient");
//			PassClient.put("HospID", "1609PASS");
//			sql="update sa_gather_log set gatherbaseinfo=? where id=?";
//			pst=passmysqlconn.prepareStatement(sql);
//			pst.setString(1, json.toString());
//			pst.setInt(2, Integer.parseInt(map.get("id").toString()));
//			pst.executeUpdate();
//		}
		
	}
}
