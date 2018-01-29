package com.medicom.passlan.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.imp.custom.AdultInfaceImpl;
import com.medicom.passlan.inter.imp.custom.BacresisInfaceImpl;
import com.medicom.passlan.inter.imp.custom.BrifInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DoctorprivInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DosageInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DrugLevelInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DrugdisInfaceImpl;
import com.medicom.passlan.inter.imp.custom.DuptherapyInfaceImpl;
import com.medicom.passlan.inter.imp.custom.HepdosInfaceImpl;
import com.medicom.passlan.inter.imp.custom.InterInfaceImpl;
import com.medicom.passlan.inter.imp.custom.IvInfaceImpl;
import com.medicom.passlan.inter.imp.custom.OperativeAdviceImpl;
import com.medicom.passlan.inter.imp.custom.OperativeOperImpl;
import com.medicom.passlan.inter.imp.custom.OperativeTimeImpl;
import com.medicom.passlan.inter.imp.custom.OperprivInfaceImpl;
import com.medicom.passlan.inter.imp.custom.PediatricInfaceImpl;
import com.medicom.passlan.inter.imp.custom.RendosInfaceImpl;
import com.medicom.passlan.inter.imp.custom.RouteInfaceImpl;
import com.medicom.passlan.inter.imp.custom.SexInfaceImpl;
import com.medicom.passlan.inter.imp.custom.SysDoctorInfaceImpl;
import com.medicom.passlan.inter.imp.custom.UnagespInfaceImpl;
import com.medicom.passlan.inter.imp.shield.ShieldImple;
import com.medicom.passlan.inter.imp.sys.AllerGenImpl;
import com.medicom.passlan.inter.imp.sys.DiseaseImpl;
import com.medicom.passlan.inter.imp.sys.DistDrugImpl;
import com.medicom.passlan.inter.imp.sys.DrugDoseUnitImpl;
import com.medicom.passlan.inter.imp.sys.FrequencyImpl;
import com.medicom.passlan.inter.imp.sys.OperationImpl;
import com.medicom.passlan.inter.imp.sys.ProDrugReasonImpl;
import com.medicom.passlan.inter.imp.sys.RedisClieanImpl;
import com.medicom.passlan.inter.imp.sys.RouteMatchImpl;
import com.medicom.passlan.inter.imp.sys.SysConfigImpl;
import com.medicom.passlan.inter.imp.sys.SysDisDisctionImpl;
import com.medicom.passlan.inter.imp.sys.SysDrugMatchImpl;
import com.medicom.passlan.inter.imp.sys.SysHospitalImpl;
import com.medicom.passlan.inter.imp.sys.SysMatchRelationImpl;
import com.medicom.passlan.inter.imp.sys.SysParamsImpl;
import com.medicom.passlan.inter.imp.sys.SysRoutedictionImpl;

/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歅assLanManage </li>
 * <li>绫诲悕绉帮細  RedisInitService </li>
 * <li>绫绘弿杩帮細   redis绯荤粺鍒濆鍖栫殑澶勭悊绫诲彲浠ョ洿鎺ヤ娇鐢�/li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�8鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
@Service
public class RedisInitService {
	
	private Logger logger = Logger.getLogger(RedisInitService.class);
	
	/**
	 * 鑷畾涔夌殑妯″潡
	 */
	@Autowired
	private AdultInfaceImpl adultInfaceImpl;
	
	@Autowired
	private BacresisInfaceImpl bacresisInfaceImpl;
	
	@Autowired
	private BrifInfaceImpl brifInfaceImpl;
	
	@Autowired
	private DoctorprivInfaceImpl doctorprivInfaceImpl;
	
	@Autowired
	private DosageInfaceImpl dosageInfaceImpl;
	
	@Autowired
	private DrugdisInfaceImpl drugdisInfaceImpl;
	
	@Autowired
	private DrugLevelInfaceImpl drugLevelInfaceImpl;
	
	@Autowired
	private DuptherapyInfaceImpl duptherapyInfaceImpl;
	
	@Autowired
	private HepdosInfaceImpl hepdosInfaceImpl;
	
	@Autowired
	private InterInfaceImpl interInfaceImpl;
	
	@Autowired
	private IvInfaceImpl ivInfaceImpl;
	
	@Autowired
	private OperativeAdviceImpl operativeAdviceImpl;
	
	@Autowired
	private OperativeOperImpl operativeOperImpl;
	
	@Autowired
	private OperativeTimeImpl operativeTimeImpl;
	
	@Autowired
	private OperprivInfaceImpl operprivInfaceImpl;
	
	@Autowired
	private PediatricInfaceImpl pediatricInfaceImpl;
	
	@Autowired
	private RendosInfaceImpl rendosInfaceImpl;
	
	@Autowired
	private RouteInfaceImpl routeInfaceImpl;
	
	@Autowired
	private SexInfaceImpl sexInfaceImpl;
	
	@Autowired
	private UnagespInfaceImpl unagespInfaceImpl;
	
	/**
	 * 绯荤粺鐨勬暟鎹鐞�
	 */
	@Autowired
	private SysDoctorInfaceImpl sysDoctorInfaceImpl;
	
	@Autowired
	private AllerGenImpl allerGenImpl;
	
	@Autowired
	private DiseaseImpl diseaseImpl;
	
	@Autowired
	private DistDrugImpl distDrugImpl;
	
	@Autowired
	private DrugDoseUnitImpl drugDoseUnitImpl;
	
	@Autowired
	private FrequencyImpl frequencyImpl;
	
	@Autowired
	private OperationImpl operationImpl;
	
	@Autowired
	private ProDrugReasonImpl proDrugReasonImpl;
	
	@Autowired
	private RouteMatchImpl routeMatchImpl;
	
	@Autowired
	private SysConfigImpl sysConfigImpl;
	
	@Autowired
	private SysDrugMatchImpl sysDrugMatchImpl;
	
	@Autowired
	private SysMatchRelationImpl sysMatchRelationImpl;
	
	@Autowired
	private SysParamsImpl sysParamsImpl;
	
	@Autowired
	private SysHospitalImpl sysHospitalImpl;
	
	@Autowired
	private SysRoutedictionImpl sysRoutedictionImpl;
	
	@Autowired
	private SysDisDisctionImpl sysDisDisctionImpl;
	
	/**
	 * 灞忚斀鐨勫垵濮�
	 * 鍖�
	 */
	
	@Autowired
	private ShieldImple shieldImple;
	
	@Autowired
	private RedisClieanImpl redisClieanImpl;

	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  initcustom </li>
	 * <li>鍔熻兘鎻忚堪锛�鍒濆鍖栬嚜瀹氫箟鐨勬暟鎹�/li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
	 * </ul>
	 */
	public String initcustom(){
		try{
			adultInfaceImpl.initAllDate();
			bacresisInfaceImpl.initByHiscode(null);
			brifInfaceImpl.initAllDate();
			doctorprivInfaceImpl.initAllDate(null);
			dosageInfaceImpl.initAll(null);;
			drugdisInfaceImpl.initAllDate(7);
			drugdisInfaceImpl.initAllDate(8);
			drugdisInfaceImpl.initAllDate(9);
			drugLevelInfaceImpl.initAll();
			duptherapyInfaceImpl.initAll();
			hepdosInfaceImpl.initAllDate(null);		
			interInfaceImpl.initAll();;
			ivInfaceImpl.initAll();
			operativeAdviceImpl.initAllDate();
			operativeOperImpl.initAllDate();
			operativeTimeImpl.initAllDate();
			operprivInfaceImpl.initAllDate();
			pediatricInfaceImpl.initAllDate();
			rendosInfaceImpl.initAllDate();
			routeInfaceImpl.initAllDate();
			sexInfaceImpl.initAllDate();
			unagespInfaceImpl.initAllDate(12);
			unagespInfaceImpl.initAllDate(13);
			unagespInfaceImpl.initAllDate(14);
			return "ok";
		}catch(Exception e){
			logger.error("鍒濆鍖栬嚜瀹氫箟鏁版嵁鍑洪敊锛�");
			return "error";
		}
	}
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  initSys </li>
	 * <li>鍔熻兘鎻忚堪锛氬垵濮嬪寲绯荤粺缂撳瓨 </li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
	 * </ul> 
	 * @return
	 */
	public String initSys(){
		try{
			sysDoctorInfaceImpl.initAllDate();			
			allerGenImpl.initAll();			
			diseaseImpl.initAll();
			distDrugImpl.initAll();			
			drugDoseUnitImpl.initAll();			
			frequencyImpl.initAll();			
			operationImpl.initAll();			
			proDrugReasonImpl.initAll();			
			routeMatchImpl.initAll();			
			sysConfigImpl.initAll();			
			sysDrugMatchImpl.initAll();
			sysMatchRelationImpl.initAll();
			sysParamsImpl.initAll();
			sysHospitalImpl.initAll();
			sysRoutedictionImpl.initAll();
			sysDisDisctionImpl.initAll();
			return "ok";
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  initShield </li>
	 * <li>鍔熻兘鎻忚堪锛氬垵濮嬪寲灞忚斀 </li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
	 * </ul> 
	 * @return
	 */
	public String initShield(){
		try{
			shieldImple.initAllDate(1);
			shieldImple.initAllDate(2);
			shieldImple.initAllDate(3);
			shieldImple.initAllDate(4);
			shieldImple.initAllDate(5);
			shieldImple.initAllDate(6);
			shieldImple.initAllDate(7);
			shieldImple.initAllDate(8);
			shieldImple.initAllDate(9);
			shieldImple.initAllDate(10);
			shieldImple.initAllDate(11);
			shieldImple.initAllDate(12);
			shieldImple.initAllDate(13);
			shieldImple.initAllDate(14);
			shieldImple.initAllDate(15);
			shieldImple.initAllDate(16);
			shieldImple.initAllDate(17);
			return "ok";
		}catch(Exception e){
			return "error";
		}
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  cleanAllDate </li>
	 * <li>鍔熻兘鎻忚堪锛氭竻闄ゆ墍鏈夌殑鏁版嵁 </li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
	 * </ul>
	 */
	public void cleanAllDate() throws Exception{
		try{
			redisClieanImpl.cleanalldate();
		}catch(Exception e){
			throw new Exception("娓呴櫎redis鐨勬暟鎹嚭閿欙紒");
		}
		
//		String key = "redis-http-html";
//		String values = getString();
//		redisClieanImpl.add(key, values);
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  getString </li>
	 * <li>鍔熻兘鎻忚堪锛歝++鐨刾assCore涓湁updatecommond鐨勬柟娉曪紝璇ユ柟娉曡繑鍥炰竴浜涗笅杞界殑json瀛楃涓诧紝鍥犱负鎴戜滑娌℃湁璇ユ帴鍙ｆ墍浠ユ殏鏃跺啓姝�</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�鏃�</li>
	 * </ul> 
	 * @return String
	 */
	private String getString(){
		try
		{
			long startTime = System.currentTimeMillis();
			//InputStream im = PassSoapServiceImpl.class.getResourceAsStream("/update_json.txt");
			InputStream im = RedisInitService.class.getClassLoader().getResourceAsStream("update_json.txt");
			String text = readToBuffer(im);
			long endTime = System.currentTimeMillis();

		    float seconds = (endTime - startTime) / 1000F;

		    System.out.println(Float.toString(seconds) + " =getString()");

			return text;
		}catch(Exception e){
			return "";
		}
		
	}
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  readToBuffer </li>
	 * <li>鍔熻兘鎻忚堪锛氬皢鏂囨湰鏂囦欢涓殑鍐呭璇诲叆鍒癰uffer涓�</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�1鏃�</li>
	 * </ul> 
	 * @param filePath 鏂囦欢鐨勮矾寰�
	 * @throws IOException 寮傚父澶勭悊
	 */
    private  String readToBuffer(InputStream is) throws IOException {
    	try{
    		StringBuffer buffer = new StringBuffer();
            String line; // 鐢ㄦ潵淇濆瓨姣忚璇诲彇鐨勫唴瀹�
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 璇诲彇绗竴琛�
            while (line != null) { // 濡傛灉 line 涓虹┖璇存槑璇诲畬浜�
                buffer.append(line); // 灏嗚鍒扮殑鍐呭娣诲姞鍒�buffer 涓�
                //buffer.append("\n"); // 娣诲姞鎹㈣绗�
                line = reader.readLine(); // 璇诲彇涓嬩竴琛�
            }
            reader.close();
            is.close();
            return buffer.toString();
    	}catch(Exception e){
    		return "";
    	}
    	
    }
}
