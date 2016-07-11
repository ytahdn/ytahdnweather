package com.ytahdnweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Build;

public class HttpUtil {

	/**
	 * 发送请求 返回数据
	 * 
	 * @param address
	 * @param listener
	 */
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				/*HttpURLConnection connection = null;*/

				try {
					/*这种方式报 EOFEXCEPTION
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					*/
					InputStream in =getUrlData(address);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// 回调onFinish()方法
						listener.OnFinished(response.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					if (listener != null) {
						listener.onError(e);
						e.printStackTrace();
					}
				} finally {}
			}
		}).start();

	}

	public static InputStream getUrlData(final String url)
			throws URISyntaxException, ClientProtocolException, IOException {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet method = new HttpGet(new URI(url));
		final HttpResponse res = client.execute(method);
		return res.getEntity().getContent();
	}

}
