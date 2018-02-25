package ch.com.screen;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.dom4j.DocumentException;

import ch.com.alterhiscode.AlterHiscode;
import ch.com.sys.Jiekoutype;
import ch.com.sys.Passservice;
import net.sf.json.JSONObject;

import com.ch.jdbc.Mysqlconn;

public class Passscreen {
	public List list;
	public String jsonback;
	
	public List getList() {
		return list;
	}

	public String getJsonback() {
		return jsonback;
	}

	public List getResult(String gatherbaseinfo,String gatherresult,int anlitype,String servername) throws TimeoutException, DocumentException, IOException, ClassNotFoundException, SQLException{
		AlterHiscode alterHiscode = new AlterHiscode();
		gatherbaseinfo=alterHiscode.alterhis(gatherbaseinfo);
		
		String url=null;
//		//passjava�ӿ�
		
		Jiekoutype Jiekoutype=new Jiekoutype();
		url=Jiekoutype.getUrl(1, anlitype,servername);
		list=new ArrayList();
		if(url==null){
			list.add("δ�ҵ����ʵ�����ַ");
			return list;
		}
		//�������˷��ؽ��json
		Passservice Passservice=new Passservice();
		jsonback=Passservice.getPassResult(gatherbaseinfo, url);
		
		if(jsonback==null){
			list.add("pass-java���ؽ��Ϊ��");
			return list;
		}
		if("".equals(gatherresult)){
			list.add("pass-win��������Ϊ��");
			return list;
		}
//		if("".equals(gatherresult) && jsonback.length()>0){
//			list.add("json������Ϊ�գ���Ϊȷ��java�永���Ƿ�ȱ�ٽ��");
//			return list;
//		}
//		
		//�����json�����ݿ����Ա�
		if(anlitype==1 || anlitype==2){
			Screenresule_1609 Screenresule=new Screenresule_1609();
			list=Screenresule.screenres(gatherresult,jsonback);
		}else if(anlitype==5){
			Detailresule Detailresule= new Detailresule();
			list=Detailresule.detail(gatherresult, jsonback);
		}else if(anlitype==4 || anlitype==7){
			Queryresule Queryresule=new Queryresule();
			list=Queryresule.query(gatherresult, jsonback);
		}else if(anlitype==8){
			Moduleresule Moduleresule=new Moduleresule();
			list=Moduleresule.module(gatherresult, jsonback);
		}else {
			Xmltojson Xmltojson=new Xmltojson();
			if(gatherresult.contains("<?xml")){
				gatherresult=Xmltojson.getjson(gatherresult);
			}
			if(jsonback.contains("<?xml")){
				jsonback=Xmltojson.getjson(jsonback);
			}
			
			if(!jsonback.equals(gatherresult)){
				list.add("���ԣ�"+gatherresult);
				list.add("��Ӧ��"+jsonback);
			}
		}
		return list;
	}
}
