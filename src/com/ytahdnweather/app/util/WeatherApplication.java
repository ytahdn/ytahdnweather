package com.ytahdnweather.app.util;

import android.app.Application;
import android.content.Context;

public class WeatherApplication extends Application {
	private static Context context;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
	}
	/**��ȡ������
	 * @return
	 */
	public static Context getContext(){
		
		return context;
	}

}
