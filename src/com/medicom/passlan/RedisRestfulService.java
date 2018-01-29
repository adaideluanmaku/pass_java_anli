package com.medicom.passlan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/redis")  
@Controller
public interface RedisRestfulService {
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  updateSys </li>
	 * <li>功能描述：更新系统缓存 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年10月31日 </li>
	 * </ul> 
	 * @return ok or error
	 */
	 @RequestMapping(value = "/updateSys", produces = "text/plain;charset=UTF-8")  
	 public @ResponseBody String updateSys(); 
	 
	 /**
	  * 
	  * <ul>
	  * <li>方法名：  updateCustom </li>
	  * <li>功能描述：更新自定义的数据 </li>
	  * <li>创建人：  周应强 </li>
	  * <li>创建时间：2016年10月31日 </li>
	  * </ul> 
	  * @return ok or error
	  */
	 @RequestMapping(value = "/updateCustom", produces = "text/plain;charset=UTF-8")  
	 public @ResponseBody String updateCustom(); 
	 
	 
	 
	 /**
	  * 
	  * <ul>
	  * <li>方法名：  updateShield </li>
	  * <li>功能描述： 更新屏蔽信息</li>
	  * <li>创建人：  周应强 </li>
	  * <li>创建时间：2016年10月31日 </li>
	  * </ul> 
	  * @return ok or error
	  */
	 @RequestMapping(value = "/updateShield", produces = "text/plain;charset=UTF-8")  
	 public @ResponseBody String updateShield(); 
	 
	 /**
	  * 
	  * <ul>
	  * <li>方法名：  cleanRedis </li>
	  * <li>功能描述：清除reids数据的方法 </li>
	  * <li>创建人：  周应强 </li>
	  * <li>创建时间：2016年10月31日 </li>
	  * </ul> 
	  * @return ok or error
	  */
	 @RequestMapping(value = "/cleanRedis", produces = "text/plain;charset=UTF-8")  
	 public @ResponseBody String cleanRedis();
	 
	 

}
