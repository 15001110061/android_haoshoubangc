package com.cgzz.job.activity;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.BaseActivityCloseListener;
import com.cgzz.job.R;
import com.cgzz.job.BaseActivity.BitmapCache;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;

/**
 * 订单详情
 */
public class OrdersFeedbackActivity extends BaseActivity implements OnClickListener {

	private String orderid = "", phone = "", type = "", orderdetailid = "", voiceurl = "", message = "", seconds = "";
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.detail_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserDetail(data);
					voiceurl = bundle.getString("voiceurl");
					message = bundle.getString("message");
					// seconds = bundle.getString("seconds");
					Assignment(bundle);

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserDetail(data);
					ToastUtil.makeShortText(OrdersFeedbackActivity.this, bundle.get("msg").toString());
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
		setContentView(R.layout.activity_ordersfeedback);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_QIANGDAN, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");// 订单id
		orderdetailid = intent.getStringExtra("orderdetailid");

		type = intent.getStringExtra("type");
		if ("0".equals(type)) {
			setTitle("我的订单", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		} else {
			setTitle("我的订单", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "取消订单");
		}

		initView();
		init();

		detail(UrlConfig.detail_Http, application.getToken(), application.getUserId(), orderid, true);

	}

	private TextView tv_ordersfeedback_title, tv_ordersfeedback_time, tv_ordersfeedback_site, tv_ordersfeedback_2,
			tv_ordersfeedback_4, tv_ordersfeedback_call, tv_ordersfeedback_view_route, tv_ordersfeedback_training;
	ImageView iv_ordersfeedback_picture;
	LinearLayout llLeft, llright;
	RelativeLayout rl_home_item_b;
	TextView tv_ordersfeedback_titlee;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		rl_home_item_b = (RelativeLayout) findViewById(R.id.rl_home_item_b);
		iv_ordersfeedback_picture = (ImageView) findViewById(R.id.iv_ordersfeedback_picture);
		tv_ordersfeedback_title = (TextView) findViewById(R.id.tv_ordersfeedback_title);
		tv_ordersfeedback_time = (TextView) findViewById(R.id.tv_ordersfeedback_time);
		tv_ordersfeedback_site = (TextView) findViewById(R.id.tv_ordersfeedback_site);
		tv_ordersfeedback_2 = (TextView) findViewById(R.id.tv_ordersfeedback_2);
		tv_ordersfeedback_4 = (TextView) findViewById(R.id.tv_ordersfeedback_4);
		tv_ordersfeedback_call = (TextView) findViewById(R.id.tv_ordersfeedback_call);
		tv_ordersfeedback_view_route = (TextView) findViewById(R.id.tv_ordersfeedback_view_route);
		tv_ordersfeedback_training = (TextView) findViewById(R.id.tv_ordersfeedback_training);
	}

	private void init() {
		tv_ordersfeedback_view_route.setOnClickListener(this);
		tv_ordersfeedback_training.setOnClickListener(this);
		tv_ordersfeedback_call.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		rl_home_item_b.setOnClickListener(this);
	}

	String latitude = "", longitude = "", name = "", address = "", star = "";

	// public ImageLoader mImageLoader;
	private void Assignment(Bundle bundle) {

		// mImageLoader = new ImageLoader(
		// GlobalVariables.getRequestQueue(this), new BitmapCache());
		// 头像
		if (!Utils.isEmpty(bundle.getString("front_photos"))) {
			ImageListener listener = ImageLoader.getImageListener(iv_ordersfeedback_picture,
					R.drawable.image_jiudianmoren, R.drawable.image_jiudianmoren);

			mImageLoader.get(bundle.getString("front_photos"), listener);
		}

		tv_ordersfeedback_title.setText(bundle.getString("name"));

		tv_ordersfeedback_time.setText(bundle.getString("dutydate"));

		tv_ordersfeedback_site.setText(bundle.getString("address"));

		tv_ordersfeedback_4.setText(bundle.getString("made"));
		if (!Utils.isEmpty(bundle.getString("rooms"))) {
			tv_ordersfeedback_2.setText(bundle.getString("rooms"));
		} else {
			tv_ordersfeedback_2.setText("0");
		}

		// if ("0".equals(bundle.getString("standard_price"))) {
		// tv_ordersfeedback_2.setText(bundle.getString("rooms"));
		// } else {
		// tv_ordersfeedback_2.setText(bundle.getString("standard_price"));
		// }
		latitude = bundle.getString("latitude").toString();
		longitude = bundle.getString("longitude").toString();
		phone = bundle.get("phone").toString();

		name = bundle.get("name").toString();
		address = bundle.get("address").toString();
		star = bundle.get("star").toString();
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		// case R.id.tv_ordersfeedback_voice:// 语音播报
		// TTSController.getInstance(OrdersFeedbackActivity.this).playText(
		// tv_ordersfeedback_title.getText() + "于"
		// + tv_ordersfeedback_time.getText() + "到"
		// + tv_ordersfeedback_site.getText() + "。您预计可挣"
		// + tv_ordersfeedback_money.getText() + "元");
		// break;
		case R.id.tv_ordersfeedback_cancel_order:// 取消订单
			break;

		case R.id.tv_ordersfeedback_view_route:// 查看路线
			String url = "http://m.amap.com/?from=" + application.getLatitude() + "," + application.getLongitude() + "("
					+ "当前" + ")&to=" + latitude + "," + longitude + "(" + name + ")&type=1&opt=1";

			intent = new Intent(OrdersFeedbackActivity.this, WebPathActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "查看路线");

			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			intent.putExtra("name", name);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			startActivity(intent);
			break;
		case R.id.tv_ordersfeedback_training:// 查看培训与留言
			
			if (!"".equals(voiceurl) || !"".equals(message)) {
				intent = new Intent(OrdersFeedbackActivity.this, TrainingAndMessageActivity.class);
				intent.putExtra("voiceurl", voiceurl);
				intent.putExtra("message", message);
				// intent.putExtra("seconds", seconds);

				startActivity(intent);
			} else {
				ToastUtil.makeShortText(OrdersFeedbackActivity.this, "暂无培训留言");
			}

			break;
		case R.id.ll_title_left:// 返回
			onBackPressed();

			break;
		case R.id.rl_home_item_b:// 地图显示位置
			intent = new Intent(OrdersFeedbackActivity.this, MarkerActivity.class);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.putExtra("hotelratingbar", star);
			intent.putExtra("hoteltitle", name);
			intent.putExtra("hotelsnippet", address);
			startActivity(intent);
			break;

		case R.id.tv_ordersfeedback_call:// 打电话
			popTheirProfile(phone);

			break;

		case R.id.ll_title_right:// 取消订单
			intent = new Intent(OrdersFeedbackActivity.this, CancelReasonActivity.class);

			intent.putExtra("orderdetailid", orderdetailid);

			// startActivity(intent);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}

	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String isquxiao = data.getStringExtra("isquxiao");
			// 根据上面发送过去的请求吗来区别
			switch (requestCode) {
			case 1:
				if ("y".equals(isquxiao))
					finish();
				break;
			default:
				break;
			}
		}

	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile(final String tel) {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("是否拨打酒店电话?");
		up.setText("是");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("否");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				Utils.call(tel, OrdersFeedbackActivity.this);

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}

	private void detail(String url, String token, String userid, String orderid, boolean loadedtype) {
		showWaitDialog();

		url = url + "?apptype=2&token=" + token + "&userid=" + userid + "&orderid=" + orderid;

		HashMap map = new HashMap<String, String>();
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, OrdersFeedbackActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(OrdersFeedbackActivity.this), HttpStaticApi.detail_Http, null,
				loadedtype);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if ("0".equals(type)) {
			finish();
		} else {
			refreshOrders();
			application.popClosePath(true, UrlConfig.PATH_KEY_QIANGDAN);
			Intent intent = new Intent(OrdersFeedbackActivity.this, TabMainActivity.class);
			intent.putExtra("cityname", application.getCityName());
			intent.putExtra("cityid", application.getCityCode());
			intent.putExtra("type", "2");
			startActivity(intent);
		}
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
