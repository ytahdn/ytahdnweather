package com.ytahdnweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytahdnweather.app.R;
import com.ytahdnweather.app.util.HttpCallbackListener;
import com.ytahdnweather.app.util.HttpUtil;
import com.ytahdnweather.app.util.LogUtil;
import com.ytahdnweather.app.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;// 城市名
	private TextView publishText;// 发布时间
	private TextView weatherDespText;// 天气描述信息
	private TextView temp1Text;// 气温1
	private TextView temp2Text;// 气温2
	private TextView currentDateText;// 当前日期
	private Button switchCity;// 城市切换
	private Button refreshWeather;// 更新天气

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// 初始化空间
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		String county_code = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(county_code)) {
			// 根据县级code查询天气信息
			publishText.setText("刷新中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(county_code);
		} else {
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:// 切换城市
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:// 刷新天气
			publishText.setText("刷新中...");
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String weatherCode = preferences.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}else{
				publishText.setText("刷新失败");
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 查询县级代号所对应的天气代号
	 * 
	 * @param countyCode
	 */
	public void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}

	/**
	 * 根据天气代码查询具体的天气信息
	 * 
	 * @param weatherCode
	 */
	public void queryWeatherInfo(String weatherCode) {

		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * 显示天气信息
	 * 
	 */
	public void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WeatherActivity.this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);

	}

	/**
	 * 从服务器查询信息
	 * 
	 * @param adderss
	 * @param countyCode
	 */
	public void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onError(final Exception e) {
				
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("刷新失败");
						e.printStackTrace();
						LogUtil.d("exception", e.toString());
					}
				});
			}

			@Override
			public void OnFinished(String response) {
				// TODO Auto-generated method stub
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// 查询县级城市code对应的天气code
						String[] arr = response.split("\\|");
						if (arr != null && arr.length == 2) {

							String weatherCode = arr[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this,
							response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}

			}
		});
	}

}
