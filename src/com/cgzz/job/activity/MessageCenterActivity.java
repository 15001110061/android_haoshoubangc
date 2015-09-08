package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.ConsultingPagerAdapter;
import com.cgzz.job.adapter.MessageCenterAdapter;
import com.cgzz.job.adapter.MessageCenterAdapter.OnTelClickListener;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomListView;
import com.cgzz.job.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.view.CustomListView.OnRefreshListener;

/***
 * @author wjm ��Ϣ����
 */
public class MessageCenterActivity extends BaseActivity implements
		OnClickListener, OnTelClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private MessageCenterAdapter messageCenterAdapter;
	private int logoCollection = 1;// ҳ���ʶ
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.message_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					if (loadedtype) {
						CollectionData.clear();
						logoCollection = 2;
					} else {
						logoCollection++;
					}

					bundle = ParserUtil.ParserMessagelistB(data);

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
							lvCollection.onLoadMorNodata();
							// ToastUtil.makeShortText(MessageCenterActivity.this,
							// MessageCenterActivity.this.getResources()
							// .getString(R.string.http_nodata));
						} else {
							lvCollection.setCanLoadMore(false);

							rl_home_1.setVisibility(View.GONE);
							rl_home_2.setVisibility(View.VISIBLE);
						}

					}

					messageCenterAdapter.refreshMYData(CollectionData);
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
		setContentView(R.layout.activity_message);
		setTitle("��Ϣ����", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		initView();

	}

	RelativeLayout rl_home_1, rl_home_2;
	View nozixun;

	@SuppressLint("NewApi")
	private void initView() {
		rl_home_1 = (RelativeLayout) findViewById(R.id.rl_home_1);
		rl_home_2 = (RelativeLayout) findViewById(R.id.rl_home_2);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		// �·��б�
		// WindowManager m = this.getWindowManager();
		// Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// ivLine = (ImageView) findViewById(R.id.iv_line);
		// // ���û����ߵĿ��
		// android.view.ViewGroup.LayoutParams layoutParams = ivLine
		// .getLayoutParams();
		// LENGTH = d.getWidth() / 2;
		// layoutParams.width = LENGTH;
		// ivLine.setLayoutParams(layoutParams);

		// WindowManager m = this.getWindowManager();
		// Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������
		//
		// LayoutInflater inflater = getLayoutInflater();
		// int h = d.getHeight();
		// nozixun = inflater.inflate(R.layout.layout_nozixun, null);

		// ���listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(MessageCenterActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(MessageCenterActivity.this,
				0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(true);// �ر�����ˢ��
		lvCollection.setCanLoadMore(false);// �򿪼��ظ���

		messageCenterAdapter = new MessageCenterAdapter(
				MessageCenterActivity.this);
		messageCenterAdapter.setOnTelClickListener(this, 0);
		lvCollection.setAdapter(messageCenterAdapter);

		lvCollection.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getMessage(UrlConfig.messagelistB_Http, logoCollection,
						application.getToken(), application.getUserId(), false);
			}
		});

		lvCollection.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				logoCollection = 1;
				getMessage(UrlConfig.messagelistB_Http, logoCollection,
						application.getToken(), application.getUserId(), true);

			}
		});

		logoCollection = 1;
		getMessage(UrlConfig.messagelistB_Http, logoCollection,
				application.getToken(), application.getUserId(), true);
		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.tv_route_tab_apply://
			break;
		default:
			break;
		}
	}

	/**
	 * �ҵ���Ϣ
	 */
	private void getMessage(String url, int page, String token, String userid,
			boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("page", page + "");
		map.put("apptype", "2");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MessageCenterActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MessageCenterActivity.this),
				HttpStaticApi.message_Http, null, loadedtype);
	}

	@Override
	public void onTelClick(int position, View v, int logo) {
		if (!"".equals(CollectionData.get(position).get("url")))
			headViewPagerOnItemOnClick(CollectionData.get(position)
					.get("title"), CollectionData.get(position).get("url"));

	}

	public void headViewPagerOnItemOnClick(String title, String url) {
		if (!"".equals(url)) {
			Intent intent = new Intent(MessageCenterActivity.this,
					WebBrowserActivity.class);
			String t = "";
			if ("".equals(title)) {
				t = "ϵͳ��Ϣ";
			} else {
				t = title;
			}
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, t);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
		} else {
		}
	}
}
