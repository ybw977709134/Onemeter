package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.onemeter.R;

public class NewLeadSourceAdapter extends BaseAdapter {
	private Context context;
	private String[] str_source;

	public NewLeadSourceAdapter(Context context, String[] str_source) {
		this.context = context;
		this.str_source = str_source;
	}

	@Override
	public int getCount() {

		return str_source.length;
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
		TextView textView_source = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.newlead_source_item, null);
			textView_source = (TextView) view
					.findViewById(R.id.textView_source);
			view.setTag(new MyView(textView_source));
		} else {
			MyView myView = (MyView) view.getTag();
			textView_source = myView.textView_source;
		}
		textView_source.setText(str_source[position]);
		return view;
	}

	public class MyView {
		private TextView textView_source;

		public MyView(TextView textView_source) {

			this.textView_source = textView_source;

		}
	}

}
