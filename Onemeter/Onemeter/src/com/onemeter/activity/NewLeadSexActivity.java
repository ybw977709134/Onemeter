package com.onemeter.activity;

import com.onemeter.R;
import com.onemeter.adapter.NewLeadClassAdapter;
import com.onemeter.adapter.NewLeadSexAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.fragment.NewleadFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NewLeadSexActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private LinearLayout linearLayout_back_sex = null;
	private ListView listView_sex = null;
	private static final String[] str_sex = new String[] { "男", "女", "保密" };
	private String[] str_sex_id = new String[] { "1", "2", "3" };
	public static final String MY_NEW_LIFEFORM = "NEW_LIFEFORM";
	String resultCod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newlead_sex_layout);
		resultCod = getIntent().getStringExtra("requestCode");
		initView();
	}

	private void initView() {
		linearLayout_back_sex = (LinearLayout) findViewById(R.id.linearLayout_back_sex);
		listView_sex = (ListView) findViewById(R.id.listView_sex);
		listView_sex.setAdapter(new NewLeadSexAdapter(this, str_sex));
		linearLayout_back_sex.setOnClickListener(this);
		listView_sex.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.linearLayout_back_sex:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		String str = str_sex[position].toString();
		String str1 = str_sex_id[position].toString();
		toast(str);
		Intent intent = new Intent();
		intent.putExtra("one", str);
		intent.putExtra("one_id", str1);
		if (resultCod.equals("3333")) {
			setResult(3333, intent);
		} else if (resultCod.equals("1")) {
			setResult(1, intent);
		}else if(resultCod.equals("131313"))
			setResult(131313, intent);
		finish();

	}
}
