package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.ReviewsAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 工资条
 */
public class WagesActivity extends BaseActivity implements OnClickListener {
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private String orderid = "", hotelid = "";
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.wages_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					CurrentData.clear();
					bundle = ParserUtil.ParserWages(data);

					hotelid = bundle.getString("hotelid");

					if (bundle != null) {
						Assignment(bundle);
						if (bundle.getSerializable("list") != null)
							if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {
								CurrentData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
								adapter.refreshMYData(CurrentData);
							}
						if ("1".equals(bundle.getString("iscash"))) {
							CustomDialog.alertDialog2(WagesActivity.this, false, true, false, null,
									"对方选择了现金支付，请务必当面核实并结清工资", new CustomDialog.PopUpDialogListener() {

								@Override
								public void doPositiveClick(Boolean isOk) {
									// if (isOk) {// 确定
									// } else {
									//
									// }

								}
							});
						}

					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserWages(data);
					ToastUtil.makeShortText(WagesActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.evaluate_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(WagesActivity.this, bundle.get("msg").toString());
					onBackPressed();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(WagesActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wages);
		setTitle("工资条", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "完成");
		application = (GlobalVariables) getApplicationContext();
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");// 订单id

		initView();
		init();

		wages(UrlConfig.wagesB_Http, application.getToken(), application.getUserId(), orderid, true);

	}

	ImageView tv_wages_picture, tv_wages_picture2;
	LinearLayout llLeft, llright;
	TextView tv_wages_site, tv_wages_isxianjin, tv_wages_jine, tv_wages_dashang, tv_wages_name;
	private GridView grid;
	private ReviewsAdapter adapter;
	// private List<ReviewsItem> datas;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_wages_site = (TextView) findViewById(R.id.tv_wages_site);
		tv_wages_isxianjin = (TextView) findViewById(R.id.tv_wages_isxianjin);
		tv_wages_jine = (TextView) findViewById(R.id.tv_wages_jine);
		tv_wages_dashang = (TextView) findViewById(R.id.tv_wages_dashang);
		tv_wages_picture = (ImageView) findViewById(R.id.tv_wages_picture);
		tv_wages_picture2 = (ImageView) findViewById(R.id.tv_wages_picture2);
		tv_wages_name = (TextView) findViewById(R.id.tv_wages_name);
		grid = (GridView) findViewById(R.id.grid);
		adapter = new ReviewsAdapter(WagesActivity.this);
		grid.setAdapter(adapter);

	}

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		grid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				if ("1".equals(CurrentData.get(position).get("Select"))) {
					CurrentData.get(position).put("Select", "0");
				} else {
					CurrentData.get(position).put("Select", "1");
				}

				adapter.refreshMYData(CurrentData);
			}
		});

	}

	private void Assignment(Bundle bundle) {

		tv_wages_site.setText(bundle.getString("message"));

		if ("1".equals(bundle.getString("iscash"))) {
			tv_wages_isxianjin.setVisibility(View.VISIBLE);
			tv_wages_picture2.setVisibility(View.VISIBLE);
		} else {
			tv_wages_isxianjin.setVisibility(View.GONE);
			tv_wages_picture2.setVisibility(View.GONE);
		}

		tv_wages_jine.setText("￥" + bundle.getString("earn_money"));
		if ("0".equals(bundle.getString("bounus"))) {
			tv_wages_dashang.setVisibility(View.INVISIBLE);
		} else {
			tv_wages_dashang.setText("(含打赏" + bundle.getString("bounus") + "元)");
		}

		tv_wages_name.setText(bundle.getString("name"));

		// 头像
		if (!Utils.isEmpty(bundle.getString("front_photos"))) {
			try {
				ImageListener listener = ImageLoader.getImageListener(tv_wages_picture, R.drawable.image_jiudianmoren,
						R.drawable.image_jiudianmoren);
				mImageLoader.get(bundle.getString("front_photos"), listener);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.tv_ordersfeedback_cancel_order:// 取消订单
			break;

		case R.id.ll_title_left:// 返回
			onBackPressed();

			break;
		case R.id.ll_title_right:// 完成

			StringBuffer params = new StringBuffer();
			for (int i = 0; i < CurrentData.size(); i++) {
				if ("1".equals(CurrentData.get(i).get("Select"))) {
					params.append(CurrentData.get(i).get("id") + ";");
				}

			}

			char ch = ';';

			if (!"".equals(params.toString())) {
				evaluate(UrlConfig.evaluate_Http, application.getToken(), application.getUserId(), hotelid,
						trimFirstAndLastChar(params.toString(), ch), true);
			} else {
				onBackPressed();
			}

			break;

		default:
			break;
		}

	}

	public static String trimFirstAndLastChar(String source, char element) {
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do {
			int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
			int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element)
					: source.length();
			source = source.substring(beginIndex, endIndex);
			beginIndexFlag = (source.indexOf(element) == 0);
			endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
		} while (beginIndexFlag || endIndexFlag);
		return source;
	}

	private void wages(String url, String token, String userid, String orderid, boolean loadedtype) {

		showWaitDialog();

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "2");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, WagesActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(WagesActivity.this), HttpStaticApi.wages_Http, null, loadedtype);
	}

	private void evaluate(String url, String token, String userid, String hotelid, String options, boolean loadedtype) {

		showWaitDialog();

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "2");
		map.put("token", token);
		map.put("userid", userid);
		map.put("hotelid", hotelid);

		map.put("options", options);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, WagesActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(WagesActivity.this), HttpStaticApi.evaluate_Http, null, loadedtype);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
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
