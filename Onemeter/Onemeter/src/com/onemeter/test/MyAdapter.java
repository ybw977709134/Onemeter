package com.onemeter.test;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.MyApplication;

public class MyAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;

	/**
	 * 数据集合
	 */
	// private List<DemoBean> data = null;
	// private List<DData> dataBean=MyApplication.dataBean;

	/**
	 * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
	 */
	@SuppressLint("UseSparseArrays")
	public Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

	public MyAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		// if (dataBean != null) {
		// this.dataBean = dataBean;
		// }
		// 初始化,默认都没有选中
		configCheckMap(false);
	}

	/**
	 * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
	 */
	public void configCheckMap(boolean bool) {

		for (int i = 0; i < MyApplication.dataBean.size(); i++) {
			isCheckMap.put(i, bool);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MyApplication.dataBean.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MyApplication.dataBean.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.expandable_list_item, null);
			holder = new ViewHolder();
			holder.gengxin_time_date = (TextView) convertView
					.findViewById(R.id.gengxin_time_date);
			holder.lead_cst_name = (TextView) convertView
					.findViewById(R.id.lead_cst_name);
			holder.lead_text_status = (TextView) convertView
					.findViewById(R.id.lead_text_status);
			holder.Dynamic_text = (TextView) convertView
					.findViewById(R.id.Dynamic_text);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * 获得该item 是否允许删除
		 */
		if (MyApplication.dataBean.size() == 0) {
			return parent;
		} else {
			if (null!=MyApplication.dataBean.get(position)) {
			boolean canRemove = MyApplication.dataBean.get(position)
					.isCanRemove();
			boolean checkedd = MyApplication.dataBean.get(position)
					.isIscheckedd();

			// 设置itme的值
			String str = MyApplication.dataBean.get(position).getTime_stamp();
			String time = str.substring(5, 10);
			holder.gengxin_time_date.setText(time);
			holder.lead_cst_name.setText(MyApplication.dataBean.get(position)
					.getStudent_name());
			if (null!=MyApplication.dataBean.get(position).getStatusdata()) {
				
				if (!TextUtils.isEmpty(MyApplication.dataBean.get(position).getStatusdata().getStatus())) {
					holder.lead_text_status.setText(MyApplication.dataBean
							.get(position).getStatusdata().getStatus());
				}else {
					holder.lead_text_status.setText(null);
				}
				}else {
					holder.lead_text_status.setText(null);
				}
			holder.Dynamic_text.setText(MyApplication.dataBean.get(position)
					.getMemo());
			holder.checkBox.setVisibility(View.GONE);
			holder.checkBox.setChecked(checkedd);

			/*
			 * 设置单选按钮的选中
			 */
			holder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							/*
							 * 将选择项加载到map里面寄存
							 */
							isCheckMap.put(position, isChecked);

							MyApplication.isCheckMap = isCheckMap;
						}
					});

			if (!canRemove) {
				// 隐藏单选按钮,因为是不可删除的
				holder.checkBox.setVisibility(View.GONE);
				holder.checkBox.setChecked(false);
			} else {
				holder.checkBox.setVisibility(View.VISIBLE);

				if (isCheckMap.get(position) == null) {
					isCheckMap.put(position, false);
				}
				holder.checkBox.setChecked(isCheckMap.get(position));
				holder.checkBox.setTag(holder);
			}
		}
		}
		return convertView;
	}

	public class ViewHolder {
		TextView gengxin_time_date;
		TextView lead_cst_name;
		TextView lead_text_status;
		TextView Dynamic_text;
		CheckBox checkBox;
	}

}
