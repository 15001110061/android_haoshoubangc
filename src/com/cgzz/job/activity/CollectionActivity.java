package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.CollectionAdapter;
import com.cgzz.job.adapter.CollectionAdapter.OnTelClickListener;
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
 * @author wjm 我的收藏
 */
public class CollectionActivity extends BaseActivity implements
		OnClickListener, OnTelClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private CollectionAdapter Collectionadapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.collection_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					if (loadedtype) {
						CollectionData.clear();
						logoCollection = 2;
					} else {
						logoCollection++;
					}

					bundle = ParserUtil.ParserCollection(data);

					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CollectionData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						rl_home_1.setVisibility(View.VISIBLE);
						rl_home_2.setVisibility(View.GONE);
						lvCollection.setCanLoadMore(true);
					} else {

						if (!loadedtype) {
							ToastUtil.makeShortText(CollectionActivity.this,
									CollectionActivity.this.getResources()
											.getString(R.string.http_nodata));
						} else {
							lvCollection.setCanLoadMore(false);

							rl_home_1.setVisibility(View.GONE);
							rl_home_2.setVisibility(View.VISIBLE);
						}

					}

					Collectionadapter.refreshMYData(CollectionData);
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
	LinearLayout llLeft;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		setTitle("我的收藏", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "注册");
		initView();

	}

	RelativeLayout rl_home_1, rl_home_2;

	@SuppressLint("NewApi")
	private void initView() {
		rl_home_1 = (RelativeLayout) findViewById(R.id.rl_home_1);
		rl_home_2 = (RelativeLayout) findViewById(R.id.rl_home_2);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		// 下方列表
		// WindowManager m = this.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// ivLine = (ImageView) findViewById(R.id.iv_line);
		// // 设置滑动线的宽度
		// android.view.ViewGroup.LayoutParams layoutParams = ivLine
		// .getLayoutParams();
		// LENGTH = d.getWidth() / 2;
		// layoutParams.width = LENGTH;
		// ivLine.setLayoutParams(layoutParams);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(CollectionActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(CollectionActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(true);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多
		Collectionadapter = new CollectionAdapter(CollectionActivity.this);
		lvCollection.setAdapter(Collectionadapter);

		Collectionadapter.setOnTelClickListener(this, 0);
		lvCollection.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getCollection(UrlConfig.collection_Http,
						application.getToken(), application.getUserId(),
						logoCollection, false);
			}
		});

		lvCollection.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				logoCollection = 1;
				getCollection(UrlConfig.collection_Http,
						application.getToken(), application.getUserId(),
						logoCollection, true);

			}
		});

		logoCollection = 1;
		getCollection(UrlConfig.collection_Http, application.getToken(),
				application.getUserId(), logoCollection, true);

		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.tv_route_tab_apply://
			break;
		default:
			break;
		}
	}

	/**
	 * 我的收藏列表
	 */
	private void getCollection(String url, String token, String userid,
			int page, boolean loadedtype) {
		url = url + "?apptype=2&token=" + token + "&userid=" + userid
				+ "&page=" + page;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				CollectionActivity.this, url, null, callBack,
				GlobalVariables.getRequestQueue(CollectionActivity.this),
				HttpStaticApi.collection_Http, null, loadedtype);
	}

	/***
	 * 打电话
	 */
	@Override
	public void onTelClick(int position, View v, int logo) {

		// intent.putExtra("orderid", );
		popTheirProfile(CollectionData.get(position).get("phone"));
	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile(final String tel) {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two),
				Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("是否拨打酒店电话?");
		up.setText("是");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("否");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				Utils.call(tel, CollectionActivity.this);

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

}
