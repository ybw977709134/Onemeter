package com.onemeter.net;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.onemeter.activity.ChagePassWordActivity;
import com.onemeter.activity.LeadItemUpdateActivity;
import com.onemeter.activity.LeadNewItemUpdateActivity;
import com.onemeter.activity.LeadUpdateStateActivity;
import com.onemeter.activity.LeadsDetailsActivity;
import com.onemeter.activity.LoginActivity;
import com.onemeter.activity.MainActivity;
import com.onemeter.activity.NewAddLeadEditActivity;
import com.onemeter.activity.NewLeadContactActivity;
import com.onemeter.activity.NewLeadEditActivity;
import com.onemeter.activity.NewLeadSourceActivity;
import com.onemeter.activity.NewLeadStateActivity;
import com.onemeter.activity.NewsAddLeadEditorActivity;
import com.onemeter.activity.NewsLeadEditorActivity;
import com.onemeter.activity.WelcomeActivity;
import com.onemeter.fragment.LeadsFragment;
import com.onemeter.fragment.StatisticFragment;
import com.onemeter.util.Constants;



/**
 * 和服务器进行数据交互的工具类
 * 
 * @author JPH
 * @date 2015-6-1 下午3:35:19
 */
public class NetUtil {
	private HttpUtils hu;

	public NetUtil(Context context) {
		hu = new HttpUtils();
	}

	/**
	 * 向服务期发送Post请求(此方法只能在UI线程中调用)
	 * 
	 * @param observer
	 *            被观察者
	 * @param params
	 *            请求参数
	 * @param api
	 *            请求的接口
	 */
	public void sendPostToServer(RequestParams params, final String api,
			final Object observer ,String action) {
		send(params, api, observer, action,HttpMethod.POST);
	}

		


	public void send(RequestParams params, final String api,
			final Object observer,final String action, HttpMethod method) {
		StringBuffer sb = new StringBuffer(Constants.SERVER_UL);
		sb.append(api);
		hu.send(method, sb.toString(), params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				Log.i("Onemeter", "conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					Log.i("Onemeter", "upload: " + current + "/" + total);
				} else {
					Log.i("Onemeter", "reply: " + current + "/" + total);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				handlePostReslut(api, responseInfo.result, true, observer,action);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.i("Onemeter", error.getEntity() + ":" + msg);
				handlePostReslut(api, msg, false, observer,action);
			}
		});
	}

	/**
	 * 根据请求代码（即请求的方法）来处理Post请求服务器返回的结果
	 * 
	 * @param api
	 * @param result
	 * @param isSuccess
	 * @param observer
	 */
	protected void handlePostReslut(String api, String result,
			boolean isSuccess, Object observer,String action) {
		// 将服务器返回的结果交给业务逻辑页处理
		Log.i("Onemeter", "api:" + api + "\nresult:" + result);
		if(api.equals(Constants.API_CHECK_GOODZ_LOGIN)){//登录
			if(observer instanceof LoginActivity){
				((LoginActivity)observer).onCompleted(result,isSuccess,api);
			}else if(observer instanceof WelcomeActivity){
				((WelcomeActivity)observer).onCompleted(result,isSuccess,api);
			}
		}
		
		if(api.equals(Constants.API_FORGET_LOGIN_PASSWORD)){//修改密码
			if(observer instanceof ChagePassWordActivity){
				((ChagePassWordActivity)observer).onCompleted(result,isSuccess,api);
			}
		}
		if(api.equals(Constants.API_CECKED_MOBILE)&&action.equals("checkCustomerMobile")){//验证手机号是否存在
			if(observer instanceof NewLeadContactActivity){
				((NewLeadContactActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			
//			if(observer instanceof NewsLeadEditorActivity){
//				((NewsLeadEditorActivity)observer).onCompleted(result,isSuccess,api,action);
//			}
		}
//		
		if(api.equals(Constants.API_TIANJIA_CUSTOMER)&&action.equals("addCustomer")){//添加潜客 
			if(observer instanceof NewLeadContactActivity){
				((NewLeadContactActivity)observer).onCompleted(result,isSuccess,api,action);
			}
		}
		if(api.equals(Constants.API_TIANJIA_STATUS)&&action.equals("getStatusData")){//获取状态 
			if(observer instanceof NewLeadStateActivity){
				((NewLeadStateActivity)observer).onCompleted(result,isSuccess,api);
			}
			if(observer instanceof LeadsFragment){
				((LeadsFragment)observer).onCompleted(result,isSuccess,api,action);
			}
			if(observer instanceof LeadUpdateStateActivity){
				((LeadUpdateStateActivity)observer).onCompleted(result,isSuccess,api);
			}
		}
		if(api.equals(Constants.API_TIANJIA_SOURCE)){//获取来源
			if(observer instanceof NewLeadSourceActivity){
				((NewLeadSourceActivity)observer).onCompleted(result,isSuccess,api);
			}
		}
		
		if(api.equals(Constants.API_CUSTOMER_CONVERSION)&&action.equals("getCustomerConversionData")){//获取潜客统计数据
				if(observer  instanceof StatisticFragment){		
			((StatisticFragment)observer).onCompleted(result,isSuccess,api);
				}
		}
		if(api.equals(Constants.API_CUSTOMER_ALLMSG)&&action.equals("getCustomerData")){
			if(observer instanceof LeadsFragment){		
				((LeadsFragment)observer).onCompleted(result,isSuccess,api,action);
					}
		}
		
		if(api.equals(Constants.API_CUSTOMER_DATABY_MSG)&&action.equals("getCustomerDataById")){
			if(observer instanceof NewLeadEditActivity){//获取指定潜客信息		
				((NewLeadEditActivity)observer).onCompleted(result,isSuccess,api,action);
					}
			
			if(observer instanceof LeadsDetailsActivity){
				((LeadsDetailsActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			if(observer instanceof LeadItemUpdateActivity){
				((LeadItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			if(observer instanceof LeadNewItemUpdateActivity){
				((LeadNewItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			
			if(observer instanceof NewAddLeadEditActivity){
				((NewAddLeadEditActivity)observer).onCompleted(result,isSuccess,api,action);
				
			}
		}
		
		if(api.equals(Constants.API_CUSTOMER_UPDATE_STATUS)&&action.equals("modifyCustomersStatus")){
			if(observer instanceof LeadItemUpdateActivity){//更新指定潜客状态
				((LeadItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			if(observer instanceof LeadNewItemUpdateActivity){//更新潜客状态信息
				((LeadNewItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
		}
		
		if(api.equals(Constants.API_CUSTOMER_UPDATE_FORSUB)&&action.equals("updateCustomer")){
			if(observer instanceof NewsLeadEditorActivity){//更新指定潜客信息
				((NewsLeadEditorActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			if(observer instanceof NewsAddLeadEditorActivity){//更新指定潜客信息
				((NewsAddLeadEditorActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			
		}
		if(api.equals(Constants.API_CUSTOMER_UPDATE_DYNAMIC)&&action.equals("addDynamic")){
			if(observer instanceof LeadItemUpdateActivity){//更新指定动态信息
				((LeadItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			
		}
		
		if(api.equals(Constants.API_CUSTOMER_GET_DYNAMIC)&&action.equals("getDynamicDataByCustomerId")){
			if(observer instanceof LeadItemUpdateActivity){//获取动态信息
				((LeadItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
			
		}
		if(api.equals(Constants.API_CUSTOMER_UPDATE_SHIXIAO)&&action.equals("modifyCustomerStatusByStatusName")){
			if(observer instanceof LeadsFragment){//更新指定状态为失效
				((LeadsFragment)observer).onCompleted(result,isSuccess,api,action);
			}
		}
		
		if(api.equals(Constants.API_CUSTOMER_UPDATE_PILIANGDONGTAI)&&action.equals("batchAddDynamic")){
			if(observer instanceof LeadNewItemUpdateActivity){//更新批量动态信息
				((LeadNewItemUpdateActivity)observer).onCompleted(result,isSuccess,api,action);
			}
		}
		
	}

	
}
