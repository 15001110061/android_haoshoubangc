package com.cgzz.job.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;

/***
 * 当前订单适配器
 * 
 */
public class AssetsAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public AssetsAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.fragment_assets_item, null);
			holder = new ViewHolder();

			holder.iv_assets_biaoshi = (ImageView) convertView
					.findViewById(R.id.iv_assets_biaoshi);
			holder.iv_assets_zhuangtai = (TextView) convertView
					.findViewById(R.id.iv_assets_zhuangtai);
			holder.iv_assets_qian = (TextView) convertView
					.findViewById(R.id.iv_assets_qian);
			holder.iv_assets_miaoshu = (TextView) convertView
					.findViewById(R.id.iv_assets_miaoshu);
			holder.iv_assets_time = (TextView) convertView
					.findViewById(R.id.iv_assets_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);
		holder.iv_assets_qian.setText("￥"+map.get("income"));
		holder.iv_assets_miaoshu.setText(map.get("comment"));
		holder.iv_assets_time.setText(map.get("paydate"));
		Resources resources = context.getResources();
		if ("1".equals(map.get("paytype"))) {// （1：系统奖励，2：工作收入，3：打赏）

			Drawable btnDrawable = resources
					.getDrawable(R.drawable.icon_xitongjiangli);
			holder.iv_assets_biaoshi.setBackgroundDrawable(btnDrawable);

			holder.iv_assets_qian.setTextColor(context.getResources().getColor(
					R.color.common_09B473));

			holder.iv_assets_zhuangtai.setText("系统奖励");

		} else if ("2".equals(map.get("paytype"))) {

			Drawable btnDrawable = resources
					.getDrawable(R.drawable.icon_gongzuoshouru);
			holder.iv_assets_biaoshi.setBackgroundDrawable(btnDrawable);
			holder.iv_assets_qian.setTextColor(context.getResources().getColor(
					R.color.common_FF971E));

			holder.iv_assets_zhuangtai.setText("工作收入");
		} else if ("3".equals(map.get("paytype"))) {

			Drawable btnDrawable = resources
					.getDrawable(R.drawable.icon_dashangs);
			holder.iv_assets_biaoshi.setBackgroundDrawable(btnDrawable);
			holder.iv_assets_qian.setTextColor(context.getResources().getColor(
					R.color.common_F37491));
			holder.iv_assets_zhuangtai.setText("打赏");
		} else {
			// holder.iv_assets_biaoshi.setVisibility(View.GONE);

		}

		// "income": 30,//收入
		// "paytype": 1,//收入类型（1：系统奖励，2：工作收入，3：打赏）
		// "paydate": "2015-08-12",
		// "comment": "满100单送现金活动"
		// if (map.get("year").equals("")) {
		// holder.rl_home_one.setVisibility(View.GONE);
		// } else {
		// holder.rl_home_one.setVisibility(View.VISIBLE);
		// }

		return convertView;
	}

	class ViewHolder {
		TextView iv_assets_zhuangtai, iv_assets_qian, iv_assets_miaoshu,
				iv_assets_time;
		ImageView iv_assets_biaoshi;
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
