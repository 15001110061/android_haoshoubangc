package com.cgzz.job.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.TTSController;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.CustomDialog;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechUser;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import cn.jpush.android.api.InstrumentedActivity;

public class SplashActivity extends InstrumentedActivity implements AMapLocationListener, Runnable {
	private GlobalVariables application;
	Handler handlers = new Handler();
	// AlertDialog show;
	Object downloadurl = null;
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			Bundle bundle = null;
			Message msg = new Message();
			switch (method) {
			case HttpStaticApi.version_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					try {

						bundle = ParserUtil.ParserVersion(data);
						downloadurl = bundle.get("downloadurl");
						if (downloadurl != null) {
							final String url2 = bundle.get("downloadurl").toString();
							if ("1".equals(bundle.get("forced_upgrade").toString())) {
								CustomDialog.alertDialog(SplashActivity.this, false, false, true, null,
										bundle.get("message").toString(), new CustomDialog.PopUpDialogListener() {

									@Override
									public void doPositiveClick(Boolean isOk) {
										if (isOk) {// ȷ��
											String url = url2;
											Intent intent = new Intent(Intent.ACTION_VIEW);
											intent.setData(Uri.parse(url));
											startActivity(intent);
											openActivity();
										} else {
											openActivity();

										}

									}
								});

							} else {
								CustomDialog.alertDialog(SplashActivity.this, false, true, true, null,
										bundle.get("message").toString(), new CustomDialog.PopUpDialogListener() {

									@Override
									public void doPositiveClick(Boolean isOk) {
										if (isOk) {// ȷ��
											String url = url2;
											Intent intent = new Intent(Intent.ACTION_VIEW);
											intent.setData(Uri.parse(url));
											startActivity(intent);
											openActivity();
										} else {
											openActivity();

										}

									}
								});
							}

						}

					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case HttpStaticApi.FAILURE_HTTP:
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
	Boolean user_first;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.layout_splash);
		application = (GlobalVariables) getApplicationContext();

		SharedPreferences setting = getSharedPreferences("FIRST" + Utils.getVersionName(SplashActivity.this), 0);
		user_first = setting.getBoolean("FIRST", true);

		// �ƴ�
		SpeechUser.getUser().login(SplashActivity.this, null, null, "appid=" + getString(R.string.app_id), listener);
		TTSController ttsManager = TTSController.getInstance(this);// ��ʼ������ģ��
		ttsManager.init();
		TTSController.getInstance(this).startSpeaking();

		// ����
		MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);

		Location();

	}

	private void Location() {
		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 4000);

	}

	/**
	 * �û���¼�ص�������.
	 */
	private SpeechListener listener = new SpeechListener() {

		@Override
		public void onData(byte[] arg0) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {
			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}
	};

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	private AMapLocation aMapLocation;// �����ж϶�λ��ʱ
	private Handler handler = new Handler();

	@Override
	public void run() {
		if (aMapLocation == null) {
			// ToastUtil.makeShortText(SplashActivity.this, "��λʧ��");
			stopLocation();// ���ٵ���λ

			openActivity();

		} else {
			if (downloadurl == null)
				openActivity();
		}

	}

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
			// String str = ("��λ�ɹ�:(" + geoLng + "," + geoLat + ")" + "\n�� �� :"
			// + location.getAccuracy() + "��"
			// + "\n��λ��ʽ:" + location.getProvider() + "\n���б���:" + cityCode +
			// "\nλ������:" + desc + "\nʡ:"
			// + location.getProvince() + "\n��:" + location.getCity() +
			// "\n��(��):" + location.getDistrict()
			// + "\n�������:" + location.getAdCode());
			// ToastUtil.makeShortText(SplashActivity.this, "" + str);
			application.setLatitude(geoLat + "");
			application.setLongitude(geoLng + "");
			application.setCityCode(cityCode);
			application.setCityName(location.getCity());
			application.setDesc(desc);
			getVersion(UrlConfig.version_Http, true);

		}

	}

	private LocationManagerProxy aMapLocManager = null;

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	/**
	 * �汾����
	 */
	private void getVersion(String url, boolean loadedtype) {
		PackageManager manager;
		PackageInfo info = null;
		manager = this.getPackageManager();
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		url = url + "?version=" + info.versionCode + "&apptype=2";

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, SplashActivity.this, url, null, callbackData,
				GlobalVariables.getRequestQueue(SplashActivity.this), HttpStaticApi.version_Http, null, loadedtype);

	}

	private void openActivity() {
		if (user_first) {// ��һ��
			Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
			startActivity(intent);
			finish();

		} else {
			// if (application.isLogon()) {
			if (application.isGestures()) {
				Intent intent = new Intent(SplashActivity.this, GestureVerifyActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(SplashActivity.this, TabMainActivity.class);
				intent.putExtra("cityname", application.getCityName());
				intent.putExtra("cityid", application.getCityCode());
				startActivity(intent);
			}

			finish();
		}
	}
}
