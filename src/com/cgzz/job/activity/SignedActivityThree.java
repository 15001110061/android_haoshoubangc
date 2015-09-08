package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.BaseActivityCloseListener;
import com.cgzz.job.R;
import com.cgzz.job.adapter.PromptAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.bean.PoiLocation;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.wheel.GoOffWheelView;
import com.cgzz.job.wheel.GoOffWheelView.WheelDateChoiseListener;
import com.cgzz.job.wheelview.NumericWheelAdapter;
import com.cgzz.job.wheelview.OnWheelScrollListener;
import com.cgzz.job.wheelview.WheelView;

/***
 * 
 * @author wjm 注册第一步
 */
public class SignedActivityThree extends BaseActivity implements OnClickListener, WheelDateChoiseListener,
		OnCheckedChangeListener, TextWatcher, OnPoiSearchListener, OnItemClickListener {

	LinearLayout llLeft;
	public GlobalVariables application;
	private int type = 0;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.register_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					application.popClosePath(true, UrlConfig.PATH_KEY_REGISTERED);
					ToastUtil.makeShortText(SignedActivityThree.this, "注册成功");

					Intent intent = new Intent(SignedActivityThree.this, LoginActivity.class);
					Bundle bundle2 = new Bundle();
					bundle2.putString("mobile", mobile);
					intent.putExtras(bundle2);
					startActivity(intent);

					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserLogin(data);
					ToastUtil.makeShortText(SignedActivityThree.this, bundle.get("msg").toString());
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
	GoOffWheelView goOffWheelView;
	String mobile, password, faceurl, cardurl, portrait, realname, latitude, longitude, address, card;
	int isTrain = 0;
	int isSign = 0;
	int age, sex;
	String introduceno;
	EditText et_signed_nianxian;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signed_three);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_REGISTERED, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});

		setTitle("三步完成注册", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		Intent intent = getIntent();
		mobile = intent.getStringExtra("mobile");

		password = intent.getStringExtra("password");
		portrait = intent.getStringExtra("portrait");
		sex = Integer.parseInt(intent.getStringExtra("sex"));
		age = Integer.parseInt(intent.getStringExtra("age"));
		card = intent.getStringExtra("card");
		realname = intent.getStringExtra("realname");
		introduceno = intent.getStringExtra("introduceno");
		// try {
		// introduceno = Integer.parseInt(intent.getStringExtra("introduceno"));
		// } catch (Exception e) {
		// introduceno=0;
		// }

		cardurl = intent.getStringExtra("cardUrl");
		initView();
		initListenger();
		initPopWindow();

	}

	RelativeLayout ll;
	AutoCompleteTextView et_signed_home;
	TextView tv_signed_next, et_signed_starttime, et_signed_endtime;
	CheckBox cb_seting_training, et_signed_sign;

	private void initView() {
		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		et_signed_home = (AutoCompleteTextView) findViewById(R.id.et_signed_home);
		et_signed_home.addTextChangedListener(this);
		et_signed_home.setOnItemClickListener(this);
		et_signed_starttime = (TextView) findViewById(R.id.et_signed_starttime);
		et_signed_endtime = (TextView) findViewById(R.id.et_signed_endtime);
		tv_signed_next = (TextView) findViewById(R.id.tv_signed_next);
		cb_seting_training = (CheckBox) findViewById(R.id.cb_seting_training);
		et_signed_sign = (CheckBox) findViewById(R.id.et_signed_sign);
		et_signed_nianxian = (EditText) findViewById(R.id.et_signed_nianxian);
		et_signed_nianxian.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		//
		ll = (RelativeLayout) findViewById(R.id.ll);
		ll.addView(getDataPick());
	}

	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private int mYear = 1996;
	private int mMonth = 0;
	private int mDay = 1;
	View view = null;
	TextView tv_go_off_ok, tv_go_off_cancel, tv_go_off_title;

	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);

		int curYear = mYear;
		int curMonth = mMonth + 1;
		int curDate = mDay;

		view = inflater.inflate(R.layout.wheel_date_picker, null);
		ImageButton dis = (ImageButton) view.findViewById(R.id.ib_dis);
		tv_go_off_ok = (TextView) view.findViewById(R.id.tv_go_off_ok);
		tv_go_off_cancel = (TextView) view.findViewById(R.id.tv_go_off_cancel);
		tv_go_off_title = (TextView) view.findViewById(R.id.tv_go_off_title);
		year = (WheelView) view.findViewById(R.id.year);
		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this, 1950, norYear);
		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(false);// 是否可循环滑动
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(this, 1, 12, "%02d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(false);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth);
		day.setCyclic(false);

		year.setVisibleItems(7);// 设置显示行数
		month.setVisibleItems(7);
		day.setVisibleItems(7);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		tv_go_off_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ll.setVisibility(View.GONE);

			}
		});
		tv_go_off_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ll.setVisibility(View.GONE);
				if (type == 1) {
					et_signed_starttime.setText("");
				} else if (type == 2) {
					et_signed_endtime.setText("");
				}

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ll.setVisibility(View.GONE);
				if (type == 1) {
					et_signed_starttime.setText("");
				} else if (type == 2) {
					et_signed_endtime.setText("");
				}
			}
		});

		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;// 年
			int n_month = month.getCurrentItem() + 1;// 月

			// initDay(n_year,n_month);

			if (type == 1) {
				et_signed_starttime.setText(n_year + "年" + n_month + "月");
			} else if (type == 2) {
				et_signed_endtime.setText(n_year + "年" + n_month + "月");
			}

			String birthday = new StringBuilder().append((year.getCurrentItem() + 1950)).append("-")
					.append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1)
							: (month.getCurrentItem() + 1))
					.append("-").append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1)
							: (day.getCurrentItem() + 1))
					.toString();
		}
	};

	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		et_signed_home.setOnClickListener(this);
		et_signed_starttime.setOnClickListener(this);
		et_signed_endtime.setOnClickListener(this);
		tv_signed_next.setOnClickListener(this);
		et_signed_starttime.setOnClickListener(this);
		et_signed_endtime.setOnClickListener(this);
		cb_seting_training.setOnCheckedChangeListener(this);
		et_signed_sign.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_signed_next:// 下一步
			// mobile = intent.getStringExtra("mobile");
			// password = intent.getStringExtra("password");
			// portrait = intent.getStringExtra("portrait");
			// sex = intent.getStringExtra("sex");
			// age = intent.getStringExtra("age");
			// realname = intent.getStringExtra("realname");
			// introduceno = intent.getStringExtra("introduceno");
			// int isTrain =0;
			// int isSign =0;

			if (cb_seting_training.isChecked()) {
				isTrain = 1;
			} else {
				isTrain = 0;
			}

			if (et_signed_sign.isChecked()) {
				isSign = 1;
			} else {
				isSign = 0;
			}

			if (Utils.isEmpty(address)) {
				ToastUtil.makeShortText(this, "请填写家庭住址");
				return;
			}
			int nianxian = 0;
			try {
				nianxian = Integer.parseInt(et_signed_nianxian.getText().toString());

				if (nianxian > 30) {
					ToastUtil.makeShortText(this, "工作年限请填写0~30年之间");
					return;
				}

			} catch (Exception e) {
				// TODO: handle exception
				nianxian = 0;
			}

			getRegister(UrlConfig.register_Http, mobile, password, address, application.getCityCode(), 1, isTrain,
					isSign, card, sex, realname, cardurl, age, portrait, latitude, longitude, introduceno, nianxian,
					true);
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;

		case R.id.et_signed_starttime:// 开始时间
			ll.setVisibility(View.VISIBLE);
			tv_go_off_title.setText("选择开始时间");
			type = 1;

			break;
		case R.id.et_signed_endtime:// 结束时间
			ll.setVisibility(View.VISIBLE);
			tv_go_off_title.setText("选择结束时间");
			type = 2;

			break;

		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	@Override
	public void chooseTime(String date) {
		ToastUtil.makeShortText(SignedActivityThree.this, date);
		if (type == 1) {
			et_signed_starttime.setText(date);
		} else if (type == 2) {
			et_signed_endtime.setText(date);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (cb_seting_training == arg0) {
			if (arg1) {
				isTrain = 1;
			} else {
				isTrain = 0;
			}
		} else if (et_signed_sign == arg0) {

			if (arg1) {
				isSign = 1;
			} else {
				isSign = 0;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private void initPopWindow() {

		prompAdapter = new PromptAdapter(this);
		et_signed_home.setAdapter(prompAdapter);
		prompAdapter.refreshData(prompList);
	}

	private List<PoiLocation> prompList = new ArrayList<PoiLocation>();
	private PromptAdapter prompAdapter;
	private PoiSearch poiSearch;// POI搜索
	private PoiSearch.Query query;// Poi查询条件类

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {
					List<PoiItem> poiItems = result.getPois();
					if (poiItems != null) {
						prompList.clear();
						for (int i = 0; i < poiItems.size(); i++) {
							PoiLocation ai = new PoiLocation();
							ai.setLat(poiItems.get(i).getLatLonPoint());
							ai.setTitle(poiItems.get(i).getTitle());

							prompList.add(ai);
						}
						// 刷新列表

						prompAdapter.refreshData(prompList);
						et_signed_home.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// 保存到配置文件，通过历史记录读取。并跳转页面
								PoiLocation poh2 = prompList.get(position);
								longitude = poh2.getLat().getLongitude() + "";
								latitude = poh2.getLat().getLatitude() + "";
								address = poh2.getTitle() + "";

							}
						});

					}

				}
			}
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		doSearchQuery(arg0.toString().trim());

	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery(String searchText) {
		if (!Utils.isEmpty(searchText)) {

			query = new PoiSearch.Query(searchText, "", application.getCityCode());
			query.setPageSize(10);
			query.setPageNum(0);

			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();
		} else {
			// 显示历史记录
			// ToastUtil.makeShortText(DestinationActivity.this, "请输入搜索关键字");
		}

	}

	private void getRegister(String url, String mobile, String password, String address, String cityno, int worktype,
			int isTrain2, int isSign2, String card, int sex, String realname, String cardUrl, int age2, String portrait,
			String latitude, String longitude, String introduceno, int nianxian, boolean loadedtype) {

		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", mobile);
		map.put("password", password);
		map.put("address", address);
		map.put("cityno", cityno);
		map.put("worktype", worktype + "");
		map.put("isTrain", isTrain2 + "");
		map.put("isSign", isSign2 + "");
		map.put("card", card);
		map.put("sex", sex + "");
		map.put("realname", removeAllSpace(realname));
		map.put("cardUrl", cardUrl);
		map.put("age", age2 + "");
		map.put("portrait", portrait);
		map.put("latitude", latitude);
		map.put("longitude", longitude);
		if (!Utils.isEmpty(introduceno))
			map.put("introduceno", introduceno + "");
		map.put("workage", nianxian + "");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SignedActivityThree.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SignedActivityThree.this), HttpStaticApi.register_Http, null,
				loadedtype);
	}

	public String removeAllSpace(String str) {
		String tmpstr = str.replace(" ", "");
		return tmpstr;
	}
}
