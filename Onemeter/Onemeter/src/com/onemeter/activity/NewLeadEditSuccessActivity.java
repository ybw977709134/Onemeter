package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;

/**
 * 新增潜客成功页面
 * 
 * @author angelyin
 * @date 2015-10-14 下午10:24:37
 */
public class NewLeadEditSuccessActivity extends BaseActivity implements
		OnClickListener {
	private Button bnt_continue = null;
	private Button btn_edit = null;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newlead_success_layout);
		initView();
	}

	private void initView() {
		btn_edit = (Button) findViewById(R.id.btn_edit);
		btn_edit.setOnClickListener(this);
		bnt_continue = (Button) findViewById(R.id.bnt_continue);
		bnt_continue.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bnt_continue:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_edit:
			intent = new Intent(this, NewLeadEditActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
