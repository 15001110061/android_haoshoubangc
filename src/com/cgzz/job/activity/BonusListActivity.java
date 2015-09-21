package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.BonusListAdapter;
import com.cgzz.job.adapter.BonusListAdapter.OnCancelOrderClickListener;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.view.ScaleGallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BonusListActivity extends BaseActivity
		implements OnClickListener, OnCancelOrderClickListener {

	private ScaleGallery gallery;

	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private String orderid = "";
	private BonusListAdapter bonusListAdapter ;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.redmylist_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserRedmylist(data);
					CurrentData.clear();
					
					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {


						CurrentData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
					} 
					bonusListAdapter.refreshMYData(CurrentData);
					
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserRedmylist(data);
					ToastUtil.makeShortText(BonusListActivity.this, bundle.get("msg").toString());
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
		setContentView(R.layout.activity_bouns_list);
		setTitle("红包列表", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		initView();
	}	private LinearLayout llLeft;
	TextView tv_home_bonus;
	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		gallery = (ScaleGallery) findViewById(R.id.gallery);
		gallery.setSpacing(-1);

		bonusListAdapter = new BonusListAdapter(this, gallery);
		bonusListAdapter.setOnCancelOrderClickListener(this, 0);

		gallery.setAdapter(bonusListAdapter);
		gallery.setUnselectedAlpha(0.2f);

	}


	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 
			finish();
			break;
			
		case R.id.tv_home_bonus://
		
			break;
			
		default:
			break;
		}

	}
	@Override
	protected void onResume() {
		super.onResume();


		getRedMylist(UrlConfig.redmylist_Http, application.getToken(), application.getUserId(), 
				true);
	}
	/**
	 * 订单列表
	 */
	private void getRedMylist(String url, String token, String userid,boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, BonusListActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(BonusListActivity.this), HttpStaticApi.redmylist_Http, null,
				loadedtype);

	}


	@Override
	public void onCancelOrderClick(int position, View v, int logo) {
		
		String	url = CurrentData.get(position).get("url")+"";
		Intent	intent = new Intent(BonusListActivity.this, WebBrowserActivity.class);
		intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "红包");
		intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
		startActivity(intent);
		finish();

	}



	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
