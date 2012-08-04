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

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.apkits.android.common.StreamConverter;
import com.apkits.android.crypt.HashCrypt;
import com.apkits.android.crypt.HashCrypt.CryptType;
import com.apkits.android.network.HttpConnection;
import com.apkits.android.resource.BitmapScaleUitl;

/**
 * </br><b>name : </b>		WebImageView
 * </br><b>description :</b>TODO
 * </br>@author : 			桥下一粒砂
 * </br><b>e-mail : </b>	chenyoca@gmail.com
 * </br><b>weibo : </b>		@桥下一粒砂
 * </br><b>date : </b>		2012-8-4 下午4:01:38
 *
 */
public class WebImageView extends ImageView {

	/**
	 * 调试信息输出
	 */
	private static final String TAG = "WebImageView";
	
	/**
	 * Andorid环境上下文
	 */
	private Context mContext;
	
	/**
	 * 重置图片大小
	 */
	private int[] mResize = {-1,-1};
	
	/**
	 * 下载更新回调
	 */
	private Handler mUpdateCallback = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Bitmap img = (Bitmap) msg.obj;
			if( mResize[0] > 10 && mResize[1] > 10){
				img = BitmapScaleUitl.extractThumbnail(img, mResize[0], mResize[1]);
			}
			WebImageView.this.setImageBitmap(img);
		}
	};
	
	/**
	 * </br><b>title : </b>		设置图片大小
	 * </br><b>description :</b>强制大小，不按比例缩放。图片宽高必须大小10。
	 * </br><b>time :</b>		2012-8-4 下午4:42:50
	 * @param width
	 * @param height
	 */
	public void setImageSize(int width,int height){
		mResize = new int[]{width,height};
	}
	
	/**
	 * </br><b>description : </b>	在XML中构建
	 * @param context
	 * @param attrs
	 */
	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	/**
	 * </br><b>description : </b>	在Java代码中构建
	 * @param context
	 */
	public WebImageView(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
	 * </br><b>title : </b>		TODO
	 * </br><b>description :</b>一旦设置，即从网络下载图片。
	 * </br><b>time :</b>		2012-8-4 下午4:03:19
	 * @param url
	 */
	public void setImageUrl(final String url){
		String tempFile = HashCrypt.encode(CryptType.SHA1, url);
		if(mContext.getFileStreamPath(tempFile).exists()){
			try {
				this.setImageBitmap(StreamConverter.convertBitmap(mContext.openFileInput(tempFile)));
			}catch (IOException e) {
				Log.e(TAG,String.format("Cannot read the temp image (%) !", tempFile));
			}
		}else{
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						InputStream is = HttpConnection.get(url);
						Message msg = new Message();
						msg.obj = StreamConverter.convertBitmap(is);
						mUpdateCallback.sendMessage(msg);
					} catch (IOException e) {
						Log.e(TAG,String.format("Cannot fetch image from url (%) !", url));
					}
				}
			}).start();
		}
	}


}
