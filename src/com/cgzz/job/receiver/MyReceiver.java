package com.cgzz.job.receiver;

import java.util.List;

import com.cgzz.job.activity.BonusActivity;
import com.cgzz.job.activity.MessageCenterActivity;
import com.cgzz.job.activity.SingleActivity;
import com.cgzz.job.activity.TabMainActivity;
import com.cgzz.job.activity.WagesActivity;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.utils.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

/**
 * �Զ��������
 * 
 */
public class MyReceiver extends BroadcastReceiver {
	Bundle bundles = null;
	public GlobalVariables application;

	@Override
	public void onReceive(Context context, Intent intent) {

		application = (GlobalVariables) context.getApplicationContext();
		Bundle bundle = intent.getExtras();

		// System.out.println("wjm=Jpush==onReceive :" + intent.getAction() + ",
		// extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			// String regId =
			// bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// System.out.println("wjm=Jpush==����Registration Id :" + regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			// System.out.println("wjm=Jpush===���յ������������Զ�����Ϣ:" +
			// bundle.getString(JPushInterface.EXTRA_MESSAGE) + "==="
			// + bundle.getString(JPushInterface.EXTRA_EXTRA));
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// System.out.println("wjm=Jpush==���յ�����������֪ͨ��ID:" + notifactionId +
			// "===" + bundle);
			try {
				bundles = ParserUtil.JGlJSONC(bundle.getString(JPushInterface.EXTRA_EXTRA));

				if (bundles != null && "5".equals(bundles.getString("type"))) {
					application.setReddot(true);
					redDot(context);
				} else if (bundles != null && "1".equals(bundles.getString("type"))) {
					redDothome(context);
					// TTSController.getInstance(context).playText("����һ���¶���");
				} else if (bundles != null && "4".equals(bundles.getString("type"))) {
					// TTSController.getInstance(context).playText("����һ�����ڶ���");
				} else if (bundles != null && "6".equals(bundles.getString("type"))) {
					JPushInterface.clearNotificationById(context, notifactionId);

					if (!Utils.isEmpty(bundles.getString("homemessage"))) {
						application.setHomemessage(bundles.getString("homemessage"));
						redDothomeMssage(context);
					}

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			// System.out.println("wjm===EXTRA_EXTRA=" +
			// bundle.getString(JPushInterface.EXTRA_EXTRA) +
			// "===EXTRA_MESSAGE="
			// + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			try {
				bundles = ParserUtil.JGlJSONC(bundle.getString(JPushInterface.EXTRA_EXTRA));

				if (bundles != null && "1".equals(bundles.getString("type"))) {//SingleActivity
					Intent intents = new Intent(context, SingleActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intents.putExtra("orderid", bundles.getString("orderid"));
					intents.putExtra("type", bundles.getString("type"));
					context.startActivity(intents);
				} else if (bundles != null && "3".equals(bundles.getString("type"))) {
					Intent intents = new Intent(context, WagesActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intents.putExtra("orderid", bundles.getString("orderid"));
					context.startActivity(intents);
				} else if (bundles != null && "2".equals(bundles.getString("type"))) {
					Intent intents = new Intent(context, TabMainActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intents);
				} else if (bundles != null && "4".equals(bundles.getString("type"))) {
					Intent intents = new Intent(context, SingleActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intents.putExtra("orderid", bundles.getString("orderid"));
					intents.putExtra("type", bundles.getString("type"));
					intents.putExtra("orderDetailId", bundles.getString("orderDetailId"));
					context.startActivity(intents);
				} else if (bundles != null && "5".equals(bundles.getString("type"))) {
					redDot0(context);
					Intent intents = new Intent(context, MessageCenterActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intents);

				}else if (bundles != null && "7".equals(bundles.getString("type"))) {
					Intent intents = new Intent(context, BonusActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intents.putExtra("redid", bundles.getString("redid"));
					context.startActivity(intents);

				}
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			// ��������� JPushInterface.EXTRA_EXTRA �����ݴ�����룬������µ�Activity��
			// ��һ����ҳ��..
			// System.out.println("wjm=Jpush==�û��յ���RICH PUSH CALLBACK:" +
			// bundle.getString(JPushInterface.EXTRA_EXTRA));
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			// boolean connected =
			// intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE,
			// false);
		} else {
		}
	}

	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	public void redDot(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "reddot");
		intentobd.putExtra("isReddot", "1");// 1��ʾ
		context.sendBroadcast(intentobd);
	}

	public void redDot0(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "reddot");
		intentobd.putExtra("isReddot", "0");// 1��ʾ
		context.sendBroadcast(intentobd);
	}

	public void redDothome(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "reddothome");
		intentobd.putExtra("isReddot", "1");// 1��ʾ
		context.sendBroadcast(intentobd);
	}

	public void redDothomeMssage(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "reddothomemessage");
		context.sendBroadcast(intentobd);
	}

	/***
	 * ջ����Activity����
	 */
	public String getActivityName(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String packageName = cn.getClassName();
		return packageName;
	}

	public boolean isRunningForeground(Context c) {
		String packageName = getPackageName(c);
		String topActivityClassName = getTopActivityName(c);
		if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
			return true;
		} else {
			return false;
		}
	}

	public String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	public String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}
}
