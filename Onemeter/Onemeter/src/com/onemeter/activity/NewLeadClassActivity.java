package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.onemeter.R;
import com.onemeter.adapter.NewLeadClassAdapter;
import com.onemeter.app.BaseActivity;

/**
 * 年级选择页面
 * 
 * @author angelyin
 * @date 2015-10-14 上午10:43:44
 */
public class NewLeadClassActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private LinearLayout linearLayout_back_class = null;
	private ListView listView_class = null;
	private static final String[] str_class = new String[] { "一年级", "二年级",
			"三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级" };
	String requestCod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newslead_class_layout);
		requestCod = getIntent().getStringExtra("requestCode");
		initView();
	}

	private void initView() {
		linearLayout_back_class = (LinearLayout) findViewById(R.id.linearLayout_back_class);
		listView_class = (ListView) findViewById(R.id.listView_class);
		listView_class.setAdapter(new NewLeadClassAdapter(this, str_class));
		linearLayout_back_class.setOnClickListener(this);

		listView_class.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearLayout_back_class:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		String str = str_class[position].toString();
		toast(str);
		Intent intent = new Intent();
		intent.putExtra("second", str);
		if (requestCod.equals("4444")) {
			setResult(4444, intent);
		} else if (requestCod.equals("2")) {
			setResult(2, intent);
		}else if(requestCod.equals("141414")){
			setResult(141414, intent);
		}

		finish();
	}
}
