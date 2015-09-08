package com.cgzz.job.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.cgzz.job.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReviewsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private LinearLayout tv_wages_item_title;
	TextView tv_wages_item_title2;
	private int clickTemp = -1;
	private Context context;//
	private ArrayList<Map<String, String>> datas;

	public ReviewsAdapter(Context context) {
		this.context = context;
		// this.myImageIds=myImageIds;
		datas = new ArrayList<Map<String, String>>();
		this.inflater = LayoutInflater.from(context);
	}

	public void refreshMYData(ArrayList<Map<String, String>> dataGroup) {
		if (dataGroup != null) {
			datas = dataGroup;
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSeclection(int position) {
		clickTemp = position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.reviewsadapter_item, null);

		tv_wages_item_title = (LinearLayout) convertView.findViewById(R.id.tv_wages_item_title);
		tv_wages_item_title2 = (TextView) convertView.findViewById(R.id.tv_wages_item_title2);

		Map<String, String> map = datas.get(position);
		String dict_value = map.get("dict_value");
		String Select = map.get("Select");
		tv_wages_item_title2.setText(dict_value);
		if ("1".equals(Select)) {
			tv_wages_item_title.setBackgroundResource(R.drawable.shape_current_item_et_bgs);
		} else {
			tv_wages_item_title.setBackgroundResource(R.drawable.shape_current_item_et_bg);
		}
		return convertView;
	}

}
