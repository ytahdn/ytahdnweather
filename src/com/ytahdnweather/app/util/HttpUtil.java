package com.ytahdnweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuffer response = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						listener.OnFinished(response.toString());
					}

				} catch (Exception e) {
					// TODO: handle exception
					if (listener != null) {

						listener.onError(e);
					}
				}
			}
		}).start();

	}

}
