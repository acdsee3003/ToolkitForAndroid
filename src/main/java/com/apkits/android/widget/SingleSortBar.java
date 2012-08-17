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
package com.apkits.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/** 
 * @ClassName: SingleSortBar 
 * @Description: TODO
 * @author yongjia.chen
 * @date 2012-8-17 上午9:28:14
 *  
 */

public class SingleSortBar extends LinearLayout {

    public SingleSortBar(Context context) {
        super(context);
    }

    public SingleSortBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /** 按钮列表 */
    private List<Button> mSortButtons = new ArrayList<Button>();
    
    /** 数据列表 */
    private List< List<String> > mDataArrays;
    
    /** 被选择的索引 */
    private int[] mSelectedIndexs;
    
    /** 提示标题 */
    private static final String TITLE = "请选择：";
    
    /** 选择监听回调 */
    private OnSortItemSelectedListener mListener;
    
    /**
     * @ClassName: OnSortItemSelectedListener 
     * @Description: TODO
     * @author yongjia.chen
     * @date 2012-8-17 下午2:49:08
     *
     */
    public interface OnSortItemSelectedListener{
        void onSelected(int column,int index,String data);
    };
    
    /**
     * @ClassName: ItemClickListener 
     * @Description: 点击处理
     * @author yongjia.chen
     * @date 2012-8-17 下午2:38:49
     *
     */
    private class ItemClickListener implements DialogInterface.OnClickListener{
        private int mIndex = 0;
        public ItemClickListener(int index){
            mIndex = index;
        }
        @Override
        public void onClick(DialogInterface dialog, int which){
            mSelectedIndexs[mIndex] = which;
            String data = mDataArrays.get(mIndex).get(which);
            mSortButtons.get(mIndex).setText(data);
            dialog.dismiss();
            mListener.onSelected(mIndex,which, data);
        }
    };
    
    /**
     * @Title: init
     * @Description:    初始化SortBar
     * @param count      按钮数量     
     * @param bgResId
     */
    public void init(int count,OnSortItemSelectedListener listener){
        mListener = listener;
        mDataArrays = new ArrayList<List<String>>(count);
        mSelectedIndexs = new int[count];
        
        for(int i=0;i<count;i++){
            final Button item = new Button(getContext());
            LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
            mSortButtons.add(item);
            addView(item,param);
            final int position = i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> data = mDataArrays.get(position);
                    String[] cache = new String[data.size()]; 
                    data.toArray(cache);
                    AlertDialog ad = new AlertDialog.Builder(getContext())
                        .setTitle(TITLE) 
                        .setSingleChoiceItems(cache,mSelectedIndexs[position],new ItemClickListener(position))
                        .create();  
                   ad.show();
                }
            });
        }
    }
    
    /**
     * @Title: create
     * @Description:    创建指定索引的SortBar数据条目
     * @param index      
     * @param data
     * @param selectedItem
     */
    public void create(int bgResId,int index,List<String> data,int selectedItem){
        if( index < 0 || index >= mSelectedIndexs.length ) {
            throw new IllegalArgumentException("Out of index for SingleSortBar items!");
        }
        mDataArrays.add(index, data);
        Button item = mSortButtons.get(index);
        item.setText(data.get(selectedItem));
        item.setBackgroundResource(bgResId);
        mSelectedIndexs[index] = selectedItem;
    }
}
