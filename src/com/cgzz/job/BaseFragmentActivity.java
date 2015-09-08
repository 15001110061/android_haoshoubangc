package com.cgzz.job;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.utils.Utils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class BaseFragmentActivity extends FragmentActivity {
	protected ProgressDialog waitDialog;
	public GlobalVariables application;
	protected final int TITLE_TYPE_TEXT = 0;
	protected final int TITLE_TYPE_IMG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		Utils.addActivity(this);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
		application = (GlobalVariables) getApplicationContext();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * ȫ�ֵȴ��Ի���
	 */
	public void showWaitDialog() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitDialog == null || !waitDialog.isShowing()) {
					waitDialog = new ProgressDialog(BaseFragmentActivity.this);
					waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					waitDialog.setCanceledOnTouchOutside(false);
					ImageView view = new ImageView(BaseFragmentActivity.this);
					view.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					Animation loadAnimation = AnimationUtils.loadAnimation(
							BaseFragmentActivity.this, R.anim.rotate);
					view.startAnimation(loadAnimation);
					loadAnimation.start();
					view.setImageResource(R.drawable.picture_loading);
					// waitDialog.setCancelable(false);
					waitDialog.show();
					waitDialog.setContentView(view);
				}

			}
		});

	}

	public void dismissDialog() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitDialog != null && waitDialog.isShowing()) {
					waitDialog.dismiss();
					waitDialog = null;
				}
			}
		});

	}

	protected void setTitle(Object title, boolean left, int leftType, Object l,
			boolean right, int rightType, Object r) {
		try {
			TextView tvTitle = (TextView) findViewById(R.id.tv_title);
			TextView tvLeft = (TextView) findViewById(R.id.tv_title_left);
			LinearLayout llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
			ImageView ivLeft = (ImageView) findViewById(R.id.iv_title_left);
			TextView tvRight = (TextView) findViewById(R.id.tv_title_right);
			ImageView ivRight = (ImageView) findViewById(R.id.iv_title_right);
			LinearLayout llRight = (LinearLayout) findViewById(R.id.ll_title_right);
			if (title != null && title instanceof String) {
				if (!TextUtils.isEmpty((String) title)) {
					tvTitle.setVisibility(View.VISIBLE);
					tvTitle.setText((String) title);
				} else {
					tvTitle.setVisibility(View.INVISIBLE);
				}
			} else if (title != null && title instanceof Integer) {
				if (((Integer) title) > 0) {
					tvTitle.setVisibility(View.VISIBLE);
					tvTitle.setText((Integer) title);
				} else {
					tvTitle.setVisibility(View.INVISIBLE);
				}

			}
			if (left) {
				llLeft.setVisibility(View.VISIBLE);
				if (leftType == TITLE_TYPE_TEXT) {
					ivLeft.setVisibility(View.GONE);
					tvLeft.setVisibility(View.VISIBLE);
					if (l instanceof String) {
						tvLeft.setText((String) l);
					} else if (l instanceof Integer) {
						tvLeft.setText((Integer) l);
					}
				} else if (leftType == TITLE_TYPE_IMG) {
					ivLeft.setVisibility(View.VISIBLE);
					tvLeft.setVisibility(View.GONE);
					if (l instanceof Integer) {
						ivLeft.setImageResource((Integer) l);
					}
				}
			} else {
				llLeft.setVisibility(View.INVISIBLE);
			}
			if (right) {
				llRight.setVisibility(View.VISIBLE);
				if (rightType == TITLE_TYPE_TEXT) {
					ivRight.setVisibility(View.GONE);
					tvRight.setVisibility(View.VISIBLE);
					if (r instanceof String) {
						tvRight.setText((String) r);
					} else if (r instanceof Integer) {
						tvRight.setText((Integer) r);
					}
				} else if (rightType == TITLE_TYPE_IMG) {
					ivRight.setVisibility(View.VISIBLE);
					tvRight.setVisibility(View.GONE);
					if (r instanceof Integer) {
						ivRight.setImageResource((Integer) r);
					}
				}
			} else {
				llRight.setVisibility(View.INVISIBLE);
			}

		} catch (Exception e) {

		}
	}
}
