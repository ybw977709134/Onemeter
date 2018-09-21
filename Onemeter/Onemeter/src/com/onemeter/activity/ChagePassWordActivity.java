package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;

/**
 * 修改密码页面
 * 
 * @author angelyin
 * @date 2015-10-12 下午4:36:45
 */
public class ChagePassWordActivity extends BaseActivity implements
		OnClickListener {
	/** 返回 **/
	private LinearLayout linearLayout_back = null;
	/** 显示和隐藏文本内容 **/
	private ImageView imageView_login_close = null;
	/** 旧密码 **/
	private EditText editText_oldpassword = null;
	/** 新密码 **/
	private EditText editText_newspassword = null;
	/** 再次输入新密码 **/
	private EditText editText_password = null;
	private Button button1;
	/** 是否可以点击 **/
	private boolean isclick = false;
	/** 控制开关 **/
	private boolean isopen = false;

	private String Oldpassword;
	private String Newpassword;
	private String password;
	/** 进度条弹框 **/
	ProgressDialog prodialog;// 加载进度条对话框
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changepassword_layout);

		initView();
	}

	private void initView() {
		linearLayout_back = (LinearLayout) findViewById(R.id.linearLayout_back);
		imageView_login_close = (ImageView) findViewById(R.id.imageView_login_close);
		editText_oldpassword = (EditText) findViewById(R.id.editText_oldpassword);
		editText_newspassword = (EditText) findViewById(R.id.editText_newspassword);
		editText_password = (EditText) findViewById(R.id.editText_determinepasswrod);
//		prodialog = new ProgressDialog(this);// 进度条对话框

		button1 = (Button) findViewById(R.id.button1);

		linearLayout_back.setOnClickListener(this);
		imageView_login_close.setOnClickListener(this);
		button1.setOnClickListener(this);
		editText_oldpassword.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;// 监听前的文本

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 0) {
					button1.setEnabled(true);
					button1.setTextColor(Color.rgb(10, 145, 229));

				} else {
					button1.setEnabled(false);
					button1.setTextColor(Color.parseColor("#c0c0c0"));
				}

			}
		});

		editText_newspassword.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;// 监听前的文本

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 0) {
					button1.setEnabled(true);
					button1.setTextColor(Color.rgb(10, 145, 229));

				} else {
					button1.setEnabled(false);
					button1.setTextColor(Color.parseColor("#c0c0c0"));
				}

			}
		});
		editText_password.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;// 监听前的文本

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 0) {
					button1.setEnabled(true);
					button1.setTextColor(Color.rgb(10, 145, 229));

				} else {
					button1.setEnabled(false);
					button1.setTextColor(Color.parseColor("#c0c0c0"));
				}

			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearLayout_back:
			finish();
			break;
		case R.id.imageView_login_close:

			if (isopen) {
				imageView_login_close
						.setImageResource(R.drawable.icon_login_close);
				isopen = false;
				// 将文本框的内容以密码显示
				editText_oldpassword
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				editText_oldpassword.postInvalidate();
				editText_newspassword
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				editText_newspassword.postInvalidate();
				editText_password
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				editText_password.postInvalidate();
			} else {
				imageView_login_close
						.setImageResource(R.drawable.icon_login_open);
				isopen = true;
				// 将文本框的内容以明文显示
				editText_oldpassword
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				editText_oldpassword.postInvalidate();
				editText_newspassword
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				editText_newspassword.postInvalidate();
				editText_password
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				editText_password.postInvalidate();
			}

			break;

		case R.id.button1:
			// 判断输入框
			// 发送请求
			getChangePassWord();

			break;
		}
	}

	/**
	 * 发送修改密码请求
	 * 
	 * @author angelyin
	 * @date 2015-10-12 下午12:26:52
	 */
	private void getChangePassWord() {
		// 获取输入框中的值
		Oldpassword = editText_oldpassword.getText().toString().trim();
		Newpassword = editText_newspassword.getText().toString().trim();
		password = editText_password.getText().toString().trim();

		if (Oldpassword.length() == 0) {
			toast("旧密码不能为空");
			return;

		} else if (Oldpassword.length() > 0
				&& !Oldpassword.equals(MyApplication.uuserDate.getPassword())) {
			toast("原密码填写错误");
			return;
		
		} else if (Newpassword.length() == 0) {
			toast("新密码不能为空");
			return;
		}
		if (Newpassword.length() <6||Newpassword.length()>20) {
			toast("密码要求为6~20位，由数字、英文字符、-_两种特殊字符组成的字符串");
			return;
		} 
		if (password.length() == 0 || !password.equals(Newpassword)) {
			toast("再次输入的密码不一致");
			return;
		} else {
			if (!MyApplication.isNetAvailable) {// 网络不可用
				Utils.showToast(this, "网络不可用，请打开网络");
				return;
			}
		}
//		toast(Oldpassword + "------" + Newpassword + "------"
//				+ MyApplication.uuserDate.getPassword());
//		prodialog.setMessage("加载中");
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "changePassword");
		params.addBodyParameter("old", Oldpassword);
		params.addBodyParameter("new", Newpassword);
		params.addBodyParameter("uid", MyApplication.uuserDate.getUid());
		Log.i("xiaozhang", MyApplication.uuserDate.getUid());
		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_FORGET_LOGIN_PASSWORD, this, "changePassword");
	}

	/**
	 * 当请求数据完成后调用此方法更新数据
	 * 
	 * @author angelyin
	 * @date 2015-10-12 下午3:00:06
	 * @param result
	 * @param isSuccess
	 * @param api
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
		if (api.equals(Constants.API_FORGET_LOGIN_PASSWORD)) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 成功后处理逻辑
				toast("修改成功，请使用新密码登录");
			    MainActivity.instance.finish();
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
				
			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 失败后处理逻辑
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					toast("参数错误");
					return;
				}
				if (statusCode == -99) {
					toast("用户不存在");
					return;
				}
				if (statusCode == 72) {
					toast("密码错误");
					return;
				}
			}

		}
	}

}
