package ch.com.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.pentaho.di.trans.steps.append.Append;

import ch.com.screen.Passscreen;
import ch.com.sys.Autoredis;
import ch.com.sys.Fenye;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.Sqlserverconn;

public class Json_err {
	public int zpage=0;//
	public int page=1;//
	public int sum=15;//
	public int id;
	public int anlitype;
	public String anliname;
	public List list;
	public int count;//
	public int errcount;
	public int req=0;
	public int paixu=0;
	public int messeage=0;
//	public int jiekoutype=0;
	public String version;
	public String redisanliname;
	public String servername;
	public List iplist;
	public String anliname1;
	public int anlistatus=0;
	
	
	public void setAnlistatus(int anlistatus) {
		this.anlistatus = anlistatus;
	}
	public int getErrcount() {
		return errcount;
	}
	public String getAnliname1() {
		return anliname1;
	}
	public List getIplist() {
		return iplist;
	}
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
	public void setRedisanliname(String redisanliname) {
		this.redisanliname = redisanliname;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getMesseage() {
		return messeage;
	}
	public int getPaixu() {
		return paixu;
	}
	public void setPaixu(int paixu) {
		this.paixu = paixu;
	}
	public void setReq(int req) {
		this.req = req;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public String getAnliname() {
		return anliname;
	}
	public void setAnliname(String anliname) {
		this.anliname = anliname.trim();
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
	public int getZpage() {
		return zpage;
	}
	public void setZpage(int zpage) {
		this.zpage = zpage;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	//
	public void getShujubaoliu() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		
		//
		if(page<=1){
			sql="select count(*) from sa_gather_log where anlitype=? and anliname like ? and version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, anlitype);
			pst.setString(2, "%"+anliname+"%");
			pst.setString(3, version);
			rs=pst.executeQuery();
			rs.next();
			Fenye Fenye=new Fenye();
			count=Integer.parseInt(rs.getObject(1).toString());
			zpage=Fenye.getZpage(count,sum);
			page=1;
		}
		if(zpage==0){
			return;
		}
		//
		if(page>zpage){
			page=page-1;
		}
		if("".equals(anliname)){
			if(paixu==0){
				sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
						+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
						+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
						+ "left join log e on a.anliname=e.anliname and a.version=e.version "
						+ "where a.anlitype=? and a.version=? order by a.id asc limit ?, ?";
			}else{
				sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
						+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
						+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
						+ "left join log e on a.anliname=e.anliname and a.version=e.version "
						+ "where a.anlitype=? and a.version=? order by b.state desc , b.anliname asc limit ?, ?";
			}
//			sql="select a.*,b.state,b.json_err from sa_gather_log a left join anli_err b on a.id=b.id where anlitype=? order by a.id asc limit ?, ?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, anlitype);
			pst.setString(2, version);
			pst.setInt(3, (page-1)*sum);
			pst.setInt(4, sum);
			rs=pst.executeQuery();
			list=mysql.getlist(rs);
		}else{
			if(paixu==0){
				sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
						+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
						+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
						+ "left join log e on a.anliname=e.anliname and a.version=e.version "
						+ "where a.anlitype=? and a.anliname like ? and a.version=? order by a.id asc limit ?, ?";
			}else{
				sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
						+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
						+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
						+ "left join log e on a.anliname=e.anliname and a.version=e.version "
						+ "where a.anlitype=? and a.anliname like ? and a.version=? order by b.state desc , b.anliname asc limit ?, ?";
			}
//			sql="select a.*,b.state,b.json_err from sa_gather_log a left join anli_err b on a.id=b.id where anlitype=? and anliname like ? order by a.id asc limit ?, ?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, anlitype);
			pst.setString(2, "%"+anliname+"%");
			pst.setString(3, version);
			pst.setInt(4, (page-1)*sum);
			pst.setInt(5, sum);
			rs=pst.executeQuery();
			list=mysql.getlist(rs);
		}
//		if(paixu==0){
//			sql="select a.*,b.state,b.json_err from sa_gather_log a left join anli_err b on a.id=b.id where anlitype=?  order by a.id asc limit ?, ?";
//		}else{
//			sql="select a.*,b.state,b.json_err from sa_gather_log a left join anli_err b on a.id=b.id where anlitype=? order by b.json_err desc limit ?, ?";
//		}
//		pst=mysqlconn.prepareStatement(sql);
//		pst.setInt(1, anlitype);
//		pst.setInt(2, (page-1)*sum);
//		pst.setInt(3, sum);
//		rs=pst.executeQuery();
//		list=mysql.getlist(rs);
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	//
	public void Jsonscreen() throws ClassNotFoundException, SQLException, TimeoutException, DocumentException, IOException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		
		if(anlistatus==1){
			sql="delete from anli_err where id=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.executeUpdate();
			
			pst.close();
			mysqlconn.close();
			return;
		}
		//
//		System.out.println(id);
		sql="select id,gatherbaseinfo,gatherresult,anliname,anlitype,moduleid,modulename from sa_gather_log where id=? and  anlitype=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		pst.setInt(2, anlitype);
		rs=pst.executeQuery();
		List jsonlist=mysql.getlist(rs);
		String gatherbaseinfo = null;
		String gatherresult=null;
		for(int i=0;i<jsonlist.size();i++){
			Map map=(Map)jsonlist.get(i);
			gatherbaseinfo=map.get("gatherbaseinfo").toString();
			gatherresult=map.get("gatherresult").toString();
			anliname1=map.get("anliname").toString();//shoudong
			id=Integer.parseInt(map.get("id").toString());//sa_gather_log
			

//			if(anlitype==2){//redis
//				Autoredis Autoredis=new Autoredis();
//				Autoredis.oneupdate(anliname1,map.get("version").toString(),servername);
//			}
			
//			//JAVA
			Passscreen Passscreen=new Passscreen();
//			Passscreen.getResult(gatherbaseinfo, gatherresult,jiekoutype);
			Passscreen.getResult(gatherbaseinfo, gatherresult,anlitype,servername);
			
			List jsonerr=Passscreen.getList();
			
			//
			String jsonback=Passscreen.getJsonback();
			sql="update sa_gather_log set gatherresult_java=? where id=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, jsonback);
			pst.setInt(2, id);
			pst.executeUpdate();
			
			if(jsonerr.size()>0){
				//ListJSON
				JSONObject obj=new JSONObject();
				obj.element("tojson", jsonerr);
				
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
					pst.setString(4,anliname1);
					pst.setInt(5,anlitype);
					pst.setString(6,version);
					pst.executeUpdate();
				}
			}else{
				sql="delete from anli_err where id=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setInt(1, id);
				pst.executeUpdate();
			}
		}
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	//
	public void Jsonscreenall() throws ClassNotFoundException, SQLException, TimeoutException, DocumentException, IOException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		String sql=null;
		
		
		//
//		System.out.println(anlitype);
		if(StringUtils.isBlank(anliname)){
			sql="select a.id,a.gatherbaseinfo,a.gatherresult,a.anliname,a.anlitype,a.moduleid,a.modulename,a.version,b.anlistatus from sa_gather_log a "
				+"left join log b on a.anliname=b.anliname and a.version=b.version  "
				+"where a.anlitype=? and a.version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, anlitype);
			pst.setString(2, version);
		}else{
			sql="select a.id,a.gatherbaseinfo,a.gatherresult,a.anliname,a.anlitype,a.moduleid,a.modulename,a.version,b.anlistatus from sa_gather_log a "
					+"left join log b on a.anliname=b.anliname and a.version=b.version "
					+"where a.anlitype=? and a.version=? and a.anliname like ?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, anlitype);
			pst.setString(2, version);
			pst.setString(3, "%"+anliname+"%");
		}
		rs=pst.executeQuery();
		List jsonlist=mysql.getlist(rs);
		String gatherbaseinfo = null;
		String gatherresult=null;
		for(int i=0;i<jsonlist.size();i++){
			Map map=(Map)jsonlist.get(i);
			if(map.get("anlistatus")!=null){
				if(Integer.parseInt(map.get("anlistatus").toString())==1){
					sql="delete from anli_err where id=?";
					pst=mysqlconn.prepareStatement(sql);
					pst.setInt(1, Integer.parseInt(map.get("id").toString()));
					pst.executeUpdate();
					continue;
				}
			}
			
			System.out.println(map.get("anliname"));
			gatherbaseinfo=map.get("gatherbaseinfo").toString();
			gatherresult=map.get("gatherresult").toString();
			id=Integer.parseInt(map.get("id").toString());//sa_gather_log
			anliname1=map.get("anliname").toString();//anli_err
//			String anliname1=map.get("anliname").toString();//�ֶ����Բ���shoudong������ʹ��
			
//			if(anlitype==2){//redis
//				Autoredis Autoredis=new Autoredis();
//				Autoredis.oneupdate(anliname1,map.get("version").toString(),servername);
//				
//			}
			
			//
			Passscreen Passscreen=new Passscreen();
//			Passscreen.getResult(gatherbaseinfo, gatherresult,jiekoutype);
			Passscreen.getResult(gatherbaseinfo, gatherresult,anlitype,servername);
			List jsonerr=Passscreen.getList();
			
			//
			String jsonback=Passscreen.getJsonback();
			sql="update sa_gather_log set gatherresult_java=? where id=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, jsonback);
			pst.setInt(2, id);
			pst.executeUpdate();
			
			if(jsonerr.size()>0){
				//ListJSON
				JSONObject obj=new JSONObject();
				obj.element("tojson", jsonerr);
				sql="select count(*) from anli_err where id=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setInt(1, id);
				rs=pst.executeQuery();
				rs.next();
				if(Integer.parseInt(rs.getObject(1).toString())>0){
					sql="update anli_err set json_err=?, anliname=? where id=?";
					pst=mysqlconn.prepareStatement(sql);
					pst.setObject(1, obj.toString());
					pst.setString(2,anliname1);
					pst.setInt(3, id);
					pst.executeUpdate();
				}else{
					sql="insert into anli_err(id,json_err,state,anliname,anlitype,version) values(?,?,?,?,?,?)";
					pst=mysqlconn.prepareStatement(sql);
					pst.setInt(1, id);
					pst.setObject(2,obj.toString());
					pst.setInt(3,1);
					pst.setString(4,anliname1);
					pst.setInt(5,anlitype);
					pst.setString(6,version);
					pst.executeUpdate();
				}
			}else{
				sql="delete from anli_err where id=?";
				pst=mysqlconn.prepareStatement(sql);
				pst.setInt(1, id);
				pst.executeUpdate();
			}
		}
		
		//
		sql="delete from anli_err where id not in(select id from sa_gather_log)";
		st=mysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//
		sql="select count(*) from anli_err where anliname like ? and version=? and anlitype=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, "%"+anliname+"%");
		pst.setString(2, version);
		pst.setInt(3, anlitype);
		rs=pst.executeQuery();
		rs.next();
		errcount=Integer.parseInt(rs.getObject(1).toString());
		
		
		st.close();
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	public void deletejson() throws ClassNotFoundException, SQLException, IOException{
		//����mysql���ݿ�
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		ResultSet rs;
		String sql;
		
		sql="delete from sa_gather_log where id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		pst.executeUpdate();
		
		sql="delete from anli_err where id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		pst.executeUpdate();
		
		sql="delete from notes where anliname=? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname1);
		pst.setString(2, version);
		pst.executeUpdate();
		
		sql="delete from log where anliname=? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname1);
		pst.setString(2, version);
		pst.executeUpdate();
		
		sql="select count(*) from sa_gather_log where anlitype=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, anlitype);
		rs=pst.executeQuery();
		rs.next();
		count=Integer.parseInt(rs.getObject(1).toString());
		
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	public String execute() throws ClassNotFoundException, SQLException, TimeoutException, DocumentException, IOException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		//
		ServerIP ServerIP=new ServerIP();
		ServerIP.getshuju();
		iplist=ServerIP.getList();
//		System.out.println("�������ͣ�"+req);
//		if(anlitype==1 || anlitype==2){
//			System.out.println("�������Ľӿڵ�ַ");
//			jiekoutype=1;
//		}
//		if(anlitype==3 || anlitype==4){
//			System.out.println("���ò�ѯ�Ľӿڵ�ַ");
//			jiekoutype=2;
//		}
		
		if(req==1){
			System.out.println("screenone");
			Jsonscreen();
			messeage=2;
		}
		if(req==2){
			System.out.println("screenall");
			Jsonscreenall();
			messeage=1;
		}
		if(req==3){
			System.out.println("delete:"+anliname);
			//安全起见后期会屏蔽这个功能
//			deletejson();
		}
		
		//redis
//		if(req==4){
//			Autoredis Autoredis=new Autoredis();
//			Autoredis.oneupdate(redisanliname,version);
//		}
		getShujubaoliu();
		return "success";
	}
}
