package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.ConsultingPagerAdapter;
import com.cgzz.job.adapter.NewsAdapter;
import com.cgzz.job.adapter.TrainingAdapter;
import com.cgzz.job.adapter.ViewPagerAdapter;
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
 * @author wjm ��ҳ����ѯ
 */
public class MainConsultingFragment extends BaseActivity implements
		OnClickListener, OnPageChangeListener, OnItemClickListener {
	private ViewPager viewpagerHeader;
	private final int GETDATAMessage = 101;
	private final int EmptyMessage = 102;
	private TextView tvNews, tvTraining;
	private ScheduledExecutorService scheduledExecutorService;
	private ImageLoader mImageLoader;
	private ArrayList<ImageView> mViews;
	private LinearLayout addScroll;
	private ArrayList<Map<String, String>> shufflingList;
	private boolean isRunThread = true;
	private boolean mark = true;
	private int currentIndex;
	private boolean isManul;
	private ViewPager viewpager;
	private ImageView ivLine;
	private int LENGTH;
	private CustomListView lvNews, lvTraining;
	private NewsAdapter Newsadapter;
	private TrainingAdapter Trainingadapter;
	public static final int TYPE_TAB_INVITE = 0;
	public static final int TYPE_TAB_APPLY = 1;
	public static final int TYPE_TAB_BEING = 2;
	public static final int TYPE_TAB_DONE = 3;
	private int currentIndicatorPosition;
	private int logoCONDUCT = 1;// ҳ���ʶ
	private int logoTraining = 1;// ҳ���ʶ
	private ArrayList<Map<String, String>> NewsData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> fromPassengerMaps;
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {

			case HttpStaticApi.carousel_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserCarousel(data);
					fromPassengerMaps = (List<Map<String, String>>) bundle
							.getSerializable("list");
				
					if (fromPassengerMaps.size() > 0) {
						viewpagerHeader.setVisibility(View.VISIBLE);
						addScroll.setVisibility(View.VISIBLE);
						try {
							new Thread(new Runnable() {
								@Override
								public void run() {

									Message msg = handler.obtainMessage();
									msg.obj = fromPassengerMaps;
									msg.what = GETDATAMessage;
									handler.sendMessage(msg);
								}
							}).start();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				}
				break;

			case HttpStaticApi.consulting_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
				
					if (loadedtype) {
						NewsData.clear();
						logoCONDUCT = 2; 
					} else {
						logoCONDUCT++;
					}

					bundle = ParserUtil.ParserConsulting_Training(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						lvNews.setCanLoadMore(true);// �򿪼��ظ���
						NewsData.addAll((ArrayList<Map<String, String>>) bundle
								.getSerializable("list"));
						lvNews.removeHeaderView(nozixun);
					} else {

						if (!loadedtype) {
							lvNews.onLoadMorNodata();
//							ToastUtil.makeShortText(
//									MainConsultingFragment.this,
//									MainConsultingFragment.this.getResources()
//											.getString(R.string.http_nodata));
						} else {
							lvNews.addHeaderView(nozixun);
						lvNews.setAdapter(Newsadapter);
							lvNews.setCanLoadMore(false);// �򿪼��ظ���
						}

					}

					Newsadapter.refreshMYData(NewsData);
					lvNews.onLoadMoreComplete();
					lvNews.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvNews.onLoadMoreComplete();
					lvNews.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.training_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						TrainingData.clear();
						logoTraining = 2;
					} else {
						logoTraining++;
					}

					bundle = ParserUtil.ParserConsulting_Training(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						lvTraining.setCanLoadMore(true);// �򿪼��ظ���
						TrainingData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						lvTraining.removeHeaderView(nozixun);
					} else {

						if (!loadedtype) {
							lvNews.onLoadMorNodata();
//							ToastUtil.makeShortText(
//									MainConsultingFragment.this,
//									MainConsultingFragment.this.getResources()
//											.getString(R.string.http_nodata));
						} else {
							lvTraining.addHeaderView(nozixun);
							lvTraining.setAdapter(Trainingadapter);
							lvTraining.setCanLoadMore(false);// �򿪼��ظ���
						}

					}

					Trainingadapter.refreshMYData(TrainingData);
					lvTraining.onLoadMoreComplete();
					lvTraining.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvTraining.onLoadMoreComplete();
					lvTraining.onRefreshComplete();
					break;
				}
				break;

			}
		}
	};

	private class ViewPagerTask implements Runnable {

		@Override
		public void run() {
			handler.sendEmptyMessage(EmptyMessage);
		}
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETDATAMessage:
				fromPassengerMaps = (ArrayList<Map<String, String>>) msg.obj;
				if (fromPassengerMaps != null && fromPassengerMaps.size() > 0) {
					mImageLoader = new ImageLoader(
							GlobalVariables
									.getRequestQueue(MainConsultingFragment.this),
							new BitmapCache());
					mViews = new ArrayList<ImageView>();
					initImg(fromPassengerMaps);
					setAdapter();
					if (fromPassengerMaps.size() > 1) {
						scheduledExecutorService = Executors
								.newSingleThreadScheduledExecutor();
						// �趨ִ���̼߳ƻ�,��ʼ4s�ӳ�,ÿ��������ɺ��ӳ�3s��ִ��һ������
						scheduledExecutorService.scheduleWithFixedDelay(
								new ViewPagerTask(), 4, 3, TimeUnit.SECONDS);
					}
				} else {

				}
				break;

			case EmptyMessage:
				if (mark) {
					currentIndex++;
					viewpagerHeader.setCurrentItem(currentIndex, true);
				}
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_consulting);
//		setTitle("���ְ�", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
//				TITLE_TYPE_TEXT, "ע��");
		initView();
		carousel(UrlConfig.carousel_Http, "2", true);

	}

	View nozixun;

	@SuppressLint("NewApi")
	private void initView() {
		// ͼƬ��ѯ
		viewpagerHeader = (ViewPager) findViewById(R.id.vp);
		addScroll = (LinearLayout) findViewById(R.id.ll_scroll);
		tvNews = (TextView) findViewById(R.id.tv_route_tab_invite);
		tvTraining = (TextView) findViewById(R.id.tv_route_tab_apply);
		tvNews.setOnClickListener(this);
		tvTraining.setOnClickListener(this);

		// �·��б�
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ivLine = (ImageView) findViewById(R.id.iv_line);
		// ���û����ߵĿ��
		android.view.ViewGroup.LayoutParams layoutParams = ivLine
				.getLayoutParams();
		LENGTH = d.getWidth() / 2;
		layoutParams.width = LENGTH;
		ivLine.setLayoutParams(layoutParams);

		LayoutInflater inflater = getLayoutInflater();
		int h = d.getHeight();
		nozixun = inflater.inflate(R.layout.layout_nozixun, null);
		RelativeLayout rl_nologo_4 = (RelativeLayout) nozixun
				.findViewById(R.id.rl_nologo_4);
		rl_nologo_4.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - 505));

		// ���listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvNews = new CustomListView(MainConsultingFragment.this);
		lvNews.setFadingEdgeLength(0);
		lvNews.setDivider(getResources().getDrawable(R.color.common_grey));
		lvNews.setDividerHeight(Utils.dip2px(MainConsultingFragment.this, 0));
		lvNews.setFooterDividersEnabled(false);

		lvNews.setCanRefresh(true);// �ر�����ˢ��
		lvNews.setCanLoadMore(false);// �򿪼��ظ���
		Newsadapter = new NewsAdapter(MainConsultingFragment.this);
		lvNews.setAdapter(Newsadapter);
		lvNews.setOnItemClickListener(this);

		lvNews.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getConsulting(UrlConfig.Consulting_Training_Http, logoCONDUCT,
						false);
			}
		});

		lvNews.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvNews.removeHeaderView(nozixun);
				logoCONDUCT = 1;
				getConsulting(UrlConfig.Consulting_Training_Http, logoCONDUCT,
						true);

			}
		});

		lvTraining = new CustomListView(MainConsultingFragment.this);
		lvTraining.setFadingEdgeLength(0);
		lvTraining.setDivider(getResources().getDrawable(R.color.common_grey));
		lvTraining.setDividerHeight(Utils
				.dip2px(MainConsultingFragment.this, 0));
		lvTraining.setFooterDividersEnabled(false);
		lvTraining.setCanRefresh(true);// �ر�����ˢ��
		lvTraining.setCanLoadMore(false);// �򿪼��ظ���
		Trainingadapter = new TrainingAdapter(MainConsultingFragment.this);
		lvTraining.setAdapter(Trainingadapter);
		lvTraining.setOnItemClickListener(this);
		lvTraining.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getTraining(UrlConfig.Consulting_Training_Http, logoTraining,
						false);
			}
		});

		lvTraining.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvTraining.removeHeaderView(nozixun);
				logoTraining = 1;
				getTraining(UrlConfig.Consulting_Training_Http, logoTraining,
						true);
			}
		});
		logoCONDUCT = 1;
		logoTraining = 1;
		getConsulting(UrlConfig.Consulting_Training_Http, logoCONDUCT, true);
		getTraining(UrlConfig.Consulting_Training_Http, logoTraining, true);
		listviews.add(lvNews);
		listviews.add(lvTraining);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOnPageChangeListener(new ConsultingViewpagerPort());
		switchTabText(TYPE_TAB_INVITE);
		//

	}

	/**
	 * �л���ǩ
	 * 
	 * @param type
	 */

	private void switchTabText(int type) {
		switch (type) {
		case TYPE_TAB_INVITE:
			tvNews.setEnabled(false);
			tvTraining.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 0));
			currentIndicatorPosition = LENGTH * 0;
			tvNews.setTextColor(this.getResources().getColor(
					R.color.common_FF971E));
			tvTraining.setTextColor(this.getResources().getColor(
					R.color.common_text));
			break;
		case TYPE_TAB_APPLY:
			tvNews.setEnabled(true);
			tvTraining.setEnabled(false);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 1));
			currentIndicatorPosition = LENGTH * 1;
			tvNews.setTextColor(this.getResources().getColor(
					R.color.common_text));
			tvTraining.setTextColor(this.getResources().getColor(
					R.color.common_FF971E));
			break;
		// case TYPE_TAB_BEING:
		// tvNews.setEnabled(true);
		// tvTraining.setEnabled(true);
		// viewpager.setCurrentItem(type);
		// ivLine.startAnimation(getAnimation(currentIndicatorPosition,
		// LENGTH * 2));
		// currentIndicatorPosition = LENGTH * 2;
		// break;
		// case TYPE_TAB_DONE:
		// tvNews.setEnabled(true);
		// tvTraining.setEnabled(true);
		// viewpager.setCurrentItem(type);
		// ivLine.startAnimation(getAnimation(currentIndicatorPosition,
		// LENGTH * 3));
		// currentIndicatorPosition = LENGTH * 3;
		// break;
		}
	}

	/**
	 * ��ȡһ��ƽ�ƵĶ���
	 */
	private Animation getAnimation(float fromXValue, float toXValue) {
		Animation animation = new TranslateAnimation(fromXValue, toXValue, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(200);
		return animation;
	}

	/**
	 * ViewPager������
	 */

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}

			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}

	public void headViewPagerOnItemOnClick(String title, String url) {
		if (!"".equals(url)) {
			Intent intent = new Intent(MainConsultingFragment.this,
					WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, title);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
		} else {
		}
	}

	@Override
	public void onDestroy() {

		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();// �������е��߳�
		}
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}
		mark = false;
		super.onDestroy();
	}

	private boolean isStop = false;

	@Override
	public void onStop() {
		mark = false;
		super.onStop();
		if (scheduledExecutorService != null) {// ���治�ɼ��������е��ֲ�
			scheduledExecutorService.shutdown();// �������е��߳�
		}
		isStop = true;
	}

	@Override
	public void onResume() {
		mark = true;
		super.onResume();
		if (shufflingList != null && shufflingList.size() > 1) {// ����ɼ���ʱ���ֲ�
			if (isStop) {// �򵥵��Ż�������ִ����stop�ſ������ֲ�ͼ���¹���
				scheduledExecutorService = Executors
						.newSingleThreadScheduledExecutor();
				// �趨ִ���̼߳ƻ�,��ʼ4s�ӳ�,ÿ��������ɺ��ӳ�3s��ִ��һ������
				scheduledExecutorService.scheduleWithFixedDelay(
						new ViewPagerTask(), 4, 3, TimeUnit.SECONDS);
			}
		}
		isStop = false;
	}

	private ViewPagerAdapter viewPagerAdaPter;

	public void initImg(List<Map<String, String>> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		int size = (list.size() == 1) ? 1 : list.size() + 2;// ���ֻ��һ������Ҫ��ѯ
		// �жϼ��ϵĴ�С������imageview
		for (int i = 0; i < size; i++) {
			ImageView imageView = new ImageView(MainConsultingFragment.this);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			mViews.add(imageView);
		}
		int length = mViews.size();
		setImg(length, list);
	}

	public void setImg(int length, List<Map<String, String>> list) {
		int lengths = (length == 1) ? 1 : length - 2;// �жϵ�ǰ�м���ImageView�����ֻ��һ������Ҫ��ѯ
		int index = 0;
		// �����ImageVIew����
		for (int i = 0; i < length; i++) {
			if (i < lengths) {// �����һ��image��ֱ�Ӹ�ֵ������ж���͸���0�����ĽǱ겻��Ҫ��ֵ
				index = (list.size() == 1) ? i : i + 1;

				Map<String, String> map = list.get(i);
				// ����
				final String title = map.get("remark");
				// ·��
				String req_url_path = map.get("dict_param");
				final String url = req_url_path;
				try {
					ImageListener listener = ImageLoader.getImageListener(
							mViews.get(index), R.drawable.icon_loadfailed_bg,
							R.drawable.icon_loadfailed_bg);
					mImageLoader.get(map.get("dict_value"), listener);
				} catch (Exception e) {
				}
				mViews.get(index).setOnTouchListener(new OnTouchListener() {

					private long downTime;

					@Override
					public boolean onTouch(View vw, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							mark = false;
							downTime = System.currentTimeMillis();
							break;

						case MotionEvent.ACTION_CANCEL:
							if (isRunThread) {// ��ֹ���ڻ�����ʱ����Ŀ��ѯ
								isRunThread = false;
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isRunThread = true;
										mark = true;
									}
								}).start();
							}
							break;
						case MotionEvent.ACTION_UP:
							if (System.currentTimeMillis() - downTime < 500) {
								// ��ǰ��View�ĵ���¼�����д������
								headViewPagerOnItemOnClick(title, url);
							}
							mark = true;
							break;
						}
						return true;
					}
				});

				if (mViews.size() > 1) {// ��ͼƬdayu���ŵ�ʱ��
					View view = new View(MainConsultingFragment.this);
					LayoutParams lay = new LayoutParams(8, 8);
					lay.leftMargin = 7;
					view.setBackgroundResource(R.drawable.select_radio);
					view.setLayoutParams(lay);
					view.setEnabled(false);
					addScroll.addView(view);
				}
			}
		}
		if (lengths == 1) {
			return;
		}
		Map<String, String> mapAfter = list.get(list.size() - 1);// ����ͼ��������ͼ���ñ���
		ImageListener listenerAfter = ImageLoader.getImageListener(
				mViews.get(0), R.drawable.icon_loadfailed_bg,
				R.drawable.icon_loadfailed_bg);
		if (mapAfter.get("dict_value") != null)
			try {
				mImageLoader.get(mapAfter.get("dict_value"), listenerAfter);
			} catch (Exception e) {
				// TODO: handle exception
			}
		

		Map<String, String> mapFirst = list.get(0);// ����ͼ����ǰ���ͼ���ñ���

		ImageListener listenerFirst = ImageLoader.getImageListener(
				mViews.get(length - 1), R.drawable.icon_loadfailed_bg,
				R.drawable.icon_loadfailed_bg);
		if (mapFirst.get("dict_value") != null)
			try {
				mImageLoader.get(mapFirst.get("dict_value"), listenerFirst);
			} catch (Exception e) {
				// TODO: handle exception
			}
		
	}

	public void setAdapter() {
		viewPagerAdaPter = new ViewPagerAdapter(mViews);
		viewpagerHeader.setAdapter(viewPagerAdaPter);
		viewpagerHeader.setOnPageChangeListener(this);
		viewpagerHeader.setCurrentItem((mViews.size() == 1) ? 0 : 1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mViews == null || mViews.size() <= 1) {
			return;
		}
		// ����ط�����������ѭ��
		if (arg1 == 0.0) {
			if (arg0 == 0) {
				viewpagerHeader.setCurrentItem(mViews.size() - 2, false);
			} else if (arg0 == mViews.size() - 1) {
				viewpagerHeader.setCurrentItem(1, false);
			}
		}// �������ж�����Ϊ��ʱ�򻬶���ʱ�����ز���ȫ��
		else if (arg1 > 0.98 && arg0 == mViews.size() - 2) {
			viewpagerHeader.setCurrentItem(1, false);
		} else if (arg0 == 0 && arg1 < 0.00999) {
			viewpagerHeader.setCurrentItem(mViews.size() - 2, false);
		}
	}

	@Override
	public void onPageSelected(int position) {

		currentIndex = position;
		int childCount = addScroll.getChildCount();
		if (mViews.size() > 1) {// ҳ��ı�Ķ�СԲ��
			for (int i = 0; i < childCount; i++) {
				View view = addScroll.getChildAt(i);
				view.setEnabled(false);
			}
			if (position == 0) {
				View views = addScroll.getChildAt(mViews.size() - 3);
				if (views != null) {
					views.setEnabled(true);
				}
			} else if (position == mViews.size() - 1) {
				View views = addScroll.getChildAt(0);
				if (views != null) {
					views.setEnabled(true);
				}
			} else {
				View views = addScroll.getChildAt(position - 1);
				if (views != null) {
					views.setEnabled(true);
				}
			}
		}

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
		case R.id.tv_route_tab_invite:// ����
			isManul = true;
			switchTabText(TYPE_TAB_INVITE);
			break;
		case R.id.tv_route_tab_apply:// ��ѵ
			isManul = true;
			switchTabText(TYPE_TAB_APPLY);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			if (arg0 == lvNews) {
				if (NewsData != null && NewsData.get(arg2 - 1) != null) {

					headViewPagerOnItemOnClick(
							NewsData.get(arg2 - 1).get("title"),
							NewsData.get(arg2 - 1).get("url"));
				}
			} else if (arg0 == lvTraining) {
				if (TrainingData != null && TrainingData.get(arg2 - 1) != null) {
					headViewPagerOnItemOnClick(
							TrainingData.get(arg2 - 1).get("title"),
							TrainingData.get(arg2 - 1).get("url"));
				}

			}
		} catch (Exception e) {
		}

	}

	/**
	 * ��Ѷ�б�
	 */
	private void getConsulting(String url, int page, boolean loadedtype) {

		HashMap map = new HashMap<String, String>();
		map.put("type", "1");
		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MainConsultingFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainConsultingFragment.this),
				HttpStaticApi.consulting_Http, null, loadedtype);
	}

	/**
	 * ��Ѷ�б�
	 */
	private void getTraining(String url, int page, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("type", "2");
		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MainConsultingFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainConsultingFragment.this),
				HttpStaticApi.training_Http, null, loadedtype);
	}

	/**
	 * ��ȡ�ֲ�ͼ
	 */
	private void carousel(String url, String apptype, boolean loadedtype) {
		url = UrlConfig.carousel_Http + "?apptype=2";
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", apptype);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				MainConsultingFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainConsultingFragment.this),
				HttpStaticApi.carousel_Http, null, loadedtype);

	}
}
