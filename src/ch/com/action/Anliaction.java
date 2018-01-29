package ch.com.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import ch.com.sys.Fenye;

import com.ch.jdbc.Mysqlconn;
import com.opensymphony.xwork2.inject.Container;

public class Anliaction {
	public int anlitype;
	public List list;
	public int zpage=0;//��ҳ��
	public int page=1;//��ǰҳ��
	public int sum=15;//ҳ����ʾ����
	public int res;
	public String anliname;
	public int count;//ҳ������
	public int paixu=0;
	public String version;//��ȡ��ݰ汾
	public List iplist;
	
	public List getIplist() {
		return iplist;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	public int getPaixu() {
		return paixu;
	}

	public void setPaixu(int paixu) {
		this.paixu = paixu;
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
		this.anliname = anliname;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getZpage() {
		return zpage;
	}
	
	public void setZpage(int zpage) {
		this.zpage = zpage;
	}
	
	public int getAnlitype() {
		return anlitype;
	}

	public void setAnlitype(int anlitype) {
		this.anlitype = anlitype;
	}
	
	public List getList() {
		return list;
	}

	/**
	 * ��ȡsa_gather_log��ݰ���
	 * ����JSPҳ�淭ҳ����
	 * @throws IOException 
	 * */
	//��ȡ��ݰ汾
//	public void Version() throws ClassNotFoundException, SQLException, IOException{
//		//����mysql��ݿ�
//		Mysqlconn mysql=new Mysqlconn();
//		Connection mysqlconn=mysql.getConn();
//		
//		String sql="select distinct version from sa_gather_log";
//		Statement st=mysqlconn.createStatement();
//		ResultSet rs=st.executeQuery(sql);
//		list=mysql.getlist(rs);
//		for(int i=0;i<list.size();i++){
//			Map map=(Map)list.get(i);
//			if("".equals(map.get("version")) || map.get("version")==null){
//				continue;
//			}
//			version=map.get("version").toString();
//		}
//		rs.close();
//		st.close();
//		mysqlconn.close();
//	}
	
	public void getShuju() throws ClassNotFoundException, SQLException, IOException{
		//����mysql��ݿ�
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		ResultSet rs;
		String sql;
		
		//��ʼҳ����ߵ�һ��������ݿ⣬��֤��Сҳ����ݣ����ҷ�ҳ���ܱ�����ǰҳ��
		if(page<=1){
			sql="select count(*) from sa_gather_log where anlitype=? and version=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setInt(1, anlitype);
			pst.setString(2, version);
			rs=pst.executeQuery();
			rs.next();
			count=Integer.parseInt(rs.getObject(1).toString());
			Fenye Fenye=new Fenye();
			zpage=Fenye.getZpage(count,sum);
			page=1;
		}
		if(zpage==0){
			return;
		}
		//��֤���ҳ�����
		if(page>zpage){
			page=page-1;
		}
//		sql="select a.*,b.state,b.json_err from sa_gather_log a left join anli_err b on a.id=b.id where a.anlitype=? and a.version=? order by a.id asc limit ?, ?";
		sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
				+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
				+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
				+ "left join log e on a.anliname=e.anliname and a.version=e.version "
				+ "where a.anlitype=? and a.version=? order by e.anlistatus asc,a.id asc limit ?, ?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, anlitype);
		pst.setString(2, version);
		pst.setInt(3, (page-1)*sum);
		pst.setInt(4, sum);
		rs=pst.executeQuery();
		list=mysql.getlist(rs);
		
		
		rs.close();
		pst.close();
		mysqlconn.close();
	}
	
	/**
	 * �����ѯ����
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * */
	public void getAnli() throws ClassNotFoundException, SQLException, IOException {
//		System.out.println("��ǰҳ�棺"+page);
//		System.out.println("��ҳ��"+zpage);
		//����mysql��ݿ�
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		ResultSet rs;
		String sql;
		
		//��ʼҳ����ߵ�һ��������ݿ⣬��֤��Сҳ����ݣ����ҷ�ҳ���ܱ�����ǰҳ��
//		System.out.println(anliname);
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
		//��֤���ҳ�����
		if(page>zpage){
			page=page-1;
		}
			
		if(paixu==0){
			sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
					+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
					+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
					+ "left join log e on a.anliname=e.anliname and a.version=e.version "
					+ "where a.anlitype=? and a.anliname like ? and a.version=? order by e.anlistatus asc,a.id asc limit ?, ?";
		}else{
			sql="select a.*,b.state,b.json_err,c.tablename,d.note,e.anlistatus from sa_gather_log a left join anli_err b on a.id=b.id "
					+ "left join (select distinct anliname,version,tablename from shoudong_redis) c on a.anliname=c.anliname and a.version=c.version "
					+ "left join notes d on a.anliname=d.anliname and a.version=d.version "
					+ "left join log e on a.anliname=e.anliname and a.version=e.version "
					+ "where a.anlitype=? and a.anliname like ? and a.version=? order by b.state desc , b.anliname asc limit ?, ?";
		}
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, anlitype);
		pst.setString(2, "%"+anliname+"%");
		pst.setString(3, version);
		pst.setInt(4, (page-1)*sum);
		pst.setInt(5, sum);
		rs=pst.executeQuery();
		list=mysql.getlist(rs);
		
		rs.close();
		pst.close();
		mysqlconn.close();
		
	}
	
	public String execute() throws ClassNotFoundException, SQLException, IOException{
		//��ȡ�����������ַ
		ServerIP ServerIP=new ServerIP();
		ServerIP.getshuju();
		iplist=ServerIP.getList();
		
//		Version();//��ȡ��ݰ汾
		//����ȫ�����
		if(res==1){
			System.out.println("��ȡȫ�����");
			getShuju();
		}
		//�����������
		if(res==2){
			System.out.println("������Ʋ�ѯ���߷�ҳ��"+anliname);
			getAnli();
		}
		return "success";
	}
}
