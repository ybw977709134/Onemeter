package com.onemeter.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.onemeter.entity.GgetCustomerDataById;
import com.onemeter.entity.MyCustomerDataById;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelMain;
/**
 * 新增潜客信息--》第一步
 * @author angelyin
 * @date 2015-10-13 下午10:23:41
 */
public class NewAddLeadEditActivity extends BaseActivity implements OnClickListener{
	/**联系方式（点击）**/
	private TextView lianxifangshi;
	private Button button_edit_cancel=null;
	private Button button_edit_next=null;
	/**学生姓名**/
	private EditText editText_edit_student_name=null;
    /**感兴趣课程**/	
	private EditText editText_edit_like_class=null;
	/**性别**/
	private TextView edt_sex; 
	/**生日**/
	private TextView  tx_birthday=null;
	/**年级**/
	private TextView  edt_grahf_class=null;
	
	private LinearLayout linearLayout_edit_sex=null;
	private LinearLayout linearLayout_edit_birthday=null;
	private LinearLayout linearLayout_edit_class=null;
	
	@SuppressLint("SimpleDateFormat")
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	WheelMain wheelMain;
	private PopupWindow mPopupWindow;
	private Context context;
	/**进度条弹框**/
	 ProgressDialog prodialog;// 加载进度条对话框
	 private MyCustomerDataById mMyCustomerDataById;
	 private List<GgetCustomerDataById>  gcdylist;
	 String StudentName;
	 String course;
	 String sex;
	 String birthday;
	 String grahf_class;
	 Intent intent;
	 String  customerr_id;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.addnewlead_bianji_layout);
	intent=getIntent();
	customerr_id=intent.getStringExtra("customerr_id");
	prodialog=new ProgressDialog(this);
	gcdylist=new ArrayList<GgetCustomerDataById>();
	getNetCustomerDataBy();
	initView();
	 
} 

/**
 * 根据Id获取指潜客信息
 * @author angelyin
 * @date 2015-10-25 下午12:22:12
 */
private void getNetCustomerDataBy() {
	// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(this,getResources().getString(R.string.msg_sys_net_unavailable));
						return;
			}
	    prodialog.setMessage("加载中");
	    prodialog.show();
		RequestParams params=new RequestParams();
		params.addBodyParameter("action","getCustomerDataById");				
		params.addBodyParameter("id",customerr_id);		
		params.addBodyParameter("o","json");
		new NetUtil(this).sendPostToServer(params, Constants.API_CUSTOMER_DATABY_MSG, NewAddLeadEditActivity.this,"getCustomerDataById");
	
}

private void initView() {
	button_edit_cancel=(Button)findViewById(R.id.add_button_edit_cancel);
	button_edit_cancel.setOnClickListener(this);
	button_edit_next=(Button)findViewById(R.id.add_button_edit_next);
	button_edit_next.setOnClickListener(this);
	linearLayout_edit_sex=(LinearLayout)findViewById(R.id.add_linearLayout_edit_sex);
	linearLayout_edit_birthday=(LinearLayout)findViewById(R.id.add_linearLayout_edit_birthday);
	linearLayout_edit_class=(LinearLayout)findViewById(R.id.add_linearLayout_edit_class);
	linearLayout_edit_sex.setOnClickListener(this);
	linearLayout_edit_birthday.setOnClickListener(this);
	linearLayout_edit_class.setOnClickListener(this);
	
	tx_birthday=(TextView)findViewById(R.id.add_tx_birthday);
	editText_edit_student_name=(EditText)findViewById(R.id.add_editText_edit_student_name);
	editText_edit_like_class=(EditText)findViewById(R.id.add_editText_edit_like_class);
	edt_sex=(TextView)findViewById(R.id.add_edt_sex);
	edt_grahf_class=(TextView)findViewById(R.id.add_edt_grahf_class);
	
	
}

@Override
public void onClick(View view) {
    switch (view.getId()) {
	case R.id.add_button_edit_cancel:
		//取消	
		finish();
		break;
	case R.id.add_button_edit_next:
		//下一步		
		getUpdateDate();
		
		break;
	case R.id.add_linearLayout_edit_sex:
			//修改性别
		Intent intent2=new Intent(this,NewLeadSexActivity.class);
		intent2.putExtra("requestCode", "131313");
		startActivityForResult(intent2,1122);
		break;
	case R.id.add_linearLayout_edit_birthday:
		//修改生日
		popupWindow();
		break;
	case R.id.add_linearLayout_edit_class:
		//修改年级
		Intent intent3=new Intent(this,NewLeadClassActivity.class);
		intent3.putExtra("requestCode", "141414");
		startActivityForResult(intent3,2233);
		break;

	default:
		break;
	}
}

/**
 * 获取更新后的数据
 * @author angelyin
 * @date 2015-10-25 下午6:14:31
 */
 private void getUpdateDate() {
	 StudentName=editText_edit_student_name.getText().toString().trim();
	 course=editText_edit_like_class.getText().toString().trim();
	 sex=edt_sex.getText().toString().trim();
	 birthday=tx_birthday.getText().toString().trim();
	 grahf_class=edt_grahf_class.getText().toString().trim();
	 	 
	 //如果不为空则存放到集合中保存
	if(validatePhoneAndPwd()){
					
		MyApplication.nCrDatelist.setStudentName(StudentName);
		MyApplication.nCrDatelist.setKecheng(course);
		MyApplication.nCrDatelist.setBirthday(birthday);
		MyApplication.nCrDatelist.setGrade_info(grahf_class);
		if(sex.equals("男")){
			MyApplication.nCrDatelist.setSex("1");
		}else if(sex.equals("女")){
			MyApplication.nCrDatelist.setSex("2");
		}else if(sex.equals("保密")){
			MyApplication.nCrDatelist.setSex("3");
		}
		intent=new Intent(this,NewsAddLeadEditorActivity.class);		
		intent.putExtra("Parent", gcdylist.get(0).getParent_name());
		intent.putExtra("Mobile", gcdylist.get(0).getMobile());
		intent.putExtra("Status", gcdylist.get(0).getStatusdata().getStatus());
		intent.putExtra("Status_id", gcdylist.get(0).getStatus_id()+"");
		intent.putExtra("Book_date", gcdylist.get(0).getBook_date());
		intent.putExtra("Source", gcdylist.get(0).getSourcedata().getSource());
		intent.putExtra("Source_id", gcdylist.get(0).getSource_id()+"");
		intent.putExtra("Customer_id",customerr_id);
		startActivity(intent);
		
	} 
	 
	 
	 
	 
}

private boolean validatePhoneAndPwd() {
	if("".equals(StudentName)){
		toast("请输入学生姓名");
		return false;
	}
//	if("".equals(sex)){
//		toast("请选择性别");
//		return false;
//	}
	if("".equals(birthday)){
		toast("请选择生日");
		return false;
	}
//	if("".equals(grahf_class)){
//		toast("请选择年级");
//		return false;
//	}
	return true;
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if(requestCode==1122){//性别
		if(resultCode==131313){
		 String result=data.getStringExtra("one");
		 String result_id=data.getStringExtra("one_id");
		   edt_sex.setText(result);
		}
	}
	
	if(requestCode==2233){//年级
		if(resultCode==141414){
			 String result=data.getStringExtra("second");
			 edt_grahf_class.setText(result);
			}
	}
}
private void popupWindow() {
	LayoutInflater inflater=LayoutInflater.from(this);
	View layoutView=inflater.inflate(R.layout.timepicker, null);
	TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
	TextView ok = (TextView) layoutView.findViewById(R.id.ok);
	ScreenInfo screenInfo = new ScreenInfo(this);
	wheelMain = new WheelMain(layoutView);
	wheelMain.screenheight = screenInfo.getHeight();
	String time = tx_birthday.getText().toString();
	Calendar calendar = Calendar.getInstance();
	if(JudgeDate.isDate(time, "yyyy-MM-dd")){
		try {
			calendar.setTime(dateFormat.parse(time));
		} catch (ParseException e) {
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
			tx_birthday.setText(wheelMain.getTime());
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
 * 请求结束后此方法用于更新数据
 * @author angelyin
 * @date 2015-10-23 下午2:37:29
 * @param result
 * @param isSuccess
 * @param api
 * @param action
 */
public void onCompleted(String result, boolean isSuccess, String api,
		String action) {
	if (prodialog != null && prodialog.isShowing()){
		prodialog.dismiss();
		Log.i("Onemeter","关闭prodialog");
	}
	if (!isSuccess) {// 请求不成功
		toast(getResources().getString(R.string.msg_request_error)); 
		return;
	}
	Status status = StatusUtil.handleStatus(this, result);
	/***************************************************************/
	if(api.equals(Constants.API_CUSTOMER_DATABY_MSG)&&action.equals("getCustomerDataById")){
		if(status.equals(Status.STATUS_SUCCESS)){//请求成功
			mMyCustomerDataById = (MyCustomerDataById) GsonUtil.convertJson2Object(result, MyCustomerDataById.class,
					GsonUtil.JSON_JAVABEAN);
			if(gcdylist.size()!=0){
				gcdylist.clear();
				gcdylist=mMyCustomerDataById.getBody().getGetCustomerDataById();
			}else{			
			  gcdylist=mMyCustomerDataById.getBody().getGetCustomerDataById();
			}
			intDataView();
			
		}
		if(status.equals(Status.STATUS_UNHANDLE)){//请求失败
			Integer statusCode = StatusUtil.parseStatus(result);
			if (statusCode == null)
				return;
	 		
			if(statusCode==1){
				Utils.showToast(this, "参数错误");
				return;
			}
		}
	}
}

/**
 * 获取数据后更新数据到组件上
 * @author angelyin
 * @date 2015-10-23 下午3:28:52
 */
private void intDataView() {
	//设置学生姓名
	editText_edit_student_name.setText(gcdylist.get(0).getStudent_name().toString());
	editText_edit_like_class.setText(gcdylist.get(0).getInterest_course());
	if(gcdylist.get(0).getSex().equals("3")){
	edt_sex.setText("保密");
	}else if(gcdylist.get(0).getSex().equals("2")){
		edt_sex.setText("女");
	}else if(gcdylist.get(0).getSex().equals("1")){
		edt_sex.setText("男");
	}
	tx_birthday.setText(gcdylist.get(0).getBirthday().toString());
	edt_grahf_class.setText(gcdylist.get(0).getGrade_info().toString());
	
}





}
