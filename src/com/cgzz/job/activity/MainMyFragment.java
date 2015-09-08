package com.cgzz.job.activity;

import java.util.HashMap;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.cgzz.job.view.BadgeView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm 主页：我
 */
public class MainMyFragment extends BaseActivity implements OnClickListener {
	private TextView tv_my_name, tv_my_describe, iv_my_income, iv_my_collection, iv_my_news, iv_my_recommend,
			tv_my_seting, iv_seting_about, iv_seting_opinion, tv_their_tel;
	// ImageView iv_my_pic;
	com.cgzz.job.view.CustomerRatingBarGo room_ratingbar_my_car2;
	RelativeLayout rl_my_workcard;

	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.orderNum_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserOrderNum(data);
					tv_my_describe.setText("共接了" + bundle.getString("workstatics").toString() + "单");
					application.setOrderNum(bundle.getString("workstatics").toString());
					application.setXinyong(bundle.getString("xinyong").toString());

					if ("1".equals(bundle.get("havebank").toString())) {
						badge3.hide();
					} else {
						badge3.show(true);
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					dismissWaitDialog();
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					dismissWaitDialog();
					bundle = ParserUtil.ParserOrderNum(data);
					ToastUtil.makeShortText(MainMyFragment.this, bundle.get("msg").toString());
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_my);
		// setTitle("好手帮", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
		// TITLE_TYPE_TEXT, "注册");
		releaseBroadcastReceiver();
		findView();
		init();
		Assignment();

		addWXPlatform();
		addSMS();

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (application.isLogon()) {
			orderNum(UrlConfig.orderNum_Http, application.getToken(), application.getUserId(), true);
			tv_my_name.setText(application.getRealname());
		} else {
		}

	}

	BadgeView badge, badge3;
	// ShareUtil shareUtil;
	private PopupWindow pwShare;
	private View viewShare;
	private RelativeLayout rlMessage, rlWechat, rlWeMoment, rl_my_1, rl_my_2, rl_my_xiaoxi;
	ImageView iv_my_pic;
	TextView iv_my_xiaoxi, iv_my_xiaoxi2, iv_my_xiaoxi3;
	ImageButton dis;

	private void findView() {
		// iv_my_pic = (ImageView) findViewById(R.id.iv_my_pic);
		rl_my_1 = (RelativeLayout) findViewById(R.id.rl_my_1);
		rl_my_2 = (RelativeLayout) findViewById(R.id.rl_my_2);

		if (application.isLogon()) {
			rl_my_1.setVisibility(View.VISIBLE);
			rl_my_2.setVisibility(View.GONE);
		} else {
			rl_my_1.setVisibility(View.GONE);
			rl_my_2.setVisibility(View.VISIBLE);

		}
		iv_my_pic = (ImageView) findViewById(R.id.iv_my_pic);
		room_ratingbar_my_car2 = (com.cgzz.job.view.CustomerRatingBarGo) findViewById(R.id.room_ratingbar_my_car2);
		rl_my_workcard = (RelativeLayout) findViewById(R.id.rl_my_workcard);
		tv_my_name = (TextView) findViewById(R.id.tv_my_name);
		tv_my_describe = (TextView) findViewById(R.id.tv_my_describe);
		iv_seting_about = (TextView) findViewById(R.id.iv_seting_about);
		iv_my_income = (TextView) findViewById(R.id.iv_my_income);
		iv_my_collection = (TextView) findViewById(R.id.iv_my_collection);
		iv_my_news = (TextView) findViewById(R.id.iv_my_news);
		iv_my_recommend = (TextView) findViewById(R.id.iv_my_recommend);
		// iv_my_data = (TextView) findViewById(R.id.iv_my_data);
		tv_my_seting = (TextView) findViewById(R.id.tv_my_seting);
		iv_seting_opinion = (TextView) findViewById(R.id.iv_seting_opinion);
		iv_my_xiaoxi = (TextView) findViewById(R.id.iv_my_xiaoxi);
		rl_my_xiaoxi = (RelativeLayout) findViewById(R.id.rl_my_xiaoxi);

		iv_my_xiaoxi2 = (TextView) findViewById(R.id.iv_my_xiaoxi2);
		iv_my_xiaoxi3 = (TextView) findViewById(R.id.iv_my_xiaoxi3);
		badge = new BadgeView(this, iv_my_xiaoxi2);
		badge.setText("");
		badge.setTextSize(1);
		badge.setTextColor(this.getResources().getColor(R.color.common_dc2f2c));
		badge.setBadgePosition(BadgeView.POSITION_CENTER);
		badge.setBackgroundResource(R.drawable.redpoint_icon);

		badge3 = new BadgeView(this, iv_my_xiaoxi3);
		badge3.setText("");
		badge3.setTextSize(1);
		badge3.setTextColor(this.getResources().getColor(R.color.common_dc2f2c));
		badge3.setBadgePosition(BadgeView.POSITION_CENTER);
		badge3.setBackgroundResource(R.drawable.redpoint_icon);

		if (application.isLogon()) {

			if (application.isReddot()) {
				badge.show(true);
			} else {
				badge.hide();
			}
		}

		// ShareSDK.initSDK(this);
		// shareUtil = new ShareUtil(this, this);

		viewShare = LayoutInflater.from(this).inflate(R.layout.share_popup, null);
		rlWechat = (RelativeLayout) viewShare.findViewById(R.id.relative_wechat);
		rlWeMoment = (RelativeLayout) viewShare.findViewById(R.id.relative_wemoment);
		rlMessage = (RelativeLayout) viewShare.findViewById(R.id.relative_shortmessage);
		dis = (ImageButton) viewShare.findViewById(R.id.ib_dis);
		pwShare = new PopupWindow(viewShare);
		pwShare.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		pwShare.setOutsideTouchable(true);
		pwShare.setFocusable(true);
		pwShare.setAnimationStyle(R.style.MyPopupAnimation);

		tv_my_describe.setText("共接了" + application.getOrderNum() + "单");
	}

	private void init() {
		rl_my_workcard.setOnClickListener(this);
		iv_my_income.setOnClickListener(this);
		iv_my_news.setOnClickListener(this);
		iv_my_collection.setOnClickListener(this);
		tv_my_seting.setOnClickListener(this);
		iv_my_recommend.setOnClickListener(this);
		iv_seting_about.setOnClickListener(this);
		iv_seting_opinion.setOnClickListener(this);
		iv_my_pic.setOnClickListener(this);
		room_ratingbar_my_car2.setOnClickListener(this);
		tv_my_describe.setOnClickListener(this);
		tv_my_name.setOnClickListener(this);
		rlMessage.setOnClickListener(this);
		rlWechat.setOnClickListener(this);
		rlWeMoment.setOnClickListener(this);
		// iv_my_xiaoxi.setOnClickListener(this);
		rl_my_xiaoxi.setOnClickListener(this);
		rl_my_2.setOnClickListener(this);
		dis.setOnClickListener(this);
		// 生成二维码
		// Bitmap qrCodeBitmap = null;
		// try {
		// qrCodeBitmap = EncodingHandler.Create2DCode("我的天那");
		// } catch (WriterException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// if (qrCodeBitmap != null)
		// iv_workcard.setImageBitmap(qrCodeBitmap);
	}

	// public ImageLoader mImageLoader;
	private void Assignment() {
		// mImageLoader = new ImageLoader(
		// GlobalVariables.getRequestQueue(this), new BitmapCache());
		// 头像
		if (!Utils.isEmpty(application.getFaceUrl())) {
			ImageListener listener = ImageLoader.getImageListener(iv_my_pic, R.drawable.icon_touxiangmoren,
					R.drawable.icon_touxiangmoren);
			mImageLoader.get(application.getFaceUrl(), listener);
		}

		room_ratingbar_my_car2.setProgress(Integer.parseInt(application.getStarlevel()));

		tv_my_name.setText(application.getRealname());

		room_ratingbar_my_car2.setProgress(Integer.parseInt(application.getStarlevel()));
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.iv_my_income:// 收入
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, MyIncomeActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}

			break;
		case R.id.iv_my_collection:// 我的收藏
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, CollectionActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}

			break;
		case R.id.iv_my_recommend:// 推荐给好友
			if (application.isLogon()) {
				pwShare.setWidth(LayoutParams.MATCH_PARENT);
				pwShare.setHeight(LayoutParams.WRAP_CONTENT);
				pwShare.showAtLocation(arg0, Gravity.BOTTOM, 0, 0);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}

			break;
		case R.id.rl_my_workcard:// 查看工牌
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, WorkcardActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}

			break;

		case R.id.tv_my_seting:// 更多设置
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, SetingActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}

			break;

		case R.id.iv_seting_about:// 关于

			intent = new Intent(MainMyFragment.this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_seting_opinion:// 意见反馈
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, OpinionActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}

			break;
		case R.id.tv_my_name:// 基本资料
		case R.id.tv_my_describe:
		case R.id.room_ratingbar_my_car2:
		case R.id.iv_my_pic:
			intent = new Intent(MainMyFragment.this, TheirProfileActivity.class);
			startActivityForResult(intent, 1);
			break;

		case R.id.relative_wechat:// 微信好友
			pwShare.dismiss();
			UMImage urlImage = new UMImage(MainMyFragment.this, R.drawable.ic_splash);

			WeiXinShareContent weixinContent = new WeiXinShareContent();
			weixinContent.setShareContent("刚刚注册了好手帮手机应用，轻松找到了酒店客房清洁工作，工资月结，收入比以前增加不少，给你推荐一下。");
			weixinContent.setTitle("“好手帮”手机软件能帮你找到很多酒店清洁工作");
			weixinContent.setTargetUrl("http://www.haoshoubang.com/m/index.html");
			weixinContent.setShareMedia(urlImage);
			mController.setShareMedia(weixinContent);

			mController.directShare(MainMyFragment.this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				}
			});
			break;

		case R.id.relative_wemoment:// 朋友圈
			pwShare.dismiss();
			UMImage urlImage2 = new UMImage(MainMyFragment.this, R.drawable.ic_splash);
			// 设置朋友圈分享的内容
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent("刚刚注册了好手帮手机应用，轻松找到了酒店客房清洁工作，工资月结，收入比以前增加不少，给你推荐一下。");
			circleMedia.setTitle("“好手帮”手机软件能帮你找到很多酒店清洁工作");
			circleMedia.setShareMedia(urlImage2);
			circleMedia.setTargetUrl("http://www.haoshoubang.com/m/index.html");
			mController.setShareMedia(circleMedia);

			mController.directShare(MainMyFragment.this, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				}
			});
			break;
		case R.id.relative_shortmessage:// 短信
			pwShare.dismiss();
			SmsShareContent sms = new SmsShareContent();
			sms.setShareContent("刚刚用了好手帮app，轻松请来了客房清洁人员，软件好用，做房专业。流程全跟踪，效率大大提高，还有数据反馈棒棒的。点这里下载t.im/hsbbc");
			mController.setShareMedia(sms);

			mController.directShare(MainMyFragment.this, SHARE_MEDIA.SMS, new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				}
			});
			break;

		case R.id.rl_my_2:// 登录
			intent = new Intent(MainMyFragment.this, LoginActivity.class);
			startActivity(intent);
			finish();

			break;
		case R.id.rl_my_xiaoxi:// 消息中心

			if (application.isLogon()) {
				redDot();
				intent = new Intent(MainMyFragment.this, MessageCenterActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "请先进行登录");
			}
			break;

		case R.id.ib_dis://
			pwShare.dismiss();

			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String ischenggong = data.getStringExtra("ischenggong");
			// 根据上面发送过去的请求吗来区别
			switch (requestCode) {
			case 1:
				if (ischenggong.equals("y")) {
					// 头像
					ImageListener listener = ImageLoader.getImageListener(iv_my_pic, R.drawable.icon_touxiangmoren,
							R.drawable.icon_touxiangmoren);
					mImageLoader.get(application.getFaceUrl(), listener);
				} else {

				}
				break;
			default:
				break;
			}
		}

	}

	private void orderNum(String url, String token, String userid, boolean loadedtype) {
		if (!userid.equals("")) {
			HashMap map = new HashMap<String, String>();
			map.put("apptype", "2");
			map.put("token", token);
			map.put("userid", userid);
			AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainMyFragment.this, url, map, callbackData,
					GlobalVariables.getRequestQueue(MainMyFragment.this), HttpStaticApi.orderNum_Http, null,
					loadedtype);
		}

	}

	OBDBroadcastReceiver recobdlist;

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
			if (TYPE.equals("reddot")) {
				String isReddot = bundle.getString("isReddot");
				if (isReddot.equals("1")) {
					badge.show(true);
					application.setReddot(true);
				} else if (isReddot.equals("0")) {
					badge.hide();
					application.setReddot(false);
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

	public void redDot() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "reddot");
		intentobd.putExtra("isReddot", "0");// 1显示0隐藏
		sendBroadcast(intentobd);
	}

	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return AppID：wx21282fa0dad9d60e
	 *         AppSecret：2dbe367ac38677e2db55fcf8f2f9bb9d
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxfe5743c125b3c59b";
		String appSecret = "b7a9a76b0385c971cb1d67f9d3cec1ee";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(MainMyFragment.this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(MainMyFragment.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		wxCircleHandler.showCompressToast(false);
	}

	/**
	 * 添加短信平台
	 */
	private void addSMS() {
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}
}
