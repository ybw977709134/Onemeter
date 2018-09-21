package com.onemeter.activity;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 添加潜客成功后的页面
 * @author angelyin
 * @date 2015-10-28 下午3:52:07
 */
public class NewsAddLeadActivity extends BaseActivity implements OnClickListener{
	/**返回键**/
	private ImageView imageView_back;
	/**继续添加**/
	private Button button_continue_add;
	/**编辑按钮**/
	private Button imageView_edit;
	String Student_Name;
	String Student_Mobai;
	String customerr_Id;
	/**显示姓名**/
	private TextView  text_student_name;
	/**显示手机号码**/
	private TextView  text_student_mobai;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.add_newlead_layout);
	Intent intent=getIntent();
	Student_Name=intent.getStringExtra("studentName");
	Student_Mobai=intent.getStringExtra("studentMobai");
	customerr_Id=intent.getStringExtra("customerr_Id");	
	initView();
	
}
private void initView() {
	text_student_name=(TextView)findViewById(R.id.text_student_name);
	text_student_mobai=(TextView)findViewById(R.id.text_student_mobai);
	button_continue_add=(Button)findViewById(R.id.button_continue_add);
	imageView_edit=(Button)findViewById(R.id.imageView_edit);
	text_student_name.setText(Student_Name);
	text_student_mobai.setText(Student_Mobai);
	button_continue_add.setOnClickListener(this);
	imageView_edit.setOnClickListener(this);
}
@Override
public void onClick(View view) {
	switch (view.getId()) {
	case R.id.button_continue_add:
		//继续添加
      Intent intent=new Intent(this,MainActivity.class);
      intent.putExtra("NewleadFragment", 1);
      startActivity(intent);
		break;
	case R.id.imageView_edit:
		//编辑
		Intent intent1=new Intent(this,NewAddLeadEditActivity.class);
		intent1.putExtra("customerr_id", customerr_Id);
	    startActivity(intent1);
		break;

	default:
		break;
	}
	}

public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	return super.onKeyDown(keyCode, event);
}

}
