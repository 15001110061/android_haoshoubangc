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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.BaseActivityCloseListener;
import com.cgzz.job.R;
import com.cgzz.job.adapter.ConsultingPagerAdapter;
import com.cgzz.job.adapter.MyIncomeAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.BadgeView;
import com.cgzz.job.view.CustomListView;

/***
 * @author wjm 收入
 */
public class MyIncomeActivity extends BaseActivity implements OnClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private TextView tv_myincome_jifen, tv_myincome_zichan;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private MyIncomeAdapter myIncomeAdapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private LinearLayout llLeft, llright;
	String zichan = "", havabank = "";
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.myIncome_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					CollectionData.clear();
					bundle = ParserUtil.ParserMyIncome(data);
					zichan = bundle.get("totalincome").toString();
					havabank = bundle.get("havebank").toString();
					if ("1".equals(havabank)) {
						badge.hide();
					} else {
						badge.show(true);
					}
					tv_myincome_zichan.setText("￥" + bundle.get("totalincome").toString());
					tv_myincome_jifen.setText(bundle.get("totalscore").toString());
					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {
						rl_home_1.setVisibility(View.VISIBLE);
						// rl_home_2.setVisibility(View.GONE);
						CollectionData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
					} else {

						rl_home_1.setVisibility(View.GONE);
						// rl_home_2.setVisibility(View.VISIBLE);

						// ToastUtil.makeShortText(
						// MyIncomeActivity.this,
						// MyIncomeActivity.this.getResources().getString(
						// R.string.http_nodata));
					}

					myIncomeAdapter.refreshMYData(CollectionData);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myincome);

		application = (GlobalVariables) getApplicationContext();

		application.putClosePath(UrlConfig.PATH_KEY_ADDCARD, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});
		setTitle("我的收入", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "工资卡");
		initView();

	}

	RelativeLayout rl_myincome;
	RelativeLayout rl_home_1;
	TextView tv_title_right;
	BadgeView badge;

	private void initView() {
		rl_home_1 = (RelativeLayout) findViewById(R.id.rl_home_1);
		// rl_home_2 = (RelativeLayout) findViewById(R.id.rl_home_2);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_title_right = (TextView) findViewById(R.id.tv_title_right);

		tv_myincome_jifen = (TextView) findViewById(R.id.tv_myincome_jifen);
		tv_myincome_zichan = (TextView) findViewById(R.id.tv_myincome_zichan);
		rl_myincome = (RelativeLayout) findViewById(R.id.rl_myincome);
		llright.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		rl_myincome.setOnClickListener(this);
		// 下方列表
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(MyIncomeActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(MyIncomeActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多
		myIncomeAdapter = new MyIncomeAdapter(MyIncomeActivity.this);
		lvCollection.setAdapter(myIncomeAdapter);

		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(listviews);
		viewpager.setAdapter(pagerAdapter);

		badge = new BadgeView(this, tv_title_right);
		badge.setText("");
		badge.setTextSize(1);
		badge.setTextColor(this.getResources().getColor(R.color.common_dc2f2c));
		badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badge.setBackgroundResource(R.drawable.redpoint_icon);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 银行卡
			if ("1".equals(havabank)) {
				intent = new Intent(MyIncomeActivity.this, LookbankActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(MyIncomeActivity.this, AddBankCardActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.rl_myincome:// 总资产
			intent = new Intent(MyIncomeActivity.this, AssetsActivity.class);

			Bundle bundle = new Bundle();
			bundle.putString("zichan", zichan);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		badge.hide();
		getMyIncome(UrlConfig.myIncome_Http, application.getToken(), application.getUserId(), false);
	}

	/**
	 * 我的收入接口
	 */
	private void getMyIncome(String url, String token, String userid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MyIncomeActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MyIncomeActivity.this), HttpStaticApi.myIncome_Http, null, loadedtype);
	}

}
