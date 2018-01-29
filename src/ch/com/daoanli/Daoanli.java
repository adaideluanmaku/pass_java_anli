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
 * ��sqlserver�������ݵ�mysql��
 * **/
public class Daoanli {
	public String anliname;
	public int anlitype=0;
	public int moduleid=0;
	public String modulename;
	
	//����JSON����
	public void Baseinfo(String json,String version) throws ClassNotFoundException, SQLException, IOException{
		JSONObject jsonin=JSONObject.fromObject(json);
		JSONObject Patient=jsonin.getJSONObject("Patient");
		anliname=Patient.getString("Name");
		
		//��ȡԭ�������ı����¼
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
	
	//sqlserver���������ݵ�mysql
	public String Sqlser_to_mysql_all(String version,int dao,String anliname1) throws ClassNotFoundException, SQLException, IOException{
		if(StringUtils.isBlank(version)){
			return "������汾��";
		}
		InputStream in=Mysqlconn.class.getClassLoader().getResourceAsStream("config.properties");
		Properties prop=new Properties();		
		prop.load(in);
		
		//����sqlserver���ݿ�
		Sqlserverconn sqlser=new Sqlserverconn();
		//����mysql���ݿ�
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
		
		//����ǰɾ��log��,ȫɾ����ɾ�����ְ���
		if(dao==111){
			sql="delete from sa_gather_log where version=? and usertype <> 1";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, version);
			pst.executeUpdate();
		}else if(dao==112){
			if("".equals(anliname1) || anliname1==null){
				return "��������Ϊ��";
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
		
		//ɾ����������,ȫɾ����ɾ�����ְ���
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
		
		
		// Ĭ�������ÿִ��һ��sql�ύһ�� ,�ر��Զ��ύ�������.commit()ʵ������ִ��sql
		
		String hiscode=prop.getProperty("hiscode");
		Updatejson Updatejson=new Updatejson();//������ʱ����HISCODEֵ
		mysqlconn.setAutoCommit(false);
		sql="insert into sa_gather_log(gatherbaseinfo,gatherresult,anliname,anlitype,moduleid,modulename,version,usertype) values(?,?,?,?,?,?,?,?)";
		pst=mysqlconn.prepareStatement(sql);
		
		for(int i=0;i<sqlserlist.size();i++){
			Map sqlsermap=(Map)sqlserlist.get(i);
			//gatherbaseinfo��������,�ֽ�����
			Baseinfo(sqlsermap.get("gatherbaseinfo").toString(),version);
			if(dao==111){
				String gatherbaseinfo=Updatejson.getUpdate(sqlsermap.get("gatherbaseinfo").toString(),hiscode);//�滻HISCODE
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
					String gatherbaseinfo=Updatejson.getUpdate(sqlsermap.get("gatherbaseinfo").toString(),hiscode);//�滻HISCODE
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
					String gatherbaseinfo=Updatejson.getUpdate(sqlsermap.get("gatherbaseinfo").toString(),hiscode);//�滻HISCODE
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
		//���setAutoCommit(false)����������ִ��SQL
		mysqlconn.commit();
		System.out.println("���ݵ������");
		
		//�����ظ���ȥ�ش���
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
		
		//����ǰɾ��log��
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
		//���setAutoCommit(false)����������ִ��SQL
		mysqlconn.commit();
		System.out.println("��������ȥ�ش������");
		
		st.close();
		rs.close();
		pst.close();
		sqlserconn.close();
		mysqlconn.close();
		System.out.println("sqlserver����mysql����");
		return "sqlserver����mysql����";
	}
}
