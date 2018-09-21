package com.onemeter.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.onemeter.entity.DData;
import com.onemeter.entity.NewCustomer;
import com.onemeter.entity.UpdateCustomer;
import com.onemeter.entity.StatusDDate.BBody.getStatusData;
import com.onemeter.entity.UserDate;
import com.onemeter.entity.checkedMsg;
import com.onemeter.receiver.NetChangeReceiver;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 自定义app启动
 * 
 * @author angelyin
 * @date 2015-10-9 下午7:50:54
 */
public class MyApplication extends Application {
	private static MyApplication app;
	/** 网络是否可用的标识 **/
	public static boolean isNetAvailable = false;
	/** 网络状态改变接收器 **/
	public NetChangeReceiver receiver;
	/** 用户登录后的初始化数据信息 **/
	public static UserDate uuserDate;
	/** 新增潜客填写信息 **/
	public static NewCustomer nCrDate;
	public static UpdateCustomer nCrDatelist;
	/** 获取状态的集合 **/
	public static List<getStatusData> gstlist;
	public static List<DData> dataBean;;
	// 未联系时间
	public static List<String> str_time;
	public static List<String> IDmap;
	public static List<String> checkedID;
	public static Map<Integer, Boolean> isCheckMap;
	public static List<String> checkedName;

	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate() {
		super.onCreate();
		isNetAvailable = isNetworkConnected();
		receiver = new NetChangeReceiver();
		uuserDate = new UserDate();
		nCrDate = new NewCustomer();
		gstlist = new ArrayList<getStatusData>();
		str_time = new ArrayList<String>();
		dataBean = new ArrayList<DData>();
		IDmap = new ArrayList<String>();
		nCrDatelist = new UpdateCustomer();
		isCheckMap = new HashMap<Integer, Boolean>();
		checkedID = new ArrayList<String>();
		checkedName=new ArrayList<String>();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addCategory("android.intent.category.DEFAULT");
		registerReceiver(receiver, filter);// 注册广播接收器receiver
		initImageLoader(getApplicationContext());
		str_time.clear();
		str_time.add(0, "不限");
		str_time.add(1, "1周");
		str_time.add(2, "2周");
		str_time.add(3, "3周");
		str_time.add(4, "3周以上");
	}

	/**
	 * 判断网络状态是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		} else {
			return false;
		}
	}

	/**
	 * imageload加载图片初始化
	 * 
	 * @param applicationContext
	 */
	private void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "cancan/Cache");
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// 缓存的文件数量
				.discCacheFileCount(50).memoryCache(new WeakMemoryCache())
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
				// 30 * 1000))
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public static MyApplication getInstance() {
		if (app == null) {
			app = new MyApplication();
		}
		return app;
	}

}
