package com.onemeter.util;

/**
 * 常量类，为整个应用提供常用的常量
 * 
 * @author angelyin
 * @date 2015年6月1日21:25:26
 */
public class Constants {
	/** 服务器的的路径(正式服务器) **/
	public static String SERVER_UL = "http://dev01-websrv.onemeter.co/";
	/** 是否为开发模式 **/
	public final static boolean DEVELOPER_MODE = false;
	/** 用户登录 **/
	public final static String API_CHECK_GOODZ_LOGIN = "om_im_api.php/";
	/** 修改密码 **/
	public final static String API_FORGET_LOGIN_PASSWORD = "om_im_api.php/";
	/** 添加潜客 **/
	public final static String API_TIANJIA_CUSTOMER = "om_im_api.php/";
	/** 获取添加潜客的状态 **/
	public final static String API_TIANJIA_STATUS = "om_im_api.php/";
	/** 获取添加潜客的来源 **/
	public final static String API_TIANJIA_SOURCE = "om_im_api.php/";
	/** 验证手机号码是否已存在 **/
	public final static String API_CECKED_MOBILE = "om_im_api.php/";
	/** 获取潜客统计数据 **/
	public final static String API_CUSTOMER_CONVERSION = "om_im_api.php/";
	/** 获取所有潜客信息 **/
	public final static String API_CUSTOMER_ALLMSG = "om_im_api.php/";
	/** 获取指定ID潜客信息 **/
	public final static String API_CUSTOMER_DATABY_MSG = "om_im_api.php/";
	/** 更新指定潜客状态 **/
	public final static String API_CUSTOMER_UPDATE_STATUS = "om_im_api.php/";
	/** 更新指定潜客提交信息 **/
	public final static String API_CUSTOMER_UPDATE_FORSUB = "om_im_api.php/";
	/** 更新动态信息 **/
	public final static String API_CUSTOMER_UPDATE_DYNAMIC = "om_im_api.php/";
	/** 获取指定历史动态信息 **/
	public final static String API_CUSTOMER_GET_DYNAMIC = "om_im_api.php/";
	/** 更新指定潜客状态为失效 **/
	public final static String API_CUSTOMER_UPDATE_SHIXIAO = "om_im_api.php/";
	/**更新批量动态信息**/
	public final static String API_CUSTOMER_UPDATE_PILIANGDONGTAI="om_im_api.php/";
	
}
