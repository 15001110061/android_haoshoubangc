package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.ConsultingPagerAdapter;
import com.cgzz.job.adapter.CurrentAdapter;
import com.cgzz.job.adapter.CurrentAdapter.OnCancelOrderClickListener;
import com.cgzz.job.adapter.CurrentAdapter.OnRouteClickListener;
import com.cgzz.job.adapter.CurrentAdapter.OnTelClickListener;
import com.cgzz.job.adapter.CurrentAdapter.OnTextClickListener;
import com.cgzz.job.adapter.FillOrdersAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomDialog;
import com.cgzz.job.view.CustomListView;
import com.cgzz.job.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.view.CustomListView.OnRefreshListener;

/***
 * @author wjm 主页：订单
 */
public class MainOrdersFragment extends BaseActivity implements
		OnClickListener, OnCancelOrderClickListener, OnRouteClickListener,
		OnTelClickListener, OnTextClickListener {
	private TextView tvCurrent, tvFinish;
	private ArrayList<Map<String, String>> shufflingList;
	private boolean isManul;
	private ViewPager viewpager;
	private ImageView ivLine;
	private int LENGTH;
	private CustomListView lvCurrent, lvFillOrders;
	private CurrentAdapter Currentadapter;
	private FillOrdersAdapter FillOrdersadapter;
	public static final int TYPE_TAB_Current = 0;
	public static final int TYPE_TAB_Finish = 1;
	private int currentIndicatorPosition;
	private int logoCurrent = 1;// 页码标识
	private int logoFillOrders = 1;// 页码标识
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.mylist_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						CurrentData.clear();
						logoCurrent = 2;
					} else {
						logoCurrent++;
					}

					bundle = ParserUtil.ParserMylist(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						lvCurrent.removeHeaderView(noorders);
						lvCurrent.setCanLoadMore(true);
					} else {

						if (!loadedtype) {
							lvCurrent.onLoadMorNodata();
						} else {
							lvCurrent.setCanLoadMore(false);
							lvCurrent.addHeaderView(noorders);

						}

					}
					Currentadapter.refreshMYData(CurrentData);
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
				case HttpStaticApi.FAILURE_HTTP:
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.Training_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						TrainingData.clear();
						logoFillOrders = 2;
					} else {
						logoFillOrders++;
					}

					bundle = ParserUtil.ParserMylist(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						lvFillOrders.setCanLoadMore(true);// 打开加载更多
						TrainingData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						lvFillOrders.removeHeaderView(nowanchengorders);
					} else {
						if (!loadedtype) {
							lvCurrent.onLoadMorNodata();
							// ToastUtil.makeShortText(MainOrdersFragment.this,
							// MainOrdersFragment.this.getResources()
							// .getString(R.string.http_nodata));
						} else {
							lvFillOrders.setCanLoadMore(false);// 打开加载更多
							lvFillOrders.addHeaderView(nowanchengorders);
							lvFillOrders.setAdapter(FillOrdersadapter);
						}

					}

					FillOrdersadapter.refreshMYData(TrainingData);

					lvFillOrders.onLoadMoreComplete();
					lvFillOrders.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
				case HttpStaticApi.FAILURE_HTTP:
					lvFillOrders.onLoadMoreComplete();
					lvFillOrders.onRefreshComplete();
					break;
				}
				break;
			case HttpStaticApi.arrive_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle
							.get("msg").toString());

					logoCurrent = 1;
					getMylist(UrlConfig.mylist_Http, application.getToken(),
							application.getUserId(), logoCurrent, true);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle
							.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.canarrive_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserCanarrive(data);

					String type = bundle.getString("type").toString();
					final String position = bundle.getString("position")
							.toString();
					String msg = bundle.getString("msg").toString();
					if ("1".equals(type)) {
						CustomDialog.alertDialog(MainOrdersFragment.this,
								false, true, true, null, msg,
								new CustomDialog.PopUpDialogListener() {

									@Override
									public void doPositiveClick(Boolean isOk) {
										if (isOk) {// 确定
											try {
												getArrive(

														UrlConfig.arrive_Http,
														application.getToken(),
														application.getUserId(),
														CurrentData
																.get(Integer
																		.parseInt(position))
																.get("orderdetailid"),
														true);
											} catch (Exception e) {
												// TODO: handle exception
											}

										} else {
										}

									}
								});
					} else if ("0".equals(type)) {
						CustomDialog.alertDialog2(MainOrdersFragment.this,
								false, true, false, null, msg,
								new CustomDialog.PopUpDialogListener() {

									@Override
									public void doPositiveClick(Boolean isOk) {
										if (isOk) {// 确定

										} else {

										}

									}
								});

					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					break;

				default:
					break;
				}
				break;

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_orders);
		initView();

	}

	View nolog, noorders, nowanchengorders;
	TextView tv_nolog_log, tv_noorders_orders;

	public int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	@SuppressLint("NewApi")
	private void initView() {
		releaseBroadcastReceiver();
		tvCurrent = (TextView) findViewById(R.id.tv_route_tab_invite);
		tvFinish = (TextView) findViewById(R.id.tv_route_tab_apply);
		tvCurrent.setOnClickListener(this);
		tvFinish.setOnClickListener(this);

		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ivLine = (ImageView) findViewById(R.id.iv_line);
		// 设置滑动线的宽度
		android.view.ViewGroup.LayoutParams layoutParams = ivLine
				.getLayoutParams();
		LENGTH = d.getWidth() / 2;
		layoutParams.width = LENGTH;
		ivLine.setLayoutParams(layoutParams);

		LayoutInflater inflater = getLayoutInflater();
		int h = d.getHeight();
		int px173 = getResources()
				.getDimensionPixelSize(R.dimen.dd_dimen_269px)
				+ getStatusHeight(MainOrdersFragment.this);//
		// 没有登录
		nolog = inflater.inflate(R.layout.layout_nolog, null);
		RelativeLayout rl_nologo_1 = (RelativeLayout) nolog
				.findViewById(R.id.rl_nologo_1);
		tv_nolog_log = (TextView) nolog.findViewById(R.id.tv_nolog_log);
		tv_nolog_log.setOnClickListener(this);
		rl_nologo_1.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - px173));

		// 没有当前的订单
		noorders = inflater.inflate(R.layout.layout_noorders, null);
		RelativeLayout rl_nologo_2 = (RelativeLayout) noorders
				.findViewById(R.id.rl_nologo_2);
		tv_noorders_orders = (TextView) noorders
				.findViewById(R.id.tv_noorders_orders);
		tv_noorders_orders.setOnClickListener(this);

		rl_nologo_2.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - px173));
		// 没有完成的订单
		nowanchengorders = inflater.inflate(R.layout.layout_nowanchengorders,
				null);
		RelativeLayout rl_nologo_3 = (RelativeLayout) nowanchengorders
				.findViewById(R.id.rl_nologo_3);
		rl_nologo_3.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - px173));

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCurrent = new CustomListView(MainOrdersFragment.this);
		lvCurrent.setFadingEdgeLength(0);
		lvCurrent.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCurrent.setDividerHeight(Utils.dip2px(MainOrdersFragment.this, 0));
		lvCurrent.setFooterDividersEnabled(false);
		lvCurrent.setCanRefresh(true);// 关闭下拉刷新
		lvCurrent.setCanLoadMore(false);// 打开加载更多
		Currentadapter = new CurrentAdapter(MainOrdersFragment.this);

		Currentadapter.setOnCancelOrderClickListener(this, 0);
		Currentadapter.setOnRouteClickListener(this, 0);
		Currentadapter.setOnTelClickListener(this, 0);
		Currentadapter.setOnTextClickListener(this, 0);
		lvCurrent.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				getMylist(UrlConfig.mylist_Http, application.getToken(),
						application.getUserId(), logoCurrent, false);
			}
		});

		lvCurrent.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvCurrent.removeHeaderView(noorders);
				logoCurrent = 1;
				getMylist(UrlConfig.mylist_Http, application.getToken(),
						application.getUserId(), logoCurrent, true);

			}
		});

		lvFillOrders = new CustomListView(MainOrdersFragment.this);
		lvFillOrders.setFadingEdgeLength(0);
		lvFillOrders
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvFillOrders.setDividerHeight(Utils.dip2px(MainOrdersFragment.this, 0));
		lvFillOrders.setFooterDividersEnabled(false);
		lvFillOrders.setCanRefresh(true);// 关闭下拉刷新
		lvFillOrders.setCanLoadMore(false);// 打开加载更多
		FillOrdersadapter = new FillOrdersAdapter(MainOrdersFragment.this);

		lvFillOrders.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				getTraining(UrlConfig.mylist_Http, application.getToken(),
						application.getUserId(), logoFillOrders, false);
			}
		});

		lvFillOrders.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvFillOrders.removeHeaderView(nowanchengorders);

				logoFillOrders = 1;
				getTraining(UrlConfig.mylist_Http, application.getToken(),
						application.getUserId(), logoFillOrders, true);
			}
		});

		if (!application.isLogon()) {
			lvCurrent.setCanRefresh(false);
			lvFillOrders.setCanRefresh(false);
			lvCurrent.addHeaderView(nolog);
			lvFillOrders.addHeaderView(nolog);
		} else {

			logoCurrent = 1;
			logoFillOrders = 1;
			getMylist(UrlConfig.mylist_Http, application.getToken(),
					application.getUserId(), logoCurrent, true);
			getTraining(UrlConfig.mylist_Http, application.getToken(),
					application.getUserId(), logoFillOrders, true);
		}
		lvCurrent.setAdapter(Currentadapter);
		lvFillOrders.setAdapter(FillOrdersadapter);
		listviews.add(lvCurrent);
		listviews.add(lvFillOrders);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOnPageChangeListener(new ConsultingViewpagerPort());
		switchTabText(TYPE_TAB_Current);
	}

	/**
	 * 切换标签
	 * 
	 * @param type
	 */

	private void switchTabText(int type) {
		switch (type) {
		case TYPE_TAB_Current:
			tvCurrent.setEnabled(false);
			tvFinish.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 0));
			currentIndicatorPosition = LENGTH * 0;
			tvCurrent.setTextColor(this.getResources().getColor(
					R.color.common_FF971E));
			tvFinish.setTextColor(this.getResources().getColor(
					R.color.common_text));
			// if (CurrentData.size() > 0) {
			// include_noorders.setVisibility(View.GONE);
			// include_nowanchengorders.setVisibility(View.GONE);
			// } else {
			// include_noorders.setVisibility(View.VISIBLE);
			// include_nowanchengorders.setVisibility(View.GONE);
			// }

			break;
		case TYPE_TAB_Finish:
			tvCurrent.setEnabled(true);
			tvFinish.setEnabled(false);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 1));
			currentIndicatorPosition = LENGTH * 1;
			tvCurrent.setTextColor(this.getResources().getColor(
					R.color.common_text));
			tvFinish.setTextColor(this.getResources().getColor(
					R.color.common_FF971E));

			// if (TrainingData.size() > 0) {
			// include_noorders.setVisibility(View.GONE);
			// include_nowanchengorders.setVisibility(View.GONE);
			// } else {
			// include_noorders.setVisibility(View.GONE);
			// include_nowanchengorders.setVisibility(View.VISIBLE);
			// }
			break;
		}
	}

	/**
	 * 获取一个平移的动画
	 */
	private Animation getAnimation(float fromXValue, float toXValue) {
		Animation animation = new TranslateAnimation(fromXValue, toXValue, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(200);
		return animation;
	}

	class ConsultingViewpagerPort implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			if (!isManul) {
				switchTabText(arg0);
			} else {
				isManul = false;
			}

		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_route_tab_invite:// 新闻
			isManul = true;
			switchTabText(TYPE_TAB_Current);
			break;
		case R.id.tv_route_tab_apply:// 培训
			isManul = true;
			switchTabText(TYPE_TAB_Finish);
			break;

		case R.id.tv_nolog_log:// 登录

			startActivity(new Intent(MainOrdersFragment.this,
					LoginActivity.class));
			finish();

			break;

		case R.id.tv_noorders_orders:// 首页
			startActivity(new Intent(MainOrdersFragment.this,
					TabMainActivity.class));
			finish();

			break;

		default:
			break;
		}
	}

	/**
	 * 当前订单
	 */
	private void getMylist(String url, String token, String userid, int page,
			boolean loadedtype) {
		url = url + "?apptype=2&token=" + token + "&userid=" + userid
				+ "&page=" + page + "&type=1";
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				MainOrdersFragment.this, url, null, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this),
				HttpStaticApi.mylist_Http, null, loadedtype);
	}

	/**
	 * 完成订单
	 */
	private void getTraining(String url, String token, String userid, int page,
			boolean loadedtype) {
		url = url + "?apptype=2&token=" + token + "&userid=" + userid
				+ "&page=" + page + "&type=2";
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				MainOrdersFragment.this, url, null, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this),
				HttpStaticApi.Training_Http, null, loadedtype);

	}

	/**
	 * 确认到达接口
	 */
	private void getArrive(String url, String token, String userid,
			String orderdetailid, boolean loadedtype) {

		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderdetailid", orderdetailid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this),
				HttpStaticApi.arrive_Http, null, loadedtype);

	}

	/**
	 * 能否确认到达接口
	 */
	private void canarrive(String url, String token, String userid,
			String orderdetailid, String position, boolean loadedtype) {

		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderdetailid);
		map.put("position", position);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this),
				HttpStaticApi.canarrive_Http, null, loadedtype);

	}

	/**
	 * 取消订单
	 */
	@Override
	public void onCancelOrderClick(int position, View v, int logo) {
	
		if ("1".equals(CurrentData.get(position).get("status"))) {
			ToastUtil
					.makeShortText(MainOrdersFragment.this, "对方已确认订单信息,不能取消订单");
		} else {
			Intent intent = new Intent(MainOrdersFragment.this,
					CancelReasonActivity.class);
			intent.putExtra("orderdetailid",
					CurrentData.get(position).get("orderdetailid"));
			startActivityForResult(intent, 1);
		}

	}

	// 回调方法，从第二个页面回来的时候会执行这个方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String isquxiao = data.getStringExtra("isquxiao");
			// 根据上面发送过去的请求吗来区别
			switch (requestCode) {
			case 1:
				if ("y".equals(isquxiao)) {
					logoCurrent = 1;
					getMylist(UrlConfig.mylist_Http, application.getToken(),
							application.getUserId(), logoCurrent, true);
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 订单详情
	 */
	@Override
	public void onRouteClick(int position, View v, int logo) {

		Intent intent = new Intent(MainOrdersFragment.this,
				OrdersFeedbackActivity.class);

		intent.putExtra("orderid", CurrentData.get(position).get("orderid"));

		intent.putExtra("type", "0");

		startActivity(intent);

	}

	/***
	 * 能否确认到达接口
	 */
	@Override
	public void onTelClick(final int position, View v, int logo) {
		canarrive(UrlConfig.canarrive_Http, application.getToken(),
				application.getUserId(),
				CurrentData.get(position).get("orderid"), position + "", true);

	}

	/***
	 * 发短信
	 */
	@Override
	public void onTextClick(int position, View v, int logo) {
		// ToastUtil.makeShortText(MainOrdersFragment.this, "发短信" + position);
		// Utils.texts(getResources().getString(R.string.call_customer),
		// MainOrdersFragment.this);

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
				Utils.call(tel, MainOrdersFragment.this);

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

	OBDBroadcastReceiver recobdlist;

	private void releaseBroadcastReceiver() {
		recobdlist = new OBDBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cgzz.job.accesstype");
		registerReceiver(recobdlist, filter);
	}

	private class OBDBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String TYPE = bundle.getString("TYPE");
			if ("refreshOrders".equals(TYPE)) {
				String refresh = bundle.getString("refresh");
				if ("1".equals(refresh)) {
					logoCurrent = 1;
					logoFillOrders = 1;
					getMylist(UrlConfig.mylist_Http, application.getToken(),
							application.getUserId(), logoCurrent, true);
					getTraining(UrlConfig.mylist_Http, application.getToken(),
							application.getUserId(), logoFillOrders, true);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (recobdlist != null)
			unregisterReceiver(recobdlist);

		super.onDestroy();
	}

}
