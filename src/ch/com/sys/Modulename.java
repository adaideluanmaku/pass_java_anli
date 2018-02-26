package ch.com.sys;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.ch.jdbc.Mysqlconn;

public class Modulename {
	public int anlitype=0;
	public int moduleid=0;
	public String modulename;
	
	public int getAnlitype() {
		return anlitype;
	}

	public int getModuleid() {
		return moduleid;
	}

	public String getModulename() {
		return modulename;
	}

	//	public String getName(int anlitype1, int moduleid1){
//		String name=null;
//		if(anlitype==0&&moduleid==0){
//			name="test����";
//		}else if(anlitype==0&&moduleid==1){//�Զ����
//			name="������Χ";
//		}else if(anlitype==0&&moduleid==2){
//			name="���𺦼���";
//		}else if(anlitype==0&&moduleid==3){
//			name="���𺦼���";
//		}else if(anlitype==0&&moduleid==4){
//			name="�໥����";
//		}else if(anlitype==0&&moduleid==5){
//			name="��������";
//		}else if(anlitype==0&&moduleid==6){
//			name="����Ũ��";
//		}else if(anlitype==0&&moduleid==7){
//			name="ҩ�����֢";
//		}else if(anlitype==0&&moduleid==8){
//			name="������Ӧ";
//		}else if(anlitype==0&&moduleid==9){
//			name="����Ӧ֢";
//		}else if(anlitype==0&&moduleid==10){
//			name="��ͯ��ҩ";
//		}else if(anlitype==0&&moduleid==11){
//			name="������ҩ";
//		}else if(anlitype==0&&moduleid==12){
//			name="������ҩ";
//		}else if(anlitype==0&&moduleid==13){
//			name="������ҩ";
//		}else if(anlitype==0&&moduleid==14){
//			name="������ҩ";
//		}else if(anlitype==0&&moduleid==15){
//			name="�Ա���ҩ";
//		}else if(anlitype==0&&moduleid==16){
//			name="ҩ�����";
//		}else if(anlitype==0&&moduleid==17){
//			name="��ҩ;��";
//		}else if(anlitype==0&&moduleid==18){
//			name="�ظ���ҩ";
//		}else if(anlitype==0&&moduleid==19){
//			name="ԽȨ��ҩ";
//		}else if(anlitype==0&&moduleid==20){
//			name="Χ������ҩ";
//		}else if(anlitype==0&&moduleid==21){
//			name="ϸ����ҩ��";
//		}else if(anlitype==1&&moduleid==1){//�ֶ����
//			name="������Χ";
//		}else if(anlitype==1&&moduleid==2){
//			name="���𺦼���";
//		}else if(anlitype==1&&moduleid==3){
//			name="���𺦼���";
//		}else if(anlitype==1&&moduleid==4){
//			name="�໥����";
//		}else if(anlitype==1&&moduleid==5){
//			name="��������";
//		}else if(anlitype==1&&moduleid==6){
//			name="����Ũ��";
//		}else if(anlitype==1&&moduleid==7){
//			name="ҩ�����֢";
//		}else if(anlitype==1&&moduleid==8){
//			name="������Ӧ";
//		}else if(anlitype==1&&moduleid==9){
//			name="����Ӧ֢";
//		}else if(anlitype==1&&moduleid==10){
//			name="��ͯ��ҩ";
//		}else if(anlitype==1&&moduleid==11){
//			name="������ҩ";
//		}else if(anlitype==1&&moduleid==12){
//			name="������ҩ";
//		}else if(anlitype==1&&moduleid==13){
//			name="������ҩ";
//		}else if(anlitype==1&&moduleid==14){
//			name="������ҩ";
//		}else if(anlitype==1&&moduleid==15){
//			name="�Ա���ҩ";
//		}else if(anlitype==1&&moduleid==16){
//			name="ҩ�����";
//		}else if(anlitype==1&&moduleid==17){
//			name="��ҩ;��";
//		}else if(anlitype==1&&moduleid==18){
//			name="�ظ���ҩ";
//		}else if(anlitype==1&&moduleid==19){
//			name="ԽȨ��ҩ";
//		}else if(anlitype==1&&moduleid==20){
//			name="Χ������ҩ";
//		}else if(anlitype==1&&moduleid==21){
//			name="ϸ����ҩ��";
//		}else if(anlitype==2&&moduleid==11){//�ֶ�˵����
//		name="ҩƷ˵����";
//		}else if(anlitype==2&&moduleid==14){
//			name="�Զ���ҩƷ˵����";
//		}else if(anlitype==3&&moduleid==51){
//			name="��Ҫ��Ϣ";
//		}else if(anlitype==3&&moduleid==52){
//			name="��Ҫ��ʾ";
//		}
//		return name;
//	}
	
//	public void module(String Name){
//		if(Name.contains("��������")){//�жϲ�ѯģ��
//			moduleid=52;
//			modulename="��Ҫ��ʾ";
//		}else if(Name.contains("˵����")){
//			moduleid=11;
//			modulename="˵����";
//		}else if(Name.contains("��Χ")  || Name.contains("������")){//�ж����ģ��
//			moduleid=1;
//			modulename="������Χ";
//		}else if(Name.contains("����")){
//			moduleid=2;
//			modulename="���𺦼���";
//		}else if(Name.contains("����")){
//			moduleid=3;
//			modulename="���𺦼���";
//		}else if(Name.contains("�໥")){
//			moduleid=4;
//			modulename="�໥����";
//		}else if(Name.contains("����") || Name.contains("����-")){
//			moduleid=5;
//			modulename="��������";
//		}else if(Name.contains("����Ũ��") || Name.contains("����-Ũ��") || Name.contains("������")){
//			moduleid=6;
//			modulename="����Ũ��";
//		}else if(Name.contains("ҩ�����") || Name.contains("ҩ��-����")){
//			moduleid=7;
//			modulename="ҩ�����";
//		}else if(Name.contains("����")){
//			moduleid=8;
//			modulename="������Ӧ";
//		}else if(Name.contains("����")){
//			moduleid=9;
//			modulename="����Ӧ֢";
//		}else if(Name.contains("��ͯ")){
//			moduleid=10;
//			modulename="��ͯ��ҩ";
//		}else if(Name.contains("����")){
//			moduleid=11;
//			modulename="������ҩ";
//		}else if(Name.contains("����")){
//			moduleid=12;
//			modulename="������ҩ";
//		}else if(Name.contains("����")){
//			moduleid=13;
//			modulename="������ҩ";
//		}else if(Name.contains("����")){
//			moduleid=14;
//			modulename="������ҩ";
//		}else if(Name.contains("�Ա�")){
//			moduleid=15;
//			modulename="�Ա���ҩ";
//		}else if(Name.contains("����")){
//			moduleid=16;
//			modulename="ҩ�����";
//		}else if(Name.contains("��ҩ")){
//			moduleid=17;
//			modulename="��ҩ;��";
//		}else if(Name.contains("�ظ�")){
//			moduleid=18;
//			modulename="�ظ���ҩ";
//		}else if(Name.contains("ԽȨ")){
//			moduleid=19;
//			modulename="ԽȨ��ҩ";
//		}else if(Name.contains("Χ��") ){
//			moduleid=20;
//			modulename="Χ������";
//		}else if(Name.contains("ϸ��")){
//			moduleid=21;
//			modulename="ϸ����ҩ";
//		}else{
//			moduleid=0;
//			modulename="δƥ��";
//		}
//	}
	
	public void module1(String Name) throws ClassNotFoundException, SQLException, IOException{
		Mysqlconn mysql=new Mysqlconn();
		Connection mysqlconn=mysql.getConn();
		Statement st=null;
		ResultSet rs=null;
		String sql=null;
		
		StrLevenshtein strLevenshtein = new StrLevenshtein();
		sql="select moduleid,modulename from  mc_modulename where moduletype=1";
		st= mysqlconn.createStatement();
		rs=st.executeQuery(sql);
		List list = mysql.getlist(rs);
		int maxint=0;
		for(int i=0; i<list.size();i++){
			Map map = (Map)list.get(i);
//			float strint=strLevenshtein.getSimilarityRatio(map.get("modulename").toString(),Name);
			int strint=strLevenshtein.str2tosame(map.get("modulename").toString(),Name);
			if(strint>maxint){
				maxint=strint;
				modulename=map.get("modulename").toString();
				moduleid=Integer.parseInt(map.get("moduleid").toString());
//				if(strint==1){
//					break;
//				}
			}
		}
		
		if(maxint==0){
			moduleid=0;
			modulename="未定位";
		}
		rs.close();
		st.close();
		mysqlconn.close();
	}
	
	public void getId(String Name) throws ClassNotFoundException, SQLException, IOException{
		
		if(Name.contains("浮动")){//fudong
			anlitype=4;
			module1(Name);
		}else if(Name.contains("手动")){//shoudong
			anlitype=2;
			module1(Name);
		}else if(Name.contains("说明书")){//shuomingshu
			anlitype=3;
			module1(Name);
		}else  if(Name.contains("ZYZDY")){//ziyouzidingyi
			anlitype=10;
			module1(Name);
		}else{//
			module1(Name);//
			if(moduleid>0){
				anlitype=1;//
			}else{
				anlitype=9;//
			}
		}
	}
}
