package com.onemeter.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.CustomerConversionData;
import com.onemeter.entity.CustomerConversionData.BBody.GgetCustomerConversionData.data;
import com.onemeter.net.NetUtil;
import com.onemeter.util.Constants;
import com.onemeter.util.GsonUtil;
import com.onemeter.util.StatusUtil;
import com.onemeter.util.StatusUtil.Status;
import com.onemeter.util.Utils;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelBirthday;
import com.onemeter.wiget.WheelMain;

/**
 * 统计模块
 * 
 * @author angelyin
 * @date 2015-10-13 下午1:33:27
 */

public class StatisticFragment extends Fragment implements OnClickListener,
		OnChartValueSelectedListener {
	private View view = null;
	/** 时间日期 **/
	private TextView textView_date = null;
	/** 点击打开日历选择 **/
	private LinearLayout layout_date;
	WheelBirthday wheelMain;
	@SuppressLint("SimpleDateFormat")
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private PopupWindow mPopupWindow;
	/** 进度条弹框 **/
	ProgressDialog prodialog;// 加载进度条对话框
	NetUtil netUtil;
	CustomerConversionData mCustomerConversionData;
	/** 填充潜客的集合 **/
	private List<data> datalist;
	/** 潜客的数量 **/
	String count = null;
	// 环形图表相关
	private PieChart mChart;
	private RelativeLayout pieChart_rel;
	/** 百分比显示数据 **/
	private String[] mParties;
	/** 区域颜色选择 **/
	public static final int[] VORDIPLOM_COLORS = { Color.rgb(192, 255, 140),
			Color.rgb(255, 247, 140), Color.rgb(0, 0, 128),
			Color.rgb(140, 234, 255), Color.rgb(118, 174, 175),
			Color.rgb(193, 37, 82), Color.rgb(255, 102, 10),
			Color.rgb(245, 199, 300), Color.rgb(106, 150, 31),
			Color.rgb(179, 100, 53) };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.statistics_fragment_layout, container,
				false);
		initView();
		getCustomerConversionData();
		return view;
	}

	/**
	 * 初始化组件
	 * 
	 * @author angelyin
	 * @date 2015-10-17 下午7:13:21
	 */
	private void initView() {
		textView_date = (TextView) view.findViewById(R.id.textView_date);
		layout_date = (LinearLayout) view.findViewById(R.id.layout_date);
		pieChart_rel = (RelativeLayout) view.findViewById(R.id.pieChart_rel);

		Calendar calendar = Calendar.getInstance();
		textView_date.setText(calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "");
		prodialog = new ProgressDialog(getActivity());// 进度条对话框
		netUtil = new NetUtil(getActivity());
		layout_date.setOnClickListener(this);

	}

	/**
	 * 初始化图表
	 * 
	 * @author angelyin
	 * @date 2015-10-17 下午6:19:19
	 */
	private void initPieChart() {
		mChart = (PieChart) view.findViewById(R.id.chart1);
		// 设置中心孔的颜色
		mChart.setHoleColor(Color.rgb(235, 235, 235));
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				"OpenSans-Regular.ttf");
		// 设置字体的风格值
		mChart.setValueTypeface(tf);
		// 设置中心字体的风格值
		mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "OpenSans-Light.ttf"));
		// 设置在最大半径的百分比饼图中心孔半径（最大=整个图的半径），默认为50%
		mChart.setHoleRadius(70f);
		// 设置一个描述文本显示在图表的右下角，大小为y-legend文本大小
		mChart.setDescription("");
		// 设置为true 为图表设置Y值
		mChart.setDrawYValues(true);
		// 将此设置为true，以绘制在饼图中心显示的文本
		mChart.setDrawCenterText(true);
		// 设置为true 图形中心为空，显示环形
		mChart.setDrawHoleEnabled(true);
		// 设置旋转偏移
		mChart.setRotationAngle(0);
		// 设置为true 百分比区域显示相应描述值
		mChart.setDrawXValues(false);

		// 设置为true 图表可以触摸使他旋转
		mChart.setRotationEnabled(true);

		// 设置为true 可以显示百分比的值
		mChart.setUsePercentValues(true);
		// 设置在图表中的值旁边的单位，例如%
		// mChart.setUnit(" %");
		// 设置为true 则单位被绘制在图表中的值旁边
		// mChart.setDrawUnitsInChart(true);
		// 添加一个选择监听器
		mChart.setOnChartValueSelectedListener(this);
		// 设置为false 则禁止手势触摸
		mChart.setTouchEnabled(false);
		// 设置中心圈文本内容

		mChart.setCenterText("总潜客:" + count);
		// 设置中心文本字体大小
		mChart.setCenterTextSize(20);
		setData(datalist.size(), 100);

		mChart.animateXY(1500, 1500);
		mChart.spin(2000, 0, 360);
		// 返回图表的对象 ，自方法可以用于自定义生成的图表
		Legend l = mChart.getLegend();
		// // 将对象的位置设置为整个图表的摸个位置
		if (datalist.size() > 6) {
			l.setPosition(LegendPosition.RIGHT_OF_CHART);
		} else {
			l.setPosition(LegendPosition.BELOW_CHART_CENTER);
		}
		// //将X轴的区域像素转换为dp
		l.setXEntrySpace(7f);
		// //将y轴的区域像素转换为dp
		l.setYEntrySpace(5f);

	}

	private void setData(int count, float range) {

		float mult = range;

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		for (int i = 0; i < count; i++) {
			yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++)
			xVals.add(mParties[i % mParties.length]);

		PieDataSet set1 = new PieDataSet(yVals1, null);
		// 设置百分比区域间的空隙
		set1.setSliceSpace(5f);
		// 设置不同百分比区域颜色
		set1.setColors(VORDIPLOM_COLORS);
		// set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
		PieData data = new PieData(xVals, set1);
		// 为图表设置一个新的数据对象。数据对象包含显示所需的所有值和信息
		mChart.setData(data);
		// undo all highlights
		mChart.highlightValues(null);
		mChart.invalidate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_date:
			// 日历选择
			getDate();

			break;

		default:
			break;
		}

	}

	/**
	 * 弹出日历选择器
	 * 
	 * @author angelyin
	 * @date 2015-10-14 下午10:55:50
	 */
	private void getDate() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View layoutView = inflater.inflate(R.layout.timepicker, null);
		TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
		TextView ok = (TextView) layoutView.findViewById(R.id.ok);
		ScreenInfo screenInfo = new ScreenInfo(getActivity());
		wheelMain = new WheelBirthday(layoutView,null);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = textView_date.getText().toString();
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
				textView_date.setText(wheelMain.getTime());
				getCustomerConversionData();
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
	 * 获取潜客转化率数据
	 * 
	 * @author angelyin
	 * @date 2015-10-16 下午4:36:08
	 */
	private void getCustomerConversionData() {
		if (!MyApplication.isNetAvailable) {// 网络不可用
			Utils.showToast(getActivity(), "网络不可用，请打开网络");
			return;
		}
		prodialog.setMessage("加载中");
		prodialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", "getCustomerConversionData");
		params.addBodyParameter("school_id",
				MyApplication.uuserDate.getSchool());
		params.addBodyParameter("day", textView_date.getText().toString());
		params.addBodyParameter("user_id", MyApplication.uuserDate.getUid());
		params.addBodyParameter("o", "json");
		netUtil.sendPostToServer(params, Constants.API_CUSTOMER_CONVERSION,
				StatisticFragment.this, "getCustomerConversionData");

	}

	public void onCompleted(String result, boolean isSuccess, String api) {

		if (prodialog != null && prodialog.isShowing()) {
			prodialog.dismiss();
			Log.i("Onemeter", "关闭prodialog");
		}
		if (!isSuccess) {// 请求不成功
			Toast.makeText(getActivity(),
					getResources().getString(R.string.msg_request_error),
					Toast.LENGTH_SHORT).show();
			return;
		}
		Status status = StatusUtil.handleStatus(getActivity(), result);
		/**************************************************************************************************/

		if (status.equals(Status.STATUS_SUCCESS)) {// 请求成功
			String state = null;
			Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
			JSONObject jsonObject;
			JSONObject jsonObject1;
			JSONObject jsonObject2;

			try {
				jsonObject = new JSONObject(result);
				jsonObject1 = jsonObject.getJSONObject("body");
				jsonObject2 = jsonObject1
						.getJSONObject("getCustomerConversionData");
				count = jsonObject2.getString("count");
				JSONArray jsonArray = jsonObject2.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					state = jsonArray.getJSONObject(i).getString("status");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (count.equals("0") || state == null) {// 没有数据
				pieChart_rel.setVisibility(4);
				Toast.makeText(getActivity(),
						textView_date.getText().toString() + "没有细分统计数据",
						Toast.LENGTH_SHORT).show();
				return;
			} else {// 有数据
				pieChart_rel.setVisibility(0);
				datalist = new ArrayList<data>();

				mCustomerConversionData = (CustomerConversionData) GsonUtil
						.convertJson2Object(result,
								CustomerConversionData.class,
								GsonUtil.JSON_JAVABEAN);
				datalist = mCustomerConversionData.getBody()
						.getGetCustomerConversionData().getData();
				mParties = new String[datalist.size()];
				for (int i = 0; i < datalist.size(); i++) {
					mParties[i] = datalist.get(i).getStatus().toString();
				}
				initPieChart();
				mChart.setVisibility(view.VISIBLE);
			}

		}
		if (status.equals(Status.STATUS_UNHANDLE)) {// 请求失败
			Integer statusCode = StatusUtil.parseStatus(result);
			if (statusCode == null)
				return;
			if (statusCode == 1) {
				Toast.makeText(getActivity(), "参数错误", Toast.LENGTH_SHORT)
						.show();
				return;
			}

		}
	}

	/**
	 * 将获取到的数据转化为树状图百分比显示
	 * 
	 * @author angelyin
	 * @date 2015-10-17 下午3:03:17
	 */
	private void getShow() {

	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		if (e == null)
			return;

	}

	@Override
	public void onNothingSelected() {

	}

}
