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
import com.cgzz.job.adapter.NewsAdapter.BitmapCache;
import com.cgzz.job.adapter.NewsAdapter.ViewHolder;
import com.cgzz.job.application.GlobalVariables;

/***
 * 新闻适配器
 * 
 */
public class TrainingAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;

	public TrainingAdapter(Context contexts) {
		this.context = contexts;
		this.inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = getInviteView(position, convertView);
		return convertView;
	}

	private View getInviteView(final int position, View convertView) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_consulting_item, null);
			holder = new ViewHolder();
			holder.tv_new_title = (TextView) convertView.findViewById(R.id.tv_new_title);
			holder.tv_new_message = (TextView) convertView.findViewById(R.id.tv_new_message);
			holder.tv_new_picture = (ImageView) convertView.findViewById(R.id.tv_new_picture);

			holder.tv_new_checking = (TextView) convertView.findViewById(R.id.tv_new_checking);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = data.get(position);

		holder.tv_new_title.setText(map.get("title"));
		holder.tv_new_message.setText(map.get("remark"));

		if ("0".equals(map.get("views"))) {
			holder.tv_new_checking.setVisibility(View.GONE);
		} else {
			holder.tv_new_checking.setText("查看" + map.get("views") + "人");
		}

		String image = map.get("imgurl");
		ImageListener listener = ImageLoader.getImageListener(holder.tv_new_picture, R.drawable.icon_xinwen,
				R.drawable.icon_xinwen);
		try {
			 mImageLoader.get(image, listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return convertView;
	}

	class ViewHolder {
		TextView tv_new_title, tv_new_message, tv_new_checking;
		ImageView tv_new_picture;
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
