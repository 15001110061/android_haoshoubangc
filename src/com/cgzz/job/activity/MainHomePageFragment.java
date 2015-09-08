package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.cgzz.job.BaseFragmentActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.HomeFragmentPagerAdapter;
import com.cgzz.job.adapter.Myadapter2;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomListView;

/***
 * @author wjm ��ҳ����ҳ
 */
public class MainHomePageFragment extends BaseFragmentActivity implements
		OnPageChangeListener, OnClickListener, AMapLocationListener, Runnable {
	private TextView tv_home_item_page, tv_home_item_sign, tv_title_left;
	private View mBaseView;
	private ViewPager viewPager;
	private HomePageFragment_fragment mfragment;
	private ArrayList<Fragment> fragmentList;
	private HomeFragmentPagerAdapter homeFragmentPagerAdapter;
	private Context context;
	private RelativeLayout rl;
	private int logoCollection = 1;// ҳ���ʶ
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> shufflingList;

	private LocationManagerProxy aMapLocManager = null;
	private TextView myLocation;
	private AMapLocation aMapLocation;// �����ж϶�λ��ʱ
	private Handler handler = new Handler();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissDialog();
			switch (method) {
			case HttpStaticApi.testa_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					if (loadedtype) {
						fragmentList.clear();
						homeFragmentPagerAdapter.refreshMYData(fragmentList);
					}

					logoCollection++;
					// ��������
					shufflingList = new ArrayList<Map<String, String>>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("tv_current_name", "��������" + logoCollection);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);

					for (int i = 0; i < shufflingList.size(); i++) {
						mfragment = new HomePageFragment_fragment(
								shufflingList.get(i), 0);
						fragmentList.add(mfragment);
					}
					homeFragmentPagerAdapter.refreshMYData(fragmentList);

					if (loadedtype) {
						if (fragmentList.size() > 0) {
							tv_home_item_page.setText(1 + "/"
									+ fragmentList.size());
							viewPager.setCurrentItem(0);
						} else {
							tv_home_item_page.setText(0 + "/"
									+ fragmentList.size());
						}

					} else {
						tv_home_item_page.setText(viewPager.getCurrentItem()
								+ 1 + "/" + fragmentList.size());
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				}
				break;

			}
		}
	};
//	String cityname = "", cityid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_homepage);
		// setTitle("�ð���", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
		// TITLE_TYPE_IMG, R.drawable.stub_back);

		Intent intent = getIntent();
//		cityname = intent.getStringExtra("cityname");
//		cityid = intent.getStringExtra("cityid");

		initView();
		init();
		// Location();
		logoCollection = 1;
		getCurrent("https://www.baidu.com/", false);
		// initmPopupWindowView();
	}

	LinearLayout llright, llLeft;

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		rl = (RelativeLayout) findViewById(R.id.rl);
		tv_home_item_sign = (TextView) findViewById(R.id.tv_home_item_sign);
		tv_home_item_page = (TextView) findViewById(R.id.tv_home_item_page);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		tv_title_left = (TextView) findViewById(R.id.tv_title_left);
		homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(
				this.getSupportFragmentManager());
		viewPager.setAdapter(homeFragmentPagerAdapter);
		// viewPager.setPageMargin(20);
		viewPager.setOffscreenPageLimit(3);
		// homeFragmentPagerAdapter.setPageMargin(15);
		if (Utils.isEmpty(		application.getUsercityName())) {
			
			tv_title_left.setText(application.getCityName());
		} else {
			tv_title_left.setText(application.getUsercityName());
		}

	}

	private void init() {
		viewPager.setOnPageChangeListener(this);
		tv_home_item_sign.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		fragmentList = new ArrayList<Fragment>();

	}

	private void Location() {
		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 12000);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// fragmentList.get(0).setUserVisibleHint(true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * ���ٶ�λ
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();// ֹͣ��λ
	}

	@Override
	public void onPageSelected(int arg0) {
		if (fragmentList.size() == (arg0 + 1)) {
			getCurrent("https://www.baidu.com/", false);
		}
		tv_home_item_page.setText(viewPager.getCurrentItem() + 1 + "/"
				+ fragmentList.size());

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.tv_home_item_sign:// ���ȱ���
			ToastUtil.makeShortText(context, "���ȱ���");
			break;
		case R.id.ll_title_left://
			// popupwindow.showAsDropDown(arg0);

			intent = new Intent(MainHomePageFragment.this,
					SelectCityActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.ll_title_right:// ˢ��

			logoCollection = 1;
			getCurrent("https://www.baidu.com/", true);
			break;
		default:
			break;
		}

	}

	/**
	 * �˷����Ѿ�����
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * ��϶�λ�ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			stopLocation();// ���ٵ���λ
			this.aMapLocation = location;// �жϳ�ʱ����
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("��λ�ɹ�:(" + geoLng + "," + geoLat + ")"
					+ "\n��    ��    :" + location.getAccuracy() + "��"
					+ "\n��λ��ʽ:" + location.getProvider() + "\n���б���:" + cityCode
					+ "\nλ������:" + desc + "\nʡ:" + location.getProvince()
					+ "\n��:" + location.getCity() + "\n��(��):"
					+ location.getDistrict() + "\n�������:" + location.getAdCode());
			ToastUtil.makeShortText(MainHomePageFragment.this, "" + str);

			logoCollection = 1;
			getCurrent("https://www.baidu.com/", false);

		}
	}

	@Override
	public void run() {
		if (aMapLocation == null) {
			ToastUtil.makeShortText(MainHomePageFragment.this, "��λʧ��");
			stopLocation();// ���ٵ���λ
		} else {

		}
	}

	/**
	 * ��ǰ����
	 */
	private void getCurrent(String url, boolean loadedtype) {
		showWaitDialog();
		url = url;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				MainHomePageFragment.this, url, null, callBack,
				GlobalVariables.getRequestQueue(MainHomePageFragment.this),
				HttpStaticApi.testa_Http, null, loadedtype);
	}

	// ArrayList<Map<String, String>> listlv;
	// Myadapter2 adapter;
	// PopupWindow popupwindow;
	// CustomListView lvCars;

	// public void initmPopupWindowView() {
	// // ��ȡ�Զ��岼���ļ�����ͼ
	// View customView = getLayoutInflater().inflate(R.layout.popview_item,
	// null, false);
	// // ����PopupWindow��Ⱥ͸߶�
	// popupwindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT, true);
	// popupwindow.setWidth(LayoutParams.WRAP_CONTENT);
	// popupwindow.setHeight(LayoutParams.WRAP_CONTENT);
	// // popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
	// popupwindow.setOutsideTouchable(true);
	// // �����Ļ�������ּ�Back��ʱPopupWindow��ʧ
	// popupwindow.setBackgroundDrawable(new BitmapDrawable());
	// // �Զ���view��Ӵ����¼�
	// customView.setOnTouchListener(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (popupwindow != null && popupwindow.isShowing()) {
	// popupwindow.dismiss();
	// popupwindow = null;
	// }
	// return false;
	// }
	// });
	//
	// // ����
	// lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
	// // lvCars.setDivider(getResources().getDrawable(R.color.devide_line));
	// // lvCars.setDividerHeight(Utils.dip2px(this, 1));
	// // lvCars.setFooterDividersEnabled(false);
	// lvCars.setCanRefresh(false);// �ر�����ˢ��
	// lvCars.setCanLoadMore(false);// �򿪼��ظ���
	// lvCars.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	//
	// if (lvCars == arg0) {
	// popupwindow.dismiss();
	// tv_title_left.setText(listlv.get(arg2 - 1).get("cityname"));
	// logoCollection = 1;
	// getCurrent("https://www.baidu.com/", true);
	// }
	// }
	// });
	// adapter = new Myadapter2(this);
	// lvCars.setAdapter(adapter);
	// listlv = getreadModels();
	// adapter.refreshData(listlv);
	// }

	private ArrayList<Map<String, String>> getreadModels() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("cityname", "����");
		map.put("cityid", "010");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("cityname", "���");
		map2.put("cityid", "022");
		list.add(map);
		list.add(map2);
		return list;
	}
}
