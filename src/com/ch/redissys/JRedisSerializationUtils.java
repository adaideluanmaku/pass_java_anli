package com.ch.redissys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


/**
 * 
 * <ul>
 * <li>椤圭洰鍚嶇О锛歊edisInit </li>
 * <li>绫诲悕绉帮細  JRedisSerializationUtils </li>
 * <li>绫绘弿杩帮細   搴忓垪鍖栧璞�/li>
 * <li>鍒涘缓浜猴細鍛ㄥ簲寮�</li>
 * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
 * <li>淇敼澶囨敞锛�/li>
 * </ul>
 */
public class JRedisSerializationUtils {
	static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();
	
//	private static Kryo kryo = new Kryo();
			   //   .createStructConfiguration();

	/** fst搴忓垪鍖�*/
	public static byte[] serialize(Object obj) {
		//return configuration.asByteArray(obj);
		ByteArrayOutputStream out = null;
		FSTObjectOutput fout = null;
		try {
			out = new ByteArrayOutputStream();
			fout = new FSTObjectOutput(out);
			fout.writeObject(obj);
			fout.flush();
			return out.toByteArray();
		} catch (IOException e) {
			//logger.error("FST搴忓垪鍖栧嚭閿欙細",e);
			return null;
		}finally {
			if(fout != null)
			try {
				fout.close();
			} catch (IOException e) {}
		}
	}

//	/** fst鍙嶅簭鍒楀寲 */
	public static Object unserialize(byte[] bytes) {
		//return configuration.asObject(sec);
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return in.readObject();
		} catch (Exception e) {
			//logger.error("FST鍙嶅簭鍒楀寲鍑洪敊锛�,e);
			return null;
		} finally {
			if(in != null)
			try {
				in.close();
			} catch (IOException e) {}
		}
	}
	 
	 
//	 /** kryo搴忓垪鍖栨柟妗�*/
//	  public static byte[] serialize(Object obj) {
//	    byte[] buffer = new byte[2048];
//	    try(
//	        Output output = new Output(buffer);
//	        ) {
//	      kryo.writeClassAndObject(output, obj);
//	      return output.toBytes();
//	    } catch (Exception e) {
//	    //	logger.error("kryo搴忓垪鍖栧嚭閿欙細",e);
//	    }
//	    return null;
//	  }
//
//	  
//	  /** kryo鍙嶅簭鍒楀寲鏂规 */
//	  public static Object unserialize(byte[] src) {
//	    try(
//	    Input input = new Input(src);
//	        ){
//	      return kryo.readClassAndObject(input);
//	    }catch (Exception e) {
//	    	e.printStackTrace();
//	    	//logger.error("kryo鍙嶅簭鍒楀寲鍑洪敊锛�,e);
//	    }
//	    return null;
//	  }
	 
	 
	 /**
	  * 
	  * <ul>
	  * <li>鏂规硶鍚嶏細  serializeList </li>
	  * <li>鍔熻兘鎻忚堪锛氬簭鍒楀寲瀵硅薄 </li>
	  * <li>鍒涘缓浜猴細  鍛ㄥ簲寮�</li>
	  * <li>鍒涘缓鏃堕棿锛�016骞�鏈�3鏃�</li>
	  * </ul> 
	  * @param list
	  * @return
	  */
//	 public static byte[] serializeList(ArrayList list){
//		 return configuration.asByteArray(list);
//	 }
//	 
//	 public static ArrayList unserializeList(byte[] sec){
//		 return (ArrayList) configuration.asObject(sec);
//	 }
	 
	 
}
