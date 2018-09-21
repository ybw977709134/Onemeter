package com.onemeter.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.AddCustomerResult;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelMain;
import com.onemeter.wiget.WheelYuYue;

/**
 * 新增潜客->联系方式activitys
 * 
 * @author angelyin
 * @date 2015-10-13 下午10:25:40
 */
public class NewLeadContactActivity extends BaseActivity implements
		OnClickListener {
	/** 上一步按钮 **/
	private Button button_step;
	/** 添加按钮 **/
	private Button button_add;
	/** 学生信息 **/
	private TextView textView_studengtmessage;
	/** 家长姓名 **/
	private EditText newlead_jiazhang_name;
	/** 手机号码 **/
	private EditText newlead_mobile;
	/** 状态 **/
	private TextView newlead_status;
	/** 预约时间 **/
	private TextView newlead_order_date;
	/** 来源 **/
	private TextView newlead_source;
	// 声明字段
	private String parentName;
	private String mobile;
	// 三个布局
	private LinearLayout linearLayout_zt;
	private LinearLayout linearLayout_source;
	private LinearLayout linearLayout_date;
	@SuppressLint("SimpleDateFormat")
	/**时间格式选择**/
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	WheelYuYue wheelMain;
	/** 弹窗 **/
	private PopupWindow mPopupWindow;
	Intent intent;
	/** 进度条弹框 **/
	ProgressDialog prodialog;// 加载进度条对话框

	AddCustomerResult maddCustomerResult;
	String status_id;
	String status_result;
	String suorce_result;
	String suorce_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newlead_contact_layout);
		initView();
		// Log.i("ceshi",
		// MyApplication.nCrDate.getSchool_id()+"/n"+MyApplication.nCrDate.getEmp_id()+"/n"+MyApplication.nCrDate.getSex()+"/n"+MyApplication.nCrDate.getGrade_info()
		// +"/n"+MyApplication.nCrDate.getBirthday());
	}

	private void initView() {
		button_step = (Button) findViewById(R.id.button_step);
		button_add = (Button) findViewById(R.id.button_add);
		textView_studengtmessage = (TextView) findViewById(R.id.textView_studengtmessage);
		// 输入内容
		newlead_jiazhang_name = (EditText) findViewById(R.id.newlead_jiazhang_name);
		newlead_mobile = (EditText) findViewById(R.id.newlead_mobile);
		newlead_status = (TextView) findViewById(R.id.newlead_status);
		newlead_order_date = (TextView) findViewById(R.id.newlead_order_date);
		newlead_source = (TextView) findViewById(R.id.newlead_source);
		// 点击布局
		linearLayout_zt = (LinearLayout) findViewById(R.id.linearLayout_zt);
		linearLayout_source = (LinearLayout) findViewById(R.id.linearLayout_source);
		linearLayout_date = (LinearLayout) findViewById(R.id.linearLayout_date);
		// 清除
		newlead_mobile.setText("");
		newlead_jiazhang_name.setText("");
		prodialog = new ProgressDialog(this);// 进度条对话框

		linearLayout_zt.setOnClickListener(this);
		linearLayout_date.setOnClickListener(this);
		linearLayout_source.setOnClickListener(this);
		textView_studengtmessage.setOnClickListener(this);
		button_step.setOnClickListener(this);
		button_add.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_step:
			// 上一步
			finish();
			break;
		case R.id.button_add:
			// 添加
			getJiaZhangMesg();

			break;
		case R.id.textView_studengtmessage:
			finish();
			break;
		case R.id.linearLayout_zt:
			// 状态
			intent = new Intent(this, NewLeadStateActivity.class);
			intent.putExtra("resultCode", "11");
			startActivityForResult(intent, 1101);
			break;
		case R.id.linearLayout_source:
			// 来源
			intent = new Intent(this, NewLeadSourceActivity.class);
			intent.putExtra("resultCode", "22");
			startActivityForResult(intent, 2202);
			break;
		case R.id.linearLayout_date:
			// 预约日期
			popupWindow();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 判断回传值
		if (requestCode == 1101) {
			if (resultCode == 11) {
				status_result = data.getStringExtra("oone");
				status_id = data.getStringExtra("oone_id");
				newlead_status.setText(status_result);

			}
		}
		if (requestCode == 2202) {
			if (resultCode == 22) {
				suorce_result = data.getStringExtra("ssecond");
				suorce_id = data.getStringExtra("ssecond_id");
				newlead_source.setText(suorce_result);
			}
		}
	}

	/**
	 * 判断并获取输入框内容，并提交
	 * 
	 * @author angelyin
	 * @date 2015-10-13 下午10:45:21
	 */
	private void getJiaZhangMesg() {
		parentName = newlead_jiazhang_name.getText().toString().trim();
		mobile = newlead_mobile.getText().toString().trim();

		if (parentName.length() == 0) {
			toast("请输入家长姓名");
			return;
		} else if (mobile.length() == 0) {
			toast("请输入手机号码");
			return;
		} else if (!Utils.cellphoneValid(mobile)) {
			toast("手机号码不正确");
			return;
		} else if (newlead_status.getText().toString().equals("")) {
			toast("请选择状态");
			return;
		} else if (newlead_order_date.getText().toString().equals("")) {
			toast("请选择预约时间");
			return;
		} else if (newlead_source.getText().toString().equals("")) {
			toast("请选择来源");
			return;
		} else {
			if (!MyApplication.isNetAvailable) {// 网络不可用
				Utils.showToast(this, "网络不可用，请打开网络");
				return;
			}
		}
		MyApplication.nCrDate.setParent_name(parentName);
		isCheckedMobile();

	}

	/**
	 * 发送请求验证手机号是否存在
	 * 
	 * @author angelyin
	 * @date 2015-10-15 上午11:32:34
	 */
	private void isCheckedMobile() {
//		prodialog.setMessage("加载中");
//		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "checkCustomerMobile");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params, Constants.API_CECKED_MOBILE,
				this, "checkCustomerMobile");

	}

	/**
	 * 向服务器提交请求
	 * 
	 * @author angelyin
	 * @date 2015-10-13 下午11:46:57
	 */
	private void ForSubmit() {
//		prodialog.setMessage("加载中");
//		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "addCustomer");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("branch_id",
				MyApplication.nCrDate.getBranch_id());
		params.addBodyParameter("parent_name",
				MyApplication.nCrDate.getParent_name());
		params.addBodyParameter("student_name",
				MyApplication.nCrDate.getStudentName());
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("source_id", suorce_id);
		// Log.i("add", "source_id"+newlead_source.getText().toString().trim());
		params.addBodyParameter("status_id", status_id);
		// Log.i("add", "status_id"+newlead_status.getText().toString().trim());
		params.addBodyParameter("emp_id", MyApplication.nCrDate.getEmp_id());
		params.addBodyParameter("school_info",
				MyApplication.nCrDate.getSchool_info());
		params.addBodyParameter("grade_info",
				MyApplication.nCrDate.getGrade_info());
		params.addBodyParameter("interest_course",
				MyApplication.nCrDate.getKecheng());
		params.addBodyParameter("sex", MyApplication.nCrDate.getSex());
//		Log.i("add", MyApplication.nCrDate.getSex());
		params.addBodyParameter("birthday", MyApplication.nCrDate.getBirthday());
		params.addBodyParameter("book_date", newlead_order_date.getText()
				.toString());
		// Log.i("add", "leads_date"+);
		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_TIANJIA_CUSTOMER, this, "addCustomer");

	}

	/**
	 * 日历弹窗
	 * 
	 * @author angelyin
	 * @date 2015-10-13 下午11:42:39
	 */
	private void popupWindow() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View layoutView = inflater.inflate(R.layout.timepicker, null);
		TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
		TextView ok = (TextView) layoutView.findViewById(R.id.ok);
		ScreenInfo screenInfo = new ScreenInfo(this);
		wheelMain = new WheelYuYue(layoutView);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = newlead_order_date.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 获取到预约日期
				newlead_order_date.setText(wheelMain.getTime());

				mPopupWindow.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow = new PopupWindow(layoutView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.showAtLocation(layoutView, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

		layoutView = null;
		cancel = null;
	}

	/**
	 * 当请求数据完成后调用此方法更新数据
	 * 
	 * @author angelyin
	 * @date 2015-10-14 上午12:03:27
	 * @param result
	 * @param isSuccess
	 * @param api
	 */
	public void onCompleted(String result, boolean isSuccess, String api,
			String action) {
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
		if (api.equals(Constants.API_CECKED_MOBILE)
				&& action.equals("checkCustomerMobile")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
				ForSubmit();

			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(this, "参数错误");
					return;
				}
				if (statusCode == 70) {
					Utils.showToast(this, "该家长手机号已存在");
					return;
				}
			}

		}

		if (api.equals(Constants.API_TIANJIA_CUSTOMER)
				&& action.equals("addCustomer")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
				maddCustomerResult = (AddCustomerResult) GsonUtil
						.convertJson2Object(result, AddCustomerResult.class,
								GsonUtil.JSON_JAVABEAN);

				String  Student_name =maddCustomerResult.getBody().getAddCustomer().getStudent_name();
				String  Student_mobai =maddCustomerResult.getBody().getAddCustomer().getMobile();
				String  customerr_id =maddCustomerResult.getBody().getAddCustomer().getId();
		        //跳转到添加成功页面
				intent =new Intent(this,NewsAddLeadActivity.class);
				intent.putExtra("studentName", Student_name);
				intent.putExtra("studentMobai", Student_mobai);
				intent.putExtra("customerr_Id", customerr_id);
				
			 	startActivity(intent);
			 	finish();

			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(this, "参数错误");
					return;
				}
				if (statusCode == 3) {
					Utils.showToast(this, "数据库错误，添加失败");
					return;
				}
			}
		}
	}

}
