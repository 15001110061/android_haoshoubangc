package com.cgzz.job.application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cgzz.job.BaseActivityCloseListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import cn.jpush.android.api.JPushInterface;

/**
 * ȫ�ֱ���
 */
public class GlobalVariables extends LitePalApplication {
	public SparseArray<SparseArray<BaseActivityCloseListener>> closeMap;
	public static RequestQueue mQueue;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private boolean isLogon = false; // �Ƿ��ѵ�¼
	private boolean gestures = false; // �Ƿ������Ƶ�¼
	private boolean stopAccept = true; // �Ƿ�ֹͣ�ӵ�

	private String cityCode = ""; // ���б���
	private String cityName = ""; // ��������

	private String faceUrl = ""; // ͷ���ַ
	private String userId = "";// �û�ID
	private String userno = "";// ��ͺ�
	private String realname = "";// ����
	private String mobile = "";// �ֻ���
	private String age = "";// ����
	private String workage = "";// ��������
	private String sex = "";// �Ա�
	private String card = "";// ���֤����
	private String address = "";// ��ͥ��ַ

	private String portrait = "";// ͷ���ַ
	private String starlevel = "";// �Ǽ�
	private String xinyong = "";// ����
	private String is_healthy = "";// ����֤ 0�� 1��
	private String token = "";//
	private String GesturesPasswords = "";// ��������

	private String usercityName = "";// �û�ѡ���������

	private String usercityCode = "";// �û�ѡ�����id

	private String latitude = "";// ��ǰ��λ����
	private String longitude = "";//
	private String homeLatitude = "";// �ҵ�����
	private String homeLongitude = "";//

	private String desc = "";//
	private String orderNum = "";// ��������
	private boolean isReddot = false;
	private boolean isReddothome = false;
	private String homemessage = "";// ��ҳ֪ͨ��Ϣ
	


	public boolean isReddothome() {
		return isReddothome;
	}

	public void setReddothome(boolean isReddothome) {
		this.isReddothome = isReddothome;
		editor.putBoolean("isReddothome", isReddothome);
		editor.commit();
	}

	public boolean isReddot() {
		return isReddot;
	}

	public void setReddot(boolean isReddot) {
		this.isReddot = isReddot;
		editor.putBoolean("isReddot", isReddot);
		editor.commit();
	}

	public String getHomeLatitude() {
		return homeLatitude;
	}



	public void setHomeLatitude(String homeLatitude) {
		this.homeLatitude = homeLatitude;
		editor.putString("homeLatitude", homeLatitude);
		editor.commit();
	}

	public String getHomeLongitude() {
		return homeLongitude;
	}

	public void setHomeLongitude(String homeLongitude) {
		this.homeLongitude = homeLongitude;
		editor.putString("homeLongitude", homeLongitude);
		editor.commit();
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
		editor.putString("desc", desc);
		editor.commit();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
		editor.putString("orderNum", orderNum);
		editor.commit();
	}

	public String getIs_healthy() {
		return is_healthy;
	}

	public void setIs_healthy(String is_healthy) {
		this.is_healthy = is_healthy;
		editor.putString("is_healthy", is_healthy);
		editor.commit();
	}

	public String getUsercityName() {
		return usercityName;
	}

	public void setUsercityName(String usercityName) {
		this.usercityName = usercityName;
		editor.putString("usercityName", usercityName);
		editor.commit();
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
		editor.putString("latitude", latitude);
		editor.commit();
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
		editor.putString("longitude", longitude);
		editor.commit();
	}

	public String getUsercityCode() {
		return usercityCode;
	}

	public void setUsercityCode(String usercityCode) {
		this.usercityCode = usercityCode;
		editor.putString("usercityCode", usercityCode);
		editor.commit();
	}

	public String getGesturesPasswords() {
		return GesturesPasswords;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
		editor.putString("cityName", cityName);
		editor.commit();
	}

	public void setGesturesPasswords(String gesturesPasswords) {
		GesturesPasswords = gesturesPasswords;
		editor.putString("GesturesPasswords", GesturesPasswords);
		editor.commit();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		editor.putString("userId", userId);
		editor.commit();
	}

	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
		editor.putString("userno", userno);
		editor.commit();
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
		editor.putString("realname", realname);
		editor.commit();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
		editor.putString("mobile", mobile);
		editor.commit();
	}
	
	
	public String getHomemessage() {
		return homemessage;
	}

	public void setHomemessage(String homemessage) {
		this.homemessage = homemessage;
		editor.putString("homemessage", homemessage);
		editor.commit();
	}
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
		editor.putString("age", age);
		editor.commit();
	}

	public String getWorkage() {
		return workage;
	}

	public void setWorkage(String workage) {
		this.workage = workage;
		editor.putString("workage", workage);
		editor.commit();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		editor.putString("sex", sex);
		editor.commit();
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
		editor.putString("card", card);
		editor.commit();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		editor.putString("address", address);
		editor.commit();
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
		editor.putString("portrait", portrait);
		editor.commit();
	}

	public String getStarlevel() {
		return starlevel;
	}

	public void setStarlevel(String starlevel) {
		this.starlevel = starlevel;
		editor.putString("starlevel", starlevel);
		editor.commit();
	}

	public String getXinyong() {
		return xinyong;
	}

	public void setXinyong(String xinyong) {
		this.xinyong = xinyong;
		editor.putString("xinyong", xinyong);
		editor.commit();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		editor.putString("token", token);
		editor.commit();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ȡ�����ļ�
		sp = getSharedPreferences("ownconfigure", Activity.MODE_PRIVATE);
		editor = sp.edit();
		getAccess();

		// Jpush
		if (true) {
			JPushInterface.setDebugMode(true); // ���ÿ�����־,����ʱ��ر���־
			JPushInterface.init(this); // ��ʼ�� JPush
			JPushInterface.resumePush(getApplicationContext());
		}

		//
		closeMap = new SparseArray<SparseArray<BaseActivityCloseListener>>();
	}

	public static RequestQueue getRequestQueue(Context c) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(c);
		}
		return mQueue;
	}

	public boolean isLogon() {
		return isLogon;
	}

	public void setLogon(boolean isLogon) {
		this.isLogon = isLogon;
		editor.putBoolean("isLogon", isLogon);
		editor.commit();
	}

	public boolean isGestures() {
		return gestures;
	}

	public void setGestures(boolean gestures) {
		this.gestures = gestures;
		editor.putBoolean("gestures", gestures);
		editor.commit();
	}

	public boolean isStopAccept() {
		return stopAccept;
	}

	public void setStopAccept(boolean stopAccept) {
		this.stopAccept = stopAccept;
		editor.putBoolean("stopAccept", stopAccept);
		editor.commit();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
		editor.putString("cityCode", cityCode);
		editor.commit();
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
		editor.putString("faceUrl", faceUrl);
		editor.commit();
	}

	/***
	 * ��ȡ�����ļ�������ֵ
	 */
	public void getAccess() {

		// �˴�ֻ��ȡ�����ļ���д��ط�Ϊ��¼�ͱ༭������Ϣ
		sp = getSharedPreferences("ownconfigure", Activity.MODE_PRIVATE);
		isLogon = sp.getBoolean("isLogon", false);
		isReddot = sp.getBoolean("isReddot", isReddot);
		isReddothome = sp.getBoolean("isReddothome", isReddothome);
		gestures = sp.getBoolean("gestures", false);
		stopAccept = sp.getBoolean("stopAccept", false);
	
		faceUrl = sp.getString("faceUrl", "");
		// cityCode = sp.getString("cityCode", "");
		userId = sp.getString("userId", "");
		userno = sp.getString("userno", "");
		realname = sp.getString("realname", "");
		mobile = sp.getString("mobile", "");
		age = sp.getString("age", "");
		workage = sp.getString("workage", "");
		sex = sp.getString("sex", "");
		card = sp.getString("card", "");
		address = sp.getString("address", "");
		portrait = sp.getString("portrait", "");

		homeLatitude = sp.getString("homeLatitude", "");
		homeLongitude = sp.getString("homeLongitude", "");

		starlevel = sp.getString("starlevel", "1");
		xinyong = sp.getString("xinyong", "");
		token = sp.getString("token", "");

		is_healthy = sp.getString("is_healthy", "0");
		GesturesPasswords = sp.getString("GesturesPasswords", "");
		cityCode = sp.getString("cityCode", "010");
		cityName = sp.getString("cityName", "������");
		latitude = sp.getString("latitude", "");
		longitude = sp.getString("longitude", "");
		
		
		usercityName = sp.getString("usercityName", "");
		usercityCode = sp.getString("usercityCode", "");

		desc = sp.getString("desc", "");

		homeLatitude = sp.getString("homeLatitude", "");
		homeLongitude = sp.getString("homeLongitude", "");


		orderNum = sp.getString("orderNum", "0");

		homemessage = sp.getString("homemessage", "");
		

	}

	public void setAccess(Bundle bundle) {

		if (bundle.containsKey("is_accept")) {
			if ("0".equals(bundle.getString("is_accept"))) {

				setStopAccept(true);
			} else {
				setStopAccept(false);
			}
		}

		if (bundle.containsKey("id")) {
			setUserId(bundle.getString("id"));
		}
		if (bundle.containsKey("userno")) {
			setUserno(bundle.getString("userno"));
		}
		if (bundle.containsKey("realname")) {
			setRealname(bundle.getString("realname"));
		}
		if (bundle.containsKey("mobile")) {
			setMobile(bundle.getString("mobile"));
		}

		if (bundle.containsKey("age")) {
			setAge(bundle.getString("age"));
		}
		if (bundle.containsKey("workage")) {
			setWorkage(bundle.getString("workage"));
		}
		if (bundle.containsKey("sex")) {
			setSex(bundle.getString("sex"));
		}
		if (bundle.containsKey("card")) {
			setCard(bundle.getString("card"));
		}
		if (bundle.containsKey("address")) {
			setAddress(bundle.getString("address"));
		}

		if (bundle.containsKey("portrait")) {
			setFaceUrl(bundle.getString("portrait"));
		}
		if (bundle.containsKey("starlevel")) {
			setStarlevel(bundle.getString("starlevel"));
		}
		if (bundle.containsKey("xinyong")) {
			setXinyong(bundle.getString("xinyong"));

		}

		if (bundle.containsKey("token")) {
			setToken(bundle.getString("token"));
		}
		if (bundle.containsKey("is_healthy")) {
			setIs_healthy(bundle.getString("is_healthy"));
		}
		if (bundle.containsKey("homeLatitude")) {
			setHomeLatitude(bundle.getString("homeLatitude"));
		}
		if (bundle.containsKey("homeLongitude")) {
			setHomeLongitude(bundle.getString("homeLongitude"));
		}

	}

	/**
	 * �첽�ص���������������
	 */
	// private ObserverCallBack callbackData = new ObserverCallBack() {
	// public void back(String data, int encoding, int method, Object obj) {
	// switch (method) {// �ж��ĸ�����������
	// case HttpStaticApi.logon_Http:
	// switch (encoding) {// �жϴ˷������صĽ��
	//
	// case HttpStaticApi.setRid_Http: // ���ñ����ɹ�
	// switch (encoding) {// �жϴ˷������صĽ��
	// case HttpStaticApi.SUCCESS_HTTP:
	// break;
	// case HttpStaticApi.FAILURE_HTTP:
	// break;
	// default:
	// break;
	// }
	// break;
	// default:
	// break;
	// }
	//
	// }
	// }
	// };

	public synchronized void putClosePath(int key, BaseActivityCloseListener listener) {
		if (closeMap.indexOfKey(key) < 0) {
			SparseArray<BaseActivityCloseListener> sa = new SparseArray<BaseActivityCloseListener>();
			sa.put(sa.size(), listener);
			closeMap.put(key, sa);
		} else {
			SparseArray<BaseActivityCloseListener> sa = closeMap.get(key);
			if (sa.indexOfValue(listener) < 0) {
				sa.put(sa.size(), listener);
			}
		}
	}

	public synchronized void popClosePath(boolean finish, int key) {
		if (closeMap.indexOfKey(key) >= 0) {
			SparseArray<BaseActivityCloseListener> sa = closeMap.get(key);
			if (finish) {
				int s = sa.size();
				for (int i = 0; i < s; i++) {
					BaseActivityCloseListener bl = sa.get(sa.keyAt(i));
					bl.onFinish();
				}
			}
			sa.clear();
			closeMap.remove(key);
		}
	}
	
//	@Override
//	public Resources getResources() {
//		Resources res = super.getResources();
//		Configuration config = new Configuration();
//		config.setToDefaults();
//		res.updateConfiguration(config, res.getDisplayMetrics());
//		return res;
//	}
}
