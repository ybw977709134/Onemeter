package com.onemeter.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.util.Log;
import android.view.View;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.onemeter.activity.LoginActivity;
import com.onemeter.util.C_Constants;
import com.onemeter.util.C_Constants.API;

/**
 * 网络层工具类</br> 1.使用application/json 2.支持自定义HttpMethod
 * 
 */
public class N_NetUtil {
	private HttpUtils hu;
	private final String LOGTAG = "Onemeter";

	public N_NetUtil() {
		hu = new HttpUtils();
	}

	/**
	 * 使用application/json方式，向服务期发送Post请求
	 * 
	 * @param bodyEntity
	 * @param api
	 * @param observer
	 * @param what
	 * @Date 2015-10-9 下午7:02:05
	 */
	public void sendPost_PutToServer(HttpEntity bodyEntity, final API api,
			final Object observer, final Object... what) {
		StringBuffer sb = new StringBuffer(C_Constants.SERVER_UL);
		sb.append(api.url());
		RequestParams params = new RequestParams();
		params.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));// 使用application/json
		params.setBodyEntity(bodyEntity);// 设置bodyEntity
		this.send(sb.toString(), params, api, observer, what);
	}

	/**
	 * 项服务器发送Get请求
	 * 
	 * @param paramsMap
	 *            要发送的参数
	 * @param api
	 * @param observer
	 * @param what
	 */
	public void sendGet_DeleteToServer(Map<String, Object> paramsMap,
			final API api, final Object observer, final Object... what) {
		StringBuffer sb = new StringBuffer(C_Constants.SERVER_UL);
		sb.append(api.url()).append(initParams(paramsMap));
		if (sb.toString().contains("?")) {
			sb.append("&t=");
		} else {
			sb.append("?t=");
		}
		sb.append(System.currentTimeMillis());
		this.send(sb.toString(), null, api, observer, what);
	}

	/**
	 * 向服务器发送请求
	 * 
	 * @date 2015-5-11 下午5:29:52
	 * @param url
	 * @param params
	 * @param api
	 * @param observer
	 * @param what
	 */
	private void send(String url, RequestParams params, final API api,
			final Object observer, final Object... what) {
		doWhat(what, false);
		hu.send(api.method(), url, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				Log.i(LOGTAG, "conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					Log.i(LOGTAG, "upload: " + current + "/" + total);
				} else {
					Log.i(LOGTAG, "reply: " + current + "/" + total);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (responseInfo.statusCode == 204) {// 服务器请求成功但是没返回body
					handleReslut(api.url(), "{\"code\":0}", true, observer,
							what);
				} else {
					handleReslut(api.url(), responseInfo.result, true,
							observer, what);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.i(LOGTAG, error.getExceptionCode() + ":" + msg);
				if (error.getExceptionCode() == 204) {// 服务器请求成功但是没返回body
					handleReslut(api.url(), String.valueOf("{\"code\":0}"),
							true, observer, what);
				} else {
					handleReslut(api.url(), String.valueOf(error.getEntity()),
							handleExceptionCode(error), observer, what);
				}
			}
		});
	}

	/**
	 * 构造参数（将Map转换成url参数）
	 * 
	 * @date 2015-5-11 下午5:04:38
	 * @param entity
	 * @return
	 */
	private String initParams(Map<String, Object> params) {
		if (params == null)
			return "";
		StringBuffer urlParamsSb = new StringBuffer("?");
		for (String key : params.keySet()) {
			String param = "";
			try {
				param = URLEncoder.encode(params.get(key).toString(), "utf-8");
				param = URLEncoder.encode(param, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			urlParamsSb.append(key).append("=").append(param).append("&");
		}
		urlParamsSb.deleteCharAt(urlParamsSb.length() - 1);
		Log.i(LOGTAG, urlParamsSb.toString());
		return urlParamsSb.toString();
	}

	/**
	 * 处理服务器返回的异常信息
	 * 
	 * @param error
	 * @author JPH
	 * @date 2015-5-8 下午2:18:20
	 */
	protected boolean handleExceptionCode(HttpException error) {
		switch (error.getExceptionCode()) {
		case 400:// 验证失败（参数错误，手机号格式错误，密码错误，用户不存在等）
			return true;
		case 500:
			return true;
		case 404:// 接口不存在
			return true;
		default:
			break;
		}
		return false;
	}

	/**
	 * 根据请求代码（即请求的方法）来处理Post请求服务器返回的结果
	 * 
	 * @param api
	 * @param result
	 * @param isSuccess
	 * @param observer
	 * @param what
	 */
	protected void handleReslut(String api, String result, boolean isSuccess,
			Object observer, Object... what) {
		doWhat(what, true);
		Log.i(LOGTAG, "api:" + api + "\nresult:" + result);
		if (observer == null)
			return;
		if (api.equals(C_Constants.API_CHECK_GOODZ_LOGIN.url())) {
			if (observer instanceof LoginActivity) {// 用户登录
				((LoginActivity) observer).onCompleted(result, isSuccess, api);
			}
		}

	}

	/**
	 * 对传过来的Object[] what参数做处理
	 * 
	 * @param what
	 * @param isFinish
	 *            是否请求完成
	 * 
	 */
	private void doWhat(Object[] what, boolean isStart) {
		for (int i = 0; i < what.length; i++) {
			Object obj = what[i];
			if (obj instanceof ArrayList<?>) {// 启用按钮
				ArrayList<?> arrayList = (ArrayList<?>) obj;
				for (Object tempObj : arrayList) {
					if (tempObj instanceof View) {
						((View) tempObj).setEnabled(isStart);
					} else {
						break;
					}
				}
			}
		}
	}
}
