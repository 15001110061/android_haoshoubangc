package com.cgzz.job.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.activity.SignedActivityOne.TimeCount;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.wheel.GoOffWheelView;
import com.cgzz.job.wheel.GoOffWheelView.WheelDateChoiseListener;

/***
 * 
 * @author wjm ��������
 */
public class PasswordActivity extends BaseActivity implements OnClickListener,
		WheelDateChoiseListener {
	TimeCount countdown;
	EditText et_signed_phones, et_signed_captchas, et_signed_passwords;
	TextView et_signed_send_captchas_code, tv_signed_next;
	LinearLayout llLeft;
	public GlobalVariables application;
	String Code = "";
	String mobiles = "";
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.sendCode_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
			
					bundle = ParserUtil.ParserSendCode(data);
					Code = bundle.get("result").toString();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserSendCode(data);
					ToastUtil.makeShortText(PasswordActivity.this,
							bundle.get("msg").toString());
					break;
				default:
					break;
				}

				break;

			case HttpStaticApi.forgetPassword_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserForgetPassword(data);
					ToastUtil.makeShortText(PasswordActivity.this,
							bundle.get("msg").toString());
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserForgetPassword(data);
					ToastUtil.makeShortText(PasswordActivity.this,
							bundle.get("msg").toString());
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
		setContentView(R.layout.activity_password);
		application = (GlobalVariables) getApplicationContext();
		setTitle("��������", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");

		initView();
		initListenger();

	}

	Resources resources;
	Drawable btnXuanzhong, btnWeixuanzhong;

	private void initView() {
		resources = this.getResources();
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		et_signed_phones = (EditText) findViewById(R.id.et_signed_phones);
		et_signed_captchas = (EditText) findViewById(R.id.et_signed_captchas);
		et_signed_passwords = (EditText) findViewById(R.id.et_signed_passwords);
		et_signed_send_captchas_code = (TextView) findViewById(R.id.et_signed_send_captchas_code);
		tv_signed_next = (TextView) findViewById(R.id.tv_signed_next);
		btnWeixuanzhong = resources
				.getDrawable(R.drawable.shape_current_signed_rls_bg);
		btnXuanzhong = resources
				.getDrawable(R.drawable.shape_current_signed_rl_bg);
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		tv_signed_next.setOnClickListener(this);
		et_signed_send_captchas_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.tv_signed_next:// �ύ
			mobiles = et_signed_phones.getText().toString();
			String passwords = et_signed_passwords.getText().toString();
			if (Utils.isEmpty(passwords)) {
				ToastUtil.makeShortText(this, "������������");
				return;
			}
			String captchas = et_signed_captchas.getText().toString();
			if (Utils.isEmpty(captchas)) {
				ToastUtil.makeShortText(this, "��֤�벻��Ϊ��");
				return;
			}

			 if (!Code.equals(captchas)) {
			 ToastUtil.makeShortText(this, "��֤�벻��ȷ");
			 return;
			 }
			getForgetPassword(UrlConfig.forgetPassword_Http, mobiles,
					passwords, "2", true);
			break;
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.et_signed_send_captchas_code:// ��֤��
			// ����CountDownTimer����
			mobiles = et_signed_phones.getText().toString();
			if (Utils.isEmpty(mobiles) || !Utils.isPhoneNumberValid(mobiles)) {
				ToastUtil.makeShortText(this, "��������ȷ�ֻ���");
				return;
			}
			// ��ȡ��֤��
			getCode(UrlConfig.sendCode_Http, mobiles, true);
			countdown = new TimeCount(60000*3, 1000);
			countdown.start();
			et_signed_send_captchas_code.requestFocus();
			et_signed_send_captchas_code.setBackgroundDrawable(btnXuanzhong);

			break;
		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	/* ����һ������ʱ���ڲ��� */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			et_signed_send_captchas_code.setBackgroundDrawable(btnWeixuanzhong);
			et_signed_send_captchas_code.setText("���·���");
			et_signed_send_captchas_code.setClickable(true);
			et_signed_send_captchas_code.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ������ʾ
			et_signed_send_captchas_code.setClickable(false);
			et_signed_send_captchas_code.setEnabled(false);
			et_signed_send_captchas_code.setText(millisUntilFinished / 1000
					+ "������·���");
		}
	}

	private void getCode(String url, String phone, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", phone);
//		map.put("apptype", "2");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				PasswordActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(PasswordActivity.this),
				HttpStaticApi.sendCode_Http, null, loadedtype);
	}

	private void getForgetPassword(String url, String mobile, String password,
			String apptype, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", mobile);
		map.put("password", password);
		map.put("apptype", apptype);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				PasswordActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(PasswordActivity.this),
				HttpStaticApi.forgetPassword_Http, null, loadedtype);
	}

	@Override
	public void chooseTime(String date) {
		ToastUtil.makeShortText(PasswordActivity.this, date);

	}

}
