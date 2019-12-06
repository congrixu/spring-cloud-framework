package com.rxv5.http;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rxv5.util.FastjsonUtil;

/**
 * httpclient工具类
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	public static final String DEFAULT_CHARSET = "UTF-8";
	private static final int timeout = 8000;

	private static HttpClientConfig clientConfig;

	public static void init(HttpClientConfig config) {
		clientConfig = config;
	}

	private static CloseableHttpClient getHttpClient(URI uri) {
		if (null == clientConfig) {
			logger.error("HttpClientUtil is not initialized! Please init first...");
			return null;
		}
		return clientConfig.getHttpClient(uri.getHost(), uri.getPort());
	}

	/**
	 * 发送http get请求到指定url并返回
	 *
	 * @param url    请求地址
	 * @param params 携带的http request参数
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String get(String url, Map<String, String> params) throws IOException, URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		if (null != params) {
			for (String param : params.keySet()) {
				uriBuilder.addParameter(param, params.get(param));
			}
		}
		URI uri = uriBuilder.build();
		CloseableHttpClient httpclient = getHttpClient(uri);
		HttpClientContext context = HttpClientContext.create();

		try {
			HttpGet httpget = new HttpGet(uri);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
			httpget.setConfig(config);
			logger.info("Executing get request " + httpget.getRequestLine());
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			assert httpclient != null;
			String responseBody = httpclient.execute(httpget, responseHandler, context);
			logger.debug("Executed get {}\n result {}", httpget.getRequestLine(), responseBody);
			return responseBody;
		} finally {
			clientConfig.releaseConn(context.getConnection(HttpClientConnection.class));
		}
	}

	/**
	 * post multipart请求到指定url
	 *
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String postMultipart(String url, Map<String, String> params, List<File> files)
			throws IOException, URISyntaxException {
		// TODO not tested...
		CloseableHttpClient httpclient = getHttpClient(new URI(url));
		HttpClientContext context = HttpClientContext.create();
		try {
			logger.info("Executing post request:{}", url);
			HttpPost http = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(8000).build();
			http.setConfig(config);
			if (null != params) {
				List<NameValuePair> entityParam = Lists.newArrayList();
				for (String name : params.keySet()) {
					entityParam.add(new BasicNameValuePair(name, params.get(name)));
				}
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.setCharset(Charset.forName(DEFAULT_CHARSET));
				for (File file : files) {
					builder.addPart(file.getName(), new FileBody(file));
				}
				for (String param : params.keySet()) {
					builder.addPart(param, new StringBody(params.get(param), ContentType.TEXT_PLAIN));
				}
				http.setEntity(builder.build());
			}
			assert httpclient != null;
			CloseableHttpResponse response = httpclient.execute(http, context);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("Executed post {} \n failed: {}", http.getRequestLine(), response.getStatusLine());
			}
			String retJson = EntityUtils.toString(response.getEntity());
			logger.debug("Executed post {}\n result: {}", http.getRequestLine(), retJson);

			return retJson;

		} finally {
			clientConfig.releaseConn(context.getConnection(HttpClientConnection.class));
		}
	}

	/**
	 * 发送http post请求到指定url并返回
	 *
	 * @param url    请求地址
	 * @param params 携带的http request参数
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> params) throws URISyntaxException, IOException {
		CloseableHttpClient httpclient = getHttpClient(new URI(url));
		HttpClientContext context = HttpClientContext.create();
		try {
			logger.info("Executing post request:{} params {}", url,
					params == null ? "params is null" : FastjsonUtil.obj2Json(params));
			HttpPost http = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
			http.setConfig(config);
			http.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + DEFAULT_CHARSET);
			if (null != params) {
				List<NameValuePair> entityParam = Lists.newArrayList();
				for (String name : params.keySet()) {
					entityParam.add(new BasicNameValuePair(name, params.get(name)));
				}
				http.setEntity(new UrlEncodedFormEntity(entityParam, DEFAULT_CHARSET));
			}
			assert httpclient != null;
			CloseableHttpResponse response = httpclient.execute(http, context);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("Executed post {} \n failed: {}", http.getRequestLine(), response.getStatusLine());
			}
			String retJson = EntityUtils.toString(response.getEntity());
			logger.debug("Executed post {}\n result: {}", http.getRequestLine(), retJson);

			return retJson;

		} finally {
			clientConfig.releaseConn(context.getConnection(HttpClientConnection.class));
		}
	}

	/**
	 * 发送http post请求，请求体为json格式String
	 *
	 * @param url
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public static String postJSONBody(String url, JSONObject jsonObj) throws Exception {
		if (null == jsonObj) {
			throw new IllegalArgumentException("param jsonStr should not be null!");
		}
		CloseableHttpClient httpclient = getHttpClient(new URI(url));
		HttpClientContext context = HttpClientContext.create();
		try {
			logger.info("Executing post json request:{} json string: {}", url, jsonObj.toJSONString());
			HttpPost http = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
			http.setConfig(config);
			http.setHeader("Content-Type", "application/json; charset=" + DEFAULT_CHARSET);
			StringEntity se = new StringEntity(jsonObj.toJSONString(), DEFAULT_CHARSET);
			http.setEntity(se);
			assert httpclient != null;
			CloseableHttpResponse response = httpclient.execute(http, context);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("Executed post {} \n failed: {}", http.getRequestLine(), response.getStatusLine());
			}
			String responseBody = EntityUtils.toString(response.getEntity());
			logger.debug("Executed post {}\n result: {}", http.getRequestLine(), responseBody);

			return responseBody;

		} finally {
			clientConfig.releaseConn(context.getConnection(HttpClientConnection.class));
		}
	}

	/**
	 * 发送http get请求，解析返回的JSON串为指定对象实例
	 *
	 * @param url 能够返回JSON数据的url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static <T> T getJson2Object(String url, Class<T> retClass) throws Exception {
		logger.debug("This url is {}", url);
		return getJson2Object(url, null, retClass);
	}

	/**
	 * 发送http get请求，解析返回的JSON串为指定对象实例
	 *
	 * @param url      能够返回JSON数据的url
	 * @param params   请求参数key-value集合
	 * @param retClass
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static <T> T getJson2Object(String url, Map<String, String> params, Class<T> retClass)
			throws ClientProtocolException, IOException, URISyntaxException {
		logger.debug("This url is {}", url);
		String result = get(url, params);
		return StringUtils.isNotBlank(result) ? FastjsonUtil.json2Obj(result, retClass) : null;
	}

	/**
	 * 发送http post请求，解析返回的JSON串为指定对象实例
	 *
	 * @param url      能够返回JSON数据的url
	 * @param params   请求参数key-value集合
	 * @param retClass
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static <T> T postJson2Object(String url, Map<String, String> params, Class<T> retClass)
			throws URISyntaxException, IOException {
		String result = post(url, params);
		return StringUtils.isNotBlank(result) ? FastjsonUtil.json2Obj(result, retClass) : null;
	}

	public static <T> List<T> postJson2List(String url, Map<String, String> params, Class<T> retClass)
			throws URISyntaxException, IOException {
		return FastjsonUtil.json2ObjList(post(url, params), retClass);
	}

	/**
	 * 发送http post请求到指定url并返回
	 *
	 * @param url    请求地址
	 * @param params 携带的http request参数
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void postNoReturn(String url, Map<String, String> params) throws URISyntaxException, IOException {
		CloseableHttpClient httpclient = getHttpClient(new URI(url));
		HttpClientContext context = HttpClientContext.create();
		try {
			logger.info("Executing post request:{} params {}", url,
					params == null ? "params is null" : FastjsonUtil.obj2Json(params));
			HttpPost http = new HttpPost(url);
			RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).build();
			http.setConfig(config);
			http.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + DEFAULT_CHARSET);
			if (null != params) {
				List<NameValuePair> entityParam = Lists.newArrayList();
				for (String name : params.keySet()) {
					entityParam.add(new BasicNameValuePair(name, params.get(name)));
				}
				http.setEntity(new UrlEncodedFormEntity(entityParam, DEFAULT_CHARSET));
			}
			assert httpclient != null;
			httpclient.execute(http, context);
		} finally {
			clientConfig.releaseConn(context.getConnection(HttpClientConnection.class));
		}
	}

	public static void main(String[] args) {
		HttpClientConfig config = HttpClientConfig.init();
		HttpClientUtil.init(config);
		try {
			//            System.out
			//                    .println(HttpClientUtil
			//                            .getJson2Object(
			//                                    "http://admin.nalipei.com/product/service/sku/filter/category?catId=32&attrVals=192,143",
			//                                    Map.class).toString());
			//            Map<String, String> params = Maps.newHashMap();
			//            params.put("catId", "32");
			//            params.put("attrVals", "192,143");
			//            System.out.println(HttpClientUtil.postJson2Object(
			//                    "http://admin.nalipei.com/product/service/sku/filter/category", params,
			//                    Map.class).toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			config.destroy();
		}
	}
}
