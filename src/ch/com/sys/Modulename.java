package ch.com.sys;

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
//			name="test案例";
//		}else if(anlitype==0&&moduleid==1){//自动审查
//			name="剂量范围";
//		}else if(anlitype==0&&moduleid==2){
//			name="肝损害剂量";
//		}else if(anlitype==0&&moduleid==3){
//			name="肾损害剂量";
//		}else if(anlitype==0&&moduleid==4){
//			name="相互作用";
//		}else if(anlitype==0&&moduleid==5){
//			name="体外配伍";
//		}else if(anlitype==0&&moduleid==6){
//			name="配伍浓度";
//		}else if(anlitype==0&&moduleid==7){
//			name="药物禁忌症";
//		}else if(anlitype==0&&moduleid==8){
//			name="不良反应";
//		}else if(anlitype==0&&moduleid==9){
//			name="超适应症";
//		}else if(anlitype==0&&moduleid==10){
//			name="儿童用药";
//		}else if(anlitype==0&&moduleid==11){
//			name="成人用药";
//		}else if(anlitype==0&&moduleid==12){
//			name="老人用药";
//		}else if(anlitype==0&&moduleid==13){
//			name="妊娠用药";
//		}else if(anlitype==0&&moduleid==14){
//			name="哺乳用药";
//		}else if(anlitype==0&&moduleid==15){
//			name="性别用药";
//		}else if(anlitype==0&&moduleid==16){
//			name="药物过敏";
//		}else if(anlitype==0&&moduleid==17){
//			name="给药途径";
//		}else if(anlitype==0&&moduleid==18){
//			name="重复用药";
//		}else if(anlitype==0&&moduleid==19){
//			name="越权用药";
//		}else if(anlitype==0&&moduleid==20){
//			name="围术期用药";
//		}else if(anlitype==0&&moduleid==21){
//			name="细菌耐药率";
//		}else if(anlitype==1&&moduleid==1){//手动审查
//			name="剂量范围";
//		}else if(anlitype==1&&moduleid==2){
//			name="肝损害剂量";
//		}else if(anlitype==1&&moduleid==3){
//			name="肾损害剂量";
//		}else if(anlitype==1&&moduleid==4){
//			name="相互作用";
//		}else if(anlitype==1&&moduleid==5){
//			name="体外配伍";
//		}else if(anlitype==1&&moduleid==6){
//			name="配伍浓度";
//		}else if(anlitype==1&&moduleid==7){
//			name="药物禁忌症";
//		}else if(anlitype==1&&moduleid==8){
//			name="不良反应";
//		}else if(anlitype==1&&moduleid==9){
//			name="超适应症";
//		}else if(anlitype==1&&moduleid==10){
//			name="儿童用药";
//		}else if(anlitype==1&&moduleid==11){
//			name="成人用药";
//		}else if(anlitype==1&&moduleid==12){
//			name="老人用药";
//		}else if(anlitype==1&&moduleid==13){
//			name="妊娠用药";
//		}else if(anlitype==1&&moduleid==14){
//			name="哺乳用药";
//		}else if(anlitype==1&&moduleid==15){
//			name="性别用药";
//		}else if(anlitype==1&&moduleid==16){
//			name="药物过敏";
//		}else if(anlitype==1&&moduleid==17){
//			name="给药途径";
//		}else if(anlitype==1&&moduleid==18){
//			name="重复用药";
//		}else if(anlitype==1&&moduleid==19){
//			name="越权用药";
//		}else if(anlitype==1&&moduleid==20){
//			name="围术期用药";
//		}else if(anlitype==1&&moduleid==21){
//			name="细菌耐药率";
//		}else if(anlitype==2&&moduleid==11){//手动说明书
//		name="药品说明书";
//		}else if(anlitype==2&&moduleid==14){
//			name="自定义药品说明书";
//		}else if(anlitype==3&&moduleid==51){
//			name="简要信息";
//		}else if(anlitype==3&&moduleid==52){
//			name="重要提示";
//		}
//		return name;
//	}
	
	public void module(String Name){
		if(Name.contains("浮动窗口")){//判断查询模块
			moduleid=52;
			modulename="重要提示";
		}else if(Name.contains("说明书")){
			moduleid=11;
			modulename="说明书";
		}else if(Name.contains("范围")  || Name.contains("超多日")){//判断审查模块
			moduleid=1;
			modulename="剂量范围";
		}else if(Name.contains("肝损")){
			moduleid=2;
			modulename="肝损害剂量";
		}else if(Name.contains("肾损")){
			moduleid=3;
			modulename="肾损害剂量";
		}else if(Name.contains("相互")){
			moduleid=4;
			modulename="相互作用";
		}else if(Name.contains("体外") || Name.contains("体外-")){
			moduleid=5;
			modulename="体外配伍";
		}else if(Name.contains("配伍浓度") || Name.contains("配伍-浓度") || Name.contains("钾离子")){
			moduleid=6;
			modulename="配伍浓度";
		}else if(Name.contains("药物禁忌") || Name.contains("药物-禁忌")){
			moduleid=7;
			modulename="药物禁忌";
		}else if(Name.contains("不良")){
			moduleid=8;
			modulename="不良反应";
		}else if(Name.contains("超适")){
			moduleid=9;
			modulename="超适应症";
		}else if(Name.contains("儿童")){
			moduleid=10;
			modulename="儿童用药";
		}else if(Name.contains("成人")){
			moduleid=11;
			modulename="成人用药";
		}else if(Name.contains("老人")){
			moduleid=12;
			modulename="老人用药";
		}else if(Name.contains("妊娠")){
			moduleid=13;
			modulename="妊娠用药";
		}else if(Name.contains("哺乳")){
			moduleid=14;
			modulename="哺乳用药";
		}else if(Name.contains("性别")){
			moduleid=15;
			modulename="性别用药";
		}else if(Name.contains("过敏")){
			moduleid=16;
			modulename="药物过敏";
		}else if(Name.contains("给药")){
			moduleid=17;
			modulename="给药途径";
		}else if(Name.contains("重复")){
			moduleid=18;
			modulename="重复用药";
		}else if(Name.contains("越权")){
			moduleid=19;
			modulename="越权用药";
		}else if(Name.contains("围手") ){
			moduleid=20;
			modulename="围手术期";
		}else if(Name.contains("细菌")){
			moduleid=21;
			modulename="细菌耐药";
		}else{
			moduleid=0;
			modulename="未匹配";
		}
	}
	
	public void getId(String Name){
		
		if(Name.contains("浮动")){//浮动窗口案例
			anlitype=4;
			module(Name);
		}else if(Name.contains("手动")){//手动案例
			anlitype=2;
			module(Name);
		}else if(Name.contains("说明书")){//说明书案例
			anlitype=3;
			module(Name);
		}else {//未定位案例
			module(Name);//先获取模块定位
			if(moduleid>0){
				anlitype=1;//自动案例
			}else{
				anlitype=9;//未定位
			}
		}
	}
}
