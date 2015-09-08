package com.cgzz.job.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
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
import com.cgzz.job.utils.ScreenShot;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.view.TitleBarView;

/**
 * 用于显示简单的html的界面.传递title和url即可.
 * 
 * 
 */
public class WebPathActivity extends BaseActivity implements OnClickListener {
	/**
	 * 显示的标题.
	 */
	public static final String ACTION_KEY_TITLE = "action_key_title";
	/**
	 * 加载的的url
	 */
	public static final String ACTION_KEY_URL = "action_key_url";
	private WebView webview;
	private ProgressBar progressbar;
	private String title, url, longitude, latitude, name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path);
		Intent intent = getIntent();
		title = intent.getStringExtra(ACTION_KEY_TITLE);
		url = intent.getStringExtra(ACTION_KEY_URL);
		name = intent.getStringExtra("name");
		latitude = intent.getStringExtra("latitude");
		longitude = intent.getStringExtra("longitude");
		// setTitle(title, true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
		// TITLE_TYPE_TEXT, "截屏");
		initView();
	}

	LinearLayout llLeft, llright;
	TitleBarView mTitleBarView;

	private void initView() {
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient());
		webview.loadUrl(url);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);

		mTitleBarView.getTitleLeft().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleLeft().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(false);
					mTitleBarView.getTitleRight().setEnabled(true);
					String url = "http://m.amap.com/?from="
							+ application.getLatitude() + ","
							+ application.getLongitude() + "(" + "当前" + ")&to="
							+ latitude + "," + longitude + "(" + name
							+ ")&type=1&opt=1";
					webview.loadUrl(url);
				}
			}
		});

		mTitleBarView.getTitleRight().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleRight().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(true);
					mTitleBarView.getTitleRight().setEnabled(false);
					if (!"".equals(application.getHomeLatitude())) {
						String url = "http://m.amap.com/?from="
								+ application.getLatitude() + ","
								+ application.getLongitude() + "(" + "家"
								+ ")&to=" + application.getHomeLatitude() + ","
								+ application.getHomeLongitude() + "(" + name
								+ ")&type=1&opt=1";
						webview.loadUrl(url);
					} else {
						ToastUtil.makeShortText(WebPathActivity.this,
								"还未设置家的位置,请在个人信息中填写");
						mTitleBarView.getTitleLeft().setEnabled(false);
						mTitleBarView.getTitleRight().setEnabled(true);
					}

				}

			}
		});
		mTitleBarView.getTitleLeft().performClick();
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
//			webview.destroy();
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
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right://
			try {

				String fileName = String.valueOf(System.currentTimeMillis())
						+ ".png";
				// String filePath = HttpStaticApi.Send_TheirProfile + "/"
				// + fileName;

				File path = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				File outputImage = new File(path, fileName);
				ScreenShot.shoot(this, outputImage);
				ToastUtil.makeShortText(WebPathActivity.this, "截屏成功");
				try {
					sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
							Uri.parse("file://"
									+ Environment.getExternalStorageDirectory())));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			} catch (Exception e) {
				ToastUtil.makeShortText(WebPathActivity.this, "截屏失败");
			} 

			break;

		default:
			break;
		}

	}
	  private String getSDAvailableSize() {  
	        File path = Environment.getExternalStorageDirectory();  
	        StatFs stat = new StatFs(path.getPath());  
	        long blockSize = stat.getBlockSize();  
	        long availableBlocks = stat.getAvailableBlocks();  
	        return Formatter.formatFileSize(WebPathActivity.this, blockSize * availableBlocks);  
	    }  
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
