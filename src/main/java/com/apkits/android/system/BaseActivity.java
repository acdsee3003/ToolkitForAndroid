/**
 * Copyright (C) 2012 TookitForAndroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apkits.android.system;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * <ul>
 * <li><b>name : </b>       BaseActivity</li>
 * <li><b>description :</b> Activity特性辅助工具</li>
 * <li><b>author : </b>     桥下一粒砂           </li>
 * <li><b>e-mail : </b>     chenyoca@gmail.com  </li>
 * <li><b>weibo : </b>      @桥下一粒砂          </li>
 * <li><b>date : </b>       2012-7-8 下午5:29:41</li>
 * </ul>
 */
public class BaseActivity {

	/** Activity引用  */
	public Activity activity;
	
	/** Context引用 */
	public Context context;
	
	/**  双击退出模块 */
	private DClickExit mDClickExit;
	
	/**
	 * <b>description :</b>		TODO
	 * </br><b>time :</b>		2012-8-22 下午11:01:55
	 * @param activity
	 */
	public BaseActivity(Activity act){
		activity = act;
		context = activity;
	}
	
	/**
	 * </br><b>description :</b>启用双击返回键退出程序
	 * </br><b>time :</b>		2012-7-18 下午9:24:41
	 */
	public void enabledDClickExit(){
		mDClickExit = new DClickExit(activity);
	}

	/**
	 * <b>description :</b>		检查按键
	 * </br><b>time :</b>		2012-8-22 下午11:03:57
	 * @param keyCode
	 * @return
	 */
	public boolean checkExist(int keyCode) {
		if( null != mDClickExit ){
			return mDClickExit.doubleClickExit(keyCode);
		}else{
			return false;
		}
	}
	
	/**
	 * </br><b>description :</b>设置Activity全屏显示。
	 * @param activity 			Activity引用
	 * @param isFull 			true为全屏，false为非全屏
	 */
	public static void setFullScreen(Activity activity,boolean isFull){
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
	
	/**
	 * </br><b>description :</b>隐藏Activity的系统默认标题栏
	 * @param activity Activity对象
	 */
	public static void hideTitleBar(Activity activity){
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	/**
	 * </br><b>description :</b>强制设置Actiity的显示方向为垂直方向。
	 * @param activity 			Activity对象
	 */
	public static void setScreenVertical(Activity activity){
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 * </br><b>description :</b>强制设置Actiity的显示方向为横向。
	 * @param activity 			Activity对象
	 */
	public static void setScreenHorizontal(Activity activity){
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
     * </br><b>description :</b>隐藏软件输入法
     * </br><b>time :</b>       2012-7-12 下午7:20:00
	 * @param activity
	 */
	public static void hideSoftInput(Activity activity){
	    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/**
	 * </br><b>description :</b>使UI适配输入法
	 * </br><b>time :</b>		2012-7-17 下午10:21:26
	 * @param activity
	 */
	public static void adjustSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
	
	/**
	 * </br><b>description :</b>跳转到某个Activity
	 * </br><b>time :</b>		2012-7-8 下午3:20:00
	 * @param activity			本Activity
	 * @param targetActivity	目标Activity的Class
	 */
	public static void switchTo(Activity activity,Class<? extends Activity> targetActivity){
		switchTo(activity, new Intent(activity,targetActivity));
	}
	
	/**
	 * </br><b>description :</b>根据给定的Intent进行Activity跳转
	 * </br><b>time :</b>		2012-7-8 下午3:22:23
	 * @param activity			Activity对象
	 * @param intent			要传递的Intent对象
	 */
	public static void switchTo(Activity activity,Intent intent){
		activity.startActivity(intent);
		activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}
	
	/**
	 * </br><b>description :</b>带参数进行Activity跳转
	 * </br><b>time :</b>		2012-7-8 下午3:24:54
	 * @param activity			Activity对象
	 * @param targetActivity	目标Activity的Class
	 * @param params			跳转所带的参数
	 */
	public static void switchTo(Activity activity,Class<? extends Activity> targetActivity,
	        Map<String,Object> params){
			Intent intent = new Intent(activity,targetActivity);
			if( null != params ){
				for(Map.Entry<String, Object> entry : params.entrySet()){
					IntentUtil.setValueToIntent(intent, entry.getKey(), entry.getValue());
				}
			}
			switchTo(activity, intent);
	}
	
	/**
	 * </br><b>description :</b>显示Toast消息，并保证运行在UI线程中
	 * </br><b>time :</b>		2012-7-10 下午08:36:02
	 * @param activity
	 * @param message
	 */
	public static void show(final Activity activity,final String message){
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
