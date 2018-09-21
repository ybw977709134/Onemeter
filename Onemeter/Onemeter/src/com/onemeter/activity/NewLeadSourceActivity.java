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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.SourceDatta;
import com.onemeter.entity.SourceDatta.BBody.getSourceData;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.util.Utils;

/**
 * 获取来源
 * 
 * @author angelyin
 * @date 2015-10-14 下午9:51:47
 */
public class NewLeadSourceActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private LinearLayout linearLayout_back_source = null;
	private ListView listView_source;
	/** 加载进度条对话框 */
	private ProgressDialog prodialog;
	NewLeadSourceAdapter newLeadSourceAdapter;
	SourceDatta mSourceData;
	private List<getSourceData> gtSDataList;
	String resultCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newlead_source_layout);
		prodialog = new ProgressDialog(this);// 进度条对话框
		gtSDataList = new ArrayList<getSourceData>();
		prodialog.setMessage("加载中");
		resultCode = getIntent().getStringExtra("resultCode");
		getSource();
		initView();
	}

	/**
	 * 发送请求获取相关来源数据
	 * 
	 * @author angelyin
	 * @date 2015-10-14 下午9:59:16
	 */
	private void getSource() {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {// 判断网络是否可用
			toast(getResources().getString(R.string.msg_sys_net_unavailable));
			finish();
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getSourceData");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("o", "json");
		params.addBodyParameter("limit","50");
		new NetUtil(this).sendPostToServer(params,
				Constants.API_TIANJIA_SOURCE, this, "getSourceData");

	}

	private void initView() {
		linearLayout_back_source = (LinearLayout) findViewById(R.id.linearLayout_back_source);
		listView_source = (ListView) findViewById(R.id.listView_source);
		newLeadSourceAdapter = new NewLeadSourceAdapter(this);
		listView_source.setAdapter(newLeadSourceAdapter);

		linearLayout_back_source.setOnClickListener(this);
		listView_source.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearLayout_back_source:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 当请求数据完成后调用此方法更新数据
	 * 
	 * @author angelyin
	 * @date 2015-10-14 下午10:05:34
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
			mSourceData = (SourceDatta) GsonUtil.convertJson2Object(result,
					SourceDatta.class, GsonUtil.JSON_JAVABEAN);
			gtSDataList = mSourceData.getBody().getGetSourceData();

			newLeadSourceAdapter.notifyDataSetChanged();

			// try {
			// JSONObject jsonObject=new JSONObject(result);
			// JSONObject jsonObject1 = jsonObject.getJSONObject("body");
			// JSONArray jsonArray=jsonObject1.getJSONArray("getSourceData");
			// for (int i = 0; i < jsonArray.length(); i++) {
			// str_source_id[i]=jsonArray.getJSONObject(i).getString("id");
			// str_source[i] =jsonArray.getJSONObject(i).getString("source");
			// Log.i("Onemeter",
			// jsonArray.getJSONObject(i).getString("source"));
			// newLeadSourceAdapter.notifyDataSetChanged();
			// }
			//
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
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

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		String source = gtSDataList.get(position).getSource().toString();
		String source_id = gtSDataList.get(position).getId();
		toast(source);
		Intent intent = new Intent();
		intent.putExtra("ssecond", source);
		intent.putExtra("ssecond_id", source_id);
		if (resultCode.equals("22")) {
			setResult(22, intent);
		} else if (resultCode.equals("66666")) {
			setResult(66666, intent);
		} else if(resultCode.equals("6161")){
			setResult(6161, intent);
		}
		finish();
	}

	public class NewLeadSourceAdapter extends BaseAdapter {

		private Context context;

		public NewLeadSourceAdapter(Context context) {
			this.context = context;

		}

		@Override
		public int getCount() {

			return gtSDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return gtSDataList.get(position);
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
						R.layout.newlead_source_item, null);
				holder.textView_source = (TextView) convertView
						.findViewById(R.id.textView_source);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			holder.textView_source.setText(gtSDataList.get(position)
					.getSource().toString());
			return convertView;
		}

		public class ViewHolder {
			private TextView textView_source;

		}

	}
}
