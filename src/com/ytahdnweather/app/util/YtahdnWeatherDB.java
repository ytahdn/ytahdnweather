package com.ytahdnweather.app.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ytahdnweather.app.db.YtahdnWeatherOpenHelper;
import com.ytahdnweather.app.model.City;
import com.ytahdnweather.app.model.County;
import com.ytahdnweather.app.model.Province;

public class YtahdnWeatherDB {

	/**
	 * 数据库名
	 * 
	 */
	public static final String DB_NAME = "ytahdn_weather";

	/**
	 * 版本
	 * 
	 */
	public static final int VERSION = 1;

	private static YtahdnWeatherDB ytahdnWeatherDB;
	private SQLiteDatabase db;

	private YtahdnWeatherDB(Context context) {

		YtahdnWeatherOpenHelper helper = new YtahdnWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = helper.getWritableDatabase();
	}

	/**
	 * 获取 YtahdnWeatherDB 示例
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static YtahdnWeatherDB getInstence(Context context) {

		if (ytahdnWeatherDB == null) {

			ytahdnWeatherDB = new YtahdnWeatherDB(context);
		}
		return ytahdnWeatherDB;
	}

	/**
	 * 将Province存储到数据库
	 * 
	 * @param province
	 */
	public void saveProvince(Province province) {
		if (province != null) {

			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}

	}

	/**
	 * 获取Province内容
	 * 
	 * @return
	 */
	public List<Province> loadProvinces() {

		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {

			do {

				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将city存储到数据库
	 * 
	 * @param province
	 */
	public void saveCity(City city) {
		if (city != null) {

			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}

	}
	/**读取某个省份下的所有 城市
	 * 
	 */
	public  List<City> loadCitys(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {

			do {

				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**将County保存到数据库
	 * @param county
	 */
	public void saveCounty(County county){
		
		if (county != null) {

			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
			
		}
	}
	
	/**一个市下面的县
	 * @param cityId
	 * @return
	 */
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {

			do {

				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	

}
