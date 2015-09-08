package com.cgzz.job.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.cgzz.job.application.GlobalVariables;

/**
 * 未使用 连接网络 3g或者wifi
 */
public class NetworkStatusReceiver extends BroadcastReceiver {
	public GlobalVariables application;
	private static final int MSG_SET_ALIAS = 1001;
	Context context;
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";//开机
	static final String ACTION2 ="android.net.conn.CONNECTIVITY_CHANGE";//网络
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		application = (GlobalVariables) context.getApplicationContext();
		if (intent.getAction().equals(ACTION2)) {
			State wifiState = null;
			State mobileState = null;
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();
			if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED == mobileState) {
				// 手机网络连接成功
				NetworkReceiver(context);
			} else if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {
				// 手机没有任何的网络
			} else if (wifiState != null && State.CONNECTED == wifiState) {
				// 无线网络连接成功
				NetworkReceiver(context);
			

			}
		}else if(intent.getAction().equals(ACTION)){
		}
	

	}

	public void NetworkReceiver(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "Network");
		intentobd.putExtra("isNetwork", "1");//1有网络
		context.sendBroadcast(intentobd);
	}



//	/**
//	 * 异步回调回来并处理数据
//	 */
//	private ObserverCallBack callbackData = new ObserverCallBack() {
//		public void back(String data, int encoding, int method, Object obj) {
//			switch (method) {// 判断哪个方法的请求
//			case HttpStaticApi.logon_Http:
//				switch (encoding) {// 判断此方法返回的结果
//
//				case HttpStaticApi.setRid_Http: // 设置别名成功
//					switch (encoding) {// 判断此方法返回的结果
//					case HttpStaticApi.SUCCESS_HTTP:
//						break;
//					case HttpStaticApi.FAILURE_HTTP:
////						ToastUtil.makeShortText(context, "别名设置失败");
//						break;
//					default:
//						break;
//					}
//					break;
//				default:
//					break;
//				}
//
//			}
//		}
//	};
}