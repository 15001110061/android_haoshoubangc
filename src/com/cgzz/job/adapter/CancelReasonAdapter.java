package com.cgzz.job.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgzz.job.R;

public class CancelReasonAdapter extends BaseAdapter {

	// ������ݵ�list
//	private ArrayList<Map<String, String>> list;
	// ������
	private Context context;
	// �������벼��
	private LayoutInflater inflater = null;
	private List<Map<String, String>> data;
	// ������
	public CancelReasonAdapter(
			Context context) {
		this.context = context;
//		this.list = list;
		data = new ArrayList<Map<String, String>>();
		inflater = LayoutInflater.from(context);
	}

	public void refreshMYData(List<Map<String, String>> dataGroup) {
		if (dataGroup != null) {
			data = dataGroup;
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolders holder = null;
		if (convertView == null) {
			// ���ViewHolder����
			holder = new ViewHolders();
			// ���벼�ֲ���ֵ��convertview
			convertView = inflater.inflate(R.layout.cancelreason_listviewitem,
					null);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
//			holder.img = (ImageView) convertView.findViewById(R.id.item_iv);
			// Ϊview���ñ�ǩ
			convertView.setTag(holder);
		} else {
			// ȡ��holder
			holder = (ViewHolders) convertView.getTag();
		}
		// ����list��TextView����ʾ
		holder.tv.setText(data.get(position).get("dict_value").toString());
		// ����flag������checkbox��ѡ��״��
		holder.cb.setChecked("true".equals(data.get(position).get("flag")));
		return convertView;
	}

	public final class ViewHolders {
		public TextView tv;
		public CheckBox cb;
//		public ImageView img;
	}
}