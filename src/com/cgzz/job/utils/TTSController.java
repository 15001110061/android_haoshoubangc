package com.cgzz.job.utils;

import android.content.Context;
import android.os.Bundle;

import com.cgzz.job.R;
import com.cgzz.job.activity.MainHomeActivity;
import com.cgzz.job.application.GlobalVariables;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;

/**
 * �����������
 * 
 */
public class TTSController implements SynthesizerListener {

	public static TTSController ttsManager;
	private Context mContext;
	// �ϳɶ���.
	public static SpeechSynthesizer mSpeechSynthesizer;
	public GlobalVariables application;

	TTSController(Context context) {
		mContext = context;
		application = (GlobalVariables) mContext.getApplicationContext();
	}

	public static TTSController getInstance(Context context) {
		if (ttsManager == null) {
			ttsManager = new TTSController(context);
		}
		return ttsManager;
	}

	public void init() {

		SpeechUser.getUser().login(mContext, null, null,
				"appid=" + mContext.getString(R.string.app_id), listener);
		// ��ʼ���ϳɶ���.
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
		initSpeechSynthesizer();
	}

	/**
	 * ʹ��SpeechSynthesizer�ϳ��������������ϳ�Dialog.
	 * 
	 * @param
	 */
	public void playText(String playText) {
		// if(application.isSetupPhonetic()){
		// if (!isfinish) {
		// return;
		// }
		if (null == mSpeechSynthesizer) {
			// �����ϳɶ���.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
			initSpeechSynthesizer();
		}
		// ���������ϳ�.
		mSpeechSynthesizer.startSpeaking(playText, this);
		// }

	}

	public void stopSpeaking() {
		if (mSpeechSynthesizer != null)
			mSpeechSynthesizer.stopSpeaking();
	}

	public void startSpeaking() {
		isfinish = true;
	}

	private void initSpeechSynthesizer() {
		// ���÷�����
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		// ��������
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "tts_speed");
		// ��������
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "tts_volume");
		// �������
		mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "tts_pitch");

	}

	/**
	 * �û���¼�ص�������.
	 */
	private SpeechListener listener = new SpeechListener() {

		@Override
		public void onData(byte[] arg0) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {

			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}
	};

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	boolean isfinish = true;

	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub
		isfinish = true;
//		MainHomeActivity.stop();
	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub
		isfinish = false;

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

	public static void destroy() {
		if (mSpeechSynthesizer != null) {
			mSpeechSynthesizer.stopSpeaking();
		}
	}

}
