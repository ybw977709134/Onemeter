package com.onemeter.util;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 网络请求的接口类
 * 
 * @ClassName: Constants
 * @author angelyin 2015-10-9 下午4:02:22
 */
public class C_Constants {
	/** 服务器的的路径(测试服务器) **/
	public static String SERVER_UL = "http://dev01-websrv.onemeter.co/om_im_api.php/";

	/** 是否为开发模式 **/
	public final static boolean DEVELOPER_MODE = false;
	/** 用户登录 **/
	public final static API API_CHECK_GOODZ_LOGIN = new API() {
		@Override
		public String url() {
			return "action=zsbLogin";
		}

		@Override
		public HttpMethod method() {
			return HttpMethod.POST;
		}
	};

	/****************************************************/
	public interface API {
		String url();

		HttpMethod method();
	}
}
