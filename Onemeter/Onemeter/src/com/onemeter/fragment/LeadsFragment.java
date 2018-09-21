package com.onemeter.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshBase.Mode;
import pulltorefresh.PullToRefreshBase.OnRefreshListener;
import pulltorefresh.PullToRefreshListView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.activity.LeadItemUpdateActivity;
import com.onemeter.activity.LeadNewItemUpdateActivity;
import com.onemeter.activity.LeadsDetailsActivity;
import com.onemeter.activity.NewLeadEditActivity;
import com.onemeter.adapter.LeadStateAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.AllCustomerData;
import com.onemeter.entity.DData;
import com.onemeter.entity.StatusDDate;
import com.onemeter.net.NetUtil;
import com.onemeter.slideexpandable.ActionSlideExpandableListView;
import com.onemeter.slideexpandable.ActionSlideExpandableListView.OnActionClickListener;
import com.onemeter.test.MyAdapter;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.util.Utils;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelMain;

/**
 * 我的潜客模块
 * 
 * @author angelyin
 * @date 2015-10-13 下午1:33:08
 */
public class LeadsFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private ActionSlideExpandableListView lvShow = null;
	int y;
	private View view = null;
	private TextView tx_multiple_choice = null;
	// 状态
	private LinearLayout linearLayout_state = null;
	private ListView listView_pop_state = null;
	private ListView listView_pop_time = null;
	// 预约日期
	private LinearLayout linearLayout_yuyuedate = null;
	// 预约日期的不限布局
	private LinearLayout linearLayout_no = null;
	// 起始时间
	private TextView textView_startdate = null;
	// 结束时间
	private TextView textView_overdate = null;
	// 筛选
	private Button button_screening = null;
	// 未联系时间
	private LinearLayout linearLayout_nolxtime = null;
	@SuppressLint("SimpleDateFormat")
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	WheelMain wheelMain;
	/** 状态弹窗 **/
	private PopupWindow state_mPopupWindow;
	/** 未联系时间弹窗 **/
	private PopupWindow ontime_mPopupWindow;
	/** 刷选时间段弹窗 **/
	private PopupWindow time_mPopupWindow;
	private PopupWindow mPopupWindow;
	private ImageView img_search;
	private ImageView imageView_leads_triangle_down_one;
	private ImageView imageView_leads_triangle_down_two;
	private ImageView imageView_leads_triangle_down_three;
	private LeadStateAdapter state_adapter;
	private Dialog dialog;
	/** 加载进度条对话框 */
	private ProgressDialog prodialog;
	private PullToRefreshListView listview;
	private MyAdapter myAdapter;
	private Intent intent;
	StatusDDate mStatusDate;
	AllCustomerData mMyCustomerData;
	// 未联系弹窗
	LinearLayout list_f0;
	LinearLayout list_f1;
	LinearLayout list_f2;
	LinearLayout list_f3;
	LinearLayout list_f4;
	// 全条件收索组件
	private Button ima_jin;
	private Button ima_quan;
	private LinearLayout lead_linearLayout1;
	private LinearLayout quan_layout;
	private TextView btn_oldeDay;
	private TextView showtime_date;
	private TextView btn_newDay;
	/** 传入的时间 **/
	private String day;
	/** 搜索布局显示 **/
	private RelativeLayout rel_shousuo;
	/** 搜索时需要隐藏的布局 **/
	private RelativeLayout rel_layout;
	/** 搜索框 **/
	private static EditText editText1;
	/** 取消搜索 **/
	private Button bt_dimiss;
	boolean flag = true;
	private TextView tx_status;
	private TextView tx_no_contact_time;
	private ImageView imageView_f0;
	private ImageView imageView_f1;
	private ImageView imageView_f2;
	private ImageView imageView_f3;
	private ImageView imageView_f4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new Dialog(getActivity(), R.style.dialog);
		prodialog = new ProgressDialog(getActivity());// 进度条对话框
		prodialog.setMessage("加载中");
		prodialog.show();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leads_fragment_layout, container,
				false);
		initView();
		registerUpAllResultReceiver();
		registerMainResultReceiver();
		return view;
	}

	MainResultReceiver mainResultReceiver = new MainResultReceiver();

	private void registerMainResultReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("sateresult");
		getActivity().registerReceiver(mainResultReceiver, intentFilter);
	}

	private void relaseRegisterMainResultReceiver() {
		getActivity().unregisterReceiver(mainResultReceiver);
	}

	private class MainResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
//			if(intent!=null){
//				int type = intent.getIntExtra("tyype", 1);
//				switch (type) {
//				case 1:
//					flag=false;
//					break;
//
//				case 2:
//					
//					break;
//				}
//				
//			}			
			flag = false;
		}

	}

	/**
	 * 初始化数据
	 * 
	 * @author angelyin
	 * @date 2015-10-20 下午7:11:33
	 */
	private void initData() {
		getNetStatus();
		getNetAllCustomer();

	}

	@Override
	public void onResume() {
		super.onResume();
		if (!flag) {
			return;
		}
		rel_shousuo.setVisibility(View.GONE);
		rel_layout.setVisibility(View.VISIBLE);
		Intent intent1 = new Intent();
		intent1.setAction("downallresult");
		// 所有项目全部不选中
		IsShow(false);
		myAdapter.notifyDataSetChanged();
		tx_multiple_choice.setText("多选");
		intent1.putExtra("isshow", false);
		getActivity().sendBroadcast(intent1);
		ima_jin.setVisibility(View.VISIBLE);
		ima_quan.setVisibility(View.GONE);
		quan_layout.setVisibility(View.GONE);
		lead_linearLayout1.setVisibility(View.VISIBLE);
        
//		 getNetAllCustomer();
	}

	/**
	 * 获取所有潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-20 下午7:29:17
	 */
	private void getNetAllCustomer() {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerData");
		if (MyApplication.uuserDate.getLeixing().equals("校长")) {
			params.addBodyParameter("school_id",
					MyApplication.nCrDate.getSchool_id());

		} else if (MyApplication.uuserDate.getLeixing().equals("普通")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("customer_id",
					MyApplication.uuserDate.getUid());
		}
		params.addBodyParameter("pageSize", "100");
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_ALLMSG, LeadsFragment.this,
				"getCustomerData");
	}

	/**
	 * 获取潜客的状态类型
	 * 
	 * @author angelyin
	 * @date 2015-10-20 下午7:12:29
	 */
	private void getNetStatus() {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {// 判断网络是否可用
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getStatusData");
		params.addBodyParameter("school_id",
				MyApplication.nCrDate.getSchool_id());
		params.addBodyParameter("limit", "50");
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_TIANJIA_STATUS, LeadsFragment.this,
				"getStatusData");

	}

	private void initView() {
		bt_dimiss = (Button) view.findViewById(R.id.bt_dimiss);
		editText1 = (EditText) view.findViewById(R.id.editText1);
		rel_shousuo = (RelativeLayout) view.findViewById(R.id.rel_shousuo);
		rel_layout = (RelativeLayout) view.findViewById(R.id.rel_layout);
		btn_oldeDay = (TextView) view.findViewById(R.id.btn_oldeDay);
		btn_newDay = (TextView) view.findViewById(R.id.btn_newDay);
		showtime_date = (TextView) view.findViewById(R.id.showtime_date);
		showtime_date.setText(Utils.parseDate(dateFormat.format(date)));
		ima_jin = (Button) view.findViewById(R.id.ima_jin);
		ima_quan = (Button) view.findViewById(R.id.ima_quan);
		quan_layout = (LinearLayout) view.findViewById(R.id.quan_layout);
		lead_linearLayout1 = (LinearLayout) view
				.findViewById(R.id.linearLayout1);
		linearLayout_yuyuedate = (LinearLayout) view
				.findViewById(R.id.linearLayout_yuyuedate);
		linearLayout_state = (LinearLayout) view
				.findViewById(R.id.linearLayout_state);
		linearLayout_nolxtime = (LinearLayout) view
				.findViewById(R.id.linearLayout_nolxtime);
		tx_multiple_choice = (TextView) view
				.findViewById(R.id.tx_multiple_choice);
		tx_status = (TextView) view.findViewById(R.id.tx_status);
		tx_no_contact_time = (TextView) view
				.findViewById(R.id.tx_no_contact_time);
		btn_oldeDay.setOnClickListener(this);
		btn_newDay.setOnClickListener(this);
		linearLayout_yuyuedate.setOnClickListener(this);
		linearLayout_state.setOnClickListener(this);
		linearLayout_nolxtime.setOnClickListener(this);
		tx_multiple_choice.setOnClickListener(this);
		img_search = (ImageView) view.findViewById(R.id.img_search);

		img_search.setOnClickListener(this);
		ima_quan.setOnClickListener(this);
		ima_jin.setOnClickListener(this);
		bt_dimiss.setOnClickListener(this);

		// 搜索框监听
		editText1.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					// do something;
					rel_shousuo.setVisibility(View.GONE);
					rel_layout.setVisibility(View.VISIBLE);
					// xianshiKeyboard(view);
					// 根据搜索框内容发送请求
					getNetCustomerForSOSO();
					editText1.setText("");
					return true;

				}
				return false;
			}
		});

		imageView_leads_triangle_down_one = (ImageView) view
				.findViewById(R.id.imageView_leads_triangle_down_one);
		imageView_leads_triangle_down_two = (ImageView) view
				.findViewById(R.id.imageView_leads_triangle_down_two);
		imageView_leads_triangle_down_three = (ImageView) view
				.findViewById(R.id.imageView_leads_triangle_down_three);

		listview = (PullToRefreshListView) view.findViewById(R.id.list);

		myAdapter = new MyAdapter(getActivity());

		listview.setAdapter(myAdapter);
		//一级菜单点击
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (tx_multiple_choice.getText().toString().trim().equals("多选")) {
					MyApplication.IDmap.clear();
					MyApplication.IDmap.add(MyApplication.dataBean.get(position)
							.getId());
					intent = new Intent(getActivity(), LeadItemUpdateActivity.class);
					startActivity(intent);

				} else {
					if (MyApplication.dataBean.get(position).isIscheckedd()) {
						MyApplication.dataBean.get(position).setIscheckedd(false);
						myAdapter.isCheckMap.put(position, false);
					} else {
						myAdapter.isCheckMap.put(position, true);

						MyApplication.dataBean.get(position).setIscheckedd(true);
					}
					/*
					 * 将选择项加载到map里面寄存
					 */
					MyApplication.isCheckMap = myAdapter.isCheckMap;
					myAdapter.notifyDataSetChanged();
				}
				
			}
		});
		// 二级菜单 按钮下的点击事件
		listview.setItemActionListener(new OnActionClickListener() {

			@Override
			public void onClick(View listView, View Layoutview, int position) {
				// 传递数据
				MyApplication.IDmap.clear();
				MyApplication.IDmap.add(MyApplication.dataBean.get(position)
						.getId());

				if (Layoutview.getId() == R.id.linearLayout_Del) {// 失效按钮的点击事件
					getShiXiaoClick(position);

				} else if (Layoutview.getId() == R.id.linearLayout_Edit) {// 编辑按钮的点击事件

					Intent intent = new Intent(getActivity(),
							NewLeadEditActivity.class);
					startActivity(intent);
				} else if (Layoutview.getId() == R.id.linearLayout_Update) {// 详情按钮的点击事件
					Intent intent = new Intent(getActivity(),
							LeadsDetailsActivity.class);
					startActivity(intent);

				} else if (Layoutview.getId() == R.id.linearLayout_Call) {// 拨打电话按钮的点击事件

					String mobai = MyApplication.dataBean.get(position)
							.getMobile();
					getMobaiCall(mobai);

				}

			}

		}, R.id.linearLayout_Del, R.id.linearLayout_Edit,
				R.id.linearLayout_Update, R.id.linearLayout_Call);
		listview.setMode(Mode.BOTH);
		//刷新数据
		listview.setOnRefreshListener(new OnRefreshListener<ActionSlideExpandableListView>() {

		
			@Override
			public void onRefresh(PullToRefreshBase<ActionSlideExpandableListView> refreshView) {
				//设置刷新时间
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),  
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
				if (refreshView.isHeaderShown()) {
					//下拉刷新				
					//提示文字
					listview.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("下拉刷新");
					listview.getLoadingLayoutProxy(true, false).setPullLabel(" ");
					listview.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新..");
					listview.getLoadingLayoutProxy(true, false).setReleaseLabel("松开以刷新");
					new GetDataTask().execute();
					 
//					
				} else {
//					//上拉加载更多
				
					listview.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载更多");
					listview.getLoadingLayoutProxy(false, true).setPullLabel("");
					listview.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
					listview.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
					// 得到上一次滚动条的位置，让加载后的页面停在上一次的位置，便于用户操作
					y= MyApplication.dataBean.size(); 
					//开启异步加载任务
					new GetBottomDataTask().execute();
				}
				 
	                
			}
			
			
		});
		lvShow = listview.getRefreshableView();
	}
	//刷新数据
	 private class GetDataTask extends AsyncTask<Void, Void, List<String> >{

		@Override
		protected List<String> doInBackground(Void... arg0) {
			 try {  
	                Thread.sleep(1000);  
	            } catch (InterruptedException e) {  
	            }  
			return null;
		}
		
		@Override
		protected void onPostExecute(List<String> result) {
			getNetAllCustomer();
            myAdapter.notifyDataSetChanged();  
 	        listview.onRefreshComplete(); 
			super.onPostExecute(result);
		}
		 
	 }
	
	
	//异步加载更多
	  private class GetBottomDataTask extends AsyncTask<Void, Void, List<DData> >{

		@Override
		protected List<DData> doInBackground(Void... params) {
			 try {  
	                Thread.sleep(2000);  
	            } catch (InterruptedException e) {  
	            }  
			
			return MyApplication.dataBean ;
		}
		
		
		protected void onPostExecute(List<DData> result) {  
			  
	          myAdapter.notifyDataSetChanged();  
	          listview.onRefreshComplete(); 
	       // 设置滚动条的位置 -- 加载更多之后，滚动条的位置应该在上一次划到的位置
	          lvShow.setSelection(y);
	          super.onPostExecute(result);  
	      }  
	  }
	   
	
	  
	  
	/**
	 * 根据搜索框内容发送请求
	 * 
	 * @author angelyin
	 * @date 2015-10-28 下午2:50:47
	 */
	protected void getNetCustomerForSOSO() {
		String edst = editText1.getText().toString().trim();
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(edst);
		if ("".equals(edst)) {
			Utils.showToast(getActivity(), "请输入搜索内容");
			return;
		}

		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerData");

		if (MyApplication.uuserDate.getLeixing().equals("校长")) {
			params.addBodyParameter("school_id",
					MyApplication.nCrDate.getSchool_id());
		} else if (MyApplication.uuserDate.getLeixing().equals("普通")) {

			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("customer_id",
					MyApplication.uuserDate.getUid());
		}
		// 判断输入的是否为数字
		if (m.matches()) {
			params.addBodyParameter("mobile", edst);
		} else {
			params.addBodyParameter("stuname", edst);
		}
		params.addBodyParameter("pageSize", "100");
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_ALLMSG, LeadsFragment.this,
				"getCustomerData");
		//发送广播
		Intent intent = new Intent();
		intent.setAction("cancelresult");
		getActivity().sendBroadcast(intent);
		tx_multiple_choice.setText("多选");

	}

	/**
	 * 拨打电话
	 * 
	 * @author angelyin
	 * @date 2015-10-19 下午5:32:09
	 */
	protected void getMobaiCall(final String mobai) {
		final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.callphone_dialog_layout, null);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lParams = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		dialog.onWindowAttributesChanged(lParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Button button_ok = (Button) view.findViewById(R.id.button_ok);
		Button button_dimiss = (Button) view.findViewById(R.id.button_dimiss);
		button_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(getActivity(), Test.class);
				// startActivity(intent);
				// 拨打电话
				Utils.callFastPhone(getActivity(), mobai);
				dialog.dismiss();

			}
		});
		button_dimiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}

	/**
	 * 失效按钮的点击事件
	 * 
	 * @author angelyin
	 * @date 2015-10-19 下午5:29:44
	 */
	protected void getShiXiaoClick(final int position) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.lose_effcacy_dialog_layout, null);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.mypopwindow_anim_style);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		dialog.onWindowAttributesChanged(wl);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		Button bt_sx = (Button) view.findViewById(R.id.bt_sx);
		// 弹出框的失效按钮的点击事件
		bt_sx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 下方失效按钮点击事件
				String Customer_id = MyApplication.dataBean.get(position)
						.getId();
				getNetchangedStatus(Customer_id);
				dialog.dismiss();
			}
		});
		Button bt_qx = (Button) view.findViewById(R.id.bt_qx);
		bt_qx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

	}

	/**
	 * 提交请求更改当前潜客的状态为失效
	 * 
	 * @author angelyin
	 * @date 2015-10-27 上午9:56:57
	 * @param customer_id
	 */
	protected void getNetchangedStatus(String customer_id) {
		if (!MyApplication.isNetAvailable) {// 网络不可用
			Utils.showToast(getActivity(), "网络不可用，请打开网络");
			return;
		}
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "modifyCustomerStatusByStatusName");
		params.addBodyParameter("customer_id", customer_id);
		params.addBodyParameter("statusname", "失效");
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_UPDATE_SHIXIAO, LeadsFragment.this,
				"modifyCustomerStatusByStatusName");

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ima_quan:
			// 按钮全
			ima_jin.setVisibility(View.VISIBLE);
			ima_quan.setVisibility(View.GONE);
			quan_layout.setVisibility(View.GONE);
			lead_linearLayout1.setVisibility(View.VISIBLE);
			getNetAllCustomer();
			break;
		case R.id.ima_jin:
			// 按钮今
			ima_jin.setVisibility(View.GONE);
			ima_quan.setVisibility(View.VISIBLE);
			quan_layout.setVisibility(View.VISIBLE);
			lead_linearLayout1.setVisibility(View.GONE);
			MyApplication.dataBean.clear();
			myAdapter.notifyDataSetChanged();
			getNetCustomerforData();
			break;
		case R.id.btn_oldeDay:
			// 前一天
			try {
				MyApplication.dataBean.clear();
				myAdapter.notifyDataSetChanged();
				Date ss = Utils.getNextDay(dateFormat.parse(showtime_date
						.getText().toString()));
				// Utils.showToast(getActivity(), dateFormat.format(ss));
				showtime_date.setText(dateFormat.format(ss));
				getNetCustomerforData();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			break;
		case R.id.btn_newDay:
			// 后一天
			try {
				MyApplication.dataBean.clear();
				myAdapter.notifyDataSetChanged();
				Date ss = Utils.getAddDay(dateFormat.parse(showtime_date
						.getText().toString()));
				// Utils.showToast(getActivity(), dateFormat.format(ss));
				showtime_date.setText(dateFormat.format(ss));
				getNetCustomerforData();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			break;
		// 预约日期
		case R.id.linearLayout_yuyuedate:

			//当点击显示的时候

			if (tx_multiple_choice.getText().toString().trim().equals("多选")) {
				imageView_leads_triangle_down_two
						.setImageResource(R.drawable.icon_leads_triangle_up);
				showYuYuePopwindow(view);
			}
			break;

		case R.id.linearLayout_state:
			// 状态
			if (tx_multiple_choice.getText().toString().trim().equals("多选")) {
				imageView_leads_triangle_down_one
						.setImageResource(R.drawable.icon_leads_triangle_up);
				showStatePopwindow(view);
			}
			break;

		case R.id.linearLayout_nolxtime:
			// 未联系时间
			if (tx_multiple_choice.getText().toString().trim().equals("多选")) {
				imageView_leads_triangle_down_three
						.setImageResource(R.drawable.icon_leads_triangle_up);
				showNoTimePopwindow(view);
			}
			break;

		case R.id.img_search:
			// 搜索按钮
			// intent = new Intent(getActivity(), SearchLeadsActivity.class);
			// startActivity(intent);
			MyApplication.dataBean.clear();
			myAdapter.notifyDataSetChanged();
			rel_shousuo.setVisibility(View.VISIBLE);
			rel_layout.setVisibility(View.GONE);
			Intent intent2 = new Intent();
			intent2.setAction("mainresult");
			getActivity().sendBroadcast(intent2);
			// 获取焦点
			xianshiKeyboard();
			break;
		case R.id.bt_dimiss:
			// 取消搜索
			rel_shousuo.setVisibility(View.GONE);
			rel_layout.setVisibility(View.VISIBLE);
			getNetAllCustomer();
			HideKeyboard(view);
			Intent intent3 = new Intent();
			intent3.setAction("cancelresult");
			getActivity().sendBroadcast(intent3);
			 tx_multiple_choice.setText("全选");
			break;
		// 多选
		case R.id.tx_multiple_choice:
			Intent intent1 = new Intent();
			intent1.setAction("downallresult");
			/*
			 * 当点击显示的时候
			 */
			if (tx_multiple_choice.getText().toString().trim().equals("多选")) {
				// 所有项目全部选中
				IsShow(true);
				intent1.putExtra("isshow", true);
				myAdapter.notifyDataSetChanged();
				tx_multiple_choice.setText("取消");

			} else {
				// 所有项目全部不选中
				IsShow(false);
				myAdapter.notifyDataSetChanged();
				tx_multiple_choice.setText("多选");
				intent1.putExtra("isshow", false);
			}

			getActivity().sendBroadcast(intent1);
			break;
		case R.id.button_screening:
			// 筛选
			if (textView_startdate.length() == 0
					|| textView_overdate.length() == 0) {
				Toast.makeText(getActivity(), "请输入筛选时间", Toast.LENGTH_SHORT)
						.show();
			} else {
				getNetDayCustomer();
			}
			break;
		case R.id.list_f0:
			getNetAllCustomer();
			tx_no_contact_time.setText("未联系时间");
			tx_no_contact_time.setTextColor(Color.BLACK);
			ontime_mPopupWindow.dismiss();
			break;
		case R.id.list_f1:
			getNetNOtimeCustomer("1");
			tx_no_contact_time.setText("1周");
			tx_no_contact_time.setTextColor(Color.RED);
			ontime_mPopupWindow.dismiss();
			break;
		case R.id.list_f2:
			getNetNOtimeCustomer("2");
			ontime_mPopupWindow.dismiss();
			tx_no_contact_time.setText("2周");
			tx_no_contact_time.setTextColor(Color.RED);
			break;
		case R.id.list_f3:
			getNetNOtimeCustomer("3");
			ontime_mPopupWindow.dismiss();
			tx_no_contact_time.setText("3周");
			tx_no_contact_time.setTextColor(Color.RED);
			break;
		case R.id.list_f4:
			getNetNOtimeCustomer("4");
			ontime_mPopupWindow.dismiss();
			tx_no_contact_time.setText("3周以上");
			tx_no_contact_time.setTextColor(Color.RED);
			break;
		}
	}

	/**
	 * //根据今天日期发送请求获取数据
	 * 
	 * @author angelyin
	 * @date 2015-10-26 下午4:29:22
	 */
	private void getNetCustomerforData() {

		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerData");
		if (MyApplication.uuserDate.getLeixing().equals("普通")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("customer_id",
					MyApplication.uuserDate.getUid());
			params.addBodyParameter("day", showtime_date.getText().toString());

		} else if (MyApplication.uuserDate.getLeixing().equals("校长")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("day", showtime_date.getText().toString());
		}
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_ALLMSG, LeadsFragment.this,
				"getCustomerData");

	}

	/**
	 * 根据时间段筛选潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-22 下午5:20:57
	 */
	private void getNetDayCustomer() {
		time_mPopupWindow.dismiss();
		MyApplication.dataBean.clear();
		myAdapter.notifyDataSetChanged();
		// 筛选
		String startDay = textView_startdate.getText().toString().trim();
		String endDay = textView_overdate.getText().toString().trim();
		// // //转化时间格式
		String yuyue = null;
		String str1 = startDay.substring(0, 4);
		String str2 = startDay.substring(5, 7);
		String str3 = startDay.substring(8, 10);
		String str4 = endDay.substring(0, 4);
		String str5 = endDay.substring(5, 7);
		String str6 = endDay.substring(8, 10);
		yuyue = str1 + "/" + str2 + "/" + str3 + "-" + str4 + "/" + str5 + "/"
				+ str6;

		Utils.showToast(getActivity(), yuyue);
		// 判断两个时间段的前后
		if (startDay == null || endDay == null) {
			Utils.showToast(getActivity(), "时间段不能为空");
			return;
		} else {
			// 判断网络是否连接
			if (!MyApplication.isNetAvailable) {
				Utils.showToast(
						getActivity(),
						getResources().getString(
								R.string.msg_sys_net_unavailable));
				return;
			}
			prodialog.show();
			RequestParams params = new RequestParams();
			params.addBodyParameter("action", "getCustomerData");
			if (MyApplication.uuserDate.getLeixing().equals("普通")) {
				params.addBodyParameter("school_id",
						MyApplication.uuserDate.getSchool());
				params.addBodyParameter("customer_id",
						MyApplication.uuserDate.getUid());
				params.addBodyParameter("yuyue", yuyue);

			} else if (MyApplication.uuserDate.getLeixing().equals("校长")) {
				params.addBodyParameter("school_id",
						MyApplication.uuserDate.getSchool());
				params.addBodyParameter("yuyue", yuyue);
			}
			params.addBodyParameter("o", "json");
			new NetUtil(getActivity()).sendPostToServer(params,
					Constants.API_CUSTOMER_ALLMSG, LeadsFragment.this,
					"getCustomerData");
		}
	}

	/**
	 * 根据为联系时间获取潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-22 下午4:37:39
	 */
	private void getNetNOtimeCustomer(String wktime) {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerData");
		if (MyApplication.uuserDate.getLeixing().equals("普通")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("customer_id",
					MyApplication.uuserDate.getUid());
			params.addBodyParameter("wktime", wktime);

		} else if (MyApplication.uuserDate.getLeixing().equals("校长")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("wktime", wktime);
		}
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_ALLMSG, LeadsFragment.this,
				"getCustomerData");

	}

	public void IsShow(Boolean show) {
		// 获取当前的数据数量
		int count = myAdapter.getCount();
		// 进行遍历
		for (int i = 0; i < count; i++) {
			// 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
			int position = i - (count - myAdapter.getCount());
			DData bean = (DData) myAdapter.getItem(position);
			bean.setCanRemove(show);
			bean.setIscheckedd(!show);
		}
		myAdapter.notifyDataSetChanged();
	}

	UpAllResultReceiver upAllResultReceiver = new UpAllResultReceiver();

	private void registerUpAllResultReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("upallresult");
		getActivity().registerReceiver(upAllResultReceiver, intentFilter);
	}

	private void relaseRegisterUpAllResultReceiver() {
		getActivity().unregisterReceiver(upAllResultReceiver);
	}

	/**
	 * 广播
	 * 
	 * @author angelyin
	 * @date 2015-10-27 下午2:26:13
	 */
	private class UpAllResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				int type = intent.getIntExtra("type", 1);
				switch (type) {
				case 1:
					Boolean ischecked = intent.getBooleanExtra("ischecked",
							false);
					if (ischecked) {
						// 所有项目全部选中
						List<DData> daList = new ArrayList<DData>();
						List<DData> ddate = mMyCustomerData.getBody().getCustomerData
								.getData();
						for (int i = 0; i < ddate.size(); i++) {
							DData date = ddate.get(i);
							date.setIscheckedd(true);
							daList.add(date);
							myAdapter.notifyDataSetChanged();
						}
						MyApplication.dataBean = ddate;
						myAdapter.notifyDataSetChanged();
					} else {
						// 所有项目全部不选中

						List<DData> daList = new ArrayList<DData>();
						List<DData> ddate = mMyCustomerData.getBody().getCustomerData
								.getData();
						for (int i = 0; i < ddate.size(); i++) {
							DData date = ddate.get(i);
							date.setIscheckedd(false);
							daList.add(date);
							myAdapter.notifyDataSetChanged();
						}
						MyApplication.dataBean = ddate;
						myAdapter.notifyDataSetChanged();
					}
					break;
				case 2:
					// 批量失效
					MyApplication.checkedID.clear();
					boolean potion = false;
					for (int i = 0; i < MyApplication.isCheckMap.size(); i++) {
						potion = MyApplication.isCheckMap.get(i).booleanValue();
						if (potion) {
							String Customer_id = MyApplication.dataBean.get(i)
									.getId();
							// Log.i("ttre","Customer_id:"+Customer_id);
							MyApplication.checkedID.add(Customer_id);
						}
					}
					if (MyApplication.checkedID.size() == 0) {
						Utils.showToast(getActivity(), "请选择潜客");
						return;
					} else {
						getNetPiLiangUpdateShiXiao();
					}
					break;
				case 3:
					// 批量更新
					MyApplication.checkedID.clear();
					MyApplication.checkedName.clear();
					boolean potion1 = false;
					for (int i = 0; i < MyApplication.isCheckMap.size(); i++) {
						potion1 = MyApplication.isCheckMap.get(i)
								.booleanValue();
						if (potion1) {
							String Customer_id = MyApplication.dataBean.get(i).getId();
							String Customer_Name=MyApplication.dataBean.get(i).getStudent_name();
							// Log.i("ttre","Customer_id:"+Customer_id);
							MyApplication.checkedID.add(Customer_id);
							MyApplication.checkedName.add(Customer_Name);
						}
					}
					if (MyApplication.checkedID.size() == 0) {
						Utils.showToast(getActivity(), "请选择潜客");
						return;
					} else {
						// 跳转
						intent = new Intent(getActivity(),
								LeadNewItemUpdateActivity.class);
						startActivity(intent);
//						getActivity().finish();
					}
					break;
				default:
					break;
				}

			}
		}
	}

	/*
	 * 状态的popupWindow的点击事件
	 */
	private void showStatePopwindow(View view) {
		View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.popwindow_state, null);

		state_mPopupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		state_mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				imageView_leads_triangle_down_one
						.setImageResource(R.drawable.icon_leads_triangle_down);
				return false;
			}
		});
		state_mPopupWindow.setFocusable(true);
		state_mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.white));
		state_mPopupWindow.showAsDropDown(view);
		state_mPopupWindow.update();
		listView_pop_state = (ListView) contentView
				.findViewById(R.id.listView_pop_state);
		state_adapter = new LeadStateAdapter(getActivity(),
				MyApplication.gstlist, 0);
		listView_pop_state.setAdapter(state_adapter);
		// listView上的每一项Item的点击事件
		listView_pop_state.setOnItemClickListener(this);
		LinearLayout linearLayout_unlimited = (LinearLayout) contentView
				.findViewById(R.id.linearLayout_unlimited);
		linearLayout_unlimited.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getNetAllCustomer();
				tx_status.setTextColor(Color.BLACK);
				tx_status.setText("状态");
				state_mPopupWindow.dismiss();
			}
		});

	}

	/**
	 * 批量失效弹窗
	 * 
	 * @author angelyin
	 * @date 2015-10-27 下午3:59:05
	 */
	public void getNetPiLiangUpdateShiXiao() {

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.lose_effcacy_dialog_layout, null);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.mypopwindow_anim_style);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		dialog.onWindowAttributesChanged(wl);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		Button bt_sx = (Button) view.findViewById(R.id.bt_sx);
		// 弹出框的失效按钮的点击事件
		bt_sx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getNetPiliangCustomer();
				dialog.dismiss();
			}
		});
		Button bt_qx = (Button) view.findViewById(R.id.bt_qx);
		bt_qx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

	}

	/**
	 * 批量失效弹窗
	 * 
	 * @author angelyin
	 * @date 2015-10-27 下午4:06:33
	 */
	protected void getNetPiliangCustomer() {
		String cusID = "";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < MyApplication.checkedID.size(); i++) {
			cusID = MyApplication.checkedID.get(i);
			sb.append(cusID + ",");
		}
		cusID = sb.toString();
		cusID = cusID.substring(0, cusID.length() - 1);
		if (!MyApplication.isNetAvailable) {// 网络不可用
			Utils.showToast(getActivity(), "网络不可用，请打开网络");
			return;
		}
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "modifyCustomerStatusByStatusName");
		params.addBodyParameter("customer_id", cusID);
		params.addBodyParameter("statusname", "失效");
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_UPDATE_SHIXIAO, LeadsFragment.this,
				"modifyCustomerStatusByStatusName");

	}

	/*
	 * 未联系时间的popupWindow的点击事件
	 */
	private void showNoTimePopwindow(View view) {
		View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.popwindow_time_layout, null);
		ontime_mPopupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		ontime_mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				imageView_leads_triangle_down_three
						.setImageResource(R.drawable.icon_leads_triangle_down);
				return false;
			}
		});
		ontime_mPopupWindow.setFocusable(true);
		ontime_mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.white));
		ontime_mPopupWindow.showAsDropDown(view);
		ontime_mPopupWindow.update();
		list_f0 = (LinearLayout) contentView.findViewById(R.id.list_f0);
		list_f1 = (LinearLayout) contentView.findViewById(R.id.list_f1);
		list_f2 = (LinearLayout) contentView.findViewById(R.id.list_f2);
		list_f3 = (LinearLayout) contentView.findViewById(R.id.list_f3);
		list_f4 = (LinearLayout) contentView.findViewById(R.id.list_f4);
		imageView_f0 = (ImageView) contentView.findViewById(R.id.imageView_f0);
		imageView_f1 = (ImageView) contentView.findViewById(R.id.imageView_f1);
		imageView_f2 = (ImageView) contentView.findViewById(R.id.imageView_f2);
		imageView_f3 = (ImageView) contentView.findViewById(R.id.imageView_f3);
		imageView_f4 = (ImageView) contentView.findViewById(R.id.imageView_f4);
		list_f0.setOnClickListener(this);
		list_f1.setOnClickListener(this);
		list_f2.setOnClickListener(this);
		list_f3.setOnClickListener(this);
		list_f4.setOnClickListener(this);

	}

	/*
	 * 预约日期的popupWindow的点击事件
	 */
	private void showYuYuePopwindow(View view) {
		View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.popwindow_yuyuedate, null);
		time_mPopupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		time_mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				imageView_leads_triangle_down_two
						.setImageResource(R.drawable.icon_leads_triangle_down);
				return false;
			}
		});
		time_mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.white));
		time_mPopupWindow.showAsDropDown(view);
		time_mPopupWindow.update();

		// 预约日期的不限布局的点击事件
		linearLayout_no = (LinearLayout) contentView
				.findViewById(R.id.linearLayout_no);
		linearLayout_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getNetAllCustomer();
				time_mPopupWindow.dismiss();
			}
		});
		// 开始时间选择
		textView_startdate = (TextView) contentView
				.findViewById(R.id.textView_startdate);
		textView_startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 弹出时间选择器
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View layoutView = inflater.inflate(R.layout.timepicker, null);
				TextView cancel = (TextView) layoutView
						.findViewById(R.id.cancel);
				TextView ok = (TextView) layoutView.findViewById(R.id.ok);
				ScreenInfo screenInfo = new ScreenInfo(getActivity());
				wheelMain = new WheelMain(layoutView);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = textView_startdate.getText().toString();
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
				wheelMain.initDateTimePicker(year, 0 + month, day);

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						textView_startdate.setText(Utils.parseDate(wheelMain
								.getTime()));
						Utils.showToast(getActivity(),
								Utils.parseDate(wheelMain.getTime()));
						mPopupWindow.dismiss();
					}
				});
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						mPopupWindow.dismiss();
					}
				});
				mPopupWindow = new PopupWindow(layoutView,
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
						true);
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.setTouchable(true);
				mPopupWindow.setFocusable(true);
				mPopupWindow.update();
				mPopupWindow.showAtLocation(layoutView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);

				layoutView = null;
				cancel = null;

			}
		});
		// 结束时间选择
		textView_overdate = (TextView) contentView
				.findViewById(R.id.textView_overdate);
		textView_overdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View layoutView = inflater.inflate(R.layout.timepicker, null);
				TextView cancel = (TextView) layoutView
						.findViewById(R.id.cancel);
				TextView ok = (TextView) layoutView.findViewById(R.id.ok);
				ScreenInfo screenInfo = new ScreenInfo(getActivity());
				wheelMain = new WheelMain(layoutView);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = textView_overdate.getText().toString();
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
						textView_overdate.setText(Utils.parseDate(wheelMain
								.getTime()));
						mPopupWindow.dismiss();
					}
				});
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						mPopupWindow.dismiss();
					}
				});
				mPopupWindow = new PopupWindow(layoutView,
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
						true);
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
		});
		// 筛选按钮的点击事件
		button_screening = (Button) contentView
				.findViewById(R.id.button_screening);
		button_screening.setOnClickListener(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		relaseRegisterUpAllResultReceiver();
		relaseRegisterMainResultReceiver();
	}

	public void onCompleted(String result, boolean isSuccess, String api,
			String action) {
		if (prodialog != null && prodialog.isShowing()) {
			prodialog.dismiss();
			Log.i("Onemeter", "关闭prodialog");
		}
		if (!isSuccess) {// 请求不成功
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_request_error));
			return;
		}
		Status status = StatusUtil.handleStatus(getActivity(), result);
		/******************************************************************************************/
		if (api.equals(Constants.API_CUSTOMER_ALLMSG)
				&& action.equals("getStatusData")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
				mStatusDate = (StatusDDate) GsonUtil.convertJson2Object(result,
						StatusDDate.class, GsonUtil.JSON_JAVABEAN);

				MyApplication.gstlist = mStatusDate.getBody()
						.getGetStatusData();

			}

			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(getActivity(), "参数错误");
					return;
				}

			}

		}
		/***************************************************************************************/
		if (api.equals(Constants.API_CUSTOMER_ALLMSG)
				&& action.equals("getCustomerData")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
			// Utils.showToast(getActivity(), "我的潜客请求成功");
				String count = null;
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject2 = jsonObject.getJSONObject("body");
					JSONObject jsonObject3 = jsonObject2
							.getJSONObject("getCustomerData");
					count = jsonObject3.getString("count");

				} catch (JSONException e) {

					e.printStackTrace();
				}
				if (count.equals("0")) {

					return;
				} else {
					mMyCustomerData = (AllCustomerData) GsonUtil
							.convertJson2Object(result, AllCustomerData.class,
									GsonUtil.JSON_JAVABEAN);
					/**
					 * 全部的潜客信息
					 */
					List<DData> daList = new ArrayList<DData>();
					List<DData> ddate = mMyCustomerData.getBody().getCustomerData
							.getData();
					for (int i = 0; i < ddate.size(); i++) {
						DData date = ddate.get(i);
						date.setIscheckedd(false);
						daList.add(date);
					}
					MyApplication.dataBean = ddate;
					myAdapter.notifyDataSetChanged();
				}

			}
			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(getActivity(), "参数错误");
					return;
				}

			}

		}

		if (api.equals(Constants.API_CUSTOMER_UPDATE_SHIXIAO)
				&& action.equals("modifyCustomerStatusByStatusName")) {
			if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
				Utils.showToast(getActivity(), "修改成功");
				Intent intent1 = new Intent();
				intent1.setAction("downallresult");
				// 所有项目全部不选中
				IsShow(false);
				myAdapter.notifyDataSetChanged();
				tx_multiple_choice.setText("多选");
				intent1.putExtra("isshow", false);
				getActivity().sendBroadcast(intent1);
				getNetAllCustomer();
			}

			if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败，处理状态码
				Integer statusCode = StatusUtil.parseStatus(result);
				if (statusCode == null)
					return;
				if (statusCode == 1) {
					Utils.showToast(getActivity(), "参数错误");
					return;
				}
				if (statusCode == 3) {
					Utils.showToast(getActivity(), "修改失败");
					return;
				}
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if (parent == listView_pop_state) {// 状态类型
			state_adapter.setSelectedPosition(position);
			state_adapter.notifyDataSetChanged();
			MyApplication.dataBean.clear();
			myAdapter.notifyDataSetChanged();
			getNetstateCustomer(MyApplication.gstlist.get(position).getId());
			MyApplication.gstlist.get(position).getStatus();
			tx_status.setText(MyApplication.gstlist.get(position).getStatus());
			tx_status.setTextColor(Color.RED);
			state_mPopupWindow.dismiss();

		}
//
//		if (parent == listview) {// 二级菜单一级item点击
//			
//
//		}
	}

	/**
	 * 根据类型获取潜客信息
	 * 
	 * @author angelyin
	 * @date 2015-10-21 下午7:13:39
	 */
	private void getNetstateCustomer(String Status_id) {
		// 判断网络是否连接
		if (!MyApplication.isNetAvailable) {
			Utils.showToast(getActivity(),
					getResources().getString(R.string.msg_sys_net_unavailable));
			return;
		}
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerData");
		if (MyApplication.uuserDate.getLeixing().equals("普通")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("customer_id",
					MyApplication.uuserDate.getUid());
			params.addBodyParameter("status", Status_id);

		} else if (MyApplication.uuserDate.getLeixing().equals("校长")) {
			params.addBodyParameter("school_id",
					MyApplication.uuserDate.getSchool());
			params.addBodyParameter("status", Status_id);
		}
		params.addBodyParameter("o", "json");
		new NetUtil(getActivity()).sendPostToServer(params,
				Constants.API_CUSTOMER_ALLMSG, LeadsFragment.this,
				"getCustomerData");

	}

	/**
	 * 控制软键盘隐藏
	 * 
	 * @param token
	 */
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		}
	}

	/**
	 * 显示 软键盘
	 * 
	 * @param v
	 */
	public static void xianshiKeyboard() {
		editText1.setFocusable(true);
		editText1.setFocusableInTouchMode(true);
		editText1.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) editText1
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText1, 0);

	}

}
