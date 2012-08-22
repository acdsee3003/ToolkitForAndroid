package com.apkits.android.ui;

import android.view.LayoutInflater;
import android.view.View;

/**
	 * </br><b>name : </b>		ViewCreator
	 * </br><b>description :</b>创建View和更新View的接口
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-7-14 上午12:35:05
	 * @param <E>
	 */
	public interface ViewCreator<E>{
		/**
		 * </br><b>description :</b>创建View,HolderAdapter需要创建View时，会调用此方法创建View。
		 * </br><b>time :</b>		2012-7-10 下午11:03:47
		 * @param inflater
		 * @param position
		 * @param data
		 * @return
		 */
		View createView(LayoutInflater inflater,int position,E data);
		
		/**
		 * </br><b>description :</b>更新View
		 * </br><b>time :</b>		2012-7-10 下午11:04:30
		 * @param view
		 * @param position
		 * @param data
		 */
		void updateView(View view,int position,E data);
	};