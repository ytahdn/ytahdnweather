package com.ytahdnweather.app.adapter;

import java.util.List;

import com.ytahdnweather.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AreaAdapter extends ArrayAdapter<String> {

	private int resourceId;

	public AreaAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String arr = getItem(position);
		View v;
		if (convertView == null) {
			v = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			v = convertView;
		}

		TextView area_text = (TextView) v.findViewById(R.id.area_text);
		area_text.setText(arr);
		return v;
	}

}
