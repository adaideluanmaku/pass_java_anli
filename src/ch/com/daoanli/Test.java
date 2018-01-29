package ch.com.daoanli;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.dom4j.DocumentException;

import ch.com.action.Sa_gather_json;
import ch.com.sys.Fenye;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.Sqlserverconn;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, DocumentException {
		// TODO Auto-generated method stub
//		//����sqlserver���ݿ�
//		Sqlserverconn sqlser=new Sqlserverconn();
//		Connection sqlserconn=sqlser.getConn();
//		
//		//����mysql���ݿ�
//		Mysqlconn mysql=new Mysqlconn();
//		Connection mysqlconn=mysql.getConn();
		
		//��sqlserver�������뵽mysql��
//		Daoanli Daoanli=new Daoanli();
//		Daoanli.Sqlser_to_mysql();
//		int count=2;
//		int row = 2;
//		int basrow = count/row;
//		if(basrow*row<count){
//			basrow = basrow +1;
//		}
//		System.out.println(basrow);
//		for(int f = 0;f<basrow;f++){
//			Integer start = f*row;
//			Integer end = (f+1)*row;
//			if(end> count){
//				end = count;
//			}
//			System.out.println(start+":"+end);
//    	}
//		Sa_gather_json Sa_gather_json=new Sa_gather_json();
//		Sa_gather_json.setTest(1);
//		Sa_gather_json.setGatherresult_java("1");
//		Sa_gather_json.setGatherresult("1");
//		Sa_gather_json.getJsonerr1();
//		List list =Sa_gather_json.getJson_err();
//		System.out.println(list);
		
		String a="����sa_gather_log����";
		String autf=URLEncoder.encode(a,"GBK");
		System.out.println(autf);
		
	}
}
