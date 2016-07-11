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
	private TextView cityNameText;// ������
	private TextView publishText;// ����ʱ��
	private TextView weatherDespText;// ����������Ϣ
	private TextView temp1Text;// ����1
	private TextView temp2Text;// ����2
	private TextView currentDateText;// ��ǰ����
	private Button switchCity;// �����л�
	private Button refreshWeather;// ��������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// ��ʼ���ռ�
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
			// �����ؼ�code��ѯ������Ϣ
			publishText.setText("ˢ����...");
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
		case R.id.switch_city:// �л�����
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:// ˢ������
			publishText.setText("ˢ����...");
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String weatherCode = preferences.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}else{
				publishText.setText("ˢ��ʧ��");
			}

			break;

		default:
			break;
		}
	}

	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * 
	 * @param countyCode
	 */
	public void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}

	/**
	 * �������������ѯ�����������Ϣ
	 * 
	 * @param weatherCode
	 */
	public void queryWeatherInfo(String weatherCode) {

		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * ��ʾ������Ϣ
	 * 
	 */
	public void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WeatherActivity.this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);

	}

	/**
	 * �ӷ�������ѯ��Ϣ
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
						publishText.setText("ˢ��ʧ��");
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
						// ��ѯ�ؼ�����code��Ӧ������code
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
