package com.onemeter.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.util.Utils;

/**
 * 登录页面
 * 
 * @ClassName: LoginActivity
 * @author angelyin 2015-10-9 下午3:58:34
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	/** 提示 **/
	private TextView login_toast;
	/** 学校账号 **/
	private EditText login_school_et;
	/** 我的账号或手机号 **/
	private EditText login_userNum_et;
	/** 密码 **/
	private EditText login_password_et;
	/** 登录按钮 **/
	private TextView btn_login;

	private String school;
	private String userNum;
	private String password;
	MyApplication myApplication;
	Intent intent;
	/** 进度条弹框 **/
	ProgressDialog prodialog;// 加载进度条对话框

	/** 普通用户登录实体类 **/
	LoginDate_common loginDate;
	/** 校长用户登录实体类 **/
	LoginDate_xiaozhang login_xzdate;

	private SharedPreferences shpPreferences;
	private Editor editor;
	/**控制开关**/
	private boolean isopen = false;
	private ImageView imageView_password_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		initView();
	}

	/**
	 * 初始化组件
	 * 
	 * @Title: initView
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initView() {
		login_toast = (TextView) findViewById(R.id.login_toast);
		btn_login = (TextView) findViewById(R.id.btn_login);
		login_school_et = (EditText) findViewById(R.id.login_school_et);
		login_userNum_et = (EditText) findViewById(R.id.login_userNum_et);
		login_password_et = (EditText) findViewById(R.id.login_password_et);
		imageView_password_img=(ImageView)findViewById(R.id.imageView_password_img);
		imageView_password_img.setOnClickListener(this);
		prodialog = new ProgressDialog(this);// 进度条对话框
		getUser();

		btn_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			// 登录

			getLogin();
			break;
			//明暗文密码按钮
		case R.id.imageView_password_img:
			if (isopen) {
				imageView_password_img.setImageResource(R.drawable.icon_login_close);
				isopen = false;
				login_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
				login_password_et.postInvalidate();
			}else{
				imageView_password_img.setImageResource(R.drawable.icon_login_open);
				isopen = true;
				// 将文本框的内容以明文显示
				login_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				login_password_et.postInvalidate();
			}
			break;
		}	

	}

	/**
	 * 获取保存的用户名和密码
	 */
	private void getUser() {
		shpPreferences = getSharedPreferences("app_config",
				Context.MODE_PRIVATE);
		editor = shpPreferences.edit();
		if (shpPreferences.getString("school", "") != null// 设置学校账号
				&& !shpPreferences.getString("school", "").equals("")) {
			login_school_et.setText(shpPreferences.getString("school", ""));
			login_school_et.setSelection(login_school_et.getText().toString()
					.length());
		}

		if (shpPreferences.getString("userNum", "") != null// 设置用户名
				&& !shpPreferences.getString("userNum", "").equals("")) {
			login_userNum_et.setText(shpPreferences.getString("userNum", ""));
			login_userNum_et.setSelection(login_userNum_et.getText().toString()
					.length());
		}
//		if (shpPreferences.getString("password", "") != null// 设置 密码
//				&& !shpPreferences.getString("password", "").equals("")) {
//			login_password_et.setText(shpPreferences.getString("password", ""));
//			login_password_et.setSelection(login_password_et.getText().toString()
//					.length());
//		}
	}

	/**
	 * 登录操作
	 * 
	 * @Title: getLogin
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getLogin() {
		school = login_school_et.getText().toString().trim();
		userNum = login_userNum_et.getText().toString().trim();
		password = login_password_et.getText().toString().trim();
		if (school == null || school.equals("")) {
			toast("请输入学校账号");
			return;
		} else if (userNum == null || userNum.equals("")) {
			toast("请输入手机号码或用户名");
			return;
		} else if (password == null || password.equals("")) {
			toast("请输入密码");
			return;
		} else {
			if (!MyApplication.isNetAvailable) {// 网络不可用
				Utils.showToast(this, "网络不可用，请打开网络");
				return;
			}
			
			
           
			prodialog.setMessage("加载中");
			prodialog.show();
			RequestParams params = new RequestParams();
			params.addBodyParameter("action", "zsbLogin");
			params.addBodyParameter("school", school);
			params.addBodyParameter("cellphone", userNum);
			params.addBodyParameter("password", password);
			params.addBodyParameter("o", "json");
			new NetUtil(this).sendPostToServer(params,
					Constants.API_CHECK_GOODZ_LOGIN, this, "zsbLogin");
		}

	

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
				editor.putString("school", school);
				editor.putString("userNum", userNum);
				editor.putString("password", password);
				editor.putString("status", "1");
				editor.commit();
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

				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();

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
