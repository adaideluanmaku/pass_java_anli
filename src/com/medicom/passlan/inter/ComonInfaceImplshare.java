package com.medicom.passlan.inter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.medicom.passlan.util.RedisSerialization;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ComonInfaceImplshare<T> implements RedisOperModel<T>{
	
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	
	private Logger logger = Logger.getLogger(ComonInfaceImplshare.class);
	

	/**
	 * 通用的方法直接可以获取到数据
	 */
	public boolean addOrUpdateListCommon(T t, String key) {
		try{
			byte[] keys = RedisSerialization.serialize(key);
			ShardedJedis jedis = shardedJedisPool.getResource();
			List<T> list = null;
			byte[] values = jedis.get(keys);
			if(values==null){
				list =  new ArrayList<T>();
				list.add(t);
			}else{
				list =  (List<T>) RedisSerialization.unserialize(values);
				list.add(t);
			}
			jedis.set(keys, RedisSerialization.serialize(list));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		
	}
	
	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	/**
	 * 
	 * <ul>
	 * <li>方法名：  addOrUpdateList </li>
	 * <li>功能描述： 直接新增数据的方法</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年6月27日 </li>
	 * </ul> 
	 * @param list list
	 * @param key 主键
	 * @return true or false;
	 */
	public boolean addOrUpdateList(List<T> list,String key){
		try{
			byte[] keys = RedisSerialization.serialize(key);
			ShardedJedis jedis = shardedJedisPool.getResource();
			if(jedis.exists(keys)){
	        	jedis.del(keys);
	        }
			jedis.set(keys, RedisSerialization.serialize(list));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	/**
	 * 
	 * <ul>
	 * <li>方法名：  addOrUpdate </li>
	 * <li>功能描述： 增加或者更新对象</li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul> 
	 * @param t 泛型的对象
	 * @param key 主键ID
	 * @param jedis
	 * @return
	 */
	public boolean addOrUpdate(T t,String key,ShardedJedis jedis){
		try{
			byte[] keys = RedisSerialization.serialize(key);
			if(jedis.exists(keys)){
	        	jedis.del(keys);
	        }
			jedis.set(keys, RedisSerialization.serialize(t));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
	}
	

	public boolean addOrUpdateCommon(T t, String key) {
		try{
			byte[] keys = RedisSerialization.serialize(key);
			ShardedJedis jedis = shardedJedisPool.getResource();
			if(jedis.exists(keys)){
	        	jedis.del(keys);
	        }
			jedis.set(keys, RedisSerialization.serialize(t));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		
	}

	public boolean deleteCommon(String key) {
		try{
			ShardedJedis jedis = shardedJedisPool.getResource();
			byte[] keys = RedisSerialization.serialize(key);
	    	if(jedis.exists(keys)){
	        	jedis.del(keys);
	        }
			return true;
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		
	}

	/**
	 * key值是否存在
	 */
	public boolean isExiterCommon(String key) {
		try{
			ShardedJedis jedis = shardedJedisPool.getResource();
			byte[] keys = RedisSerialization.serialize(key);
	    	if(jedis.exists(keys)){
	        	return true;
	        }else{
	        	return false;
	        }
		}catch(Exception e){
			logger.error(e.getMessage());
			return false;
		}
		
	}
	
	public boolean addListCommon(List<T> list,String key) {
		try{
			if(list!=null&&list.size()>0){
				ShardedJedis jedis = shardedJedisPool.getResource();
				byte[] keys = RedisSerialization.serialize(key);
		    	if(jedis.exists(keys)){
		        	jedis.del(keys);
		        }
		    	jedis.set(keys,RedisSerialization.serialize(list));
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
	 * 
	 * <ul>
	 * <li>方法名：  addMapinListCommon </li>
	 * <li>功能描述：增加map对象的数据用于全部数据更新 </li>
	 * <li>创建人：  周应强 </li>
	 * <li>创建时间：2016年7月1日 </li>
	 * </ul> 
	 * @param map 映射关系
	 * @return
	 */
	public boolean addMapinListCommon(Map<String,List<T>> map){
		try{
			if(map!=null&&map.size()>0){
				ShardedJedis jedis = shardedJedisPool.getResource();
		    	Iterator<Entry<String, List<T>>> iter = map.entrySet().iterator();
		        while (iter.hasNext()) {
				   Map.Entry entry = (Map.Entry) iter.next(); Object key = entry.getKey();
				   Object val = entry.getValue();
				   if(jedis!=null){
					   //System.out.println(key);
						byte[] keys = RedisSerialization.serialize(key);
		            	if(jedis.exists(keys)){
			            	jedis.del(keys);
			            }
		            	jedis.set(keys,RedisSerialization.serialize(entry.getValue()));
		            }
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
				ShardedJedis jedis = shardedJedisPool.getResource();
		    	Iterator<Entry<String, List<T>>> iter = map.entrySet().iterator();
		        while (iter.hasNext()) {
				   Map.Entry entry = (Map.Entry) iter.next(); Object key = entry.getKey();
				   Object val = entry.getValue();
				   if(jedis!=null){
					   //System.out.println(key);
						byte[] keys = RedisSerialization.serialize(key);
		            	if(jedis.exists(keys)){
			            	jedis.del(keys);
			            }
		            	//jedis.set(keys,RedisSerialization.serialize(entry.getValue()));
		            }
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
	 * 获取redis的数据
	 */
	public <T>T get(Object key) {
		try{
			ShardedJedis jedis = shardedJedisPool.getResource();
			byte[] keys = RedisSerialization.serialize(key);
		    byte[] values = jedis.get(keys);   
		    if (values == null) {
		         return null;
		    }else{
		    	 return (T) RedisSerialization.unserialize(values);
		    }
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}  
	}
}
