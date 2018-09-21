package com.onemeter.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.util.Utils;

/**
 * 潜客批量更新操作页面
 * @author angelyin
 * @date 2015-10-27 下午5:52:22
 */
public class LeadNewItemUpdateActivity extends BaseActivity implements OnClickListener{
	/**取消按钮**/
	private Button new_button_update_dimiss;
	/**保存按钮**/
	private Button new_button_update_save;
	/**学生姓名**/
	private TextView new_textView_update_stu_name;
	/**状态**/
	private TextView new_textView_update_state;
	/**更新日期**/
	private TextView new_textView_gx_date;
	/**最新状态**/
	private EditText new_editText_update_dynamic;
	/**状态栏点击**/
	private LinearLayout new_linearLayout_update_state;
	Intent  intent;
	String result_id;
	private String  newState;
	private String   newDate; 
	private String   newdynamic; 
	private MyCustomerDataById mMyCustomerDataById;
	private List<GgetCustomerDataById>  gcdylist;
    String  oldeState;
    String  oldestateId;
    @SuppressLint("SimpleDateFormat")
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**进度条弹框**/
	 ProgressDialog prodialog;// 加载进度条对话框
	 String cusID="";
	 String cusName="";
	 Intent iintent;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.leads_update_layout);
    	prodialog=new ProgressDialog(this);
    	gcdylist=new ArrayList<GgetCustomerDataById>();
    	getNetNewCustomerstate();
    	initView();
    	
    	
    }
  
	/**
      * 根据ID获取潜客信息
      * @author angelyin
      * @date 2015-10-28 上午11:12:35
      */
     private void getNetNewCustomerstate() {
    	String CustomerID=MyApplication.checkedID.get(0);
    	// 判断网络是否连接
    	if (!MyApplication.isNetAvailable) {
    		Utils.showToast(this,getResources().getString(R.string.msg_sys_net_unavailable));
    			return;
    		}	
    	RequestParams params=new RequestParams();
		params.addBodyParameter("action","getCustomerDataById");		
		params.addBodyParameter("id",CustomerID);
		params.addBodyParameter("o","json");
		new NetUtil(this).sendPostToServer(params, Constants.API_CUSTOMER_DATABY_MSG, LeadNewItemUpdateActivity.this,"getCustomerDataById");
    	
		
	}
     
	/**
      * 初始化数据
      * @author angelyin
      * @date 2015-10-27 下午7:08:33
      */
	private void initView() {
		//拼接姓名
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < MyApplication.checkedName.size(); i++) {
			cusName=MyApplication.checkedName.get(i);
				  sb.append(cusName+",");
			}
		cusName=  sb.toString();
		cusName=cusName.substring(0,cusName.length()-1);
		//拼接ID
		StringBuilder sb1 = new StringBuilder();

		for (int i = 0; i < MyApplication.checkedID.size(); i++) {
				  cusID=MyApplication.checkedID.get(i);
				  sb1.append(cusID+",");
			}
		cusID=  sb1.toString();
		cusID=cusID.substring(0,cusID.length()-1);
		
		
		new_button_update_dimiss=(Button)findViewById(R.id.new_button_update_dimiss);
		new_button_update_save=(Button)findViewById(R.id.new_button_update_save);
		new_textView_update_stu_name=(TextView)findViewById(R.id.new_textView_update_stu_name);
		new_textView_update_stu_name.setText(cusName);
		new_textView_update_state=(TextView)findViewById(R.id.new_textView_update_state);
		new_textView_gx_date=(TextView)findViewById(R.id.new_textView_gx_date);
		new_linearLayout_update_state=(LinearLayout)findViewById(R.id.new_linearLayout_update_state);
		new_editText_update_dynamic=(EditText)findViewById(R.id.new_editText_update_dynamic);
		new_textView_gx_date.setText(dateFormat.format(new java.util.Date()));
		new_button_update_dimiss.setOnClickListener(this);
		new_button_update_save.setOnClickListener(this);
		new_linearLayout_update_state.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_button_update_dimiss:
			//取消			
			new_textView_update_stu_name.setText("");
			iintent= new Intent();
			iintent.setAction("sateresult");	
			iintent.putExtra("tyype", 1);
			sendBroadcast(iintent);			
//			Intent	intent22=new Intent(this,MainActivity.class);
//			startActivity(intent22);
			finish();
			break;
		case R.id.new_button_update_save:
			//保存
			NetPiLiangUpdate();
			break;
		case R.id.new_linearLayout_update_state:
			//状态选择
			intent=new Intent(LeadNewItemUpdateActivity.this,NewLeadStateActivity.class);
			intent.putExtra("resultCode", "212121");
			startActivityForResult(intent, 8888);			
			break;
		}
		
	}
	/**
	 * 批量更新状态
	 * @author angelyin
	 * @date 2015-10-28 上午10:15:37
	 */
	private void NetPiLiangUpdate() {
		newState=new_textView_update_state.getText().toString().trim();
		newDate=new_textView_gx_date.getText().toString().trim();
		newdynamic= new_editText_update_dynamic.getText().toString().trim();
		if(validatePhoneAndPwd()){
			// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
				Utils.showToast(this,getResources().getString(R.string.msg_sys_net_unavailable));
					return;
				}
		
		 getNetNewUpdateStatus();
		 getNetNewUpdateDynamic();
		}
		
	}
	/**
	 * 提交动态信息到服务器
	 * @author angelyin
	 * @date 2015-10-28 上午10:27:12
	 */
	private void getNetNewUpdateDynamic() {
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params=new RequestParams();
		params.addBodyParameter("action", "batchAddDynamic");
		params.addBodyParameter("school_id", MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("branch_id", MyApplication.nCrDate.getBranch_id());
		params.addBodyParameter("emp_id", MyApplication.nCrDate.getEmp_id());
		params.addBodyParameter("memo", newdynamic);
		params.addBodyParameter("customer_id",cusID);
		params.addBodyParameter("time_stamp", dateFormat.format(new java.util.Date()));
//		Log.i("dat", dateFormat.format(new java.util.Date()));	
		params.addBodyParameter("o","json");
		new NetUtil(this).sendPostToServer(params,Constants.API_CUSTOMER_UPDATE_PILIANGDONGTAI, LeadNewItemUpdateActivity.this,"batchAddDynamic");

		
		
	}
	
	/**
	 * 提交批量状态到服务器
	 * @author angelyin
	 * @date 2015-10-28 上午10:27:35
	 */
	private void getNetNewUpdateStatus() {
		
		
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params=new RequestParams();
		params.addBodyParameter("action", "modifyCustomersStatus");
		params.addBodyParameter("ids",cusID);
		Log.i("uud", "cusID:"+cusID);
		if(newState.equals(oldeState)){
		params.addBodyParameter("sid",oldestateId);
		}else {
		params.addBodyParameter("sid",result_id);
		Log.i("uud", "result_id:"+result_id);
		}
		params.addBodyParameter("o","json");
		new NetUtil(this).sendPostToServer(params,Constants.API_CUSTOMER_UPDATE_STATUS, LeadNewItemUpdateActivity.this,"modifyCustomersStatus");
		
	}
	
	/**
	 * 判断不为空的情况
	 * @author angelyin
	 * @date 2015-10-28 上午10:27:55
	 * @return
	 */
	private boolean validatePhoneAndPwd() {
		if("".equals(newState)){
			Utils.showToast(this, "请选择状态");
			return false;
		}
		
		if("".equals(newDate)){
			Utils.showToast(this, "更新日期不能为空");
			return false;
		}
		
		if("".equals(newdynamic)){
			Utils.showToast(this, "动态不能为空");
			return false;
		}
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(requestCode==8888){//状态选择
    	  if(resultCode==212121){
    		  String result=data.getStringExtra("oone");
    		  result_id=data.getStringExtra("oone_id");
    		  new_textView_update_state.setText(result);
    	  }
      }
	
	
	}
	
	
	
	/**
	 * 请求结束后此方法用于更新数据
	 * @author angelyin
	 * @date 2015-10-28 上午10:50:55
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
   /******************************************************************************/
		if(api.equals(Constants.API_CUSTOMER_UPDATE_STATUS)&&action.equals("modifyCustomersStatus")){		
			if(status.equals(Status.STATUS_SUCCESS)){//成功
//				toast("批量状态修改成功");
				new_textView_update_stu_name.setText("");
				intent=new Intent(this,MainActivity.class);
				startActivity(intent);
				
			}
			if(status.equals(Status.STATUS_UNHANDLE)){//失败
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if(statusCode==1){
					Utils.showToast(this, "参数错误");
					return;
				}
				if(statusCode==3){
					Utils.showToast(this, "修改失败");
					return;
				}
			}
			
		}
		
		
		if(api.equals(Constants.API_CUSTOMER_DATABY_MSG)&&action.equals("getCustomerDataById")){
			if(status.equals(Status.STATUS_SUCCESS)){//请求成功
				mMyCustomerDataById = (MyCustomerDataById) GsonUtil.convertJson2Object(result, MyCustomerDataById.class,
						GsonUtil.JSON_JAVABEAN);				
					gcdylist.clear();
					gcdylist=mMyCustomerDataById.getBody().getGetCustomerDataById();
			
				//获取指定ID成功后设置状态值
				new_textView_update_state.setText(gcdylist.get(0).getStatusdata().getStatus());
				oldeState=gcdylist.get(0).getStatusdata().getStatus();
				oldestateId=gcdylist.get(0).getStatusdata().getId();
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
		
		if(api.equals(Constants.API_CUSTOMER_UPDATE_DYNAMIC)&&action.equals("addDynamic")){
			if(status.equals(Status.STATUS_SUCCESS)){//成功
				toast("修改成功");
							
			}
			if(status.equals(Status.STATUS_UNHANDLE)){//失败
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if(statusCode==1){
					Utils.showToast(this, "参数错误");
					return;
				}
				if(statusCode==3){
					Utils.showToast(this, "修改失败");
					return;
				}
			}
		}
		
		
	}
}
