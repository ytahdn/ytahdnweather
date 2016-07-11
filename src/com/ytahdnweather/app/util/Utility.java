package com.ytahdnweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.ytahdnweather.app.model.City;
import com.ytahdnweather.app.model.County;
import com.ytahdnweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 * 
	 * @param response
	 * @param db
	 * @return
	 */
	public synchronized static Boolean handleProvincesResponse(String response,
			YtahdnWeatherDB db) {
		if (!TextUtils.isEmpty(response)) {

			String[] provinces = response.split(",");
			if (provinces != null && provinces.length > 0) {

				for (String p : provinces) {

					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceName(array[1]);
					province.setProvinceCode(array[0]);
					db.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级
	 * 
	 * @param response
	 * @param db
	 * @param provinceId
	 * @return
	 */
	public synchronized static Boolean handCitiesResponse(String response,
			YtahdnWeatherDB db, int provinceId) {
		if (!TextUtils.isEmpty(response)) {

			String[] cities = response.split(",");
			if (cities != null && cities.length > 0) {

				for (String c : cities) {

					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					db.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据并保存
	 * 
	 * @param response
	 * @param db
	 * @param cityId
	 * @return
	 */
	public synchronized static Boolean handleCountiesResponse(String response,
			YtahdnWeatherDB db, int cityId) {

		if (!TextUtils.isEmpty(response)) {

			String[] counties = response.split(",");
			if (counties != null && counties.length > 0) {

				for (String c : counties) {

					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					db.saveCounty(county);
				}
				return true;
			}
		}
		return false;

	}

	/**
	 * 解析服务器返回的JSON，并将解析出的数据存储到本地
	 * 
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfoToSharedPrreference(context, cityName, weatherCode,
					temp1, temp2, weatherDesp, publishTime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveWeatherInfoToSharedPrreference(Context context,
			String cityName, String weatherCode, String temp1, String temp2,
			String weatherDesp, String publishTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日",
				Locale.CHINA);

		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_select", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", format.format(new Date()));
		editor.commit();
	}

}
