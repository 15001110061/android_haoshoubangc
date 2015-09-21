package com.cgzz.job.http;

import com.cgzz.job.application.GlobalVariables;

/**
 * Url
 * 
 * http://service.haoshoubang.com/good-help/
 */
public class UrlConfig {
	public static final int PATH_KEY_REGISTERED = 1001;// 注册成功关闭的集合
	public static final int PATH_KEY_ADDCARD = 1002;// 添加银行卡关闭的集合
	public static final int PATH_KEY_QIANGDAN = 1003;// 抢单成功后
//	public static  String ROOT = "";
//	 public static final String ROOT = "http://test.haoshoubang.com/";
	public static  final String OWNER_LABEL_LIST = GlobalVariables.ROOT + "?r=default/owner/labelList";
	/**
	 * 登录
	 */
	public static  String login_Http = GlobalVariables.ROOT + "good-help/userc/login";

	/**
	 * 验证码
	 */
	public static final String sendCode_Http = GlobalVariables.ROOT + "good-help/base/sendCode";

	/**
	 * 上传图片
	 */
	public static final String uploadiv_Http = GlobalVariables.ROOT + "good-help/base/upload";

	/**
	 * 注册
	 */
	public static final String register_Http = GlobalVariables.ROOT + "good-help/userc/register";

	/**
	 * 忘记密码
	 */
	public static final String forgetPassword_Http = GlobalVariables.ROOT + "good-help/base/forgetPassword";

	/**
	 * 意见反馈
	 */
	public static final String opinion_Http = GlobalVariables.ROOT + "good-help/base/opinion";

	/**
	 * 修改密码
	 */
	public static final String updatePassword_Http = GlobalVariables.ROOT + "good-help/base/updatePassword";

	/**
	 * 更新个人资料
	 */
	public static final String updateUser_Http = GlobalVariables.ROOT + "good-help/userc/update";

	/**
	 * 9、轮播图接口
	 */
	public static final String carousel_Http = GlobalVariables.ROOT + "good-help/base/carousel";

	/**
	 * 当前订单裂变
	 */
	public static final String mylist_Http = GlobalVariables.ROOT + "good-help/orderc/mylist";
	/**
	 * 城市列表
	 */
	public static final String citylist_Http = GlobalVariables.ROOT + "good-help/base/citylist";

	/**
	 * 订单详情页接口
	 */
	public static final String detail_Http = GlobalVariables.ROOT + "good-help/orderc/detail";

	/**
	 * 我的收藏接口
	 */
	public static final String collection_Http = GlobalVariables.ROOT + "good-help/orderc/collection";

	/**
	 * 推荐订单列表接口
	 */
	public static final String ordercList_Http = GlobalVariables.ROOT + "good-help/orderc/list";
	
	
	

	/**
	 * 用户抢单接口
	 */
	public static final String grab_Http = GlobalVariables.ROOT + "good-help/orderc/grab";

	/**
	 * 完成酒店订单记录接口
	 */
	public static final String record_Http = GlobalVariables.ROOT + "good-help/orderc/record";

	/**
	 * 确认到达接口
	 */
	public static final String arrive_Http = GlobalVariables.ROOT + "good-help/orderc/arrive";

	/**
	 * 取消订单原因列表接口
	 */
	public static final String cancelReason_Http = GlobalVariables.ROOT + "good-help/orderc/cancelReason";

	/**
	 * 取消订单接口
	 */
	public static final String cancel_Http = GlobalVariables.ROOT + "good-help/orderc/cancel";

	/**
	 * 停止接单接口
	 */
	public static final String stopAccept_Http = GlobalVariables.ROOT + "good-help/userc/stopAccept";

	/**
	 * 我的收入接口
	 */
	public static final String myIncome_Http = GlobalVariables.ROOT + "good-help/userc/myIncome";

	/**
	 * 我的收入明细接口
	 */
	public static final String incomeDetail_Http_Http = GlobalVariables.ROOT + "good-help/userc/incomeDetail";

	/**
	 * 添加银行卡接口
	 */
	public static final String addbank_Http = GlobalVariables.ROOT + "good-help/userc/addbank";

	/**
	 * 公告培训接口
	 */
	public static final String Consulting_Training_Http = GlobalVariables.ROOT + "good-help/information/list";

	/**
	 * 我的接单次数接口
	 */
	public static final String orderNum_Http = GlobalVariables.ROOT + "good-help/userc/orderNum";

	/**
	 * 更新版本
	 */
	public static final String version_Http = GlobalVariables.ROOT + "good-help/base/version";

	/**
	 * 能否确认到达接口
	 */
	public static final String canarrive_Http = GlobalVariables.ROOT + "good-help/orderc/canarrive";

	/**
	 * 我的银行卡信息接口
	 */
	public static final String myBank_Http = GlobalVariables.ROOT + "good-help/userc/myBank";

	/**
	 * 消息列表接口
	 */
	public static final String messagelistB_Http = GlobalVariables.ROOT + "good-help/information/messagelist";

	/**
	 * 工资条
	 */
	public static final String wagesB_Http = GlobalVariables.ROOT + "good-help/orderc/wages";

	/**
	 * 工资条页面评价完成
	 */
	public static final String evaluate_Http = GlobalVariables.ROOT + "good-help/orderc/evaluate";

	/**
	 * 帮客确认长期订单接口
	 */
	public static final String confirmOrder_Http = GlobalVariables.ROOT + "good-help/orderc/confirmOrder";
	

	/**
	 *是否有红包接口
	 */
	public static final String status_Http = GlobalVariables.ROOT + "good-help/red/status";
	
	
	
	

	/**
	 *我的红包列表接口
	 */
	public static final String redmylist_Http = GlobalVariables.ROOT + "good-help/red/mylist";
}
