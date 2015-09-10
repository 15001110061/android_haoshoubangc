package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.AssetsAdapter;
import com.cgzz.job.adapter.ConsultingPagerAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomListView;
import com.cgzz.job.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.view.CustomListView.OnRefreshListener;

/***
 * @author wjm 更改银行卡
 */
public class LookbankActivity extends BaseActivity implements OnClickListener {
	private LinearLayout llLeft, llright;
	// String zichan = "";
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.myBank_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMyBank(data);
					if (!Utils.isEmpty(bundle.getString("bank_icon"))) {
						try {
							ImageListener listener = ImageLoader.getImageListener(iv_6, R.drawable.image_yinhang,
									R.drawable.image_yinhang);
							mImageLoader.get(bundle.getString("bank_icon"), listener);
						} catch (Exception e) {
							// TODO: handle exception
						}
					
					}

					iv_my_income2.setText(bundle.getString("bank_name"));
					iv_my_income3.setText(bundle.getString("bank_username"));
					iv_my_income4.setText("**** **** **** " + bundle.getString("bank_card"));
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				}
				break;

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lookbank);
		setTitle("工资卡", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");

		// Intent intent = getIntent();
		// zichan = intent.getStringExtra("zichan");
		initView();

		setview();

	}

	private int logoCurrent = 1;// 页码标识
	TextView iv_my_income2, iv_my_income4, tv_lookbank_genghuan, iv_my_income3;
	ImageView iv_6;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_lookbank_genghuan = (TextView) findViewById(R.id.tv_lookbank_genghuan);

		iv_my_income3 = (TextView) findViewById(R.id.iv_my_income3);
		iv_my_income2 = (TextView) findViewById(R.id.iv_my_income2);// 那家银行
		iv_my_income4 = (TextView) findViewById(R.id.iv_my_income4);// 卡号
		iv_6 = (ImageView) findViewById(R.id.iv_6);// 银行卡图片
		llright.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		tv_lookbank_genghuan.setOnClickListener(this);
	}

	private void setview() {

	}

	@Override
	protected void onResume() {
		super.onResume();

		myBank(UrlConfig.myBank_Http, application.getToken(), application.getUserId(), true);

	}

	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			onBackPressed();
			break;
		case R.id.tv_lookbank_genghuan:// 银行卡
			intent = new Intent(LookbankActivity.this, AddBankCardActivity.class);
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
		finish();
	}

	/**
	 * 我的银行卡信息接口
	 */
	private void myBank(String url, String token, String userid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, LookbankActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(LookbankActivity.this), HttpStaticApi.myBank_Http, null, loadedtype);
	}
}
