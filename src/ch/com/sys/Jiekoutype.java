package ch.com.sys;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ch.jdbc.Mysqlconn;

import ch.com.screen.Passscreen;

public class Jiekoutype {
	public String url;
	public String getUrl(int javaorwin, int type,String servername) throws IOException, ClassNotFoundException, SQLException{
//		InputStream in=Passscreen.class.getClassLoader().getResourceAsStream("config.properties");
//		Properties prop=new Properties();
//		prop.load(in);
		//mysql
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		if(javaorwin==1){
			sql="select * from serverip where servername=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, servername);
			rs=pst.executeQuery();
			List list=mysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				//passjava�ӿ�
				if(type==1 || type==2){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoScreen";
				}
				if(type==3 || type==4 || type==7){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoQuery";
				}
				if(type==5){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoDetail";
				}
				if(type==6){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoReason";
				}
				if(type==8){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoModule";
				}
			}
			rs.close();
			pst.close();
		}
		if(javaorwin==2){
			sql="select * from serverip where servername=?";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, servername);
			rs=pst.executeQuery();
			List list=mysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				//passwin�ӿ�
				if(type==1 || type==2){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoScreen";
				}
				if(type==3 || type==4 || type==7){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoQuery";
				}
				if(type==5){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoDetail";
				}
				if(type==6){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoReason";
				}
				if(type==8){
					url=map.get("address")+"/PASSwebService.asmx/Mc_DoModule";
				}
			}
			rs.close();
			pst.close();
		}
		mysqlconn.close();
		return url;
	}
}
