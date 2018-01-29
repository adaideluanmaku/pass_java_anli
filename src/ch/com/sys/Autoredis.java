package ch.com.sys;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.ch.jdbc.Mysqlconn;
import com.ch.jdbc.PassMysqlconn;
import com.ch.redissys.Redis_conf;
import com.ch.redissys.Redis_update;
import com.medicom.passlan.inter.imp.shield.ShieldImple;
import com.medicom.passlan.redis.shield.ShieldData;

//����ά������ϵͳ��ˢredis����
public class Autoredis {
	public String tablename;
	public String selectsql;
	
	//ͨ��ά�����߸�������redis
	public void allupdate(String anliname,String version) throws ClassNotFoundException, SQLException, IOException, TimeoutException{
		InputStream in=PassMysqlconn.class.getClassLoader().getResourceAsStream("config.properties");
		Properties prop=new Properties();		
		prop.load(in);
		String redisurl=prop.getProperty("passlanmanage");
		
		//����mysql���ݿ�
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		//����PASSmysql���ݿ�
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select * from shoudong_redis where anliname=? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, anliname);
		pst.setString(2, version);
		rs=pst.executeQuery();
		List list=mysql.getlist(rs);
		
		if(list.size()>0){
			for(int i=0; i<list.size();i++){
				Map map=(Map)list.get(i);
				String shuodongsql=map.get("sql").toString();
				
				//����PASS�������ӵ�PASSPA2DB���ݿ�
				sql=shuodongsql;
				st=passmysqlconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}
			Passservice Passservice=new Passservice();
			String result_clean = Passservice.getPassResult("",redisurl+"/redis/cleanRedis") ; 
			System.out.println("���redis,"+result_clean);
	    	
	    	//��ʼ��ϵͳ����
	    	String result_sys = Passservice.getPassResult("",redisurl+"/redis/updateSys") ;
			System.out.println("����redicsϵͳ���棬"+result_sys);
			
			//��ʼ��ϵͳ�Զ��������
	    	String result_custom =Passservice.getPassResult("",redisurl+"/redis/updateCustom") ; 
	    	System.out.println("����redics�Զ�������ݣ�"+result_custom);
	    	
	    	//��ʼ��ϵͳ��������
	    	String result_shield = Passservice.getPassResult("",redisurl+"/redis/updateShield") ; 
	    	System.out.println("����redics�������ݣ�"+result_shield);
	    	
		}
		
		rs.close();
		pst.close();
		mysqlconn.close();
		
	}
	
	//��ͨ��ά�����ߣ�����redis��������
	public void oneupdate(String anliname,String version,String servername) throws ClassNotFoundException, SQLException, IOException, TimeoutException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		Redis_conf Redis_conf=new Redis_conf();
		Redis_conf.setServername(servername);
		Redis_conf.redisconnect();
		
		//����mysql���ݿ�
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		//����PASSmysql���ݿ�
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		sql="select * from shoudong_redis where anliname like ? and version=?";
		pst=mysqlconn.prepareStatement(sql);
		pst.setString(1, "%"+anliname+"%");
		pst.setString(2, version);
		rs=pst.executeQuery();
		List list=mysql.getlist(rs);
		
		if(list.size()>0){
			for(int i=0; i<list.size();i++){
				Map map=(Map)list.get(i);
				String updatesql=map.get("updatesql").toString();
				selectsql=map.get("selectsql").toString();
				tablename=map.get("tablename").toString();
				if(updatesql!=null || !"".equals(updatesql)){
					//����PASS�������ӵ�PASSPA2DB���ݿ�
					System.out.println(updatesql);
					sql=updatesql;
					st=passmysqlconn.createStatement();
					int t=st.executeUpdate(sql);
					st.close();
					if(t>0){
						//������ݸ��³ɹ����͵���redisˢ�»���
						oneupdateredis();
					}else{
						System.out.println("δ�ҵ�����");
					}
				}
			}
			
		rs.close();
		pst.close();
		mysqlconn.close();
		}
	}
	
	//ˢ�µ���redisʱ�������л�������ٸ��µ�redis����
	public void oneupdateredis() throws ClassNotFoundException, SQLException, IOException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		//����PASSmysql���ݿ�
		PassMysqlconn passmysql=new PassMysqlconn();
		Connection passmysqlconn=passmysql.getConn();
		
		PreparedStatement pst;
		Statement st;
		ResultSet rs;
		String sql;
		
		Redis_update Redis_update=new Redis_update();
		
		//�ֵ��
		if("mc_dict_doctor".equals(tablename)){
			System.out.println(selectsql);
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysDoctorInfaceImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("doctorcode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_allergen".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.allerGenImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("allercode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_disease".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.diseaseImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("discode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_drug".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.distDrugImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("drugcode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_cost_dose_unit".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.drugDoseUnitImpl(Integer.parseInt(map.get("match_scheme").toString()), 
						map.get("drugcode").toString(),map.get("drugspec").toString(),map.get("doseunit").toString(),
						map.get("costunit").toString(),map.get("conversion").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_frequency".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.frequencyImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("frequency").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_operation".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.operationImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("operationcode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_pro_drug_reason".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.proDrugReasonImpl(map.get("caseid").toString(), map.get("recipno").toString(),
						map.get("orderDeptCode").toString(), map.get("orderDoctorCode").toString()
						, map.get("druguniquecode").toString(), map.get("moduleId").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_route".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.routeMatchImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("routecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_config".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysConfigImpl(map.get("paraname").toString(), map.get("paramvalue").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_drug_pass".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysDrugMatchImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_hospital_match_relation".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysMatchRelationImpl(map.get("hiscode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_params".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysParamsImpl(map.get("checkmode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_hospital".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysHospitalImpl(map.get("hiscode_user").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_route".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysRoutedictionImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("routecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_dict_disease".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sysDisDisctionImpl(Integer.parseInt(map.get("match_scheme").toString()), map.get("discode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_adult".equals(tablename)){
			//�Զ����
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.adultInfaceImpl(map.get("hiscode").toString(), map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_bacresis".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.bacresisInfaceImpl(map.get("hiscode").toString(), map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_brief".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.brifInfaceImpl(map.get("hiscode").toString(), map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_doctor_priv".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.doctorprivInfaceImpl(map.get("hiscode").toString(), map.get("doctorcode").toString(),
						map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_dosage".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.dosageInfaceImpl(map.get("hiscode").toString(), map.get("druguniquecode").toString(),
						map.get("doseunit").toString(), map.get("routecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_drug_dis".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.drugdisInfaceImpl(Integer.parseInt(map.get("moduleid").toString()),map.get("hiscode").toString(), map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_druglevel".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.drugLevelInfaceImpl(Integer.parseInt(map.get("pkid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_user_duptherapy".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.duptherapyInfaceImpl(Integer.parseInt(map.get("pkid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_user_hepdos".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.hepdosInfaceImpl(map.get("hiscode").toString(),map.get("druguniquecode").toString(),
						map.get("doseunit").toString(),map.get("routecode").toString(),map.get("heplabel").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_inter".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.interInfaceImpl(Integer.parseInt(map.get("pkid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_user_iv".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.ivInfaceImpl(Integer.parseInt(map.get("pkid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_pro_drug_advice".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.operativeAdviceImpl(map.get("caseid").toString());
			}
			rs.close();
			st.close();
		}else if("mc_pro_drug_operation".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.operativeOperImpl(map.get("caseid").toString(),map.get("operationcode").toString());
				Redis_update.operativeTimeImpl(map.get("caseid").toString(),map.get("operationcode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_operation_priv".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.operprivInfaceImpl(map.get("caseid").toString(),map.get("operationcode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_pediatric".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.pediatricInfaceImpl(map.get("hiscode").toString(),map.get("druguniquecode").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_rendos".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.rendosInfaceImpl(map.get("hiscode").toString(),map.get("druguniquecode").toString(),
						map.get("doseunit").toString(),map.get("routecode").toString(),
						map.get("renlabel").toString());
			}
			rs.close();
			st.close();
		}else if("mc_user_drugroute".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.routeInfaceImpl(Integer.parseInt(map.get("pkid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_user_sex".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.sexInfaceImpl(Integer.parseInt(map.get("pkid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_user_unage_sp".equals(tablename)){
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.unagespInfaceImpl(Integer.parseInt(map.get("pkid").toString()),Integer.parseInt(map.get("moduleid").toString()));
			}
			rs.close();
			st.close();
		}else if("mc_user_shielddata".equals(tablename)){
			//���α�
			sql=selectsql;
			st=passmysqlconn.createStatement();
			rs=st.executeQuery(sql);
			List list=passmysql.getlist(rs);
			ShieldImple ShieldImple=new ShieldImple();
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				Redis_update.ShieldImple(Integer.parseInt(map.get("pkid").toString()),Integer.parseInt(map.get("moduleid").toString()));
			}
			rs.close();
			st.close();
		}else{
			System.out.println("δ�ҵ���ˢredis");
		}
		
		
		passmysqlconn.close();
		
	}
}
