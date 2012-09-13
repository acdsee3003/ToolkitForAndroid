/**
 * Copyright (C) 2012 ToolkitForAndroid Project
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
package com.apkits.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * <ul>
 * <li><b>name : </b>       HolderAdapter</li>
 * <li><b>description :</b> 实现HolderView模式的Adapter。</li>
 * <li><b>author : </b>     桥下一粒砂           </li>
 * <li><b>e-mail : </b>     chenyoca@gmail.com  </li>
 * <li><b>weibo : </b>      @桥下一粒砂          </li>
 * <li><b>date : </b>       2012-7-10 下午10:56:11</li>
 * </ul>
 */
public class HolderAdapter<E> extends BaseAdapter {

	/**
	 * </br><b>name : </b>		ViewHolder
	 * </br><b>description :</b>一个持有View引用对象的静态类，用以减少View的创建次数
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-7-14 上午12:31:56
	 *
	 */
	private static class ViewHolder{
		public View view;
	}
	
	/** 数据缓存 */
	private List<E> mDataCache;
	
	/** 用于从XML文件中创建Layout */
	private LayoutInflater mInflater;
	
	/** View创建器 */
	private ViewCreator<E> mCreator;
	
	/**
	 * </br><b>description : </b>	创建HolderAdapter，需要给定View创建接口。
	 * @param inflater
	 * @param creator
	 */
	public HolderAdapter(LayoutInflater inflater,ViewCreator<E> creator){
		mInflater = inflater;
		mCreator = creator;
	}

	/**
	 * </br><b>title : </b>		更新数据集
	 * </br><b>description :</b>更新数据集
	 * </br><b>time :</b>		2012-7-10 下午11:06:40
	 * @param data
	 */
	public void update(List<E> data){
		clear();
		mDataCache = data;
		notifyDataSetChanged();
	}
	
	/**
	 * <b>description :</b>		清除缓存数据
	 * </br><b>time :</b>		2012-8-10 下午9:52:56
	 */
	public void clear(){
		if( null != mDataCache ){
			mDataCache.clear();
		}
	}
	/**
	 * <b>description :</b>添加数据集，向数据缓存中添加多个元素。
	 * </br><b>time :</b>		2012-7-17 下午10:19:45
	 * @param set
	 */
	public void add(List<E> set){
	    if( null == mDataCache ) mDataCache = new ArrayList<E>();
	    mDataCache.addAll(set);
	    notifyDataSetChanged();
	}
	
	/**
	 * <b>description :</b>		交换两个元素的位置
	 * </br><b>time :</b>		2012-9-4 下午4:46:33
	 * @param src
	 * @param target
	 */
	public void exchange(int src,int target){
		if (src > target) {
			int temp = target;
			target = src;
			target = temp;
		}
		E endObject = getItem(target);
		E startObject = getItem(src);
		mDataCache.set(src, endObject);
		mDataCache.set(target, startObject);
		notifyDataSetChanged();
		
	}

	/**
	 * </br><b>description :</b>添加数据元素，向数据缓存中添加单个元素。
	 * </br><b>time :</b>		2012-7-17 下午10:19:51
	 * @param item
	 */
	public void add(E item){
	    if( null == mDataCache ) mDataCache = new ArrayList<E>();
	    mDataCache.add(item);
	    notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return null == mDataCache ? 0 : mDataCache.size();
	}

	@Override
	public E getItem(int position) {
		return null == mDataCache ? null : mDataCache.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Holder View模式实现
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			convertView = mCreator.createView(mInflater, position,getItem(position));
			holder.view = convertView;
			convertView.setTag(holder);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			mCreator.updateView(holder.view, position, getItem(position));
		}
		return convertView;
	}
}
