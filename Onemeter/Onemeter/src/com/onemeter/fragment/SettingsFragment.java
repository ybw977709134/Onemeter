package com.onemeter.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.activity.ChagePassWordActivity;
import com.onemeter.activity.LoginActivity;
import com.onemeter.activity.MainActivity;
import com.onemeter.app.MyApplication;

/**
 * 设置模块
 * 
 * @author angelyin
 * @date 2015-10-10 下午7:52:15
 */
public class SettingsFragment extends Fragment implements OnClickListener {
	private View view = null;
	private LinearLayout linearLayout_changepassword = null;;
	private TextView button_exit;
	private Intent intent;
	private TextView school_num;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.settings_fragment_layout, container,
				false);
		initView();
		return view;
	}

	private void initView() {
		linearLayout_changepassword = (LinearLayout) view
				.findViewById(R.id.linearLayout_changepassword);
		button_exit = (TextView) view.findViewById(R.id.button_exit);
		school_num = (TextView) view.findViewById(R.id.school_num);
		school_num.setText(MyApplication.uuserDate.getSchool());

		linearLayout_changepassword.setOnClickListener(this);
		button_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearLayout_changepassword:
			intent = new Intent(getActivity(), ChagePassWordActivity.class);
			startActivity(intent);
			break;

		case R.id.button_exit:
			exitDialog();
			break;
		}

	}

	private void exitDialog() {
		final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.exit_diglog_layout, null);
		dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lParams = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		dialog.onWindowAttributesChanged(lParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Button button_qd = (Button) view.findViewById(R.id.button_qd);
		Button button_qx = (Button) view.findViewById(R.id.button_qx);
		button_qd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//切换账号时初始化所有数据源
				MyApplication.dataBean.clear();
				MyApplication.checkedID.clear();
				MyApplication.gstlist.clear();
				MyApplication.isCheckMap.clear();
				MyApplication.str_time.clear();
				MyApplication.IDmap.clear();
				MyApplication.checkedName.clear();
				
				intent = new Intent(getActivity(), LoginActivity.class);				
				startActivity(intent);
				getActivity().finish();
				
				dialog.dismiss();
			}
		});
		button_qx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
