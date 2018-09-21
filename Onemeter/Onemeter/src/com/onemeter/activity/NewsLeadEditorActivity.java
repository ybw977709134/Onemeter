package com.onemeter.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelMain;
import com.onemeter.wiget.WheelYuYue;
/**
 * 修改下一步页面
 * @author angelyin
 * @date 2015-10-23 下午4:12:19
 */
public class NewsLeadEditorActivity extends BaseActivity implements OnClickListener{
	private Button button_edit_step=null;
	private Button button_edit_save=null;
	private EditText editText_edit_jzxm=null;
	private EditText editText_edit_jz_phone=null;
	private LinearLayout linearLayout_edit_zt=null;
	private LinearLayout linearLayout_edit_contactdate=null;
	private LinearLayout linearLayout_edit_source=null;
	private TextView textView_edit_source=null;
	private TextView textView_edit_zt=null;
	private TextView textView_edit_contactdate=null;
	@SuppressLint("SimpleDateFormat")
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private PopupWindow mPopupWindow;
	WheelYuYue wheelMain;
	//传递过来的值
	String Parent_name;
	String Mobile;
	String SStatus;
	String Book_date;
	String Source;
	String StatusId;
	String SourceId;
	/**进度条弹框**/
	ProgressDialog prodialog;// 加载进度条对话框
	String  Oldeparent;
	String  OldeMobai;
	String  OldeStatus;
	String  Oldeyuyue;
	String  Oldesource;
	Intent intent;
	String result_Statusid;
	String result_sourceid;
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.newlead_editor_layout);
	prodialog=new ProgressDialog(this);
	Intent intent=getIntent();
	Parent_name=intent.getStringExtra("Parent");
	Mobile=intent.getStringExtra("Mobile");
	SStatus=intent.getStringExtra("Status");
	Book_date=intent.getStringExtra("Book_date");
	Source=intent.getStringExtra("Source");
	StatusId=intent.getStringExtra("Status_id");
	SourceId=intent.getStringExtra("Source_id");
//	Log.i("ffd", "StatusId:"+StatusId);
//	Log.i("ffd", "SourceId:"+SourceId);
	initView();
}

private void initView() {
	button_edit_step=(Button)findViewById(R.id.button_edit_step);
	button_edit_save=(Button)findViewById(R.id.button_edit_save);
	button_edit_step.setOnClickListener(this);
	button_edit_save.setOnClickListener(this);
	editText_edit_jzxm=(EditText)findViewById(R.id.editText_edit_jzxm);
	editText_edit_jz_phone=(EditText)findViewById(R.id.editText_edit_jz_phone);
	linearLayout_edit_zt=(LinearLayout)findViewById(R.id.linearLayout_edit_zt);
	linearLayout_edit_contactdate=(LinearLayout)findViewById(R.id.linearLayout_edit_contactdate);
	linearLayout_edit_source=(LinearLayout)findViewById(R.id.linearLayout_edit_source);
	textView_edit_contactdate=(TextView)findViewById(R.id.textView_edit_contactdate);
	textView_edit_source=(TextView)findViewById(R.id.textView_edit_source);
	
	textView_edit_zt=(TextView)findViewById(R.id.textView_edit_zt);
	linearLayout_edit_contactdate.setOnClickListener(this);
	linearLayout_edit_source.setOnClickListener(this);
	linearLayout_edit_zt.setOnClickListener(this);
	
	//给组件设置值
	editText_edit_jzxm.setText(Parent_name);
	editText_edit_jz_phone.setText(Mobile);
	textView_edit_zt.setText(SStatus);
	textView_edit_source.setText(Source);
	textView_edit_contactdate.setText(Book_date);
}

@Override
public void onClick(View view) {
	switch (view.getId()) {
	case R.id.button_edit_step:
		//上一步
		finish();
		break;
	case R.id.button_edit_save:
		//保存
//		test();
		getUpdateNetCustomerMsg();
		break;
	case R.id.linearLayout_edit_zt:
		Intent intent2=new Intent(this,NewLeadStateActivity.class);
		intent2.putExtra("resultCode", "5555");
		startActivityForResult(intent2, 1212);
		break;
	case R.id.linearLayout_edit_contactdate:
		popupWindow();
		break;
    case R.id.linearLayout_edit_source:
		Intent intent3=new Intent(this,NewLeadSourceActivity.class);
		intent3.putExtra("resultCode", "66666");
		startActivityForResult(intent3, 1313);
		break;

	default:
		break;
	}
}

/**
 * 
 * @author angelyin
 * @date 2015-10-25 下午8:02:01
 */
private void getUpdateNetCustomerMsg() {
	  Oldeparent= editText_edit_jzxm.getText().toString().trim();
	  OldeMobai= editText_edit_jz_phone.getText().toString().trim();
	  OldeStatus= textView_edit_zt.getText().toString().trim();
	  Oldeyuyue= textView_edit_contactdate.getText().toString().trim();
	  Oldesource= textView_edit_source.getText().toString().trim();
	if(validatePhoneAndPwd()){	
		
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params=new RequestParams();
		params.addBodyParameter("action","updateCustomer");	
		params.addBodyParameter("school_id",MyApplication.nCrDate.getSchool_id());
		Log.i("ffd", "school_id:"+MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("branch_id",MyApplication.nCrDate.getBranch_id());
		Log.i("ffd", "branch_id:"+MyApplication.nCrDate.getBranch_id());
		params.addBodyParameter("parent_name",Oldeparent);
		Log.i("ffd", "Oldeparent:"+Oldeparent);
		params.addBodyParameter("student_name",MyApplication.nCrDatelist.getStudentName());
		Log.i("ffd", "student_name:"+MyApplication.nCrDatelist.getStudentName());
		params.addBodyParameter("mobile",OldeMobai);
		Log.i("ffd", "mobile:"+OldeMobai);
		//判断是否还是之前的状态和来源
		if(Source.equals(Oldesource)){
			params.addBodyParameter("source_id",SourceId);
			Log.i("ffd", "SourceId:"+SourceId);
		}else{
			params.addBodyParameter("source_id",result_sourceid);
			Log.i("ffd", "result_sourceid:"+result_sourceid);
		}
		
			if(SStatus.equals(OldeStatus)){
				params.addBodyParameter("status_id",StatusId);
				Log.i("ffd", "StatusId:"+StatusId);
			}else{
				params.addBodyParameter("status_id",result_Statusid);
				Log.i("ffd", "result_Statusid:"+result_Statusid);
			}
			
		params.addBodyParameter("emp_id",MyApplication.nCrDate.getEmp_id());
		Log.i("ffd", "emp_id:"+MyApplication.nCrDate.getEmp_id());
		params.addBodyParameter("school_info",MyApplication.nCrDatelist.getSchool_info());
		Log.i("ffd", "school_info:"+MyApplication.nCrDatelist.getSchool_info());
		params.addBodyParameter("grade_info",MyApplication.nCrDatelist.getGrade_info());
		Log.i("ffd", "grade_info:"+MyApplication.nCrDatelist.getGrade_info());
		params.addBodyParameter("interest_course",MyApplication.nCrDatelist.getKecheng());
		Log.i("ffd", "interest_course:"+MyApplication.nCrDatelist.getKecheng());
		params.addBodyParameter("sex",MyApplication.nCrDatelist.getSex());
		Log.i("ffd", "sex:"+MyApplication.nCrDatelist.getSex());
		params.addBodyParameter("birthday",MyApplication.nCrDatelist.getBirthday());
		Log.i("ffd", "birthday:"+MyApplication.nCrDatelist.getBirthday());
		params.addBodyParameter("book_date",Oldeyuyue);
		Log.i("ffd", "book_date:"+Oldeyuyue);
		params.addBodyParameter("id",MyApplication.IDmap.get(0).toString());
		Log.i("ffd", "id:"+MyApplication.IDmap.get(0).toString());
		params.addBodyParameter("o","json");
		
	
		
		new NetUtil(this).sendPostToServer(params, Constants.API_CUSTOMER_UPDATE_FORSUB, NewsLeadEditorActivity.this,"updateCustomer");
				
	}
}

///**
// * 发送请求验证手机号是否存在
// * @author angelyin
// * @date 2015-10-25 下午8:43:20
// */
//private void isCheckedMobile() {
//	prodialog.setMessage("加载中");
//	prodialog.show();
//	RequestParams params=new RequestParams();
//	params.addBodyParameter("action","checkCustomerMobile");
//	params.addBodyParameter("school_id",MyApplication.nCrDate.getSchool_id());
//	params.addBodyParameter("mobile",OldeMobai);	
//	params.addBodyParameter("o","json");
//	new NetUtil(this).sendPostToServer(params, Constants.API_CECKED_MOBILE, NewsLeadEditorActivity.this,"checkCustomerMobile");
//	
//	
//}

/**
 * 判断输入框为空
 * @author angelyin
 * @date 2015-10-25 下午8:27:59
 * @return
 */
private boolean validatePhoneAndPwd() {
	if("".equals(Oldeparent)){
		toast("请输入家长姓名");
		return false;
	}
	if("".equals(OldeMobai)){
		toast("请输入手机号码");
		return false;
	}
	if(!Utils.cellphoneValid(OldeMobai)){
		toast("手机号码不正确");
		return false;
	}
	
	if("".equals(OldeStatus)){
		toast("请选择状态");
		return false;
	}
	if("".equals(Oldeyuyue)){
		toast("请选择预约日期");
		return false;
	}
	
	if("".equals(Oldesource)){
		toast("请选择来源");		
		return false;
	}
		
	return true;
	
	
}


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if(requestCode==1212){//回传状态
		if(resultCode==5555){
			String result=data.getStringExtra("oone");
			result_Statusid=data.getStringExtra("oone_id");
			textView_edit_zt.setText(result);
		}
	}
	if(requestCode==1313){//回传来源
	if(resultCode==66666){
		String result=data.getStringExtra("ssecond");
		result_sourceid=data.getStringExtra("ssecond_id");
		textView_edit_source.setText(result);
		}
	}
}


private void popupWindow() {
	LayoutInflater inflater=LayoutInflater.from(this);
	View layoutView=inflater.inflate(R.layout.timepicker, null);
	TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
	TextView ok = (TextView) layoutView.findViewById(R.id.ok);
	ScreenInfo screenInfo = new ScreenInfo(this);
	wheelMain = new WheelYuYue(layoutView);
	wheelMain.screenheight = screenInfo.getHeight();
	String time = textView_edit_contactdate.getText().toString();
	Calendar calendar = Calendar.getInstance();
	if(JudgeDate.isDate(time, "yyyy-MM-dd")){
		try {
			calendar.setTime(dateFormat.parse(time));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	int year = calendar.get(Calendar.YEAR);
	int month = calendar.get(Calendar.MONTH);
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	wheelMain.initDateTimePicker(year,month,day);
	
	ok.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			textView_edit_contactdate.setText(wheelMain.getTime());
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
 * 请求之后此方法用于处理结果
 * @author angelyin
 * @date 2015-10-25 下午8:46:09
 * @param result
 * @param isSuccess
 * @param api
 * @param action
 */
public void onCompleted(String result, boolean isSuccess, String api,String action) {
	if (prodialog != null && prodialog.isShowing()){
		prodialog.dismiss();
		Log.i("Onemeter","关闭prodialog");
	}
	if (!isSuccess) {// 请求不成功
		toast(getResources().getString(R.string.msg_request_error)); 
		return;
	}
	Status status = StatusUtil.handleStatus(this, result);
	/*****************************************************************************/
//	if(api.equals(Constants.API_CECKED_MOBILE)&&action.equals("checkCustomerMobile")){
//		if(status.equals(Status.STATUS_SUCCESS)){//请求成功 
//			
//			ForUpdateSubmit();
//		}
//		if(status.equals(Status.STATUS_UNHANDLE)){//请求失败，处理状态码
//			Integer statusCode = StatusUtil.parseStatus(result);
//			if (statusCode == null)
//				return;
//			if(statusCode == 1){
//				Utils.showToast(this, "参数错误");
//				return;
//			}
//			if(statusCode == 70){
//				Utils.showToast(this, "该家长手机号已存在");
//				return;
//			}		
//		}
//		
//	}
	
	if(api.equals(Constants.API_CUSTOMER_UPDATE_FORSUB)&&action.equals("updateCustomer")){
		if(status.equals(Status.STATUS_SUCCESS)){//请求成功 
			
			intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			
		}
		if(status.equals(Status.STATUS_UNHANDLE)){//请求失败，处理状态码
			Integer statusCode = StatusUtil.parseStatus(result);
			if (statusCode == null)
				return;
			if(statusCode == 1){
				Utils.showToast(this, "参数错误");
				return;
			}
			if(statusCode == 3){
				Utils.showToast(this, "修改失败");
				return;
			}		
		}
		
	}
	
}


}
