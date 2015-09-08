package com.cgzz.job.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;

/***
 * 当前订单适配器
 * 
 */
public class CurrentAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public CurrentAdapter(Context contexts) {
		this.context = contexts;
		this.inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
		mImageLoader = new ImageLoader(
				GlobalVariables.getRequestQueue(context), new BitmapCache());
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = getInviteView(position, convertView);
		return convertView;
	}

	private View getInviteView(final int position, View convertView) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.fragment_current_item, null);
			holder = new ViewHolder();
			holder.tv_current_name = (TextView) convertView
					.findViewById(R.id.tv_current_name);

			holder.tv_current_state = (TextView) convertView
					.findViewById(R.id.tv_current_state);

			holder.tv_current_room = (TextView) convertView
					.findViewById(R.id.tv_current_room);

			holder.tv_current_peonumber = (TextView) convertView
					.findViewById(R.id.tv_current_peonumber);
			holder.tv_current_time = (TextView) convertView
					.findViewById(R.id.tv_current_time);
			holder.tv_current_route = (TextView) convertView
					.findViewById(R.id.tv_current_route);
			holder.tv_current_route1 = (TextView) convertView
					.findViewById(R.id.tv_current_route1);
			
			
			holder.tv_current_cancel_order = (TextView) convertView
					.findViewById(R.id.tv_current_cancel_order);
			holder.tv_current_picture = (ImageView) convertView
					.findViewById(R.id.tv_current_picture);

			holder.iv_current_tel = (ImageView) convertView
					.findViewById(R.id.iv_current_tel);

			holder.iv_current_text = (ImageView) convertView
					.findViewById(R.id.iv_current_text);

			holder.relativeLayout1 = (RelativeLayout) convertView
					.findViewById(R.id.relativeLayout1);
			holder.tv_current_name2 = (TextView) convertView
					.findViewById(R.id.tv_current_name2);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);
		holder.tv_current_name.setText(map.get("name"));
		String status = map.get("status");
		if ("0".equals(status)) {
			holder.tv_current_state.setText("尚未开始");
		} else if ("1".equals(status)) {
			holder.tv_current_state.setText("进行中");
			holder.tv_current_cancel_order.setVisibility(View.GONE);
		}

		String arrive = map.get("arrive");
		if ("0".equals(arrive)) {
			holder.tv_current_route.setVisibility(View.VISIBLE);
			holder.tv_current_route1.setVisibility(View.GONE);
		} else if ("1".equals(arrive)) {
			holder.tv_current_route.setVisibility(View.GONE);
			holder.tv_current_route1.setVisibility(View.VISIBLE);
			
		}
//		holder.tv_current_name2.setText("ID:" + map.get("orderid"));
		holder.tv_current_room.setText("清洁房间:" + map.get("roomcount"));
		holder.tv_current_peonumber.setText("帮客人数:" + map.get("workercount"));
		holder.tv_current_time.setText("开始时间:" + map.get("dutydate"));
		holder.tv_current_cancel_order
				.setOnClickListener(new CancelOrderOnClickListener(position));

		holder.relativeLayout1.setOnClickListener(new RouteOnClickListener(
				position));
		holder.tv_current_route.setOnClickListener(new TelOnClickListener(
				position));

		String image = map.get("front_photos");

		ImageListener listener = ImageLoader.getImageListener(
				holder.tv_current_picture, R.drawable.image_moren,
				R.drawable.image_moren);
		try {
			 mImageLoader.get(image, listener);
		} catch (Exception e) {
		}
		return convertView;
	}

	class ViewHolder {
		ImageView tv_current_picture, iv_current_tel, iv_current_text;

		TextView tv_current_name, tv_current_state, tv_current_route,
				tv_current_peonumber, tv_current_time, tv_current_room,
				tv_current_cancel_order,tv_current_route1,tv_current_name2;
		RelativeLayout relativeLayout1;
	}

	/***
	 * 发短信
	 */

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

	public void setOnTextClickListener(OnTextClickListener onTextClickListener,
			int logoUserInfos) {
		this.onTextClickListener = onTextClickListener;
		this.p2 = logoUserInfos;
	}

	public interface OnTextClickListener {
		public void onTextClick(int position, View v, int logo);
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

	public void setOnTelClickListener(OnTelClickListener onTelClickListener,
			int logoUserInfos) {
		this.onTelClickListener = onTelClickListener;
		this.p1 = logoUserInfos;
	}

	public interface OnTelClickListener {
		public void onTelClick(int position, View v, int logo);
	}

	/***
	 * 取消订单
	 */

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

	public void setOnCancelOrderClickListener(
			OnCancelOrderClickListener onCancelOrderClickListener,
			int logoUserInfos) {
		this.onCancelOrderClickListener = onCancelOrderClickListener;
		this.p = logoUserInfos;
	}

	public interface OnCancelOrderClickListener {
		public void onCancelOrderClick(int position, View v, int logo);
	}

	/***
	 * 查看路线
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

	public void setOnRouteClickListener(
			OnRouteClickListener onRouteClickListener, int logoUserInfos) {
		this.onRouteClickListener = onRouteClickListener;
		this.p8 = logoUserInfos;
	}

	public interface OnRouteClickListener {
		public void onRouteClick(int position, View v, int logo);
	}

	/***
	 * 取消邀请
	 */

	class RefuseOnClickListener implements View.OnClickListener {

		private int position;

		public RefuseOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onRefuseClickListener != null) {
				onRefuseClickListener.onRefuseClick(position, v, p4);
			}
		}

	}

	private OnRefuseClickListener onRefuseClickListener;
	private int p4;

	public OnRefuseClickListener getonRefuseClickListener() {
		return onRefuseClickListener;
	}

	public void setonRefuseClickListener(
			OnRefuseClickListener onRefuseClickListener, int logoUserInfos) {
		this.onRefuseClickListener = onRefuseClickListener;
		this.p4 = logoUserInfos;
	}

	public interface OnRefuseClickListener {
		public void onRefuseClick(int position, View v, int logo);
	}

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}

			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}
}
