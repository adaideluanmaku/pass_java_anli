package ch.com.action;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import ch.com.screen.Passscreen;
import ch.com.sys.Jiekoutype;
import ch.com.sys.Passservice;

public class Passwinaction {
	public int id;
	public int anlitype;
	public String anliname;
	public String gatherresult_java;
	public String gatherresult;
	public String gatherbaseinfo;
	public String note;
//	public int jiekoutype=0;
	public int req=0;
	public String winurl;
	public String url;
	public int test=0;
	public String servername1;
	public String servername2;
	public List iplist;
	public String err;
	public String version;
	public int anlistatus=0;
	
	public int getAnlistatus() {
		return anlistatus;
	}
	public void setAnlistatus(int anlistatus) {
		this.anlistatus = anlistatus;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getErr() {
		return err;
	}
	public List getIplist() {
		return iplist;
	}
	public String getServername1() {
		return servername1;
	}
	public void setServername1(String servername1) {
		this.servername1 = servername1;
	}
	public String getServername2() {
		return servername2;
	}
	public void setServername2(String servername2) {
		this.servername2 = servername2;
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
	public void setWinurl(String winurl) {
		if(!"".equals(winurl) && winurl!=null){
			this.winurl = winurl;
		}
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		if(!"".equals(url) && url!=null){
			this.url = url;
		}
	}
	public void setReq(int req) {
		this.req = req;
	}
	public int getAnlitype() {
		return anlitype;
	}
	public void setAnlitype(int anlitype) {
		this.anlitype = anlitype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnliname() {
		return anliname;
	}
	public void setAnliname(String anliname) {
		this.anliname = anliname;
	}
	public String getGatherresult_java() {
		return gatherresult_java;
	}
	public void setGatherresult_java(String gatherresult_java) {
		this.gatherresult_java = gatherresult_java;
	}
	public String getGatherresult() {
		return gatherresult;
	}
	public void setGatherresult(String gatherresult) {
		this.gatherresult = gatherresult;
	}
	public String getGatherbaseinfo() {
		return gatherbaseinfo;
	}
	public void setGatherbaseinfo(String gatherbaseinfo) {
		this.gatherbaseinfo = gatherbaseinfo;
	}
	
	public void Passwin() throws IOException, TimeoutException, ClassNotFoundException, SQLException{
		//passwin接口
		if(test==1){
			if(winurl==null || "".equals(winurl)){
				System.out.println("访问地址为空");
				gatherresult="访问地址为空";
				return;
			}
			Passservice Passservice=new Passservice(); 
			gatherresult=Passservice.getPassResult(gatherbaseinfo, winurl);
		}else  if(id>0){
			Jiekoutype Jiekoutype=new Jiekoutype();
//			winurl=Jiekoutype.getUrl(2, jiekoutype);
			String url1=Jiekoutype.getUrl(2, anlitype,servername1);
			if(url1==null){
				err="未匹配到服务器地址";
				return;
			}
			Passservice Passservice=new Passservice(); 
			gatherresult=Passservice.getPassResult(gatherbaseinfo, url1);
		}
	}
	
	public void Passjava() throws IOException, TimeoutException, ClassNotFoundException, SQLException{
		//passjava接口
		if(test==1){
			Passservice Passservice=new Passservice(); 
			gatherresult_java=Passservice.getPassResult(gatherbaseinfo, url);
		}else if(id>0){
//			System.out.println("123");
			Jiekoutype Jiekoutype=new Jiekoutype();
//			url=Jiekoutype.getUrl(1, jiekoutype);
			String url1=Jiekoutype.getUrl(1, anlitype,servername2);
			if(url1==null){
				err="未匹配到服务器地址";
				return;
			}
			Passservice Passservice=new Passservice(); 
			gatherresult_java=Passservice.getPassResult(gatherbaseinfo, url1);
		}
	}
	
	public String execute() throws IOException, TimeoutException, ClassNotFoundException, SQLException{
		//获取服务器请求地址
		ServerIP ServerIP=new ServerIP();
		ServerIP.getshuju();
		iplist=ServerIP.getList();
		
		//设置访问地址
//		if(anlitype==1 || anlitype==2){
//			System.out.println("调用win审查的接口地址");
//			jiekoutype=1;
//		}
//		if(anlitype==3 || anlitype==4 || anlitype==7){
//			System.out.println("调用win查询的接口地址");
//			jiekoutype=2;
//		}
//		if(anlitype==5){
//			System.out.println("调用win查询的接口地址");
//			jiekoutype=3;
//		}
//		if(anlitype==6){
//			System.out.println("调用win查询的接口地址");
//			jiekoutype=4;
//		}
//		if(anlitype==8){
//			System.out.println("调用win查询的接口地址");
//			jiekoutype=5;
//		}
//		System.out.println("aaa"+gatherbaseinfo);
		//处理案例
		if(req==1){
			System.out.println("PASSWIN案例审查");
			Passwin();
			System.out.println("PASSWIN审查结束");
		}
		if(req==2){
			System.out.println("PASSJAVA案例审查");
			Passjava();
			System.out.println("PASSJAVA审查结束");
		}
//		System.out.println("bbb"+gatherbaseinfo);
		return "success";
	}
}
