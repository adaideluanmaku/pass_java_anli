package ch.com.screen;

import java.io.StringReader;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mysql.fabric.xmlrpc.base.Data;
import com.opensymphony.xwork2.inject.Container;

import freemarker.template.SimpleDate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


//pass审查返回的json串和java-pass返回的json串对比
public class Moduleresule {
	private String json=null;
	private String json1=null;
	private List listerr1;
	private int listerrsum=0;
	private int moduleidstr=-1;
	private int duibistate=1;
	private String ip;
	
	public int getDuibistate() {
		return duibistate;
	}

	public void setDuibistate(int duibistate) {
		this.duibistate = duibistate;
	}

	public int getModuleidstr() {
		return moduleidstr;
	}

	public void setModuleidstr(int moduleidstr) {
		this.moduleidstr = moduleidstr;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getJson1() {
		return json1;
	}

	public void setJson1(String json1) {
		this.json1 = json1;
	}

	public List getListerr1() {
		return listerr1;
	}

	public void setListerr1(List listerr) {
		this.listerr1 = listerr;
	}

	public int getListerrsum() {
		return listerrsum;
	}

	public void setListerrsum(int listerrsum) {
		this.listerrsum = listerrsum;
	}
	
	public List module(String jsonwin,String jsonjava) throws DocumentException{
		List listerr=new ArrayList();
		json=jsonwin;
		json1=jsonjava;
		
		Xmltojson Xmltojson=new Xmltojson();
		if(json.contains("<?xml")){
			json=Xmltojson.getjson(json);
		}
		if(json1.contains("<?xml")){
			json1=Xmltojson.getjson(json1);
		}
//		System.out.println(json);
//		System.out.println(json1);
		try{//防止错误的json导致异常
			JSONObject jsonin=JSONObject.fromObject(json);//正确的json
			JSONObject jsonin1=JSONObject.fromObject(json1);//需要验证的json
//			System.out.println(jsonin);
//			System.out.println(jsonin1);
			
			//对比HighestSlcode节点
			if(!jsonin.get("ProductCode").equals(jsonin1.get("ProductCode"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：ProductCode："+jsonin.get("ProductCode"));
				listerr.add("响应：一级目录错误的节点是：ProductCode："+jsonin1.get("ProductCode"));
//				return "success";
			}
			
			if(!jsonin.get("DataVersionReg").equals(jsonin1.get("DataVersionReg"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：DataVersionReg："+jsonin.get("DataVersionReg"));
				listerr.add("响应：一级目录错误的节点是：DataVersionReg："+jsonin1.get("DataVersionReg"));
//				return "success";
			}
			
			if(!jsonin.get("UserName").equals(jsonin1.get("UserName"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：DataVersionReg："+jsonin.get("DataVersionReg"));
				listerr.add("响应：一级目录错误的节点是：DataVersionReg："+jsonin1.get("DataVersionReg"));
//				return "success";
			}
			
			if(!jsonin.get("ProjectVersion").equals(jsonin1.get("ProjectVersion"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：ProjectVersion："+jsonin.get("ProjectVersion"));
				listerr.add("响应：一级目录错误的节点是：ProjectVersion："+jsonin1.get("ProjectVersion"));
//				return "success";
			}
			
			if(!jsonin.get("DataVersion").equals(jsonin1.get("DataVersion"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：DataVersion："+jsonin.get("DataVersion"));
				listerr.add("响应：一级目录错误的节点是：DataVersion："+jsonin1.get("DataVersion"));
//				return "success";
			}
				
			if(!jsonin.get("HasIM").equals(jsonin1.get("HasIM"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：HasIM："+jsonin.get("HasIM"));
				listerr.add("响应：一级目录错误的节点是：HasIM："+jsonin1.get("HasIM"));
//				return "success";
			}
			
			if(!jsonin.get("HasPASS").equals(jsonin1.get("HasPASS"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：HasPASS："+jsonin.get("HasPASS"));
				listerr.add("响应：一级目录错误的节点是：HasPASS："+jsonin1.get("HasPASS"));
//				return "success";
			}
			
			if(!jsonin.get("HasPharm").equals(jsonin1.get("HasPharm"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：HasPharm："+jsonin.get("HasPharm"));
				listerr.add("响应：一级目录错误的节点是：HasPharm："+jsonin1.get("HasPharm"));
//				return "success";
			}	
			
			if(!jsonin.get("HasRHC").equals(jsonin1.get("HasRHC"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：HasRHC："+jsonin.get("HasRHC"));
				listerr.add("响应：一级目录错误的节点是：HasRHC："+jsonin1.get("HasRHC"));
//				return "success";
			}
			
			if(!jsonin.get("RHCName").equals(jsonin1.get("RHCName"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：RHCName："+jsonin.get("RHCName"));
				listerr.add("响应：一级目录错误的节点是：RHCName："+jsonin1.get("RHCName"));
//				return "success";
			}
			
			if(!jsonin.get("RHC").equals(jsonin1.get("RHC"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：RHC："+jsonin.get("RHC"));
				listerr.add("响应：一级目录错误的节点是：RHC："+jsonin1.get("RHC"));
//				return "success";
			}
			
			if(!jsonin.get("CanGetScreenDtlRes").equals(jsonin1.get("CanGetScreenDtlRes"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：CanGetScreenDtlRes："+jsonin.get("CanGetScreenDtlRes"));
				listerr.add("响应：一级目录错误的节点是：CanGetScreenDtlRes："+jsonin1.get("CanGetScreenDtlRes"));
//				return "success";
			}
			
			if(!jsonin.get("ForCloud").equals(jsonin1.get("ForCloud"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：ForCloud："+jsonin.get("ForCloud"));
				listerr.add("响应：一级目录错误的节点是：ForCloud："+jsonin1.get("ForCloud"));
//				return "success";
			}
			
			
			if(!jsonin.get("ReceptClients").equals(jsonin1.get("ReceptClients"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：ReceptClients："+jsonin.get("ReceptClients"));
				listerr.add("响应：一级目录错误的节点是：ReceptClients："+jsonin1.get("ReceptClients"));
//				return "success";
			}
			
			//PASS-JAVA多一个节点
//			if(!jsonin.get("hiscode_invoke").equals(jsonin1.get("hiscode_invoke"))){
////				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
////				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
//				listerr.add("断言：一级目录正确的节点是：hiscode_invoke："+jsonin.get("hiscode_invoke"));
//				listerr.add("响应：一级目录错误的节点是：hiscode_invoke："+jsonin1.get("hiscode_invoke"));
////				return "success";
//			}
			
			if(!jsonin.get("mhiscode").equals(jsonin1.get("mhiscode"))){
//				System.out.println("断言：一级目录正确的节点是HighestSlcode："+jsonin.get("HighestSlcode"));
//				System.out.println("响应：一级目录错误的节点是HighestSlcode："+jsonin1.get("HighestSlcode"));
				listerr.add("断言：一级目录正确的节点是：mhiscode："+jsonin.get("mhiscode"));
				listerr.add("响应：一级目录错误的节点是：mhiscode："+jsonin1.get("mhiscode"));
//				return "success";
			}
			
			JSONArray ScreenModuleList=jsonin.getJSONArray("ScreenModuleList");
			JSONArray ScreenModuleList1=jsonin1.getJSONArray("ScreenModuleList");
			
			if(ScreenModuleList.size()==0 || ScreenModuleList1.size()==0){
				setListerr1(listerr);
				return  listerr1;
			}
			
			if(ScreenModuleList.size()!=0 && ScreenModuleList1.size()!=0){
				if(ScreenModuleList.size()!=ScreenModuleList1.size()){
					listerr.add("断言：ScreenModuleList的总数为："+ScreenModuleList.size());
					listerr.add("响应：ScreenModuleList的总数为："+ScreenModuleList1.size());
				}
				
				for(int i=0;i<ScreenModuleList.size();i++){
					int sum=0;
					String DetailResult=ScreenModuleList.getString(i);
					String DetailResult1;
					for(int i1=0;i1<ScreenModuleList1.size();i1++){
						DetailResult1=ScreenModuleList1.getString(i1);
						if(DetailResult.equals(DetailResult1)){
							ScreenModuleList.remove(i);
							ScreenModuleList1.remove(i1);
							i=i-1;
							break;
						}
					}
				}
				
				if(ScreenModuleList.size()>0 && ScreenModuleList1.size()==0){
					listerr.add("断言：ScreenModuleList："+ScreenModuleList);
					listerr.add("响应：ScreenModuleList缺少结果："+ScreenModuleList1);
				}
				if(ScreenModuleList.size()==0 && ScreenModuleList1.size()>0){
					listerr.add("断言：ScreenModuleList："+ScreenModuleList);
					listerr.add("响应：ScreenModuleList多余结果："+ScreenModuleList1);
				}
				if(ScreenModuleList.size()>0 && ScreenModuleList1.size()>0){
					for(int i=0;i<ScreenModuleList.size();i++){
						JSONObject ScreenModule=ScreenModuleList.getJSONObject(i);
						JSONObject ScreenModule1;
						for(int i1=0;i1<ScreenModuleList1.size();i1++){
							ScreenModule1=ScreenModuleList1.getJSONObject(i1);
							if(ScreenModule.get("ModuleID")==ScreenModule1.get("ModuleID")){
								listerr.add("断言：ScreenModuleList："+ScreenModule);
								listerr.add("响应：ScreenModuleList："+ScreenModule1);
							}
						}
					}
				}
			}
//			
			JSONArray ReferenceModuleList=jsonin.getJSONArray("ReferenceModuleList");
			JSONArray ReferenceModuleList1=jsonin1.getJSONArray("ReferenceModuleList");
			
			if(ReferenceModuleList.size()==0 || ReferenceModuleList1.size()==0){
				setListerr1(listerr);
				return  listerr1;
			}
			
			if(ReferenceModuleList.size()!=0 && ReferenceModuleList1.size()!=0){
				if(ReferenceModuleList.size()!=ReferenceModuleList1.size()){
					listerr.add("断言：ReferenceModuleList的总数为："+ReferenceModuleList.size());
					listerr.add("响应：ReferenceModuleList的总数为："+ReferenceModuleList1.size());
				}
				
				for(int i=0;i<ReferenceModuleList.size();i++){
					int sum=0;
					String DetailResult=ReferenceModuleList.getString(i);
					String DetailResult1;
					for(int i1=0;i1<ReferenceModuleList1.size();i1++){
						DetailResult1=ReferenceModuleList1.getString(i1);
						if(DetailResult.equals(DetailResult1)){
							ReferenceModuleList.remove(i);
							ReferenceModuleList1.remove(i1);
							i=i-1;
							break;
						}
					}
				}
				
				if(ReferenceModuleList.size()>0 && ReferenceModuleList1.size()==0){
					listerr.add("断言：ReferenceModuleList："+ReferenceModuleList);
					listerr.add("响应：ReferenceModuleList缺少结果："+ReferenceModuleList1);
				}
				if(ReferenceModuleList.size()==0 && ReferenceModuleList1.size()>0){
					listerr.add("断言：ReferenceModuleList："+ReferenceModuleList);
					listerr.add("响应：ReferenceModuleList多余结果："+ReferenceModuleList1);
				}
				if(ReferenceModuleList.size()>0 && ReferenceModuleList1.size()>0){
					listerr.add("断言：ReferenceModuleList："+ReferenceModuleList);
					listerr.add("响应：ReferenceModuleList："+ReferenceModuleList1);
				}
			}
			
			//如果有错误，错误状态改成1
			if(listerr.size()!=0){
				listerrsum=1;
			}
			
			//输出所有错误list
			setListerr1(listerr);
			Date data=new Date();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			System.out.println("IP:"+ip+"，"+sf.format(data));
			return listerr1;
		}catch(Exception ex){
			listerrsum=1;
			System.out.println("json审查节点名称可能存在问题");
			listerr.add("json审查节点名称可能存在问题");
			setListerr1(listerr);
			Date data=new Date();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			System.out.println("IP:"+ip+"，"+sf.format(data));
			return listerr1;
		}
	}
}
