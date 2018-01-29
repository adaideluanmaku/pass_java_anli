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


//pass��鷵�ص�json����java-pass���ص�json���Ա�
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
		try{//��ֹ�����json�����쳣
			JSONObject jsonin=JSONObject.fromObject(json);//��ȷ��json
			JSONObject jsonin1=JSONObject.fromObject(json1);//��Ҫ��֤��json
//			System.out.println(jsonin);
//			System.out.println(jsonin1);
			
			//�Ա�HighestSlcode�ڵ�
			if(!jsonin.get("ProductCode").equals(jsonin1.get("ProductCode"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�ProductCode��"+jsonin.get("ProductCode"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�ProductCode��"+jsonin1.get("ProductCode"));
//				return "success";
			}
			
			if(!jsonin.get("DataVersionReg").equals(jsonin1.get("DataVersionReg"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�DataVersionReg��"+jsonin.get("DataVersionReg"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�DataVersionReg��"+jsonin1.get("DataVersionReg"));
//				return "success";
			}
			
			if(!jsonin.get("UserName").equals(jsonin1.get("UserName"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�DataVersionReg��"+jsonin.get("DataVersionReg"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�DataVersionReg��"+jsonin1.get("DataVersionReg"));
//				return "success";
			}
			
			if(!jsonin.get("ProjectVersion").equals(jsonin1.get("ProjectVersion"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�ProjectVersion��"+jsonin.get("ProjectVersion"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�ProjectVersion��"+jsonin1.get("ProjectVersion"));
//				return "success";
			}
			
			if(!jsonin.get("DataVersion").equals(jsonin1.get("DataVersion"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�DataVersion��"+jsonin.get("DataVersion"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�DataVersion��"+jsonin1.get("DataVersion"));
//				return "success";
			}
				
			if(!jsonin.get("HasIM").equals(jsonin1.get("HasIM"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�HasIM��"+jsonin.get("HasIM"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�HasIM��"+jsonin1.get("HasIM"));
//				return "success";
			}
			
			if(!jsonin.get("HasPASS").equals(jsonin1.get("HasPASS"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�HasPASS��"+jsonin.get("HasPASS"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�HasPASS��"+jsonin1.get("HasPASS"));
//				return "success";
			}
			
			if(!jsonin.get("HasPharm").equals(jsonin1.get("HasPharm"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�HasPharm��"+jsonin.get("HasPharm"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�HasPharm��"+jsonin1.get("HasPharm"));
//				return "success";
			}	
			
			if(!jsonin.get("HasRHC").equals(jsonin1.get("HasRHC"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�HasRHC��"+jsonin.get("HasRHC"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�HasRHC��"+jsonin1.get("HasRHC"));
//				return "success";
			}
			
			if(!jsonin.get("RHCName").equals(jsonin1.get("RHCName"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�RHCName��"+jsonin.get("RHCName"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�RHCName��"+jsonin1.get("RHCName"));
//				return "success";
			}
			
			if(!jsonin.get("RHC").equals(jsonin1.get("RHC"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�RHC��"+jsonin.get("RHC"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�RHC��"+jsonin1.get("RHC"));
//				return "success";
			}
			
			if(!jsonin.get("CanGetScreenDtlRes").equals(jsonin1.get("CanGetScreenDtlRes"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�CanGetScreenDtlRes��"+jsonin.get("CanGetScreenDtlRes"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�CanGetScreenDtlRes��"+jsonin1.get("CanGetScreenDtlRes"));
//				return "success";
			}
			
			if(!jsonin.get("ForCloud").equals(jsonin1.get("ForCloud"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�ForCloud��"+jsonin.get("ForCloud"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�ForCloud��"+jsonin1.get("ForCloud"));
//				return "success";
			}
			
			
			if(!jsonin.get("ReceptClients").equals(jsonin1.get("ReceptClients"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�ReceptClients��"+jsonin.get("ReceptClients"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�ReceptClients��"+jsonin1.get("ReceptClients"));
//				return "success";
			}
			
			//PASS-JAVA��һ���ڵ�
//			if(!jsonin.get("hiscode_invoke").equals(jsonin1.get("hiscode_invoke"))){
////				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
////				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
//				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�hiscode_invoke��"+jsonin.get("hiscode_invoke"));
//				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�hiscode_invoke��"+jsonin1.get("hiscode_invoke"));
////				return "success";
//			}
			
			if(!jsonin.get("mhiscode").equals(jsonin1.get("mhiscode"))){
//				System.out.println("���ԣ�һ��Ŀ¼��ȷ�Ľڵ���HighestSlcode��"+jsonin.get("HighestSlcode"));
//				System.out.println("��Ӧ��һ��Ŀ¼����Ľڵ���HighestSlcode��"+jsonin1.get("HighestSlcode"));
				listerr.add("���ԣ�һ��Ŀ¼��ȷ�Ľڵ��ǣ�mhiscode��"+jsonin.get("mhiscode"));
				listerr.add("��Ӧ��һ��Ŀ¼����Ľڵ��ǣ�mhiscode��"+jsonin1.get("mhiscode"));
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
					listerr.add("���ԣ�ScreenModuleList������Ϊ��"+ScreenModuleList.size());
					listerr.add("��Ӧ��ScreenModuleList������Ϊ��"+ScreenModuleList1.size());
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
					listerr.add("���ԣ�ScreenModuleList��"+ScreenModuleList);
					listerr.add("��Ӧ��ScreenModuleListȱ�ٽ����"+ScreenModuleList1);
				}
				if(ScreenModuleList.size()==0 && ScreenModuleList1.size()>0){
					listerr.add("���ԣ�ScreenModuleList��"+ScreenModuleList);
					listerr.add("��Ӧ��ScreenModuleList��������"+ScreenModuleList1);
				}
				if(ScreenModuleList.size()>0 && ScreenModuleList1.size()>0){
					for(int i=0;i<ScreenModuleList.size();i++){
						JSONObject ScreenModule=ScreenModuleList.getJSONObject(i);
						JSONObject ScreenModule1;
						for(int i1=0;i1<ScreenModuleList1.size();i1++){
							ScreenModule1=ScreenModuleList1.getJSONObject(i1);
							if(ScreenModule.get("ModuleID")==ScreenModule1.get("ModuleID")){
								listerr.add("���ԣ�ScreenModuleList��"+ScreenModule);
								listerr.add("��Ӧ��ScreenModuleList��"+ScreenModule1);
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
					listerr.add("���ԣ�ReferenceModuleList������Ϊ��"+ReferenceModuleList.size());
					listerr.add("��Ӧ��ReferenceModuleList������Ϊ��"+ReferenceModuleList1.size());
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
					listerr.add("���ԣ�ReferenceModuleList��"+ReferenceModuleList);
					listerr.add("��Ӧ��ReferenceModuleListȱ�ٽ����"+ReferenceModuleList1);
				}
				if(ReferenceModuleList.size()==0 && ReferenceModuleList1.size()>0){
					listerr.add("���ԣ�ReferenceModuleList��"+ReferenceModuleList);
					listerr.add("��Ӧ��ReferenceModuleList��������"+ReferenceModuleList1);
				}
				if(ReferenceModuleList.size()>0 && ReferenceModuleList1.size()>0){
					listerr.add("���ԣ�ReferenceModuleList��"+ReferenceModuleList);
					listerr.add("��Ӧ��ReferenceModuleList��"+ReferenceModuleList1);
				}
			}
			
			//����д��󣬴���״̬�ĳ�1
			if(listerr.size()!=0){
				listerrsum=1;
			}
			
			//������д���list
			setListerr1(listerr);
			Date data=new Date();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			System.out.println("IP:"+ip+"��"+sf.format(data));
			return listerr1;
		}catch(Exception ex){
			listerrsum=1;
			System.out.println("json���ڵ����ƿ��ܴ�������");
			listerr.add("json���ڵ����ƿ��ܴ�������");
			setListerr1(listerr);
			Date data=new Date();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			System.out.println("IP:"+ip+"��"+sf.format(data));
			return listerr1;
		}
	}
}
