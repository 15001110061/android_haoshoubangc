package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.CancelReasonAdapter;
import com.cgzz.job.adapter.CancelReasonAdapter.ViewHolders;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;

public class CancelReasonActivity extends BaseActivity implements OnClickListener {
	private ListView lv;
	private CancelReasonAdapter mAdapter;
	private ArrayList<Map<String, String>> list;
	LinearLayout llLeft;

	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.cancelReason_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserCancelReason(data);

					list = (ArrayList<Map<String, String>>) bundle.getSerializable("list");
					mAdapter.refreshMYData(list);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserCancelReason(data);
					ToastUtil.makeShortText(CancelReasonActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.cancel_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(CancelReasonActivity.this, bundle.get("msg").toString());

					Intent mIntent = new Intent();
					mIntent.putExtra("isquxiao", "y");
					setResult(1, mIntent);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(CancelReasonActivity.this, bundle.get("msg").toString());

					Intent mIntents = new Intent();
					mIntents.putExtra("isquxiao", "n");
					setResult(1, mIntents);
					finish();
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
	TextView tv_cancelreason_submitted, tv_cancelreason_dengdeng;
	String orderdetailid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancelreason);
		setTitle("ȡ������", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");

		//
		Intent intent = getIntent();
		orderdetailid = intent.getStringExtra("orderdetailid");
		tv_cancelreason_dengdeng = (TextView) findViewById(R.id.tv_cancelreason_dengdeng);
		lv = (ListView) findViewById(R.id.lv);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		tv_cancelreason_submitted = (TextView) findViewById(R.id.tv_cancelreason_submitted);
		tv_cancelreason_submitted.setOnClickListener(this);
		tv_cancelreason_dengdeng.setOnClickListener(this);
		// ΪAdapter׼������;
		// initDate();
		list = new ArrayList<Map<String, String>>();
		mAdapter = new CancelReasonAdapter(this);
		lv.setAdapter(mAdapter);
		llLeft.setOnClickListener(this);
		cancelReason(UrlConfig.cancelReason_Http, true);
		// ��listView�ļ�����
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				ViewHolders holder = (ViewHolders) arg1.getTag();
				// �ı�CheckBox��״̬
				holder.cb.toggle();
				// ��CheckBox��ѡ��״����¼����
				// ����ѡ����Ŀ

				for (int i = 0; i < list.size(); i++) {
					if ("true".equals(list.get(i).get("flag"))) {
						list.get(i).put("flag", "false");
					}
				}

				if (holder.cb.isChecked() == true) {
					list.get(arg2).put("flag", "true");
				} else {
					list.get(arg2).put("flag", "false");
				}
				mAdapter.refreshMYData(list);
			}
		});

	}

	// ��ʼ������
	// private void initDate() {
	// for (int i = 0; i < str.length; i++) {
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("content", str[i]);
	// map.put("flag", "false");
	// list.add(map);
	// }
	// }

	private void dataChanged() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_cancelreason_dengdeng:
		case R.id.ll_title_left:
			finish();
			break;

		case R.id.tv_cancelreason_submitted:
			String s = "";
			for (int i = 0; i < list.size(); i++) {
				if ("true".equals(list.get(i).get("flag"))) {
					s = list.get(i).get("dict_key");
				}
			}
			if (Utils.isEmpty(s)) {
				ToastUtil.makeShortText(this, "��ѡ��ȡ��ԭ��");
				return;
			}
			cancel(UrlConfig.cancel_Http, application.getToken(), application.getUserId(), orderdetailid, s, true);

			break;

		default:
			break;
		}

	}

	private void cancelReason(String url, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, CancelReasonActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(CancelReasonActivity.this), HttpStaticApi.cancelReason_Http, null,
				loadedtype);
	}

	private void cancel(String url, String token, String userid, String orderdetailid, String cancelid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderdetailid", orderdetailid);
		map.put("cancelid", cancelid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, CancelReasonActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(CancelReasonActivity.this), HttpStaticApi.cancel_Http, null,
				loadedtype);
	}
}