package ch.com.daoanli;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONObject;
import ch.com.action.Loginaction;
import ch.com.sys.Modulename;
import ch.com.sys.Updatejson;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.Sqlserverconn;


/**
 * 2016.11.26
 * 从sqlserver导入数据导mysql中
 * **/
public class Daoanli {
	public String anliname;
	public int anlitype=0;
	public int moduleid=0;
	public String modulename;
	
	//输入JSON解析
	public void Baseinfo(String json,String version) throws ClassNotFoundException, SQLException, IOException{
		JSONObject jsonin=JSONObject.fromObject(json);
		JSONObject Patient=jsonin.getJSONObject("Patient");
		anliname=Patient.getString("Name");
		
		//读取原来案例的变更记录
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		sql="select count(*) from  log where anliname=? and version=? and (anlitype is not null or anlitype <>'')";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		rs=pst.executeQuery();
		rs.next();
		if(Integer.parseInt(rs.getObject(1).toString())>0){
			sql="select * from  log where anliname=? and version=? and (anlitype is not null or anlitype <>'')";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname);
			pst.setString(2, version);
			rs=pst.executeQuery();
			List list=mysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				anlitype=Integer.parseInt(map.get("anlitype").toString());
				moduleid=Integer.parseInt(map.get("moduleid").toString());
				modulename=map.get("modulename").toString();
			}
		}else{
			Modulename Module=new Modulename();
			Module.getId(anliname);
			anlitype=Module.getAnlitype();
			moduleid=Module.getModuleid();
			modulename=Module.getModulename();
		}
	}
	
	//sqlserver导案例数据导mysql
	public String Sqlser_to_mysql_all(String version,int dao,String anliname1) throws ClassNotFoundException, SQLException, IOException{
		if(StringUtils.isBlank(version)){
			return "请输入版本号";
		}
		InputStream in=Mysqlconn.class.getClassLoader().getResourceAsStream("config.properties");
		Properties prop=new Properties();		
		prop.load(in);
		
		//接连sqlserver数据库
		Sqlserverconn sqlser=new Sqlserverconn();
		//接连mysql数据库
		Mysqlconn mysql=new Mysqlconn();
		
		Connection sqlserconn=sqlser.getConn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select gatherbaseinfo,gatherresult from sa_gather_info";
		st=sqlserconn.createStatement();
		rs=st.executeQuery(sql);
		List sqlserlist=sqlser.getlist(rs);
		
		//导入前删除log表,全删除或删除部分案例
		if(dao==111){
			sql="delete from sa_gather_log where version=? and usertype <> 1";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			pst.executeUpdate();
		}else if(dao==112){
			if("".equals(anliname1) || anliname1==null){
				return "案例名称为空";
			}
			sql="delete from  sa_gather_log where anliname like ? and version=? and usertype <> 1";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, "%"+anliname1+"%");
			pst.setString(2, version);
			pst.executeUpdate();
		}else{
			sql="delete from  sa_gather_log where anlitype=? and version=? and usertype <> 1";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, dao);
			pst.setString(2, version);
			pst.executeUpdate();
		}
		
		//删除垃圾数据,全删除或删除部分案例
		if(dao==111){
			sql="delete from anli_err where version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			pst.executeUpdate();
		}else if(dao==112){
			sql="delete from  anli_err where anliname like ? and version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, "%"+anliname1+"%");
			pst.setString(2, version);
			pst.executeUpdate();
		}else{
			sql="delete from  anli_err where anlitype=? and version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, dao);
			pst.setString(2, version);
			pst.executeUpdate();
		}
		
		
		// 默认情况下每执行一条sql提交一次 ,关闭自动提交，可配合.commit()实现批量执行sql
		
		String hiscode=prop.getProperty("hiscode");
		Updatejson Updatejson=new Updatejson();//导数据时更改HISCODE值
		mysqlconn.setAutoCommit(false);
		sql="insert into sa_gather_log(gatherbaseinfo,gatherresult,anliname,anlitype,moduleid,modulename,version,usertype) values(?,?,?,?,?,?,?,?)";
		pst=mysqlconn.prepareStatement(sql);
		
		for(int i=0;i<sqlserlist.size();i++){
			Map sqlsermap=(Map)sqlserlist.get(i);
			//gatherbaseinfo解析案例,分解类型
			Baseinfo(sqlsermap.get("gatherbaseinfo").toString(),version);
			if(dao==111){
				String gatherbaseinfo=Updatejson.getUpdate(sqlsermap.get("gatherbaseinfo").toString(),hiscode);//替换HISCODE
				pst.setString(1, gatherbaseinfo);
				pst.setString(2, sqlsermap.get("gatherresult").toString());
				pst.setString(3, anliname);
				pst.setInt(4, anlitype);
				pst.setInt(5, moduleid);
				pst.setString(6, modulename);
				pst.setString(7, version);
				pst.setInt(8, 0);
				pst.addBatch();
			}else if(dao==112){
				if(anliname.contains(anliname1)){
					String gatherbaseinfo=Updatejson.getUpdate(sqlsermap.get("gatherbaseinfo").toString(),hiscode);//替换HISCODE
					pst.setString(1, gatherbaseinfo);
					pst.setString(2, sqlsermap.get("gatherresult").toString());
					pst.setString(3, anliname);
					pst.setInt(4, anlitype);
					pst.setInt(5, moduleid);
					pst.setString(6, modulename);
					pst.setString(7, version);
					pst.setInt(8, 0);
					pst.addBatch();
				}
			}else{
				if(anlitype==dao){
					String gatherbaseinfo=Updatejson.getUpdate(sqlsermap.get("gatherbaseinfo").toString(),hiscode);//替换HISCODE
					pst.setString(1, gatherbaseinfo);
					pst.setString(2, sqlsermap.get("gatherresult").toString());
					pst.setString(3, anliname);
					pst.setInt(4, anlitype);
					pst.setInt(5, moduleid);
					pst.setString(6, modulename);
					pst.setString(7, version);
					pst.setInt(8, 0);
					pst.addBatch();
				}
			}
		}
		pst.executeBatch();
		//配合setAutoCommit(false)方法，批量执行SQL
		mysqlconn.commit();
		System.out.println("数据导入结束");
		
		//数据重复，去重处理
		if(dao==111){
			sql="select anlitype,moduleid,modulename,anliname,version,gatherbaseinfo,gatherresult from sa_gather_log where version=? and usertype <> 1 group by anliname,version ";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			rs=pst.executeQuery();
		}else if(dao==112){
			sql="select anlitype,moduleid,modulename,anliname,version,gatherbaseinfo,gatherresult from sa_gather_log where version=? and anliname like ? and usertype <> 1 group by anliname,version ";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			pst.setString(2, "%"+anliname1+"%");
			rs=pst.executeQuery();
		}else{
//			if(anlitype==dao){
				sql="select id, anlitype,moduleid,modulename,anliname,version,gatherbaseinfo,gatherresult "
						+ "from sa_gather_log where version=? and anlitype=? and usertype <> 1 group by anlitype,moduleid,"
						+ "modulename,anliname,version";
				pst=mysqlconn.prepareStatement(sql);
				pst.setString(1, version);
				pst.setInt(2, dao);
				rs=pst.executeQuery();
//			}
			
		}
		List sqlserlist1=mysql.getlist(rs);
		
		//导入前删除log表
		if(dao==111){
			sql="delete from sa_gather_log where version=? and usertype <> 1 ";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			pst.executeUpdate();
		}else if(dao==112){
			sql="delete from sa_gather_log where version=? and anliname like ? and usertype <> 1";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			pst.setString(2, "%"+anliname1+"%");
			pst.executeUpdate();
		}else{
//			if(anlitype==dao){
				sql="delete from sa_gather_log where version=? and anlitype=? and usertype <> 1";
				pst=mysqlconn.prepareStatement(sql);
				pst.setString(1, version);
				pst.setInt(2, dao);
				pst.executeUpdate();
//			}
		}
		
				
		mysqlconn.setAutoCommit(false);
		sql="insert into sa_gather_log(gatherbaseinfo,gatherresult,anliname,anlitype,moduleid,modulename,version,usertype) values(?,?,?,?,?,?,?,?)";
		pst=mysqlconn.prepareStatement(sql);
		for(int i=0;i<sqlserlist1.size();i++){
			Map sqlsermap=(Map)sqlserlist1.get(i);
			pst.setString(1, sqlsermap.get("gatherbaseinfo").toString());
			pst.setString(2, sqlsermap.get("gatherresult").toString());
			pst.setString(3, sqlsermap.get("anliname").toString() );
			pst.setInt(4,  Integer.parseInt(sqlsermap.get("anlitype").toString()));
			pst.setInt(5, Integer.parseInt(sqlsermap.get("moduleid").toString()));
			pst.setString(6,  sqlsermap.get("modulename").toString());
			pst.setString(7, version);
			pst.setInt(8, 0);
			pst.addBatch();
		}
		pst.executeBatch();
		//配合setAutoCommit(false)方法，批量执行SQL
		mysqlconn.commit();
		System.out.println("导入数据去重处理结束");
		
		st.close();
		rs.close();
		pst.close();
		sqlserconn.close();
		mysqlconn.close();
		System.out.println("sqlserver导入mysql结束");
		return "sqlserver导入mysql结束";
	}
}
