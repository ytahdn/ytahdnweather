package com.ytahdnweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ytahdnweather.app.R;
import com.ytahdnweather.app.adapter.AreaAdapter;
import com.ytahdnweather.app.model.City;
import com.ytahdnweather.app.model.County;
import com.ytahdnweather.app.model.Province;
import com.ytahdnweather.app.util.HttpCallbackListener;
import com.ytahdnweather.app.util.HttpUtil;
import com.ytahdnweather.app.util.LogUtil;
import com.ytahdnweather.app.util.Utility;
import com.ytahdnweather.app.util.WeatherApplication;
import com.ytahdnweather.app.util.YtahdnWeatherDB;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private ListView listview;
	private TextView titletext;
	private AreaAdapter adapter;
	private List<String> dataList = new ArrayList<String>();
	private YtahdnWeatherDB db;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private Province selectedProvince;
	private City selectedCity;
	private int currentLevel;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listview = (ListView) findViewById(R.id.list_view);
		titletext = (TextView) findViewById(R.id.title_text);
		adapter = new AreaAdapter(this, R.layout.area_item, dataList);
		listview.setAdapter(adapter);
		db = YtahdnWeatherDB.getInstence(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCounties();
				}

			}
		});

		queryProvinces();
	}

	/**
	 * 加载省份信息
	 */
	public void queryProvinces() {

		provinceList = db.loadProvinces();

		if (provinceList.size() > 0) {

			dataList.clear();
			for (Province p : provinceList) {

				dataList.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titletext.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * 查询市级城市
	 * 
	 */
	public void queryCities() {
		cityList = db.loadCitys(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titletext.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询县级城市
	 * 
	 */
	public void queryCounties() {
		countyList = db.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titletext.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * 从服务器查找数据
	 * 
	 * @param code
	 * @param type
	 */
	public void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {

			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Toast.makeText(WeatherApplication.getContext(), "获取天气省份信息出错",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnFinished(String response) {
				// TODO Auto-generated method stub
				LogUtil.d("response", response);
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(response, db);
				} else if ("city".equals(type)) {
					result = Utility.handCitiesResponse(response, db,
							selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(response, db,
							selectedCity.getId());
				}

				if (result) {

					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}
		});

	}

	/**
	 * 打开进度对话框
	 * 
	 */
	public void showProgressDialog() {

		if (dialog == null) {

			dialog = new ProgressDialog(this);
			dialog.setMessage("正在加载...");
			dialog.setCanceledOnTouchOutside(false);
		}
		dialog.show();
	}

	/**
	 * 关闭进度对话框
	 * 
	 */
	public void closeProgressDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	/**
	 * 自定义返回键
	 * 
	 * */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (currentLevel == LEVEL_COUNTY) {

			queryCities();
		} else if (currentLevel == LEVEL_CITY) {

			queryProvinces();
		} else {

			finish();
		}
	}
}
