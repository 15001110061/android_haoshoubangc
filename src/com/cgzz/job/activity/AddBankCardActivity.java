package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.BaseActivityCloseListener;
import com.cgzz.job.R;
import com.cgzz.job.adapter.Myadapter3;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomListView;

/***
 * 
 * @author wjm ������п�
 */
public class AddBankCardActivity extends BaseActivity implements OnClickListener {

	LinearLayout llLeft;
	public GlobalVariables application;
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.addbank_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(AddBankCardActivity.this, bundle.get("msg").toString());
					// application.popClosePath(true,
					// UrlConfig.PATH_KEY_ADDCARD);
					// startActivity( new Intent(AddBankCardActivity.this,
					// MyIncomeActivity.class));

					Intent intent = new Intent(AddBankCardActivity.this, LookbankActivity.class);
					startActivity(intent);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(AddBankCardActivity.this, bundle.get("msg").toString());
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addbankcard);
		application = (GlobalVariables) getApplicationContext();

		application.putClosePath(UrlConfig.PATH_KEY_ADDCARD, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});
		setTitle("������п�", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");

		initView();
		initListenger();
		initmPopupWindowView();

	}

	EditText ev_bank_name, ev_bank_num;
	TextView ev_bank_type, tv_bank_next;

	private void initView() {

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);

		ev_bank_type = (TextView) findViewById(R.id.ev_bank_type);
		ev_bank_name = (EditText) findViewById(R.id.ev_bank_name);
		tv_bank_next = (TextView) findViewById(R.id.tv_bank_next);
		ev_bank_num = (EditText) findViewById(R.id.ev_bank_num);
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		tv_bank_next.setOnClickListener(this);
		ev_bank_type.setOnClickListener(this);

		// ev_bank_num.addTextChangedListener(textWatcher);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.tv_bank_next:// �ύ
			String bank_type = ev_bank_type.getText().toString();
			if (Utils.isEmpty(bank_type) || Utils.isEmpty(id)) {
				ToastUtil.makeShortText(this, "��ѡ�񿨵�����");
				return;
			}

			String bank_name = ev_bank_name.getText().toString();
			if (Utils.isEmpty(bank_name)) {
				ToastUtil.makeShortText(this, "����д����");
				return;
			} else {

				if (!checkNameChese(bank_name)) {
					ToastUtil.makeShortText(this, "�ֿ�������������");
					return;
				}
			}

			String bank_num = ev_bank_num.getText().toString();
			if (Utils.isEmpty(bank_num)) {
				ToastUtil.makeShortText(this, "����д����");
				return;
			}
			if (bank_num.length() < 14 || bank_num.length() > 19) {
				ToastUtil.makeShortText(this, "����Ӧ����14λ");
				return;
			}

			getAddbank(UrlConfig.addbank_Http, application.getToken(), application.getUserId(), id, bank_name, bank_num,
					true);
			break;
		case R.id.ll_title_left:// ����
			onBackPressed();
			break;
		case R.id.ev_bank_type:
			if (popupwindow != null)
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
			break;

		default:
			break;
		}
	}

	public boolean checkNameChese(String name) {

		boolean res = true;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

			if (!isChinese(cTemp[i])) {

				res = false;

				break;

			}

		}

		return res;

	}

	public boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	public void onBackPressed() {
		super.onBackPressed();
//		Intent intent = new Intent(AddBankCardActivity.this, LookbankActivity.class);
//		startActivity(intent);
		finish();
	};

	private void getAddbank(String url, String token, String userid, String bank_type, String bank_username,
			String bank_card, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "2");
		map.put("token", token);
		map.put("userid", userid);
		map.put("bank_type", bank_type);
		map.put("bank_username", bank_username);
		map.put("bank_card", bank_card);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, AddBankCardActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(AddBankCardActivity.this), HttpStaticApi.addbank_Http, null,
				loadedtype);
	}

	PopupWindow popupwindow;
	CustomListView lvCars;

	/**
	 * ���ƺŵ�����
	 */
	public void initmPopupWindowView() {
		// ��ȡ�Զ��岼���ļ�����ͼ
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// ����PopupWindow��Ⱥ͸߶�
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// �����Ļ�������ּ�Back��ʱPopupWindow��ʧ
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		// �Զ���view��Ӵ����¼�
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					// popupwindow = null;
				}
				return false;
			}
		});
		ImageButton ib_dis = (ImageButton) customView.findViewById(R.id.ib_dis);
		ib_dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					// popupwindow = null;
				}

			}
		});
		// ����
		lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
		lvCars.setCacheColorHint(Color.TRANSPARENT);
		lvCars.setDivider(getResources().getDrawable(R.color.common_white));
		lvCars.setDividerHeight(Utils.dip2px(this, 0));
		lvCars.setFooterDividersEnabled(false);
		lvCars.setCanRefresh(false);// �ر�����ˢ��
		lvCars.setCanLoadMore(false);// �򿪼��ظ���
		lvCars.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (lvCars == arg0) {
					ev_bank_type.setText(listlv.get(arg2 - 1).get("name"));
					id = listlv.get(arg2 - 1).get("id");
					popupwindow.dismiss();
				}
			}
		});
		adapter = new Myadapter3(this);
		lvCars.setAdapter(adapter);
		listlv = getreadModels();
		adapter.refreshData(listlv);
	}

	String id = "";
	ArrayList<Map<String, String>> listlv;
	Myadapter3 adapter;

	private ArrayList<Map<String, String>> getreadModels() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "�й���������");
		map.put("id", "1");
		list.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "�й�ũҵ����");
		map2.put("id", "2");
		list.add(map2);

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("name", "�й���������");
		map3.put("id", "3");
		list.add(map3);

		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("name", "�й�����");
		map4.put("id", "4");
		list.add(map4);

		Map<String, String> map5 = new HashMap<String, String>();
		map5.put("name", "�й�������������");
		map5.put("id", "5");
		list.add(map5);
		Map<String, String> map6 = new HashMap<String, String>();
		map6.put("name", "��������");
		map6.put("id", "6");
		list.add(map6);
		Map<String, String> map7 = new HashMap<String, String>();
		map7.put("name", "��ͨ����");
		map7.put("id", "7");
		list.add(map7);

		Map<String, String> map8 = new HashMap<String, String>();
		map8.put("name", "�й���������");
		map8.put("id", "8");
		list.add(map8);
		Map<String, String> map9 = new HashMap<String, String>();
		map9.put("name", "�㷢����");
		map9.put("id", "9");
		list.add(map9);
		Map<String, String> map10 = new HashMap<String, String>();
		map10.put("name", "��������");
		map10.put("id", "10");
		list.add(map10);
		Map<String, String> map11 = new HashMap<String, String>();
		map11.put("name", "��������");
		map11.put("id", "11");
		list.add(map11);
		return list;
	}

}
