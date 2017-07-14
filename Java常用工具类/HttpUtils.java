package com.qhit.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class HttpUtils {

	final static Header HEADER_USER_AGENT = new BasicHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36");
	final static Header HEADER_CONNECTION = new BasicHeader("connection", "keep-alive");
	final static Header HEADER_CACHE_CONTROL = new BasicHeader("cache-control", "max-age=0");
	final static Header HEADER_ACCEPT = new BasicHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	final static Header HEADER_ACCEPT_ENCODING = new BasicHeader("accept-encoding", "gzip, deflate, sdch");
	final static Header HEADER_ACCEPT_LANGUAGE = new BasicHeader("accept-language", "zh-CN,zh;q=0.8,en;q=0.6");

	/**
	 * 发送post请求
	 * 
	 * @param url
	 *            请求的url链接
	 * @param charSet
	 *            字符编码
	 * @param nameValuePairs
	 *            参数
	 * @return
	 */
	public static String requestPost(String url, String charSet, NameValuePair... nameValuePairs) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(HEADER_USER_AGENT);
			httpPost.setHeader(HEADER_CONNECTION);
			httpPost.setHeader(HEADER_CACHE_CONTROL);
			httpPost.setHeader(HEADER_ACCEPT);
			httpPost.setHeader(HEADER_ACCEPT_ENCODING);
			httpPost.setHeader(HEADER_ACCEPT_LANGUAGE);

			List<namevaluepair> formparams = Arrays.asList(nameValuePairs);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

			httpPost.setEntity(formEntity);
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				return inputStreamToString(entity, charSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (Exception e) {
			}
		}
		return "";
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            请求的url链接
	 * @param charSet
	 *            字符编码
	 * @param nameValuePairs
	 *            参数
	 * @return
	 */
	public static String requestGet(String url, String charSet, NameValuePair... nameValuePairs) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader(HEADER_USER_AGENT);
			httpget.setHeader(HEADER_CONNECTION);
			httpget.setHeader(HEADER_CACHE_CONTROL);
			httpget.setHeader(HEADER_ACCEPT);
			httpget.setHeader(HEADER_ACCEPT_ENCODING);
			httpget.setHeader(HEADER_ACCEPT_LANGUAGE);

			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				return inputStreamToString(entity, charSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (Exception e) {
			}
		}
		return "";
	}

	/**
	 * 返回流
	 * 
	 * @param entity
	 * @param charSet
	 * @return
	 * @throws Exception
	 */
	private static String inputStreamToString(HttpEntity entity, String charSet) throws Exception {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = entity.getContent();
			byte[] bytes = new byte[52428800];
			int buffer = -1;

			while ((buffer = is.read(bytes)) != -1) {
				baos.write(bytes, 0, buffer);
			}
			return new String(baos.toByteArray(), charSet);
		} finally {
			is.close();
			baos.close();
		}
	}
}