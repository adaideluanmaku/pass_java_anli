package com.medicom.passlan.inter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;


import com.medicom.passlan.util.RedisSerialization;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  ObjectInfaceImpl </li>
 * <li>类描述：   基本数据的保存，不是保存对象的方式</li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月29日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class ObjectInfaceImpl implements RedisOperObject{
	
//	@Autowired
//	private ShardedJedisPool shardedJedisPool;
	
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	
	private Logger logger = Logger.getLogger(ObjectInfaceImpl.class);
	
	public boolean cleanalldate(){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//            	byte[] pattern = RedisSerialization.serialize("*");
//            	Set<byte[]> set = connection.keys(pattern) ;
//            	Iterator<byte[]> it = set.iterator();  
//            	while (it.hasNext()) {  
//            	  byte[] str = it.next();  
//            	 
//            	  //connection.del(str);
//            	  String key = (String) RedisSerialization.unserialize(str);
//            	  System.out.println(key);
            	connection.flushDb();
                redisTemplate.convertAndSend("PASS_RH_V4.1", "clean-update-redis-over");
            	return true;
            }
        });
	}

	public boolean add(final String key, final Object t) {
//		try{
//			byte[] keys = RedisSerialization.serialize(key);
//			ShardedJedis jedis = shardedJedisPool.getResource();
//			if(jedis.exists(keys)){
//	        	jedis.del(keys);
//	        }
//			jedis.set(keys, RedisSerialization.serialize(t));
//			return true;
//			
//			 return redisTemplate.execute(new RedisCallback<Boolean>() {
//		            @Override
//		            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {
//		                byte[] keys = SerializationUtils.serialize(key);
//		                if (null != connection.get(keys)) {
//		                    return false;
//		                }
//		                byte[] values = SerializationUtils.serialize(obj);
//		                return connection.setNX(keys, values);
//		            }
//		        });
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
		
		boolean issure =  redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {
	                byte[] keys = RedisSerialization.serialize(key);
	                if (null != connection.get(keys)) {
	                	if (1 == connection.del(keys)) {
	                        byte[] values = RedisSerialization.serialize(t);
	                        return connection.setNX(keys, values);
	                    }
	                	return false;
	                }else
	                {
	                	 byte[] values = RedisSerialization.serialize(t);
	 	                return connection.setNX(keys, values);
	                }
	               
	            }
	        });
		if(issure){
			 redisTemplate.convertAndSend("PASS_RH_V4.1", key);
		}
		return issure;
	}
	

//	public ShardedJedisPool getShardedJedisPool() {
//		return shardedJedisPool;
//	}



	public boolean addOrUpdate(final String key, final Object t) {
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
		boolean issure =  redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
                    if (1 == connection.del(keys)) {
                        byte[] values = RedisSerialization.serialize(t);
                        return connection.setNX(keys, values);
                    }
                    return false;
                }else{
                	 byte[] values = RedisSerialization.serialize(t);
                	 return connection.setNX(keys, values);
               }            
            }
        });
		if(issure){
			 redisTemplate.convertAndSend("PASS_RH_V4.1", key);
		}
		return issure;
	}

	public boolean delete(final String key) {
//		try{
//			byte[] keys = RedisSerialization.serialize(key);
//			ShardedJedis jedis = shardedJedisPool.getResource();
//			jedis.del(keys);
//			return true;
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
		
		boolean issure =   redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {
                byte[] keys = RedisSerialization.serialize(key);
                if (1 == connection.del(keys)) {
                    return true;
                }
                return false;
            }
        });
		if(issure){
			 redisTemplate.convertAndSend("PASS_RH_V4.1", key);
		}
		return issure;
	}

	public boolean isExiter(final String key) {
//		try{
//			byte[] keys = RedisSerialization.serialize(key);
//			ShardedJedis jedis = shardedJedisPool.getResource();
//			if(jedis.exists(key)){
//				return true;
//			}else{
//				return false;
//			}
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
		return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
            	byte[] keys = RedisSerialization.serialize(key);
                if (null != connection.get(keys)) {
//                    if (1 == connection.del(keys)) {
//                        byte[] values = RedisSerialization.serialize(t);
//                        return connection.setNX(keys, values);
//                    }
                    return true;
                }
                return false;
            }
        });
	}
	
//	public boolean addOrUpdate(Object t,String key,ShardedJedis jedis){
//		redisTemplate.getConnectionFactory().getConnection();
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
//		
//	}
	
//	public boolean addOrUpdate(Object t,String key,RedisConnection getConnection){
//		//redisTemplate.getConnectionFactory().getConnection();
//		return redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(getConnection) throws DataAccessException {
//                byte[] keys = RedisSerialization.serialize(key);
//                if (null != connection.get(keys)) {
//                    if (1 == connection.del(keys)) {
//                        byte[] values = RedisSerialization.serialize(t);
//                        return connection.setNX(keys, values);
//                    }
//                    return false;
//                }
//                return false;
//            }
//        });
//		
//	}
	

	public boolean addList(final String key, final List<Object> list) {
//		try{
//			byte[] keys = RedisSerialization.serialize(key);
//			ShardedJedis jedis = shardedJedisPool.getResource();
//			if(jedis.exists(keys)){
//	        	jedis.del(keys);
//	        }
//			jedis.set(keys, RedisSerialization.serialize(list));
//			return true;
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			return false;
//		}
		
		boolean issure = redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {
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


	public <T> T get(Object key) {
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

	

}
