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
 * @author wjm 总资产
 */
public class AssetsActivity extends BaseActivity implements OnClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private TextView tv_myincome_jifen, tv_myincome_zichan;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private AssetsAdapter assetsAdapter;
	private LinearLayout llLeft, llright;
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.incomeDetail_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						CurrentData.clear();
						logoCurrent = 2;
					} else {
						logoCurrent++;
					}
					bundle = ParserUtil.ParserIncomeDetail(data);
//					if(!bundle.getString("bank_name").equals("")){
////						rl_yinhang.setVisibility(View.VISIBLE);
//						iv_my_income2.setText(bundle.getString("bank_name"));
//						iv_my_income4.setText("**** **** **** "+bundle.getString("bank_card"));
//						
//						
//						ImageListener listener = ImageLoader.getImageListener(
//								iv_6, R.drawable.image_yinhang,
//								R.drawable.image_yinhang);
//						mImageLoader.get(bundle.getString("bank_icon"), listener);
//						
//					}else {
//						rl_yinhang.setVisibility(View.GONE);
//					}
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {
						rl_home_1.setVisibility(View.VISIBLE);
						rl_home_2.setVisibility(View.GONE);
						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
					} else {
						
						
						if (!loadedtype) {
							lvCollection.onLoadMorNodata();
//							ToastUtil.makeShortText(MainOrdersFragment.this,
//									MainOrdersFragment.this.getResources()
//											.getString(R.string.http_nodata));
						} else {
							lvCollection.setCanLoadMore(false);
							rl_home_1.setVisibility(View.GONE);
							rl_home_2.setVisibility(View.VISIBLE);
						}
						
						
						
						
						
					
//						ToastUtil.makeShortText(
//								AssetsActivity.this,
//								AssetsActivity.this.getResources().getString(
//										R.string.http_nodata));
					}

					assetsAdapter.refreshMYData(CurrentData);
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;
				}
				break;

			}
		}
	};
	String zichan = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assets);
		setTitle("我的收入", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "银行卡");

		Intent intent = getIntent();
		zichan = intent.getStringExtra("zichan");
		initView();
		setview();

	}

	private int logoCurrent = 1;// 页码标识
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	RelativeLayout rl_myincome,rl_yinhang;
	TextView iv_my_income2,iv_my_income4 ;
	ImageView iv_6;
	RelativeLayout rl_home_1, rl_home_2;
	private void initView() {
		rl_home_1 = (RelativeLayout) findViewById(R.id.rl_home_1);
		rl_home_2 = (RelativeLayout) findViewById(R.id.rl_home_2);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_myincome_jifen = (TextView) findViewById(R.id.tv_myincome_jifen);
		tv_myincome_zichan = (TextView) findViewById(R.id.tv_myincome_zichan);
		rl_myincome = (RelativeLayout) findViewById(R.id.rl_myincome);
		rl_yinhang= (RelativeLayout) findViewById(R.id.rl_yinhang);
		
		iv_my_income2 = (TextView) findViewById(R.id.iv_my_income2);
		iv_my_income4= (TextView) findViewById(R.id.iv_my_income4);
		iv_6= (ImageView) findViewById(R.id.iv_6);
		llright.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		rl_myincome.setOnClickListener(this);
		// 下方列表
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(AssetsActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(AssetsActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(true);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多
		assetsAdapter = new AssetsAdapter(AssetsActivity.this);
		lvCollection.setAdapter(assetsAdapter);

		lvCollection.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getIncomeDetail(UrlConfig.incomeDetail_Http_Http,
						application.getToken(), application.getUserId(),
						logoCurrent, false);
			}
		});

		lvCollection.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				logoCurrent = 1;

				getIncomeDetail(UrlConfig.incomeDetail_Http_Http,
						application.getToken(), application.getUserId(),
						logoCurrent, true);
			}
		});

		logoCurrent = 1;

		getIncomeDetail(UrlConfig.incomeDetail_Http_Http,
				application.getToken(), application.getUserId(), logoCurrent,
				true);
		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
	}

	private void setview() {
		tv_myincome_zichan.setText("￥"+zichan);

	}

	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			onBackPressed();
			break;
		case R.id.rl_myincome:// 总资产

			break;
		case R.id.ll_title_right:// 银行卡
			intent = new Intent(AssetsActivity.this,
					AddBankCardActivity.class);
			startActivity(intent);
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
	 * 我的收入接口
	 */
	private void getIncomeDetail(String url, String token, String userid,
			int page, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);

		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				AssetsActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(AssetsActivity.this),
				HttpStaticApi.incomeDetail_Http, null, loadedtype);
	}

}
