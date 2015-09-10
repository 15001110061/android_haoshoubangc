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
import com.cgzz.job.adapter.WorkcardAdapter.OnTelClickListener;
import com.cgzz.job.adapter.WorkcardAdapter.TelOnClickListener;
import com.cgzz.job.application.GlobalVariables;

/***
 * ��ǰ����������
 * 
 */
public class CollectionAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public CollectionAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.fragment_collection_item,
					null);
			holder = new ViewHolder();
			holder.iv_collection_tel = (ImageView) convertView
					.findViewById(R.id.iv_collection_tel);
			holder.iv_collection_picture = (ImageView) convertView
					.findViewById(R.id.iv_collection_picture);

			holder.tv_collection_peonumber = (TextView) convertView
					.findViewById(R.id.tv_collection_peonumber);
			holder.iv_collection_qiangdan = (TextView) convertView
					.findViewById(R.id.iv_collection_qiangdan);
			holder.iv_collection_hezuo = (TextView) convertView
					.findViewById(R.id.iv_collection_hezuo);
			holder.iv_collection_shouru = (TextView) convertView
					.findViewById(R.id.iv_collection_shouru);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);

		holder.tv_collection_peonumber.setText(map.get("name"));

		holder.iv_collection_qiangdan.setText(map.get("grab_count"));

		holder.iv_collection_hezuo.setText(map.get("cooperate_count"));
		holder.iv_collection_shouru.setText(map.get("income"));

		String image = map.get("front_photos");
		try {
			ImageListener listener = ImageLoader.getImageListener(
					holder.iv_collection_picture, R.drawable.image_moren,
					R.drawable.image_moren);
			mImageLoader.get(image, listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
	

		holder.iv_collection_tel.setOnClickListener(new TelOnClickListener(
				position));

		return convertView;
	}

	class ViewHolder {
		ImageView iv_collection_picture, iv_collection_tel;

		TextView tv_current_name, tv_collection_peonumber,
				iv_collection_qiangdan, iv_collection_hezuo,
				iv_collection_shouru;

	}

	/***
	 * ��绰
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
