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

public class Mysqlconn {

	public Connection getConn() throws ClassNotFoundException, SQLException, IOException{
		InputStream in=Mysqlconn.class.getClassLoader().getResourceAsStream("config.properties");
		Properties prop=new Properties();		
		prop.load(in);
		String url=prop.getProperty("mysqlurl");
		String username=prop.getProperty("mysqlname");
		String password=prop.getProperty("mysqlpassword");
		String driver=prop.getProperty("mysqldriver");
		
		Class.forName(driver);  
//		System.out.println("Á¬½Ó:"+url);
		return DriverManager.getConnection(url, username, password); 
	}
	
	public List getlist(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		int len=rsmd.getColumnCount();
		List list=new ArrayList();
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
