package com.onemeter.util;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具类
 * 
 */
public class SharedPreferencesUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private String name = "wrz";
	private int mode = Context.MODE_MULTI_PROCESS;

	public SharedPreferencesUtil(Context context) {
		sp = context.getSharedPreferences(name, mode);
		editor = sp.edit();
	}

	public SharedPreferencesUtil(Context context, String name, int mode) {
		this.sp = context.getSharedPreferences(name, mode);
		this.editor = sp.edit();
	}

	/**
	 * 增加信息
	 */
	public void add(Map<String, String> map) {
		Set<String> set = map.keySet();
		for (String key : set) {
			editor.putString(key, map.get(key));
		}
		editor.apply();
	}

	/**
	 * 删除信息
	 */
	public void deleteAll() throws Exception {
		editor.clear();
		editor.apply();
	}

	/**
	 * 删除一条信息
	 */
	public void delete(String key) {
		editor.remove(key);
		editor.apply();
	}

	/**
	 * 获取信息
	 * 
	 */
	public String get(String key) {
		if (sp != null) {
			return sp.getString(key, "");
		}
		return "";
	}

	/**
	 * 获取此SharedPreferences的Editor实例
	 * 
	 */
	public Editor getEditor() {
		return editor;
	}

}
