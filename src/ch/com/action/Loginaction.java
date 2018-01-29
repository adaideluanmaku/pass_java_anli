package ch.com.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import ch.com.daoanli.Daoanli;
import ch.com.sys.Updatejson;

import com.ch.jdbc.Mysqlconn;

public class Loginaction {
	public List list;
	public int req=0;
	public String message;
	public String hiscode;
	public String version;
	public int dao;
	public String anliname;
	
	
	public String getVersion() {
		return version;
	}

	public void setAnliname(String anliname) {
		this.anliname = anliname;
	}

	public void setDao(int dao) {
		this.dao = dao;
	}

	public void setVersion(String version) {
		this.version = version.trim();
	}

	public void setHiscode(String hiscode) {
		this.hiscode = hiscode;
	}

	public String getMessage() {
		return message;
	}

	public void setReq(int req) {
		this.req = req;
	}

	public List getList() {
//		System.out.println(list);
		return list;
	}

	//获取案例类型生成列表
	public void anlifenlei() throws ClassNotFoundException, SQLException, IOException{
		//接连mysql数据库
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		
		String sql="select distinct anlitype from sa_gather_log";
		Statement st=mysqlconn.createStatement();
		ResultSet rs=st.executeQuery(sql);
		list=mysql.getlist(rs);
		rs.close();
		st.close();
		mysqlconn.close();
	}
	
	//备份LOG表
	public void logbak() throws ClassNotFoundException, SQLException, IOException{
		//接连mysql数据库
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
//		//验证页面选择的版本号和log表里面数据的版本号是否一致
//		sql="select count(version) from sa_gather_log where version=?";
//		pst=mysqlconn.prepareStatement(sql);
//		pst.setString(1, version);
//		rs=pst.executeQuery();
//		rs.next();
//		int sum=Integer.parseInt(rs.getObject(1).toString());
//		if(sum==0){
//			message="选择的版本号和数据库数据版本号不一致";
//			return;
//		}
		
		sql="select * from sa_gather_log where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		rs=pst.executeQuery();
		List sqlserlist=mysql.getlist(rs);
		if(sqlserlist.size()==0){
			message="备份时log表无数据";
			return;
		}
		
		//备份log表，备份前删除log_bak表
		sql="delete from sa_gather_log_bak where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		pst.executeUpdate();
		
		// 默认情况下每执行一条sql提交一次 ,关闭自动提交，可配合.commit()实现批量执行sql
		mysqlconn.setAutoCommit(false);
		sql="insert into sa_gather_log_bak(anlitype,moduleid,modulename,anliname,gatherbaseinfo,gatherresult,version,usertype) values(?,?,?,?,?,?,?,?)";
		pst=mysqlconn.prepareStatement(sql);
		for(int i=0;i<sqlserlist.size();i++){
			Map sqlsermap=(Map)sqlserlist.get(i);
			pst.setInt(1, Integer.parseInt(sqlsermap.get("anlitype").toString()));
			pst.setInt(2, Integer.parseInt(sqlsermap.get("moduleid").toString()));
			pst.setString(3, sqlsermap.get("modulename").toString());
			pst.setString(4, sqlsermap.get("anliname").toString());
			if(sqlsermap.get("gatherbaseinfo")==null || "".equals(sqlsermap.get("gatherbaseinfo"))){
				pst.setString(5,"");
			}else{
				pst.setString(5, sqlsermap.get("gatherbaseinfo").toString());
			}
			if(sqlsermap.get("gatherresult")==null || "".equals(sqlsermap.get("gatherresult"))){
				pst.setString(6,"");
			}else{
				pst.setString(6, sqlsermap.get("gatherresult").toString());
			}
			pst.setString(7, sqlsermap.get("version").toString());
			pst.setInt(8, Integer.parseInt(sqlsermap.get("usertype").toString()));
			pst.addBatch();
		}
		pst.executeBatch();
		//配合setAutoCommit(false)方法，批量执行SQL
		mysqlconn.commit();
		rs.close();
		pst.close();
		mysqlconn.close();
		message="log表备份完成";
	}
	
	//还原LOG表
	public void huanyuanlog() throws ClassNotFoundException, SQLException, IOException{
		//接连mysql数据库
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		String sql=null;
		
		sql="select * from sa_gather_log_bak where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		rs=pst.executeQuery();
		List sqlserlist=mysql.getlist(rs);
		if(sqlserlist.size()==0){
			message="备份表里面没有数据";
			return;
		}
		
		//还原log表，备份前删除log表
		sql="delete from sa_gather_log where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		pst.executeUpdate();
		
		//删除垃圾数据
		sql="delete from anli_err where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		pst.executeUpdate();
				
		// 默认情况下每执行一条sql提交一次 ,关闭自动提交，可配合.commit()实现批量执行sql
		mysqlconn.setAutoCommit(false);
		sql="insert into sa_gather_log(anlitype,moduleid,modulename,anliname,gatherbaseinfo,gatherresult,version,usertype) values(?,?,?,?,?,?,?,?)";
		pst=mysqlconn.prepareStatement(sql);
		for(int i=0;i<sqlserlist.size();i++){
			Map sqlsermap=(Map)sqlserlist.get(i);
			pst.setInt(1, Integer.parseInt(sqlsermap.get("anlitype").toString()));
			pst.setInt(2, Integer.parseInt(sqlsermap.get("moduleid").toString()));
			pst.setString(3, sqlsermap.get("modulename").toString());
			pst.setString(4, sqlsermap.get("anliname").toString());
			pst.setString(5, sqlsermap.get("gatherbaseinfo").toString());
			pst.setString(6, sqlsermap.get("gatherresult").toString());
			pst.setString(7, sqlsermap.get("version").toString());
			pst.setInt(8, Integer.parseInt(sqlsermap.get("usertype").toString()));
			pst.addBatch();
		}
		pst.executeBatch();
		//配合setAutoCommit(false)方法，批量执行SQL
		mysqlconn.commit();
		rs.close();
		pst.close();
		st.close();
		mysqlconn.close();
		message="log表还原完成";
	}
	
	public String execute() throws ClassNotFoundException, SQLException, IOException{
//		System.out.println(req);
		if("".equals(version) || version==null){
			return "success3";
		}
		
		if(req==1){
			if(dao==100){
				message="请选择导入的案例类型";
			}else{
				System.out.println("开始从源数据库导入案例到mysql数据库");
				Daoanli Daoanli=new Daoanli();
				message=Daoanli.Sqlser_to_mysql_all(version,dao,anliname);
				System.out.println("案例导入结束");
			}
			return "success2";
		}
		if(req==2 && !"".equals(hiscode)){
			System.out.println("更新HISCODE");
			Updatejson Updatejson=new Updatejson();
			Updatejson.getUpdatejson(hiscode,version);
			System.out.println("更新结束");
			message="HISCODE更新完成";
			return "success2";
		}
		if(req==3){
			System.out.println("备份log表");
			logbak();
			System.out.println("备份结束");
			return "success2";
		}
		if(req==4){
			System.out.println("还原log表");
			huanyuanlog();
			System.out.println("还原结束");
			return "success2";
		}
		System.out.println("开始查找案例类型");
//		anlifenlei();
		return "success1";
	}
}
