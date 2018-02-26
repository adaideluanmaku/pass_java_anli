package ch.com.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ch.jdbc.Mysqlconn;

public class ServerIP {
	public String redisip;
	public String redisport;
	public int redisnum;
	public String redispw;
	public String servername;
	public String address;
	public int res;
	public List list;
	public int id;
	public int iptype;
	
	public void setRedisport(String redisport) {
		this.redisport = redisport;
	}

	public void setRedisnum(int redisnum) {
		this.redisnum = redisnum;
	}

	public void setRedispw(String redispw) {
		this.redispw = redispw;
	}

	public void setIptype(int iptype) {
		this.iptype = iptype;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRedisip() {
		return redisip;
	}

	public void setRedisip(String redisip) {
		this.redisip = redisip.trim();
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.trim();
	}

	public void setRes(int res) {
		this.res = res;
	}

	public List getList() {
		return list;
	}

	public void addip() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		ResultSet rs;
		String sql;
		
		if("".equals(servername) || servername==null){
			return;
		}
		
		sql="select count(*) from serverip where servername=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, servername);
		rs=pst.executeQuery();
		rs.next();
		if(Integer.parseInt(rs.getObject(1).toString())>0){
			String ziduan="";
			if(!"".equals(redisip)){
				ziduan=ziduan+"redisip='"+redisip+"',";
			}
			if(!"".equals(address)){
				ziduan=ziduan+"address='"+address+"',";
			}
			if(!"".equals(redisport)){
				ziduan=ziduan+"redisport='"+redisport+"',";
			}
			if(redisnum!=999){
				ziduan=ziduan+"redisnum="+redisnum+",";
			}
			if(!"".equals(redispw)){
				ziduan=ziduan+"redispw='"+redispw+"',";
			}
			ziduan=ziduan.substring(0, ziduan.length()-1);
//			System.out.println("��������ƴ���ֶΣ�"+ziduan);
			sql="update serverip set "+ziduan+" where servername=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, servername);
			pst.executeUpdate();
		}else{
			sql="insert into serverip(redisip,servername,address,iptype,redisport,redisnum,redispw) values(?,?,?,?,?,?,?)";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, redisip);
			pst.setString(2, servername);
			pst.setString(3, address);
			pst.setInt(4, iptype);
			pst.setString(5, redisport);
			if(redisnum==999){
				pst.setInt(6, 0);
			}else{
				pst.setInt(6, redisnum);
			}
			pst.setString(7, redispw);
			pst.executeUpdate();
		}
		
		pst.close();
		mysqlconn.close();
	}
	
	public void getshuju() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select * from serverip order by iptype asc";
		st=mysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=mysql.getlist(rs);
		
		rs.close();
		st.close();
		mysqlconn.close();
	}
	
	public void deleteshuju() throws ClassNotFoundException, SQLException, IOException{
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="delete from serverip where id=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setInt(1, id);
		pst.executeUpdate();
		
		pst.close();
		mysqlconn.close();
	}
	public String execute() throws ClassNotFoundException, SQLException, IOException{
		if(res==1){
			addip();
		}
		if(res==2){
			deleteshuju();
		}
		getshuju();
		return "success";
	}
}
