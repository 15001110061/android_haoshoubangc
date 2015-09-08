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
 * @author wjm ��ҳ����
 */
public class MainMyFragment extends BaseActivity implements OnClickListener {
	private TextView tv_my_name, tv_my_describe, iv_my_income, iv_my_collection, iv_my_news, iv_my_recommend,
			tv_my_seting, iv_seting_about, iv_seting_opinion, tv_their_tel;
	// ImageView iv_my_pic;
	com.cgzz.job.view.CustomerRatingBarGo room_ratingbar_my_car2;
	RelativeLayout rl_my_workcard;

	/**
	 * �첽�ص���������������
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
					tv_my_describe.setText("������" + bundle.getString("workstatics").toString() + "��");
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
		// setTitle("���ְ�", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
		// TITLE_TYPE_TEXT, "ע��");
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
		pwShare.setBackgroundDrawable(new BitmapDrawable());// û�д˾����ⲿ������ʧ
		pwShare.setOutsideTouchable(true);
		pwShare.setFocusable(true);
		pwShare.setAnimationStyle(R.style.MyPopupAnimation);

		tv_my_describe.setText("������" + application.getOrderNum() + "��");
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
		// ���ɶ�ά��
		// Bitmap qrCodeBitmap = null;
		// try {
		// qrCodeBitmap = EncodingHandler.Create2DCode("�ҵ�����");
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
		// ͷ��
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
		case R.id.iv_my_income:// ����
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, MyIncomeActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		case R.id.iv_my_collection:// �ҵ��ղ�
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, CollectionActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		case R.id.iv_my_recommend:// �Ƽ�������
			if (application.isLogon()) {
				pwShare.setWidth(LayoutParams.MATCH_PARENT);
				pwShare.setHeight(LayoutParams.WRAP_CONTENT);
				pwShare.showAtLocation(arg0, Gravity.BOTTOM, 0, 0);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		case R.id.rl_my_workcard:// �鿴����
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, WorkcardActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;

		case R.id.tv_my_seting:// ��������
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, SetingActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;

		case R.id.iv_seting_about:// ����

			intent = new Intent(MainMyFragment.this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_seting_opinion:// �������
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, OpinionActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		case R.id.tv_my_name:// ��������
		case R.id.tv_my_describe:
		case R.id.room_ratingbar_my_car2:
		case R.id.iv_my_pic:
			intent = new Intent(MainMyFragment.this, TheirProfileActivity.class);
			startActivityForResult(intent, 1);
			break;

		case R.id.relative_wechat:// ΢�ź���
			pwShare.dismiss();
			UMImage urlImage = new UMImage(MainMyFragment.this, R.drawable.ic_splash);

			WeiXinShareContent weixinContent = new WeiXinShareContent();
			weixinContent.setShareContent("�ո�ע���˺��ְ��ֻ�Ӧ�ã������ҵ��˾Ƶ�ͷ���๤���������½ᣬ�������ǰ���Ӳ��٣������Ƽ�һ�¡�");
			weixinContent.setTitle("�����ְ�ֻ�����ܰ����ҵ��ܶ�Ƶ���๤��");
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

		case R.id.relative_wemoment:// ����Ȧ
			pwShare.dismiss();
			UMImage urlImage2 = new UMImage(MainMyFragment.this, R.drawable.ic_splash);
			// ��������Ȧ���������
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent("�ո�ע���˺��ְ��ֻ�Ӧ�ã������ҵ��˾Ƶ�ͷ���๤���������½ᣬ�������ǰ���Ӳ��٣������Ƽ�һ�¡�");
			circleMedia.setTitle("�����ְ�ֻ�����ܰ����ҵ��ܶ�Ƶ���๤��");
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
		case R.id.relative_shortmessage:// ����
			pwShare.dismiss();
			SmsShareContent sms = new SmsShareContent();
			sms.setShareContent("�ո����˺��ְ�app�����������˿ͷ������Ա��������ã�����רҵ������ȫ���٣�Ч�ʴ����ߣ��������ݷ��������ġ�����������t.im/hsbbc");
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

		case R.id.rl_my_2:// ��¼
			intent = new Intent(MainMyFragment.this, LoginActivity.class);
			startActivity(intent);
			finish();

			break;
		case R.id.rl_my_xiaoxi:// ��Ϣ����

			if (application.isLogon()) {
				redDot();
				intent = new Intent(MainMyFragment.this, MessageCenterActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
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
			// �������淢�͹�ȥ��������������
			switch (requestCode) {
			case 1:
				if (ischenggong.equals("y")) {
					// ͷ��
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
		intentobd.putExtra("isReddot", "0");// 1��ʾ0����
		sendBroadcast(intentobd);
	}

	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	/**
	 * @�������� : ���΢��ƽ̨����
	 * @return AppID��wx21282fa0dad9d60e
	 *         AppSecret��2dbe367ac38677e2db55fcf8f2f9bb9d
	 */
	private void addWXPlatform() {
		// ע�⣺��΢����Ȩ��ʱ�򣬱��봫��appSecret
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appId = "wxfe5743c125b3c59b";
		String appSecret = "b7a9a76b0385c971cb1d67f9d3cec1ee";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(MainMyFragment.this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(MainMyFragment.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		wxCircleHandler.showCompressToast(false);
	}

	/**
	 * ��Ӷ���ƽ̨
	 */
	private void addSMS() {
		// ��Ӷ���
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}
}
