package com.medicom.passlan.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
/**
 * 
 * <ul>
 * <li>项目名称：PassLanManage </li>
 * <li>类名称：  RedisSerialization </li>
 * <li>类描述：   序列化的工具 </li>
 * <li>创建人：周应强 </li>
 * <li>创建时间：2016年6月24日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class RedisSerialization {
	
	static FSTConfiguration configuration = FSTConfiguration
			  .createDefaultConfiguration();
	
//	private static Kryo kryo = new Kryo();
			   //   .createStructConfiguration();

	/** fst序列化 */
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
			//logger.error("FST序列化出错：",e);
			return null;
		}finally {
			if(fout != null)
			try {
				fout.close();
			} catch (IOException e) {}
		}
	}

	/** fst反序列化 */
	public static Object unserialize(byte[] bytes) {
		//return configuration.asObject(sec);
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return in.readObject();
		} catch (Exception e) {
			//logger.error("FST反序列化出错：",e);
			return null;
		} finally {
			if(in != null)
			try {
				in.close();
			} catch (IOException e) {}
		}
	}

}
