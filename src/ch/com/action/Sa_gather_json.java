package ch.com.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.dom4j.DocumentException;

import net.sf.ehcache.search.expression.IsNull;
import net.sf.json.JSONObject;
import ch.com.screen.Detailresule;
import ch.com.screen.Moduleresule;
import ch.com.screen.Passscreen;
import ch.com.screen.Queryresule;
import ch.com.screen.Screenresule_1609;
import ch.com.screen.Screenresule_1712;
import ch.com.screen.Xmltojson;
import ch.com.sys.Jiekoutype;
import ch.com.sys.Modulename;
import ch.com.sys.Passservice;

import com.ch.jdbc.Mysqlconn;

public class Sa_gather_json {
	public int id;
	public int anlitype;
	public int moduleid;
	public String anliname;
	public List list=null;
	public String gatherbaseinfo;
	public String gatherresult; 
	public String gatherresult_java;
	public String note;
	public List json_err;
	public String status;
	public String version;
	public int res=0;
	public String message;
	public String message1;
	public String winurl;
	public String url;
	public int test=0;
	public List iplist;
	public int usertype;
	public int anlistatus=0;
	
	public int getAnlistatus() {
		return anlistatus;
	}

	public void setAnlistatus(int anlistatus) {
		this.anlistatus = anlistatus;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public List getIplist() {
		return iplist;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public String getWinurl() {
		return winurl;
	}

	public String getUrl() {
		return url;
	}

	public String getMessage() {
		return message;
	}
	public String getMessage1() {
		return message1;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List getJson_err() {
		return json_err;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public String getStatus() {
		return status;
	}
	
	public String getGatherresult_java() {
		return gatherresult_java;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGatherbaseinfo(String gatherbaseinfo) {
		this.gatherbaseinfo = gatherbaseinfo;
	}
	
	public void setGatherresult_java(String gatherresult_java) {
		this.gatherresult_java = gatherresult_java;
	}

	public String getGatherbaseinfo() {
		return gatherbaseinfo;
	}
	public String getGatherresult() {
		return gatherresult;
	}
	
	public void setGatherresult(String gatherresult) {
		this.gatherresult = gatherresult;
	}

	public List getList() {
		return list;
	}
	public int getAnlitype() {
		return anlitype;
	}
	public void setAnlitype(int anlitype) {
		this.anlitype = anlitype;
	}
	public int getModuleid() {
		return moduleid;
	}
	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
	}
	public String getAnliname() {
		return anliname;
	}
	public void setAnliname(String anliname) {
		this.anliname = anliname.trim();
	}
	
	/**
	 * sa_gather_log
	 * @throws IOException 
	 * */
	public void getGatherlog() throws ClassNotFoundException, SQLException, IOException{
		//获取数据
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		
		sql="select a.gatherbaseinfo,a.gatherresult,a.gatherresult_java,b.note,c.anlistatus from sa_gather_log a "
				+ "left join notes b on a.anliname=b.anliname and a.version=b.version "
				+ "left join log c on a.anliname=c.anliname and a.version=c.version "
				+ "where a.id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		rs=pst.executeQuery();
		list=mysql.getlist(rs);
		System.out.println(list);
		if(list.size()>1){
//			System.out.println("出现数据重复,请查看数据库");
			status="err";
		}
		for(int i=0;i<list.size();i++){
			Map result=(Map)list.get(i);
			if(!"".equals(result.get("gatherbaseinfo")) && result.get("gatherbaseinfo")!=null){
				gatherbaseinfo=result.get("gatherbaseinfo").toString();
			}
			if(!"".equals(result.get("gatherresult")) && result.get("gatherresult")!=null){
				gatherresult=result.get("gatherresult").toString();
			}
			if("".equals(result.get("gatherresult_java")) || result.get("gatherresult_java")==null){
				gatherresult_java="";
			}else{
				gatherresult_java=result.get("gatherresult_java").toString();
			}
			if(!"".equals(result.get("anlitype")) && result.get("anlitype")!=null){
				anlitype=Integer.parseInt(result.get("anlitype").toString());
			}
			if(!"".equals(result.get("note")) && result.get("note")!=null){
				note=result.get("note").toString();
			}
			if(!"".equals(result.get("anlistatus")) && result.get("anlistatus")!=null){
				anlistatus=Integer.parseInt(result.get("anlistatus").toString());
			}
		}
		rs.close();
		pst.close();
		mysqlconn.close();
		status="ok";
	}
	
	public void getJsonerr() throws ClassNotFoundException, SQLException, IOException, TimeoutException, DocumentException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		
		if("".equals(id)){
			if("".equals(url)){
				json_err=new ArrayList();
				json_err.add("id and url is null");
				rs.close();
				pst.close();
				mysqlconn.close();
				return;
			}
			Screenresule_1609 Screenresule=new Screenresule_1609();
			json_err=Screenresule.screenres(gatherresult,gatherresult_java);
			rs.close();
			pst.close();
			mysqlconn.close();
			return;
		}
		
		sql="select b.json_err from sa_gather_log a left join anli_err b on a.id=b.id where a.id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		rs=pst.executeQuery();
		list=mysql.getlist(rs);
		for(int i=0;i<list.size();i++){
			Map result=(Map)list.get(i);
			if("".equals(result.get("json_err")) || result.get("json_err")==null){
				json_err=null;
			}else{
				JSONObject obj=JSONObject.fromObject(result.get("json_err"));
				json_err = (List) obj.get("tojson");
			}
		}
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	public void getUpdatelog() throws ClassNotFoundException, SQLException, IOException{
//		System.out.println(gatherbaseinfo);
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		
		//anlitype
		sql="select * from  sa_gather_log where id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		rs=pst.executeQuery();
		list=mysql.getlist(rs);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			int n=Integer.parseInt(map.get("anlitype").toString());
			sql="select * from  log where anliname=? and version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname);
			pst.setString(2, version);
			rs=pst.executeQuery();
			List list1=mysql.getlist(rs);
			if(anlitype != n){
				if(list1.size()>0){
					sql="update log set anlitype=?,moduleid=?,modulename=? where anliname=? and version=?";
					pst=mysqlconn.prepareStatement(sql);
					pst.setInt(1, anlitype);
					pst.setInt(2, Integer.parseInt(map.get("moduleid").toString()));
					pst.setString(3, map.get("modulename").toString());
					pst.setString(4, anliname);
					pst.setString(5, version);
					pst.executeUpdate();
				}else{
					sql="insert into  log(anliname,anlitype,version,moduleid,modulename) values(?,?,?,?,?)";
					pst=mysqlconn.prepareStatement(sql);
					pst.setString(1, anliname);
					pst.setInt(2, anlitype);
					pst.setString(3, version);
					pst.setInt(4, Integer.parseInt(map.get("moduleid").toString()));
					pst.setString(5, map.get("modulename").toString());
					pst.executeUpdate();
				}
			}
			if(anlistatus>0){
				if(list1.size()>0){
					sql="update log set anlistatus=? where anliname=? and version=?";
					pst=mysqlconn.prepareStatement(sql);
					pst.setInt(1, anlistatus);
					pst.setString(2, anliname);
					pst.setString(3, version);
					pst.executeUpdate();
				}else{
					sql="insert into  log(anliname,anlistatus,version) values(?,?,?)";
					pst=mysqlconn.prepareStatement(sql);
					pst.setString(1, anliname);
					pst.setInt(2, anlistatus);
					pst.setString(3, version);
					pst.executeUpdate();
				}
				sql="delete from anli_err where id=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setInt(1, id);
				pst.executeUpdate();
			}else{
				if(list1.size()>0){
					for(int i1=0;i1<list1.size();i1++){
						Map map1=(Map)list1.get(i1);
						if( map1.get("anlitype") != null){
							sql="update log set anlistatus=? where anliname=? and version=?";
							pst=mysqlconn.prepareStatement(sql);
							pst.setInt(1, anlistatus);
							pst.setString(2, anliname);
							pst.setString(3, version);
							pst.executeUpdate();
						}else{
							sql="delete from log where anliname=? and version=?";
							pst=mysqlconn.prepareStatement(sql);
							pst.setString(1, anliname);
							pst.setString(2, version);
							pst.executeUpdate();
						}
					}
				}
			}
		}
		
		//sa_gather_log
		sql="update sa_gather_log set gatherresult=?,gatherbaseinfo=?,gatherresult_java=?,anlitype=? where id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, gatherresult);
		pst.setString(2, gatherbaseinfo);
		pst.setString(3, gatherresult_java);
		pst.setInt(4, anlitype);
		pst.setInt(5, id);
		pst.executeUpdate();
		
		//NOTE
		sql="select count(*) from  notes where anliname=? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		rs=pst.executeQuery();
		rs.next();
		if(Integer.parseInt(rs.getObject(1).toString())>0){
			if(!"".equals(note)){
				sql="update notes set note=? where anliname=? and version=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setString(1, note);
				pst.setString(2, anliname);
				pst.setString(3, version);
				pst.executeUpdate();
			}else{
				sql="delete from notes where anliname=? and version=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setString(1, anliname);
				pst.setString(2, version);
				pst.executeUpdate();
			}
			
		}else if(!"".equals(note)){
			sql="insert into  notes(anliname,note,version) values(?,?,?)";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname);
			pst.setString(2, note);
			pst.setString(3, version);
			pst.executeUpdate();
		}
		
		rs.close();
		pst.close();
		mysqlconn.close();
		
	}
	
	public void Insertjson() throws ClassNotFoundException, SQLException, IOException{
//		System.out.println(gatherresult);
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		ResultSet rs;
		String sql;
		
//		sql="select count(*) from sa_gather_log where anliname=? and version=? and anlitype=?";
		sql="select count(*) from sa_gather_log where anliname=? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		rs=pst.executeQuery();
		rs.next();
		if(Integer.parseInt(rs.getObject(1).toString())>0){
			id=0;
			message1="repeat anliname";
			rs.close();
			pst.close();
			mysqlconn.close();
			return;
		}
		Modulename Module=new Modulename();
		Module.getId(anliname);
//		int anlitype=Module.getAnlitype();
		int moduleid=Module.getModuleid();
		String modulename=Module.getModulename();
		
		sql="insert into sa_gather_log(anliname,anlitype,moduleid,modulename,version,usertype) values(?,?,?,?,?,?)";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setInt(2, anlitype);
		pst.setInt(3, moduleid);
		pst.setString(4, modulename);
		pst.setString(5, version);
		pst.setInt(6, usertype);
		pst.executeUpdate();
		
		sql="select id from sa_gather_log where anliname=? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		rs=pst.executeQuery();
		List list=mysql.getlist(rs);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			id=Integer.parseInt(map.get("id").toString());
		}
		message="INSERT anliname "+anliname;
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	public void getJsonerr1() throws DocumentException, ClassNotFoundException, SQLException, IOException{
		json_err=new ArrayList();
		if("".equals(gatherresult)){
			json_err.add("pass-win not null");
			return;
		}
		if("".equals(gatherresult_java)){
			json_err.add("pass-java not null");
			return;
		}
		
		if(gatherresult_java.equals(gatherresult)){
			json_err.add("OK");
			return;
		}
		if(test==1){
			if(url.contains("Mc_DoScreen")){
				anlitype=1;
			}
			if(url.contains("Mc_DoQuery")){
				anlitype=4;
			}
			if(url.contains("Mc_DoDetail")){
				anlitype=5;
			}
			if(url.contains("Mc_DoReason")){
				anlitype=6;
			}
			if(url.contains("Mc_DoModule")){
				anlitype=8;
			}
		}
		//json
		if("1712".equals(version)){
			Screenresule_1712 Screenresule=new Screenresule_1712();
			if(anlitype==1 || anlitype==2){
				json_err=Screenresule.screenres(gatherresult,gatherresult_java);
			}else if(anlitype==5){
				Detailresule Detailresule= new Detailresule();
				json_err=Detailresule.detail(gatherresult, gatherresult_java);
			}else if(anlitype==4 || anlitype==7){
				Queryresule Queryresule=new Queryresule();
				json_err=Queryresule.query(gatherresult, gatherresult_java);
			}else if(anlitype==8){
				Moduleresule Moduleresule=new Moduleresule();
				json_err=Moduleresule.module(gatherresult, gatherresult_java);
			}else {
				Xmltojson Xmltojson=new Xmltojson();
				if(gatherresult.contains("<?xml")){
					gatherresult=Xmltojson.getjson(gatherresult);
				}
				if(gatherresult_java.contains("<?xml")){
					gatherresult_java=Xmltojson.getjson(gatherresult_java);
				}
				if(!gatherresult_java.equals(gatherresult)){
					json_err.add("Assertion："+gatherresult);
					json_err.add("response："+gatherresult_java);
				}
			}
		}else if("1609".equals(version)){
			Screenresule_1609 Screenresule=new Screenresule_1609();
			if(anlitype==1 || anlitype==2){
				json_err=Screenresule.screenres(gatherresult,gatherresult_java);
			}else if(anlitype==5){
				Detailresule Detailresule= new Detailresule();
				json_err=Detailresule.detail(gatherresult, gatherresult_java);
			}else if(anlitype==4 || anlitype==7){
				Queryresule Queryresule=new Queryresule();
				json_err=Queryresule.query(gatherresult, gatherresult_java);
			}else if(anlitype==8){
				Moduleresule Moduleresule=new Moduleresule();
				json_err=Moduleresule.module(gatherresult, gatherresult_java);
			}else {
				Xmltojson Xmltojson=new Xmltojson();
				if(gatherresult.contains("<?xml")){
					gatherresult=Xmltojson.getjson(gatherresult);
				}
				if(gatherresult_java.contains("<?xml")){
					gatherresult_java=Xmltojson.getjson(gatherresult_java);
				}
				if(!gatherresult_java.equals(gatherresult)){
					json_err.add("Assertion："+gatherresult);
					json_err.add("response："+gatherresult_java);
				}
			}
		}
		
		
		//
		if(test==1){
			return;
		}
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		ResultSet rs;
		String sql;
		
		if(json_err.size()>0){
			//ListJSON
			JSONObject obj=new JSONObject();
			obj.element("tojson", json_err);
			
			sql="select count(*) from anli_err where id=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, id);
			rs=pst.executeQuery();
			rs.next();
			if(Integer.parseInt(rs.getObject(1).toString())>0){
				sql="update anli_err set json_err=? where id=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setObject(1, obj.toString());
				pst.setInt(2, id);
				pst.executeUpdate();
			}else{
				sql="insert into anli_err(id,json_err,state,anliname,anlitype,version) values(?,?,?,?,?,?)";
				pst=mysqlconn.prepareStatement(sql);
				pst.setInt(1, id);
				pst.setObject(2,obj.toString());
				pst.setInt(3,1);
				pst.setString(4,anliname);
				pst.setInt(5,anlitype);
				pst.setString(6,version);
				pst.executeUpdate();
			}
			rs.close();
		}else{
			sql="delete from anli_err where id=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.executeUpdate();
		}
		pst.close();
		mysqlconn.close();
	}
	
	public String execute() throws ClassNotFoundException, SQLException, IOException, TimeoutException, DocumentException{
		//
		ServerIP ServerIP=new ServerIP();
		ServerIP.getshuju();
		iplist=ServerIP.getList();
//		System.out.println(res);
//		if(res==1){
//			if(test==1){
//				getJsonerr1();
//			}else{
//				getJsonerr();//anli_err
//			}
//			return "success2";
//		}
		if(res==1){
			System.out.println("anli_err ID："+id);
			getJsonerr();//anli_err
			return "success2";
		}
		if(res==5){
			getJsonerr1();
			System.out.println("win-json"+gatherresult);
			System.out.println("java-json"+gatherresult_java);
			return "success2";
		}
		
		if(res==2){
			Jiekoutype Jiekoutype=new Jiekoutype();
//			url=Jiekoutype.getUrl(1, anlitype);
//			winurl=Jiekoutype.getUrl(2, anlitype);
//			System.out.println("获取sa_gather_log案例："+anliname);
			getGatherlog();//获取sa_gather_log案例
			return "success1";
		}
		
		if(res==3){
			System.out.println("获取sa_gather_log案例："+anliname);
			getUpdatelog();//sa_gather_log
			getGatherlog();//sa_gather_log
			message="json-ok";
			return "success1";
		}
		if(res==6){
			return "success1";
		}
		
		if(!"".equals(anliname) && res==4){
//			System.out.println("获取sa_gather_log案例："+anliname);
			Jiekoutype Jiekoutype=new Jiekoutype();
//			url=Jiekoutype.getUrl(1, anlitype);
//			winurl=Jiekoutype.getUrl(2, anlitype);
			Insertjson();//sa_gather_log
			getGatherlog();//sa_gather_log
		}
		
		return "success1";
		
	}
}
