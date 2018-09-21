package com.onemeter.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ParseException;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onemeter.R;

/**
 * 工具类，包含一些常用的工具方法：<br>
 * 获取屏幕的尺寸<br>
 * 弹出Toast<br>
 * 震动<br>
 * 选择照片<br>
 * 裁切照片<br>
 * 格式化日期<br>
 * 获取临时文件的路径<br>
 * 
 * 
 */
public class Utils {
	/**
	 * 获取json中指定key对应的value，只支持value为String类型，
	 * </br>例：获取json中courier对应的value{"courier"
	 * :{"identitycode":"111111111111111",
	 * "cellphone":"18516606694","name":"来忠"},"code":0}
	 * 
	 * @author JPH
	 * @date 2015-5-22 下午4:31:40
	 * @param key
	 * @param jsonStr
	 * @return
	 */
	public static String getInternalJson(String key, String jsonStr) {
		String targetJson = "";
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(jsonStr))
			return targetJson;
		try {
			JSONObject object = new JSONObject(jsonStr);
			targetJson = object.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return targetJson == null ? "" : targetJson;
	}
	/**
	 * 获取屏幕的宽度
	 * 
	 * @param activity
	 * @return int[widthPixels,heightPixels]
	 */
	public static int[] getScreenWidth(Activity activity) {
		// 获得屏幕宽度
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int displayWidth = outMetrics.widthPixels;// 屏幕的宽度
		int displayHeight = outMetrics.heightPixels;// 屏幕高度
		return new int[] { displayWidth, displayHeight };
	}

	@SuppressLint("SimpleDateFormat")
	public static String parseDate(String stringdate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			try {
				calendar.setTime(formatter.parse(stringdate));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			Log.w("info", "解析错误！" + e.toString());
			e.printStackTrace();
		}
		Date date = new Date();
		date.setTime(calendar.getTimeInMillis());
		// Toast.makeText(this,formatter.format(date),Toast.LENGTH_SHORT).show();
		return formatter.format(date);
	}

	/**
	 * 弹出一个Toast
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, String text, int duration) {
		if (context == null)
			return;
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * 弹出一个Toast
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, int resid) {
		if (context == null)
			return;
		Utils.showToast(context, context.getString(resid));
	}

	/**
	 * 弹出一个Toast,持续Toast.LENGTH_SHORT时间
	 * 
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}

	/**
	 * 日期减少一天
	 * 
	 * @author angelyin
	 * @date 2015-10-26 下午5:31:18
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 日期增加一天
	 * 
	 * @author angelyin
	 * @date 2015-10-26 下午5:47:48
	 * @param date
	 * @return
	 */
	public static Date getAddDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 *            格式化模式，如：yyyy-MM-dd HH.ss.mm.ss
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,
				Locale.getDefault());
		return sdf.format(date);
	}

	/**
	 * 格式化日期，以默认的格式（yyyyMMddHHmmss）
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		// TODO Auto-generated method stub
		return formatDate(date, "yyyyMMddHHmmss");
	}

	/**
	 * 控制手机震动
	 * 
	 * @param context
	 * @param duration
	 *            震动时间，单位毫秒
	 */
	public static void vibration(Context context, Integer... duration) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		if (duration != null && duration.length > 0) {
			vibrator.vibrate(duration[0]); // 长按振动
		} else {
			vibrator.vibrate(60); // 长按振动
		}
	}

	/**
	 * 获取临时文件的路径如：（/storage/emulated/0/ksudi/temp）
	 * 
	 * @return
	 */
	public static File getTempFilePath() {
		File tempFile = new File(getAppFilePath() + "/temp/");
		if (!tempFile.exists()) {// 如果目录不存在则创建目录
			tempFile.mkdirs();
		}
		return tempFile;
	}

	public static String getAppFilePath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().getPath()
					+ "/ksudi";
		return Environment.getRootDirectory().getPath() + "/ksudi";
	}

	/**
	 * 不需要调到拨号界面，直接拨打电话
	 * 
	 * @author JPH
	 * @date 2014-12-8 上午10:50:13
	 * @param context
	 * @param phoneNumber
	 */
	public static void callFastPhone(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNumber));
		context.startActivity(intent);
	}

	/**
	 * 清除应用的本地缓存
	 * 
	 * @author angelyin
	 * @date 2014-12-10 下午8:35:59
	 * @param context
	 * @param cacheDir
	 * @return
	 */
	public static boolean clearCatch(Context context, File cacheDir) {
		// TODO Auto-generated method stub
		if (cacheDir == null) {
			return false;
		}
		;
		if (cacheDir.isDirectory()) {
			File[] files = cacheDir.listFiles();
			if (files == null)
				return false;
			for (int i = 0; i < files.length; i++) {
				File tempFile = files[i];
				if (tempFile != null) {
					if (tempFile.isFile()) {
						tempFile.delete();
					} else {
						clearCatch(context, tempFile);
					}
				}
			}
		} else {
			cacheDir.delete();
		}
		return true;
	}

	/**
	 * 获取缓存目录的大小
	 * 
	 * @author JPH
	 * @date 2014-12-10 下午8:36:42
	 * @param directory
	 * @return
	 */
	public static long getCacheSize(File directory) {
		if (directory == null)
			return 0;
		long catchSize = 0;
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files == null)
				return 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					catchSize += getCacheSize(files[i]);
				} else {
					catchSize += files[i].length();
				}
			}
		} else {
			catchSize += directory.length();
		}
		return catchSize;
	}

	/**
	 * 发声音和震动
	 * 
	 * @author Shoxz
	 * @date 2012-12-15 17:32
	 */
	public void sendSoundAndVibrate() {

	}

	/**
	 * 验证手机号是否正确
	 * 
	 * @Author shawn
	 * @Date 2014年12月18日
	 * @param cellphone
	 * @return true为通过
	 */
	public static boolean cellphoneValid(String cellphone) {
		String rgx = "[1][3578]\\d{9}";
		Pattern p = Pattern.compile(rgx);
		Matcher m = p.matcher(cellphone);
		return m.matches();
	}

	/**
	 * 验证密码是否包含中文
	 * 
	 * @Author shawn
	 * @Date 2014年12月18日
	 * @param password
	 * @return
	 */
	public static boolean passwordValid(String password) {
		String rgx = ".*?[\u4E00-\u9FA5]+.*?";
		Pattern p = Pattern.compile(rgx);
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/** 清楚通知 */
	public static void clearNotification(Context context, int noticeid) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(noticeid);
	}

	/**
	 * 弹出打开网络提示框
	 * 
	 * @author JPH
	 * @date 2015-2-2 下午3:38:28
	 */
	public static PopupWindow showOpenNetPopup(final Activity activity,
			View anchor) {
		View menuView = LayoutInflater.from(activity).inflate(
				R.layout.pop_open_net, null);
		PopupWindow popupWindow = new PopupWindow(menuView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, false);
		popupWindow.showAsDropDown(anchor);
		RelativeLayout rl_pop_net = (RelativeLayout) menuView
				.findViewById(R.id.rl_pop_net);
		rl_pop_net.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showDebugToast(activity, "打开系统设置");
				if (android.os.Build.VERSION.SDK_INT > 13) {// 3.2以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_SETTINGS));
				} else {
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				}
			}
		});
		return popupWindow;
	}

	/**
	 * 在测试快发是弹出提示信息
	 * 
	 * @author JPH
	 * @date 2015-1-26 下午4:21:34
	 * @param context
	 * @param text
	 */
	public static void showDebugToast(Context context, String text) {
		if (C_Constants.DEVELOPER_MODE) {
			showToast(context, text, Toast.LENGTH_SHORT);
		}
	}

	/**
	 * dp转换成px
	 * 
	 * @author shawn
	 * @date 2015-3-3_14-30-42
	 * @param dp
	 * @param context
	 */
	public static int dp2px(int dp, Context context) {
		return ((int) ((context.getResources().getDisplayMetrics().density) * dp));
	}

	/**
	 * 将double形式的object转换成long
	 * 
	 * @param object
	 * @return
	 * @author JPH
	 * @date 2015-4-28 下午4:44:14
	 */
	public static long doubleObjectToLong(Object object) {
		if (object == null)
			return -1000;
		return Long.parseLong(object.toString().replace(".0", ""));
	}

	/**
	 * 播放提示音
	 * 
	 * @author JPH
	 * @date 2015-5-18 下午2:36:33
	 * @param context
	 * @param resId
	 *            要播放的提示音的资源id
	 */
	public static void playRing(Context context, int resId) {
		final SoundPool pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		final int soundID = pool.load(context, resId, 1);
		pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				pool.play(soundID, 1, 1, 0, 0, 1);
			}
		});
	}

}
