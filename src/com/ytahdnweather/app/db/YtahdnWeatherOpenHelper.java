package com.ytahdnweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class YtahdnWeatherOpenHelper extends SQLiteOpenHelper {

	/**建表
	 * 省份
	 */
	private static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement" + ",province_name text"
			+ ",province_code text)";
	/**建表
	 * 市区
	 */
	private static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement" + ",city_name text"
			+ ",city_code text" + ",province_id integer)";
	/**建表
	 * 县级
	 */
	private static final String CREATE_COUNTY = "create table County("
			+ "id integer primary key autoincrement" + ",county_name text"
			+ ",county_code text" + ",city_id integer)";

	public YtahdnWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);//创建省
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
