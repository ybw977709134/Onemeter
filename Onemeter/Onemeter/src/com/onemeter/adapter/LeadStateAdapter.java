package com.onemeter.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.entity.StatusDDate.BBody.getStatusData;

public class LeadStateAdapter extends BaseAdapter {
	private Context context;
	private List<getStatusData> gstlist;
	private int selectedPosition;

	public LeadStateAdapter(Context context, List<getStatusData> gstlist,
			int selectedPosition) {

		this.context = context;
		this.gstlist = gstlist;
		this.selectedPosition = selectedPosition;
	}

	// 选中行
	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	@Override
	public int getCount() {

		return gstlist.size();
	}

	@Override
	public Object getItem(int position) {
		return gstlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		TextView textView_state = null;
		RadioButton radiobutton = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.lead_state_item, null);
			textView_state = (TextView) view.findViewById(R.id.textView_state);
			radiobutton = (RadioButton) view.findViewById(R.id.radiobutton);
			view.setTag(new MyView(textView_state, radiobutton));
		} else {
			MyView myView = (MyView) view.getTag();
			textView_state = myView.textView_state;
			radiobutton = myView.radioButton;
		}
		textView_state.setText(gstlist.get(position).getStatus().toString());
		if (selectedPosition == position) {
			radiobutton.setSelected(true);
			radiobutton.setPressed(true);
			radiobutton.setVisibility(View.VISIBLE);
			view.setBackgroundColor(Color.rgb(220, 220, 220));
//			textView_state.setText(textView_state);
		} else {
			radiobutton.setSelected(false);
			radiobutton.setPressed(false);
			radiobutton.setVisibility(View.GONE);
			view.setBackgroundColor(Color.WHITE);
		}
		return view;
	}

	public class MyView {
		private TextView textView_state;
		private RadioButton radioButton;

		public MyView(TextView textView_state, RadioButton radiobutton) {
			this.textView_state = textView_state;
			this.radioButton = radiobutton;
		}
	}

}
