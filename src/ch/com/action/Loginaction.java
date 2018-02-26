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

	//
	public void anlifenlei() throws ClassNotFoundException, SQLException, IOException{
		//mysql
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
	
	//LOG
	public void logbak() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
//		//��֤ҳ��ѡ��İ汾�ź�log���������ݵİ汾���Ƿ�һ��
//		sql="select count(version) from sa_gather_log where version=?";
//		pst=mysqlconn.prepareStatement(sql);
//		pst.setString(1, version);
//		rs=pst.executeQuery();
//		rs.next();
//		int sum=Integer.parseInt(rs.getObject(1).toString());
//		if(sum==0){
//			message="ѡ��İ汾�ź����ݿ����ݰ汾�Ų�һ��";
//			return;
//		}
		
		sql="select * from sa_gather_log where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		rs=pst.executeQuery();
		List sqlserlist=mysql.getlist(rs);
		if(sqlserlist.size()==0){
			message="not find "+version;
			return;
		}
		
		//log_bak
		sql="delete from sa_gather_log_bak where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		pst.executeUpdate();
		
		//sql
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
		//setAutoCommit(false)
		mysqlconn.commit();
		rs.close();
		pst.close();
		mysqlconn.close();
		message="Finish the log bak";
	}
	
	//LOG
	public void huanyuanlog() throws ClassNotFoundException, SQLException, IOException{
		//mysql
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
			message="���ݱ�����û������";
			return;
		}
		
		//log
		sql="delete from sa_gather_log where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		pst.executeUpdate();
		
		//
		sql="delete from anli_err where version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, version);
		pst.executeUpdate();
				
		//
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
		//setAutoCommit(false)
		mysqlconn.commit();
		rs.close();
		pst.close();
		st.close();
		mysqlconn.close();
		message="Restore log completion";
	}
	
	public String execute() throws ClassNotFoundException, SQLException, IOException{
//		System.out.println(req);
		if("".equals(version) || version==null){
			return "success3";
		}
		
		if(req==1){
			if(dao==100){
				message="not Import";
			}else{
				Daoanli Daoanli=new Daoanli();
				message=Daoanli.Sqlser_to_mysql_all(version,dao,anliname);
			}
			return "success2";
		}
		if(req==2 && !"".equals(hiscode)){
			Updatejson Updatejson=new Updatejson();
			Updatejson.getUpdatejson(hiscode,version);
			message="UPDATE HISCODE";
			return "success2";
		}
		if(req==3){
			logbak();
			return "success2";
		}
		if(req==4){
			huanyuanlog();
			return "success2";
		}
//		anlifenlei();
		return "success1";
	}
}
