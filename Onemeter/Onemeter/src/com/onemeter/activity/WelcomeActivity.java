package com.onemeter.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.LoginDate_common;
import com.onemeter.entity.LoginDate_xiaozhang;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;


public class WelcomeActivity extends BaseActivity {
private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟三秒  
    private SharedPreferences shpPreferences;
    String school;
    String userNum;
    String password;
    String status;
 	/** 普通用户登录实体类 **/
 	LoginDate_common loginDate;
 	/** 校长用户登录实体类 **/
 	LoginDate_xiaozhang login_xzdate;
 	ProgressDialog prodialog;// 加载进度条对话框
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.welcome_layout); 
        shpPreferences = getSharedPreferences("app_config",
				Context.MODE_PRIVATE);
       school=shpPreferences.getString("school", "");
       userNum=shpPreferences.getString("userNum", "");
       password=shpPreferences.getString("password", "");
       status=shpPreferences.getString("status", "");
        new Handler().postDelayed(new Runnable(){ 
  
         @Override 
         public void run() { 
        	 if ("1".equals(status)) {
        		 RequestParams params = new RequestParams();
     			params.addBodyParameter("action", "zsbLogin");
     			params.addBodyParameter("school", school);
     			params.addBodyParameter("cellphone", userNum);
     			params.addBodyParameter("password", password);
     			params.addBodyParameter("o", "json");
     			new NetUtil(WelcomeActivity.this).sendPostToServer(params,
     			Constants.API_CHECK_GOODZ_LOGIN, WelcomeActivity.this, "zsbLogin");
        		 
			}else {
				 Intent mainIntent = new Intent(WelcomeActivity.this,LoginActivity.class); 
                 WelcomeActivity.this.startActivity(mainIntent); 
                 WelcomeActivity.this.finish(); 
			}
        	   
         } 
            
        }, SPLASH_DISPLAY_LENGHT); 
        
    } 
    /**
	 * 当请求数据完成后调用此方法更新数据
	 * 
	 * @Title: onCompleted
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param result
	 * @param @param isSuccess
	 * @param @param api 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void onCompleted(String result, boolean isSuccess, String api) {
		if (prodialog != null && prodialog.isShowing()) {
			prodialog.dismiss();
			Log.i("Onemeter", "关闭prodialog");
		}
		if (!isSuccess) {// 请求不成功
			toast(getResources().getString(R.string.msg_request_error));
			return;
		}
		Status status = StatusUtil.handleStatus(this, result);

		/**************************************************************************************************/
		if (api.equals(Constants.API_CHECK_GOODZ_LOGIN)) {// 判断商户登入
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功，处理逻辑
				toast("登录成功！");
				// 判断是否是校长登录
				JSONObject jsonObject = null;
				JSONObject jsonObject1 = null;
				JSONObject jsonObject2 = null;
				JSONObject jsonObject3 = null;
				try {
					jsonObject = new JSONObject(result);
					jsonObject1 = jsonObject.getJSONObject("body");
					jsonObject2 = jsonObject1.getJSONObject("zsbLogin");
					jsonObject3 = jsonObject2.getJSONObject("user");

				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (jsonObject3.isNull("admin_id")) {
					// 普通用户登录
					loginDate = (LoginDate_common) GsonUtil.convertJson2Object(
							result, LoginDate_common.class,
							GsonUtil.JSON_JAVABEAN);

					MyApplication.uuserDate.setSchool(loginDate.getBody()
							.getZsbLogin().getUser().getSchool_id());
					MyApplication.uuserDate.setUserNum(loginDate.getBody()
							.getZsbLogin().getUser().getCellphone());
					MyApplication.uuserDate.setLeixing("普通");
					MyApplication.uuserDate.setPassword(password);
					// Log.i("ceshi",MyApplication.uuserDate.getUserNum()+"------"+MyApplication.uuserDate.getSchool()+"------"+password);
					MyApplication.uuserDate.setUid(loginDate.getBody()
							.getZsbLogin().getUser().getId());
					// -------------------------------------------------------------
					MyApplication.nCrDate.setSchool_id(loginDate.getBody()
							.getZsbLogin().getUser().getSchool_id());

					if (loginDate.getBody().getZsbLogin().getUser()
							.getBranch_id() == null) {
						MyApplication.nCrDate.setBranch_id(loginDate.getBody()
								.getZsbLogin().getSchool().getAdmin_id());
					} else {
						MyApplication.nCrDate.setBranch_id(loginDate.getBody()
								.getZsbLogin().getUser().getBranch_id());
					}
					MyApplication.nCrDate.setEmp_id(loginDate.getBody()
							.getZsbLogin().getUser().getId());

				} else {
					// 校长登录
					login_xzdate = (LoginDate_xiaozhang) GsonUtil
							.convertJson2Object(result,
									LoginDate_xiaozhang.class,
									GsonUtil.JSON_JAVABEAN);
					MyApplication.uuserDate.setLeixing("校长");
					MyApplication.uuserDate.setSchool(login_xzdate.getBody()
							.getZsbLogin().getUser().getAdmin_id());
					MyApplication.uuserDate.setPassword(password);
					// Log.i("ceshi",MyApplication.uuserDate.getUserNum()+"------"+MyApplication.uuserDate.getSchool()+"------"+password);
					MyApplication.uuserDate.setUid(login_xzdate.getBody()
							.getZsbLogin().getUser().getAdmin_id());
					// -------------------------------------------------------------
					MyApplication.nCrDate.setSchool_id(login_xzdate.getBody()
							.getZsbLogin().getUser().getAdmin_id());
					MyApplication.nCrDate.setBranch_id(login_xzdate.getBody()
							.getZsbLogin().getSchool().getAdmin_id());

					MyApplication.nCrDate.setEmp_id(login_xzdate.getBody()
							.getZsbLogin().getUser().getAdmin_id());

				}

				 Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class); 
                 WelcomeActivity.this.startActivity(mainIntent); 
                 WelcomeActivity.this.finish(); 

			}

			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 72) {
					Utils.showToast(this, "密码错误");
					return;
				}
				if (statusCode == 73) {
					Utils.showToast(this, "该用户已停用");
					return;
				}
				if (statusCode == -99) {
					Utils.showToast(this, "用户不存在");
					return;
				}
				if (statusCode == 1) {
					Utils.showToast(this, "参数错误");
					return;
				}
			}
		}

	}

}

