package com.cgzz.job.activity;

import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/***
 * @author wjm ���� 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("����", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		findView();
		init();

	}

	LinearLayout llLeft;
	private TextView iv_about_websites, iv_about_customer, iv_about_terms,tv_about_versioncode;

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		iv_about_websites = (TextView) findViewById(R.id.iv_about_websites);
		iv_about_customer = (TextView) findViewById(R.id.iv_about_customer);
		iv_about_terms = (TextView) findViewById(R.id.iv_about_terms);
		tv_about_versioncode = (TextView) findViewById(R.id.tv_about_versioncode);
		tv_about_versioncode.setText("���ְ� "+getVersion(AboutActivity.this));
	}

	private void init() {
		llLeft.setOnClickListener(this);
		iv_about_websites.setOnClickListener(this);
		iv_about_customer.setOnClickListener(this);
		iv_about_terms.setOnClickListener(this);
	}
	public static String getVersion(Context context)//��ȡ�汾��  
    {  
        try {  
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
            return pi.versionName;  
        } catch (NameNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            return context.getString(R.string.version_unknown);  
        }  
    }  
	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String url = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.iv_about_websites:// ��˾��վ
			url = "http://www.haoshoubang.com/m/index.html";
			intent = new Intent(AboutActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "��˾��վ");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);

			break;
		case R.id.iv_about_customer:// ��ϵ�ͷ�
			popTheirProfile();
			break;
		case R.id.iv_about_terms:// ������˵��

			url = "http://www.haoshoubang.com/bangke/html/privacy.html";
			intent = new Intent(AboutActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "������˵��");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile() {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// û�д˾����ⲿ������ʧ
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two),
				Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("�Ƿ񲦴�ͷ��绰?");
		up.setText("��");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("��");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				Utils.calls(getResources().getString(R.string.call_customer),
						AboutActivity.this);

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}
}
