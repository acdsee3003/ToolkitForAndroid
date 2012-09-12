package com.apkits.android.ui;

import android.view.LayoutInflater;
import android.view.View;

/**
 * <ul>
 * <li><b>name : </b>       ViewCreator</li>
 * <li><b>description :</b> 创建View和更新View的接口</li>
 * <li><b>author : </b>     桥下一粒砂           </li>
 * <li><b>e-mail : </b>     chenyoca@gmail.com  </li>
 * <li><b>weibo : </b>      @桥下一粒砂          </li>
 * <li><b>date : </b>       2012-7-14 上午12:35:05</li>
 * @param <E>
 * </ul>
 */
public interface ViewCreator<E> {
    /**
     * <b>description :</b>			创建View,HolderAdapter需要创建View时，会调用此方法创建View。
     * </br><b>time :</b> 			2012-7-10 下午11:03:47
     * 
     * @param inflater
     * @param position
     * @param data
     * @return
     */
    View createView(LayoutInflater inflater, int position, E data);

    /**
     * <b>description :</b>			更新View 
     * </br><b>time :</b> 			2012-7-10 下午11:04:30
     * @param view
     * @param position
     * @param data
     */
    void updateView(View view, int position, E data);
};