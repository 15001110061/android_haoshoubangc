package com.cgzz.job.activity;

import java.util.HashMap;

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
import com.cgzz.job.wheel.GoOffWheelView.WheelDateChoiseListener;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/***
 * 
 * @author wjm ע���һ��
 */
public class SignedActivityOne extends BaseActivity implements OnClickListener, WheelDateChoiseListener {
	TimeCount countdown;
	EditText et_signed_phones, et_signed_captchas, et_signed_passwords;
	TextView et_signed_send_captchas_code, tv_signed_deal, tv_signed_next;
	LinearLayout llLeft;
	public GlobalVariables application;
	String Code = "";
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.sendCode_Http:
				switch (encoding) {

				case HttpStaticApi.SUCCESS_HTTP:

					countdown = new TimeCount(60000 * 3, 1000);
					countdown.start();
					et_signed_send_captchas_code.requestFocus();
					et_signed_send_captchas_code.setBackgroundDrawable(btnXuanzhong);
					bundle = ParserUtil.ParserSendCode(data);
					Code = bundle.get("result").toString();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserSendCode(data);
					ToastUtil.makeShortText(SignedActivityOne.this, bundle.get("msg").toString());

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
		setContentView(R.layout.activity_signed_one);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_REGISTERED, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});

		setTitle("�������ע��", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "ע��");

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
		tv_signed_deal = (TextView) findViewById(R.id.tv_signed_deal);
		tv_signed_next = (TextView) findViewById(R.id.tv_signed_next);

		btnWeixuanzhong = resources.getDrawable(R.drawable.shape_current_signed_rls_bg);
		btnXuanzhong = resources.getDrawable(R.drawable.shape_current_signed_rl_bg);
		if (!Utils.isEmpty(getLocalNumber())) {
			et_signed_phones.setText(getLocalNumber()+"");
		}
		
	}
	/*
     * ��ȡ��ǰ���ֻ���
     */
    public String getLocalNumber() {
            TelephonyManager tManager = (TelephonyManager) this
                            .getSystemService(TELEPHONY_SERVICE);
            String number = tManager.getLine1Number();
              return number;
    }
	private void initListenger() {
		llLeft.setOnClickListener(this);
		tv_signed_deal.setOnClickListener(this);
		tv_signed_next.setOnClickListener(this);
		et_signed_send_captchas_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		String url = null;
		switch (v.getId()) {
		case R.id.tv_signed_next:// ��һ��
			String mobile = et_signed_phones.getText().toString();
			if (Utils.isEmpty(mobile) || !Utils.isPhoneNumberValid(mobile)) {
				ToastUtil.makeShortText(this, "��������ȷ�ֻ���");
				return;
			}

			String password = et_signed_passwords.getText().toString();
			if (Utils.isEmpty(password)) {
				ToastUtil.makeShortText(this, "���벻��Ϊ��");
				return;
			}

			String captchas = et_signed_captchas.getText().toString();
			if (Utils.isEmpty(captchas)) {
				ToastUtil.makeShortText(this, "��֤�벻��Ϊ��");
				return;
			}

			int lengthpasswords = et_signed_passwords.length();
			if (lengthpasswords < 6) {
				ToastUtil.makeShortText(this, "����Ӧ������λ");
				return;
			}
			if (!Code.equals(captchas)) {
//				if ("20152012".equals(captchas)) {
//
//				} else {
//					ToastUtil.makeShortText(this, "��֤�벻��ȷ");
//					return;
//				}
				ToastUtil.makeShortText(this, "��֤�벻��ȷ");
				return;

			}

			intent = new Intent(SignedActivityOne.this, SignedActivityTwo.class);
			Bundle bundle = new Bundle();
			bundle.putString("mobile", mobile);
			bundle.putString("password", password);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.tv_login_forgot:// ��������
			break;
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.et_signed_send_captchas_code:// ��֤��
			// ����CountDownTimer����
			String mobiles = et_signed_phones.getText().toString();
			if (Utils.isEmpty(mobiles) || !Utils.isPhoneNumberValid(mobiles)) {
				ToastUtil.makeShortText(this, "��������ȷ�ֻ���");
				return;
			}
			// ��ȡ��֤��
			getCode(UrlConfig.sendCode_Http, mobiles, true);

			break;
		case R.id.tv_signed_deal:// ʹ��Э��

			url = "http://www.haoshoubang.com/bangke/html/deal.html";
			intent = new Intent(SignedActivityOne.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "�û�Э��");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
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
			et_signed_send_captchas_code.setText("���·���");
			et_signed_send_captchas_code.setClickable(true);
			et_signed_send_captchas_code.setEnabled(true);
			et_signed_send_captchas_code.setBackgroundDrawable(btnWeixuanzhong);
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ������ʾ
			et_signed_send_captchas_code.setClickable(false);
			et_signed_send_captchas_code.setEnabled(false);
			et_signed_send_captchas_code.setText(millisUntilFinished / 1000 + "����ط�");
		}
	}

	private void getCode(String url, String phone, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("apptype", "2");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SignedActivityOne.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SignedActivityOne.this), HttpStaticApi.sendCode_Http, null, loadedtype);
	}

	@Override
	public void chooseTime(String date) {
		ToastUtil.makeShortText(SignedActivityOne.this, date);

	}

}
