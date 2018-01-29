package com.ch.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PassMysqlconn {

	public Connection getConn() throws ClassNotFoundException, SQLException, IOException{
		InputStream in=PassMysqlconn.class.getClassLoader().getResourceAsStream("config.properties");
		Properties prop=new Properties();		
		prop.load(in);
		String url=prop.getProperty("passmysqlurl");
		String username=prop.getProperty("passmysqlname");
		String password=prop.getProperty("passmysqlpassword");
		String driver=prop.getProperty("passmysqldriver");
		
		Class.forName(driver);  
//		System.out.println("Á¬½Ó:"+url);
		return DriverManager.getConnection(url, username, password); 
	}
	
	public List getlist(ResultSet rs) throws SQLException{
		List list=new ArrayList();
//		if(rs!=null){
//			return list;
//		}
		ResultSetMetaData rsmd=rs.getMetaData();
		int len=rsmd.getColumnCount();
		while(rs.next()){
			Map map=new HashMap();
			for(int i=1;i<=len;i++){
				map.put(rsmd.getColumnName(i), rs.getObject(i));
			}
			list.add(map);
		}
		return list;
	}
}
