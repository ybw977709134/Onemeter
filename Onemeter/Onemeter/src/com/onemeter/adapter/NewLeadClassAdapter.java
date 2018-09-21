package com.onemeter.adapter;

import com.onemeter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewLeadClassAdapter extends BaseAdapter {
	private Context context;
	private String[] str_class;

	public NewLeadClassAdapter(Context context, String[] str_class) {
		this.context = context;
		this.str_class = str_class;
	}

	@Override
	public int getCount() {

		return str_class.length;
	}

	@Override
	public Object getItem(int position) {
		return str_class[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		TextView textView_class = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.newlead_class_item, null);
			textView_class = (TextView) view.findViewById(R.id.textView_class);
			view.setTag(new MyView(textView_class));
		} else {
			MyView myView = (MyView) view.getTag();
			textView_class = myView.textView_class;
		}
		textView_class.setText(str_class[position]);
		return view;
	}

	public class MyView {
		private TextView textView_class;

		public MyView(TextView textView_class) {

			this.textView_class = textView_class;

		}
	}

}
