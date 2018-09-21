package com.onemeter.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.activity.NewLeadStateActivity.NewLeadStateAdapter;
import com.onemeter.activity.NewLeadStateActivity.NewLeadStateAdapter.ViewHolder;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.StatusDDate;
import com.onemeter.entity.StatusDDate.BBody.getStatusData;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.Utils;
import com.onemeter.util.StatusUtil.Status;

public class LeadUpdateStateActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	private LinearLayout linearLayout_back_state = null;
	private ListView listView_state;
	/** 加载进度条对话框 */
	private ProgressDialog prodialog;
	StatusDDate mStatusDate;
	NewLeadStateAdapter newLeadStateAdapter;
	private List<getStatusData> gstlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newlead_state_layout);
		prodialog = new ProgressDialog(this);// 进度条对话框
		prodialog.setMessage("加载中");
		getStatus();
		initView();

	}

	private void initView() {
		linearLayout_back_state = (LinearLayout) findViewById(R.id.linearLayout_back_state);
		listView_state = (ListView) findViewById(R.id.listView_state);
		gstlist = new ArrayList<getStatusData>();
		newLeadStateAdapter = new NewLeadStateAdapter(this);
		listView_state.setAdapter(newLeadStateAdapter);
		listView_state.setOnItemClickListener(this);
		linearLayout_back_state.setOnClickListener(this);

	}

	/**
	 * 发送请求获取状态的相关数据
	 * 
	 * @author angelyin
	 * @date 2015-10-14 上午10:59:17
	 */
	private void getStatus() {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {// 判断网络是否可用
			toast(getResources().getString(R.string.msg_sys_net_unavailable));
			finish();
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getStatusData");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());

		params.addBodyParameter("o", "json");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_TIANJIA_STATUS, LeadUpdateStateActivity.this,
				"getStatusData");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearLayout_back_state:
			finish();
			break;

		}
	}

	/**
	 * 当请求数据完成后调用此方法更新数据
	 * 
	 * @author angelyin
	 * @date 2015-10-14 上午11:17:32
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
		if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功

			mStatusDate = (StatusDDate) GsonUtil.convertJson2Object(result,
					StatusDDate.class, GsonUtil.JSON_JAVABEAN);

			gstlist.clear();
			gstlist = mStatusDate.getBody().getGetStatusData();
			newLeadStateAdapter.notifyDataSetChanged();

//			toast("获取状态成功");

		}
		if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
			Integer statusCode = StatusUtil.parseStatus(result);
			if (statusCode == null)
				return;
			if (statusCode == 1) {
				Utils.showToast(this, "参数错误");
				return;
			}

		}
	}

	/**
	 * 适配器
	 * 
	 * @author angelyin
	 * @date 2015-10-14 下午7:03:34
	 */
	public class NewLeadStateAdapter extends BaseAdapter {

		private Context context;

		public NewLeadStateAdapter(Context context) {
			this.context = context;

		}

		@Override
		public int getCount() {

			return gstlist.size();

		}

		@Override
		public Object getItem(int position) {
			return gstlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.newlead_state_item, null);
				holder.textView_state = (TextView) convertView
						.findViewById(R.id.textView_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			holder.textView_state.setText(gstlist.get(position).getStatus()
					.toString());
			return convertView;
		}

		public class ViewHolder {
			private TextView textView_state;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		String str = gstlist.get(position).getStatus().toString();

		String state_id = gstlist.get(position).getId();
		toast(str);
		// 回传到添加潜客提交页面
		Intent intent = new Intent();
		intent.putExtra("updateStatus", str);
		intent.putExtra("updateStatusId", state_id);
		setResult(66, intent);
		finish();

	}
}
