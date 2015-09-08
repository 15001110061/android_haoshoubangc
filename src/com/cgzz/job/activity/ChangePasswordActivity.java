package com.cgzz.job.activity;

import java.util.HashMap;

import android.content.Intent;
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
 * @author wjm �޸�����
 */
public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {

	LinearLayout llLeft;
	public GlobalVariables application;
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
			case HttpStaticApi.updatePassword_Http:
				switch (encoding) {


				
				
				
				
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(ChangePasswordActivity.this, bundle.getString("msg"));
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(ChangePasswordActivity.this, bundle.getString("msg"));
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
		setContentView(R.layout.activity_changepassword);
		application = (GlobalVariables) getApplicationContext();
		setTitle("�޸�����", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");

		initView();
		initListenger();

	}

	EditText et_changepassword_yuanshi, et_changepassword_xin;
	TextView et_changepassword_tijiao;

	private void initView() {

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		et_changepassword_yuanshi = (EditText) findViewById(R.id.et_changepassword_yuanshi);

		et_changepassword_xin = (EditText) findViewById(R.id.et_changepassword_xin);
		et_changepassword_tijiao = (TextView) findViewById(R.id.et_changepassword_tijiao);
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		et_changepassword_tijiao.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.et_changepassword_tijiao:// �ύ

			String yuanshi = et_changepassword_yuanshi.getText().toString();
			if (Utils.isEmpty(yuanshi)) {
				ToastUtil.makeShortText(this, "ԭʼ���벻��Ϊ��");
				return;
			}

			String xin = et_changepassword_xin.getText().toString();
			if (Utils.isEmpty(xin)) {
				ToastUtil.makeShortText(this, "�����벻��Ϊ��");
				return;
			}
			updatePassword(UrlConfig.updatePassword_Http, application.getUserId(), "2", application.getToken(), xin, yuanshi, true);
			break;
		case R.id.ll_title_left:// ����
			finish();
			break;
		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	private void updatePassword(String url, String userid, String apptype, String token,
			String password, String oldPassword, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("apptype", apptype);
		map.put("token", token);
		map.put("password", password);
		map.put("oldPassword", oldPassword);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				ChangePasswordActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(ChangePasswordActivity.this),
				HttpStaticApi.updatePassword_Http, null, loadedtype);
	}

}
