package com.cgzz.job.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.R;
import com.cgzz.job.adapter.CurrentAdapter.BitmapCache;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.utils.SystemParams;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.ScaleGallery;

@SuppressWarnings("deprecation")
public class BonusListAdapter extends BaseAdapter {
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
	private ImageLoader mImageLoader;

	public BonusListAdapter(Context context, ScaleGallery gallery) {
		this.context = context;
		systemParams = SystemParams.getInstance((Activity) context);
		data = new ArrayList<Map<String, String>>();
		this.gallery = gallery;
		mImageLoader = new ImageLoader(GlobalVariables.getRequestQueue(context), new BitmapCache());
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
			convertView = View.inflate(context, R.layout.activity_bonus_list, null);

			convertView.setLayoutParams(
					new Gallery.LayoutParams((int) systemParams.screenWidth * 3 / 4, LayoutParams.WRAP_CONTENT));

			holder = new ViewHolder();
			holder.rl_bonus_qiang = (RelativeLayout) convertView.findViewById(R.id.rl_bonus_qiang);

			holder.tv_bonus_logo = (ImageView) convertView.findViewById(R.id.tv_bonus_logo);
			holder.tv_bonus_titless = (TextView) convertView.findViewById(R.id.tv_bonus_titless);

			holder.tv_bonus_title = (TextView) convertView.findViewById(R.id.tv_bonus_title);
			holder.tv_bonus_qiang = (TextView) convertView.findViewById(R.id.tv_bonus_qiang);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);
		holder.tv_bonus_titless.setText(map.get("message"));
		holder.tv_bonus_title.setText(map.get("name"));

		if (!Utils.isEmpty(map.get("is_grab"))) {
			if ("1".equals(map.get("is_grab"))) {// 1：拆了 0：没拆
				holder.tv_bonus_qiang.setText("查看记录");
			} else if ("0".equals(map.get("is_grab"))) {
				holder.tv_bonus_qiang.setText("抢红包");
			}
		}

		String image = map.get("img");

		ImageListener listener = ImageLoader.getImageListener(holder.tv_bonus_logo, R.drawable.icon_bouns_logo,
				R.drawable.icon_bouns_logo);
		try {
			mImageLoader.get(image, listener);
		} catch (Exception e) {
		}

		holder.rl_bonus_qiang.setOnClickListener(new CancelOrderOnClickListener(position));

		return convertView;
	}

	class ViewHolder {
		ImageView tv_bonus_logo;
		TextView tv_bonus_titless, tv_bonus_title, tv_bonus_qiang;
		RelativeLayout rl_bonus_qiang;
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
