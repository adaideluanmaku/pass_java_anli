package com.medicom.passlan.inter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

import com.ch.redissys.Redis_conf;
import com.medicom.passlan.util.RedisSerialization;

public class ComonInfaceImpl<T> implements RedisOperModel<T>{
	public String servername;
	
	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;
	
	private Logger logger = Logger.getLogger(ComonInfaceImpl.class);
	
	
	public void setServername(String servername) {
		this.servername = servername;
	}


	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  cleanalldate </li>
	 * <li>鍔熻兘鎻忚堪锛氭竻闄ゆ暟鎹�</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
	 * </ul> 
	 * @return
	 */
	public boolean cleanalldate(){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//            	byte[] pattern = RedisSerialization.serialize("*");
//            	Set<byte[]> set = connection.keys(pattern) ;
//            	Iterator<byte[]> it = set.iterator();  
//            	while (it.hasNext()) {  
//            	  byte[] str = it.next();  
//            	  connection.del(str);
//            	  String key = (String) RedisSerialization.unserialize(str);
//            	  redisTemplate.convertAndSend("PASS_RH_V4.1", key);
//            	}  
            	connection.flushDb();
                redisTemplate.convertAndSend("PASS_RH_V4.1", "clean-update-redis-over");
            	return true;
            }
        });
	}
	
	
	//更新redis，并推送服务端消息
	public boolean addorupdate(final T t,final String  key,String servername) throws IOException, ClassNotFoundException, SQLException{
//		boolean issure = redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                byte[] keys = RedisSerialization.serialize(key);
//                if (null != connection.get(keys)) {
//                    if (1 == connection.del(keys)) {
//                        byte[] values = RedisSerialization.serialize(t);
//                        return connection.setNX(keys, values);
//                    }
//                    return true;
//                }else{
//                	 byte[] values = RedisSerialization.serialize(t);
//                     return connection.setNX(keys, values);
//                }
//            }
//        });
		Redis_conf Redis_conf=new Redis_conf();
		
		Jedis jedis=Redis_conf.redisconnect();
		boolean issure=false;
		byte[] keys=RedisSerialization.serialize(key);
		byte[] values = RedisSerialization.serialize(t);
		System.out.println("更新redis-key:"+key);
		if(jedis.exists(keys)){
			jedis.del(keys);
			jedis.set(keys, values);
//			System.out.println("aaa");
			issure=true;
		}else{
			jedis.set(keys, values);
//			System.out.println("bbb");
			issure=true;
		}
		
		if(issure){
//			jedis.publish("PASS_RH_V4.1", key);
			Redis_conf.convertAndSend(jedis,"PASS_RH_V4.1", key);
		}
		return issure;
	}
	
	public boolean addorupdateList(final List list,final String  key){
		boolean issure =  redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
                    if (1 == connection.del(keys)) {
                        byte[] values = RedisSerialization.serialize(list);
                        return connection.setNX(keys, values);
                    }
                    return false;
                }else{
                	 byte[] values = RedisSerialization.serialize(list);
                     return connection.setNX(keys, values);
                }
            }
        });
		if(issure){
			 redisTemplate.convertAndSend("PASS_RH_V4.1", key);
		}
		return issure;
	}
	
	/**
	 * 閫氱敤鐨勬柟娉曠洿鎺ュ彲浠ヨ幏鍙栧埌鏁版嵁
	 */
	public boolean addOrUpdateListCommon(T t, final String key) {
		try{
			byte[] keys = RedisSerialization.serialize(key);
			List<T> list = null;
			byte[] values = redisTemplate.getConnectionFactory().getConnection().get(keys);
			if(values==null){
				list =  new ArrayList<T>();
				list.add(t);
			}else{
				list =  (List<T>) RedisSerialization.unserialize(values);
				list.add(t);
			}
			return this.addorupdate(t, key, servername);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		
	}
	
//	public ShardedJedisPool getShardedJedisPool() {
//		return shardedJedisPool;
//	}

	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  addOrUpdateList </li>
	 * <li>鍔熻兘鎻忚堪锛�鐩存帴鏂板鏁版嵁鐨勬柟娉�/li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�7鏃�</li>
	 * </ul> 
	 * @param list list
	 * @param key 涓婚敭
	 * @return true or false;
	 */
	public boolean addOrUpdateList(List<T> list,String key){
		try{
			return this.addorupdateList(list, key);
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  addOrUpdate </li>
	 * <li>鍔熻兘鎻忚堪锛�澧炲姞鎴栬�鏇存柊瀵硅薄</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�鏃�</li>
	 * </ul> 
	 * @param t 娉涘瀷鐨勫璞�
	 * @param key 涓婚敭ID
	 * @param jedis
	 * @return
	 */
//	public boolean addOrUpdate(T t,String key,ShardedJedis jedis){
//		try{
//			byte[] keys = RedisSerialization.serialize(key);
//			if(jedis.exists(keys)){
//	        	jedis.del(keys);
//	        }
//			jedis.set(keys, RedisSerialization.serialize(t));
//			return true;
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
//	}
	

//	public boolean addOrUpdateCommon(T t, String key) {
//		try{
//			byte[] keys = RedisSerialization.serialize(key);
//			ShardedJedis jedis = shardedJedisPool.getResource();
//			if(jedis.exists(keys)){
//	        	jedis.del(keys);
//	        }
//			jedis.set(keys, RedisSerialization.serialize(t));
//			return true;
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
//		
//	}

	public boolean deleteCommon(String key) {
		return this.deleteimpl(key);
	}
	
	public boolean addorupdateObject(final String key,final Object o){
		boolean issure =  redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
                    if (1 == connection.del(keys)) {
                        byte[] values = RedisSerialization.serialize(o);
                        return connection.setNX(keys, values);
                    }
                    return false;
                }else{
                	 byte[] values = RedisSerialization.serialize(o);
                     return connection.setNX(keys, values);
                }
            }
        });
		if(issure){
			 redisTemplate.convertAndSend("PASS_RH_V4.1", key);
		}
		return issure;
	}
	
	public boolean deleteimpl(final String key){
		boolean issure = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {
                byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
	                if (1 == connection.del(keys)) {
	                    return true;
	                }
                }
                return true;
            }
        });
		if(issure){
			 redisTemplate.convertAndSend("PASS_RH_V4.1", key);
		}
		return issure;
		
	}

	/**
	 * key鍊兼槸鍚﹀瓨鍦�
	 */
	public boolean isExiterCommon( String key) {
		return this.isexiter(key);
	}
	
	public boolean isexiter(final String key){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
            	byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
                    return true;
                }
                return false;
            }
        });
	}
	
	public boolean addListCommon(List<T> list,String key) {
//		try{
//			if(list!=null&&list.size()>0){
//				ShardedJedis jedis = shardedJedisPool.getResource();
//				byte[] keys = RedisSerialization.serialize(key);
//		    	if(jedis.exists(keys)){
//		        	jedis.del(keys);
//		        }
//		    	jedis.set(keys,RedisSerialization.serialize(list));
//		    	return true;
//			}else{
//				return false;
//			}
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
		return this.addorupdateList(list, key);
	}
	
	
	/**
	 * 
	 * <ul>
	 * <li>鏂规硶鍚嶏細  addMapinListCommon </li>
	 * <li>鍔熻兘鎻忚堪锛氬鍔爉ap瀵硅薄鐨勬暟鎹敤浜庡叏閮ㄦ暟鎹洿鏂�</li>
	 * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�鏃�</li>
	 * </ul> 
	 * @param map 鏄犲皠鍏崇郴
	 * @return
	 */
	public boolean addMapinListCommon(Map<String,List<T>> map){
		try{
			if(map!=null&&map.size()>0){
		    	Iterator<Entry<String, List<T>>> iter = map.entrySet().iterator();
		        while (iter.hasNext()) {
				   Map.Entry entry = (Map.Entry) iter.next(); 
				   Object key = entry.getKey();
				   Object val = entry.getValue();
				   this.addorupdateObject(key.toString(),val);
				}
		    	return true;
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public boolean deleteMapinListCommon(Map<String,List<T>> map){
		try{
			if(map!=null&&map.size()>0){
		    	Iterator<Entry<String, List<T>>> iter = map.entrySet().iterator();
		        while (iter.hasNext()) {
				   Map.Entry entry = (Map.Entry) iter.next(); 
				   Object key = entry.getKey();
				   Object val = entry.getValue();
				   if(isExiterCommon(key.toString())){
					   deleteCommon(key.toString());
				   }
				   this.addorupdateObject(key.toString(),RedisSerialization.serialize(val));
				}
		    	return true;
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 鑾峰彇redis鐨勬暟鎹�
	 */
	public <T>T get(Object key) {
		return  (T) this.geto(key.toString());
	}
	
	public Object geto(final String key){
		return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
                    return RedisSerialization.unserialize(connection.get(keys));
                }
                return null;
            }
        });
	}
	

	public boolean addOrUpdateCommon(T t, String key,String servername) throws ClassNotFoundException, SQLException {
		try {
			return this.addorupdate(t, key,servername);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public boolean addOrUpdateCommon(T t, String key) {
		// TODO Auto-generated method stub
		return false;
	}

}
