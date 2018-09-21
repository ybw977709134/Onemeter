package com.onemeter.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;

/**
 * 潜客详情
 * 
 * @author angelyin
 * @date 2015-10-23 下午4:52:19
 */
public class LeadsDetailsActivity extends BaseActivity {
	/** 学生名称 **/
	private TextView textView_details_stu_name;
	/** 兴趣的课程 **/
	private TextView textView_details_course;
	/** 性别 **/
	private TextView textView_details_sex;
	/** 生日 **/
	private TextView textView_details_birthday;
	/** 年级 **/
	private TextView textView_details_class;
	/** 家长姓名 **/
	private TextView textView_details_pat_name;
	/** 手机号码 **/
	private TextView textView_details_pat_phone;
	/** 状态 **/
	private TextView textView_details_state;
	/** 预约日期 **/
	private TextView textView_details_yuyue_date;
	/** 来源 **/
	private TextView textView_details_source;
	/** 添加日期 **/
	private TextView textView_details_add_date;
	/** 返回键 **/
	private LinearLayout linearLayout_details_back;

	/** 进度条弹框 **/
	ProgressDialog prodialog;// 加载进度条对话框
	private MyCustomerDataById mMyCustomerDataById;
	private List<GgetCustomerDataById> gcdylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lead_details_layout);
		prodialog = new ProgressDialog(this);
		gcdylist = new ArrayList<GgetCustomerDataById>();
		getNetCustomerDataBy();
		initView();
	}

	/**
	 * 发送请求获取指定潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-23 下午4:53:07
	 */
	private void getNetCustomerDataBy() {
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerDataById");
		params.addBodyParameter("id", MyApplication.IDmap.get(0).toString());
		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_CUSTOMER_DATABY_MSG, LeadsDetailsActivity.this,
				"getCustomerDataById");

	}

	private void initView() {
		textView_details_stu_name = (TextView) findViewById(R.id.textView_details_stu_name);
		textView_details_course = (TextView) findViewById(R.id.textView_details_course);
		textView_details_sex = (TextView) findViewById(R.id.textView_details_sex);
		textView_details_birthday = (TextView) findViewById(R.id.textView_details_birthday);
		textView_details_class = (TextView) findViewById(R.id.textView_details_class);
		textView_details_pat_name = (TextView) findViewById(R.id.textView_details_pat_name);
		textView_details_state = (TextView) findViewById(R.id.textView_details_state);
		textView_details_pat_phone = (TextView) findViewById(R.id.textView_details_pat_phone);
		textView_details_yuyue_date = (TextView) findViewById(R.id.textView_details_yuyue_date);
		textView_details_source = (TextView) findViewById(R.id.textView_details_source);
		textView_details_add_date = (TextView) findViewById(R.id.textView_details_add_date);
		linearLayout_details_back = (LinearLayout) findViewById(R.id.linearLayout_details_back);
		linearLayout_details_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	/**
	 * 请求结束更新数据
	 * 
	 * @author angelyin
	 * @date 2015-10-23 下午4:59:57
	 * @param result
	 * @param isSuccess
	 * @param api
	 * @param action
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
		/*************************************************************/

		if (api.equals(Constants.API_CUSTOMER_DATABY_MSG)
				&& action.equals("getCustomerDataById")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
				mMyCustomerDataById = (MyCustomerDataById) GsonUtil
						.convertJson2Object(result, MyCustomerDataById.class,
								GsonUtil.JSON_JAVABEAN);
				if (gcdylist.size() != 0) {
					gcdylist.clear();
					gcdylist = mMyCustomerDataById.getBody()
							.getGetCustomerDataById();
				} else {
					gcdylist = mMyCustomerDataById.getBody()
							.getGetCustomerDataById();
				}

				initDateView();

			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;

				if (statusCode == 1) {
					Utils.showToast(this, "参数错误");
					return;
				}
			}
		}

	}

	/**
	 * 此方法用于更新UI组件数据
	 * 
	 * @author angelyin
	 * @date 2015-10-23 下午5:04:29
	 */
	private void initDateView() {
		textView_details_stu_name.setText(gcdylist.get(0).getStudent_name());
		textView_details_course.setText(gcdylist.get(0).getInterest_course());
		if (gcdylist.get(0).getSex().equals("1")) {
			textView_details_sex.setText("男");
		} else if (gcdylist.get(0).getSex().equals("2")) {
			textView_details_sex.setText("女");
		} else if (gcdylist.get(0).getSex().equals("3")) {
			textView_details_sex.setText("保密");
		}
		textView_details_birthday.setText(gcdylist.get(0).getBirthday());
		textView_details_class.setText(gcdylist.get(0).getGrade_info());
		textView_details_pat_name.setText(gcdylist.get(0).getParent_name());
		textView_details_pat_phone.setText(gcdylist.get(0).getMobile());
		if(null!=MyApplication.dataBean.get(0)){
			if (null!=MyApplication.dataBean.get(0).getStatusdata()){
				if (!TextUtils.isEmpty(MyApplication.dataBean.get(0).getStatusdata().getStatus())) {
					textView_details_state.setText(gcdylist.get(0).getStatusdata().getStatus());
				}else {
					textView_details_state.setText(null);
				}
				}else {
					textView_details_state.setText(null);
				}
			}
		textView_details_yuyue_date.setText(gcdylist.get(0).getBook_date());
		if(null!=MyApplication.dataBean.get(0)){
			if (null!=MyApplication.dataBean.get(0).getSourcedata()){
				if (!TextUtils.isEmpty(MyApplication.dataBean.get(0).getSourcedata().getSource())) {
					textView_details_source.setText(gcdylist.get(0).getSourcedata().getSource());
				}else {
					textView_details_source.setText(null);
				}
				}else {
					textView_details_source.setText(null);
			}
		}
		
		textView_details_add_date.setText(Utils.parseDate(gcdylist.get(0).getLeads_date()));

	}
}
