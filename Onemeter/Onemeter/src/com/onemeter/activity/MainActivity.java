package com.onemeter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.fragment.LeadsFragment;
import com.onemeter.fragment.NewleadFragment;
import com.onemeter.fragment.SettingsFragment;
import com.onemeter.fragment.StatisticFragment;

/**
 * 主页面
 * 
 * @ClassName: MainActivity
 * @author angelyin 2015-10-9 下午3:59:40
 */

public class MainActivity extends FragmentActivity implements OnClickListener {

	// 定义4个Fragment
	private LeadsFragment fragment1 = null;
	private NewleadFragment fragment2 = null;
	private StatisticFragment fragment3 = null;
	private SettingsFragment fragment4 = null;
	// 定义4个选项布局
	private View leads_layout = null;
	private View newlead_layout = null;
	private View settings_layout = null;
	private View statistics_layout = null;
	// 定义4个文字
	private TextView leads_textView = null;
	private TextView newlead_textView = null;
	private TextView settings_textView = null;
	private TextView statistics_textView = null;
	// 定义4个选项图片
	private ImageView leadsImageView = null;
	private ImageView newleadImageView = null;
	private ImageView settingsImageView = null;
	private ImageView statisticsImageView = null;

	// 延时退出时间变量
	private long ExitTime;
	// 定义FragmentManager
	FragmentManager fragmentManager;
	public String curFragmentTag = "";

	private LinearLayout main_layout;
	private RelativeLayout radio_layout;
	// 选择事件
	private CheckBox button_checkBox;
	private Button button_lose;
	private Button button_update;
	static MainActivity instance = null;   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化组件
		initViews();
		instance = this;
		fragmentManager = getSupportFragmentManager();

		// 默认打开第一个选项
		setTabSelection(R.id.leads_layout);
		registerDownAllResultReceiver();
		registerMainResultReceiver();
		registerCancelResultReceiver();
	}

	// 初始化组件
	private void initViews() {
		// 初始化4个选项布局
		leads_layout = findViewById(R.id.leads_layout);
		newlead_layout = findViewById(R.id.newlead_layout);
		settings_layout = findViewById(R.id.settings_layout);
		statistics_layout = findViewById(R.id.statistics_layout);
		// 初始化4个文字
		leads_textView = (TextView) findViewById(R.id.leads_text);
		newlead_textView = (TextView) findViewById(R.id.newlead_text);
		settings_textView = (TextView) findViewById(R.id.settings_text);
		statistics_textView = (TextView) findViewById(R.id.statistics_text);
		// 初始化4个选项图片
		leadsImageView = (ImageView) findViewById(R.id.leads_image);
		newleadImageView = (ImageView) findViewById(R.id.newlead_image);
		settingsImageView = (ImageView) findViewById(R.id.settings_image);
		statisticsImageView = (ImageView) findViewById(R.id.statistics_image);

		main_layout = (LinearLayout) findViewById(R.id.main_layout);
		radio_layout = (RelativeLayout) findViewById(R.id.radio_layout);
		main_layout.setVisibility(View.VISIBLE);
		radio_layout.setVisibility(View.GONE);

		button_checkBox = (CheckBox) findViewById(R.id.button_checkBox);
		button_lose = (Button) findViewById(R.id.button_lose);
		button_update = (Button) findViewById(R.id.button_update);

		leads_layout.setOnClickListener(this);
		newlead_layout.setOnClickListener(this);
		settings_layout.setOnClickListener(this);
		statistics_layout.setOnClickListener(this);
		button_lose.setOnClickListener(this);
		button_update.setOnClickListener(this);

		/*
		 * 设置全选按钮的选中
		 */
		button_checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Intent intent = new Intent();
						intent.setAction("upallresult");
						intent.putExtra("type", 1);
						intent.putExtra("ischecked",
								button_checkBox.isChecked());
						sendBroadcast(intent);
					}
				});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.leads_layout:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(R.id.leads_layout);

			break;
		case R.id.newlead_layout:
			setTabSelection(R.id.newlead_layout);
			break;
		case R.id.statistics_layout:
			setTabSelection(R.id.statistics_layout);
			break;
		case R.id.settings_layout:
			setTabSelection(R.id.settings_layout);
			break;
		case R.id.button_lose:
			Intent intent = new Intent();
			intent.setAction("upallresult");
			intent.putExtra("type", 2);
			sendBroadcast(intent);
			break;
		case R.id.button_update:
			Intent intent2 = new Intent();
			intent2.setAction("upallresult");
			intent2.putExtra("type", 3);
			sendBroadcast(intent2);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

	}

	// 当点击了消息tab时，选中第1个tab
	public void setTabSelection(int i) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 创建FragmentTransaction
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		hideFragments(fragmentTransaction);
		switch (i) {
		case R.id.leads_layout:
			// 当点击了消息tab时，改变控件的图片
			leadsImageView.setImageResource(R.drawable.icon_tabbar_leads_p);
			// 设置字体颜色
			leads_textView.setTextColor(Color.rgb(10, 145, 229));
			if (fragment1 == null) {
				// 如果HomeFragment为空，则创建一个并添加到界面上
				fragment1 = new LeadsFragment();
				fragmentTransaction.add(R.id.content, fragment1);
			} else {
				// 如果HomeFragment不为空，则直接将它显示出来
				fragmentTransaction.show(fragment1);
			}
			break;
		case R.id.newlead_layout:

			newleadImageView.setImageResource(R.drawable.icon_tabbar_newlead_p);
			newlead_textView.setTextColor(Color.rgb(10, 145, 229));
			if (fragment2 == null) {
				fragment2 = new NewleadFragment();
				fragmentTransaction.add(R.id.content, fragment2);
			} else {
				fragmentTransaction.show(fragment2);
			}
			break;
		case R.id.statistics_layout:
			statisticsImageView
					.setImageResource(R.drawable.icon_tabbar_statistics_p);
			statistics_textView.setTextColor(Color.rgb(10, 145, 229));
			if (fragment3 == null) {
				fragment3 = new StatisticFragment();
				fragmentTransaction.add(R.id.content, fragment3);
			} else {
				fragmentTransaction.show(fragment3);
			}
			break;
		case R.id.settings_layout:
			settingsImageView
					.setImageResource(R.drawable.icon_tabbar_settings_p);
			settings_textView.setTextColor(Color.rgb(10, 145, 229));
			if (fragment4 == null) {
				fragment4 = new SettingsFragment();
				fragmentTransaction.add(R.id.content, fragment4);
			} else {
				fragmentTransaction.show(fragment4);
			}
			break;
		}
		fragmentTransaction.commit();
	}

	// 清除掉所有的选中状态。
	private void clearSelection() {
		leadsImageView.setImageResource(R.drawable.icon_tabbar_leads);
		leads_textView.setTextColor(Color.parseColor("#666666"));
		newleadImageView.setImageResource(R.drawable.icon_tabbar_newlead);
		newlead_textView.setTextColor(Color.parseColor("#666666"));
		statisticsImageView.setImageResource(R.drawable.icon_tabbar_statistics);
		statistics_textView.setTextColor(Color.parseColor("#666666"));
		settingsImageView.setImageResource(R.drawable.icon_tabbar_settings);
		settings_textView.setTextColor(Color.parseColor("#666666"));
	}

	// 将所有的Fragment都置为隐藏状态。
	private void hideFragments(FragmentTransaction fragmentTransaction) {
		if (fragment1 != null) {
			fragmentTransaction.hide(fragment1);
		}
		if (fragment2 != null) {
			fragmentTransaction.hide(fragment2);
		}
		if (fragment3 != null) {
			fragmentTransaction.hide(fragment3);
		}
		if (fragment4 != null) {
			fragmentTransaction.hide(fragment4);
		}
	}

	/*
	 * 搜索按钮控制主界面布局的隐藏
	 */
	MainResultReceiver mainResultReceiver=new MainResultReceiver();
	private void registerMainResultReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("mainresult");
		registerReceiver(mainResultReceiver, intentFilter);
	}
	private void relaseRegisterMainResultReceiver(){
		unregisterReceiver(mainResultReceiver);
	}
	private class MainResultReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			main_layout.setVisibility(View.GONE);
			radio_layout.setVisibility(View.GONE);
		}
		
	}
	/*
	 * 搜索按钮控制主界面布局的显示
	 */
	CancelResultReceiver cancelResultReceiver=new CancelResultReceiver();
	private void registerCancelResultReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("cancelresult");
		registerReceiver(cancelResultReceiver, intentFilter);
	}
	private void relaseRegisterCancelResultReceiver() {
		unregisterReceiver(cancelResultReceiver);
	}
	
	private class CancelResultReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			main_layout.setVisibility(View.VISIBLE);
			
		}
		
	}
	/*
	 * 多选按钮控制主界面布局的隐藏、全选布局的显示
	 */
	DownAllResultReceiver downAllResultReceiver = new DownAllResultReceiver();

	private void registerDownAllResultReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("downallresult");
		registerReceiver(downAllResultReceiver, intentFilter);
	}
	private void relaseRegisterDownAllResultReceiver() {
		unregisterReceiver(downAllResultReceiver);
	}

	private class DownAllResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				boolean isshow = intent.getBooleanExtra("isshow", true);
				button_checkBox.setChecked(false);
				if (isshow) {
					main_layout.setVisibility(View.GONE);
					radio_layout.setVisibility(View.VISIBLE);
				} else {
					main_layout.setVisibility(View.VISIBLE);
					radio_layout.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		relaseRegisterDownAllResultReceiver();
		relaseRegisterMainResultReceiver();
		relaseRegisterCancelResultReceiver();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按两下退出程序
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - ExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出招生宝", Toast.LENGTH_SHORT).show();
				ExitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
   public void exit(){
	 
	finish();
   }

}
