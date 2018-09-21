package com.onemeter.test;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.onemeter.app.MyApplication;
import com.onemeter.entity.DData;

public class LoadData extends AsyncTask<Void, Void, ArrayList<DData>> {
	private ArrayList<DData> mStrings = new ArrayList<DData>();
	private int page;
	private MyCallback callback;

	public LoadData(int page,MyCallback callback)
	{
		this.callback=callback;
		for (int i = 0; i < MyApplication.dataBean.size(); i++) {
//			DemoBean demoBean = new DemoBean();
			DData  dd=new DData();
//			AllCustomerData acdate=new AllCustomerData();
//			String item = "Item";
//			demoBean.setTitle(item+i);
			dd.setLeads_date(MyApplication.dataBean.get(i).getLeads_date());
			dd.setStudent_name(MyApplication.dataBean.get(i).getStudent_name());
			dd.setStatusdata(MyApplication.dataBean.get(i).getStatusdata());
			dd.setCanRemove(false);
			mStrings.add(dd);
		}
		this.page=page;
	}
	
	@Override
	protected void onPreExecute() {
		//开始执行异步线程
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(ArrayList<DData> lists) {
		//异步操作执行结束
		if(lists.size()>0){
		if(page==1){
			callback.clearListView();
		}
		else 
			if(page==3)
		{
			lists.remove(0);
		}
		callback.updateListView(lists);
		callback.updatePage(page+1);
		}
	}
	//该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改  
   // 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作 
	@Override
	protected ArrayList<DData> doInBackground(Void... arg0) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mStrings;
	}
}
