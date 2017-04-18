package net.youmi.ads.base.network;

import android.content.Context;
import android.text.TextUtils;

import net.youmi.ads.base.utils.NetworkUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author zhitao
 * @since 2017-04-13 15:37
 */
public class HttpRequesterFactory {
	
	/**
	 * 从连接池中取连接的超时时间:1秒
	 */
	private final static int TIMEOUT_GET_CONNECTION_FROM_POOL = 1000;
	
	/**
	 * 设置http超时，即通过网络与服务器建立连接的超时时间:5秒
	 */
	private final static int TCP_CONNECTION_TIME_OUT = 5000;
	
	/**
	 * 设置socket超时，即即从服务器获取响应数据需要等待的时间:10秒
	 * 假设网速十分慢，只有0.1kb/s　那么如果这里设置为10秒的话，那么在这个情况下只能读取1Kb Max
	 */
	private final static int SOCKET_CONNECTION_TIME_OUT = 10000;
	
	/**
	 * 创建HttpURLConnection
	 *
	 * @param context    上下文
	 * @param requestUrl 请求url，如果是带有中文字符的url 需要先转码，在传入
	 *
	 * @return HttpURLConnection
	 *
	 * @throws IOException 创建失败，可能产生的IO异常
	 */
	public static HttpURLConnection newHttpURLConnection(Context context, String requestUrl) throws IOException {
		return newHttpURLConnection(context, requestUrl, null, TCP_CONNECTION_TIME_OUT, SOCKET_CONNECTION_TIME_OUT);
	}
	
	/**
	 * 创建HttpURLConnection
	 *
	 * @param context    上下文
	 * @param requestUrl 请求url，如果是带有中文字符的url 需要先转码，在传入
	 * @param userAgent  自定义的userAgent，可以不传，不传的话，会用sdk默认构造的userAgent
	 *
	 * @return HttpURLConnection
	 *
	 * @throws IOException 创建失败，可能产生的IO异常
	 */
	public static HttpURLConnection newHttpURLConnection(Context context, String requestUrl, String userAgent)
			throws IOException {
		return newHttpURLConnection(context, requestUrl, userAgent, TCP_CONNECTION_TIME_OUT, SOCKET_CONNECTION_TIME_OUT);
	}
	
	/**
	 * 创建HttpURLConnection
	 *
	 * @param context                    上下文
	 * @param requestUrl                 请求url，如果是带有中文字符的url 需要先转码，在传入
	 * @param userAgent                  自定义的userAgent，可以不传，不传的话，会用sdk默认构造的userAgent
	 * @param tcpConnectionTimeOut_ms    设置tcp连接超时，即通过网络与服务器建立连接的超时时间(毫秒)
	 * @param socketConnectionTimeout_ms 设置socket超时，即即从服务器获取响应数据需要等待的时间(毫秒)
	 *
	 * @return HttpURLConnection
	 *
	 * @throws IOException 创建失败，可能产生的IO异常
	 */
	public static HttpURLConnection newHttpURLConnection(Context context, String requestUrl, String userAgent,
			int tcpConnectionTimeOut_ms, int socketConnectionTimeout_ms) throws IOException {
		if (TextUtils.isEmpty(requestUrl)) {
			return null;
		}
		
		// 为运营商的wap网络设置代理
		String apn = NetworkUtils.getApn(context);
		Proxy proxy = null;
		SocketAddress sa = null;
		if (apn.equals(NetworkUtils.APN.APN_CMWAP) || apn.equals(NetworkUtils.APN.APN_3GWAP) ||
		    apn.equals(NetworkUtils.APN.APN_UNIWAP)) {
			sa = new InetSocketAddress("10.0.0.172", 80);
		}
		if (apn.equals(NetworkUtils.APN.APN_CTWAP)) {
			sa = new InetSocketAddress("10.0.0.200", 80);
		}
		if (sa != null) {
			proxy = new Proxy(Proxy.Type.HTTP, sa);
		}
		
		URL url = new URL(requestUrl);
		URLConnection urlConnection;
		if (proxy == null) {
			// url对象的openConnection() 方法返回一个HttpURLConnection 对象,这个对象表示应用程序和url之间的通信连接
			urlConnection = url.openConnection();
		} else {
			urlConnection = url.openConnection(proxy);
		}
		
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		
		// 设置连接超时
		httpUrlConnection.setConnectTimeout(tcpConnectionTimeOut_ms);
		
		// 设置socket超时
		httpUrlConnection.setReadTimeout(socketConnectionTimeout_ms);
		
		// 设置是否向httpUrlConnection输出，默认情况下是false。使用httpUrlConnection.getOutputStream()，把内容输出到远程服务器上。
		// httpUrlConnection.setDoOutput(true);
		
		// 设置是否从httpUrlConnection读入，默认情况下是true。使用httpUrlConnection.getInputStream()，从远程服务器上得到响应的内容。
		httpUrlConnection.setDoInput(true);
		
		// 是否使用缓存,POST请求不能用缓存
		httpUrlConnection.setUseCaches(false);
		
		// 设定传送的内容类型是可序列化的java对象 (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)。
		// httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
		
		// 设定请求的方法为”POST”，默认是GET 。
		// httpUrlConnection.setRequestMethod("POST");
		
		// 设定是否自动处理重定向请求，默认是会自动处理的
		// httpUrlConnection.setInstanceFollowRedirects(true);
		
		return httpUrlConnection;
	}
	
}
