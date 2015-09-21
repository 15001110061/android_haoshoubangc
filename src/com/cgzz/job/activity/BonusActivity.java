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
			case HttpStaticApi.detail_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserDetail(data);
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
			case HttpStaticApi.grab_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserGrab(data);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserGrab(data);
					ToastUtil.makeShortText(BonusActivity.this, bundle.get("msg").toString());
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
	String id = "";
	TextView tv_bonus_titless;
	RelativeLayout rl_bonus_qiang;

	private void initView() {

		final Intent i = getIntent();

		id = i.getStringExtra("orderid");
		// if (!"".equals(id))
		// getDetail(UrlConfig.detail_Http, application.getToken(),
		// application.getUserId(), order_id, true);
	}

	private void init() {
		rl_bonus_qiang = (RelativeLayout) findViewById(R.id.rl_bonus_qiang);//
		tv_bonus_titless = (TextView) findViewById(R.id.tv_bonus_titless);//

		rl_bonus_qiang.setOnClickListener(this);
	}

	private void setview(Bundle bundle) {

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.rl_bonus_qiang:// 抢

		String	url = "http://www.haoshoubang.com/bangke/html/privacy.html";
			intent = new Intent(BonusActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "红包");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			intent.putExtra("type", "0");
			startActivity(intent);
			finish();
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
	private void getGrab(String url, String token, String userid, String orderid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, BonusActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(BonusActivity.this), HttpStaticApi.grab_Http, null, loadedtype);

	}

}
