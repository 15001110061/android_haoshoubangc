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
 * 抢单页面
 * 
 */
public class SingleActivity extends BaseActivity implements OnClickListener {

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
					ToastUtil.makeShortText(SingleActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;
			case HttpStaticApi.grab_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserGrab(data);
					ToastUtil.makeShortText(SingleActivity.this, bundle.get("msg").toString());

					Intent intent = new Intent(SingleActivity.this, OrdersFeedbackActivity.class);
					intent.putExtra("orderid", order_id);
					intent.putExtra("orderdetailid", bundle.getString("orderdetailid"));
					intent.putExtra("type", "1");
					startActivity(intent);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserGrab(data);
					ToastUtil.makeShortText(SingleActivity.this, bundle.get("msg").toString());
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
		setContentView(R.layout.activity_singeact);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_QIANGDAN, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});
		initView();
		init();
	}

	TextView tv_home_item_time, tv_home_item_site, tv_home_item_openings2, tv_home_item_openings, tv_sing_baoming,
			tv_sing_jiedan, tv_home_titlename;
	RelativeLayout rl_home_item_c, rl_home_item_d, rl_home_item_e, rl_home_item_f;
	String order_id = "", type = "",orderDetailId="";
	TextView iv_home_item_c1, iv_home_item_d1, iv_home_item_e1, tv_home_item_time2, tv_home_is_changqi;

	private void initView() {

		final Intent i = getIntent();

		// Bundle bundle = i.getBundleExtra("bundle");
		order_id = i.getStringExtra("orderid");
		type= i.getStringExtra("type");
		orderDetailId= i.getStringExtra("orderDetailId");
		tv_home_item_time = (TextView) findViewById(R.id.tv_home_item_time);//
		tv_home_item_site = (TextView) findViewById(R.id.tv_home_item_site);//
		tv_home_item_openings2 = (TextView) findViewById(R.id.tv_home_item_openings2);//
		tv_home_item_openings = (TextView) findViewById(R.id.tv_home_item_openings);//
		rl_home_item_c = (RelativeLayout) findViewById(R.id.rl_home_item_c);//
		rl_home_item_d = (RelativeLayout) findViewById(R.id.rl_home_item_d);//
		rl_home_item_e = (RelativeLayout) findViewById(R.id.rl_home_item_e);//
		rl_home_item_f = (RelativeLayout) findViewById(R.id.rl_home_item_f);//
		tv_sing_baoming = (TextView) findViewById(R.id.tv_sing_baoming);//
		tv_sing_jiedan = (TextView) findViewById(R.id.tv_sing_jiedan);//
		tv_home_titlename = (TextView) findViewById(R.id.tv_home_titlename);//
		tv_home_item_time2 = (TextView) findViewById(R.id.tv_home_item_time2);//
		iv_home_item_c1 = (TextView) findViewById(R.id.iv_home_item_c1);
		iv_home_item_d1 = (TextView) findViewById(R.id.iv_home_item_d1);
		iv_home_item_e1 = (TextView) findViewById(R.id.iv_home_item_e1);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		tv_home_is_changqi = (TextView) findViewById(R.id.tv_home_is_changqi);
		if (!"".equals(order_id))
			getDetail(UrlConfig.detail_Http, application.getToken(), application.getUserId(), order_id, true);
	}

	private void init() {
		tv_sing_baoming.setOnClickListener(this);
		tv_sing_jiedan.setOnClickListener(this);
		iv_home_item_c1.setOnClickListener(this);
		iv_home_item_d1.setOnClickListener(this);
		iv_home_item_e1.setOnClickListener(this);
		rl_home_item_f.setOnClickListener(this);
		tv_home_item_site.setOnClickListener(this);
		llLeft.setOnClickListener(this);
	}

	Drawable btnDrawablec = null;
	Drawable btnDrawabled = null;
	Drawable btnDrawablee = null;
	Drawable btnDrawablef = null;
	String titleDrawablec = "";
	String titleDrawabled = "";
	String titleDrawablee = "";
	String titleDrawablef = "";
	//
	String latitude = "", longitude = "", hotelratingbar = "", hoteltitle = "", hotelsnippet = "", ordersystemid = "";

	private void setview(Bundle bundle) {
		tv_home_item_time.setText(bundle.getString("dutydate"));
		tv_home_item_site.setText(bundle.getString("address"));

		if (!"0".equals(bundle.getString("standard_price"))) {
			tv_home_item_openings2.setText(bundle.getString("standard_price"));
		} else {
			tv_home_item_openings2.setText(bundle.getString("suite_price"));
		}
		tv_home_titlename.setText(bundle.getString("name"));
		tv_home_item_openings.setText(bundle.getString("made"));

		latitude = bundle.getString("latitude");

		longitude = bundle.getString("longitude");
		hotelratingbar = bundle.getString("star");
		hoteltitle = bundle.getString("name");
		hotelsnippet = bundle.getString("address");


		ordersystemid = bundle.getString("ordersystemid");
		if (Utils.isEmpty(ordersystemid)) {// 短期
			tv_home_is_changqi.setVisibility(View.GONE);
			tv_home_item_time2.setText("预计清洁:" + bundle.getString("rooms") + "间");
		} else {// 长期
			if ("1".equals(type)) {
				tv_sing_baoming.setText("立即抢单");
				tv_sing_jiedan.setVisibility(View.INVISIBLE);
			} else if ("4".equals(type)) {
				tv_sing_baoming.setText("我知道了");
				tv_sing_jiedan.setVisibility(View.INVISIBLE);
			}
			tv_home_item_time2.setText("预计每天清洁:" + bundle.getString("rooms") + "间");
			tv_home_is_changqi.setVisibility(View.VISIBLE);
		}

		// if (bundle.getString("havebar").equals("1")) {
		// iv_home_item_c1.setVisibility(View.VISIBLE);
		// } else {
		//
		// Drawable btnDrawable = resources
		// .getDrawable(R.drawable.icon_dashang_sel);
		// iv_home_item_c1.setBackgroundDrawable(btnDrawable);
		//
		// }

		// if (bundle.getString("bounus").equals("1")) {
		// iv_home_item_d1.setVisibility(View.VISIBLE);
		// } else {
		// Drawable btnDrawable = resources
		// .getDrawable(R.drawable.icon_xianjin_sel);
		// iv_home_item_d1.setBackgroundDrawable(btnDrawable);
		// }

		// if (bundle.getString("havelaunch").equals("1")) {
		// iv_home_item_e1.setVisibility(View.VISIBLE);
		// } else {
		// Drawable btnDrawable = resources
		// .getDrawable(R.drawable.icon_daiwufan_sel);
		// iv_home_item_e1.setBackgroundDrawable(btnDrawable);
		// }
		Resources resources = this.getResources();
		if (!"0".equals(bundle.getString("bounus"))) {
			titleDrawablec = "对方提供额外打赏（每人" + bundle.getString("bounus") + "元）";
			btnDrawablec = resources.getDrawable(R.drawable.icon_dashang_sel);
			iv_home_item_c1.setBackgroundDrawable(btnDrawablec);
			// holder.rl_home_item_c.setVisibility(View.GONE);
		} else {
			titleDrawablec = "对方不提供额外打赏";
			btnDrawablec = resources.getDrawable(R.drawable.icon_dashang);
			iv_home_item_c1.setBackgroundDrawable(btnDrawablec);
		}

		if ("1".equals(bundle.getString("iscash"))) {
			titleDrawabled = "对方现付结算，当天就可以拿到工资";
			btnDrawabled = resources.getDrawable(R.drawable.icon_xianjin_sel);
			iv_home_item_d1.setBackgroundDrawable(btnDrawabled);
			// holder.rl_home_item_d.setVisibility(View.GONE);
		} else {
			titleDrawabled = "对方不提供现付结算待遇";
			btnDrawabled = resources.getDrawable(R.drawable.icon_xianjin);
			iv_home_item_d1.setBackgroundDrawable(btnDrawabled);
		}

		if ("1".equals(bundle.getString("havelaunch"))) {
			titleDrawablee = "对方提供午餐待遇";
			btnDrawablee = resources.getDrawable(R.drawable.icon_daiwufan_sel);
			iv_home_item_e1.setBackgroundDrawable(btnDrawablee);
			// holder.rl_home_item_e.setVisibility(View.GONE);
		} else {
			titleDrawablee = "对方不提供午餐待遇";
			btnDrawablee = resources.getDrawable(R.drawable.icon_wucan);
			iv_home_item_e1.setBackgroundDrawable(btnDrawablee);
		}

		if (!"0".equals(bundle.getString("havebar"))) {
			rl_home_item_f.setVisibility(View.VISIBLE);
			titleDrawablef = "此订单的房间内有小吧（酒水柜），会增大清洁难度";
			btnDrawablef = resources.getDrawable(R.drawable.icon_youxiaoba_sel);
		} else {
			rl_home_item_f.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left://
			onBackPressed();
			break;
		case R.id.tv_home_item_site://
			intent = new Intent(SingleActivity.this, MarkerActivity.class);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.putExtra("hotelratingbar", hotelratingbar);
			intent.putExtra("hoteltitle", hoteltitle);
			intent.putExtra("hotelsnippet", hotelsnippet);
			startActivity(intent);
			break;

		case R.id.tv_sing_baoming:// 我要接单

			if (Utils.isEmpty(ordersystemid)) {// 短期
				getGrab(UrlConfig.grab_Http, application.getToken(), application.getUserId(), order_id, true);
			} else {// 长期
				if ("1".equals(type)) {// 立即抢单
					getGrab(UrlConfig.grab_Http, application.getToken(), application.getUserId(), order_id, true);
				} else if ("4".equals(type)) {// 我知道了
					ConfirmOrder(UrlConfig.confirmOrder_Http, application.getToken(), application.getUserId(), orderDetailId,
							true);
				}
			}

			break;
		case R.id.tv_sing_jiedan:// 忽略
			onBackPressed();
			break;

		case R.id.iv_home_item_c1://
			CustomDialog.alertDialog(SingleActivity.this, true, false, true, btnDrawablec, titleDrawablec,
					new CustomDialog.PopUpDialogListener() {

						@Override
						public void doPositiveClick(Boolean isOk) {
							if (isOk) {// 确定

							}

						}
					});

			break;

		case R.id.iv_home_item_d1://

			CustomDialog.alertDialog(SingleActivity.this, true, false, true, btnDrawabled, titleDrawabled,
					new CustomDialog.PopUpDialogListener() {

						@Override
						public void doPositiveClick(Boolean isOk) {
							if (isOk) {// 确定

							}

						}
					});

			break;

		case R.id.iv_home_item_e1://

			CustomDialog.alertDialog(SingleActivity.this, true, false, true, btnDrawablee, titleDrawablee,
					new CustomDialog.PopUpDialogListener() {

						@Override
						public void doPositiveClick(Boolean isOk) {
							if (isOk) {// 确定

							}

						}
					});

			break;

		case R.id.rl_home_item_f://

			CustomDialog.alertDialog(SingleActivity.this, true, false, true, btnDrawablef, titleDrawablef,
					new CustomDialog.PopUpDialogListener() {

						@Override
						public void doPositiveClick(Boolean isOk) {
							if (isOk) {// 确定

							}

						}
					});

			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(SingleActivity.this, TabMainActivity.class);
		startActivity(intent);
		finish();
	}

	private void getDetail(String url, String token, String userid, String orderid, boolean loadedtype) {
		showWaitDialog();

		url = url + "?apptype=2&token=" + token + "&userid=" + userid + "&orderid=" + orderid;

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, SingleActivity.this, url, null, callbackData,
				GlobalVariables.getRequestQueue(SingleActivity.this), HttpStaticApi.detail_Http, null, loadedtype);

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
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SingleActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SingleActivity.this), HttpStaticApi.grab_Http, null, loadedtype);

	}

	/**
	 * 我知道了
	 */
	private void ConfirmOrder(String url, String token, String userid, String orderDetailId, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderDetailId", orderDetailId);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SingleActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SingleActivity.this), HttpStaticApi.grab_Http, null, loadedtype);

	}

}
