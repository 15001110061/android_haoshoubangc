package com.cgzz.job.activity;

import java.util.HashMap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;

/***
 * @author wjm ��������
 */
public class SetingActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private LinearLayout llLeft, llright;
	private TextView iv_seting_exit, tv_seting_huanying, tv_seting_haoping, tv_seting_changjian;
	private CheckBox cb_seting_shoushi, cb_seting_jiedan;
	private String stopAccept;
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.stopAccept_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SetingActivity.this, bundle.get("msg").toString());
					if ("1".equals(stopAccept)) {

						application.setStopAccept(false);
					} else {
						application.setStopAccept(true);
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SetingActivity.this, bundle.get("msg").toString());
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seting);
		setTitle("����", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		findView();
		init();

	}

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		iv_seting_exit = (TextView) findViewById(R.id.iv_seting_exit);
		tv_seting_huanying = (TextView) findViewById(R.id.tv_seting_huanying);
		tv_seting_changjian = (TextView) findViewById(R.id.tv_seting_changjian);
		tv_seting_haoping = (TextView) findViewById(R.id.tv_seting_haoping);
		cb_seting_jiedan = (CheckBox) findViewById(R.id.cb_seting_jiedan);

		cb_seting_shoushi = (CheckBox) findViewById(R.id.cb_seting_shoushi);
		if (application.isGestures()) {
			cb_seting_shoushi.setChecked(true);
		} else {
			cb_seting_shoushi.setChecked(false);
		}

		if (application.isStopAccept()) {
			cb_seting_jiedan.setChecked(true);// ֹͣ
		} else {
			cb_seting_jiedan.setChecked(false);// ��ʼ
		}
	}

	private void init() {
		llLeft.setOnClickListener(this);
		// iv_seting_newsremind.setOnClickListener(this);
		iv_seting_exit.setOnClickListener(this);
		cb_seting_shoushi.setOnCheckedChangeListener(this);
		cb_seting_jiedan.setOnCheckedChangeListener(this);
		tv_seting_haoping.setOnClickListener(this);
		tv_seting_huanying.setOnClickListener(this);
		tv_seting_changjian.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String url = null;
		switch (arg0.getId()) {
		case R.id.iv_seting_newsremind:
			break;
		// case R.id.iv_seting_opinion:
		//
		// intent = new Intent(SetingActivity.this, OpinionActivity.class);
		// startActivity(intent);
		// break;

		case R.id.iv_seting_exit:
			popTheirProfile();
			break;
		case R.id.ll_title_left:// ����
			finish();
			break;

		case R.id.tv_seting_huanying:// ��ӭ
			intent = new Intent(SetingActivity.this, IntroductionActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_seting_haoping:// ����
			// ���밲׿�г����ִ���
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			intent = new Intent(Intent.ACTION_VIEW, uri);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				ToastUtil.makeShortText(this, "�����Ե�¼��׿�г�");
			}
			break;
		case R.id.tv_seting_changjian:// ��������

			url = "http://www.haoshoubang.com/bangke/html/faq_c.html";
			intent = new Intent(SetingActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "��������");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if (arg0 == cb_seting_shoushi) {
			if (arg1) {
				intent = new Intent(SetingActivity.this, GestureEditActivity.class);
				startActivity(intent);
				finish();
			} else {
				application.setGestures(false);
			}

		} else if (arg0 == cb_seting_jiedan) {
			if (arg1) {
				stopAccept(UrlConfig.stopAccept_Http, application.getToken(), application.getUserId(), "0", true);
			} else {
				stopAccept(UrlConfig.stopAccept_Http, application.getToken(), application.getUserId(), "1", true);
			}

		}
		// cb_seting_newsremind.setChecked(false);
	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile() {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// û�д˾����ⲿ������ʧ
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("�Ƿ��˳�?");
		up.setText("��");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("��");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				application.setLogon(false);
				JPushInterface.stopPush(SetingActivity.this);
				Utils.closeActivity();

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

	private void stopAccept(String url, String token, String userid, String type, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("type", type);// 0ֹͣ
		stopAccept = type;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SetingActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SetingActivity.this), HttpStaticApi.stopAccept_Http, null, loadedtype);
	}
}
