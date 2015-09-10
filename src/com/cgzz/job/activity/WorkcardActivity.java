package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.BaseActivity.BitmapCache;
import com.cgzz.job.adapter.WorkcardAdapter;
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
import com.cgzz.job.view.Mycamera;

public class WorkcardActivity extends BaseActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener,
		AnimationListener {

	TextView tv_workcard_qiehuan, tv_workcad_spin;

	private ViewGroup mContainer;
	private CustomListView cl_workcad_listView;
	private int logoCurrent = 1;// 页码标识
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> shufflingList;

	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.record_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						CurrentData.clear();
						logoCurrent = 2;
					} else {
						logoCurrent++;
					}

					bundle = ParserUtil.ParserRecord(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {
						cl_workcad_listView.setCanLoadMore(true);// 打开加载更多
						cl_workcad_listView.removeHeaderView(nozixun);
						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
					} else {
						if (!loadedtype) {
							ToastUtil.makeShortText(WorkcardActivity.this,
									WorkcardActivity.this.getResources()
											.getString(R.string.http_nodata));
						} else {
							cl_workcad_listView.addHeaderView(nozixun);
							cl_workcad_listView.setAdapter(Workcardadapter);
							cl_workcad_listView.setCanLoadMore(false);// 打开加载更多
						}
					}

					Workcardadapter.refreshMYData(CurrentData);
					cl_workcad_listView.onLoadMoreComplete();
					cl_workcad_listView.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					cl_workcad_listView.onLoadMoreComplete();
					cl_workcad_listView.onRefreshComplete();
					break;
				}
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_workcard);
		setTitle("我的工牌", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
				TITLE_TYPE_TEXT, "切换");
		initView();
		Assignment();
	}

	WorkcardAdapter Workcardadapter;
	RelativeLayout rl_workcad;
	// LinearLayout ll_workcad;
	View view1, view2;
	LinearLayout llLeft, llright;
	com.cgzz.job.view.CustomerRatingBarGo cb_workcard_car;
	ImageView iv_workcard_touxiang;
	TextView tv_workcard_name, tv_workcard_age, tv_workcard_gongling,
			tv_workcard_bangkehao;
	View nozixun;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		//
		cb_workcard_car = (com.cgzz.job.view.CustomerRatingBarGo) findViewById(R.id.cb_workcard_car);
		iv_workcard_touxiang = (ImageView) findViewById(R.id.iv_workcard_touxiang);
		tv_workcard_name = (TextView) findViewById(R.id.tv_workcard_name);
		tv_workcard_age = (TextView) findViewById(R.id.tv_workcard_age);
		tv_workcard_gongling = (TextView) findViewById(R.id.tv_workcard_gongling);
		tv_workcard_bangkehao = (TextView) findViewById(R.id.tv_workcard_bangkehao);
		//
		view1 = (RelativeLayout) findViewById(R.id.ll_workcad);
		// tv_workcard_qiehuan = (TextView)
		// findViewById(R.id.tv_workcard_qiehuan);
		// tv_workcard_qiehuan.setOnClickListener(this);
		//
		// tv_workcad_spin = (TextView) findViewById(R.id.tv_workcad_spin);
		// tv_workcad_spin.setOnClickListener(this);
		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		int h = d.getHeight();
		LayoutInflater inflater = getLayoutInflater();
		nozixun = inflater.inflate(R.layout.layout_nozixuns, null);
		RelativeLayout rl_nologo_4 = (RelativeLayout) nozixun
				.findViewById(R.id.rl_nologo_4);
		rl_nologo_4.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - 505));

		view2 = (RelativeLayout) findViewById(R.id.ll_workcad2);
		// view1 = ll_workcad;
		// view2 = rl_workcad;
		cl_workcad_listView = (CustomListView) findViewById(R.id.cl_workcad_listView);
		mContainer = (ViewGroup) findViewById(R.id.container);
		cl_workcad_listView.setOnItemClickListener(this);
		cl_workcad_listView.setDivider(getResources().getDrawable(
				R.color.common_grey));
		cl_workcad_listView.setDividerHeight(Utils.dip2px(
				WorkcardActivity.this, 0));
		cl_workcad_listView.setFooterDividersEnabled(false);

		cl_workcad_listView.setCanRefresh(true);// 关闭下拉刷新
		cl_workcad_listView.setCanLoadMore(false);// 打开加载更多
		Workcardadapter = new WorkcardAdapter(WorkcardActivity.this);
		cl_workcad_listView.setAdapter(Workcardadapter);

		cl_workcad_listView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getRecord(UrlConfig.record_Http, "2", application.getToken(),
						application.getUserId(), logoCurrent + "", false);
			}
		});

		cl_workcad_listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				cl_workcad_listView.removeHeaderView(nozixun);
				logoCurrent = 1;
				getRecord(UrlConfig.record_Http, "2", application.getToken(),
						application.getUserId(), logoCurrent + "", true);

			}
		});
		logoCurrent = 1;
		getRecord(UrlConfig.record_Http, "2", application.getToken(),
				application.getUserId(), logoCurrent + "", true);

	}

	// public ImageLoader mImageLoader;
	private void Assignment() {
		// mImageLoader = new ImageLoader(
		// GlobalVariables.getRequestQueue(this), new BitmapCache());
		cb_workcard_car
				.setProgress(Integer.parseInt(application.getStarlevel()));
		// 头像
		try {
			ImageListener listener = ImageLoader.getImageListener(
					iv_workcard_touxiang, R.drawable.icon_touxiangmoren2,
					R.drawable.icon_touxiangmoren2);
			mImageLoader.get(application.getFaceUrl(), listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		// name
		tv_workcard_name.setText(application.getRealname());
		// age
		tv_workcard_age.setText(application.getAge());
		// 工龄
		tv_workcard_gongling.setText(application.getWorkage() + "年");
		// 帮客号
		tv_workcard_bangkehao.setText(application.getUserno());

	}

	/**
	 * 12、完成酒店订单记录接口
	 */
	private void getRecord(String url, String apptype, String token,
			String userid, String page, boolean loadedtype) {
		url = url + "?apptype=2&token=" + token + "&userid=" + userid
				+ "&page=" + page;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				WorkcardActivity.this, url, null, callBack,
				GlobalVariables.getRequestQueue(WorkcardActivity.this),
				HttpStaticApi.record_Http, null, loadedtype);
	}

	// private void applyRotation(int position, float start, float end) {
	// // 计算中心点
	// final float centerX = mContainer.getWidth() / 2.0f;
	// final float centerY = mContainer.getHeight() / 2.0f;
	//
	// final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
	// centerX, centerY, 0.0f, true);
	// rotation.setDuration(500);
	// rotation.setFillAfter(true);
	// rotation.setInterpolator(new AccelerateInterpolator());
	// // 设置监听
	// rotation.setAnimationListener(new DisplayNextView(position));
	//
	// mContainer.startAnimation(rotation);
	// }

	// 点击图像时，返回listview
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.tv_workcard_qiehuan:
		// // applyRotation(2, 0, 90);
		//
		// // break;
		//
		// case R.id.tv_workcad_spin:
		// View animView = view1.getVisibility() == View.VISIBLE ? view1
		// : view2;
		// Mycamera animation = new Mycamera(true);
		// animation.setAnimationListener(this);
		// animView.startAnimation(animation);
		//
		// // applyRotation(-1, 180, 90);
		// break;
		case R.id.ll_title_right:
			// applyRotation(2, 0, 90);

			// if(view1.getVisibility()==0){
			View animView = view1.getVisibility() == View.VISIBLE ? view1
					: view2;
			Mycamera animation = new Mycamera(true);
			animation.setAnimationListener(this);
			animView.startAnimation(animation);

			break;
		case R.id.ll_title_left:

			finish();
			break;
		default:
			break;
		}

	}

	// private final class DisplayNextView implements
	// Animation.AnimationListener {
	// private final int mPosition;
	//
	// private DisplayNextView(int position) {
	// mPosition = position;
	// }
	//
	// public void onAnimationStart(Animation animation) {
	// }
	//
	// // 动画结束
	// public void onAnimationEnd(Animation animation) {
	// mContainer.post(new SwapViews(mPosition));
	// }
	//
	// public void onAnimationRepeat(Animation animation) {
	// }
	// }

	// private final class SwapViews implements Runnable {
	// private final int mPosition;
	//
	// public SwapViews(int position) {
	// mPosition = position;
	// }
	//
	// public void run() {
	// final float centerX = mContainer.getWidth() / 2.0f;
	// final float centerY = mContainer.getHeight() / 2.0f;
	// Rotate3dAnimation rotation;
	//
	// if (mPosition > -1) {
	// // 显示ImageView
	// view1.setVisibility(View.GONE);
	// view2.setVisibility(View.VISIBLE);
	// view2.requestFocus();
	//
	// rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 0.0f,
	// false);
	// } else {
	// // 返回listview
	// view2.setVisibility(View.GONE);
	// view1.setVisibility(View.VISIBLE);
	// view1.requestFocus();
	//
	// rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 0.0f,
	// false);
	// }
	//
	// rotation.setDuration(500);
	// rotation.setFillAfter(true);
	// rotation.setInterpolator(new DecelerateInterpolator());
	// // 开始动画
	// mContainer.startAnimation(rotation);
	// }
	// }

	@Override
	public void onAnimationEnd(Animation arg0) {
		View nextView;

		if (view1.getVisibility() == View.VISIBLE) {
			nextView = view2;
			view1.setVisibility(View.INVISIBLE);
		} else {
			nextView = view1;
			view2.setVisibility(View.INVISIBLE);
		}
		nextView.setVisibility(View.VISIBLE);
		nextView.startAnimation(new Mycamera(false));

	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}