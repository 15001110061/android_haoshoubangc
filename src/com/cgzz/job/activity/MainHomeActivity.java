package com.cgzz.job.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.MyGalleryAdapter;
import com.cgzz.job.adapter.MyGalleryAdapter.OnCancelOrderClickListener;
import com.cgzz.job.adapter.MyGalleryAdapter.OnPhoneClickListener;
import com.cgzz.job.adapter.MyGalleryAdapter.OnRouteClickListener;
import com.cgzz.job.adapter.MyGalleryAdapter.OnTelClickListener;
import com.cgzz.job.adapter.MyGalleryAdapter.OnTextClickListener;
import com.cgzz.job.adapter.MyGalleryAdapter.OnZFClickListener;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ImageTools;
import com.cgzz.job.utils.TTSController;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.BadgeView;
import com.cgzz.job.view.CustomDialog;
import com.cgzz.job.view.ScaleGallery;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizerListener;

public class MainHomeActivity extends BaseActivity
		implements OnClickListener, OnTelClickListener, OnCancelOrderClickListener, OnRouteClickListener,
		OnTextClickListener, OnPhoneClickListener, SynthesizerListener, OnZFClickListener {

	private ScaleGallery gallery;
	private LinearLayout llright, llLeft;
	private TextView tv_title_left, tv_home_item_sign, tv_hemo_chongshi, tv_hemo_peixun, tv_hemo_denglu,
			tv_hemo_denglu1, tv_home_tz_title, tv_home_tz_cha, tv_title;

	private int logoFillOrders = 1;// 页码标识
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private String orderid = "";
	private MyGalleryAdapter myGalleryAdapter;
	private RelativeLayout rl_home_1, rl_home_2, rl_home_3, rl_home_4, rl_home_5, rl_home_6, rl_home_7;
	private int mark = 0;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.ordercList_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserOrdercList(data);

					if (loadedtype) {
						CurrentData.clear();
						logoFillOrders = 2;
					} else {
						logoFillOrders++;
					}
					if ("1".equals(bundle.getString("status"))) {// 审核通过
						rl_home_5.setVisibility(View.GONE);
						rl_home_6.setVisibility(View.GONE);
						if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

							rl_home_1.setVisibility(View.VISIBLE);
							rl_home_2.setVisibility(View.GONE);
							rl_home_3.setVisibility(View.GONE);

							CurrentData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
							tv_home_item_sign.setVisibility(View.VISIBLE);
						} else {
							if (loadedtype) {
								rl_home_1.setVisibility(View.GONE);
								rl_home_2.setVisibility(View.GONE);
								rl_home_3.setVisibility(View.VISIBLE);

							} else {
							}

						}

						myGalleryAdapter.refreshMYData(CurrentData);
					} else if ("0".equals(bundle.getString("status"))) {// 审核中
						rl_home_1.setVisibility(View.GONE);
						rl_home_2.setVisibility(View.GONE);
						rl_home_3.setVisibility(View.GONE);
						rl_home_5.setVisibility(View.GONE);
						rl_home_6.setVisibility(View.VISIBLE);
					} else if ("-1".equals(bundle.getString("status"))) {// 审核失败
						rl_home_1.setVisibility(View.GONE);
						rl_home_2.setVisibility(View.GONE);
						rl_home_3.setVisibility(View.GONE);
						rl_home_5.setVisibility(View.VISIBLE);
						rl_home_6.setVisibility(View.GONE);
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					rl_home_1.setVisibility(View.GONE);
					rl_home_2.setVisibility(View.VISIBLE);
					rl_home_3.setVisibility(View.GONE);
					rl_home_5.setVisibility(View.GONE);
					rl_home_6.setVisibility(View.GONE);
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserOrdercList(data);
					ToastUtil.makeShortText(MainHomeActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;
			case HttpStaticApi.grab_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserGrab(data);
					ToastUtil.makeShortText(MainHomeActivity.this, bundle.get("msg").toString());

					Intent intent = new Intent(MainHomeActivity.this, OrdersFeedbackActivity.class);
					intent.putExtra("orderid", orderid);
					intent.putExtra("orderdetailid", bundle.getString("orderdetailid"));
					intent.putExtra("type", "1");
					startActivity(intent);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserGrab(data);
					ToastUtil.makeShortText(MainHomeActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}

				break;

			case HttpStaticApi.status_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserStatus(data);
					if (!Utils.isEmpty(bundle.getString("status"))) {
						// 0:无红包，1：有未抢红包，2：有已抢红包
						if ("0".equals(bundle.getString("status"))) {
							tv_home_bonus.clearAnimation();
							tv_home_bonus.setVisibility(View.GONE);
						} else if ("1".equals(bundle.getString("status"))) {
							tv_home_bonus.setVisibility(View.VISIBLE);
							tv_home_bonus.startAnimation(shakeAnim);
						} else if ("2".equals(bundle.getString("status"))) {
							tv_home_bonus.setVisibility(View.VISIBLE);
							shakeAnim.cancel();

						}

					} else {
						tv_home_bonus.setVisibility(View.GONE);
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					tv_home_bonus.setVisibility(View.GONE);
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					tv_home_bonus.setVisibility(View.GONE);
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
		setContentView(R.layout.activity_home);
		initView();
		releaseBroadcastReceiver();
	}

	TextView tv_home_bonus;
	Animation shakeAnim;

	private void initView() {
		//
		tv_home_bonus = (TextView) findViewById(R.id.tv_home_bonus);
		tv_home_bonus.bringToFront();

		shakeAnim = AnimationUtils.loadAnimation(MainHomeActivity.this, R.anim.shake_x);
		tv_home_bonus.startAnimation(shakeAnim);
		tv_home_bonus.setVisibility(View.GONE);
		//
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_home_1 = (RelativeLayout) findViewById(R.id.rl_home_1);
		rl_home_2 = (RelativeLayout) findViewById(R.id.rl_home_2);
		rl_home_3 = (RelativeLayout) findViewById(R.id.rl_home_3);
		rl_home_4 = (RelativeLayout) findViewById(R.id.rl_home_4);

		// 重新审核
		rl_home_5 = (RelativeLayout) findViewById(R.id.rl_home_5);
		// 审核中
		rl_home_6 = (RelativeLayout) findViewById(R.id.rl_home_6);
		// 上方通知的推送
		rl_home_7 = (RelativeLayout) findViewById(R.id.rl_home_7);
		tv_home_tz_title = (TextView) findViewById(R.id.tv_home_tz_title);
		tv_home_tz_cha = (TextView) findViewById(R.id.tv_home_tz_cha);
		tv_hemo_denglu1 = (TextView) findViewById(R.id.tv_hemo_denglu1);
		tv_hemo_peixun = (TextView) findViewById(R.id.tv_hemo_peixun);
		tv_hemo_chongshi = (TextView) findViewById(R.id.tv_hemo_chongshi);
		tv_hemo_denglu = (TextView) findViewById(R.id.tv_hemo_denglu);
		tv_hemo_chongshi.setOnClickListener(this);
		tv_hemo_peixun.setOnClickListener(this);
		tv_hemo_denglu.setOnClickListener(this);
		tv_hemo_denglu1.setOnClickListener(this);
		tv_home_tz_cha.setOnClickListener(this);
		tv_home_bonus.setOnClickListener(this);
		if (!application.isAnnouncement) {
			tv_title.setOnClickListener(this);
			tv_title.setText("内部使用版本");
		}

		gallery = (ScaleGallery) findViewById(R.id.gallery);
		gallery.setSpacing(-1);

		myGalleryAdapter = new MyGalleryAdapter(this, gallery);
		myGalleryAdapter.setOnCancelOrderClickListener(this, 0);
		myGalleryAdapter.setOnTelClickListener(this, 0);
		myGalleryAdapter.setOnRouteClickListener(this, 0);
		myGalleryAdapter.setOnTextClickListener(this, 0);
		myGalleryAdapter.setonPhoneClickListener(this, 0);

		myGalleryAdapter.setOnZFClickListener(this, 0);
		gallery.setAdapter(myGalleryAdapter);
		gallery.setUnselectedAlpha(0.2f);
		gallery.setOnItemSelectedListener(selectedListener); //

		// mListView.setSelection(pos);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		tv_title_left = (TextView) findViewById(R.id.tv_title_left);
		tv_home_item_sign = (TextView) findViewById(R.id.tv_home_item_sign);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_home_item_sign.setOnClickListener(this);
		if (Utils.isEmpty(application.getUsercityName())) {

			tv_title_left.setText(application.getCityName());
		} else {
			tv_title_left.setText(application.getUsercityName());
		}

		if (!application.isLogon()) {
			rl_home_1.setVisibility(View.GONE);
			rl_home_2.setVisibility(View.GONE);
			rl_home_3.setVisibility(View.GONE);
			rl_home_4.setVisibility(View.VISIBLE);
		} else {
			rl_home_1.setVisibility(View.VISIBLE);
			rl_home_2.setVisibility(View.GONE);
			rl_home_3.setVisibility(View.GONE);
			rl_home_4.setVisibility(View.GONE);
			logoFillOrders = 1;
			getOrdercList(UrlConfig.ordercList_Http, application.getToken(), application.getUserId(), logoFillOrders,
					true);

			if (!Utils.isEmpty(application.getHomemessage())) {
				rl_home_7.setVisibility(View.VISIBLE);
				tv_home_tz_title.setText(application.getHomemessage());
			} else {
				rl_home_7.setVisibility(View.GONE);
			}
		}
		//
		// badge1 = new BadgeView(this, llright);
		// badge1.setText("");
		// badge1.setTextSize(1);
		// badge1.setTextColor(this.getResources().getColor(R.color.common_dc2f2c));
		// badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		// badge1.setBackgroundResource(R.drawable.redpoint_icon);
		// if (application.isLogon()) {
		// if (application.isReddothome()) {
		// badge1.show(true);
		// } else {
		// badge1.hide();
		// }
		// }
	}

	@Override
	@SuppressLint("NewApi")
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (application.isLogon()) {
			getStatus(UrlConfig.status_Http, application.getToken(), application.getUserId(), true);
		}
	}

	// 选中图片的监听事件
	AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			orderid = CurrentData.get(position).get("orderid");
			if (CurrentData != null)
				if (CurrentData.size() > 1 && CurrentData.size() == (position + 1)) {
					getOrdercList(UrlConfig.ordercList_Http, application.getToken(), application.getUserId(),
							logoFillOrders, false);
				}

			if (TTSController.mSpeechSynthesizer != null && TTSController.mSpeechSynthesizer.isSpeaking()) {
				TTSController.destroy();
			}
			if (vlogo != null) {
				Resources resources = getResources();
				Drawable btnDrawable = resources.getDrawable(R.drawable.icon_yuyinbobao);
				vlogo.setBackgroundDrawable(btnDrawable);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.tv_home_item_sign:// 抢先报名
			getGrab(UrlConfig.grab_Http, application.getToken(), application.getUserId(), orderid, true);
			break;
		case R.id.ll_title_left://
			// popupwindow.showAsDropDown(arg0);

			intent = new Intent(MainHomeActivity.this, SelectCityActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_hemo_chongshi:

			if (android.os.Build.VERSION.SDK_INT > 10) {
				intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			} else {
				intent = new Intent();
				ComponentName component = new ComponentName("com.android.settings",
						"com.android.settings.WirelessSettings");
				intent.setComponent(component);
				intent.setAction("android.intent.action.VIEW");
			}
			startActivity(intent);
			break;
		case R.id.ll_title_right:

			if (application.isLogon()) {
				logoFillOrders = 1;
				getOrdercList(UrlConfig.ordercList_Http, application.getToken(), application.getUserId(),
						logoFillOrders, true);
			} else {
				ToastUtil.makeShortText(MainHomeActivity.this, "请先进行登录");
			}

			break;

		case R.id.tv_hemo_peixun:
			try {
				try {
					TabMainActivity.tabHost.setCurrentTabByTag("3");
					TabMainActivity.setButton(TabMainActivity.buttom_3);
				} catch (Exception e) {
					// TODO: handle exception
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			break;

		case R.id.tv_hemo_denglu:
			intent = new Intent(MainHomeActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();

			break;

		case R.id.tv_hemo_denglu1:// 重新提交审核
			intent = new Intent(MainHomeActivity.this, SignedActivityFore.class);
			startActivity(intent);
			break;
		case R.id.tv_home_tz_cha://
			rl_home_7.setVisibility(View.GONE);
			application.setHomemessage("");
			break;
		case R.id.tv_title://
			mark++;
			if (mark == 4) {
				popTheirProfile();
				mark = 0;
			}

			break;

		case R.id.tv_home_bonus://

			intent = new Intent(MainHomeActivity.this, BonusListActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	private void getStatus(String url, String token, String userid, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.status_Http, null, loadedtype);

	}

	/**
	 * 订单列表
	 */
	private void getOrdercList(String url, String token, String userid, int page, boolean loadedtype) {
		redDothome();
		if (loadedtype)
			showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.ordercList_Http, null,
				loadedtype);

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

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.grab_Http, null, loadedtype);

	}

	@Override
	public void onCancelOrderClick(int position, View v, int logo) {
		Intent intent = new Intent(MainHomeActivity.this, MarkerActivity.class);
		intent.putExtra("latitude", CurrentData.get(position).get("latitude"));
		intent.putExtra("longitude", CurrentData.get(position).get("longitude"));
		intent.putExtra("hotelratingbar", CurrentData.get(position).get("star"));
		intent.putExtra("hoteltitle", CurrentData.get(position).get("name"));
		intent.putExtra("hotelsnippet", CurrentData.get(position).get("address"));
		startActivity(intent);

	}

	View vlogo;

	@Override
	public void onTelClick(int position, View v, int logo) {
		if (TTSController.mSpeechSynthesizer != null)
			if (TTSController.mSpeechSynthesizer.isSpeaking()) {
				TTSController.destroy();
				// stop(v);
				Resources resources = getResources();
				Drawable btnDrawable = resources.getDrawable(R.drawable.icon_yuyinbobao);
				vlogo.setBackgroundDrawable(btnDrawable);
			} else {
				// stop(null);
				// Animation(v);
				// start();

				Resources resources = getResources();
				Drawable btnDrawable = resources.getDrawable(R.drawable.img0);
				v.setBackgroundDrawable(btnDrawable);
				vlogo = v;
				playText(CurrentData.get(position).get("dutydate") + "到" + CurrentData.get(position).get("name")
						+ ",预计可挣" + CurrentData.get(position).get("made") + "元");

			}
		// TTSController.getInstance(MainHomeActivity.this).playText(
		// CurrentData.get(position).get("dutydate") + "到"
		// + CurrentData.get(position).get("name") + ",预计可挣"
		// + CurrentData.get(position).get("made") + "元");

	}

	static AnimationDrawable frameAnim;

	private void Animation(View view) {
		frameAnim = new AnimationDrawable();
		// 为AnimationDrawable添加动画帧
		frameAnim.addFrame(getResources().getDrawable(R.drawable.img0), 150);
		frameAnim.addFrame(getResources().getDrawable(R.drawable.img1), 150);
		frameAnim.addFrame(getResources().getDrawable(R.drawable.img2), 150);
		// 设置为循环播放
		frameAnim.setOneShot(false);

		// 设置ImageView的背景为AnimationDrawable
		view.setBackgroundDrawable(frameAnim);
	}

	public void playText(String playText) {
		if (null == TTSController.mSpeechSynthesizer) {
			// 创建合成对象.
			TTSController.mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(MainHomeActivity.this);
			initSpeechSynthesizer();
		}
		// 进行语音合成.
		TTSController.mSpeechSynthesizer.startSpeaking(playText, this);

	}

	private void initSpeechSynthesizer() {
		// 设置发音人
		TTSController.mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		// 设置语速
		TTSController.mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "tts_speed");
		// 设置音量
		TTSController.mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "tts_volume");
		// 设置语调
		TTSController.mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "tts_pitch");

	}

	/**
	 * 开始播放
	 */
	public static void start() {
		if (frameAnim != null && !frameAnim.isRunning()) {
			frameAnim.start();
		}
	}

	/**
	 * 停止播放
	 */
	public void stop(View view) {
		if (frameAnim != null && frameAnim.isRunning()) {
			frameAnim.stop();
		}
		if (view != null) {
			Resources resources = getResources();
			Drawable btnDrawable = resources.getDrawable(R.drawable.icon_yuyinbobao);
			view.setBackgroundDrawable(btnDrawable);
		} else if (vlogo != null) {
			Resources resources = getResources();
			Drawable btnDrawable = resources.getDrawable(R.drawable.icon_yuyinbobao);
			vlogo.setBackgroundDrawable(btnDrawable);
		}

	}

	Drawable btnDrawablec = null;

	Drawable btnDrawabled = null;
	Drawable btnDrawablee = null;
	Drawable btnDrawablef = null;

	String titleDrawablec = "";
	String titleDrawabled = "";
	String titleDrawablee = "";
	String titleDrawablef = "";

	/***
	 * 打赏
	 */
	@Override
	public void onRouteClick(int position, View v, int logo) {

		Resources resources = this.getResources();
		if (!"0".equals(CurrentData.get(position).get("bounus"))) {
			titleDrawablec = "对方提供额外打赏（每人" + CurrentData.get(position).get("bounus") + "元）";
			btnDrawablec = resources.getDrawable(R.drawable.icon_dashang_sel);

		} else {
			titleDrawablec = "对方不提供额外打赏";
			btnDrawablec = resources.getDrawable(R.drawable.icon_dashang);
		}

		CustomDialog.alertDialog(MainHomeActivity.this, true, false, true, btnDrawablec, titleDrawablec,
				new CustomDialog.PopUpDialogListener() {

					@Override
					public void doPositiveClick(Boolean isOk) {
						if (isOk) {// 确定

						}

					}
				});

	}

	/***
	 * 工资
	 */
	@Override
	public void onTextClick(int position, View v, int logo) {

		Resources resources = this.getResources();

		if ("0".equals(CurrentData.get(position).get("iscash"))) {
			titleDrawabled = "对方不提供现付结算待遇";
			btnDrawabled = resources.getDrawable(R.drawable.icon_xianjin);
		} else {

			titleDrawabled = "对方现付结算，当天就可以拿到工资";
			btnDrawabled = resources.getDrawable(R.drawable.icon_xianjin_sel);
		}

		CustomDialog.alertDialog(MainHomeActivity.this, true, false, true, btnDrawabled, titleDrawabled,
				new CustomDialog.PopUpDialogListener() {

					@Override
					public void doPositiveClick(Boolean isOk) {
						if (isOk) {// 确定

						}

					}
				});

	}

	/***
	 * 午餐
	 */
	@Override
	public void onPhoneClick(int position, View v, int logo) {

		Resources resources = this.getResources();

		if ("0".equals(CurrentData.get(position).get("havelaunch"))) {

			titleDrawablee = "对方不提供午餐待遇";
			btnDrawablee = resources.getDrawable(R.drawable.icon_wucan);
		} else {
			titleDrawablee = "对方提供午餐待遇";
			btnDrawablee = resources.getDrawable(R.drawable.icon_daiwufan_sel);
		}

		CustomDialog.alertDialog(MainHomeActivity.this, true, false, true, btnDrawablee, titleDrawablee,
				new CustomDialog.PopUpDialogListener() {

					@Override
					public void doPositiveClick(Boolean isOk) {
						if (isOk) {// 确定

						}

					}
				});

	}

	@Override
	public void onZFClick(int position, View v, int logo) {
		Resources resources = this.getResources();

		titleDrawablef = "此订单的房间内有小吧（酒水柜），会增大清洁难度";
		btnDrawablef = resources.getDrawable(R.drawable.icon_youxiaoba_sel);

		CustomDialog.alertDialog(MainHomeActivity.this, true, false, true, btnDrawablef, titleDrawablef,
				new CustomDialog.PopUpDialogListener() {

					@Override
					public void doPositiveClick(Boolean isOk) {
						if (isOk) {// 确定

						}

					}
				});

	}

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompleted(SpeechError arg0) {
		// stop(null);
		if (vlogo != null) {
			Resources resources = getResources();
			Drawable btnDrawable = resources.getDrawable(R.drawable.icon_yuyinbobao);
			vlogo.setBackgroundDrawable(btnDrawable);
		}

	}

	@Override
	public void onSpeakBegin() {

	}

	@Override
	public void onSpeakPaused() {

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {

	}

	public void redDothome() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "reddothome");
		intentobd.putExtra("isReddot", "0");// 1显示
		sendBroadcast(intentobd);
	}

	private void releaseBroadcastReceiver() {
		recobdlist = new OBDBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cgzz.job.accesstype");
		registerReceiver(recobdlist, filter);
	}

	private OBDBroadcastReceiver recobdlist;
	// BadgeView badge1;

	private class OBDBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String TYPE = bundle.getString("TYPE");
			if ("Network".equals(TYPE)) {
				String isNetwork = bundle.getString("isNetwork");
				if ("1".equals(isNetwork)) {

					if (CurrentData.size() == 0) {
						if (application.isLogon()) {
							logoFillOrders = 1;
							getOrdercList(UrlConfig.ordercList_Http, application.getToken(), application.getUserId(),
									logoFillOrders, true);
						}
					}
				} else if ("0".equals(isNetwork)) {
				}

			} else if ("shenhe".equals(TYPE)) {
				if (application.isLogon()) {
					logoFillOrders = 1;
					getOrdercList(UrlConfig.ordercList_Http, application.getToken(), application.getUserId(),
							logoFillOrders, true);
				}
			} else if ("reddothomemessage".equals(TYPE)) {
				if (application.isLogon()) {
					rl_home_7.setVisibility(View.VISIBLE);
					tv_home_tz_title.setText(application.getHomemessage());
				}
			}

			// else if ("reddothome".equals(TYPE)) {
			// String isReddot = bundle.getString("isReddot");
			// if (application.isLogon()) {
			//
			// if ("1".equals(isReddot)) {
			// badge1.show(true);
			// application.setReddothome(true);
			// } else if ("0".equals(isReddot)) {
			// badge1.hide();
			// application.setReddothome(false);
			// }
			// }
			// }
		}
	}

	@Override
	protected void onDestroy() {
		if (recobdlist != null)
			unregisterReceiver(recobdlist);

		super.onDestroy();
	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile() {
		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_signet_two), Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);
		if (application.isEnvironment()) {
			title.setText("当前是正式环境");
		} else {
			title.setText("当前是测试环境");
		}

		up.setText("切换到测试环境");
		under.setText("切换到正式环境");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				application.setEnvironment(false);
				application.setLogon(false);
				Utils.closeActivity();
				android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
				System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				application.setEnvironment(true);
				application.setLogon(false);
				Utils.closeActivity();
				android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
				System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}

}
