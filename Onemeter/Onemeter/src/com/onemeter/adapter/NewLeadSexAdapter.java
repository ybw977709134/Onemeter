package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.activity.NewLeadSexActivity;
import com.onemeter.adapter.NewLeadClassAdapter.MyView;

public class NewLeadSexAdapter extends BaseAdapter {
	private Context context;
	private String[] str_sex;

	public NewLeadSexAdapter(Context context, String[] str_sex) {
		this.context = context;
		this.str_sex = str_sex;
	}

	@Override
	public int getCount() {

		return str_sex.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		TextView textView_sex = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.newlead_sex_item, null);
			textView_sex = (TextView) view.findViewById(R.id.textView_sex);
			view.setTag(new MyView(textView_sex));
		} else {
			MyView myView = (MyView) view.getTag();
			textView_sex = myView.textView_sex;
		}
		textView_sex.setText(str_sex[position]);
		return view;
	}

	public class MyView {
		private TextView textView_sex;

		public MyView(TextView textView_sex) {

			this.textView_sex = textView_sex;

		}
	}

}
