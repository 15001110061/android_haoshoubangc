package com.cgzz.job.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.R;
import com.cgzz.job.utils.SystemParams;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.ScaleGallery;

@SuppressWarnings("deprecation")
public class MyGalleryAdapter extends BaseAdapter {
	Drawable btnDrawablec = null;

	Drawable btnDrawabled = null;
	Drawable btnDrawablee = null;
	String titleDrawablec = "";
	String titleDrawabled = "";
	String titleDrawablee = "";
	private Context context;
	private SystemParams systemParams;
	private List<Map<String, String>> data;
	ScaleGallery gallery;

	public MyGalleryAdapter(Context context, ScaleGallery gallery) {
		this.context = context;
		systemParams = SystemParams.getInstance((Activity) context);
		data = new ArrayList<Map<String, String>>();
		this.gallery = gallery;
	}

	public void refreshMYData(List<Map<String, String>> dataGroup) {
		if (dataGroup != null) {
			data = dataGroup;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.adapter_gallery_item, null);
			// convertView.setLayoutParams(new Gallery.LayoutParams(
			// (int) systemParams.screenWidth * 7/ 8,
			// (int) systemParams.screenWidth *10 /9));
			// convertView.setLayoutParams(new Gallery.LayoutParams(
			// 509,
			// 710));

			convertView.setLayoutParams(
					new Gallery.LayoutParams((int) systemParams.screenWidth * 3 / 4, LayoutParams.WRAP_CONTENT));

			holder = new ViewHolder();
			holder.tv_home_item_time = (TextView) convertView.findViewById(R.id.tv_home_item_time);
			holder.tv_home_item_site = (TextView) convertView.findViewById(R.id.tv_home_item_site);

			holder.tv_home_item_openings2 = (TextView) convertView.findViewById(R.id.tv_home_item_openings2);
			holder.tv_home_item_openings = (TextView) convertView.findViewById(R.id.tv_home_item_openings);
			holder.rl_home_item_c = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_c);

			holder.rl_home_item_d = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_d);
			holder.rl_home_item_e = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_e);

			holder.tv_home_item_count = (TextView) convertView.findViewById(R.id.tv_home_item_count);

			holder.tv_home_titlename = (TextView) convertView.findViewById(R.id.tv_home_titlename);

			holder.rl_home_item_b = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_b);

			holder.iv_home_item_voice = (ImageView) convertView.findViewById(R.id.iv_home_item_voice);
			holder.iv_home_item_c = (TextView) convertView.findViewById(R.id.iv_home_item_c);
			holder.iv_home_item_d = (TextView) convertView.findViewById(R.id.iv_home_item_d);
			holder.iv_home_item_e = (TextView) convertView.findViewById(R.id.iv_home_item_e);

			holder.rl_home_item_f = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_f);
			holder.tv_home_item_time2 = (TextView) convertView.findViewById(R.id.tv_home_item_time2);
			holder.tv_home_is_changqi = (TextView) convertView.findViewById(R.id.tv_home_is_changqi);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// convertView.setBackgroundColor(Color.alpha(1));

		Map<String, String> map = data.get(position);
		holder.tv_home_titlename.setText(map.get("name"));
		holder.tv_home_item_time.setText(map.get("dutydate"));
		holder.tv_home_item_site.setText(map.get("address"));
		holder.tv_home_item_openings.setText(map.get("made"));

		holder.tv_home_item_count.setText(position + 1 + "/" + map.get("count"));

		if (Utils.isEmpty(map.get("ordersystemid"))) {
			holder.tv_home_item_time2.setText("预计清洁:" + map.get("rooms") + "间");
			holder.tv_home_is_changqi.setVisibility(View.GONE);
		} else {
			holder.tv_home_item_time2.setText("预计每天清洁:" + map.get("rooms") + "间");
			holder.tv_home_is_changqi.setVisibility(View.VISIBLE);
		}

		if ("0".equals(map.get("standard_price"))) {
			holder.tv_home_item_openings2.setText(map.get("suite_price"));
		} else {
			holder.tv_home_item_openings2.setText(map.get("standard_price"));
		}

		Resources resources = context.getResources();

		if (!"0".equals(map.get("bounus"))) {
			titleDrawablec = "对方提供额外打赏（每人" + map.get("bounus") + "元）";
			btnDrawablec = resources.getDrawable(R.drawable.icon_dashang_sel);
			holder.iv_home_item_c.setBackgroundDrawable(btnDrawablec);
		} else {
			titleDrawablec = "对方不提供额外打赏";
			btnDrawablec = resources.getDrawable(R.drawable.icon_dashang);
			holder.iv_home_item_c.setBackgroundDrawable(btnDrawablec);
		}

		if ("1".equals(map.get("iscash"))) {
			titleDrawabled = "对方现付结算，当天就可以拿到工资";
			btnDrawabled = resources.getDrawable(R.drawable.icon_xianjin_sel);
			holder.iv_home_item_d.setBackgroundDrawable(btnDrawabled);
		} else {
			titleDrawabled = "对方不提供现付结算待遇";
			btnDrawabled = resources.getDrawable(R.drawable.icon_xianjin);
			holder.iv_home_item_d.setBackgroundDrawable(btnDrawabled);
		}
		if ("1".equals(map.get("havelaunch"))) {
			titleDrawablee = "对方提供午餐待遇";
			btnDrawablee = resources.getDrawable(R.drawable.icon_daiwufan_sel);
			holder.iv_home_item_e.setBackgroundDrawable(btnDrawablee);
		} else {
			titleDrawablee = "对方不提供午餐待遇";
			btnDrawablee = resources.getDrawable(R.drawable.icon_wucan);
			holder.iv_home_item_e.setBackgroundDrawable(btnDrawablee);
		}

		if (!"0".equals(map.get("havebar"))) {
			holder.rl_home_item_f.setVisibility(View.VISIBLE);
		} else {
			holder.rl_home_item_f.setVisibility(View.GONE);
		}

		holder.rl_home_item_b.setOnClickListener(new CancelOrderOnClickListener(position));

		holder.iv_home_item_voice.setOnClickListener(new TelOnClickListener(position));

		holder.iv_home_item_c.setOnClickListener(new RouteOnClickListener(position));

		holder.iv_home_item_d.setOnClickListener(new TextOnClickListener(position));
		holder.iv_home_item_e.setOnClickListener(new PhoneOnClickListener(position));

		holder.rl_home_item_f.setOnClickListener(new ZFOnClickListener(position));

		return convertView;
	}

	class ViewHolder {
		ImageView iv_home_item_voice;
		TextView tv_home_item_time, tv_home_item_site, tv_home_item_openings2, tv_home_item_openings,
				tv_home_item_count, tv_home_titlename, iv_home_item_c, iv_home_item_d, iv_home_item_e,
				tv_home_item_time2, tv_home_is_changqi;
		RelativeLayout rl_home_item_c, rl_home_item_d, rl_home_item_e, rl_home_item_b, rl_home_item_f;
	}

	class ZFOnClickListener implements View.OnClickListener {

		private int position;

		public ZFOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onZFClickListener != null) {
				onZFClickListener.onZFClick(position, v, p13);
			}
		}

	}

	private OnZFClickListener onZFClickListener;
	private int p13;

	public OnZFClickListener getonZFClickListener() {
		return onZFClickListener;
	}

	public void setOnZFClickListener(OnZFClickListener onZFClickListener, int logoUserInfos) {
		this.onZFClickListener = onZFClickListener;
		this.p13 = logoUserInfos;
	}

	public interface OnZFClickListener {
		public void onZFClick(int position, View v, int logo);
	}

	/***
	 * C
	 */

	class RouteOnClickListener implements View.OnClickListener {

		private int position;

		public RouteOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onRouteClickListener != null) {
				onRouteClickListener.onRouteClick(position, v, p8);
			}
		}

	}

	private OnRouteClickListener onRouteClickListener;
	private int p8;

	public OnRouteClickListener getonRouteClickListener() {
		return onRouteClickListener;
	}

	public void setOnRouteClickListener(OnRouteClickListener onRouteClickListener, int logoUserInfos) {
		this.onRouteClickListener = onRouteClickListener;
		this.p8 = logoUserInfos;
	}

	public interface OnRouteClickListener {
		public void onRouteClick(int position, View v, int logo);
	}

	class CancelOrderOnClickListener implements View.OnClickListener {

		private int position;

		public CancelOrderOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onCancelOrderClickListener != null) {
				onCancelOrderClickListener.onCancelOrderClick(position, v, p);
			}
		}

	}

	private OnCancelOrderClickListener onCancelOrderClickListener;
	private int p;

	public OnCancelOrderClickListener getonCancelOrderClickListener() {
		return onCancelOrderClickListener;
	}

	public void setOnCancelOrderClickListener(OnCancelOrderClickListener onCancelOrderClickListener,
			int logoUserInfos) {
		this.onCancelOrderClickListener = onCancelOrderClickListener;
		this.p = logoUserInfos;
	}

	public interface OnCancelOrderClickListener {
		public void onCancelOrderClick(int position, View v, int logo);
	}

	/***
	 * 打电话
	 */

	class TelOnClickListener implements View.OnClickListener {

		private int position;

		public TelOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onTelClickListener != null) {
				onTelClickListener.onTelClick(position, v, p1);
			}
		}

	}

	private OnTelClickListener onTelClickListener;
	private int p1;

	public OnTelClickListener getonTelClickListener() {
		return onTelClickListener;
	}

	public void setOnTelClickListener(OnTelClickListener onTelClickListener, int logoUserInfos) {
		this.onTelClickListener = onTelClickListener;
		this.p1 = logoUserInfos;
	}

	public interface OnTelClickListener {
		public void onTelClick(int position, View v, int logo);
	}

	class TextOnClickListener implements View.OnClickListener {

		private int position;

		public TextOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onTextClickListener != null) {
				onTextClickListener.onTextClick(position, v, p2);
			}
		}

	}

	private OnTextClickListener onTextClickListener;
	private int p2;

	public OnTextClickListener getonTextClickListener() {
		return onTextClickListener;
	}

	public void setOnTextClickListener(OnTextClickListener onTextClickListener, int logoUserInfos) {
		this.onTextClickListener = onTextClickListener;
		this.p2 = logoUserInfos;
	}

	public interface OnTextClickListener {
		public void onTextClick(int position, View v, int logo);
	}

	/***
	 * e
	 */

	class PhoneOnClickListener implements View.OnClickListener {

		private int position;

		public PhoneOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onPhoneClickListener != null) {
				onPhoneClickListener.onPhoneClick(position, v, p7);
			}
		}

	}

	private OnPhoneClickListener onPhoneClickListener;
	private int p7;

	public OnPhoneClickListener getonPhoneClickListener() {
		return onPhoneClickListener;
	}

	public void setonPhoneClickListener(OnPhoneClickListener onPhoneClickListener, int logoUserInfos) {
		this.onPhoneClickListener = onPhoneClickListener;
		this.p7 = logoUserInfos;
	}

	public interface OnPhoneClickListener {
		public void onPhoneClick(int position, View v, int logo);
	}
}
