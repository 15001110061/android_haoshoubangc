package com.cgzz.job.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.cgzz.job.application.GlobalVariables;

/**
 * δʹ�� �������� 3g����wifi
 */
public class NetworkStatusReceiver extends BroadcastReceiver {
	public GlobalVariables application;
	private static final int MSG_SET_ALIAS = 1001;
	Context context;
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";//����
	static final String ACTION2 ="android.net.conn.CONNECTIVITY_CHANGE";//����
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
				// �ֻ��������ӳɹ�
				NetworkReceiver(context);
			} else if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {
				// �ֻ�û���κε�����
			} else if (wifiState != null && State.CONNECTED == wifiState) {
				// �����������ӳɹ�
				NetworkReceiver(context);
			

			}
		}else if(intent.getAction().equals(ACTION)){
		}
	

	}

	public void NetworkReceiver(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "Network");
		intentobd.putExtra("isNetwork", "1");//1������
		context.sendBroadcast(intentobd);
	}



//	/**
//	 * �첽�ص���������������
//	 */
//	private ObserverCallBack callbackData = new ObserverCallBack() {
//		public void back(String data, int encoding, int method, Object obj) {
//			switch (method) {// �ж��ĸ�����������
//			case HttpStaticApi.logon_Http:
//				switch (encoding) {// �жϴ˷������صĽ��
//
//				case HttpStaticApi.setRid_Http: // ���ñ����ɹ�
//					switch (encoding) {// �жϴ˷������صĽ��
//					case HttpStaticApi.SUCCESS_HTTP:
//						break;
//					case HttpStaticApi.FAILURE_HTTP:
////						ToastUtil.makeShortText(context, "��������ʧ��");
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