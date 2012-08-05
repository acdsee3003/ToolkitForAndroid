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

import java.io.FileOutputStream;
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
import com.apkits.android.crypt.HashEncrypt;
import com.apkits.android.crypt.HashEncrypt.CryptType;
import com.apkits.android.network.HttpConnection;
import com.apkits.android.resource.BitmapScaleUitl;

/**
 * </br><b>name : </b>		WebImageView
 * </br><b>description :</b>继承自ImageView，支持自动加载网络图片，并具有缓存图片功能。
 * 						控件首先检查缓存（应用私有目录）中是否存在URL所指向的图片，如果存在，则直接读取缓存中的图片。
 * 						如果不存在，则创建一个线程下载网络图片。
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
			try {
				Bitmap img = StreamConverter.convertBitmap(mContext.openFileInput(msg.obj.toString()));
				if( mResize[0] > 10 && mResize[1] > 10){
					img = BitmapScaleUitl.extractThumbnail(img, mResize[0], mResize[1]);
				}
				WebImageView.this.setImageBitmap(img);
			} catch (IOException e) {
				Log.e(TAG,"Cannot convert file to image !");
			} 
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
	 * </br><b>title : </b>		设置网络图片地址
	 * </br><b>description :</b>首先从缓存文件夹中读取，再从网络下载。
	 * </br><b>time :</b>		2012-8-4 下午4:03:19
	 * @param url
	 */
	public void setImageUrl(final String url){
		//对URL以SHA-1加密并命名
		final String tempFile = HashEncrypt.encode(CryptType.SHA1, url);
		//是否在缓存
		if(mContext.getFileStreamPath(tempFile).exists()){
			Message msg = new Message();
			msg.obj = tempFile;
			mUpdateCallback.sendMessage(msg);
		}else{
			//下载
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						InputStream is = HttpConnection.get(url);
						
						FileOutputStream os = mContext.openFileOutput(tempFile, Context.MODE_PRIVATE);
						byte[] bytes = new byte[ 1 * 512 ]; 
						int bufferSize = 0;
						while((bufferSize = is.read(bytes)) != -1){
							os.write(bytes, 0, bufferSize);
						}
						os.close();
						is.close();
					} catch (IOException e) {
						Log.e(TAG,String.format("Cannot fetch image from url (%) !", url));
					}
					
					Message msg = new Message();
					msg.obj = tempFile;
					mUpdateCallback.sendMessage(msg);
				}
			}).start();
		}
	}


}
