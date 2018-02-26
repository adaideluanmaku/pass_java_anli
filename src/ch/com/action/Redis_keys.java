package ch.com.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.ch.jdbc.Mysqlconn;

public class Redis_keys {
	public String version;
	public String tablename;
	public String anliname;
	public String updatesql;
	public String selectsql;
	public String recsql;
	public String tablename1;
	public String anliname1;
	public String updatesql1;
	public String selectsql1;
	public String recsql1;
	public String message=null;
	public int res;
	
	public void setRes(int res) {
		this.res = res;
	}
	
	public String getTablename() {
		return tablename;
	}

	public String getAnliname() {
		return anliname;
	}
	public String getTablename1() {
		return tablename1;
	}

	public String getAnliname1() {
		return anliname1;
	}

	public String getUpdatesql1() {
		return updatesql1;
	}

	public String getSelectsql1() {
		return selectsql1;
	}

	public String getRecsql1() {
		return recsql1;
	}

	public void setRecsql(String recsql) {
		this.recsql = recsql;
	}

	public String getMessage() {
		return message;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename.trim();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version.trim();
	}

	public void setAnliname(String anliname) {
		this.anliname = anliname.trim();
	}

	public void setUpdatesql(String updatesql) {
		this.updatesql = updatesql;
	}

	public void setSelectsql(String selectsql) {
		this.selectsql = selectsql;
	}

	public void addshoudong_redis() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		String sql;
		ResultSet rs;
		
		sql="select * from shoudong_redis where anliname=? and version=? and tablename=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		pst.setString(3, tablename);
		rs=pst.executeQuery();
		List shoudonglist=mysql.getlist(rs);
		if(shoudonglist.size()>0){
			sql="update shoudong_redis set updatesql=?,selectsql=?,recsql=? where tablename=? and anliname=? and version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, updatesql);
			pst.setString(2, selectsql);
			pst.setString(3, recsql);
			pst.setString(4, tablename);
			pst.setString(5, anliname);
			pst.setString(6, version);
			int sum=pst.executeUpdate();
			if(sum>0){
				message="UPDATE "+anliname;
			}
		}else{
			sql="insert into shoudong_redis(updatesql,selectsql,tablename,anliname,version,recsql) values(?,?,?,?,?,?)";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, updatesql);
			pst.setString(2, selectsql);
			pst.setString(3, tablename);
			pst.setString(4, anliname);
			pst.setString(5, version);
			pst.setString(6, recsql);
			int sum=pst.executeUpdate();
			if(sum>0){
				message="INSERT "+anliname;
			}
		}
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	public void shoudongchaxun() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		String sql;
		ResultSet rs;
		
		sql="select * from shoudong_redis where anliname like ? and version=? and tablename =?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, "%"+anliname+"%");
		pst.setString(2, version);
		pst.setString(3, tablename);
		rs=pst.executeQuery();
		List shoudonglist=mysql.getlist(rs);
		if(shoudonglist.size()>0){
			for(int i=0;i<shoudonglist.size();i++){
				Map map=(Map)shoudonglist.get(i);
				anliname1=map.get("anliname").toString();
				tablename1=map.get("tablename").toString();
				selectsql1=map.get("selectsql").toString();
				updatesql1=map.get("updatesql").toString();
				recsql1=map.get("recsql").toString();
			}
		}else{
			message="NOT FIND anliname="+anliname+",tablename="+tablename;
		}
		
		
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	public void shoudongdelete() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		String sql;
		ResultSet rs;
		
		sql="delete from shoudong_redis where anliname=? and version=? and tablename=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		pst.setString(3, tablename);
		int a=pst.executeUpdate();
		if(a>0){
			message="DELETE anliname="+anliname+",tablename="+tablename;
		}else{
			message="NOT FIND anliname="+anliname+",tablename="+tablename;
		}
		
		pst.close();
		mysqlconn.close();
	}
	
	public String execute() throws ClassNotFoundException, SQLException, IOException{
		if(res==1){
			if((!"".equals(anliname) && anliname!=null)&&!"".equals(version)){
				addshoudong_redis();
			}else{
				message="anliname is null or version is null";
			}
//			if(message!=null){
////				message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
//				//struts2�ض���ֵ��ҳ���޷�ֱ��ʶ�����ģ�������ת��url����
//				message=URLEncoder.encode(message, "UTF-8");
//				System.out.println(message);
//			}
		}
		if(res==2){
			shoudongchaxun();
		}
		
		if(res==3){
			if(!"".equals(version) && !"".equals(anliname) && !"".equals(tablename)){
				shoudongdelete();
			}else{
				message="version is null or anliname is null or tablename is null";
			}
		}
		message=URLEncoder.encode(message, "UTF-8");
		return "success";
	}
}
