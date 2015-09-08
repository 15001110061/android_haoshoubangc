//package com.cgzz.job.utils;
//
//import java.util.HashMap;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Toast;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//
///**
// * ��������ص�����
// */
//public class ShareUtil {
//	private Context context;
//	private ShareHandler handler;
//	private SharePlatformActionListener platformActionListener;
//	private CanUseAPI useApi;
//
//	public ShareUtil(Context context, CanUseAPI useApi) {
//		this.context = context;
//		this.useApi = useApi;
//		handler = new ShareHandler();
//		platformActionListener = new SharePlatformActionListener();
//	}
//
//	/**
//	 * �õ��������
//	 */
//	public PlatformActionListener getPlatformActionListener() {
//		return platformActionListener;
//	}
//
//	// �������
//	class SharePlatformActionListener implements PlatformActionListener {
//		@Override
//		public void onCancel(Platform arg0, int arg1) {
//			Message msg = new Message();
//			msg.arg1 = 0;
//			handler.sendMessage(msg);
//		}
//
//		@Override
//		public void onComplete(Platform arg0, int arg1,
//				HashMap<String, Object> arg2) {
//			Message msg = new Message();
//			msg.arg1 = 1;
//			handler.sendMessage(msg);
//		}
//
//		@Override
//		public void onError(Platform arg0, int action, Throwable t) {
//			t.printStackTrace();
//
//			Message msg = new Message();
//			msg.arg1 = 2;
//			msg.arg2 = action;
//			msg.obj = t;
//			handler.sendMessage(msg);
//		}
//	}
//
//	// ����ʹ��
//	class ShareHandler extends Handler {
//		public void handleMessage(Message msg) {
//			if (msg.arg1 == 0) {
//				ToastUtil.makeShortText(context, "ȡ������");
//			} else if (msg.arg1 == 1) {
//				ToastUtil.makeShortText(context, "����ɹ�");
//				// ����ӿ�
//				useApi.startApi();
//			}
//			if (msg.arg1 == 2) {
//				String text = actionToString(msg.arg2);
//				// ʧ��
//				if ("WechatClientNotExistException".equals(msg.obj.getClass()
//						.getSimpleName())) {
//					text = "Ŀǰ����΢�Ű汾���ͻ�δ��װ΢�ţ���Ҫ��װ΢�Ų���ʹ��";
//				} else if ("WechatTimelineNotSupportedException".equals(msg.obj
//						.getClass().getSimpleName())) {
//					text = "Ŀǰ����΢�Ű汾���ͻ�δ��װ΢�ţ���Ҫ��װ΢�Ų���ʹ��";
//				} else {
//					text = "����ʧ��";
//				}
//				ToastUtil.makeShortText(context, text);
//			}
//		};
//	}
//
//	/** ��actionת��ΪString */
//	public static String actionToString(int action) {
//		switch (action) {
//		case Platform.ACTION_AUTHORIZING:
//			return "ACTION_AUTHORIZING";
//		case Platform.ACTION_GETTING_FRIEND_LIST:
//			return "ACTION_GETTING_FRIEND_LIST";
//		case Platform.ACTION_FOLLOWING_USER:
//			return "ACTION_FOLLOWING_USER";
//		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
//			return "ACTION_SENDING_DIRECT_MESSAGE";
//		case Platform.ACTION_TIMELINE:
//			return "ACTION_TIMELINE";
//		case Platform.ACTION_USER_INFOR:
//			return "ACTION_USER_INFOR";
//		case Platform.ACTION_SHARE:
//			return "ACTION_SHARE";
//		default:
//			return "UNKNOWN";
//		}
//	}
//
//	/**
//	 * ���ýӿڻص�
//	 * 
//	 * @author Administrator
//	 */
//	public interface CanUseAPI {
//		public void startApi();
//	}
//}
