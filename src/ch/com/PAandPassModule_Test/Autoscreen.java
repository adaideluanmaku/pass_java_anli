package ch.com.PAandPassModule_Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ListOperations;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.Oracleconn;
import com.ch.jdbc.PassMysqlconn;

import ch.com.sys.Passservice;
import freemarker.template.utility.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
/**
 * 1.调用PASS审查，结果数据PASS写入redis
 * 2.通过统计分析临时表的数据制作分表数据
 * 3.调用PA审查，结果数据PA保存在PA审查结果表里面
 * @author 陈辉
 *
 */
public class Autoscreen {
	public static Jedis jedis;
	public static final String PA_SCREENRESULTS = "PA_SCREENRESULT_LIST";
	
	public void PASS() throws ClassNotFoundException, SQLException, IOException{
		
		int count=1 ;//循环总数
		
		//缺少超多日用量 "超多日用量025"
		String[] anliname={"不良反应015","体外配伍068","儿童用药027","剂量范围014","哺乳用药046","围手术期020",
				"妊娠用药012","性别用药031","成人用药018","相互作用022","细菌耐药率015","给药途径036","老人用药021",
				"肝损害剂量027","肾损害剂量066","药物禁忌症032","药物过敏028","超适应症023",
				"越权用药019","配伍浓度020","重复用药031","钾离子浓度069","超多日用量025"};
//		String[] anliname={"不良反应015"};
		
		
		List anlilist=new ArrayList();
		
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		for(int i=0;i<anliname.length;i++){
			sql="select gatherbaseinfo from sa_gather_log where anliname=? and version='1609'";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname[i]);
			rs=pst.executeQuery();
			rs.next();
			
			anlilist.add(rs.getObject(1));
		}
		
		rs.close();
		pst.close();
		mysqlconn.close();
		
		System.out.println(anlilist.size());
		
		//请求passcore
		final Passservice passservice=new Passservice();
		
		for(int i=0;i<count;i++){
			for(int j=0;j<anlilist.size();j++){
				final JSONObject json=JSONObject.fromObject(anlilist.get(j));
				final JSONObject Patient=json.getJSONObject("Patient");
				//修改案例唯一码
				Patient.put("PatCode", Patient.getString("PatCode"));
				Patient.put("InHospNo", Patient.getString("InHospNo"));
				Patient.put("Name", Patient.getString("Name")+"_"+i+"_"+j);
				json.put("Patient", Patient);
//				System.out.println(Patient.get("Name"));
				Thread t=new Thread(new Runnable(){
	    			public void run(){
	    				try {
							String a=passservice.getPassResult(json.toString(), "http://172.18.7.160:8081/pass/ws/PASSwebService.asmx/Mc_DoScreen");
//	    					String a = passservice.getPassResult(json.toString(), "http://172.18.2.179:9099/pass/ws/PASSwebService.asmx/Mc_DoScreen");
							System.out.println("-->"+a);
	    				} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("请求发生异常");
						}
	    			}
	    		});
				t.start();
				
				try {
					if((i*anlilist.size()+(j+1))%(anlilist.size()*2)==0){
						System.out.println("循环次数："+anliname.length*count+"-->"+(i*anlilist.size()+(j+1)));
						t.join();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void PA() throws ClassNotFoundException, SQLException, IOException{
		int count=1;//一批案例的循环次数
		
		//mc_review_detail 108条数据， mc_review_main45条数据
		
		//缺少超多日用量 "超多日用量025"
//		String[] anliname={"不良反应015","体外配伍068","儿童用药027","剂量范围014","哺乳用药046","围手术期020",
//				"妊娠用药012","性别用药031","成人用药018","相互作用022","细菌耐药率015","给药途径036","老人用药021",
//				"肝损害剂量027","肾损害剂量066","药物禁忌症032","药物过敏028","超适应症023",
//				"越权用药019","配伍浓度020","重复用药031","钾离子浓度069","超多日用量025"};
		String[] anliname={"不良反应015"};
		
		List anlilist=new ArrayList();
		
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		
		for(int i=0;i<anliname.length;i++){
			sql="select gatherbaseinfo from sa_gather_log where anliname=? and version='1609'";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname[i]);
			rs=pst.executeQuery();
			rs.next();
			
			anlilist.add(rs.getObject(1));
		}
		
		rs.close();
		pst.close();
		mysqlconn.close();
		
		System.out.println(anlilist.size());
		
		//请求passcore
		final Paservice paservice2=new Paservice();
		
		for(int i=0;i<count;i++){
			for(int j=0;j<anlilist.size();j++){
				final JSONObject json=JSONObject.fromObject(anlilist.get(j));
				final JSONObject Patient=json.getJSONObject("Patient");
				//修改案例唯一码
				Patient.put("PatCode", Patient.getString("PatCode"));
				Patient.put("InHospNo", Patient.getString("InHospNo"));
				Patient.put("Name", Patient.getString("Name")+"_"+i+"_"+j);
				Patient.put("PatStatus", 2);
				//模拟PA组织caseid，PA调用审查时，将caseid存入JSON中的patcode节点中
				String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
				
				Patient.put("PatCode", caseid);
				json.put("Patient", Patient);
				
				JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
				JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
				for (int z=0;z<ScreenDrugs.size();z++) {  
					JSONObject ScreenDrug=ScreenDrugs.getJSONObject(z);
					ScreenDrug.put("RecipNo", i*anlilist.size()+(j+1));
				} 
				ScreenDrugList.put("ScreenDrugs", ScreenDrugs);
				json.put("ScreenDrugList", ScreenDrugList);
				
				JSONObject ScreenMedCondList=json.getJSONObject("ScreenMedCondList");
				JSONArray ScreenMedConds=ScreenMedCondList.getJSONArray("ScreenMedConds");
				for (int z=0;z<ScreenMedConds.size();z++) {  
					JSONObject ScreenMedCond=ScreenMedConds.getJSONObject(z);
					ScreenMedCond.put("RecipNo", i*anlilist.size()+(j+1));
				} 
				ScreenMedCondList.put("ScreenMedConds", ScreenMedConds);
				json.put("ScreenMedCondList", ScreenMedCondList);
				
//				System.out.println(Patient.get("Name"));
				Thread t=new Thread(new Runnable(){
	    			public void run(){
	    				try {
							paservice2.getPassResult(json.toString(), "http://172.18.7.160:8081/pass/ws/paScreen");
//							String a=paservice2.getPassResult(json.toString(), "http://172.18.2.179:9099/pass/ws/paScreen");
//	    					String a = paservice2.restPost( "http://172.18.3.152:7777/pass/ws/paScreen", json.toString());
//							System.out.println("-->"+a);
//							System.out.println("-->"+json.toString());
							
	    				} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("请求发生异常");
						}
	    			}
	    		});
				t.start();
				
				try {
					if((i*anlilist.size()+(j+1))%(anlilist.size()*2)==0){
						System.out.println("循环次数："+anliname.length*count+"-->"+(i*anlilist.size()+(j+1)));
						t.join();
					}
					if((i+1==count) && (j+1==anlilist.size())){
						System.out.println("循环次数："+anliname.length*count+"-->"+(i*anlilist.size()+(j+1)));
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void PA_data(String type) throws ClassNotFoundException, SQLException, IOException{
		int count=1;//一批案例的循环次数
		
		String hiscode="1609PA";
		int mhiscode=0;
		int ienddate=20170710;
		String enddate="2017-07-10";
		
		//缺少案例  "超多日用量025"
//		String[] anliname={"不良反应015","体外配伍068","儿童用药027","剂量范围014","哺乳用药046","围手术期020",
//				"妊娠用药012","性别用药031","成人用药018","相互作用022","细菌耐药率015","给药途径036","老人用药021",
//				"肝损害剂量027","肾损害剂量066","药物禁忌症032","药物过敏028","超适应症023",
//				"越权用药019","配伍浓度020","重复用药031","钾离子浓度069","超多日用量025"};
		String[] anliname={"不良反应015"};
		
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		List anlilist=new ArrayList();
		
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
		
		//mhiscode
		sql="select mhiscode from mc_hospital_match_relation where hiscode_user='"+hiscode+"'";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		rs.next();
		mhiscode=Integer.parseInt(rs.getObject(1).toString());
				
		//先清理一些表
		sql="truncate mc_import_info";
		st=passmysqlconn.createStatement();
		st.execute(sql);
				
		sql="truncate t_mc_screen_info";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate pharm_screenresults";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_review_main";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_review_detail";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_review_question_drugs";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		for(int i=0;i<anliname.length;i++){
			sql="select gatherbaseinfo from sa_gather_log where anliname=? and version='1609'";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname[i]);
			rs=pst.executeQuery();
			rs.next();
			
			anlilist.add(rs.getObject(1));
		}
		
		if("mz".equals(type)){
			//制造t_mc_screen_info数据
			sql="truncate t_mc_screen_info";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			passmysqlconn.setAutoCommit(false);
			sql="insert into t_mc_screen_info (ienddate, mhiscode, caseid, pattype) values(?,?,?,?)";
			pst=passmysqlconn.prepareStatement(sql);
			int a=0;
			int b=0;
			for(int i=0;i<count;i++){
				for(int j=0;j<anlilist.size();j++){
					a=a+1;
					final JSONObject json=JSONObject.fromObject(anlilist.get(j));
					final JSONObject Patient=json.getJSONObject("Patient");
					//门诊caseid：Mz门诊号+“＿”＋病人编号
					String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
					
					pst.setInt(1, ienddate);
					pst.setInt(2, mhiscode);
					pst.setString(3, caseid);
					pst.setInt(4, 2);
					pst.addBatch();
				}
				
				if(a-50000>=0){
					b=b+50000;
					a=a-50000;
					pst.executeBatch();
					System.out.println("挂接-病人总数："+count*anlilist.size()+"-->"+b);
				}
				if((i+1)==count){
					pst.executeBatch();
					System.out.println("挂接-病人总数："+count*anlilist.size()+"-->"+(b+a));
				}
				
			}
			passmysqlconn.commit();
			
			//制造mc_clinic_drugorder_main数据
			sql="truncate mc_clinic_drugorder_main";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			passmysqlconn.setAutoCommit(false);
			sql="insert into mc_clinic_drugorder_main (ienddate, mhiscode, doctorcode, routecode, prescno, deptcode, "
					+ "startdatetime, ordercode, cid, is_allow, caseid) values(?,?,?,?,?,?,?,?,?,?,?)";
			pst=passmysqlconn.prepareStatement(sql);
			a=0;
			b=0;
			for(int i=0;i<count;i++){
				for(int j=0;j<anlilist.size();j++){
					final JSONObject json=JSONObject.fromObject(anlilist.get(j));
					final JSONObject Patient=json.getJSONObject("Patient");
					//门诊caseid：Mz门诊号+“＿”＋病人编号
					String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
					int RecipNo=i*anlilist.size()+(j+1);
					JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
					JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
					//按照处方数还要继续循环
					
					for (int z=0;z<ScreenDrugs.size();z++) {  
						a=a+1;
						JSONObject ScreenDrug=ScreenDrugs.getJSONObject(z);
						
						pst.setInt(1,20170101);
						//Mhiscode,需要查看配对方案表配置
						pst.setInt(2,mhiscode);
						pst.setString(3,ScreenDrug.getString("DoctorCode"));
						pst.setString(4,ScreenDrug.getString("RouteCode"));
						pst.setInt(5,RecipNo);
						pst.setString(6,ScreenDrug.getString("DeptCode"));
						pst.setString(7,ScreenDrug.getString("StartTime"));
						pst.setString(8,ScreenDrug.getString("DrugUniqueCode").split("\\+")[0]);
//						pst.setString(8,ScreenDrug.getString("DrugUniqueCode"));
						//处方号+药品序列号
						pst.setString(9,ScreenDrug.getString("RecipNo")+"#"+ScreenDrug.getString("Index"));
						pst.setInt(10,0);
						pst.setString(11,caseid);
						
						pst.addBatch();
					} 
					
				}
				
				if(a-50000>=0){
					b=b+50000;
					a=a-50000;
					pst.executeBatch();
					System.out.println("病人总数："+count*anlilist.size()+"-->处方数："+b);
				}
				if((i+1)==count){
					pst.executeBatch();
					System.out.println("病人总数："+count*anlilist.size()+"-->处方数："+(b+a));
				}
				
			}
			passmysqlconn.commit();
			
			
			//制造mc_clinic_drugorder_main数据
			sql="truncate mc_clinic_drugorder_detail";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			passmysqlconn.setAutoCommit(false);
			sql="insert into mc_clinic_drugorder_detail (searchcode, grouptag, mhiscode, doctorname, orderstatus, "
					+ "remark, orderno, singledose, frequency, purpose, drugform, deptname, caseid, ienddate, "
					+ "reasonable_desc, drug_unique_code, routename, cid, cost, drugspec, num, days, ordername, "
					+ "groupstate, doseunit, numunit) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pst=passmysqlconn.prepareStatement(sql);
			a=0;
			b=0;
			for(int i=0;i<count;i++){
				for(int j=0;j<anlilist.size();j++){
					final JSONObject json=JSONObject.fromObject(anlilist.get(j));
					final JSONObject Patient=json.getJSONObject("Patient");
					//门诊caseid：Mz门诊号+“＿”＋病人编号
					String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
					int RecipNo=i*anlilist.size()+(j+1);
					JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
					JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
					//按照处方数还要继续循环
					
					for (int z=0;z<ScreenDrugs.size();z++) {  
						a=a+1;
						JSONObject ScreenDrug=ScreenDrugs.getJSONObject(z);
						
						pst.setString(1,"");
						pst.setString(2,ScreenDrug.getString("GroupTag"));
						pst.setInt(3,mhiscode);
						pst.setString(4,ScreenDrug.getString("DoctorName"));
						pst.setString(5,ScreenDrug.getString("OrderType"));
						pst.setString(6,"");
						pst.setString(7,ScreenDrug.getString("OrderNo"));
						pst.setString(8,ScreenDrug.getString("DosePerTime"));
						pst.setString(9,ScreenDrug.getString("Frequency"));
						pst.setInt(10,5);
						pst.setString(11,ScreenDrug.getString("Form"));
						pst.setString(12,ScreenDrug.getString("DeptName"));
						pst.setString(13,caseid);
						pst.setInt(14,ienddate);
						pst.setString(15,"");
						pst.setString(16,ScreenDrug.getString("DrugUniqueCode"));
						pst.setString(17,ScreenDrug.getString("RouteName"));
						pst.setString(18,ScreenDrug.getString("RecipNo")+"#"+ScreenDrug.getString("Index"));
						pst.setInt(19,10);
						pst.setString(20,"");
						pst.setInt(21,1);
						pst.setInt(22,1);
						pst.setString(23,ScreenDrug.getString("DrugName"));
						pst.setInt(24,1);
						pst.setString(25,ScreenDrug.getString("DoseUnit"));
						pst.setString(26,ScreenDrug.getString("NumUnit"));
						
						pst.addBatch();
					} 
					
				}
				
				if(a-50000>=0){
					b=b+50000;
					a=a-50000;
					pst.executeBatch();
					System.out.println("病人总数："+count*anlilist.size()+"-->处方数："+b);
				}
				if((i+1)==count){
					pst.executeBatch();
					System.out.println("病人总数："+count*anlilist.size()+"-->处方数："+(b+a));
				}
				
			}
			passmysqlconn.commit();
			
			sql="update mc_clinic_drugorder_detail a,mc_dict_drug_pass b set a.drugspec=b.drugspec where a.drug_unique_code=b.drug_unique_code ";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			
			//制造mc_clinic_prescription数据
			sql="truncate mc_clinic_prescription";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			passmysqlconn.setAutoCommit(false);
			sql="insert into mc_clinic_prescription (ienddate, is_order, mhiscode, doctorname, doctorcode, "
					+ "diagnosis, medgroupcode, is_emergency, presctype, cost, pharmacists, prescno, medgroupname, "
					+ "deptcode, drugcost, pharmacists_, enddate, deptname, caseid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?)";
			pst=passmysqlconn.prepareStatement(sql);
			a=0;
			b=0;
			for(int i=0;i<count;i++){
				for(int j=0;j<anlilist.size();j++){
					final JSONObject json=JSONObject.fromObject(anlilist.get(j));
					final JSONObject Patient=json.getJSONObject("Patient");
					//门诊caseid：Mz门诊号+“＿”＋病人编号
					String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
					int RecipNo=i*anlilist.size()+(j+1);
					JSONObject ScreenMedCondList=json.getJSONObject("ScreenMedCondList");
					JSONArray ScreenMedConds=ScreenMedCondList.getJSONArray("ScreenMedConds");
					//按照处方数还要继续循环
					
					for (int z=0;z<ScreenMedConds.size();z++) {  
						a=a+1;
						JSONObject ScreenMedCond=ScreenMedConds.getJSONObject(z);
						
						pst.setInt(1,ienddate);
						pst.setInt(2,0);
						pst.setInt(3,mhiscode);
						pst.setString(4,Patient.getString("DoctorName"));
						pst.setString(5,Patient.getString("DoctorCode"));
						pst.setString(6,ScreenMedCond.getString("DiseaseName"));
						pst.setString(7,"");
						pst.setInt(8,0);
						pst.setInt(9,0);
						pst.setInt(10,10);
						pst.setString(11,"");
						pst.setInt(12,RecipNo);
						pst.setString(13,"");
						pst.setString(14,Patient.getString("DeptCode"));
						pst.setInt(15,10);
						pst.setString(16,"");
						pst.setString(17,enddate);
						pst.setString(18,Patient.getString("DeptName"));
						pst.setString(19,caseid);
						
						pst.addBatch();
					} 
				}
				
				if(a-50000>=0){
					b=b+50000;
					a=a-50000;
					pst.executeBatch();
					System.out.println("病人总数："+count*anlilist.size()+"-->疾病数："+b);
				}
				if((i+1)==count){
					pst.executeBatch();
					System.out.println("病人总数："+count*anlilist.size()+"-->疾病数："+(b+a));
				}
				
			}
			passmysqlconn.commit();
			
			//制造mc_clinic_patient_baseinfo数据
			sql="truncate mc_clinic_patient_baseinfo";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			passmysqlconn.setAutoCommit(false);
			sql="insert into mc_clinic_patient_baseinfo (ienddate, mhiscode, allergen, weight, clinicno, hep_damage, "
					+ "ren_damage, is_preg, preg_starttime, height, standby, patientid, age, is_lact, birthdate, "
					+ "usetime, identitycard, telephone, caseid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pst=passmysqlconn.prepareStatement(sql);
			a=0;
			b=0;
			for(int i=0;i<count;i++){
				for(int j=0;j<anlilist.size();j++){
					a=a+1;
					final JSONObject json=JSONObject.fromObject(anlilist.get(j));
					final JSONObject Patient=json.getJSONObject("Patient");
					//门诊caseid：Mz门诊号+“＿”＋病人编号
					String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
					pst.setInt(1,ienddate);
					pst.setInt(2,mhiscode);
					pst.setString(3,"");
					pst.setString(4,Patient.getString("WeighKG"));
					pst.setString(5,Patient.getString("InHospNo"));
					pst.setString(6,Patient.getString("HepDamageDegree"));
					pst.setString(7,Patient.getString("RenDamageDegree"));
					pst.setString(8,Patient.getString("IsPregnancy"));
					pst.setString(9,Patient.getString("PregStartDate"));
					pst.setString(10,Patient.getString("HeightCM"));
					pst.setString(11,"");
					pst.setString(12,Patient.getString("PatCode"));
					if(StringUtils.isBlank(Patient.getString("Age"))){
						pst.setInt(13,0);//age
					}else{
						pst.setString(13,Patient.getString("Age"));//age
					}
					pst.setString(14,Patient.getString("IsLactation"));
					pst.setString(15,Patient.getString("Birthday"));
					pst.setString(16,Patient.getString("UseTime"));
					pst.setString(17,"");
					pst.setString(18,"");
					pst.setString(19,caseid);
					
					pst.addBatch();
				}
				
				if(a-50000>=0){
					b=b+50000;
					a=a-50000;
					pst.executeBatch();
					System.out.println("病人基本信息表-病人总数："+count*anlilist.size()+"-->"+b);
				}
				if((i+1)==count){
					pst.executeBatch();
					System.out.println("病人基本信息表-病人总数："+count*anlilist.size()+"-->"+(b+a));
				}
				
			}
			passmysqlconn.commit();
			
			
			//制造mc_clinic_patient_medinfo数据
			sql="truncate mc_clinic_patient_medinfo";
			st=passmysqlconn.createStatement();
			st.execute(sql);
			
			passmysqlconn.setAutoCommit(false);
			sql="insert into mc_clinic_patient_medinfo (ienddate, mhiscode, doctorname, sex, doctorcode, clinicno, "
					+ "diagnosis, iage, medgroupcode, is_emergency, presctype, cost, patientname, prescno, payclass, "
					+ "medgroupname, deptcode, age, drugcost, enddate, deptname, caseid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?)";
			pst=passmysqlconn.prepareStatement(sql);
			a=0;
			b=0;
			for(int i=0;i<count;i++){
				for(int j=0;j<anlilist.size();j++){
					a=a+1;
					final JSONObject json=JSONObject.fromObject(anlilist.get(j));
					final JSONObject Patient=json.getJSONObject("Patient");
					//门诊caseid：Mz门诊号+“＿”＋病人编号
					String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
					int RecipNo=i*anlilist.size()+(j+1);
					
					pst.setInt(1,ienddate);
					pst.setInt(2,mhiscode);
					pst.setString(3,Patient.getString("DoctorName"));
					if(StringUtils.isBlank(Patient.getString("Sex"))){
						pst.setString(4,"无");//sex
					}else{
						pst.setString(4,Patient.getString("Sex"));//sex
					}
					pst.setString(5,Patient.getString("DoctorCode"));
					pst.setString(6,Patient.getString("InHospNo"));
					pst.setString(7,"");
					pst.setInt(8,16);
					pst.setString(9,"");
					pst.setInt(10,0);
					pst.setInt(11,0);
					pst.setInt(12,10);
					pst.setString(13,Patient.getString("Name")+"_"+i+"_"+j);
					pst.setInt(14,RecipNo);
					pst.setString(15,Patient.getString("PayClass"));
					pst.setString(16,"");
					pst.setString(17,Patient.getString("DeptCode"));
					if(StringUtils.isBlank(Patient.getString("Age"))){
						pst.setInt(18,0);//age
					}else{
						pst.setString(18,Patient.getString("Age"));//age
					}
					pst.setInt(19,10);
					pst.setString(20,enddate);
					pst.setString(21,Patient.getString("DeptName"));
					pst.setString(22,caseid);
					
					pst.addBatch();
				}
				
				if(a-50000>=0){
					b=b+50000;
					a=a-50000;
					pst.executeBatch();
					System.out.println("门诊病人就诊信息-病人总数："+count*anlilist.size()+"-->"+b);
				}
				if((i+1)==count){
					pst.executeBatch();
					System.out.println("门诊病人就诊信息-病人总数："+count*anlilist.size()+"-->"+(b+a));
				}
				
			}
			passmysqlconn.commit();
			
			rs.close();
			pst.close();
			mysqlconn.close();
			passmysqlconn.close();
//			System.out.println("sa_pat_info:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
		}
	}
	
	public void Checkdata() throws ClassNotFoundException, SQLException, IOException{
		
		//缺少案例 "超多日用量025"
		String[] anliname={"不良反应015","体外配伍068","儿童用药027","剂量范围014","哺乳用药046","围手术期020",
				"妊娠用药012","性别用药031","成人用药018","相互作用022","细菌耐药率015","给药途径036","老人用药021",
				"肝损害剂量027","肾损害剂量066","药物禁忌症032","药物过敏028","超适应症023",
				"越权用药019","配伍浓度020","重复用药031","钾离子浓度069","超多日用量025"};
		
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		String sql=null;
		for(int i=0;i<anliname.length;i++){
			sql="select gatherbaseinfo from sa_gather_log where anliname=? and version='1609' ";
			pst=mysqlconn.prepareStatement(sql);
			pst.setString(1, anliname[i]);
			rs=pst.executeQuery();
			rs.next();
			
			JSONObject json=JSONObject.fromObject(rs.getObject(1).toString());
			
			JSONObject ScreenMedCondList=json.getJSONObject("ScreenMedCondList");
			JSONArray ScreenMedConds=ScreenMedCondList.getJSONArray("ScreenMedConds");
			//判断疾病里面的数据是否存在问题
			for(int j=0;j<ScreenMedConds.size();j++){
				JSONObject ScreenMedCond=(JSONObject)ScreenMedConds.get(j);
				
				if(StringUtils.isNotBlank(ScreenMedCond.get("DiseaseCode").toString())){
					sql="select count(*) from mc_dict_disease where discode=?";
					pst=passmysqlconn.prepareStatement(sql);
					pst.setString(1, ScreenMedCond.get("DiseaseCode").toString());
					rs=pst.executeQuery();
					rs.next();
					if(Integer.parseInt(rs.getObject(1).toString())==0){
						System.out.println(anliname[i]+"-->DiseaseCode:"+ScreenMedCond.getString("DiseaseCode")+" 找不到配对数据");
					}
				}
			}
			
			JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
			JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
			//判断药品里面的数据是否存在问题
			for(int j=0;j<ScreenDrugs.size();j++){
				JSONObject ScreenDrug=(JSONObject)ScreenDrugs.get(j);
				
				if(StringUtils.isNotBlank(ScreenDrug.get("RouteCode").toString())){
					sql="select count(*) from mc_dict_route where routecode=?";
					pst=passmysqlconn.prepareStatement(sql);
					pst.setString(1, ScreenDrug.getString("RouteCode"));
					rs=pst.executeQuery();
					rs.next();
					if(Integer.parseInt(rs.getObject(1).toString())==0){
						System.out.println(anliname[i]+"-->RouteCode:"+ScreenDrug.getString("RouteCode")+" 找不到配对数据");
					}
				}
				
				if(StringUtils.isNotBlank(ScreenDrug.get("DoctorCode").toString())){
					sql="select count(*) from mc_dict_doctor where doctorcode=?";
					pst=passmysqlconn.prepareStatement(sql);
					pst.setString(1, ScreenDrug.getString("DoctorCode"));
					rs=pst.executeQuery();
					rs.next();
					if(Integer.parseInt(rs.getObject(1).toString())==0){
						System.out.println(anliname[i]+"-->DoctorCode:"+ScreenDrug.getString("DoctorCode")+" 找不到配对数据");
					}
				}
				
				if(StringUtils.isNotBlank(ScreenDrug.get("DeptCode").toString())){
					sql="select count(*) from mc_dict_dept where deptcode=?";
					pst=passmysqlconn.prepareStatement(sql);
					pst.setString(1, ScreenDrug.getString("DeptCode"));
					rs=pst.executeQuery();
					rs.next();
					if(Integer.parseInt(rs.getObject(1).toString())==0){
						System.out.println(anliname[i]+"-->DeptCode:"+ScreenDrug.getString("DeptCode")+" 找不到配对数据");
					}
				}
			}
		}
		System.out.println("查找结束");
		rs.close();
		pst.close();
		mysqlconn.close();
		passmysqlconn.close();
	}
	
	public void daofenbiao() throws ClassNotFoundException, SQLException, IOException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
		
		//logid跨度，避免重复
		sql="select max(logid) from sa_pat_info";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		rs.next();
		int count=Integer.parseInt(rs.getObject(1).toString());
		
		//循环次数表示分表数
		for(int i=0;i<12;i++){
			//sa_pat_info数据
			sql="update sa_pat_info set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_pat_info set usetime='2017-0"+(i+1)+"-01',usedate='20170"+(i+1)+"01'";
			}else{
				sql="update sa_pat_info set usetime='2017-"+(i+1)+"-01',usedate='2017"+(i+1)+"01'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_pat_info_0"+(i+1)+" select * from sa_pat_info";
			}else{
				sql="insert into sa_pat_info_"+(i+1)+" select * from sa_pat_info";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_screenresults数据
			sql="update sa_screenresults set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_screenresults set usetime='2017-0"+(i+1)+"-01',usedate='20170"+(i+1)+"01'";
			}else{
				sql="update sa_screenresults set usetime='2017-"+(i+1)+"-01',usedate='2017"+(i+1)+"01'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_screenresults_0"+(i+1)+" select * from sa_screenresults";
			}else{
				sql="insert into sa_screenresults_"+(i+1)+" select * from sa_screenresults";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_disease数据
			sql="update sa_pat_disease set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_pat_disease set usedate='20170"+(i+1)+"01'";
			}else{
				sql="update sa_pat_disease set usedate='2017"+(i+1)+"01'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_pat_disease_0"+(i+1)+" select * from sa_pat_disease";
			}else{
				sql="insert into sa_pat_disease_"+(i+1)+" select * from sa_pat_disease";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_allergens数据
			sql="update sa_pat_allergens set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_pat_allergens set usedate='20170"+(i+1)+"01'";
			}else{
				sql="update sa_pat_allergens set usedate='2017"+(i+1)+"01'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_pat_allergens_0"+(i+1)+" select * from sa_pat_allergens";
			}else{
				sql="insert into sa_pat_allergens_"+(i+1)+" select * from sa_pat_allergens";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_orders数据
			sql="update sa_pat_orders set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_pat_orders set usedate='20170"+(i+1)+"01'";
			}else{
				sql="update sa_pat_orders set usedate='2017"+(i+1)+"01'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_pat_orders_0"+(i+1)+" select * from sa_pat_orders";
			}else{
				sql="insert into sa_pat_orders_"+(i+1)+" select * from sa_pat_orders";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_operation数据
			sql="update sa_pat_operation set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_pat_operation set usedate='20170"+(i+1)+"01'";
			}else{
				sql="update sa_pat_operation set usedate='2017"+(i+1)+"01'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_pat_operation_0"+(i+1)+" select * from sa_pat_operation";
			}else{
				sql="insert into sa_pat_operation_"+(i+1)+" select * from sa_pat_operation";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_request数据
			sql="update sa_request set logid=logid+"+count;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="update sa_request set reqtime='20170"+(i+1)+"01111111111'";
				sql="update sa_request set endtime='20170"+(i+1)+"01111111111'";
			}else{
				sql="update sa_request set reqtime='2017"+(i+1)+"01111111111'";
				sql="update sa_request set endtime='20170"+(i+1)+"01111111111'";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			if(i<9){
				sql="insert into sa_request_0"+(i+1)+" select * from sa_request";
			}else{
				sql="insert into sa_request_"+(i+1)+" select * from sa_request";
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			System.out.println(i+1+"月表导入完成");
		}
		
//		passmysqlconn.commit();  
		System.out.println("数据生成结束");
		rs.close();
		st.close();
		passmysqlconn.close();
	}
	
	public void rebootlinshibiao() throws ClassNotFoundException, SQLException, IOException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		Statement st=null;
		List list=null;
		String sql=null;
		
		//清空临时表数据
		//sa_pat_info数据
		sql="truncate sa_pat_info";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_pat_info_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_screenresults数据
		sql="truncate sa_screenresults";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_screenresults_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_pat_disease数据
		sql="truncate sa_pat_disease";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_pat_disease_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_pat_allergens数据
		sql="truncate sa_pat_allergens";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_pat_allergens_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_pat_orders数据
		sql="truncate sa_pat_orders";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_pat_orders_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_pat_operation数据
		sql="truncate sa_pat_operation";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_pat_operation_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//sa_request数据
		sql="truncate sa_request";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
//		sql="truncate sa_request_copy";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		System.out.println("临时表清空完成");
				
		st.close();
		passmysqlconn.close();
	}
	
	public void rebootfenbiao() throws ClassNotFoundException, SQLException, IOException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		Statement st=null;
		List list=null;
		String sql=null;
		
		for(int i=0;i<12;i++){
			//sa_pat_info数据
			if(i<9){
				sql="truncate sa_pat_info_0"+(i+1);
			}else{
				sql="truncate sa_pat_info_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_screenresults数据
			if(i<9){
				sql="truncate sa_screenresults_0"+(i+1);
			}else{
				sql="truncate sa_screenresults_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_disease数据
			if(i<9){
				sql="truncate sa_pat_disease_0"+(i+1);
			}else{
				sql="truncate sa_pat_disease_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_allergens数据
			if(i<9){
				sql="truncate sa_pat_allergens_0"+(i+1);
			}else{
				sql="truncate sa_pat_allergens_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_orders数据
			if(i<9){
				sql="truncate sa_pat_orders_0"+(i+1);
			}else{
				sql="truncate sa_pat_orders_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_operation数据
			if(i<9){
				sql="truncate sa_pat_operation_0"+(i+1);
			}else{
				sql="truncate sa_pat_operation_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_request数据
			if(i<9){
				sql="truncate sa_request_0"+(i+1);
			}else{
				sql="truncate sa_request_"+(i+1);
			}
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			System.out.println(i+1+"月表清空完成");
		}
		
		System.out.println("数据重置结束");
		st.close();
		passmysqlconn.close();
	}
	
	public void copydata() throws ClassNotFoundException, SQLException, IOException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
		
		//复制数据到临时表
//		//sa_pat_info数据
//		sql="insert into sa_pat_info_copy select * from sa_pat_info_copy1";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_screenresults数据
//		sql="insert into sa_screenresults_copy select * from sa_screenresults_copy1";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_disease数据
//		sql="insert into sa_pat_disease_copy select * from sa_pat_disease_copy1";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_allergens数据
//		sql="insert into sa_pat_allergens_copy select * from sa_pat_allergens_copy1";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_orders数据
//		sql="insert into sa_pat_orders_copy select * from sa_pat_orders_copy1";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
//		
//		//sa_pat_operation数据
//		sql="insert into sa_pat_operation_copy select * from sa_pat_operation_copy1";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
			
		//修改临时表主键，处理完数据后再还原
		//sa_screenresults数据
		sql="alter table sa_screenresults change chkresid chkresid bigint null,drop primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_disease数据
		sql="alter table sa_pat_disease change disid disid bigint null,drop primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_allergens数据
		sql="alter table sa_pat_allergens change allerid allerid bigint null,drop primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_orders数据
		sql="alter table sa_pat_orders change cid cid bigint null,drop primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_operation数据
		sql="alter table sa_pat_operation change oprid oprid bigint null,drop primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
				
//		//sa_request数据
//		sql="alter table sa_request change logid logid bigint null,drop primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
		
		//logid跨度，避免重复
		sql="select max(logid) from sa_pat_info_copy1";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		rs.next();
		int count=Integer.parseInt(rs.getObject(1).toString());
		
		//复制数据到临时表的循环次数，制造数据
		int xunhuan=50000;
		System.out.println("开始循环制造数据");
		for(int i=0;i<xunhuan;i++){
			//sa_pat_info数据
			sql="truncate sa_pat_info_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_info_copy select * from sa_pat_info_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_info_copy set logid=logid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_info_copy set patcode=CONCAT_WS('_',patcode,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_info_copy set inhospno=CONCAT_WS('_',inhospno,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_info_copy set patname=CONCAT_WS('_',patname,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_info_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_info select * from sa_pat_info_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_screenresults数据
			sql="truncate sa_screenresults_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_screenresults_copy select * from sa_screenresults_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_screenresults_copy set chkresid=chkresid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_screenresults_copy set logid=logid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_screenresults_copy set patcode=CONCAT_WS('_',patcode,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_screenresults_copy set inhospno=CONCAT_WS('_',inhospno,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_screenresults_copy set patname=CONCAT_WS('_',patname,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_screenresults_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_screenresults select * from sa_screenresults_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_disease数据
			sql="truncate sa_pat_disease_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_disease_copy select * from sa_pat_disease_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_disease_copy set disid=disid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_disease_copy set logid=logid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_disease_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_disease select * from sa_pat_disease_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_allergens数据
			sql="truncate sa_pat_allergens_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_allergens_copy select * from sa_pat_allergens_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_allergens_copy set allerid=allerid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_allergens_copy set logid=logid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_allergens_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_allergens select * from sa_pat_allergens_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_orders数据
			sql="truncate sa_pat_orders_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_orders_copy select * from sa_pat_orders_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_orders_copy set cid=cid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_orders_copy set logid=logid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_orders_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_orders select * from sa_pat_orders_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			//sa_pat_operation数据
			sql="truncate sa_pat_operation_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_operation_copy select * from sa_pat_operation_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_operation_copy set oprid=oprid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_operation_copy set logid=logid+"+count*i;
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="update sa_pat_operation_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_pat_operation select * from sa_pat_operation_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
				
			//sa_request数据
			sql="truncate sa_request_copy";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
			sql="insert into sa_request_copy select * from sa_request_copy1";
			st=passmysqlconn.createStatement();
			st.executeUpdate(sql);
			
//			sql="update sa_request_copy set logid=logid+"+count*i;
//			st=passmysqlconn.createStatement();
//			st.executeUpdate(sql);
//			
//			sql="update sa_request_copy set caseid=CONCAT_WS('_',caseid,'"+(i+1)+"')";
//			st=passmysqlconn.createStatement();
//			st.executeUpdate(sql);
			
//			sql="insert into sa_request select * from sa_request_copy";
//			st=passmysqlconn.createStatement();
//			st.executeUpdate(sql);
			
			System.out.println(xunhuan+"-->"+(i+1));
		}
				
		
		//还原主键
		//sa_screenresults数据
		sql="alter table sa_screenresults change chkresid chkresid bigint not null auto_increment primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_disease数据
		sql="alter table sa_pat_disease change disid disid bigint not null auto_increment primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_allergens数据
		sql="alter table sa_pat_allergens change allerid allerid bigint not null auto_increment primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_orders数据
		sql="alter table sa_pat_orders change cid cid bigint not null auto_increment primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		//sa_pat_operation数据
		sql="alter table sa_pat_operation change oprid oprid bigint not null auto_increment primary key";
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
				
//		//sa_request数据
//		sql="alter table sa_request change logid logid bigint not null auto_increment primary key";
//		st=passmysqlconn.createStatement();
//		st.executeUpdate(sql);
				
//		passmysqlconn.commit();  
		System.out.println("数据复制结束");
		rs.close();
		st.close();
		passmysqlconn.close();
	}
	
	public void copydata1() throws ClassNotFoundException, SQLException, IOException, InterruptedException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
			
		//每条数据的循环次数
		final int xunhuan=3;
		
		//logid跨度，避免重复
		sql="select max(logid) from sa_pat_info_copy";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		rs.next();
		final int count=Integer.parseInt(rs.getObject(1).toString());
		
		System.out.println("开始循环制造数据");
		//sa_pat_info数据
		Thread t1=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_pat_info_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_pat_info (birthday, hepdamagedegree, sex, doctorname, weight, patstatus,"
							+ " islactation, inserttime, height, deptcode, ispregnancy, rendamagedegree, usetime, "
							+ "usedate, deptname, patname, caseid, pregstartdate, visitcode, doctorcode, logid, "
							+ "hisname, patcode, inhospno, hiscode, checkmode) values(?,?,?,?,?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("birthday").toString());//
							pst.setString(2,map.get("hepdamagedegree").toString());//
							pst.setString(3,map.get("sex").toString());//
							pst.setString(4,map.get("doctorname").toString());//
							pst.setString(5,map.get("weight").toString());//
							pst.setString(6,map.get("patstatus").toString());//
							pst.setString(7,map.get("islactation").toString());//
							pst.setString(8,map.get("inserttime").toString());//
							pst.setString(9,map.get("height").toString());//
							pst.setString(10,map.get("deptcode").toString());//
							pst.setString(11,map.get("ispregnancy").toString());//
							pst.setString(12,map.get("rendamagedegree").toString());//
							pst.setString(13,map.get("usetime").toString());//
							pst.setString(14,map.get("usedate").toString());//
							pst.setString(15,map.get("deptname").toString());//
							pst.setString(16,map.get("patname").toString()+"_"+(i+1));//
							pst.setString(17,map.get("caseid").toString()+"_"+(i+1));//
							pst.setString(18,map.get("pregstartdate").toString());//
							pst.setString(19,map.get("visitcode").toString());//
							pst.setString(20,map.get("doctorcode").toString());//
							pst.setInt(21,Integer.parseInt(map.get("logid").toString())+count*i);//
							pst.setString(22,map.get("hisname").toString());//
							pst.setString(23,map.get("patcode").toString()+"_"+(i+1));//
							pst.setString(24,map.get("inhospno").toString()+"_"+(i+1));//
							pst.setString(25,map.get("hiscode").toString());//
							pst.setString(26,map.get("checkmode").toString());//
							pst.addBatch();
							
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_pat_info:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_pat_info制造数据异常");
				}
			}
		});
		t1.start();
		
		//sa_screenresults数据
		Thread t2=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_screenresults_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_screenresults (istempdrug, doctorname, moduleitems, orderindex, "
							+ "reason, recipno, frequency, slcode, patstatus, dosepertime, drugname, modulename, "
							+ "drugcode, isuser, routecode, deptcode, usetime, moduleid, usedate, deptname, "
							+ "patname, caseid, patstatusdesc, visitcode, doctorcode, slcodename, drug_unique_code, "
							+ "severity, startdatetime, is_forstatic, routename, enddatetime, checktype, logid, hisname, "
							+ "executetime, patcode, inhospno, hiscode, doseunit, shield, otherinfo, warning) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("istempdrug").toString());
							pst.setString(2,map.get("doctorname").toString());
							pst.setString(3,map.get("moduleitems").toString());
							pst.setString(4,map.get("orderindex").toString());
							pst.setString(5,map.get("reason").toString());
							pst.setString(6,map.get("recipno").toString());
							pst.setString(7,map.get("frequency").toString());
							pst.setString(8,map.get("slcode").toString());
							pst.setString(9,map.get("patstatus").toString());
							pst.setString(10,map.get("dosepertime").toString());
							pst.setString(11,map.get("drugname").toString());
							pst.setString(12,map.get("modulename").toString());
							pst.setString(13,map.get("drugcode").toString());
							pst.setString(14,map.get("isuser").toString());
							pst.setString(15,map.get("routecode").toString());
							pst.setString(16,map.get("deptcode").toString());
							pst.setString(17,map.get("usetime").toString());
							pst.setString(18,map.get("moduleid").toString());
							pst.setString(19,map.get("usedate").toString());
							pst.setString(20,map.get("deptname").toString());
							pst.setString(21,map.get("patname").toString()+"_"+(i+1));
							pst.setString(22,map.get("caseid").toString()+"_"+(i+1));
							pst.setString(23,map.get("patstatusdesc").toString());
							pst.setString(24,map.get("visitcode").toString());
							pst.setString(25,map.get("doctorcode").toString());
							pst.setString(26,map.get("slcodename").toString());
							pst.setString(27,map.get("drug_unique_code").toString());
							pst.setString(28,map.get("severity").toString());
							pst.setString(29,map.get("startdatetime").toString());
							pst.setString(30,map.get("is_forstatic").toString());
							pst.setString(31,map.get("routename").toString());
							pst.setString(32,map.get("enddatetime").toString());
							pst.setString(33,map.get("checktype").toString());
							pst.setInt(34,Integer.parseInt(map.get("logid").toString())+count*i);
							pst.setString(35,map.get("hisname").toString());
							pst.setString(36,map.get("executetime").toString());
							pst.setString(37,map.get("patcode").toString()+"_"+(i+1));
							pst.setString(38,map.get("inhospno").toString()+"_"+(i+1));
							pst.setString(39,map.get("hiscode").toString());
							pst.setString(40,map.get("doseunit").toString());
							pst.setString(41,map.get("shield").toString());
							pst.setString(42,map.get("otherinfo").toString());
							pst.setString(43,map.get("warning").toString());
							
							pst.addBatch();
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_screenresults:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_screenresults制造数据异常");
				}
			}
		});
		t2.start();
		//sa_pat_disease数据
		Thread t3=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_pat_disease_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_pat_disease (hisname, discode, dissource, hiscode, recipno, "
							+ "usedate, disname, disindex, caseid, logid) "
							+ "values(?,?,?,?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("hisname").toString());
							pst.setString(2,map.get("discode").toString());
							pst.setString(3,map.get("dissource").toString());
							pst.setString(4,map.get("hiscode").toString());
							pst.setString(5,map.get("recipno").toString());
							pst.setString(6,map.get("usedate").toString());
							pst.setString(7,map.get("disname").toString());
							pst.setString(8,map.get("disindex").toString());
							pst.setString(9,map.get("caseid").toString()+"_"+(i+1));
							pst.setInt(10,Integer.parseInt(map.get("logid").toString())+count*i);
							
							pst.addBatch();
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_pat_disease:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_pat_disease制造数据异常");
				}
			}
		});
		t3.start();
		
		//sa_pat_allergens数据
		Thread t4=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_pat_allergens_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_pat_allergens (hisname, allercode, allersource, allerindex, "
							+ "hiscode, allername, usedate, caseid, symptom, logid) "
							+ "values(?,?,?,?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("hisname").toString());
							pst.setString(2,map.get("allercode").toString());
							pst.setString(3,map.get("allersource").toString());
							pst.setString(4,map.get("allerindex").toString());
							pst.setString(5,map.get("hiscode").toString());
							pst.setString(6,map.get("allername").toString());
							pst.setString(7,map.get("usedate").toString());
							pst.setString(8,map.get("caseid").toString()+"_"+(i+1));
							pst.setString(9,map.get("symptom").toString());
							pst.setInt(10,Integer.parseInt(map.get("logid").toString())+count*i);
							
							pst.addBatch();
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_pat_allergens:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_pat_allergens制造数据异常");
				}
			}
		});
		t4.start();
		
		//sa_pat_orders数据
		Thread t5=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_pat_orders_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_pat_orders (grouptag, istempdrug, orderindex, doctorname, maxwarn, remark, orderno, "
							+ "recipno, purpose, frequency, dosepertime, operationcode, drugname, routecode, deptcode, "
							+ "allergens_strs, usedate, freqsource, deptname, caseid, operation_strs, drugsource, doctorcode, "
							+ "routesource, drug_unique_code, startdatetime, ordertype, routename, enddatetime, disease_strs, "
							+ "logid, hisname, num, executetime, medtime, hiscode, orders_strs, doseunit, numunit) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("grouptag").toString());
							pst.setString(2,map.get("istempdrug").toString());
							pst.setString(3,map.get("orderindex").toString());
							pst.setString(4,map.get("doctorname").toString());
							pst.setString(5,map.get("maxwarn").toString());
							pst.setString(6,map.get("remark").toString());
							pst.setString(7,map.get("orderno").toString());
							pst.setString(8,map.get("recipno").toString());
							pst.setString(9,map.get("purpose").toString());
							pst.setString(10,map.get("frequency").toString());
							pst.setString(11,map.get("dosepertime").toString());
							pst.setString(12,map.get("operationcode").toString());
							pst.setString(13,map.get("drugname").toString());
							pst.setString(14,map.get("routecode").toString());
							pst.setString(15,map.get("deptcode").toString());
							pst.setString(16,(String)map.get("allergens_strs"));
							pst.setString(17,map.get("usedate").toString());
							pst.setString(18,map.get("freqsource").toString());
							pst.setString(19,map.get("deptname").toString());
							pst.setString(20,map.get("caseid").toString()+"_"+(i+1));
							pst.setString(21,(String)map.get("operation_strs"));
							pst.setString(22,map.get("drugsource").toString());
							pst.setString(23,map.get("doctorcode").toString());
							pst.setString(24,map.get("routesource").toString());
							pst.setString(25,map.get("drug_unique_code").toString());
							pst.setString(26,map.get("startdatetime").toString());
							pst.setString(27,map.get("ordertype").toString());
							pst.setString(28,map.get("routename").toString());
							pst.setString(29,map.get("enddatetime").toString());
							pst.setString(30,(String)map.get("disease_strs"));
							pst.setInt(31,Integer.parseInt(map.get("logid").toString())+count*i);
							pst.setString(32,map.get("hisname").toString());
							pst.setString(33,map.get("num").toString());
							pst.setString(34,map.get("executetime").toString());
							pst.setString(35,map.get("medtime").toString());
							pst.setString(36,map.get("hiscode").toString());
							pst.setString(37,(String)map.get("orders_strs"));
							pst.setString(38,map.get("doseunit").toString());
							pst.setString(39,map.get("numunit").toString());
							
							pst.addBatch();
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_pat_orders:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_pat_orders制造数据异常");
				}
			}
		});
		t5.start();
		
		//sa_pat_operation数据
		Thread t6=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_pat_operation_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_pat_operation (hisname, oprenddate, oprname, hiscode, incisiontype, oprindex, "
							+ "usedate, oprstartdate, operationcode, caseid, logid) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("hisname").toString());
							pst.setString(2,map.get("oprenddate").toString());
							pst.setString(3,map.get("oprname").toString());
							pst.setString(4,map.get("hiscode").toString());
							pst.setInt(5,Integer.parseInt(map.get("incisiontype").toString()));
							pst.setString(6,map.get("oprindex").toString());
							pst.setString(7,map.get("usedate").toString());
							pst.setString(8,map.get("oprstartdate").toString());
							pst.setString(9,map.get("operationcode").toString());
							pst.setString(10,map.get("caseid").toString());
							pst.setInt(11,Integer.parseInt(map.get("logid").toString())+count*i);
							
							pst.addBatch();
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_pat_operation:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_pat_operation制造数据异常");
				}
			}
		});
		t6.start();
		
		//sa_pat_operation数据
		Thread t7=new Thread(new Runnable(){
			public void run(){
				PassMysqlconn passmysql=new PassMysqlconn();
				try {
					Connection passmysqlconn = passmysql.getConn();
					PreparedStatement pst=null;
					Statement st=null;
					ResultSet rs=null;
					List list=null;
					String sql=null;
					
					sql="select * from sa_request_copy";
					st=passmysqlconn.createStatement();
					rs=st.executeQuery(sql);
					list=passmysql.getlist(rs);
					
					//sa_request数据
					sql="alter table sa_request change logid logid bigint null,drop primary key";
					st=passmysqlconn.createStatement();
					st.executeUpdate(sql);
					
					passmysqlconn.setAutoCommit(false);
					sql="insert into sa_request (endtime, reqtype, clientip, RESPONDTIME, reqtime, caseid, logid) "
							+ "values(?,?,?,?,?,?,?)";
					pst=passmysqlconn.prepareStatement(sql);
					for(int j=0;j<list.size();j++){
						Map map=(Map)list.get(j);
						for(int i=0;i<xunhuan;i++){
							pst.setString(1,map.get("endtime").toString());
							pst.setInt(2,Integer.parseInt(map.get("reqtype").toString()));
							pst.setString(3,map.get("clientip").toString());
							pst.setString(4,map.get("RESPONDTIME").toString());
							pst.setString(5,map.get("reqtime").toString());
							pst.setString(6,map.get("caseid").toString());
							pst.setInt(7,Integer.parseInt(map.get("logid").toString())+count*i);
							
							pst.addBatch();
							if((i+1)%50000==0){
								pst.executeBatch();
							}
							if((i+1)==xunhuan){
								pst.executeBatch();
							}
						}
						System.out.println("sa_request:"+list.size()*xunhuan+"-->"+((j+1)*xunhuan));
					}
					
					//sa_request还原主键
					sql="alter table sa_request change logid logid bigint not null auto_increment primary key";
					st=passmysqlconn.createStatement();
					st.executeUpdate(sql);
					
					passmysqlconn.commit();
					rs.close();
					pst.close();
					passmysqlconn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("sa_request制造数据异常");
				}
			}
		});
		t7.start();
		
		t1.join();
		System.out.println("sa_pat_info 复制结束");
		t2.join();
		System.out.println("sa_screenresults 复制结束");
		t3.join();
		System.out.println("sa_pat_disease 复制结束");
		t4.join();
		System.out.println("sa_pat_allergens 复制结束");
		t5.join();
		System.out.println("sa_pat_orders 复制结束");
		t6.join();
		System.out.println("sa_pat_operation 复制结束");
		t7.join();
		System.out.println("sa_request 复制结束");
		
		System.out.println("数据制造结束");
		rs.close();
		st.close();
		passmysqlconn.close();
	}
	
	
	public void mysql_O_data() throws ClassNotFoundException, SQLException, IOException, TimeoutException, ParseException{
		//制作数据参数设置
		String[] hiscodes={"HISCODE001","HISCODE002","HISCODE003","HISCODE004"};
		
//		String hiscode="HISCODE001";
		final String ienddate="20120118";//设置时间起始点
		final String enddate="2012-01-18";
		final String costtime="2012-01-18 01:01:01";
		final String startdate="2012-01-18 01:01:01";//住院开始时间
		
		final int sum_date=40;//表示N天，每分割一次，起始日期增加一天，不能设置0，N表示循环N次分割
		final int count=100*sum_date;//一天数据量，一批案例的循环次数,数据集22条
		final int countzy=20*sum_date;//一天数据量，一批案例的循环次数,数据集22条
		final int countcy=10*sum_date;//一天数据量，一批案例的循环次数,数据集22条
		
		int mz=1;//控制数据制造开关 0关，1开
		int zy=0;
		int cy=0;
		int dict=0;
		
		//是否清空表业务表
		final int trunca=0;//0关1开
		
		//是否创建视图
		final int createview=0;//0关1开
				
		//导字典表方案数据
		int match_scheme=6;
				
		//手动开启刷redis功能
		int red=0;
		String rhmmurl="http://172.18.7.160:8088/passrhmm";
		
		//String 转 date
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date time = sdf.parse(costtime);
//		Calendar cal = Calendar.getInstance();   
//        cal.setTime(time);   
//        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
//        final String startdate=sdf.format(cal.getTime());
        
		//案例
		String[] anliname={"不良反应015","体外配伍068","儿童用药027","剂量范围014","哺乳用药046","围手术期020",
				"妊娠用药012","性别用药031","成人用药018","相互作用022","细菌耐药率015","给药途径036","老人用药021",
				"肝损害剂量027","肾损害剂量066","药物禁忌症032","药物过敏028","超适应症023",
				"越权用药019","配伍浓度020","重复用药031","钾离子浓度069","超多日用量025"};
//		String[] anliname={"不良反应015"};
		
		//pass案例数据源 pass_java_anli
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		
		//字典表数据源 passpa2db_1609_rh_v5
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		//目标数据 pass_pa_1609
		Oracleconn oraclesql=new Oracleconn();
		Connection oraclesqlconn=oraclesql.getConn();
			
		final List anlilist=new ArrayList();
		
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		List list=null;
		String sql=null;
		
		for(int o=0;o<hiscodes.length;o++){
			final String hiscode=hiscodes[o];//其实是用户HISCODE，就是hiscode_user 1609PA HISCODE001
			if(createview==1){
				//重置视图
				System.out.println("===开始重置ORACLE数据======");
				System.out.println("开始重置字典表");
				sql="create or replace view MDC2_DICT_ALLERGEN_VIEW as SELECT '"+hiscode+"' AS hiscode ,allercode ,allername "
						+ "FROM mc_dict_allergen mda ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_COST_ITEM_VIEW as SELECT '"+hiscode+"' AS hiscode , itemcode AS "
						+ "itemcode ,mdc.itemname AS itemname ,3 AS itemtype FROM    mc_dict_costitem mdc";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_DEPT_VIEW as SELECT '"+hiscode+"'AS hiscode, deptcode, deptname, "
						+ "is_clinic, is_inhosp, is_emergency FROM mc_dict_dept ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_DISEASE_VIEW as SELECT '"+hiscode+"'AS hiscode, discode, disname FROM "
						+ "mc_dict_disease mdd ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_DOCTOR_VIEW as SELECT '"+hiscode+"'AS hiscode, doctorcode, doctorname, "
						+ "deptcode, deptname, ilevel, doctorlevel, 1 AS is_clinic, 1 AS prespriv, ilevel AS antilevel FROM "
						+ "mc_dict_doctor ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_DRUG_VIEW as SELECT '"+hiscode+"'AS hiscode,a.drugcode, "
						+ "c.drug_unique_code, b.drugname, b.drugform, b.drugspec, c.approvalcode, c.comp_name, c.doseunit, "
						+ "b.costunit, b.adddate, b.is_use, a.is_anti, a.antitype, a.antilevel, b.ddd, b.dddunit, "
						+ "a.is_basedrug,88 AS unitprice FROM mc_dict_drug a, mc_dict_drug_sub b, mc_dict_drug_pass c "
						+ "where a.drugcode=b.drugcode and a.drugcode=c.drugcode ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_EXAM_VIEW as SELECT '"+hiscode+"' AS hiscode,examcode, mde.examname "
						+ "FROM mc_dict_exam mde";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_FREQUENCY_VIEW as SELECT '"+hiscode+"' AS hiscode,mdf.frequency FROM "
						+ "mc_dict_frequency mdf";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_LAB_ITEM_VIEW as SELECT '"+hiscode+"' AS hiscode,itemcode, itemname "
						+ "FROM mc_dict_labsub ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_LAB_VIEW as SELECT '"+hiscode+"' AS hiscode,labcode, labname FROM "
						+ "mc_dict_lab ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_OPERATION_VIEW as SELECT '"+hiscode+"' AS hiscode,operationcode,"
						+ "mdo.operationname FROM mc_dict_operation mdo";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="create or replace view MDC2_DICT_ROUTE_VIEW as SELECT '"+hiscode+"' AS hiscode,routecode, routename "
						+ "FROM mc_dict_route ";//MATCH_SCHEME
				st=oraclesqlconn.createStatement();
				st.execute(sql);
					
				System.out.println("开始重置业务视图");
				//门诊业务数据
				sql="CREATE OR REPLACE VIEW MDC2_MZ_ALLERGEN_VIEW (HISCODE, PATIENTID, CLINICNO, ALLERCODE, "
						+ "ALLERNAME, SYMPTOM) AS SELECT hiscode,patientid, clinicno, allercode, allername, "
						+ "symptom FROM t_mc_clinic_allergen";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW  MDC2_MZ_COST_VIEW   (  HISCODE  ,   PATIENTID  ,   CLINICNO  ,   PRESCNO  ,   ITEMCODE  ,   ITEMNAME  ,   DRUGFORM  ,   DRUGSPEC  ,   DRUGSCCJ  ,   ITEMUNIT  ,   ITEMNUM  ,   COST  ,   COSTTIME  ,   DEPTCODE  ,   DEPTNAME  ,   DOCTORCODE  ,   DOCTORNAME  ,   MEDGROUPCODE  ,   MEDGROUPNAME  ,   ROUTECODE  ,   COSTTYPE  ,   PHARMACISTS  ,   PHARMACISTS_  ,   DRUGINDEX  ) AS  " + 
						"  SELECT  hiscode , " + 
						"            patientid , " + 
						"            clinicno , " + 
						"            prescno , " + 
						"            itemcode , " + 
						"            itemname , " + 
						"            drugform , " + 
						"            drugspec , " + 
						"            drugsccj , " + 
						"            itemunit , " + 
						"            itemnum , " + 
						"            cost , " + 
						"            costtime , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            medgroupcode , " + 
						"            medgroupname , " + 
						"            routecode , " + 
						"            costtype, " + 
						"            pharmacists, " + 
						"            pharmacists_, " + 
						"						DRUGINDEX " + 
						" " + 
						"    FROM    t_mc_clinic_cost";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_MZ_DISEASE_VIEW ( HISCODE ,  PRESCNO ,  PATIENTID ,  CLINICNO , "
						+ " DISCODE ,  DISNAME ) AS SELECT hiscode,prescno, patientid, clinicno, discode, disname FROM "
						+ "t_mc_clinic_disease";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW  MDC2_MZ_EXAM_VIEW  ( HISCODE ,  PATIENTID ,  CLINICNO ,  EXAMCODE ,  EXAMNAME ,  REQUESTNO ,  BODYPART ,  EXAMRESULT ,  REPORTTIME ,  DOCTORNAME ) AS  " + 
						"  SELECT   hiscode,patientid , " + 
						"            clinicno , " + 
						"           examcode , " + 
						"            examname , " + 
						"             requestno , " + 
						"            bodypart , " + 
						"            examresult , " + 
						"             reporttime , " + 
						"             doctorname as doctorname " + 
						"    FROM    T_MC_CLINIC_EXAM";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_MZ_LAB_VIEW ( HISCODE ,  PATIENTID ,  CLINICNO ,  REQUESTNO ,  LABCODE ,  LABNAME ,  SAMPLETYPE ,  SAMPLINGTIME ,  ITEMCODE ,  ITEMNAME ,  LABRESULT ,  RESULTFLAG ,  RANGE_0 ,  UNIT ,  REPORTTIME ,  DOCTORNAME ) AS  " + 
						"  SELECT   hiscode,patientid , " + 
						"            clinicno , " + 
						"           clinicno as  requestno , " + 
						"            labcode , " + 
						"            labname , " + 
						"            sampletype , " + 
						"           samplingtime , " + 
						"            itemcode , " + 
						"           itemname , " + 
						"           labresult , " + 
						"            resultflag , " + 
						"            range_0 , " + 
						"            unit , " + 
						"            reporttime , " + 
						"            doctorname " + 
						"    FROM    t_mc_clinic_lab";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_MZ_ORDERS_VIEW  ( HISCODE ,  PRESCNO ,  PRESCTYPE ,  CID ,  PATIENTID ,  CLINICNO ,  ORDERNO ,  ORDERTYPE ,  GROUPTAG ,  DRUG_UNIQUE_CODE ,  ORDERCODE ,  ORDERNAME ,  DRUGFORM ,  DRUGSPEC ,  ROUTECODE ,  ROUTENAME ,  SINGLEDOSE ,  DOSEUNIT ,  FREQUENCY ,  NUM ,  NUMUNIT ,  COST ,  DEPTCODE ,  DEPTNAME ,  DOCTORCODE ,  DOCTORNAME ,  STARTDATETIME ,  DAYS ,  REMARK ,  PURPOSE ,  REASONABLE_DESC ) AS  " + 
						"  SELECT  hiscode , " + 
						"            prescno , " + 
						"            presctype, " + 
						"            cid , " + 
						"            patientid , " + 
						"            clinicno , " + 
						"            orderno , " + 
						"            ordertype  , " + 
						"            grouptag, " + 
						"            drug_unique_code , " + 
						"            ordercode , " + 
						"            ordername , " + 
						"            drugform , " + 
						"            drugspec , " + 
						"            routecode , " + 
						"            routename , " + 
						"            singledose , " + 
						"            doseunit , " + 
						"            frequency, " + 
						"            num , " + 
						"            numunit , " + 
						"            cost , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"           startdatetime , " + 
						"            days, " + 
						"           REMARK, " + 
						"           purpose, " + 
						"reasonable_desc " + 
						"    FROM    T_MC_CLINIC_ORDER";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW  MDC2_MZ_PATIENT_VIEW  ( HISCODE ,  PATIENTID ,  CLINICNO ,  PATIENTNAME ,  SEX ,  BIRTHDATE ,  AGE ,  HEIGHT ,  WEIGHT ,  IDENTITYCARD ,  TELEPHONE ,  ENDDATE ,  DEPTCODE ,  DEPTNAME ,  DOCTORCODE ,  DOCTORNAME ,  MEDGROUPCODE ,  MEDGROUPNAME ,  PAYCLASS ,  IS_EMERGENCY ,  IS_PREG ,  PREG_STARTTIME ,  IS_LACT ,  HEP_DAMAGE ,  REN_DAMAGE ,  STANDBY ) AS  " + 
						"  SELECT  hiscode , " + 
						"            patientid , " + 
						"            clinicno , " + 
						"           patientname , " + 
						"            sex , " + 
						"            birthdate , " + 
						"            age , " + 
						"            height , " + 
						"            weight , " + 
						"            identitycard , " + 
						"            telephone , " + 
						"            enddate , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            medgroupcode , " + 
						"            medgroupname , " + 
						"            payclass , " + 
						"            is_emergency , " + 
						"            is_preg , " + 
						"            preg_starttime , " + 
						"            is_lact , " + 
						"            hep_damage , " + 
						"            ren_damage , " + 
						"            standby " + 
						"    FROM    t_mc_clinic_patient";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW  MDC2_MZ_PRESC_VIEW  ( HISCODE ,  PATIENTID ,  CLINICNO ,  PRESCNO ,  PRESCTYPE ,  PRESCNAME ,  COST ,  DEPTCODE ,  DEPTNAME ,  DOCTORCODE ,  DOCTORNAME ,  MEDGROUPCODE ,  MEDGROUPNAME ,  ENDDATE ,  PHARM_REVIEW ,  PHARM_MIX ,  PHARM_CHECK ,  PHARM_DISPENSING ,  REMARK ) AS  " + 
						"  SELECT hiscode,patientid, " + 
						"clinicno, " + 
						"prescno, " + 
						" presctype, " + 
						" prescname, " + 
						"sum(cost)cost, " + 
						"deptcode, " + 
						"deptname, " + 
						"doctorcode, " + 
						"doctorname, " + 
						"medgroupcode, " + 
						"medgroupname, " + 
						"max(enddate) enddate, " + 
						" pharm_review, " + 
						"  pharm_mix, " + 
						" pharm_check, " + 
						" pharm_dispensing, " + 
						"remark FROM " + 
						"( " + 
						"SELECT a.hiscode, " + 
						"a.patientid, " + 
						"a.clinicno, " + 
						"a.PRESCNO as prescno, " + 
						" 1 as presctype, " + 
						"'处方名称' as  prescname, " + 
						"a.cost, " + 
						"b.deptcode, " + 
						"b.deptname, " + 
						"b.doctorcode, " + 
						"b.doctorname, " + 
						"b.medgroupcode, " + 
						"b.medgroupname, " + 
						"costtime enddate, " + 
						"'shys' pharm_review, " + 
						"'tpys'  pharm_mix, " + 
						"'hdys'  pharm_check, " + 
						"'fyys'  pharm_dispensing, " + 
						"'共3剂，每日1剂，水煎400ml，分早晚两次空腹温服。 'remark " + 
						"    FROM t_mc_clinic_cost  a LEFT JOIN t_mc_clinic_patient  b ON a.caseid=b.caseid " + 
						"    )aa " + 
						"GROUP BY hiscode,patientid, " + 
						"clinicno, " + 
						"prescno, " + 
						" presctype, " + 
						" prescname, " + 
						"deptcode, " + 
						"deptname, " + 
						"doctorcode, " + 
						"doctorname, " + 
						"medgroupcode, " + 
						"medgroupname, " + 
						" pharm_review, " + 
						"  pharm_mix, " + 
						" pharm_check, " + 
						" pharm_dispensing, " + 
						"remark";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW  MDC2_ZY_ALLERGEN_VIEW  ( HISCODE ,  PATIENTID ,  VISITID ,  ALLERCODE , "
						+ " ALLERNAME ,  SYMPTOM ) AS  SELECT   hiscode,patientid , visitid, allercode ,allername , "
						+ "symptom FROM  t_mc_outhosp_allergen UNION ALL SELECT   hiscode,patientid , visitid,  "
						+ "allercode ,  allername , symptom  FROM t_mc_inhosp_allergen";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_COST_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  ITEMCODE , "
						+ " ITEMNAME ,  DRUGFORM ,  DRUGSPEC ,  DRUGSCCJ ,  ITEMUNIT ,  ITEMNUM ,  COST ,  COSTTIME , "
						+ " DEPTCODE ,  DEPTNAME ,  WARDCODE ,  WARDNAME ,  DOCTORCODE ,  DOCTORNAME ,  MEDGROUPCODE , "
						+ " MEDGROUPNAME ,  IS_OUT ,  COSTTYPE ,  ROUTECODE ,  DRUGINDEX ) AS SELECT   hiscode ,  "
						+ "patientid ,  visitid , itemcode ,  itemname ,  drugform ,  drugspec ,   drugsccj , itemunit , "
						+ "itemnum , cost , costtime ,deptcode , deptname , deptcode AS wardcode ,  deptname AS wardname , "
						+ " doctorcode ,  doctorname ,  medgroupcode , medgroupname ,  is_out , costtype , routecode,"
						+ " DRUGINDEX FROM t_mc_outhosp_cost UNION ALL SELECT  hiscode , patientid ,  visitid ,  "
						+ "itemcode ,  itemname , drugform , drugspec , drugsccj , itemunit , itemnum ,cost ,  costtime ,  "
						+ "deptcode ,  deptname , deptcode AS wardcode ,  deptname AS wardname , doctorcode , doctorname ,"
						+ " medgroupcode , medgroupname ,  is_out , costtype , routecode, DRUGINDEX FROM  t_mc_inhosp_cost";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_DISEASE_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  DISCODE ,  DISNAME ,  TREATMENT ,  DISEASETYPE ,  IS_HOSPINFECTION ,  IS_MAIN ) AS  " + 
						"  SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            discode , " + 
						"            disname , " + 
						"            treatment , " + 
						"            diseasetype , " + 
						"            is_hospinfection , " + 
						"            is_main " + 
						"    FROM    t_mc_outhosp_disease " + 
						"    UNION ALL " + 
						"    SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            discode , " + 
						"            disname , " + 
						"            treatment , " + 
						"            diseasetype , " + 
						"            is_hospinfection , " + 
						"            is_main " + 
						"    FROM    t_mc_inhosp_disease";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_EXAM_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  REQUESTNO ,  EXAMCODE ,  EXAMNAME ,  BODYPART ,  EXAMRESULT ,  REPORTTIME ,  DOCTORNAME ) AS  " + 
						"  SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            requestno , " + 
						"            examcode , " + 
						"            examname , " + 
						"            bodypart , " + 
						"            examresult , " + 
						"            reporttime , " + 
						"            doctorname " + 
						"    FROM    t_mc_outhosp_exam " + 
						"    UNION ALL " + 
						"    SELECT  hiscode , " + 
						"           patientid , " + 
						"            visitid , " + 
						"            requestno , " + 
						"            examcode , " + 
						"            examname , " + 
						"            bodypart , " + 
						"            examresult , " + 
						"            reporttime , " + 
						"            doctorname " + 
						"    FROM    t_mc_inhosp_exam";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_LAB_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  LABCODE ,  REQUESTNO ,  LABNAME ,  SAMPLETYPE ,  SAMPLINGTIME ,  DOCTORNAME ,  REPORTTIME ,  UNIT ,  RANGE_0 ,  RESULTFLAG ,  LABRESULT ,  ITEMNAME ,  ITEMCODE ) AS  " + 
						"  SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            labcode , " + 
						"            requestno , " + 
						"            labname , " + 
						"            sampletype , " + 
						"            samplingtime , " + 
						"            doctorname , " + 
						"            reporttime , " + 
						"            unit , " + 
						"             range_0, " + 
						"            resultflag , " + 
						"            labresult , " + 
						"            itemname , " + 
						"            itemcode " + 
						"    FROM    t_mc_outhosp_lab " + 
						"    UNION ALL " + 
						"    SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            labcode , " + 
						"            requestno , " + 
						"            labname , " + 
						"            sampletype , " + 
						"            samplingtime , " + 
						"            doctorname , " + 
						"            reporttime , " + 
						"            unit , " + 
						"                range_0, " + 
						"            resultflag , " + 
						"            labresult , " + 
						"            itemname , " + 
						"            itemcode " + 
						"    FROM   t_mc_inhosp_lab";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_MED_VIEW ( HISCODE ,  HOSPITALNO ,  PATIENTID ,  VISITID ,  BIRTHPLACE ,  NATION ,  WORKADDRESS ,  CONTACTADDRESS ,  BLOODTYPE ,  BLOODPRESSURE ,  BMI ,  BSA ,  SHENGAO ,  TIZHONG ,  TELEPHONE ,  BLSH ,  BSZHUSU ,  BSXBS ,  BSJWBS ,  BSJZS ,  BSYWBLFYJCZS ,  BSJWYYS ,  BSBFJBYYYQK ,  GMYAOPIN ,  GMFANYING ,  IN_DIAGNOSIS ,  OUT_DIAGNOSIS ) AS  " + 
						"  SELECT " + 
						"hiscode, " + 
						"hospitalno, " + 
						"patientid, " + 
						"visitid, " + 
						"  '籍贯_四川成都'  birthplace, " + 
						" '民族_汉族'  nation, " + 
						" '工作地址_高新区德商国际' as workaddress, " + 
						" '联系地址_高新区德商国际55号' contactaddress, " + 
						" '血型_B' bloodtype, " + 
						" '血压_120' bloodpressure, " + 
						" '体重指数_1.2' bmi, " + 
						" '体表面积_140' bsa, " + 
						" height AS shengao, " + 
						"weight as tizhong, " + 
						" '系电话_16520114847' telephone, " + 
						" '不良嗜好_烟酒毒' blsh, " + 
						" '主诉_右腿骨折' bszhusu, " + 
						" '现病史_粉碎性骨折' bsxbs, " + 
						" '既往病史_脑震荡' bsjwbs, " + 
						" '家族史_无'  bsjzs, " + 
						" '药物不良反应及处置史_青霉素过敏' bsywblfyjczs, " + 
						" '往用药史_一袋头孢' bsjwyys, " + 
						" '伴发疾病与用药情况_青霉素' bsbfjbyyyqk, " + 
						" '过敏药品_青霉素' gmyaopin, " + 
						" '过敏症状_全身红斑' gmfanying, " + 
						" '入院诊断_右腿骨折' in_diagnosis, " + 
						" '出院诊断_右腿骨折' out_diagnosis " + 
						"FROM t_mc_inhosp_patient " + 
						"UNION ALL " + 
						"SELECT " + 
						"hiscode, " + 
						"hospitalno, " + 
						"patientid, " + 
						"visitid, " + 
						"  '籍贯_四川成都' birthplace, " + 
						" '民族_汉族' nation, " + 
						" '工作地址_高新区德商国际' as workaddress, " + 
						"'联系地址_高新区德商国际55号' contactaddress, " + 
						" '血型_B' bloodtype, " + 
						" '血压_120' bloodpressure, " + 
						" '体重指数_1.2' bmi, " + 
						" '体表面积_140' bsa, " + 
						" height AS  shengao, " + 
						"weight as tizhong, " + 
						" '联系电话_16520114847' telephone, " + 
						" '不良嗜好_烟酒毒' blsh, " + 
						" '主诉_右腿骨折' bszhusu, " + 
						" '现病史_粉碎性骨折' bsxbs, " + 
						" '既往病史_脑震荡' bsjwbs, " + 
						" '家族史_无' bsjzs, " + 
						" '药物不良反应及处置史_青霉素过敏' bsywblfyjczs, " + 
						" '既往用药史_一袋头孢' bsjwyys, " + 
						" '伴发疾病与用药情况_青霉素' bsbfjbyyyqk, " + 
						" '过敏药品_青霉素' gmyaopin, " + 
						" '过敏症状_全身红斑' gmfanying, " + 
						" '入院诊断_右腿骨折' in_diagnosis, " + 
						" '出院诊断_右腿骨折' out_diagnosis " + 
						"FROM t_mc_outhosp_patient";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_OPERATION_VIEW ( HISCODE ,  OPRID ,  PATIENTID ,  VISITID ,  OPERATIONCODE ,  OPERATIONNAME ,  INCISIONTYPE ,  STARTDATE ,  ENDDATE ,  DOCTORCODE ,  DOCTORNAME ,  DEPTCODE ,  DEPTNAME ) AS  " + 
						"  SELECT   hiscode,oprid , " + 
						"            patientid , " + 
						"            visitid, " + 
						"            operationcode , " + 
						"            operationname , " + 
						"            incisiontype , " + 
						"            startdate , " + 
						"            enddate , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            deptcode , " + 
						"            deptname " + 
						"    FROM    t_mc_outhosp_operation  UNION  ALL " + 
						" " + 
						"    SELECT " + 
						"            hiscode,oprid , " + 
						"            patientid , " + 
						"            visitid, " + 
						"            operationcode , " + 
						"            operationname , " + 
						"            incisiontype , " + 
						"            startdate , " + 
						"            enddate , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            deptcode , " + 
						"            deptname  FROM  t_mc_inhosp_operation";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_ORDERS_VIEW ( HISCODE ,  CID ,  PATIENTID ,  VISITID ,  ORDERNO ,  ORDERTYPE ,  GROUPTAG ,  IS_TEMP ,  DRUG_UNIQUE_CODE ,  ORDERCODE ,  ORDERNAME ,  DRUGFORM ,  DRUGSPEC ,  ROUTECODE ,  ROUTENAME ,  SINGLEDOSE ,  DOSEUNIT ,  FREQUENCY ,  DEPTCODE ,  DEPTNAME ,  WARDCODE ,  WARDNAME ,  DOCTORCODE ,  DOCTORNAME ,  EXECUTETIME ,  STARTDATETIME ,  ENDDATETIME ,  PURPOSE ,  REMARK ,  IS_OUT ,  IS_ALLOW ,  REASONABLE_DESC ,  MEDITIME ) AS  " + 
						"  SELECT  hiscode,cid , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            orderno , " + 
						"            ordertype , " + 
						"             '0'  as grouptag , " + 
						"            0 AS is_temp , " + 
						"            drug_unique_code , " + 
						"            ordercode , " + 
						"            ordername , " + 
						"             ''  AS drugform , " + 
						"            ''   AS drugspec , " + 
						"            routecode , " + 
						"            routename , " + 
						"            ''   AS singledose , " + 
						"            ''  AS doseunit , " + 
						"            frequency , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            deptcode AS wardcode , " + 
						"            deptname AS wardname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            executetime , " + 
						"            startdatetime , " + 
						"            enddatetime , " + 
						"            0 AS purpose , " + 
						"            remark , " + 
						"            0 AS is_out , " + 
						"            0 AS is_allow , " + 
						"             ''  AS reasonable_desc , " + 
						"            2 as meditime " + 
						"    FROM    t_mc_outhosp_order " + 
						"    UNION ALL " + 
						"    SELECT    hiscode,cid , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            orderno , " + 
						"            ordertype , " + 
						"             '0'  AS grouptag , " + 
						"            0 AS is_temp , " + 
						"            drug_unique_code , " + 
						"            ordercode , " + 
						"            ordername , " + 
						"             ''  AS drugform , " + 
						"             ''  AS drugspec , " + 
						"            routecode , " + 
						"            routename , " + 
						"            ''   AS singledose , " + 
						"             ''  AS doseunit , " + 
						"            frequency , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            deptcode AS wardcode , " + 
						"            deptname AS wardname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            executetime , " + 
						"            startdatetime , " + 
						"            enddatetime , " + 
						"            0 AS purpose , " + 
						"            remark , " + 
						"            0 AS is_out , " + 
						"            0 AS is_allow , " + 
						"             ''  AS reasonable_desc , " + 
						"            2 as meditime " + 
						"    FROM    t_mc_inhosp_order";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_PATIENT_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  HOSPITALNO ,  PATIENTNAME ,  SEX ,  BIRTHDATE ,  AGE ,  HEIGHT ,  WEIGHT ,  IDENTITYCARD ,  TELEPHONE ,  BEDNO ,  DEPTCODE ,  DEPTNAME ,  DOCTORCODE ,  DOCTORNAME ,  WARDCODE ,  WARDNAME ,  MEDGROUPCODE ,  MEDGROUPNAME ,  STARTDATE ,  ENDDATE ,  IS_LACT ,  REN_DAMAGE ,  HEP_DAMAGE ,  PREG_STARTTIME ,  IS_PREG ,  PAYCLASS ,  NURSINGCLASS ,  I_IN ,  ACCOUNTDATE ,  STANDBY ) AS  " + 
						"  SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            hospitalno , " + 
						"           substr(patientname, 1, 10) AS patientname , " + 
						"            sex , " + 
						"            birthdate , " + 
						"            age , " + 
						"            height , " + 
						"            weight , " + 
						"            substr(identitycard, 1, 24) AS identitycard , " + 
						"             telephone , " + 
						"            bedno , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            wardcode AS wardcode , " + 
						"            wardname AS wardname , " + 
						"            medgroupcode , " + 
						"            medgroupname , " + 
						"            startdate , " + 
						"            enddate , " + 
						"            is_lact , " + 
						"            ren_damage , " + 
						"            hep_damage , " + 
						"            preg_starttime , " + 
						"            is_preg , " + 
						"            payclass , " + 
						"            nursingclass , " + 
						"            0 AS i_in , " + 
						"             ''  AS accountdate, " + 
						"              '' standby " + 
						"    FROM     t_mc_outhosp_patient " + 
						"    UNION ALL " + 
						"    SELECT  hiscode , " + 
						"            patientid , " + 
						"            visitid , " + 
						"            hospitalno , " + 
						"            substr(patientname, 1, 10) " + 
						"                as patientname , " + 
						"            sex , " + 
						"            birthdate , " + 
						"            age , " + 
						"            height , " + 
						"            weight , " + 
						"            substr(identitycard, 1, 24) AS identitycard , " + 
						"            telephone , " + 
						"            bedno , " + 
						"            deptcode , " + 
						"            deptname , " + 
						"            doctorcode , " + 
						"            doctorname , " + 
						"            wardcode AS wardcode , " + 
						"            wardname AS wardname , " + 
						"            medgroupcode , " + 
						"            medgroupname , " + 
						"            startdate , " + 
						"            enddate , " + 
						"            is_lact , " + 
						"            ren_damage , " + 
						"            hep_damage , " + 
						"            preg_starttime , " + 
						"            is_preg , " + 
						"            payclass , " + 
						"            nursingclass , " + 
						"            1 AS i_in , " + 
						"             ''  AS accountdate, " + 
						"            ''   standby " + 
						"    FROM     t_mc_inhosp_patient";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_TEMPERATURE_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  TAKETIME ,  TEMPERATURE ) AS  " + 
						"  SELECT HISCODE, PATIENTID , VISITID , TAKETIME , TEMPERATURE  " + 
						"FROM   T_MC_INHOSP_TEMPERATURE " + 
						" " + 
						"UNION ALL " + 
						"SELECT HISCODE, PATIENTID , VISITID , TAKETIME , TEMPERATURE  " + 
						"FROM   T_MC_OUTHOSP_TEMPERATURE";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				sql="CREATE OR REPLACE FORCE VIEW MDC2_ZY_TRANSFERRED_VIEW ( HISCODE ,  PATIENTID ,  VISITID ,  DEPT_STAYED ,  ADMISSION_DATE_TIME ,  DISCHARGE_DATE_TIME ,  DEPT_TRANSFERED_TO ,  DOCTOR_IN_CHARGE ) AS  " + 
						"  SELECT " + 
						"hiscode, " + 
						"patientid, " + 
						"visitid, " + 
						"deptcode as dept_stayed, " + 
						"startdate AS  admission_date_time, " + 
						"enddate as discharge_date_time, " + 
						"deptcode dept_transfered_to, " + 
						"doctorcode as doctor_in_charge FROM t_mc_outhosp_patient " + 
						" " + 
						"UNION ALL " + 
						" " + 
						"SELECT " + 
						"hiscode, " + 
						"patientid, " + 
						"visitid, " + 
						"deptcode as dept_stayed, " + 
						"startdate AS  admission_date_time, " + 
						"enddate as discharge_date_time, " + 
						"deptcode dept_transfered_to, " + 
						"doctorcode as doctor_in_charge FROM t_mc_inhosp_patient";
				st=oraclesqlconn.createStatement();
				st.execute(sql);
				
				System.out.println("重置ORACLE数据结束");
			}
			
			//获取案例数据
			for(int u=0;u<anliname.length;u++){
				sql="select gatherbaseinfo from sa_gather_log where anliname=? and version='1609'";
				pst=mysqlconn.prepareStatement(sql);
				pst.setString(1, anliname[u]);
				rs=pst.executeQuery();
				rs.next();
				
				//变更HISCODE
				JSONObject json=JSONObject.fromObject(rs.getObject(1));
				JSONObject PassClient=json.getJSONObject("PassClient");
				PassClient.put("HospID", hiscode);
				json.put("PassClient", PassClient);
				anlilist.add(json.toString());
				
				//不变更HISCODE
//				anlilist.add(rs.getObject(1).toString());
			}
			
			if(mz==1){
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_allergen数据
							if(trunca==1){
								sql="truncate table t_mc_clinic_allergen";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_allergen (allercode, patientid, clinicno, hiscode, allername, symptom, "
									+ "caseid) values(?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenAllergenList=json.getJSONObject("ScreenAllergenList");
									JSONArray ScreenAllergens=ScreenAllergenList.getJSONArray("ScreenAllergens");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenAllergens.size();k++){
										a=a+1;
										JSONObject ScreenAllergen=ScreenAllergens.getJSONObject(k);
										
										pst.setString(1,ScreenAllergen.getString("AllerCode"));
										pst.setString(2,Patient.getString("PatCode"));
										pst.setString(3,Patient.getString("InHospNo"));
										pst.setString(4,PassClient.getString("HospID"));
										pst.setString(5,ScreenAllergen.getString("AllerName"));
										pst.setString(6,"过敏症状");
										pst.setString(7,caseid);
										
										pst.addBatch();
									}
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_allergen总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_allergen总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							st.close();
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_allergen制造数据异常"+e);
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						PassMysqlconn passmysql=new PassMysqlconn();
						
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							Connection passmysqlconn=passmysql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_cost数据
							if(trunca==1){
								sql="truncate table t_mc_clinic_cost";
							st=oraclesqlconn.createStatement();
							st.execute(sql);
							}
							
							//-------插入非药品费用
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_cost (doctorname, clinicno, iid, itemcode, is_use, drugform, pharmacists, "
									+ "itemname, routecode, drugsccj, deptcode, medgroupname, itemnum, pharmacists_, deptname, "
									+ "caseid, doctorcode, drugindex, medgroupcode, cost, costtime, drugspec, patientid, prescno, "
									+ "hiscode, itemunit, costtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							int iid=0;
							String ienddate1=ienddate;
							String costtime1=costtime;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
									sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(costtime1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        costtime1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									iid=iid+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
								
									pst.setString(1,Patient.getString("DoctorName"));
									pst.setString(2,Patient.getString("InHospNo"));//处方号
									pst.setInt(3,iid);//自增长字段
									pst.setString(4,"Y00000000010");//itemcode
									pst.setInt(5,1);//is_use
									pst.setString(6,"");//drugform
									pst.setString(7,"审核/调配药师");//pharmacists
									pst.setString(8,"急诊外科_医疗组");//itemname
									pst.setString(9,"");//routecode
									pst.setString(10,"");//drugsccj
									pst.setString(11,Patient.getString("DeptCode"));//deptcode
									pst.setString(12,"心血管内科_医疗组");//medgroupname
									pst.setInt(13,10);//itemnum
									pst.setString(14,"核对/发药药师");//pharmacists_
									pst.setString(15,Patient.getString("DeptName"));//deptname
									pst.setString(16,caseid);//caseid
									pst.setString(17,Patient.getString("DoctorCode"));//doctorcode
									pst.setInt(18,0);//drugindex
									pst.setInt(19,196);//medgroupcode
									pst.setInt(20,100);//cost
									pst.setString(21,costtime1);//costtime
									pst.setString(22,"");//drugspec
									pst.setString(23,Patient.getString("PatCode"));//patientid
									pst.setInt(24,i*anlilist.size()+(j+1));//prescno
									pst.setString(25,PassClient.getString("HospID"));//hiscode
									pst.setString(26,"");//itemunit
									pst.setInt(27,3);//costtype
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_cost总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_cost总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							//-------插入药品费用
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_cost (doctorname, clinicno, iid, itemcode, is_use, drugform, pharmacists, "
									+ "itemname, routecode, drugsccj, deptcode, medgroupname, itemnum, pharmacists_, deptname, "
									+ "caseid, doctorcode, drugindex, medgroupcode, cost, costtime, drugspec, patientid, prescno, "
									+ "hiscode, itemunit, costtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							a=0;
							b=0;
							costtime1=costtime;
							ienddate1=ienddate;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(costtime1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        costtime1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
									JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenDrugs.size();k++){
										a=a+1;
										iid=iid+1;
										JSONObject ScreenDrug=ScreenDrugs.getJSONObject(k);
										
										//字典表找数据
										sql="select drugspec,drugform,comp_name,doseunit from mc_dict_drug_pass where drug_unique_code="
												+ "'"+ScreenDrug.getString("DrugUniqueCode")+"'";
										st=passmysqlconn.createStatement();
										rs=st.executeQuery(sql);
										List list_drug_pass=passmysql.getlist(rs);
										String drugspec="";
										String drugform="";
										String comp_name="";
										String doseunit="";
										for(int k1=0;k1<list_drug_pass.size();k1++){
											if(k1==1){
												break;
											}
											Map map=(Map)list_drug_pass.get(k1);
											if(map.get("drugspec")!=null){
												drugspec=map.get("drugspec").toString();
											}
											if(map.get("drugform")!=null){
												drugform=map.get("drugform").toString();
											}
											if(map.get("comp_name")!=null){
												comp_name=map.get("comp_name").toString();
											}
											if(map.get("doseunit")!=null){
												doseunit=map.get("doseunit").toString();
											}
										}
										
										pst.setString(1,ScreenDrug.getString("DoctorName"));
										pst.setString(2,Patient.getString("InHospNo"));//clinicno
										pst.setInt(3,iid);//自增长字段
										pst.setString(4,"Y00000000010");//itemcode
										pst.setInt(5,1);//is_use
										pst.setString(6,drugform);//drugform
										pst.setString(7,"审核/调配药师");//pharmacists
										pst.setString(8,"急诊外科_医疗组");//itemname
										pst.setString(9,ScreenDrug.getString("RouteCode"));//routecode
										pst.setString(10,comp_name);//drugsccj
										pst.setString(11,ScreenDrug.getString("DeptCode"));//deptcode
										pst.setString(12,"心血管内科_医疗组");//medgroupname
										pst.setInt(13,10);//itemnum
										pst.setString(14,"核对/发药药师");//pharmacists_
										pst.setString(15,ScreenDrug.getString("DeptName"));//deptname
										pst.setString(16,caseid);//caseid
										pst.setString(17,ScreenDrug.getString("DoctorCode"));//doctorcode
										pst.setString(18,ScreenDrug.getString("Index"));//drugindex
										pst.setInt(19,196);//medgroupcode
										if(StringUtils.isBlank(ScreenDrug.getString("Cost"))){
											pst.setInt(20,100);//cost
										}else{
											pst.setString(20,ScreenDrug.getString("Cost"));//cost
										}
										pst.setString(21,costtime1);//costtime
										pst.setString(22,drugspec);//drugspec
										pst.setString(23,Patient.getString("PatCode"));//patientid
										pst.setInt(24,i*anlilist.size()+(j+1));//prescno
										pst.setString(25,PassClient.getString("HospID"));//hiscode
										pst.setString(26,doseunit);//itemunit
										pst.setInt(27,1);//costtype
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_cost总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_cost总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							rs.close();
							pst.close();
							passmysqlconn.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_cost制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_disease数据
							if(trunca==1){
								sql="truncate table t_mc_clinic_disease";
							st=oraclesqlconn.createStatement();
							st.execute(sql);
							}
							
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_disease (discode, patientid, prescno, clinicno, hiscode, disname, caseid) "
									+ "values(?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenMedCondList=json.getJSONObject("ScreenMedCondList");
									JSONArray ScreenMedConds=ScreenMedCondList.getJSONArray("ScreenMedConds");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenMedConds.size();k++){
										JSONObject ScreenMedCond=ScreenMedConds.getJSONObject(k);
										if(StringUtils.isBlank(ScreenMedCond.getString("DiseaseName"))){
											continue;
										}
										a=a+1;
										pst.setString(1,ScreenMedCond.getString("DiseaseCode"));
										pst.setString(2,Patient.getString("PatCode"));
										pst.setInt(3,i*anlilist.size()+(j+1));
										pst.setString(4,Patient.getString("InHospNo"));
										pst.setString(5,PassClient.getString("HospID"));
										pst.setString(6,ScreenMedCond.getString("DiseaseName"));
										pst.setString(7,caseid);
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_disease总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_disease总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_disease制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_exam数据
							if (trunca == 1) {
								sql="truncate table t_mc_clinic_exam";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_exam (ienddate, doctorname, bodypart, doctorcode, examresult, clinicno, "
									+ "instrument, requestno, patientid, reporttime, hiscode, examname, examcode, caseid) "
									+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());	
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
										
									pst.setInt(1,Integer.parseInt(ienddate1));
									pst.setString(2,Patient.getString("DoctorName"));
									pst.setString(3,"检查部位");//bodypart
									pst.setString(4,Patient.getString("DoctorCode"));
									pst.setString(5,"检查结果");//examresult
									pst.setString(6,Patient.getString("InHospNo"));
									pst.setString(7,"检查仪器");//instrument
									pst.setString(8,"申请单号");//requestno
									pst.setString(9,Patient.getString("PatCode"));
									pst.setString(10,startdate1);//reporttime
									pst.setString(11,PassClient.getString("HospID"));
									pst.setString(12,"普通透射电镜检查与诊断组套");//examname
									pst.setString(13,"020050925006N");//examcode
									pst.setString(14,caseid);
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_exam总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_exam总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_exam制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_lab数据
							if (trunca == 1) {
								sql="truncate table t_mc_clinic_lab";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_lab (ienddate, doctorname, samplingtime, doctorcode, clinicno, labcode, "
									+ "resultflag, instrument, itemcode, requestno, dd, unit, sampletype, range_0, patientid, "
									+ "reporttime, itemname, labresult, hiscode, labname, caseid) values(?,?,?,?,?,?,?,?,?,?,?,?"
									+ ",?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									pst.setInt(1,Integer.parseInt(ienddate1));
									pst.setString(2,Patient.getString("DoctorName"));
									pst.setString(3,startdate1);//samplingtime
									pst.setString(4,Patient.getString("DoctorCode"));
									pst.setString(5,Patient.getString("InHospNo"));//clinicno
									pst.setString(6,"0200509240004");//labcode
									pst.setString(7,"H");//resultflag
									pst.setString(8,"检验仪器");//instrument
									pst.setString(9,"01.0101");//itemcode
									pst.setString(10,"20140401000001");//requestno
									pst.setString(11,"");//dd
									pst.setString(12,"mg");//unit
									pst.setString(13,"样本类型");//sampletype
									pst.setString(14,"22.0~55.0");//range_0
									pst.setString(15,Patient.getString("PatCode"));//patientid
									pst.setString(16,startdate1);//reporttime
									pst.setString(17,"脑池穿刺术");//itemname
									pst.setString(18,"检验结果");//labresult
									pst.setString(19,PassClient.getString("HospID"));//hiscode
									pst.setString(20,"血清总胆固醇测定");//labname
									pst.setString(21,caseid);//caseid
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_lab总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_lab总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_lab制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						PassMysqlconn passmysql=new PassMysqlconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							Connection passmysqlconn=passmysql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_order数据
							if(trunca==1){
								sql="truncate table t_mc_clinic_order";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_order (grouptag, orderstatus, doctorname, remark, clinicno, orderno, purpose,"
									+ " singledose, frequency, drugform, routecode, deptcode, ordercode, deptname, caseid, "
									+ "reasonable_desc, doctorcode, drug_unique_code, startdatetime, ordertype, presctype, "
									+ "routename, cost, cid, drugspec, num, days, patientid, prescno, hiscode, ordername, "
									+ "groupstate, doseunit, numunit, is_allow) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
									+ ",?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
									JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									
									for(int k=0;k<ScreenDrugs.size();k++){
										a=a+1;
										JSONObject ScreenDrug=ScreenDrugs.getJSONObject(k);
										
										//字典表找数据
										sql="select drugspec,drugform,comp_name,doseunit,drugcode from mc_dict_drug_pass where drug_unique_code="
												+ "'"+ScreenDrug.getString("DrugUniqueCode")+"'";
										st=passmysqlconn.createStatement();
										rs=st.executeQuery(sql);
										List list_drug_pass=passmysql.getlist(rs);
										String drugspec="";
										String drugform="";
										String comp_name="";
										String doseunit="";
										String drugcode="";
										for(int k1=0;k1<list_drug_pass.size();k1++){
											if(k1==1){
												break;
											}
											Map map=(Map)list_drug_pass.get(k1);
											if(map.get("drugspec")!=null){
												drugspec=map.get("drugspec").toString();
											}
											if(map.get("drugform")!=null){
												drugform=map.get("drugform").toString();
											}
											if(map.get("comp_name")!=null){
												comp_name=map.get("comp_name").toString();
											}
											if(map.get("doseunit")!=null){
												doseunit=map.get("doseunit").toString();
											}
											if(map.get("drugcode")!=null){
												drugcode=map.get("drugcode").toString();
											}
										}
										
										pst.setString(1,ScreenDrug.getString("GroupTag"));//grouptag
										pst.setString(2,ScreenDrug.getString("OrderType"));//orderstatus
										pst.setString(3,ScreenDrug.getString("DoctorName"));//doctorname
										pst.setString(4,"");//remark
										pst.setString(5,Patient.getString("InHospNo"));//clinicno
										pst.setString(6,ScreenDrug.getString("OrderNo"));//orderno
										pst.setInt(7,3);//purpose
										pst.setString(8,ScreenDrug.getString("DosePerTime"));//singledose
										pst.setString(9,ScreenDrug.getString("Frequency"));//frequency
										pst.setString(10,drugform);//drugform
										pst.setString(11,ScreenDrug.getString("RouteCode"));//routecode
										pst.setString(12,ScreenDrug.getString("DeptCode"));//deptcode
										pst.setString(13,drugcode);//ordercode
										pst.setString(14,ScreenDrug.getString("DeptName"));//deptname
										pst.setString(15,caseid);//caseid
										pst.setString(16,"合理越权描述");//reasonable_desc
										pst.setString(17,ScreenDrug.getString("DoctorCode"));//doctorcode
										pst.setString(18,ScreenDrug.getString("DrugUniqueCode"));//drug_unique_code
										pst.setString(19,ScreenDrug.getString("StartTime"));//startdatetime
										pst.setInt(20,1);//ordertype
										pst.setInt(21,1);//presctype
										pst.setString(22,ScreenDrug.getString("RouteName"));//routename
										pst.setString(23,ScreenDrug.getString("Cost"));//cost
										pst.setString(24,Patient.getString("PatCode")+"_"+i+"_"+j+"#"+ScreenDrug.getString("Index"));//cid
										pst.setString(25,drugspec);//drugspec
										pst.setInt(26,10);//num
										pst.setInt(27,10);//days
										pst.setString(28,Patient.getString("PatCode"));//patientid
										pst.setInt(29,i*anlilist.size()+(j+1));//prescno
										pst.setString(30,PassClient.getString("HospID"));//hiscode
										pst.setString(31,ScreenDrug.getString("DrugName"));//ordername
										pst.setInt(32,1);//groupstate
										pst.setString(33,doseunit);//doseunit
										pst.setString(34,doseunit);//numunit
										pst.setInt(35,0);//is_allow
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_order总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_order总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							rs.close();
							pst.close();
							passmysqlconn.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_order制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_patient数据
							if(trunca==1){
								sql="truncate table t_mc_clinic_patient";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_clinic_patient (sex, doctorname, weight, clinicno, diagnosis, ren_damage, iage, "
									+ "is_emergency, height, payclass, deptcode, medgroupname, age, is_lact, birthdate, identitycard, "
									+ "deptname, caseid, ienddate, allergen, doctorcode, hep_damage, medgroupcode, is_preg, cost, "
									+ "preg_starttime, standby, patientname, patientid, hiscode, enddate, telephone) values(?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String enddate1=enddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<count;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());	
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd");
									time = sdf.parse(enddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        enddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									if(StringUtils.isBlank(Patient.getString("Sex"))){
										pst.setString(1,"无");//sex
									}else{
										pst.setString(1,Patient.getString("Sex"));//sex
									}
									pst.setString(2,Patient.getString("DoctorName"));//doctorname
									pst.setString(3,Patient.getString("WeighKG"));//weight
									pst.setString(4,Patient.getString("InHospNo"));//clinicno
									pst.setString(5,"");//diagnosis
									pst.setString(6,Patient.getString("RenDamageDegree"));//ren_damage
									pst.setInt(7,0);//iage
									pst.setInt(8,0);//is_emergency
									pst.setString(9,Patient.getString("HeightCM"));//height
									pst.setString(10,"自费");//payclass
									pst.setString(11,Patient.getString("DeptCode"));//deptcode
									pst.setString(12,"门诊心电图_医疗组");//medgroupname
									if(StringUtils.isBlank(Patient.getString("Age"))){
										pst.setInt(13,0);//age
									}else{
										pst.setString(13,Patient.getString("Age"));//age
									}
									pst.setString(14,Patient.getString("IsLactation"));//is_lact
									pst.setString(15,Patient.getString("Birthday"));//birthdate
									pst.setString(16,Patient.getString("IDCard"));//identitycard
									pst.setString(17,Patient.getString("DeptName"));//deptname
									pst.setString(18,caseid);//caseid
									pst.setInt(19,Integer.parseInt(ienddate1));//ienddate
									pst.setString(20,"");//allergen
									pst.setString(21,Patient.getString("DoctorCode"));//doctorcode
									pst.setString(22,Patient.getString("HepDamageDegree"));//hep_damage
									pst.setInt(23,3207);//medgroupcode
									pst.setString(24,Patient.getString("IsPregnancy"));//is_preg
									pst.setInt(25,100);//cost
									pst.setString(26,Patient.getString("PregStartDate"));//preg_starttime
									pst.setString(27,"");//standby
									pst.setString(28,Patient.getString("Name"));//patientname
									pst.setString(29,Patient.getString("PatCode"));//patientid
									pst.setString(30,PassClient.getString("HospID"));//hiscode
									pst.setString(31,enddate1);//enddate
									pst.setString(32,Patient.getString("Telephone"));//telephone
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_clinic_patient总数："+count*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==count){
									pst.executeBatch();
									System.out.println("t_mc_clinic_patient总数："+count*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_clinic_patient制造数据异常");
						}
					}
				}).start();
				
			}
			
			if(zy==1){
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_allergen数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_allergen";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_allergen (allercode, patientid, visitid, hiscode, allername, symptom, caseid"
									+ ") values(?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenAllergenList=json.getJSONObject("ScreenAllergenList");
									JSONArray ScreenAllergens=ScreenAllergenList.getJSONArray("ScreenAllergens");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenAllergens.size();k++){
										a=a+1;
										JSONObject ScreenAllergen=ScreenAllergens.getJSONObject(k);
										
										pst.setString(1,ScreenAllergen.getString("AllerCode"));//[allercode
										pst.setString(2,Patient.getString("PatCode"));//patientid
										pst.setInt(3,1);//visitid
										pst.setString(4,PassClient.getString("HospID"));//hiscode
										pst.setString(5,ScreenAllergen.getString("AllerName"));//allername
										pst.setString(6,"过敏症状");//symptom
										pst.setString(7,caseid);//caseid]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_allergen总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_allergen总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_allergen制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						PassMysqlconn passmysql=new PassMysqlconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							Connection passmysqlconn=passmysql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_cost数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_cost";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							//-------插入非药品费用
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_cost (doctorname, iid, wardcode, itemcode, is_use, drugform, is_out, routecode, "
									+ "itemname, visitid, drugsccj, deptcode, medgroupname, itemnum, deptname, caseid, doctorcode, "
									+ "drugindex, medgroupcode, cost, costtime, drugspec, wardname, patientid, hiscode, itemunit, "
									+ "costtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							int iid=0;
							String ienddate1=ienddate;
							String costtime1=costtime;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(countzy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(costtime1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        costtime1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									iid=iid+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
								
									pst.setString(1,Patient.getString("DoctorName"));//[doctorname
									pst.setInt(2,iid);//自增长字段
									pst.setString(3,Patient.getString("DeptCode"));//wardcode
									pst.setString(4,"Y00000000010");//itemcode
									pst.setInt(5,1);//is_use
									pst.setString(6,"");//drugform
									pst.setInt(7,0);//is_out
									pst.setString(8,"");//routecode
									pst.setString(9,"急诊外科_医疗组");//itemname
									pst.setInt(10,1);//visitid
									pst.setString(11,"");//drugsccj
									pst.setString(12,Patient.getString("DeptCode"));//deptcode
									pst.setString(13,"心血管内科_医疗组");//medgroupname
									pst.setInt(14,10);//itemnum
									pst.setString(15,Patient.getString("DeptName"));//deptname
									pst.setString(16,caseid);//caseid
									pst.setString(17,Patient.getString("DoctorCode"));//doctorcode
									pst.setInt(18,0);//drugindex
									pst.setInt(19,196);//medgroupcode
									pst.setInt(20,100);//cost
									pst.setString(21,costtime1);//costtime
									pst.setString(22,"");//drugspec
									pst.setString(23,Patient.getString("DeptName"));//wardname
									pst.setString(24,Patient.getString("PatCode"));//patientid
									pst.setString(25,PassClient.getString("HospID"));//hiscode
									pst.setString(26,"");//itemunit
									pst.setInt(27,3);//costtype
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_cost总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_cost总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							//-------插入药品费用
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_cost (doctorname, iid, wardcode, itemcode, is_use, drugform, is_out, routecode, "
									+ "itemname, visitid, drugsccj, deptcode, medgroupname, itemnum, deptname, caseid, doctorcode, "
									+ "drugindex, medgroupcode, cost, costtime, drugspec, wardname, patientid, hiscode, itemunit, "
									+ "costtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							a=0;
							b=0;
							ienddate1=ienddate;
							costtime1=costtime;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(countzy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(costtime1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        costtime1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
									JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenDrugs.size();k++){
										a=a+1;
										iid=iid+1;
										JSONObject ScreenDrug=ScreenDrugs.getJSONObject(k);
										
										//字典表找数据
										sql="select drugspec,drugform,comp_name,doseunit from mc_dict_drug_pass where drug_unique_code="
												+ "'"+ScreenDrug.getString("DrugUniqueCode")+"'";
										st=passmysqlconn.createStatement();
										rs=st.executeQuery(sql);
										List list_drug_pass=passmysql.getlist(rs);
										String drugspec="";
										String drugform="";
										String comp_name="";
										String doseunit="";
										for(int k1=0;k1<list_drug_pass.size();k1++){
											if(k1==1){
												break;
											}
											Map map=(Map)list_drug_pass.get(k1);
											if(map.get("drugspec")!=null){
												drugspec=map.get("drugspec").toString();
											}
											if(map.get("drugform")!=null){
												drugform=map.get("drugform").toString();
											}
											if(map.get("comp_name")!=null){
												comp_name=map.get("comp_name").toString();
											}
											if(map.get("doseunit")!=null){
												doseunit=map.get("doseunit").toString();
											}
										}
										
										pst.setString(1,ScreenDrug.getString("DoctorName"));//doctorname
										pst.setInt(2,iid);//自增长字段
										pst.setString(3,Patient.getString("DeptCode"));//wardcode
										pst.setString(4,"Y00000000010");//itemcode
										pst.setInt(5,1);//is_use
										pst.setString(6,drugform);//drugform
										pst.setInt(7,0);//is_out
										pst.setString(8,ScreenDrug.getString("RouteCode"));//routecode
										pst.setString(9,"急诊外科_医疗组");//itemname
										pst.setInt(10,1);//visitid
										pst.setString(11,comp_name);//drugsccj
										pst.setString(12,ScreenDrug.getString("DeptCode"));//deptcode
										pst.setString(13,"心血管内科_医疗组");//medgroupname
										pst.setInt(14,10);//itemnum
										pst.setString(15,ScreenDrug.getString("DeptName"));//deptname
										pst.setString(16,caseid);//caseid
										pst.setString(17,ScreenDrug.getString("DoctorCode"));//doctorcode
										pst.setString(18,ScreenDrug.getString("Index"));//drugindex
										pst.setInt(19,196);//medgroupcode
										if(StringUtils.isBlank(ScreenDrug.getString("Cost"))){
											pst.setInt(20,100);//cost
										}else{
											pst.setString(20,ScreenDrug.getString("Cost"));//cost
										}
										pst.setString(21,costtime1);//costtime
										pst.setString(22,drugspec);//drugspec
										pst.setString(23,Patient.getString("DeptName"));//wardname
										pst.setString(24,Patient.getString("PatCode"));//patientid
										pst.setString(25,PassClient.getString("HospID"));//hiscode
										pst.setString(26,doseunit);//itemunit
										pst.setInt(27,1);//costtype
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_cost总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_cost总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							rs.close();
							pst.close();
							passmysqlconn.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_cost制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_disease数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_disease";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_disease (discode, patientid, diseasetype, visitid, treatment, is_hospinfection, "
									+ "hiscode, disname, is_main, caseid) "
									+ "values(?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenMedCondList=json.getJSONObject("ScreenMedCondList");
									JSONArray ScreenMedConds=ScreenMedCondList.getJSONArray("ScreenMedConds");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenMedConds.size();k++){
										JSONObject ScreenMedCond=ScreenMedConds.getJSONObject(k);
										if(StringUtils.isBlank(ScreenMedCond.getString("DiseaseName"))){
											continue;
										}
										a=a+1;
										pst.setString(1,ScreenMedCond.getString("DiseaseCode"));//[discode
										pst.setString(2,Patient.getString("PatCode"));//patientid
										pst.setInt(3,1);//diseasetype
										pst.setInt(4,1);//visitid
										pst.setInt(5,0);//treatment
										pst.setInt(6,0);//is_hospinfection
										pst.setString(7,PassClient.getString("HospID"));//hiscode
										pst.setString(8,ScreenMedCond.getString("DiseaseName"));//disname
										pst.setInt(9,1);//is_main
										pst.setString(10,caseid);//caseid]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_disease总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_disease总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_disease制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_exam数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_exam";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_exam (ienddate, doctorname, bodypart, doctorcode, examresult, instrument, "
									+ "requestno, patientid, visitid, reporttime, hiscode, examname, examcode, caseid) "
									+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(countzy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
										
									pst.setInt(1,Integer.parseInt(ienddate1));
									pst.setString(2,Patient.getString("DoctorName"));
									pst.setString(3,"检查部位");//bodypart
									pst.setString(4,Patient.getString("DoctorCode"));
									pst.setString(5,"检查结果");//examresult
									pst.setString(6,Patient.getString("InHospNo"));
									pst.setString(7,"检查仪器");//instrument
									pst.setString(8,"申请单号");//requestno
									pst.setString(9,Patient.getString("PatCode"));
									pst.setString(10,startdate1);//reporttime
									pst.setString(11,PassClient.getString("HospID"));
									pst.setString(12,"普通透射电镜检查与诊断组套");//examname
									pst.setString(13,"020050925006N");//examcode
									pst.setString(14,caseid);
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_exam总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_exam总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_exam制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_lab数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_lab";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_lab (ienddate, doctorname, samplingtime, doctorcode, labcode, "
									+ "resultflag, instrument, itemcode, requestno, unit, sampletype, range_0, patientid, "
									+ "reporttime, visitid, itemname, labresult, hiscode, labname, caseid) values(?,?,?,?,?,?,?,?,?,?,?,?"
									+ ",?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(countzy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									pst.setInt(1,Integer.parseInt(ienddate1));
									pst.setString(2,Patient.getString("DoctorName"));
									pst.setString(3,startdate1);//samplingtime
									pst.setString(4,Patient.getString("DoctorCode"));
									pst.setString(5,"0200509240004");//labcode
									pst.setString(6,"H");//resultflag
									pst.setString(7,"检验仪器");//instrument
									pst.setString(8,"01.0101");//itemcode
									pst.setString(9,"20140401000001");//requestno
									pst.setString(10,"mg");//unit
									pst.setString(11,"样本类型");//sampletype
									pst.setString(12,"22.0~55.0");//range_0
									pst.setString(13,Patient.getString("PatCode"));//patientid
									pst.setString(14,startdate1);//reporttime
									pst.setInt(15,1);
									pst.setString(16,"脑池穿刺术");//itemname
									pst.setString(17,"检验结果");//labresult
									pst.setString(18,PassClient.getString("HospID"));//hiscode
									pst.setString(19,"血清总胆固醇测定");//labname
									pst.setString(20,caseid);//caseid
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_lab总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_lab总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_lab制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_operation数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_operation";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_operation (doctorname, doctorcode, startdate, operationname, patientid, visitid, "
									+ "deptcode, incisiontype, oprid, hiscode, enddate, deptname, operationcode, caseid) values(?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							int iid=0;
							String ienddate1=ienddate;
							String enddate1=enddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(countzy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd");
									time = sdf.parse(enddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        enddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenOperationList=json.getJSONObject("ScreenOperationList");
									JSONArray ScreenOperations=ScreenOperationList.getJSONArray("ScreenOperations");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									
									for(int k=0;k<ScreenOperations.size();k++){
										JSONObject ScreenOperation=ScreenOperations.getJSONObject(k);
										if(StringUtils.isBlank(ScreenOperation.getString("OprName"))){
											continue;
										}
										a=a+1;
										iid=iid+1;
										pst.setString(1,Patient.getString("DoctorName"));//[doctorname
										pst.setString(2,Patient.getString("DoctorCode"));//doctorcode
										pst.setString(3,ScreenOperation.getString("OprStartDate"));//startdate
										pst.setString(4,ScreenOperation.getString("OprName"));//operationname
										pst.setString(5,Patient.getString("PatCode"));//patientid
										pst.setInt(6,1);//visitid
										pst.setString(7,Patient.getString("DeptCode"));//deptcode
										pst.setInt(8,1);//incisiontype
										pst.setInt(9,iid);//oprid
										pst.setString(10,PassClient.getString("HospID"));//hiscode
										pst.setString(11,ScreenOperation.getString("OprEndDate"));//enddate
										pst.setString(12,Patient.getString("DeptName"));//deptname
										pst.setString(13,ScreenOperation.getString("OprCode"));//operationcode
										pst.setString(14,caseid);//caseid]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_operation总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_operation总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_operation制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						PassMysqlconn passmysql=new PassMysqlconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							Connection passmysqlconn=passmysql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_order数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_order";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_order (grouptag, orderstatus, doctorname, is_temp, remark, pa_enddatetime, "
									+ "orderno, wardcode, purpose, singledose, frequency, drugform, is_out, visitid, routecode, "
									+ "deptcode, ordercode, deptname, caseid, reasonable_desc, doctorcode, meditime, drug_unique_code, "
									+ "startdatetime, ordertype, routename, enddatetime, cid, drugspec, executetime, wardname, "
									+ "patientid, hiscode, ordername, groupstate, doseunit, is_allow) values(?,?,?,?,?,?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
									JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									
									for(int k=0;k<ScreenDrugs.size();k++){
										a=a+1;
										JSONObject ScreenDrug=ScreenDrugs.getJSONObject(k);
										
										//字典表找数据
										sql="select drugspec,drugform,comp_name,doseunit,drugcode from mc_dict_drug_pass where drug_unique_code="
												+ "'"+ScreenDrug.getString("DrugUniqueCode")+"'";
										st=passmysqlconn.createStatement();
										rs=st.executeQuery(sql);
										List list_drug_pass=passmysql.getlist(rs);
										String drugspec="";
										String drugform="";
										String comp_name="";
										String doseunit="";
										String drugcode="";
										for(int k1=0;k1<list_drug_pass.size();k1++){
											if(k1==1){
												break;
											}
											Map map=(Map)list_drug_pass.get(k1);
											if(map.get("drugspec")!=null){
												drugspec=map.get("drugspec").toString();
											}
											if(map.get("drugform")!=null){
												drugform=map.get("drugform").toString();
											}
											if(map.get("comp_name")!=null){
												comp_name=map.get("comp_name").toString();
											}
											if(map.get("doseunit")!=null){
												doseunit=map.get("doseunit").toString();
											}
											if(map.get("drugcode")!=null){
												drugcode=map.get("drugcode").toString();
											}
										}
										
										pst.setString(1,ScreenDrug.getString("GroupTag"));//[grouptag
										pst.setInt(2,1);//orderstatus
										pst.setString(3,ScreenDrug.getString("DoctorName"));//doctorname
										pst.setString(4,ScreenDrug.getString("IsTempDrug"));//is_temp
										pst.setString(5,"");//remark
										pst.setString(6,startdate1);//pa_enddatetime
										pst.setString(7,ScreenDrug.getString("Index"));//orderno
										pst.setString(8,Patient.getString("DeptCode"));//wardcode
										pst.setInt(9,0);//purpose
										pst.setString(10,ScreenDrug.getString("DosePerTime"));//singledose
										pst.setString(11,ScreenDrug.getString("Frequency"));//frequency
										pst.setString(12,drugform);//drugform
										pst.setInt(13,1);//is_out
										pst.setInt(14,1);//visitid
										pst.setString(15,ScreenDrug.getString("RouteCode"));//routecode
										pst.setString(16,ScreenDrug.getString("DeptCode"));//deptcode
										pst.setString(17,drugcode);//ordercode
										pst.setString(18,ScreenDrug.getString("DeptName"));//deptname
										pst.setString(19,caseid);//caseid
										pst.setString(20,"合理越权描述");//reasonable_desc
										pst.setString(21,ScreenDrug.getString("DoctorCode"));//doctorcode
										pst.setInt(22,0);//meditime
										pst.setString(23,ScreenDrug.getString("DrugUniqueCode"));//drug_unique_code
										pst.setString(24,ScreenDrug.getString("StartTime"));//startdatetime
										pst.setInt(25,1);//ordertype
										pst.setString(26,ScreenDrug.getString("RouteName"));//routename
										pst.setString(27,ScreenDrug.getString("EndTime"));//enddatetime
										pst.setString(28,Patient.getString("PatCode")+"_"+i+"_"+j+"#"+ScreenDrug.getString("Index"));//cid
										pst.setString(29,drugspec);//drugspec
										pst.setString(30,ScreenDrug.getString("ExecuteTime"));//executetime
										pst.setString(31,Patient.getString("DeptName"));//wardname
										pst.setString(32,Patient.getString("PatCode"));//patientid
										pst.setString(33,PassClient.getString("HospID"));//hiscode
										pst.setString(34,ScreenDrug.getString("DrugName"));//ordername
										pst.setInt(35,1);//groupstate
										pst.setString(36,doseunit);//doseunit
										pst.setInt(37,0);//is_allow]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_order总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_order总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							rs.close();
							pst.close();
							passmysqlconn.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_order制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_clinic_patient数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_patient";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_patient (sex, bedno, doctorname, in_diagnosis, weight, firstdeptname, ren_damage, "
									+ "iage, wardcode, i_in, height, hospitalno, payclass, visitid, deptcode, medgroupname, age, is_lact, "
									+ "birthdate, identitycard, deptname, caseid, incisiontypes, allergen, doctorcode, hep_damage, "
									+ "medgroupcode, is_preg, cost, preg_starttime, nursingclass, wardname, startdate, standby, "
									+ "patientname, patientid, hiscode, operations, enddate, accountdate, telephone) values(?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String enddate1=enddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(countzy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd");
									time = sdf.parse(enddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        enddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									if(StringUtils.isBlank(Patient.getString("Sex"))){
										pst.setString(1,"无");//sex
									}else{
										pst.setString(1,Patient.getString("Sex"));//sex
									}
									pst.setInt(2,1);//bedno
									pst.setString(3,Patient.getString("DoctorName"));//doctorname
									pst.setString(4,"");//in_diagnosis
									pst.setString(5,Patient.getString("WeighKG"));//weight
									pst.setString(6,Patient.getString("DeptName"));//firstdeptname
									pst.setString(7,Patient.getString("RenDamageDegree"));//ren_damage
									pst.setInt(8,0);//iage
									pst.setString(9,Patient.getString("DeptCode"));//wardcode
									pst.setInt(10,0);//i_in
									pst.setString(11,Patient.getString("HeightCM"));//height
									pst.setString(12,PassClient.getString("HospID"));//hospitalno
									pst.setString(13,"自费");//payclass
									pst.setInt(14,1);//visitid
									pst.setString(15,Patient.getString("DeptCode"));//deptcode
									pst.setString(16,"门诊心电图_医疗组");//medgroupname
									if(StringUtils.isBlank(Patient.getString("Age"))){
										pst.setInt(17,0);//age
									}else{
										pst.setString(17,Patient.getString("Age"));//age
									}
									pst.setString(18,Patient.getString("IsLactation"));//is_lact
									pst.setString(19,Patient.getString("Birthday"));//birthdate
									pst.setString(20,Patient.getString("IDCard"));//identitycard
									pst.setString(21,Patient.getString("DeptName"));//deptname
									pst.setString(22,caseid);//caseid
									pst.setString(23,"切口类型串");//incisiontypes
									pst.setString(24,"");//allergen
									pst.setString(25,Patient.getString("DoctorCode"));//doctorcode
									pst.setString(26,Patient.getString("HepDamageDegree"));//hep_damage
									pst.setInt(27,3207);//medgroupcode
									pst.setString(28,Patient.getString("IsPregnancy"));//is_preg
									pst.setInt(29,100);//cost
									pst.setString(30,Patient.getString("PregStartDate"));//preg_starttime
									pst.setInt(31,1);//nursingclass
									pst.setString(32,Patient.getString("DeptName"));//wardname
									pst.setString(33,startdate1);//startdate
									pst.setString(34,"");//standby
									pst.setString(35,Patient.getString("Name"));//patientname
									pst.setString(36,Patient.getString("PatCode"));//patientid
									pst.setString(37,PassClient.getString("HospID"));//hiscode
									pst.setString(38,"手术列表串");//operations
									pst.setString(39,enddate1);//enddate
									pst.setString(40,"");//accountdate
									pst.setString(41,Patient.getString("Telephone"));//telephone]
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_patient总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_patient总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_patient制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_inhosp_temperature数据
							if(trunca==1){
								sql="truncate table t_mc_inhosp_temperature";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_inhosp_temperature (patientid, visitid, hiscode, taketime, temperature, caseid) "
									+ "values(?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countzy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									pst.setString(1,Patient.getString("PatCode"));//[patientid
									pst.setInt(2,1);//visitid
									pst.setString(3,PassClient.getString("HospID"));//hiscode
									pst.setString(4,startdate1);//taketime
									pst.setString(5,"36.30");//temperature
									pst.setString(6,caseid);//caseid]
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_inhosp_temperature总数："+countzy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countzy){
									pst.executeBatch();
									System.out.println("t_mc_inhosp_temperature总数："+countzy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_inhosp_temperature制造数据异常");
						}
					}
				}).start();
				
			}
			
			if(cy==1){
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_allergen数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_allergen";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_allergen (allercode, patientid, visitid, hiscode, allername, symptom, caseid"
									+ ") values(?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenAllergenList=json.getJSONObject("ScreenAllergenList");
									JSONArray ScreenAllergens=ScreenAllergenList.getJSONArray("ScreenAllergens");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenAllergens.size();k++){
										a=a+1;
										JSONObject ScreenAllergen=ScreenAllergens.getJSONObject(k);
										
										pst.setString(1,ScreenAllergen.getString("AllerCode"));//[allercode
										pst.setString(2,Patient.getString("PatCode"));//patientid
										pst.setInt(3,1);//visitid
										pst.setString(4,PassClient.getString("HospID"));//hiscode
										pst.setString(5,ScreenAllergen.getString("AllerName"));//allername
										pst.setString(6,"过敏症状");//symptom
										pst.setString(7,caseid);//caseid]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_allergen总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_allergen总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_allergen制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						PassMysqlconn passmysql=new PassMysqlconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							Connection passmysqlconn=passmysql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_cost数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_cost";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							//-------插入非药品费用
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_cost (doctorname, iid, wardcode, itemcode, is_use, drugform, is_out, routecode, "
									+ "itemname, visitid, drugsccj, deptcode, medgroupname, itemnum, deptname, caseid, doctorcode, "
									+ "drugindex, medgroupcode, cost, costtime, drugspec, wardname, patientid, hiscode, itemunit, "
									+ "costtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							int iid=0;
							String ienddate1=ienddate;
							String costtime1=costtime;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(countcy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(costtime1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        costtime1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									iid=iid+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
								
									pst.setString(1,Patient.getString("DoctorName"));//[doctorname
									pst.setInt(2,iid);//自增长字段
									pst.setString(3,Patient.getString("DeptCode"));//wardcode
									pst.setString(4,"Y00000000010");//itemcode
									pst.setInt(5,1);//is_use
									pst.setString(6,"");//drugform
									pst.setInt(7,0);//is_out
									pst.setString(8,"");//routecode
									pst.setString(9,"急诊外科_医疗组");//itemname
									pst.setInt(10,1);//visitid
									pst.setString(11,"");//drugsccj
									pst.setString(12,Patient.getString("DeptCode"));//deptcode
									pst.setString(13,"心血管内科_医疗组");//medgroupname
									pst.setInt(14,10);//itemnum
									pst.setString(15,Patient.getString("DeptName"));//deptname
									pst.setString(16,caseid);//caseid
									pst.setString(17,Patient.getString("DoctorCode"));//doctorcode
									pst.setInt(18,0);//drugindex
									pst.setInt(19,196);//medgroupcode
									pst.setInt(20,100);//cost
									pst.setString(21,costtime1);//costtime
									pst.setString(22,"");//drugspec
									pst.setString(23,Patient.getString("DeptName"));//wardname
									pst.setString(24,Patient.getString("PatCode"));//patientid
									pst.setString(25,PassClient.getString("HospID"));//hiscode
									pst.setString(26,"");//itemunit
									pst.setInt(27,3);//costtype
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_cost总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_cost总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							//-------插入药品费用
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_cost (doctorname, iid, wardcode, itemcode, is_use, drugform, is_out, routecode, "
									+ "itemname, visitid, drugsccj, deptcode, medgroupname, itemnum, deptname, caseid, doctorcode, "
									+ "drugindex, medgroupcode, cost, costtime, drugspec, wardname, patientid, hiscode, itemunit, "
									+ "costtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							a=0;
							b=0;
							ienddate1=ienddate;
							costtime1=costtime;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(countcy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(costtime1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        costtime1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
									JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenDrugs.size();k++){
										a=a+1;
										iid=iid+1;
										JSONObject ScreenDrug=ScreenDrugs.getJSONObject(k);
										
										//字典表找数据
										sql="select drugspec,drugform,comp_name,doseunit from mc_dict_drug_pass where drug_unique_code="
												+ "'"+ScreenDrug.getString("DrugUniqueCode")+"'";
										st=passmysqlconn.createStatement();
										rs=st.executeQuery(sql);
										List list_drug_pass=passmysql.getlist(rs);
										String drugspec="";
										String drugform="";
										String comp_name="";
										String doseunit="";
										for(int k1=0;k1<list_drug_pass.size();k1++){
											if(k1==1){
												break;
											}
											Map map=(Map)list_drug_pass.get(k1);
											if(map.get("drugspec")!=null){
												drugspec=map.get("drugspec").toString();
											}
											if(map.get("drugform")!=null){
												drugform=map.get("drugform").toString();
											}
											if(map.get("comp_name")!=null){
												comp_name=map.get("comp_name").toString();
											}
											if(map.get("doseunit")!=null){
												doseunit=map.get("doseunit").toString();
											}
										}
										
										pst.setString(1,ScreenDrug.getString("DoctorName"));//doctorname
										pst.setInt(2,iid);//自增长字段
										pst.setString(3,Patient.getString("DeptCode"));//wardcode
										pst.setString(4,"Y00000000010");//itemcode
										pst.setInt(5,1);//is_use
										pst.setString(6,drugform);//drugform
										pst.setInt(7,0);//is_out
										pst.setString(8,ScreenDrug.getString("RouteCode"));//routecode
										pst.setString(9,"急诊外科_医疗组");//itemname
										pst.setInt(10,1);//visitid
										pst.setString(11,comp_name);//drugsccj
										pst.setString(12,ScreenDrug.getString("DeptCode"));//deptcode
										pst.setString(13,"心血管内科_医疗组");//medgroupname
										pst.setInt(14,10);//itemnum
										pst.setString(15,ScreenDrug.getString("DeptName"));//deptname
										pst.setString(16,caseid);//caseid
										pst.setString(17,ScreenDrug.getString("DoctorCode"));//doctorcode
										if(ScreenDrug.getString("Index")==null || "".equals(ScreenDrug.getString("Index"))){
											pst.setInt(18,k+1);//drugindex
										}else{
											pst.setString(18,ScreenDrug.getString("Index"));//drugindex
										}
										
										pst.setInt(19,196);//medgroupcode
										if(StringUtils.isBlank(ScreenDrug.getString("Cost"))){
											pst.setInt(20,100);//cost
										}else{
											pst.setString(20,ScreenDrug.getString("Cost"));//cost
										}
										pst.setString(21,costtime1);//costtime
										pst.setString(22,drugspec);//drugspec
										pst.setString(23,Patient.getString("DeptName"));//wardname
										pst.setString(24,Patient.getString("PatCode"));//patientid
										pst.setString(25,PassClient.getString("HospID"));//hiscode
										pst.setString(26,doseunit);//itemunit
										pst.setInt(27,1);//costtype
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_cost总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_cost总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							rs.close();
							pst.close();
							passmysqlconn.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_cost制造数据异常"+e);
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_disease数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_disease";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_disease (discode, patientid, diseasetype, visitid, treatment, is_hospinfection, "
									+ "hiscode, disname, is_main, caseid) "
									+ "values(?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenMedCondList=json.getJSONObject("ScreenMedCondList");
									JSONArray ScreenMedConds=ScreenMedCondList.getJSONArray("ScreenMedConds");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									for(int k=0;k<ScreenMedConds.size();k++){
										JSONObject ScreenMedCond=ScreenMedConds.getJSONObject(k);
										if(StringUtils.isBlank(ScreenMedCond.getString("DiseaseName"))){
											continue;
										}
										a=a+1;
										pst.setString(1,ScreenMedCond.getString("DiseaseCode"));//[discode
										pst.setString(2,Patient.getString("PatCode"));//patientid
										pst.setInt(3,1);//diseasetype
										pst.setInt(4,1);//visitid
										pst.setInt(5,0);//treatment
										pst.setInt(6,0);//is_hospinfection
										pst.setString(7,PassClient.getString("HospID"));//hiscode
										pst.setString(8,ScreenMedCond.getString("DiseaseName"));//disname
										pst.setInt(9,1);//is_main
										pst.setString(10,caseid);//caseid]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_disease总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_disease总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_disease制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_exam数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_exam";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_exam (ienddate, doctorname, bodypart, doctorcode, examresult, instrument, "
									+ "requestno, patientid, visitid, reporttime, hiscode, examname, examcode, caseid) "
									+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(countcy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());	

							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
										
									pst.setInt(1,Integer.parseInt(ienddate1));
									pst.setString(2,Patient.getString("DoctorName"));
									pst.setString(3,"检查部位");//bodypart
									pst.setString(4,Patient.getString("DoctorCode"));
									pst.setString(5,"检查结果");//examresult
									pst.setString(6,Patient.getString("InHospNo"));
									pst.setString(7,"检查仪器");//instrument
									pst.setString(8,"申请单号");//requestno
									pst.setString(9,Patient.getString("PatCode"));
									pst.setString(10,startdate1);//reporttime
									pst.setString(11,PassClient.getString("HospID"));
									pst.setString(12,"普通透射电镜检查与诊断组套");//examname
									pst.setString(13,"020050925006N");//examcode
									pst.setString(14,caseid);
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_exam总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_exam总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_exam制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_lab数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_lab";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_lab (ienddate, doctorname, samplingtime, doctorcode, labcode, "
									+ "resultflag, instrument, itemcode, requestno, unit, sampletype, range_0, patientid, "
									+ "reporttime, visitid, itemname, labresult, hiscode, labname, caseid) values(?,?,?,?,?,?,?,?,?,?,?,?"
									+ ",?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(countcy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());	
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									pst.setInt(1,Integer.parseInt(ienddate1));
									pst.setString(2,Patient.getString("DoctorName"));
									pst.setString(3,startdate1);//samplingtime
									pst.setString(4,Patient.getString("DoctorCode"));
									pst.setString(5,"0200509240004");//labcode
									pst.setString(6,"H");//resultflag
									pst.setString(7,"检验仪器");//instrument
									pst.setString(8,"01.0101");//itemcode
									pst.setString(9,"20140401000001");//requestno
									pst.setString(10,"mg");//unit
									pst.setString(11,"样本类型");//sampletype
									pst.setString(12,"22.0~55.0");//range_0
									pst.setString(13,Patient.getString("PatCode"));//patientid
									pst.setString(14,startdate1);//reporttime
									pst.setInt(15,1);
									pst.setString(16,"脑池穿刺术");//itemname
									pst.setString(17,"检验结果");//labresult
									pst.setString(18,PassClient.getString("HospID"));//hiscode
									pst.setString(19,"血清总胆固醇测定");//labname
									pst.setString(20,caseid);//caseid
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_lab总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_lab总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_lab制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn=oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_operation数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_operation";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_operation (doctorname, doctorcode, startdate, operationname, patientid, visitid, "
									+ "deptcode, incisiontype, oprid, hiscode, enddate, deptname, operationcode, caseid) values(?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							int iid=0;
							String ienddate1=ienddate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenOperationList=json.getJSONObject("ScreenOperationList");
									JSONArray ScreenOperations=ScreenOperationList.getJSONArray("ScreenOperations");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									
									for(int k=0;k<ScreenOperations.size();k++){
										JSONObject ScreenOperation=ScreenOperations.getJSONObject(k);
										if(StringUtils.isBlank(ScreenOperation.getString("OprName"))){
											continue;
										}
										a=a+1;
										iid=iid+1;
										pst.setString(1,Patient.getString("DoctorName"));//[doctorname
										pst.setString(2,Patient.getString("DoctorCode"));//doctorcode
										pst.setString(3,ScreenOperation.getString("OprStartDate"));//startdate
										pst.setString(4,ScreenOperation.getString("OprName"));//operationname
										pst.setString(5,Patient.getString("PatCode"));//patientid
										pst.setInt(6,1);//visitid
										pst.setString(7,Patient.getString("DeptCode"));//deptcode
										pst.setInt(8,1);//incisiontype
										pst.setInt(9,iid);//oprid
										pst.setString(10,PassClient.getString("HospID"));//hiscode
										pst.setString(11,ScreenOperation.getString("OprEndDate"));//enddate
										pst.setString(12,Patient.getString("DeptName"));//deptname
										pst.setString(13,ScreenOperation.getString("OprCode"));//operationcode
										pst.setString(14,caseid);//caseid]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_operation总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_operation总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_operation制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						PassMysqlconn passmysql=new PassMysqlconn();
						try {
							Connection oraclesqlconn = oraclesql.getConn();
							Connection passmysqlconn = passmysql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_order数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_order";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_order (grouptag, orderstatus, doctorname, is_temp, remark, pa_enddatetime, "
									+ "orderno, wardcode, purpose, singledose, frequency, drugform, is_out, visitid, routecode, "
									+ "deptcode, ordercode, deptname, caseid, reasonable_desc, doctorcode, meditime, drug_unique_code, "
									+ "startdatetime, ordertype, routename, enddatetime, cid, drugspec, executetime, wardname, "
									+ "patientid, hiscode, ordername, groupstate, doseunit, is_allow) values(?,?,?,?,?,?,?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									JSONObject ScreenDrugList=json.getJSONObject("ScreenDrugList");
									JSONArray ScreenDrugs=ScreenDrugList.getJSONArray("ScreenDrugs");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");
									
									for(int k=0;k<ScreenDrugs.size();k++){
										a=a+1;
										JSONObject ScreenDrug=ScreenDrugs.getJSONObject(k);
										
										//字典表找数据
										sql="select drugspec,drugform,comp_name,doseunit,drugcode from mc_dict_drug_pass where drug_unique_code="
												+ "'"+ScreenDrug.getString("DrugUniqueCode")+"'";
										st=passmysqlconn.createStatement();
										rs=st.executeQuery(sql);
										List list_drug_pass=passmysql.getlist(rs);
										String drugspec="";
										String drugform="";
										String comp_name="";
										String doseunit="";
										String drugcode="";
										for(int k1=0;k1<list_drug_pass.size();k1++){
											if(k1==1){
												break;
											}
											Map map=(Map)list_drug_pass.get(k1);
											if(map.get("drugspec")!=null){
												drugspec=map.get("drugspec").toString();
											}
											if(map.get("drugform")!=null){
												drugform=map.get("drugform").toString();
											}
											if(map.get("comp_name")!=null){
												comp_name=map.get("comp_name").toString();
											}
											if(map.get("doseunit")!=null){
												doseunit=map.get("doseunit").toString();
											}
											if(map.get("drugcode")!=null){
												drugcode=map.get("drugcode").toString();
											}
										}
										
										pst.setString(1,ScreenDrug.getString("GroupTag"));//[grouptag
										pst.setInt(2,1);//orderstatus
										pst.setString(3,ScreenDrug.getString("DoctorName"));//doctorname
										pst.setString(4,ScreenDrug.getString("IsTempDrug"));//is_temp
										pst.setString(5,"");//remark
										pst.setString(6,startdate1);//pa_enddatetime
										pst.setString(7,ScreenDrug.getString("Index"));//orderno
										pst.setString(8,Patient.getString("DeptCode"));//wardcode
										pst.setInt(9,0);//purpose
										pst.setString(10,ScreenDrug.getString("DosePerTime"));//singledose
										pst.setString(11,ScreenDrug.getString("Frequency"));//frequency
										pst.setString(12,drugform);//drugform
										pst.setInt(13,1);//is_out
										pst.setInt(14,1);//visitid
										pst.setString(15,ScreenDrug.getString("RouteCode"));//routecode
										pst.setString(16,ScreenDrug.getString("DeptCode"));//deptcode
										pst.setString(17,drugcode);//ordercode
										pst.setString(18,ScreenDrug.getString("DeptName"));//deptname
										pst.setString(19,caseid);//caseid
										pst.setString(20,"合理越权描述");//reasonable_desc
										pst.setString(21,ScreenDrug.getString("DoctorCode"));//doctorcode
										pst.setInt(22,0);//meditime
										pst.setString(23,ScreenDrug.getString("DrugUniqueCode"));//drug_unique_code
										pst.setString(24,ScreenDrug.getString("StartTime"));//startdatetime
										pst.setInt(25,1);//ordertype
										pst.setString(26,ScreenDrug.getString("RouteName"));//routename
										pst.setString(27,ScreenDrug.getString("EndTime"));//enddatetime
										pst.setString(28,Patient.getString("PatCode")+"_"+i+"_"+j+"#"+ScreenDrug.getString("Index"));//cid
										pst.setString(29,drugspec);//drugspec
										pst.setString(30,ScreenDrug.getString("ExecuteTime"));//executetime
										pst.setString(31,Patient.getString("DeptName"));//wardname
										pst.setString(32,Patient.getString("PatCode"));//patientid
										pst.setString(33,PassClient.getString("HospID"));//hiscode
										pst.setString(34,ScreenDrug.getString("DrugName"));//ordername
										pst.setInt(35,1);//groupstate
										pst.setString(36,doseunit);//doseunit
										pst.setInt(37,0);//is_allow]
										
										pst.addBatch();
									}
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_order总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_order总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							rs.close();
							pst.close();
							passmysqlconn.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_order制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn = oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_patient数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_patient";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_patient (sex, bedno, doctorname, in_diagnosis, weight, firstdeptname, ren_damage, "
									+ "iage, wardcode, i_in, height, hospitalno, payclass, visitid, deptcode, medgroupname, age, is_lact, "
									+ "birthdate, identitycard, deptname, caseid, incisiontypes, allergen, doctorcode, hep_damage, "
									+ "medgroupcode, is_preg, cost, preg_starttime, nursingclass, wardname, startdate, standby, "
									+ "patientname, patientid, hiscode, operations, enddate, accountdate, telephone) values(?,?,?,?,?,?,?,"
									+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String enddate1=enddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(countcy/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd");
									time = sdf.parse(enddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        enddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									if(StringUtils.isBlank(Patient.getString("Sex"))){
										pst.setString(1,"无");//sex
									}else{
										pst.setString(1,Patient.getString("Sex"));//sex
									}
									pst.setInt(2,1);//bedno
									pst.setString(3,Patient.getString("DoctorName"));//doctorname
									pst.setString(4,"");//in_diagnosis
									pst.setString(5,Patient.getString("WeighKG"));//weight
									pst.setString(6,Patient.getString("DeptName"));//firstdeptname
									pst.setString(7,Patient.getString("RenDamageDegree"));//ren_damage
									pst.setInt(8,0);//iage
									pst.setString(9,Patient.getString("DeptCode"));//wardcode
									pst.setInt(10,0);//i_in
									pst.setString(11,Patient.getString("HeightCM"));//height
									pst.setString(12,PassClient.getString("HospID"));//hospitalno
									pst.setString(13,"自费");//payclass
									pst.setInt(14,1);//visitid
									pst.setString(15,Patient.getString("DeptCode"));//deptcode
									pst.setString(16,"门诊心电图_医疗组");//medgroupname
									if(StringUtils.isBlank(Patient.getString("Age"))){
										pst.setInt(17,0);//age
									}else{
										pst.setString(17,Patient.getString("Age"));//age
									}
									pst.setString(18,Patient.getString("IsLactation"));//is_lact
									pst.setString(19,Patient.getString("Birthday"));//birthdate
									pst.setString(20,Patient.getString("IDCard"));//identitycard
									pst.setString(21,Patient.getString("DeptName"));//deptname
									pst.setString(22,caseid);//caseid
									pst.setString(23,"切口类型串");//incisiontypes
									pst.setString(24,"");//allergen
									pst.setString(25,Patient.getString("DoctorCode"));//doctorcode
									pst.setString(26,Patient.getString("HepDamageDegree"));//hep_damage
									pst.setInt(27,3207);//medgroupcode
									pst.setString(28,Patient.getString("IsPregnancy"));//is_preg
									pst.setInt(29,100);//cost
									pst.setString(30,Patient.getString("PregStartDate"));//preg_starttime
									pst.setInt(31,1);//nursingclass
									pst.setString(32,Patient.getString("DeptName"));//wardname
									pst.setString(33,startdate1);//startdate
									pst.setString(34,"");//standby
									pst.setString(35,Patient.getString("Name"));//patientname
									pst.setString(36,Patient.getString("PatCode"));//patientid
									pst.setString(37,PassClient.getString("HospID"));//hiscode
									pst.setString(38,"手术列表串");//operations
									pst.setString(39,enddate1);//enddate
									pst.setString(40,"");//accountdate
									pst.setString(41,Patient.getString("Telephone"));//telephone]
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_patient总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_patient总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_patient制造数据异常");
						}
					}
				}).start();
				
				new Thread(new Runnable(){
					public void run(){
						Oracleconn oraclesql=new Oracleconn();
						try {
							Connection oraclesqlconn = oraclesql.getConn();
							PreparedStatement pst=null;
							Statement st=null;
							ResultSet rs=null;
							List list=null;
							String sql=null;
							
							//制造t_mc_outhosp_temperature数据
							if(trunca==1){
								sql="truncate table t_mc_outhosp_temperature";
								st=oraclesqlconn.createStatement();
								st.execute(sql);
							}
							
							oraclesqlconn.setAutoCommit(false);
							sql="insert into t_mc_outhosp_temperature (patientid, visitid, hiscode, taketime, temperature, caseid) "
									+ "values(?,?,?,?,?,?)";
							pst=oraclesqlconn.prepareStatement(sql);
							int a=0;
							int b=0;
							String ienddate1=ienddate;
							String startdate1=startdate;
							SimpleDateFormat sdf=null;
							Date time=null;
							Calendar cal=null;
							for(int i=0;i<countcy;i++){
								//数据分割，增加时间
								if(i%(count/sum_date)==0 && i>0){
									sdf=new SimpleDateFormat("yyyyMMdd");
									time = sdf.parse(ienddate1);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, 1); //通过calendar方法计算天数加减法，例如：1或者-1
							        ienddate1=sdf.format(cal.getTime());
							        
							        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									time = sdf.parse(startdate);
									cal = Calendar.getInstance();   
							        cal.setTime(time);   
							        cal.add(Calendar.DATE, i/sum_date-new Random().nextInt(10)); //通过calendar方法计算天数加减法，例如：1或者-1
							        startdate1=sdf.format(cal.getTime());
								}
								for(int j=0;j<anlilist.size();j++){
									a=a+1;
									JSONObject json=JSONObject.fromObject(anlilist.get(j));
									JSONObject PassClient=json.getJSONObject("PassClient");
									JSONObject Patient=json.getJSONObject("Patient");
									Patient.put("PatCode", hiscode+ienddate1+i+"_"+j);
									Patient.put("InHospNo",hiscode+ienddate1+i+"_"+j);
									//门诊caseid：Mz门诊号+“＿”＋病人编号
									String caseid="Mz"+Patient.getString("PatCode")+"_"+Patient.getString("InHospNo");

									pst.setString(1,Patient.getString("PatCode"));//[patientid
									pst.setInt(2,1);//visitid
									pst.setString(3,PassClient.getString("HospID"));//hiscode
									pst.setString(4,startdate1);//taketime
									pst.setString(5,"36.30");//temperature
									pst.setString(6,caseid);//caseid]
									
									pst.addBatch();
									
									json.clear();
									json = null;
								}
								
								if(a-50000>=0){
									b=b+50000;
									a=a-50000;
									pst.executeBatch();
									System.out.println("t_mc_outhosp_temperature总数："+countcy*anlilist.size()+"-->有效数据："+b);
								}
								if((i+1)==countcy){
									pst.executeBatch();
									System.out.println("t_mc_outhosp_temperature总数："+countcy*anlilist.size()+"-->有效数据："+(b+a));
								}
								
							}
							oraclesqlconn.commit();
							
							pst.close();
							oraclesqlconn.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("t_mc_outhosp_temperature制造数据异常");
						}
					}
				}).start();
				
			}
			
			if(dict==1){
				System.out.println("开始导字典表数据");
				//mc_dict_allergen
				sql="select * from mc_dict_allergen where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_allergen";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_allergen(searchcode, allercode, match_user, pass_allertype, is_save, "
						+ "pass_allerid, allername, match_scheme, match_time, unable_match_desc, unable_match, pass_allername) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//[searchcode
					pst.setString(2,map.get("allercode").toString());//allercode
					pst.setString(3,map.get("match_user").toString());//match_user
					pst.setInt(4,Integer.parseInt(map.get("pass_allertype").toString()));//pass_allertype
					pst.setInt(5,Integer.parseInt(map.get("is_save").toString()));//is_save
//					pst.setString(6,map.get("createdate").toString());//createdate
					pst.setInt(6,Integer.parseInt(map.get("pass_allerid").toString()));//pass_allerid
					pst.setString(7,map.get("allername").toString());//allername
					pst.setInt(8,match_scheme);//match_scheme
					pst.setString(9,"");//match_time
					if(StringUtils.isBlank((String)map.get("unable_match_desc"))){
						pst.setString(10,"");//unable_match_desc
					}else{
						pst.setString(10,map.get("unable_match_desc").toString());
					}
					pst.setInt(11,Integer.parseInt(map.get("unable_match").toString()));//unable_match
					pst.setString(12,map.get("pass_allername").toString());//pass_allername]
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_costitem
				sql="select * from mc_dict_costitem where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_costitem ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_costitem( itemtype, searchcode, itemname, is_save, is_byx, match_scheme, itemcode) "
						+ "values(?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
//					pst.setString(1,map.get("inserttime").toString());//[inserttime
					pst.setString(1,map.get("itemtype").toString());//itemtype
					if("".equals(map.get("searchcode")) || map.get("searchcode")==null){
						pst.setString(2,"");//searchcode
					}else{
						pst.setString(2,map.get("searchcode").toString());//searchcode
					}
					pst.setString(3,map.get("itemname").toString());//itemname
					pst.setString(4,map.get("is_save").toString());//is_save
					pst.setString(5,map.get("is_byx").toString());//is_byx
					pst.setInt(6,match_scheme);//match_scheme
					pst.setString(7,map.get("itemcode").toString());//itemcode]
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_dept
				sql="select * from mc_dict_dept where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_dept";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_dept( searchcode, deptcode, is_save, is_inhosp, match_scheme, "
						+ "is_emergency, deptname, is_clinic) values(?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//searchcode
					pst.setString(2,map.get("deptcode").toString());//deptcode
					pst.setString(3,map.get("is_save").toString());//is_save
					pst.setString(4,map.get("is_inhosp").toString());//is_inhosp
					pst.setString(5,map.get("match_scheme").toString());//match_scheme
					pst.setString(6,map.get("is_emergency").toString());//is_emergency
					pst.setString(7,map.get("deptname").toString());//deptname
					pst.setString(8,map.get("is_clinic").toString());//is_clinic				
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_disease
				sql="select * from mc_dict_disease where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_disease";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_disease(searchcode, typecode, match_user, is_save,  is_mxb, "
						+ "match_scheme, disname, pass_icd_code, pass_icd_name, dis_type, discode, typename, "
						+ "match_time, unable_match_desc, unable_match) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//[searchcode
					if("".equals(map.get("typecode")) || map.get("typecode")==null){
						pst.setString(2,"");//typecode
					}else{
						pst.setString(2,map.get("typecode").toString());//typecode
					}
					if("".equals(map.get("match_user")) || map.get("match_user")==null){
						pst.setString(3,"");//match_user
					}else{
						pst.setString(3,map.get("match_user").toString());//match_user
					}
					
					pst.setString(4,map.get("is_save").toString());//is_save
					pst.setString(5,map.get("is_mxb").toString());//is_mxb
					pst.setInt(6,match_scheme);//match_scheme
					pst.setString(7,map.get("disname").toString());//disname
					if("".equals(map.get("pass_icd_code")) || map.get("pass_icd_code")==null){
						pst.setString(8,"");//pass_icd_code
					}else{
						pst.setString(8,map.get("pass_icd_code").toString());//pass_icd_code
					}
					if("".equals(map.get("pass_icd_name")) || map.get("pass_icd_name")==null){
						pst.setString(9,"");//pass_icd_code
					}else{
						pst.setString(9,map.get("pass_icd_name").toString());//pass_icd_name
					}
					pst.setString(10,map.get("dis_type").toString());//dis_type
					pst.setString(11,map.get("discode").toString());//discode
					if("".equals(map.get("typename")) || map.get("typename")==null){
						pst.setString(12,"");//pass_icd_code
					}else{
						pst.setString(12,map.get("typename").toString());//typename
					}
					if("".equals(map.get("match_time")) || map.get("match_time")==null){
						pst.setString(13,"");//pass_icd_code
					}else{
						pst.setString(13,map.get("match_time").toString());//match_time
					}
					if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
						pst.setString(14,"");//pass_icd_code
					}else{
						pst.setString(14,map.get("unable_match_desc").toString());//unable_match_desc
					}
					
					pst.setString(15,map.get("unable_match").toString());//unable_match]
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_doctor
				sql="select * from mc_dict_doctor where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_doctor";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_doctor(searchcode, doctorlevel, doctorname, ilevel, doctorcode, deptcode, "
						+ "is_save, antilevel, match_scheme, prespriv, password, deptname, is_clinic) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//searchcode
					if(map.get("doctorlevel")==null || "".equals(map.get("doctorlevel"))){
						pst.setString(2,"");//doctorlevel
					}else{
						pst.setString(2,map.get("doctorlevel").toString());//doctorlevel
					}
					
					pst.setString(3,map.get("doctorname").toString());//doctorname
					pst.setString(4,map.get("ilevel").toString());//ilevel
					pst.setString(5,map.get("doctorcode").toString());//doctorcode
					pst.setString(6,map.get("deptcode").toString());//deptcode
					pst.setString(7,map.get("is_save").toString());//is_save
					pst.setString(8,map.get("antilevel").toString());//antilevel
					pst.setString(9,map.get("match_scheme").toString());//match_scheme
					pst.setString(10,map.get("prespriv").toString());//prespriv
					pst.setString(11,map.get("password").toString());//password
					pst.setString(12,map.get("deptname").toString());//deptname
					pst.setString(13,map.get("is_clinic").toString());//is_clinic
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_drug
				sql="select * from mc_dict_drug where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_drug";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_drug(searchcode, is_bisection_use, operuser, is_save, drugformtype, "
						+ "jdmtype, state, druggroupcode, match_scheme, is_bloodmed, socialsecurity_ratio, "
						+ "drugtype, drugname, drugform, drugcode, stimulantingred, is_anti, antilevel, antitype, "
						+ "is_basedrug_p, classid, druggroupname, is_socialsecurity, is_sugarmed, is_stimulant, "
						+ "classtitle, is_basedrug, is_dearmed, high_alert_level, is_srpreparations, is_needskintest, "
						+ "socialsecurity_desc, typename, is_solvent, is_poison, otctype, opertime, drggrp_searchcode) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//searchcode
					pst.setString(2,map.get("is_bisection_use").toString());//is_bisection_use
					if(map.get("operuser")==null || "".equals(map.get("operuser"))){
						pst.setString(3,"");//operuser
					}else{
						pst.setString(3,map.get("operuser").toString());//operuser
					}
					
					pst.setString(4,map.get("is_save").toString());//is_save
					pst.setString(5,map.get("drugformtype").toString());//drugformtype
					pst.setString(6,map.get("jdmtype").toString());//jdmtype
					pst.setString(7,map.get("state").toString());//state
					if(map.get("druggroupcode")==null || "".equals(map.get("druggroupcode"))){
						pst.setString(8,"");//druggroupcode
					}else{
						pst.setString(8,map.get("druggroupcode").toString());//druggroupcode
					}
					
					pst.setString(9,map.get("match_scheme").toString());//match_scheme
					pst.setString(10,map.get("is_bloodmed").toString());//is_bloodmed
					if(map.get("socialsecurity_ratio")==null || "".equals(map.get("socialsecurity_ratio"))){
						pst.setString(11,"");//socialsecurity_ratio
					}else{
						pst.setString(11,map.get("socialsecurity_ratio").toString());//socialsecurity_ratio
					}
					pst.setString(12,map.get("drugtype").toString());//drugtype
					pst.setString(13,map.get("drugname").toString());//drugname
					if(map.get("drugform")==null || "".equals(map.get("drugform"))){
						pst.setString(14,"");//drugform
					}else{
						pst.setString(14,map.get("drugform").toString());//drugform
					}
					
					pst.setString(15,map.get("drugcode").toString());//drugcode
					if(map.get("stimulantingred")==null || "".equals(map.get("stimulantingred"))){
						pst.setString(16,"");//stimulantingred
					}else{
						pst.setString(16,map.get("stimulantingred").toString());//stimulantingred
					}
					pst.setString(17,map.get("is_anti").toString());//is_anti
					pst.setString(18,map.get("antilevel").toString());//antilevel
					pst.setString(19,map.get("antitype").toString());//antitype
					pst.setString(20,map.get("is_basedrug_p").toString());//is_basedrug_p
					if(map.get("classid")==null || "".equals(map.get("classid"))){
						pst.setString(21,"");//classid
					}else{
						pst.setString(21,map.get("classid").toString());//classid
					}
					
					if(map.get("druggroupname")==null || "".equals(map.get("druggroupname"))){
						pst.setString(22,"");//druggroupname
					}else{
						pst.setString(22,map.get("druggroupname").toString());//druggroupname
					}
					pst.setString(23,map.get("is_socialsecurity").toString());//is_socialsecurity
					pst.setString(24,map.get("is_sugarmed").toString());//is_sugarmed
					pst.setString(25,map.get("is_stimulant").toString());//is_stimulant
					if(map.get("classtitle")==null || "".equals(map.get("classtitle"))){
						pst.setString(26,"");//classtitle
					}else{
						pst.setString(26,map.get("classtitle").toString());//classtitle
					}
					
					pst.setString(27,map.get("is_basedrug").toString());//is_basedrug
					pst.setString(28,map.get("is_dearmed").toString());//is_dearmed
					pst.setString(29,map.get("high_alert_level").toString());//high_alert_level
					pst.setString(30,map.get("is_srpreparations").toString());//is_srpreparations
					pst.setString(31,map.get("is_needskintest").toString());//is_needskintest
					if(map.get("socialsecurity_desc")==null || "".equals(map.get("socialsecurity_desc"))){
						pst.setString(32,"");//socialsecurity_desc
					}else{
						pst.setString(32,map.get("socialsecurity_desc").toString());//socialsecurity_desc
					}
					if(map.get("typename")==null || "".equals(map.get("typename"))){
						pst.setString(33,"");//typename
					}else{
						pst.setString(33,map.get("typename").toString());//typename
					}
					
					pst.setString(34,map.get("is_solvent").toString());//is_solvent
					pst.setString(35,map.get("is_poison").toString());//is_poison
					pst.setString(36,map.get("otctype").toString());//otctype
					if(map.get("opertime")==null || "".equals(map.get("opertime"))){
						pst.setString(37,"");//opertime
					}else{
						pst.setString(37,map.get("opertime").toString());//opertime
					}
					if(map.get("drggrp_searchcode")==null || "".equals(map.get("drggrp_searchcode"))){
						pst.setString(38,"");//drggrp_searchcode
					}else{
						pst.setString(38,map.get("drggrp_searchcode").toString());//drggrp_searchcode
					}
					
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_drug_pass
				sql="select * from mc_dict_drug_pass where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_drug_pass";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_drug_pass(searchcode, oprpi_time, match_scheme, pass_drugname, "
						+ "comp_name, menulabel, drugname, drugform, drugcode, pass_drugcode, pass_upstate, proid, "
						+ "pass_approvalcode, pass_form_name, match_time, unable_match_desc, unable_match, pass_st_strength,"
						+ " match_user, drug_unique_code, approvalcode, pass_dividend, pass_divisor, pass_st_comp_name, "
						+ "drugspec, oprpi_user, pass_doseunit, pass_nametype, doseunit) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//[searchcode
					if("".equals(map.get("oprpi_time")) || map.get("oprpi_time")==null){
						pst.setString(2,"");//oprpi_time
					}else{
						pst.setString(2,map.get("oprpi_time").toString());//oprpi_time
					}
					
					pst.setInt(3,match_scheme);//match_scheme
					if("".equals(map.get("pass_drugname")) || map.get("pass_drugname")==null){
						pst.setString(4,"");//pass_drugname
					}else{
						pst.setString(4,map.get("pass_drugname").toString());//pass_drugname
					}
					if("".equals(map.get("comp_name")) || map.get("comp_name")==null){
						pst.setString(5,"");//comp_name
					}else{
						pst.setString(5,map.get("comp_name").toString());//comp_name
					}
					
					if("".equals(map.get("menulabel")) || map.get("menulabel")==null){
						pst.setString(6,"");//menulabel
					}else{
						pst.setString(6,map.get("menulabel").toString());//menulabel
					}
					
					pst.setString(7,map.get("drugname").toString());//drugname
					if("".equals(map.get("drugform")) || map.get("drugform")==null){
						pst.setString(8,"");//drugform
					}else{
						pst.setString(8,map.get("drugform").toString());//drugform
					}
					
					pst.setString(9,map.get("drugcode").toString());//drugcode
					pst.setString(10,map.get("pass_drugcode").toString());//pass_drugcode
					if("".equals(map.get("pass_upstate")) || map.get("pass_upstate")==null){
						pst.setInt(11,0);//pass_upstate
					}else{
						pst.setInt(11,Integer.parseInt(map.get("pass_upstate").toString()));//pass_upstate
					}
					
					pst.setString(12,map.get("proid").toString());//proid
					if("".equals(map.get("pass_approvalcode")) || map.get("pass_approvalcode")==null){
						pst.setString(13,"");//pass_approvalcode
					}else{
						pst.setString(13,map.get("pass_approvalcode").toString());//pass_approvalcode
					}
					if("".equals(map.get("pass_form_name")) || map.get("pass_form_name")==null){
						pst.setString(14,"");//pass_form_name
					}else{
						pst.setString(14,map.get("pass_form_name").toString());//pass_form_name
					}
					if("".equals(map.get("match_time")) || map.get("match_time")==null){
						pst.setString(15,"");//match_time
					}else{
						pst.setString(15,map.get("match_time").toString());//match_time
					}
					if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
						pst.setString(16,"");//unable_match_desc
					}else{
						pst.setString(16,map.get("unable_match_desc").toString());//unable_match_desc
					}
					
					pst.setString(17,map.get("unable_match").toString());//unable_match
					if("".equals(map.get("pass_st_strength")) || map.get("pass_st_strength")==null){
						pst.setString(18,"");//pass_st_strength
					}else{
						pst.setString(18,map.get("pass_st_strength").toString());//pass_st_strength
					}
					if("".equals(map.get("match_user")) || map.get("match_user")==null){
						pst.setString(19,"");//match_user
					}else{
						pst.setString(19,map.get("match_user").toString());//match_user
					}
					
					pst.setString(20,map.get("drug_unique_code").toString());//drug_unique_code
					if("".equals(map.get("approvalcode")) || map.get("approvalcode")==null){
						pst.setString(21,"");//approvalcode
					}else{
						pst.setString(21,map.get("approvalcode").toString());//approvalcode
					}
					
					pst.setString(22,map.get("pass_dividend").toString());//pass_dividend
					pst.setString(23,map.get("pass_divisor").toString());//pass_divisor
					if("".equals(map.get("pass_st_comp_name")) || map.get("pass_st_comp_name")==null){
						pst.setString(24,"");//pass_st_comp_name
					}else{
						pst.setString(24,map.get("pass_st_comp_name").toString());//pass_st_comp_name
					}
					if("".equals(map.get("drugspec")) || map.get("drugspec")==null){
						pst.setString(25,"");//oprpi_user
					}else{
						pst.setString(25,map.get("drugspec").toString());//drugspec
					}
					
					if("".equals(map.get("oprpi_user")) || map.get("oprpi_user")==null){
						pst.setString(26,"");//oprpi_user
					}else{
						pst.setString(26,map.get("oprpi_user").toString());//oprpi_user
					}
					if("".equals(map.get("pass_doseunit")) || map.get("pass_doseunit")==null){
						pst.setString(27,"");//pass_doseunit
					}else{
						pst.setString(27,map.get("pass_doseunit").toString());//pass_doseunit
					}
					
					pst.setString(28,map.get("pass_nametype").toString());//pass_nametype
					pst.setString(29,map.get("doseunit").toString());//doseunit]
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_drug_sub
				sql="select * from mc_dict_drug_sub where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_drug_sub ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_drug_sub(searchcode, dddunit, ddd, is_save, state, adddate, match_scheme, "
						+ "is_use, drugname, drugspec, ddd_costunit, inserttime, drugform, drugcode, costunit) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//searchcode
					if("".equals(map.get("dddunit")) || map.get("dddunit")==null){
						pst.setString(2,"");//dddunit
					}else{
						pst.setString(2,map.get("dddunit").toString());//dddunit
					}
					if("".equals(map.get("ddd")) || map.get("ddd")==null){
						pst.setString(3,"");//ddd
					}else{
						pst.setString(3,map.get("ddd").toString());//ddd
					}
					
					pst.setString(4,map.get("is_save").toString());//is_save
					pst.setString(5,map.get("state").toString());//state
					if("".equals(map.get("adddate")) || map.get("adddate")==null){
						pst.setString(6,"");//adddate
					}else{
						pst.setString(6,map.get("adddate").toString());//adddate
					}
					
					pst.setString(7,map.get("match_scheme").toString());//match_scheme
					pst.setString(8,map.get("is_use").toString());//is_use
					pst.setString(9,map.get("drugname").toString());//drugname
					pst.setString(10,map.get("drugspec").toString());//drugspec
					if("".equals(map.get("ddd_costunit")) || map.get("ddd_costunit")==null){
						pst.setString(11,"");//ddd_costunit
					}else{
						pst.setString(11,map.get("ddd_costunit").toString());//ddd_costunit
					}
					if("".equals(map.get("ddd_costunit")) || map.get("ddd_costunit")==null){
						pst.setString(11,"");//ddd_costunit
					}else{
						pst.setString(11,map.get("ddd_costunit").toString());//ddd_costunit
					}
					if("".equals(map.get("inserttime")) || map.get("inserttime")==null){
						pst.setString(12,"");//inserttime
					}else{
						pst.setString(12,map.get("inserttime").toString());//inserttime
					}
					if("".equals(map.get("drugform")) || map.get("drugform")==null){
						pst.setString(13,"");//drugform
					}else{
						pst.setString(13,map.get("drugform").toString());//drugform
					}
					
					pst.setString(14,map.get("drugcode").toString());//drugcode
					pst.setString(15,map.get("costunit").toString());//costunit
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_exam
				sql="select * from mc_dict_exam where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_exam ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_exam(inserttime, searchcode, is_save, examname, examcode, match_scheme, type) "
						+ "values(to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("inserttime").toString().substring(0,19));//inserttime
					if("".equals(map.get("searchcode")) || map.get("searchcode")==null){
						pst.setString(2,"");//searchcode
					}else{
						pst.setString(2,map.get("searchcode").toString());//searchcode
					}
					
					pst.setString(3,map.get("is_save").toString());//is_save
					pst.setString(4,map.get("examname").toString());//examname
					pst.setString(5,map.get("examcode").toString());//examcode
					pst.setString(6,map.get("match_scheme").toString());//match_scheme
					pst.setString(7,map.get("type").toString());//type
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_frequency
				sql="select * from mc_dict_frequency where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_frequency ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_frequency(pharmfrequency, days, times, is_save, match_desc, "
						+ "match_scheme, frequency, unable_match) values(?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					if("".equals(map.get("pharmfrequency")) || map.get("pharmfrequency")==null){
						pst.setString(1,"");//pharmfrequency
					}else{
						pst.setString(1,map.get("pharmfrequency").toString());//pharmfrequency
					}
					
					pst.setString(2,map.get("days").toString());//days
					pst.setString(3,map.get("times").toString());//times
					pst.setString(4,map.get("is_save").toString());//is_save
					if("".equals(map.get("match_desc")) || map.get("match_desc")==null){
						pst.setString(5,"");//match_desc
					}else{
						pst.setString(5,map.get("match_desc").toString());//match_desc
					}
					
					pst.setString(6,map.get("match_scheme").toString());//match_scheme
					pst.setString(7,map.get("frequency").toString());//frequency
					pst.setString(8,map.get("unable_match").toString());//unable_match
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_lab
				sql="select * from mc_dict_lab where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_lab ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_lab( searchcode, labcode, is_save, labname, match_scheme, type) "
						+ "values(?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//searchcode
					pst.setString(2,map.get("labcode").toString());//labcode
					pst.setString(3,map.get("is_save").toString());//is_save
					pst.setString(4,map.get("labname").toString());//labname
					pst.setString(5,map.get("match_scheme").toString());//match_scheme
					pst.setString(6,map.get("type").toString());//type
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_labitem
				sql="select * from mc_dict_labsub ";
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_labsub ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_labsub( searchcode, itemname, is_save, match_scheme, itemcode, medshow_id, type_id) "
						+ "values(?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//searchcode
					pst.setString(2,map.get("itemname").toString());//itemname
					pst.setString(3,map.get("is_save").toString());//is_save
					pst.setString(4,map.get("match_scheme").toString());//match_scheme
					pst.setString(5,map.get("itemcode").toString());//itemcode
					pst.setString(6,map.get("medshow_id").toString());//medshow_id
					pst.setString(7,map.get("type_id").toString());//type_id
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_operation
				sql="select * from mc_dict_operation where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_operation ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_operation(premoment_high, searchcode, premoment_low, operationname, is_save, "
						+ "typename, createdate, match_scheme, useanti, drugtime, operationcode) "
						+ "values(?,?,?,?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("premoment_high").toString());//premoment_high
					pst.setString(2,map.get("searchcode").toString());//searchcode
					pst.setString(3,map.get("premoment_low").toString());//premoment_low
					pst.setString(4,map.get("operationname").toString());//operationname
					pst.setString(5,map.get("is_save").toString());//is_save
					if("".equals(map.get("typename")) || map.get("typename")==null){
						pst.setString(6,"");//typename
					}else{
						pst.setString(6,map.get("typename").toString());//typename
					}
					
					pst.setString(7,map.get("createdate").toString().substring(0,19));//createdate,处理date类型方法
					pst.setString(8,map.get("match_scheme").toString());//match_scheme
					pst.setString(9,map.get("useanti").toString());//useanti
					pst.setString(10,map.get("drugtime").toString());//drugtime
					pst.setString(11,map.get("operationcode").toString());//operationcode
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				//mc_dict_route
				sql="select * from mc_dict_route where match_scheme="+match_scheme;
				st=passmysqlconn.createStatement();
				rs=st.executeQuery(sql);
				list=passmysql.getlist(rs);
				
				sql="truncate table mc_dict_route ";
				st=oraclesqlconn.createStatement();
				st.executeUpdate(sql);
				
				oraclesqlconn.setAutoCommit(false);
				sql="insert into mc_dict_route(searchcode, match_user, is_save, createdate, match_scheme, routename, "
						+ "isskintest, pass_route_name, routecode, route_type, abbrev, pass_routeid, match_time, "
						+ "unable_match_desc, unable_match) values(?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?)";
				pst=oraclesqlconn.prepareStatement(sql);
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					pst.setString(1,map.get("searchcode").toString());//[searchcode
					if("".equals(map.get("match_user")) || map.get("match_user")==null){
						pst.setString(2,"");//match_user
					}else{
						pst.setString(2,map.get("match_user").toString());//match_user
					}
					
					pst.setString(3,map.get("is_save").toString());//is_save
					pst.setString(4,map.get("createdate").toString().substring(0,19));//createdate
					pst.setInt(5,match_scheme);//match_scheme
					pst.setString(6,map.get("routename").toString());//routename
					pst.setString(7,map.get("isskintest").toString());//isskintest
					if("".equals(map.get("pass_route_name")) || map.get("pass_route_name")==null){
						pst.setString(8,"");//pass_route_name
					}else{
						pst.setString(8,map.get("pass_route_name").toString());//pass_route_name
					}
					
					pst.setString(9,map.get("routecode").toString());//routecode
					pst.setString(10,map.get("route_type").toString());//route_type
					if("".equals(map.get("abbrev")) || map.get("abbrev")==null){
						pst.setString(11,"");//pass_doseunit
					}else{
						pst.setString(11,map.get("abbrev").toString());//abbrev
					}
					
					pst.setString(12,map.get("pass_routeid").toString());//pass_routeid
					if("".equals(map.get("match_time")) || map.get("match_time")==null){
						pst.setString(13,"");//match_time
					}else{
						pst.setString(13,map.get("match_time").toString());//match_time
					}
					
					if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
						pst.setString(14,"");//pass_doseunit
					}else{
						pst.setString(14,map.get("unable_match_desc").toString());//unable_match_desc
					}
					
					pst.setString(15,map.get("unable_match").toString());//unable_match]
					
					pst.addBatch();
				}
				pst.executeBatch();
				oraclesqlconn.commit();
				
				System.out.println("导字典表数据结束");
		    	rs.close();
				st.close();
				pst.close();
				passmysqlconn.close();
			}
		}
		
		System.out.println("ORACLE数据导入多线程开启");
		
		if(red==1){
			//刷新redis
			Passservice Passservice=new Passservice();
			String result_clean = Passservice.getPassResult("",rhmmurl+"/redis/cleanRedis") ; 
			System.out.println("clean-redis,"+result_clean);
	    	
	    	String result_sys = Passservice.getPassResult("",rhmmurl+"/redis/updateSys") ;
			System.out.println("update-Sys"+result_sys);
			
	    	String result_custom =Passservice.getPassResult("",rhmmurl+"/redis/updateCustom") ; 
	    	System.out.println("update-custom"+result_custom);
	    	
	    	String result_shield = Passservice.getPassResult("",rhmmurl+"/redis/updateShield") ; 
	    	System.out.println("update-shield"+result_shield);
		}
		
		rs.close();
		pst.close();
		st.close();
		oraclesqlconn.close();
		mysqlconn.close();
		passmysqlconn.close();
	}
	
	public void PAclear() throws ClassNotFoundException, SQLException, IOException{
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		Statement st=null;
		String sql=null;
		
		String data="2017-07-16";//导数据日期,his数据库的日期也需要制作成和这个相同
		int kid=1;//根据mc_kettle_info选择配置
		
		//重置记录表
		sql="truncate mc_import_info";
		st=passmysqlconn.createStatement();
		st.execute(sql);
			
		sql="truncate t_mc_screen_info";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_screen_info";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="insert into mc_import_info(c_end_date,kid) values('"+data+"',"+kid+")";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		//清理数据
		sql="truncate pharm_screenresults";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_review_main";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_review_detail";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_review_question_drugs";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		System.out.println("审查挂机等表清理结束");
		
		//清理门诊表
		sql="truncate mc_clinic_allergen";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_caseid_ienddate";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_cost";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_disease";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_drugcost_caseid";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_drugcost_costtime";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_drugorder_detail";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_drugorder_main";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_exam";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_lab";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_operation";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_order";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_patient_baseinfo";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_patient_medinfo";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_clinic_prescription";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		System.out.println("门诊清理结束");
		
		//清理住院表
		sql="truncate mc_inhosp_allergen";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_cost";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_disease";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_drugcost_caseid";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_drugcost_costtime";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_drugcostdistinct";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_drugorder_detail";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_drugorder_main";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_exam";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_lab";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_operation";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_order";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_patient_baseinfo";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_patient_medinfo";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_inhosp_temperature";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		System.out.println("住院表清理结束");
		
		//清理出院表
		sql="truncate mc_outhosp_allergen";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_caseid_ienddate";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_cost";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_disease";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_drugcost_caseid";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_drugcost_costtime";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_drugcostdistinct";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_drugorder_detail";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_drugorder_main";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_exam";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_lab";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_operation";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_order";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_patient_baseinfo";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_patient_medinfo";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate mc_outhosp_temperature";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		System.out.println("出院清理结束");
		
		//清理临时表
		sql="truncate t_mc_clinic_allergen";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_clinic_cost";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_clinic_disease";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_clinic_exam";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_clinic_lab";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_clinic_order";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_clinic_patient";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_allergen";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_cost";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_disease";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_exam";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_lab";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_operation";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_order";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_patient";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_inhosp_temperature";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_allergen";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_cost";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_disease";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_exam";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_lab";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_operation";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_order";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_patient";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		sql="truncate t_mc_outhosp_temperature";
		st=passmysqlconn.createStatement();
		st.execute(sql);
		
		System.out.println("临时表清理结束");
		
		st.close();
		passmysqlconn.close();
		
		System.out.println("PA数据重置结束");
		
	}
	
	public void passredis(){
		//redis连接
		jedis = new Jedis("172.18.7.160", 6379);
		jedis.auth("123");
		jedis.select(0);

		System.out.println(jedis.ping());
		
		//PASS redis查询
		Set<String> keys = jedis.keys("*X_SA_SR*"); 
		System.out.println("redis-key PASS总数:"+keys.size());
	}
	
	public void passredis_clear(){
		//redis连接
		jedis = new Jedis("172.18.7.160", 6379);
		jedis.auth("123");
		jedis.select(0);

		System.out.println(jedis.ping());
		
		//PASS redis查询
		Set<String> keys = jedis.keys("*X_SA_SR*"); 
		Iterator<String> it=keys.iterator() ;   
		int a =0;
		while(it.hasNext()){
			a=a+1;
		    String key = it.next();   
//				    System.out.println(key);
		    System.out.println(keys.size()+"-->"+a);
		    jedis.del(key);
		}
	}
	
	public void paredis(){
		//redis连接
		jedis = new Jedis("172.18.7.160", 6379);
		jedis.auth("123");
		jedis.select(0);

		System.out.println(jedis.ping());
		
		//PASS redis查询
		Set<String> keys1 = jedis.keys(PA_SCREENRESULTS);
		System.out.println("redis-key PA总数:"+keys1.size());
		
//		jedis.del(PA_SCREENRESULTS);
//		System.out.println(jedis.get(PA_SCREENRESULTS));
		
		//redis数据类型为list时
//		System.out.println("list内部数据总数："+jedis.llen(PA_SCREENRESULTS));
//		System.out.println(jedis.lrange(PA_SCREENRESULTS, 0, 10));
		
		//jedis.lrange(key, start, len); len=-1表示不限制
//		List<String> values = jedis.lrange("PA_SCREENRESULT_LIST", 0, -1);
//		for(int i=0;i<values.size();i++){
//			System.out.println(values.get(i));
//		}
		
	}
	
	public void paredis_clear(){
		//redis连接
		jedis = new Jedis("172.18.7.160", 6379);
		jedis.auth("123");
		jedis.select(0);

		System.out.println(jedis.ping());
		
		jedis.del(PA_SCREENRESULTS);
		System.out.println("redis剩余数量："+jedis.llen(PA_SCREENRESULTS));
	}
	
	public void pass_code() throws ClassNotFoundException, SQLException, IOException, TimeoutException{
		int match_scheme=6;
		String rhmmurl="http://172.18.7.160:8088/passrhmm";
		
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		Statement st=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql=null;
		List list=null;
		
		//mc_dict_allergen
		sql="delete from mc_dict_allergen where match_scheme="+match_scheme;
		st=passmysqlconn.createStatement();
		st.executeUpdate(sql);
		
		sql="select * from mc_dict_allergen where match_scheme=4";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		
		passmysqlconn.setAutoCommit(false);
		sql="insert into mc_dict_allergen(searchcode, allercode, match_user, pass_allertype, is_save, createdate, "
				+ "pass_allerid, allername, match_scheme, match_time, unable_match_desc, unable_match, pass_allername) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pst=passmysqlconn.prepareStatement(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			pst.setString(1,map.get("searchcode").toString());//[searchcode
			pst.setString(2,map.get("allercode").toString());//allercode
			pst.setString(3,map.get("match_user").toString());//match_user
			pst.setString(4,map.get("pass_allertype").toString());//pass_allertype
			pst.setString(5,map.get("is_save").toString());//is_save
			pst.setString(6,map.get("createdate").toString());//createdate
			pst.setString(7,map.get("pass_allerid").toString());//pass_allerid
			pst.setString(8,map.get("allername").toString());//allername
			pst.setInt(9,match_scheme);//match_scheme
			pst.setString(10,map.get("match_time").toString());//match_time
			if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
				pst.setString(11,"");//unable_match_desc
			}else{
				pst.setString(11,map.get("unable_match_desc").toString());
			}
			pst.setString(12,map.get("unable_match").toString());//unable_match
			pst.setString(13,map.get("pass_allername").toString());//pass_allername]
			
			pst.addBatch();
		}
		pst.executeBatch();
		passmysqlconn.commit();
		
		//mc_dict_costitem
		sql="delete from mc_dict_costitem where match_scheme="+match_scheme;
		st.executeUpdate(sql);
		
		sql="select * from mc_dict_costitem where match_scheme=4";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		
		passmysqlconn.setAutoCommit(false);
		sql="insert into mc_dict_costitem(inserttime, itemtype, searchcode, itemname, is_save, is_byx, match_scheme, itemcode) "
				+ "values(?,?,?,?,?,?,?,?)";
		pst=passmysqlconn.prepareStatement(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			pst.setString(1,map.get("inserttime").toString());//[inserttime
			pst.setString(2,map.get("itemtype").toString());//itemtype
			if("".equals(map.get("searchcode")) || map.get("searchcode")==null){
				pst.setString(3,"");//searchcode
			}else{
				pst.setString(3,map.get("searchcode").toString());//searchcode
			}
			pst.setString(4,map.get("itemname").toString());//itemname
			pst.setString(5,map.get("is_save").toString());//is_save
			pst.setString(6,map.get("is_byx").toString());//is_byx
			pst.setInt(7,match_scheme);//match_scheme
			pst.setString(8,map.get("itemcode").toString());//itemcode]
			
			pst.addBatch();
		}
		pst.executeBatch();
		passmysqlconn.commit();
		
		//mc_dict_dept
		sql="delete from mc_dict_dept where match_scheme="+match_scheme;
		st.executeUpdate(sql);
		
		sql="select * from mc_dict_dept where match_scheme=4";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		
		passmysqlconn.setAutoCommit(false);
		sql="insert into mc_dict_dept(inserttime, searchcode, deptcode, is_save, is_inhosp, match_scheme, "
				+ "is_emergency, deptname, is_clinic) values(?,?,?,?,?,?,?,?,?)";
		pst=passmysqlconn.prepareStatement(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			pst.setString(1,map.get("inserttime").toString());//[inserttime
			pst.setString(2,map.get("searchcode").toString());//searchcode
			pst.setString(3,map.get("deptcode").toString());//deptcode
			pst.setString(4,map.get("is_save").toString());//is_save
			pst.setString(5,map.get("is_inhosp").toString());//is_inhosp
			pst.setInt(6,match_scheme);//match_scheme
			pst.setString(7,map.get("is_emergency").toString());//is_emergency
			pst.setString(8,map.get("deptname").toString());//deptname
			pst.setString(9,map.get("is_clinic").toString());//is_clinic]
			
			pst.addBatch();
		}
		pst.executeBatch();
		passmysqlconn.commit();
		
		//mc_dict_disease
		sql="delete from mc_dict_disease where match_scheme="+match_scheme;
		st.executeUpdate(sql);
		
		sql="select * from mc_dict_disease where match_scheme=4";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		
		passmysqlconn.setAutoCommit(false);
		sql="insert into mc_dict_disease(searchcode, typecode, match_user, is_save, createdate, is_mxb, "
				+ "match_scheme, disname, pass_icd_code, pass_icd_name, dis_type, discode, typename, "
				+ "match_time, unable_match_desc, unable_match) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pst=passmysqlconn.prepareStatement(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			pst.setString(1,map.get("searchcode").toString());//[searchcode
			if("".equals(map.get("typecode")) || map.get("typecode")==null){
				pst.setString(2,"");//typecode
			}else{
				pst.setString(2,map.get("typecode").toString());//typecode
			}
			if("".equals(map.get("match_user")) || map.get("match_user")==null){
				pst.setString(3,"");//match_user
			}else{
				pst.setString(3,map.get("match_user").toString());//match_user
			}
			
			pst.setString(4,map.get("is_save").toString());//is_save
			pst.setString(5,map.get("createdate").toString());//createdate
			pst.setString(6,map.get("is_mxb").toString());//is_mxb
			pst.setInt(7,match_scheme);//match_scheme
			pst.setString(8,map.get("disname").toString());//disname
			if("".equals(map.get("pass_icd_code")) || map.get("pass_icd_code")==null){
				pst.setString(9,"");//pass_icd_code
			}else{
				pst.setString(9,map.get("pass_icd_code").toString());//pass_icd_code
			}
			if("".equals(map.get("pass_icd_name")) || map.get("pass_icd_name")==null){
				pst.setString(10,"");//pass_icd_code
			}else{
				pst.setString(10,map.get("pass_icd_name").toString());//pass_icd_name
			}
			pst.setString(11,map.get("dis_type").toString());//dis_type
			pst.setString(12,map.get("discode").toString());//discode
			if("".equals(map.get("typename")) || map.get("typename")==null){
				pst.setString(13,"");//pass_icd_code
			}else{
				pst.setString(13,map.get("typename").toString());//typename
			}
			if("".equals(map.get("match_time")) || map.get("match_time")==null){
				pst.setString(14,"");//pass_icd_code
			}else{
				pst.setString(14,map.get("match_time").toString());//match_time
			}
			if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
				pst.setString(15,"");//pass_icd_code
			}else{
				pst.setString(15,map.get("unable_match_desc").toString());//unable_match_desc
			}
			
			pst.setString(16,map.get("unable_match").toString());//unable_match]
			
			pst.addBatch();
		}
		pst.executeBatch();
		passmysqlconn.commit();
		
		//mc_dict_drug_pass
		sql="delete from mc_dict_drug_pass where match_scheme="+match_scheme;
		st.executeUpdate(sql);
		
		sql="select * from mc_dict_drug_pass where match_scheme=4";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		
		passmysqlconn.setAutoCommit(false);
		sql="insert into mc_dict_drug_pass(searchcode, oprpi_time, match_scheme, pass_drugname, "
				+ "comp_name, menulabel, drugname, drugform, drugcode, pass_drugcode, pass_upstate, proid, "
				+ "pass_approvalcode, pass_form_name, match_time, unable_match_desc, unable_match, pass_st_strength,"
				+ " match_user, drug_unique_code, approvalcode, pass_dividend, pass_divisor, pass_st_comp_name, "
				+ "drugspec, oprpi_user, pass_doseunit, pass_nametype, doseunit) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pst=passmysqlconn.prepareStatement(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			pst.setString(1,map.get("searchcode").toString());//[searchcode
			if("".equals(map.get("oprpi_time")) || map.get("oprpi_time")==null){
				pst.setString(2,"");//oprpi_time
			}else{
				pst.setString(2,map.get("oprpi_time").toString());//oprpi_time
			}
			
			pst.setInt(3,match_scheme);//match_scheme
			if("".equals(map.get("pass_drugname")) || map.get("pass_drugname")==null){
				pst.setString(4,"");//pass_drugname
			}else{
				pst.setString(4,map.get("pass_drugname").toString());//pass_drugname
			}
			if("".equals(map.get("comp_name")) || map.get("comp_name")==null){
				pst.setString(5,"");//comp_name
			}else{
				pst.setString(5,map.get("comp_name").toString());//comp_name
			}
			
			if("".equals(map.get("menulabel")) || map.get("menulabel")==null){
				pst.setString(6,"");//menulabel
			}else{
				pst.setString(6,map.get("menulabel").toString());//menulabel
			}
			
			pst.setString(7,map.get("drugname").toString());//drugname
			if("".equals(map.get("drugform")) || map.get("drugform")==null){
				pst.setString(8,"");//drugform
			}else{
				pst.setString(8,map.get("drugform").toString());//drugform
			}
			
			pst.setString(9,map.get("drugcode").toString());//drugcode
			pst.setString(10,map.get("pass_drugcode").toString());//pass_drugcode
			if("".equals(map.get("pass_upstate")) || map.get("pass_upstate")==null){
				pst.setInt(11,0);//pass_upstate
			}else{
				pst.setInt(11,Integer.parseInt(map.get("pass_upstate").toString()));//pass_upstate
			}
			
			pst.setString(12,map.get("proid").toString());//proid
			if("".equals(map.get("pass_approvalcode")) || map.get("pass_approvalcode")==null){
				pst.setString(13,"");//pass_approvalcode
			}else{
				pst.setString(13,map.get("pass_approvalcode").toString());//pass_approvalcode
			}
			if("".equals(map.get("pass_form_name")) || map.get("pass_form_name")==null){
				pst.setString(14,"");//pass_form_name
			}else{
				pst.setString(14,map.get("pass_form_name").toString());//pass_form_name
			}
			if("".equals(map.get("match_time")) || map.get("match_time")==null){
				pst.setString(15,"");//match_time
			}else{
				pst.setString(15,map.get("match_time").toString());//match_time
			}
			if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
				pst.setString(16,"");//unable_match_desc
			}else{
				pst.setString(16,map.get("unable_match_desc").toString());//unable_match_desc
			}
			
			pst.setString(17,map.get("unable_match").toString());//unable_match
			if("".equals(map.get("pass_st_strength")) || map.get("pass_st_strength")==null){
				pst.setString(18,"");//pass_st_strength
			}else{
				pst.setString(18,map.get("pass_st_strength").toString());//pass_st_strength
			}
			if("".equals(map.get("match_user")) || map.get("match_user")==null){
				pst.setString(19,"");//match_user
			}else{
				pst.setString(19,map.get("match_user").toString());//match_user
			}
			
			pst.setString(20,map.get("drug_unique_code").toString());//drug_unique_code
			if("".equals(map.get("approvalcode")) || map.get("approvalcode")==null){
				pst.setString(21,"");//approvalcode
			}else{
				pst.setString(21,map.get("approvalcode").toString());//approvalcode
			}
			
			pst.setString(22,map.get("pass_dividend").toString());//pass_dividend
			pst.setString(23,map.get("pass_divisor").toString());//pass_divisor
			if("".equals(map.get("pass_st_comp_name")) || map.get("pass_st_comp_name")==null){
				pst.setString(24,"");//pass_st_comp_name
			}else{
				pst.setString(24,map.get("pass_st_comp_name").toString());//pass_st_comp_name
			}
			if("".equals(map.get("drugspec")) || map.get("drugspec")==null){
				pst.setString(25,"");//oprpi_user
			}else{
				pst.setString(25,map.get("drugspec").toString());//drugspec
			}
			
			if("".equals(map.get("oprpi_user")) || map.get("oprpi_user")==null){
				pst.setString(26,"");//oprpi_user
			}else{
				pst.setString(26,map.get("oprpi_user").toString());//oprpi_user
			}
			if("".equals(map.get("pass_doseunit")) || map.get("pass_doseunit")==null){
				pst.setString(27,"");//pass_doseunit
			}else{
				pst.setString(27,map.get("pass_doseunit").toString());//pass_doseunit
			}
			
			pst.setString(28,map.get("pass_nametype").toString());//pass_nametype
			pst.setString(29,map.get("doseunit").toString());//doseunit]
			
			pst.addBatch();
		}
		pst.executeBatch();
		passmysqlconn.commit();
		
		
		//mc_dict_route
		sql="delete from mc_dict_route where match_scheme="+match_scheme;
		st.executeUpdate(sql);
		
		sql="select * from mc_dict_route where match_scheme=4";
		st=passmysqlconn.createStatement();
		rs=st.executeQuery(sql);
		list=passmysql.getlist(rs);
		
		passmysqlconn.setAutoCommit(false);
		sql="insert into mc_dict_route(searchcode, match_user, is_save, createdate, match_scheme, routename, "
				+ "isskintest, pass_route_name, routecode, route_type, abbrev, pass_routeid, match_time, "
				+ "unable_match_desc, unable_match) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pst=passmysqlconn.prepareStatement(sql);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			pst.setString(1,map.get("searchcode").toString());//[searchcode
			if("".equals(map.get("match_user")) || map.get("match_user")==null){
				pst.setString(2,"");//match_user
			}else{
				pst.setString(2,map.get("match_user").toString());//match_user
			}
			
			pst.setString(3,map.get("is_save").toString());//is_save
			pst.setString(4,map.get("createdate").toString());//createdate
			pst.setInt(5,match_scheme);//match_scheme
			pst.setString(6,map.get("routename").toString());//routename
			pst.setString(7,map.get("isskintest").toString());//isskintest
			if("".equals(map.get("pass_route_name")) || map.get("pass_route_name")==null){
				pst.setString(8,"");//pass_route_name
			}else{
				pst.setString(8,map.get("pass_route_name").toString());//pass_route_name
			}
			
			pst.setString(9,map.get("routecode").toString());//routecode
			pst.setString(10,map.get("route_type").toString());//route_type
			if("".equals(map.get("abbrev")) || map.get("abbrev")==null){
				pst.setString(11,"");//pass_doseunit
			}else{
				pst.setString(11,map.get("abbrev").toString());//abbrev
			}
			
			pst.setString(12,map.get("pass_routeid").toString());//pass_routeid
			if("".equals(map.get("match_time")) || map.get("match_time")==null){
				pst.setString(13,"");//match_time
			}else{
				pst.setString(13,map.get("match_time").toString());//match_time
			}
			
			if("".equals(map.get("unable_match_desc")) || map.get("unable_match_desc")==null){
				pst.setString(14,"");//pass_doseunit
			}else{
				pst.setString(14,map.get("unable_match_desc").toString());//unable_match_desc
			}
			
			pst.setString(15,map.get("unable_match").toString());//unable_match]
			
			pst.addBatch();
		}
		pst.executeBatch();
		passmysqlconn.commit();
		
		//刷新redis
		Passservice Passservice=new Passservice();
		String result_clean = Passservice.getPassResult("",rhmmurl+"/redis/cleanRedis") ; 
		System.out.println("clean-redis,"+result_clean);
    	
    	String result_sys = Passservice.getPassResult("",rhmmurl+"/redis/updateSys") ;
		System.out.println("update-Sys"+result_sys);
		
    	String result_custom =Passservice.getPassResult("",rhmmurl+"/redis/updateCustom") ; 
    	System.out.println("update-custom"+result_custom);
    	
    	String result_shield = Passservice.getPassResult("",rhmmurl+"/redis/updateShield") ; 
    	System.out.println("update-shield"+result_shield);
		
    	rs.close();
		st.close();
		pst.close();
		passmysqlconn.close();
	}
	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, TimeoutException, InterruptedException, ParseException{
		Autoscreen autoscreen=new Autoscreen();
		
		//PASS功能===========================
		
		//检查案例里面的医生、科室、疾病等是否在字典表中
//		autoscreen.Checkdata();
		
			//调用PASS审查，制造redis数据
			//清空临时表,
//		autoscreen.rebootlinshibiao();
//		long startTime = System.currentTimeMillis();
//		autoscreen.PASS();
//		long endTime = System.currentTimeMillis();
//		System.out.println("总耗时："+(endTime-startTime)+"毫秒");
		
		//redis查询
//		autoscreen.passredis();
		//redis清空方法
//		autoscreen.passredis_clear();
		
		/**
		 * 制作PASS统计分析数据
		 */
//		long startTime = System.currentTimeMillis();
		
			//PASS统计分析数据准备，准备一份案例，通过PASS审查，通过AP工程从redis里面将数据预处理到临时表，1,拷贝1份出来，2，拷贝两份出来
			//清空分表正式表数据，清空临时表,
		autoscreen.rebootlinshibiao();
//		autoscreen.rebootfenbiao();
		
			//临时表制造大数据（方案1：拷贝两份出来）（不考虑使用这个方法）
//		autoscreen.copydata();
		
			//临时表制造大数据,（方案2：拷贝1份出来）
		autoscreen.copydata1();
		
//			//从临时表导数据到正式表（分别导入到12份分表）
//		autoscreen.daofenbiao();
		
//		long endTime = System.currentTimeMillis();
//		System.out.println("总耗时："+(endTime-startTime)+"毫秒");
		
		
		//PA功能=====================
		
			//调用pa自动审查接口
//		long startTime = System.currentTimeMillis();
			//十万级病人数据审查时间估计15分钟
//		autoscreen.PA();
//		long endTime = System.currentTimeMillis();
//		System.out.println("总耗时："+(endTime-startTime)+"毫秒");
		
		//redis查询
//		autoscreen.paredis();
		//redis清空方法
//		autoscreen.paredis_clear();
		
//		//制造PA案例挂接时所需要的门诊、出院、住院数据
//		autoscreen.PA_data("mz");
		
		//pa--HIS数据模拟
		//制作新的HISCODE配对数据，复制原有的配对数据来生成
		//如果是一套新的字典数据，需要通过这个方法把数据单独刷到redis
		//这个作废，不要使用
//		autoscreen.pass_code();//这个作废，不要使用，目前PA数据已经有了，然后重新导会导致维护的案例失效
		
		//PA库表清理重置
			//1.记得修改mc_import_info时间,和KID
			//2.记得修改HIS库数据日期
//		autoscreen.PAclear();
		
		//制造HIS数据到oracle库，记得修改数据时间
//		autoscreen.mysql_O_data();
//		autoscreen.mysql_O_data("HISCODE001");
	}
}
