package com.cgzz.job.http;

import android.os.Environment;

public class HttpStaticApi {

	public static final String IP = "www.haoshoubang.com";// IP
	public static final String PORT = "8080";// 端口

	public static final String Send_TheirProfile = Environment
			.getExternalStorageDirectory() + "/PartTimeJob/theirProfile"; // 个人资料头像和身份证图片保存目录
	
	public static final String Send_Audio = Environment
			.getExternalStorageDirectory() + "/PartTimeJob/audio"; // 音频文件
	
	
	public static final String DB_PATH = Environment
			.getExternalStorageDirectory() + "/PartTimeJob/db/";// 数据库存放目录
	public static final int SUCCESS_HTTP = 10000;// 成功
	public static final int FAILURE_HTTP = 40001;// 失败
	public static final int FAILURE_MSG_HTTP = 40002;// 失败提示msg

	public static final int testa_Http = 10002;//
	public static final int testb_Http = 10003;//

	public static final int login_Http = 10010;// 登录
	public static final int sendCode_Http = 10011;// 验证码
	public static final int uploadiv_Http = 10012;//
	public static final int register_Http = 10013;//
	public static final int forgetPassword_Http = 10014;//
	public static final int opinion_Http = 10015;//
	public static final int updatePassword_Http = 10016;//
	public static final int carousel_Http = 10017;//
	public static final int mylist_Http = 10018;//
	public static final int Training_Http = 10019;//
	public static final int citylist_Http = 10020;//
	public static final int detail_Http = 10021;//
	public static final int collection_Http = 10022;//
	public static final int ordercList_Http = 10023;//
	public static final int grab_Http = 10024;//
	public static final int record_Http = 10025;//
	
	public static final int arrive_Http = 10026;//
	public static final int cancelReason_Http = 10027;//
	public static final int cancel_Http = 10028;//
	public static final int stopAccept_Http = 10029;//
	
	public static final int myIncome_Http = 10030;//
	public static final int  incomeDetail_Http = 10031;//
	public static final int  addbank_Http = 10032;//
	public static final int  consulting_Http = 10033;//
	public static final int  training_Http = 10034;//
	public static final int  orderNum_Http = 10035;//
	public static final int  message_Http = 10036;//
	public static final int  version_Http = 10037;//
	public static final int  canarrive_Http = 10038;//
	public static final int  myBank_Http = 10039;//
	public static final int  wages_Http = 10040;//
	public static final int  evaluate_Http = 10041;//
}
