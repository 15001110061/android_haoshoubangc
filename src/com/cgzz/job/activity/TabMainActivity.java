package com.cgzz.job.activity;

import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.BadgeView;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

public class TabMainActivity extends TabActivity implements OnClickListener {
	public static TabHost tabHost;
	ImageButton buttom_1, buttom_4;
	public static ImageButton buttom_3, buttom_2;
	private static View currentButton;
	private String cityname = "", cityid = "", type = "";
	private OBDBroadcastReceiver recobdlist;
	BadgeView badge, badge1;
	public GlobalVariables application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabmain);
		application = (GlobalVariables) getApplicationContext();
		Utils.closeActivity();
		Intent intents = getIntent();
		releaseBroadcastReceiver();
		cityname = intents.getStringExtra("cityname");
		cityid = intents.getStringExtra("cityid");
		type = intents.getStringExtra("type");
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		// MainHomeActivity MainHomePageFragment
		intent = new Intent().setClass(this, MainHomeActivity.class);// 首页
		intent.putExtra("cityname", cityname);
		intent.putExtra("cityid", cityid);
		spec = tabHost.newTabSpec("1").setIndicator("1").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MainOrdersFragment.class);// 订单
		spec = tabHost.newTabSpec("2").setIndicator("2").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MainConsultingFragment.class);// 咨询
		spec = tabHost.newTabSpec("3").setIndicator("3").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, MainMyFragment.class);// 我的
		spec = tabHost.newTabSpec("4").setIndicator("4").setContent(intent);
		tabHost.addTab(spec);

		buttom_1 = (ImageButton) findViewById(R.id.buttom_1);
		buttom_2 = (ImageButton) findViewById(R.id.buttom_2);
		buttom_3 = (ImageButton) findViewById(R.id.buttom_3);
		buttom_4 = (ImageButton) findViewById(R.id.buttom_4);

		badge = new BadgeView(this, buttom_4);
		badge.setText("");
		badge.setTextSize(1);
		badge.setTextColor(this.getResources().getColor(R.color.common_dc2f2c));
		badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badge.setBackgroundResource(R.drawable.redpoint_icon);

		badge1 = new BadgeView(this, buttom_1);
		badge1.setText("");
		badge1.setTextSize(1);
		badge1.setTextColor(this.getResources().getColor(R.color.common_dc2f2c));
		badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badge1.setBackgroundResource(R.drawable.redpoint_icon);

		if (application.isLogon()) {
			if (application.isReddothome()) {
				badge1.show(true);
			} else {
				badge1.hide();
			}

			if (application.isReddot()) {
				badge.show(true);
			} else {
				badge.hide();
			}
		}

		buttom_1.setOnClickListener(this);
		buttom_2.setOnClickListener(this);
		buttom_3.setOnClickListener(this);
		buttom_4.setOnClickListener(this);

		if ("2".equals(type)) {
			tabHost.setCurrentTab(1);
			buttom_2.performClick();
		} else {
			tabHost.setCurrentTab(0);
			buttom_1.performClick();
		}
	}

	@Override
	public void onClick(View arg0) {
		try {
			switch (arg0.getId()) {
			case R.id.buttom_1://
				tabHost.setCurrentTabByTag("1");
				break;
			case R.id.buttom_2://
				tabHost.setCurrentTabByTag("2");
				break;
			case R.id.buttom_3://
				tabHost.setCurrentTabByTag("3");
				break;
			case R.id.buttom_4://
				tabHost.setCurrentTabByTag("4");
				break;
			default:
				break;
			}
			setButton(arg0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}

	public static void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	private void releaseBroadcastReceiver() {
		recobdlist = new OBDBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cgzz.job.accesstype");
		registerReceiver(recobdlist, filter);
	}

	private class OBDBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String TYPE = bundle.getString("TYPE");
			if ("reddot".equals(TYPE)) {
				String isReddot = bundle.getString("isReddot");
				if (application.isLogon()) {

					if ("1".equals(isReddot)) {

						badge.show(true);
						application.setReddot(true);
					} else if ("0".equals(isReddot)) {
						badge.hide();
						application.setReddot(false);
					}
				}
			} else if ("reddothome".equals(TYPE)) {
				String isReddot = bundle.getString("isReddot");
				if (application.isLogon()) {

					if ("1".equals(isReddot)) {
						badge1.show(true);
						application.setReddothome(true);
					} else if ("0".equals(isReddot)) {
						badge1.hide();
						application.setReddothome(false);
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (recobdlist != null)
			unregisterReceiver(recobdlist);

		super.onDestroy();
	}

	private long firstTime = 0;

	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 1000) { // 如果两次按键时间间隔大于2秒，则不退出
			ToastUtil.makeShortText(this, "再按一次退出程序");
			firstTime = secondTime;// 更新firstTime
		} else {
			// 两次按键小于2秒时，退出应用
			Utils.closeActivity();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// 需要监听的事件
			onBackPressed();
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

}