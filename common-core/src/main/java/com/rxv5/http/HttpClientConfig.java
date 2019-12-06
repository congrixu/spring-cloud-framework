package com.rxv5.http;

import org.apache.http.Consts;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.TimeUnit;

public class HttpClientConfig {

	private static final int DEFAULT_PORT = 80;
	private static final int DEFAULT_MAXTOTAL = 200;
	private static final int DEFAULT_MAXPERROUTE = 50;

	private final PoolingHttpClientConnectionManager connManager;

	private HttpClientConfig(PoolingHttpClientConnectionManager connManager) {
		this.connManager = connManager;
	}

	public static HttpClientConfig init() {
		return init(DEFAULT_MAXTOTAL, DEFAULT_MAXPERROUTE);
	}

	public static HttpClientConfig init(int maxtotal, int maxperroute) {
		// Use a custom connection factory to customize the process of
		// initialization of outgoing HTTP connections. Beside standard connection
		// configuration parameters HTTP connection factory can define message
		// parser / writer routines to be employed by individual connections.
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory();

		// Client HTTP connection objects when fully initialized can be bound to
		// an arbitrary network socket. The process of network socket initialization,
		// its connection to a remote address and binding to a local one is controlled
		// by a connection socket factory.

		// SSL context for secure connections can be created either based on
		// system or application specific properties.
		SSLContext sslcontext = SSLContexts.createSystemDefault();
		// Use custom hostname verifier to customize SSL hostname verification.
		HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();

		// Create a registry of custom connection socket factories for supported
		// protocol schemes.
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier)).build();

		// Create a connection manager with custom configuration.
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry,
				connFactory);
		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		// Configure the connection manager to use socket configuration either
		// by default or for a specific host.
		connManager.setDefaultSocketConfig(socketConfig);
		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
				.setMaxLineLength(2000).build();
		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();
		// Configure the connection manager to use connection configuration either
		// by default or for a specific host.
		connManager.setDefaultConnectionConfig(connectionConfig);
		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		connManager.setMaxTotal(maxtotal);
		connManager.setDefaultMaxPerRoute(maxperroute);
		connManager.setValidateAfterInactivity(1000 * 30);

		return new HttpClientConfig(connManager);
	}

	public void addConfig(String host) {
		addConfig(host, DEFAULT_PORT, DEFAULT_MAXPERROUTE);
	}

	public void addConfig(String host, int port, int maxperrote) {
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connManager.setSocketConfig(new HttpHost(host, port), socketConfig);
		connManager.setConnectionConfig(new HttpHost(host, port), ConnectionConfig.DEFAULT);
		connManager.setMaxPerRoute(new HttpRoute(new HttpHost(host, port)), 20);
	}

	public CloseableHttpClient getHttpClient(String host, int port) {
		// Use custom cookie store if necessary.
		CookieStore cookieStore = new BasicCookieStore();
		// Create global request configuration
		RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
				.setExpectContinueEnabled(false).build();
		ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
				// TODO configurable
				return 20 * 1000; // tomcat默认keepAliveTimeout为20s
			}
		};
		// Create an HttpClient with the given custom dependencies and configuration.
		return HttpClients.custom().useSystemProperties().setConnectionManager(connManager)
				.setDefaultCookieStore(cookieStore).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
				.setKeepAliveStrategy(connectionKeepAliveStrategy).setDefaultRequestConfig(defaultRequestConfig)
				.build();
	}

	public void releaseConn(HttpClientConnection conn) {
		connManager.releaseConnection(conn, null, 30, TimeUnit.SECONDS);
	}

	public void destroy() {
		connManager.close();
	}
}
