package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.SortAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.bean.SortModel;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.CharacterParser;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.view.ClearEditText;
import com.cgzz.job.view.PinyinComparator;
import com.cgzz.job.view.SideBar;
import com.cgzz.job.view.SideBar.OnTouchingLetterChangedListener;

public class SelectCityActivity extends BaseActivity implements OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
	 */

	private PinyinComparator pinyinComparator;
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.citylist_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserCitylist(data);

					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {
						SourceDateList = filledData((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
						// ����a-z��������Դ����
						Collections.sort(SourceDateList, pinyinComparator);
						adapter = new SortAdapter(SelectCityActivity.this, SourceDateList);
						sortListView.setAdapter(adapter);

					} else {
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserLogin(data);
					ToastUtil.makeShortText(SelectCityActivity.this, bundle.get("msg").toString());
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
	LinearLayout llLeft, ll_one;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);
		setTitle("�л�����", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		initViews();
		citylist(UrlConfig.citylist_Http, true);
		initListenger();
	}

	TextView tv_select_dangqiancity;

	private void initViews() {
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		tv_select_dangqiancity = (TextView) findViewById(R.id.tv_select_dangqiancity);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		ll_one = (LinearLayout) findViewById(R.id.ll_one);
		if (!"".equals(application.getCityName())) {
			if ("ѡ�����".equals(application.getCityName())) {
				tv_select_dangqiancity.setText("δ�ɹ���λ");
			} else {
				tv_select_dangqiancity.setText(application.getCityName());
			}

		} else {
			ll_one.setVisibility(View.GONE);
		}

	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		tv_select_dangqiancity.setOnClickListener(this);
		// layout_city_gps_city.setOnClickListener(this);
		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		// sortListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// // ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
		// // Intent resultIntent = new Intent(SelectCityActivity.this,
		// // VipAddAddressActivity.class);
		// //
		// SortModel sort = (SortModel) adapter.getItem(position);
		//
		// Intent intent = new Intent(SelectCityActivity.this,
		// TabMainActivity.class);
		//
		// application.setUsercityCode(sort.getId());
		// application.setUsercityName(sort.getName());
		//
		// // intent.putExtra("cityname", sort.getName());
		// // intent.putExtra("cityid", sort.getId());
		// startActivity(intent);
		// finish();
		//
		// // resultIntent.putExtra("city", sort);
		// // setResult(1111, resultIntent);
		// // finish();
		// Toast.makeText(SelectCityActivity.this, sort.getName(),
		// Toast.LENGTH_LONG).show();
		// }
		// });
	}

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	/**
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(List<Map<String, String>> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		if (data == null) {
			return null;
		}
		for (int i = 0; i < data.size(); i++) {
			SortModel sortModel = new SortModel();

			sortModel.setName(data.get(i).get("name"));
			sortModel.setId(data.get(i).get("id"));
			String pinyin = characterParser.getSelling(data.get(i).get("name"));
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_left:// ����
			onBackPressed();
			break;

		case R.id.tv_select_dangqiancity://
			onBackPressed();
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(SelectCityActivity.this, TabMainActivity.class);
		startActivity(intent);
		finish();
	}

	private void citylist(String url, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, SelectCityActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SelectCityActivity.this), HttpStaticApi.citylist_Http, null,
				loadedtype);
	}
}
