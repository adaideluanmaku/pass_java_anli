package ch.com.PAandPassModule_Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * <ul>
 * <li>�����ƣ�  Passservice </li>
 * <li>��������PASSWEB�ӿڵ��ã�httpclientģ����ýӿ�   </li>
 * <li>�����ˣ�</li>
 * <li>����ʱ�䣺2016��6��13�� </li>
 * <li>�޸ı�ע��</li>
 * </ul>
 */
public class Paservice {
	/**
	 * ���ó�ʱʱ��
	 */
	private static int WS_TIMEOUT = 10 * 1000;

	public String getPassResult(String jsonin,String url) throws TimeoutException, UnsupportedEncodingException {
		//PASS�ӿڵ�������Ҫת���ַ���ΪURL����
//		String jsoninxml=URLEncoder.encode(jsonin, "UTF-8");
		
		String result = null;
		String add_url = url;
		try {
			//����һ���ͻ�������
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(add_url);
			
			// ��������ʹ��䳬ʱʱ��
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(WS_TIMEOUT).setConnectTimeout(WS_TIMEOUT).build();
//			httppost.setConfig(requestConfig);
			
			//�ж��Ƿ���json���룬�������ֵ���PASS�ӿں�redis�ӿ�
			if(!"".equals(jsonin)){
				// ������������,�����ַ����ȡ���http.UTF-8
//				StringEntity stringEntity = new StringEntity(jsonin, ContentType.APPLICATION_FORM_URLENCODED);
			
				StringEntity stringEntity = new StringEntity(jsonin,HTTP.UTF_8);
				stringEntity.setContentType("application/x-www-form-urlencoded");
//				stringEntity.setContentEncoding("UTF-8");
				httppost.setEntity(stringEntity);
			}else{
				StringEntity stringEntity = new StringEntity(HTTP.UTF_8);
				stringEntity.setContentType("application/x-www-form-urlencoded");
//				stringEntity.setContentEncoding("UTF-8");
				httppost.setEntity(stringEntity);
			}
			
			//���������������
			HttpResponse httpResponse = httpClient.execute(httppost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// ����Ӧ�Ľ���л�ȡ��Ӧ�������
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity);
//				EntityUtils.toString(httpEntity);
			}else{
//				result=httpResponse.getStatusLine().toString();
				httpResponse.getStatusLine().toString();
			}
//			System.out.println("PASS输入串："+jsonin);
//			System.out.println("PASS输出串："+result);
			httpClient.close();
		} 
//		catch (ConnectTimeoutException ex) {
//			throw new TimeoutException("���ӳ�ʱ");
//		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 
	 * <ul>
	 * <li>方法名：  restPost </li>
	 * <li>功能描述： </li>
	 * <li>创建人：  jy </li>
	 * <li>创建时间：2017年6月12日 </li>
	 * </ul> 
	 * @param URL
	 * @param psJSONStr
	 * @return
	 */
	public static String restPost(String URL, String psJSONStr) {
		String result = "";
		try {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(URL);
			Response response = target.request().post(Entity.entity(psJSONStr, MediaType.APPLICATION_JSON_TYPE));
			try {
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
				}
				result = response.readEntity(String.class);
			} finally {
				response.close();
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
