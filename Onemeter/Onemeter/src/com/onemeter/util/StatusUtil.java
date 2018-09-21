package com.onemeter.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.onemeter.R;

import android.content.Context;
import android.util.Log;

/**
 * 公共状态码处理工具类
 * 
 * @ClassName: StatusUtil
 * @author angelyin 2015-10-9 下午4:02:40
 */
public class StatusUtil {
	// public static final int CODE_ERROR = -101;

	/**
	 * 请求状态码处理结果
	 */
	public enum Status {
		/** 请求状态码返回成功 **/
		STATUS_SUCCESS,
		/** 请求状态码返回失败 **/
		STATUS_FAIL,
		/** 请求状态码未处理（交给业务逻辑层处理） **/
		STATUS_UNHANDLE,
		/** 请求状态码重新进行上一步操作 **/
		// STATUS_REDO,
		/** 页面正在关闭 **/
		STATUS_CLOSEIMG
	}

	/**
	 * 处理服务器返回的状态码
	 * 
	 * @param context
	 * @param statusCode
	 *            服务器返回的状态码
	 * @return
	 */
	public static Status handleStatus(Context context, Integer statusCode) {
		if (statusCode == null) {// 未知异常
			Utils.showToast(context,
					context.getResources().getString(R.string.msg_sys_busy));
			return Status.STATUS_FAIL;
		}
		switch (statusCode) {
		case 0:// 服务器返回成功
			return Status.STATUS_SUCCESS;
		case -1:// 系统繁忙
			Utils.showToast(context,
					context.getResources().getString(R.string.msg_sys_busy));
			return Status.STATUS_FAIL;
		case 1:// 参数错误
			Utils.showToast(
					context,
					context.getResources().getString(
							R.string.msg_sys_param_error));
			// Utils.showToast(context,
			// context.getResources().getString(R.string.msg_sys_busy));
			return Status.STATUS_UNHANDLE;
		}
		return Status.STATUS_UNHANDLE;
	}

	/**
	 * 处理服务器返回的状态码（根据服务器返回的结果提取出状态码，处理状态码）
	 * 
	 * @param context
	 * @param result
	 *            服务器返回的结果
	 * @return
	 */
	public static Status handleStatus(Context context, String result) {
		Integer status = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject1 = jsonObject.getJSONObject("header");
			status = jsonObject1.getInt("err_no");
		} catch (JSONException e) {
			Log.e("Onemeter", "状态吗解析错误！" + result);
			e.printStackTrace();
		}

		Log.i("Onemeter", "status" + "==:" + status);
		return handleStatus(context, status);
	}

	/** 解析状态码 */
	public static Integer parseStatus(String result) {
		Integer status;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject jsonObject1 = jsonObject.getJSONObject("header");
			status = jsonObject1.getInt("err_no");
		} catch (JSONException e) {
			Log.i("Onemeter", "状态吗解析错误！" + result);
			status = null;
			e.printStackTrace();
		}

		return status;
	}
}
