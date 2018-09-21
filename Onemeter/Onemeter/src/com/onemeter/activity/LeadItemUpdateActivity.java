package com.onemeter.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.DynamicDataByCustomer;
import com.onemeter.entity.DynamicDataByCustomer.BBody.GgetDynamicDataByCustomerId.DynamicData;
import com.onemeter.entity.GgetCustomerDataById;
import com.onemeter.entity.MyCustomerDataById;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;

/**
 * 更新潜客动态和状态
 * 
 * @author angelyin
 * @date 2015-10-25 上午11:39:24
 */
public class LeadItemUpdateActivity extends BaseActivity implements
		OnClickListener {
	/** 状态布局点击 **/
	private LinearLayout linearLayout_itemupdate_state;
	/** 潜客姓名 **/
	private TextView textView_itemupdate_stu_name;
	/** 潜客状态 **/
	private TextView textView_itemupdate_state;
	/** 潜客最新动态输入 **/
	private EditText editText_itemupdate_dynamic;
	/** 取消按钮 **/
	private Button button_itemupdate_dimiss;
	/** 保存按钮 **/
	private Button button_itemupdate_save;
	/** 更新日期 **/
	private TextView textView_gx_item_date;
	/** 历史动态布局 **/
	private ListView dynamic_listview;
	private DynamicAdapter mdynamicAdapter;
	private List<DynamicData> dynamicDatalist;

	@SuppressLint("SimpleDateFormat")
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	/** 进度条弹框 **/
	ProgressDialog prodialog;// 加载进度条对话框
	private MyCustomerDataById mMyCustomerDataById;
	private List<GgetCustomerDataById> gcdylist;
	String UpdateState;
	String UpdateStatus_history;
	Intent intent;
	String status_id;
	/** 历史动态实体类 **/
	DynamicDataByCustomer mDynamicDataByCustomer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leads_itemupdate_layout);
		prodialog = new ProgressDialog(this);
		gcdylist = new ArrayList<GgetCustomerDataById>();
		dynamicDatalist = new ArrayList<DynamicData>();
		prodialog.setMessage("加载中");
		prodialog.show();
		// 获取指定潜客信息
		getNetCustomerDataById();
		getNetCustomerDynamicById();
		initView();

	}

	/**
	 * 发送请求获取历史动态信息
	 * 
	 * @author angelyin
	 * @date 2015-10-26 上午11:54:47
	 */
	private void getNetCustomerDynamicById() {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(this,
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getDynamicDataByCustomerId");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("customer_id", MyApplication.IDmap.get(0));
		params.addBodyParameter("o", "json");
		params.addBodyParameter("pageSize","50");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_CUSTOMER_GET_DYNAMIC,
				LeadItemUpdateActivity.this, "getDynamicDataByCustomerId");

	}

	/**
	 * 请求获取指定潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-25 下午2:37:38
	 */
	private void getNetCustomerDataById() {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(this,
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerDataById");
		params.addBodyParameter("id", MyApplication.IDmap.get(0));
		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_CUSTOMER_DATABY_MSG, LeadItemUpdateActivity.this,
				"getCustomerDataById");

	}

	private void initView() {
		linearLayout_itemupdate_state = (LinearLayout) findViewById(R.id.linearLayout_itemupdate_state);
		textView_itemupdate_stu_name = (TextView) findViewById(R.id.textView_itemupdate_stu_name);
		textView_itemupdate_state = (TextView) findViewById(R.id.textView_itemupdate_state);
		editText_itemupdate_dynamic = (EditText) findViewById(R.id.editText_itemupdate_dynamic);
		button_itemupdate_dimiss = (Button) findViewById(R.id.button_itemupdate_dimiss);
		button_itemupdate_save = (Button) findViewById(R.id.button_itemupdate_save);
		textView_gx_item_date = (TextView) findViewById(R.id.textView_gx_item_date);
		mdynamicAdapter = new DynamicAdapter(this);
		dynamic_listview = (ListView) findViewById(R.id.dynamic_listview);
		dynamic_listview.setAdapter(mdynamicAdapter);
		button_itemupdate_dimiss.setOnClickListener(this);
		button_itemupdate_save.setOnClickListener(this);
		linearLayout_itemupdate_state.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 状态布局的点击事件
		case R.id.linearLayout_itemupdate_state:
			intent = new Intent(this, LeadUpdateStateActivity.class);
			startActivityForResult(intent, 1010);
			break;
		// 取消按钮的点击事件
		case R.id.button_itemupdate_dimiss:
			finish();
			break;
		// 保存按钮的点击事件
		case R.id.button_itemupdate_save:
			getNetUpdateDate();
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1010) {
			if (resultCode == 66) {
				String status_result = data.getStringExtra("updateStatus");
				status_id = data.getStringExtra("updateStatusId");
				textView_itemupdate_state.setText(status_result);
			}
		}

	}

	/**
	 * 提交更新后的信息
	 * 
	 * @author angelyin
	 * @date 2015-10-25 下午2:40:14
	 */
	private void getNetUpdateDate() {
		UpdateState = textView_itemupdate_state.getText().toString().trim();
		UpdateStatus_history = editText_itemupdate_dynamic.getText().toString()
				.trim();
		if (validatePhoneAndPwd()) {
			// 判断网络是否连接
			if (!MyApplication.isNetAvailable) {
				Utils.showToast(
						this,
						getResources().getString(
								R.string.msg_sys_net_unavailable));
				return;
			}
			getNetUpdateStatus();
			getNetUpdateDynamic();

		}

	}

	/**
	 * 提交修改动态
	 * 
	 * @author angelyin
	 * @date 2015-10-26 上午10:35:16
	 */
	private void getNetUpdateDynamic() {
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "addDynamic");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("branch_id",
				MyApplication.nCrDate.getBranch_id());
		params.addBodyParameter("emp_id", MyApplication.nCrDate.getEmp_id());
		params.addBodyParameter("memo", UpdateStatus_history);
		params.addBodyParameter("customer_id", MyApplication.IDmap.get(0)
				.toString());
		params.addBodyParameter("time_stamp",
				dateFormat.format(new java.util.Date()));
		Log.i("dat", dateFormat.format(new java.util.Date()));
		params.addBodyParameter("o", "json");
		
		new NetUtil(this).sendPostToServer(params,
				Constants.API_CUSTOMER_UPDATE_DYNAMIC,
				LeadItemUpdateActivity.this, "addDynamic");

	}

	/**
	 * 提交修改状态
	 * 
	 * @author angelyin
	 * @date 2015-10-26 上午10:34:16
	 */
	private void getNetUpdateStatus() {
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "modifyCustomersStatus");
		params.addBodyParameter("ids", MyApplication.IDmap.get(0));
		Log.i("ddf", "ids:" + MyApplication.IDmap.get(0));
		if (gcdylist.get(0).getStatusdata().getStatus().equals(UpdateState)) {
			params.addBodyParameter("sid", gcdylist.get(0).getStatus_id() + "");
			Log.i("ddf", "sid:" + gcdylist.get(0).getStatus_id());
		} else {
			params.addBodyParameter("sid", status_id);
			Log.i("ddf", "sid:" + gcdylist.get(0).getStatus_id());
		}
		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_CUSTOMER_ALLMSG, LeadItemUpdateActivity.this,
				"modifyCustomersStatus");

	}

	/**
	 * 判断为空的情况
	 * 
	 * @author angelyin
	 * @date 2015-10-25 下午2:50:36
	 * @return
	 */
	private boolean validatePhoneAndPwd() {
		if ("".equals(UpdateState)) {
			// 状态不能为空
			toast("");
			return false;
		}
		if ("".equals(UpdateStatus_history)) {
			toast("动态不能为空");
			return false;
		}
		return true;
	}

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
		/***************************************************************/
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
				initViewData();

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

		if (api.equals(Constants.API_CUSTOMER_UPDATE_STATUS)
				&& action.equals("modifyCustomersStatus")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 成功
				toast("状态修改成功");
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);

			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 失败
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(this, "参数错误");
					return;
				}
				if (statusCode == 3) {
					Utils.showToast(this, "修改失败");
					return;
				}
			}

		}

		if (api.equals(Constants.API_CUSTOMER_UPDATE_DYNAMIC)
				&& action.equals("addDynamic")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 成功
				toast("动态修改成功");

			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 失败
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(this, "参数错误");
					return;
				}
				if (statusCode == 3) {
					Utils.showToast(this, "修改失败");
					return;
				}
			}
		}

		if (api.equals(Constants.API_CUSTOMER_GET_DYNAMIC)
				&& action.equals("getDynamicDataByCustomerId")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 成功
//				toast("动态获取成功");
				String count = null;
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject2 = jsonObject.getJSONObject("body");
					JSONObject jsonObject3 = jsonObject2
							.getJSONObject("getDynamicDataByCustomerId");
					count = jsonObject3.getString("count");

				} catch (JSONException e) {

					e.printStackTrace();
				}
				if (count.equals("0")) {
					return;
				} else {
					mDynamicDataByCustomer = (DynamicDataByCustomer) GsonUtil
							.convertJson2Object(result,
									DynamicDataByCustomer.class,
									GsonUtil.JSON_JAVABEAN);
					dynamicDatalist = mDynamicDataByCustomer.getBody()
							.getGetDynamicDataByCustomerId().getData();
					mdynamicAdapter.notifyDataSetChanged();
				}
			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 失败
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
	 * 更新组件数据
	 * 
	 * @author angelyin
	 * @date 2015-10-25 下午12:27:45
	 */
	private void initViewData() {
		textView_itemupdate_stu_name.setText(gcdylist.get(0).getStudent_name());
		textView_itemupdate_state.setText(gcdylist.get(0).getStatusdata()
				.getStatus());
		textView_gx_item_date.setText(Utils.parseDate(gcdylist.get(0)
				.getTime_stamp()));
		editText_itemupdate_dynamic.setText("");

	}

	/**
	 * 添加历史动态适配器
	 * 
	 * @author angelyin
	 * @date 2015-10-26 下午1:00:16
	 */
	public class DynamicAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;

		public DynamicAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return dynamicDatalist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dynamicDatalist.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.dynamic_layout, null);
				holder = new ViewHolder();
				holder.dynamic_time = (TextView) convertView
						.findViewById(R.id.dynamic_time);
				holder.dynamic_ttext = (TextView) convertView
						.findViewById(R.id.dynamic_ttext);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.dynamic_time.setText(Utils.parseDate(dynamicDatalist
					.get(position).getTime_stamp().toString()));
			holder.dynamic_ttext.setText(dynamicDatalist.get(position)
					.getMemo().toString());
			return convertView;
		}

		class ViewHolder {
			TextView dynamic_time;
			TextView dynamic_ttext;
		}

	}
}
