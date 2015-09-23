package com.cgzz.job.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.BaseActivityCloseListener;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomDialog;
import com.cgzz.job.view.ScaleGallery;

/***
 * 抢红包页面
 * 
 */
public class BonusActivity extends BaseActivity implements OnClickListener {

	private ScaleGallery gallery;
	private LinearLayout llright, llLeft;

	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.detailRED_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserdetailRED(data);
					setview(bundle);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserDetail(data);
					ToastUtil.makeShortText(BonusActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;
			case HttpStaticApi.grabRED_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					String url = urls + "&userid=" + application.getUserId();
					Intent intent = new Intent(BonusActivity.this, WebBrowserActivity.class);
					intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "红包");
					intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
					intent.putExtra("type", "0");
					startActivity(intent);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
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
		setContentView(R.layout.activity_bonus);
		application = (GlobalVariables) getApplicationContext();
		initView();
		init();
	}

	// TextView tv_home_item_time, tv_home_item_site, tv_home_item_openings2,
	// tv_home_item_openings, tv_sing_baoming,
	// tv_sing_jiedan, tv_home_titlename;
	// RelativeLayout rl_home_item_c, rl_home_item_d, rl_home_item_e,
	// rl_home_item_f;
	String id = "", redid = "";
	TextView tv_bonus_titless;
	RelativeLayout rl_bonus_qiang;

	private void initView() {

		final Intent i = getIntent();
		redid = i.getStringExtra("redid");
		if (!"".equals(redid))
			getdetail(UrlConfig.detail_Http, application.getToken(), application.getUserId(), redid, true);
	}

	ImageView tv_bonus_logo;
	TextView tv_bonus_title;

	private void init() {
		rl_bonus_qiang = (RelativeLayout) findViewById(R.id.rl_bonus_qiang);//
		tv_bonus_titless = (TextView) findViewById(R.id.tv_bonus_titless);//
		tv_bonus_title = (TextView) findViewById(R.id.tv_bonus_title);//
		tv_bonus_logo = (ImageView) findViewById(R.id.tv_bonus_logo);//

		rl_bonus_qiang.setOnClickListener(this);
	}

	String urls = "";

	private void setview(Bundle bundle) {
		try {
			ImageListener listener = ImageLoader.getImageListener(tv_bonus_logo, R.drawable.icon_touxiangmoren,
					R.drawable.icon_touxiangmoren);
			mImageLoader.get(bundle.getString("img"), listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
		tv_bonus_title.setText(bundle.getString("name"));
		tv_bonus_titless.setText(bundle.getString("message"));
		urls = bundle.getString("message");
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.rl_bonus_qiang:// 抢

			getgrab(UrlConfig.grabRED_Http, application.getToken(), application.getUserId(), redid, true);

			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// Intent intent = new Intent(BonusActivity.this,
		// TabMainActivity.class);
		// startActivity(intent);
		finish();
	}

	/**
	 * 用户抢单接口
	 */
	private void getdetail(String url, String token, String userid, String redid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("redid", redid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, BonusActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(BonusActivity.this), HttpStaticApi.detailRED_Http, null, loadedtype);

	}

	/**
	 */
	private void getgrab(String url, String token, String userid, String redid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("redid", redid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, BonusActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(BonusActivity.this), HttpStaticApi.grabRED_Http, null, loadedtype);

	}

}
