package com.cgzz.job.activity;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.bean.DownloadInfo;
import com.cgzz.job.http.DownloadManager;
import com.cgzz.job.http.DownloadService;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.utils.ToastUtil;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

/**
 * 培训与留言
 */
public class TrainingAndMessageActivity extends BaseActivity implements
		OnClickListener {
	private DownloadManager downloadManager;
	private String voiceurl = "", message = "";
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.detail_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserDetail(data);

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserDetail(data);
					ToastUtil.makeShortText(TrainingAndMessageActivity.this,
							bundle.get("msg").toString());
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainingandmessage);
		application = (GlobalVariables) getApplicationContext();

		downloadManager = DownloadService
				.getDownloadManager(TrainingAndMessageActivity.this);

		Intent intent = getIntent();
		voiceurl = intent.getStringExtra("voiceurl");
		message = intent.getStringExtra("message");
//		seconds = intent.getStringExtra("seconds");
		setTitle("培训与留言", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");

		initView();
		init();

	}

	LinearLayout llLeft, llright;
	RelativeLayout rl_tam_1, rl_tam_2;
	TextView tv_tam_liuyan;
	ProgressBar progressbar;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		rl_tam_1 = (RelativeLayout) findViewById(R.id.rl_tam_1);
		rl_tam_2 = (RelativeLayout) findViewById(R.id.rl_tam_2);
		tv_tam_liuyan = (TextView) findViewById(R.id.tv_tam_liuyan);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		if ("".equals(message)) {
			rl_tam_2.setVisibility(View.GONE);
		} else {
			rl_tam_2.setVisibility(View.VISIBLE);
			tv_tam_liuyan.setText(message);
		}
		if ("".equals(voiceurl)) {
			rl_tam_1.setVisibility(View.GONE);
		} else {
			rl_tam_1.setVisibility(View.VISIBLE);
		}
		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		int w = d.getWidth();
		// 判断文件是否存在
		// String url = HttpStaticApi.Send_Audio + "/" + getFileName(voiceurl);
		// if (fileIsExists(url)) {
		// // 播放
		// progressbar.setVisibility(View.GONE);
		// } else {
		download(getFileName(voiceurl), voiceurl);
		// }

	}

	private void init() {
		llLeft.setOnClickListener(this);
		rl_tam_1.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.rl_tam_1://
			String url = HttpStaticApi.Send_Audio + "/" + getFileName(voiceurl);
			if (fileIsExists(url)) {
				// 播放
				// progressbar.setVisibility(View.GONE);

				MediaPlayer2(url);
			} else {

			}
			break;

		default:
			break;
		}

	}

	DownloadInfo downloadInfo = null;

	private void download(String name, String DownloadUrl) {
		String target = HttpStaticApi.Send_Audio + "/" + name;
		try {
			// downloadManager.addNewDownload("http://www.haoshoubang.com/bangke/voice/1.mp3",
			// "力卓文件",
			// target,
			// true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
			// true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
			// null);

			downloadInfo = new DownloadInfo();

			downloadInfo.setDownloadUrl(DownloadUrl);
			downloadInfo.setAutoRename(true);
			downloadInfo.setAutoResume(true);
			downloadInfo.setFileName(name);
			downloadInfo.setFileSavePath(target);

			downloadManager.resumeDownload(downloadInfo,
					new RequestCallBack<File>() {

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							progressbar.setVisibility(View.GONE);
							// 播放

							MediaPlayer2(downloadInfo.getFileSavePath());

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							progressbar.setVisibility(View.GONE);
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							// TODO Auto-generated method stub
							super.onLoading(total, current, isUploading);
							if (downloadInfo.getFileLength() > 0) {
								progressbar.setProgress((int) (downloadInfo
										.getProgress() * 100 / downloadInfo
										.getFileLength()));
							} else {
								progressbar.setVisibility(View.GONE);
							}

						}
					});
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
		}

	}

	public boolean fileIsExists(String url) {

		try {
			File f = new File(url);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public static String getFileName(String pathandname) {

		int start = pathandname.lastIndexOf("/");
		int end = pathandname.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, pathandname.length());
		} else {
			return null;
		}

	}

	MediaPlayer mediaPlayer;

	public void MediaPlayer2(String url) {

		try {
			if (mediaPlayer != null) {
				mediaPlayer.stop();// 停止播放
				mediaPlayer.release();// 释放资源
				mediaPlayer = null;
			}

			if (mediaPlayer == null)
				mediaPlayer = new MediaPlayer();
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.reset();// 重置为初始状态
				mediaPlayer.stop();// 停止播放
				mediaPlayer.release();// 释放资源
			}
			try {
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepare();// 缓冲
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();// 开始或恢复播放
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// 播出完毕事件
						@Override
						public void onCompletion(MediaPlayer arg0) {
							mediaPlayer.stop();// 停止播放
							mediaPlayer.release();// 释放资源
							mediaPlayer = null;
						}
					});
			mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {// 错误处理事件
						@Override
						public boolean onError(MediaPlayer player, int arg1,
								int arg2) {
							mediaPlayer.stop();// 停止播放
							mediaPlayer.release();// 释放资源
							return false;
						}
					});

		} catch (Exception e) {
			ToastUtil.makeShortText(TrainingAndMessageActivity.this, "播放出错");
		}
	}

	private void MediaPlayer(String url) {
		Intent it = new Intent(Intent.ACTION_VIEW);
		it.setDataAndType(Uri.parse("file://" + url), "audio/MP3");
		startActivity(it);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();// 停止播放
					mediaPlayer.release();// 释放资源
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			downloadManager.stopDownload(downloadInfo);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
