package com.lurencun.android.view;

import android.app.Activity;
import android.os.Handler;

import com.lurencun.android.common.Params;
import com.lurencun.android.system.ActivityUtil;

/**
 * @author : 桥下一粒砂
 * @email  : chenyoca@gmail.com
 * @date   : 2012-12-19
 * @desc   : TODO
 */
public abstract class DelaySwitchActivity extends Activity {

	private Runnable switchCallback;
	private Handler switchHandler;
	private int splashDelay = 3 * 1000;
	private Class<? extends Activity> nextActivity;
	private Params params;
	
	{
		switchCallback = new Runnable(){
			@Override
			public void run() {
				switchToNextView();
			}
		};
		switchHandler = new Handler();
	}
	
	final protected void setSplashDelay(int delayMillis){
		splashDelay = delayMillis;
	}
	
	final protected void setNextActivity(Class<? extends Activity> target){
		nextActivity = target;
	}
	
	final protected void setParams(Params params){
		this.params = params;
	}
	
	final protected void cancelSwitchAction(){
		switchHandler.removeCallbacks(switchCallback);
	}
	
	final protected void switchToNextView(){
		ActivityUtil.switchTo(this, nextActivity, params);
		cancelSwitchAction();
		finish();
	}

	@Override
	final protected void onResume() {
		super.onResume();
		cancelSwitchAction();
		switchHandler.postDelayed(switchCallback, splashDelay);
		onResumeEx();
	}
	
	protected void onResumeEx(){}
}
