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
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;

/***
 * 完成订单适配器
 * 
 */
public class FillOrdersAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public FillOrdersAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.fragment_fillorders_item,
					null);
			holder = new ViewHolder();
			holder.tv_fillorders_1 = (TextView) convertView
					.findViewById(R.id.tv_fillorders_1);
			holder.tv_fillorders_2 = (TextView) convertView
					.findViewById(R.id.tv_fillorders_2);

			holder.tv_fillorders_3 = (TextView) convertView
					.findViewById(R.id.tv_fillorders_3);

			holder.tv_fillorders_4 = (TextView) convertView
					.findViewById(R.id.tv_fillorders_4);

			holder.tv_fillorders_5 = (TextView) convertView
					.findViewById(R.id.tv_fillorders_5);

			holder.tv_fillorders_state = (TextView) convertView
					.findViewById(R.id.tv_fillorders_state);

			holder.tv_fillorders_name = (TextView) convertView
					.findViewById(R.id.tv_fillorders_name);

			holder.tv_fillorders_picture = (ImageView) convertView
					.findViewById(R.id.tv_fillorders_picture);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);

		holder.tv_fillorders_name.setText(map.get("name"));
		String status = map.get("status");
		if ("2".equals(status)) {
			holder.tv_fillorders_state.setText("已完成");

			holder.tv_fillorders_1
					.setText("清洁房间: " + map.get("roomcount") + "间");
			holder.tv_fillorders_2.setText("帮客人数: " + map.get("workercount")
					+ "人");

			holder.tv_fillorders_3.setText("实际收入: ¥" + map.get("made"));

			holder.tv_fillorders_4.setText("所获积分: " + map.get("score") + "积分");

			holder.tv_fillorders_5.setText("开始时间: " + map.get("dutydate"));

		} else if ("-1".equals(status)) {
			holder.tv_fillorders_state.setText("已取消");

			holder.tv_fillorders_1
					.setText("清洁房间: " + map.get("roomcount") + "间");
			holder.tv_fillorders_2.setText("帮客人数: " + map.get("workercount")
					+ "人");
			holder.tv_fillorders_3.setText("开始时间: " + map.get("dutydate"));
			holder.tv_fillorders_4.setText("取消理由: " + map.get("dict_value"));
			holder.tv_fillorders_5.setVisibility(View.GONE);
		}

		String image = map.get("front_photos");
		ImageListener listener = ImageLoader.getImageListener(
				holder.tv_fillorders_picture, R.drawable.image_moren,
				R.drawable.image_moren);
		try {
			mImageLoader.get(image, listener);
		} catch (Exception e) {
		}
		return convertView;
	}

	class ViewHolder {
		ImageView tv_fillorders_picture;

		TextView tv_fillorders_1, tv_fillorders_2, tv_fillorders_3,
				tv_fillorders_4, tv_fillorders_5, tv_fillorders_state,
				tv_fillorders_name;

	}

	// /***
	// * 取消订单
	// */
	//
	// class CancelOrderOnClickListener implements View.OnClickListener {
	//
	// private int position;
	//
	// public CancelOrderOnClickListener(int position) {
	// this.position = position;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// if (onCancelOrderClickListener != null) {
	// onCancelOrderClickListener.onCancelOrderClick(position, v, p);
	// }
	// }
	//
	// }
	//
	// private OnCancelOrderClickListener onCancelOrderClickListener;
	// private int p;
	//
	// public OnCancelOrderClickListener getonCancelOrderClickListener() {
	// return onCancelOrderClickListener;
	// }
	//
	// public void setOnCancelOrderClickListener(
	// OnCancelOrderClickListener onCancelOrderClickListener,
	// int logoUserInfos) {
	// this.onCancelOrderClickListener = onCancelOrderClickListener;
	// this.p = logoUserInfos;
	// }
	//
	// public interface OnCancelOrderClickListener {
	// public void onCancelOrderClick(int position, View v, int logo);
	// }
	//
	// /***
	// * 查看路线
	// */
	//
	// class RouteOnClickListener implements View.OnClickListener {
	//
	// private int position;
	//
	// public RouteOnClickListener(int position) {
	// this.position = position;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// if (onRouteClickListener != null) {
	// onRouteClickListener.onRouteClick(position, v, p8);
	// }
	// }
	//
	// }
	//
	// private OnRouteClickListener onRouteClickListener;
	// private int p8;
	//
	// public OnRouteClickListener getonRouteClickListener() {
	// return onRouteClickListener;
	// }
	//
	// public void setOnRouteClickListener(
	// OnRouteClickListener onRouteClickListener, int logoUserInfos) {
	// this.onRouteClickListener = onRouteClickListener;
	// this.p8 = logoUserInfos;
	// }
	//
	// public interface OnRouteClickListener {
	// public void onRouteClick(int position, View v, int logo);
	// }
	//
	// /***
	// * 取消邀请
	// */
	//
	// class RefuseOnClickListener implements View.OnClickListener {
	//
	// private int position;
	//
	// public RefuseOnClickListener(int position) {
	// this.position = position;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// if (onRefuseClickListener != null) {
	// onRefuseClickListener.onRefuseClick(position, v, p4);
	// }
	// }
	//
	// }
	//
	// private OnRefuseClickListener onRefuseClickListener;
	// private int p4;
	//
	// public OnRefuseClickListener getonRefuseClickListener() {
	// return onRefuseClickListener;
	// }
	//
	// public void setonRefuseClickListener(
	// OnRefuseClickListener onRefuseClickListener, int logoUserInfos) {
	// this.onRefuseClickListener = onRefuseClickListener;
	// this.p4 = logoUserInfos;
	// }
	//
	// public interface OnRefuseClickListener {
	// public void onRefuseClick(int position, View v, int logo);
	// }

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
