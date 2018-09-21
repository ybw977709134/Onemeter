package com.onemeter.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.activity.NewLeadClassActivity;
import com.onemeter.activity.NewLeadContactActivity;
import com.onemeter.activity.NewLeadSexActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.wiget.JudgeDate;
import com.onemeter.wiget.ScreenInfo;
import com.onemeter.wiget.WheelBirthday;
import com.onemeter.wiget.WheelMain;

/**
 * 新增潜客模块
 * 
 * @author angelyin
 * @date 2015-10-13 下午1:32:37
 */
public class NewleadFragment extends Fragment implements OnClickListener {
	/** 学生信息输入框 **/
	private EditText editText_student_name;
	/** 感兴趣的课程 **/
	private EditText editText_like_class;
	/** 选择性别 **/
	private TextView newlead_sex_text;
	/** 生日 **/
	private TextView textView_birthday;
	/** 年级 **/
	private TextView newlead_year;
	/** 性别选项布局 **/
	private LinearLayout linearLayout_sex;
	/** 年级选项布局 **/
	private LinearLayout linearLayout_class;
	/** 生日选项布局 **/
	private LinearLayout linearLayout_birthday;
	/** 下一步按钮 **/
	private Button button_next;
	/** 学生信息 **/
	private TextView textView_studengtmessage;
	/** 联系方式 **/
	private TextView newlead_touch;
	private Intent intent;
	private View view = null;
	// 内容字段声明
	private String studentName;
	private String course;
	private String sex;
	private String birthday;
	private String cclass;
	String result_id;
	@SuppressLint("SimpleDateFormat")
	/**时间格式选择**/
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	WheelBirthday wheelMain;
	/** 弹窗 **/
	private PopupWindow mPopupWindow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.newlead_fragment_layout, container,
				false);
		initView();

		return view;
	}

	private void initView() {
		linearLayout_sex = (LinearLayout) view
				.findViewById(R.id.linearLayout_sex);
		linearLayout_class = (LinearLayout) view
				.findViewById(R.id.linearLayout_class);
		linearLayout_birthday = (LinearLayout) view
				.findViewById(R.id.linearLayout_birthday);
		button_next = (Button) view.findViewById(R.id.button_next);
		textView_birthday = (TextView) view
				.findViewById(R.id.textView_birthday);
		textView_studengtmessage = (TextView) view
				.findViewById(R.id.textView_studengtmessage);
		newlead_touch = (TextView) view.findViewById(R.id.newlead_touch);

		editText_student_name = (EditText) view
				.findViewById(R.id.editText_student_name);
		editText_like_class = (EditText) view
				.findViewById(R.id.editText_like_class);
		newlead_sex_text = (TextView) view.findViewById(R.id.newlead_sex_text);
		newlead_year = (TextView) view.findViewById(R.id.newlead_year);
		// 清除
		editText_student_name.setText("");
		editText_like_class.setText("");

		textView_studengtmessage.setOnClickListener(this);
		newlead_touch.setOnClickListener(this);
		linearLayout_sex.setOnClickListener(this);
		linearLayout_birthday.setOnClickListener(this);
		linearLayout_class.setOnClickListener(this);
		button_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearLayout_sex:
			// 跳转到选择性别页面
			intent = new Intent(getActivity(), NewLeadSexActivity.class);
			intent.putExtra("requestCode", "1");
			startActivityForResult(intent, 101);
			break;
		case R.id.linearLayout_class:
			// 跳转到选择年级页面
			intent = new Intent(getActivity(), NewLeadClassActivity.class);
			intent.putExtra("requestCode", "2");
			startActivityForResult(intent, 202);
			break;
		case R.id.button_next:
			getStudentMesg();
			break;
		case R.id.linearLayout_birthday:
			popupWindow();
			break;
		case R.id.newlead_touch:
			// 联系方式
			getStudentMesg();

			break;
		}
	}

	//
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 判断回传值
		if (requestCode == 101) {
			if (resultCode == 1) {
				String result = data.getStringExtra("one");
				result_id = data.getStringExtra("one_id");
				newlead_sex_text.setText(result);
			}
		}
		if (requestCode == 202) {
			if (resultCode == 2) {
				String result = data.getStringExtra("second");
				newlead_year.setText(result);
			}
		}
	}

	/**
	 * 判断并获取获取输入框内容
	 * 
	 * @author angelyin
	 * @date 2015-10-13 下午3:00:26
	 */
	private void getStudentMesg() {
		studentName = editText_student_name.getText().toString().trim();
		course = editText_like_class.getText().toString().trim();
		if (studentName.length() == 0) {
			Toast.makeText(getActivity(), "请输入学生姓名", Toast.LENGTH_SHORT).show();
			return;
//		} else if (newlead_sex_text.getText().toString().equals("")) {
//			Toast.makeText(getActivity(), "请选择性别", Toast.LENGTH_SHORT).show();
//			return;
		}else if (textView_birthday.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "请选择生日", Toast.LENGTH_SHORT).show();
				return;
//		} else if (newlead_year.getText().toString().equals("")) {
//			Toast.makeText(getActivity(), "请选择年级", Toast.LENGTH_SHORT).show();
//			return;
		}
		// 学生姓名
		MyApplication.nCrDate.setStudentName(studentName);
		MyApplication.nCrDate.setKecheng(course);
		if (newlead_sex_text.getText().toString().equals("保密")) {
			MyApplication.nCrDate.setSex(3 + "");
		} else {
			MyApplication.nCrDate.setSex(result_id);
		}

		MyApplication.nCrDate.setGrade_info(newlead_year.getText().toString());

		intent = new Intent(getActivity(), NewLeadContactActivity.class);
		startActivity(intent);

	}

	private void popupWindow() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View layoutView = inflater.inflate(R.layout.timepicker, null);
		TextView cancel = (TextView) layoutView.findViewById(R.id.cancel);
		TextView ok = (TextView) layoutView.findViewById(R.id.ok);
		ScreenInfo screenInfo = new ScreenInfo(getActivity());
		wheelMain = new WheelBirthday(layoutView,null);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = textView_birthday.getText().toString();
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
				// 获取到生日
				textView_birthday.setText(wheelMain.getTime());
				MyApplication.nCrDate.setBirthday(wheelMain.getTime());
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
}
