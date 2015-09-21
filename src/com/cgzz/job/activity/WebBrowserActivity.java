package com.cgzz.job.activity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.utils.ScreenShot;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;

/**
 * ������ʾ�򵥵�html�Ľ���.����title��url����.
 * 
 * 
 */
public class WebBrowserActivity extends BaseActivity implements OnClickListener {
	/**
	 * ��ʾ�ı���.
	 */
	public static final String ACTION_KEY_TITLE = "action_key_title";
	/**
	 * ���صĵ�url
	 */
	public static final String ACTION_KEY_URL = "action_key_url";
	private WebView webview;
	private ProgressBar progressbar;
	private String title, url, type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		Intent intent = getIntent();
		title = intent.getStringExtra(ACTION_KEY_TITLE);
		url = intent.getStringExtra(ACTION_KEY_URL);
		type = intent.getStringExtra("type");
		setTitle("" + title, true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		initView();
	}

	LinearLayout llLeft, llright;

	private void initView() {

		webview = (WebView) findViewById(R.id.webview);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient());
		webview.loadUrl(url);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(View.GONE);
			} else {
				if (progressbar.getVisibility() == View.GONE)
					progressbar.setVisibility(View.VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			webview.goBack();
			return true;
		} else if (!webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			// webview.destroy();
			this.finish();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		webview.destroy();
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_title_left:// ����
			onBackPressed() ;
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = null;
		super.onBackPressed();
		if (!Utils.isEmpty(type) && "0".equals(type)) {
			intent = new Intent(WebBrowserActivity.this, BonusListActivity.class);
			startActivity(intent);
		}
		finish();
	}
}
