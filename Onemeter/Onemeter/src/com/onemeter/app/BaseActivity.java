package com.onemeter.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.onemeter.activity.MainActivity;
import com.onemeter.util.ActUtil;

/**
 * activity的基类
 * 
 * @ClassName: BaseAct
 * @author angelyin 2015-10-9 下午4:00:00
 */

public abstract class BaseActivity extends Activity {
	// 延时退出时间变量
	private long ExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**
		 * 单例模式
		 */
		ActUtil.getInstance().addActivity(this);
	}

	// ======activity生命周期的各阶段调用方法======
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 软键盘的控制
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 判断是否需要隐藏
	 * 
	 * @param view
	 * @param ev
	 * @return
	 */
	private boolean isHideInput(View v, MotionEvent ev) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}

		}

		return false;
	}

	/**
	 * 控制软键盘显示或隐藏
	 * 
	 * @param token
	 */
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 吐司提示消息的方法
	 */
	private Toast toast = null;

	public void toast(String msg) {
		if (null == msg || msg.equals("")) {
			return;
		}
		if (null == toast)
			toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		else
			toast.setText(msg);
		toast.show();
	}

	/**
	 * 打开一个activity的方法
	 */
	protected void openAct(Class<?> clas) {
		Intent intent = new Intent(this, clas);
		startActivity(intent);
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // 按两下退出程序
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if ((System.currentTimeMillis() - ExitTime) > 2000) {
	// Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	// ExitTime = System.currentTimeMillis();
	// } else {
	// System.exit(0);
	// }
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

}
