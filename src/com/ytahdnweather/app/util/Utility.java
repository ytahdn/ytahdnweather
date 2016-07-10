package com.ytahdnweather.app.util;

import com.ytahdnweather.app.model.City;
import com.ytahdnweather.app.model.County;
import com.ytahdnweather.app.model.Province;

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
			}
		}
		return false;

	}

}
