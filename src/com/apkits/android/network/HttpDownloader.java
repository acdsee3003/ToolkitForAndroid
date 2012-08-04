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
package com.apkits.android.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * </br><b>name : </b>		DownloadTask
 * </br><b>description :</b>下载工具
 * </br>@author : 			桥下一粒砂
 * </br><b>e-mail : </b>	chenyoca@gmail.com
 * </br><b>weibo : </b>		@桥下一粒砂
 * </br><b>date : </b>		2012-8-4 上午10:45:21
 *
 */
public class HttpDownloader {

	/**
	 * </br><b>name : </b>		DownloadTaskListener
	 * </br><b>description :</b>下载任务过程监听
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-8-4 上午10:36:49
	 *
	 */
	public interface DownloadTaskListener {
		
		/**
		 * 在下载过程中被回调。
		 * @param taskId 任务ID.此任务ID在添加下载addTask()任务时被返回。
		 * @param progress 下载进度
		 */
		void onProcess(int taskId,int progress);
		
		/**
		 * 下载任务完成后被回调
		 * @param taskId 任务ID。此任务ID在添加下载addTask()任务时被返回。
		 */
		void onCompleted(int taskId);
		
		/**
		 * 下载失败时被回调。
		 * @param taskId 任务ID。此任务ID在添加下载addTask()任务时被返回。
		 * @param message 失败原因
		 */
		void onFailed(int taskId,String message);
		
		/**
		 * 被取消下载时回调。
		 * @param taskId 任务ID
		 */
		void onCancle(int taskId);
	}
	
	/**
	 * </br><b>name : </b>		TaskDescription
	 * </br><b>description :</b>任务描述
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-8-4 上午10:38:08
	 *
	 */
	private class TaskDescription {
		/**
		 * 任务ID
		 */
		public int id;
		
		/**
		 * URL
		 */
		public String url;
		
		/**
		 * 保存路径
		 */
		public String savePath;
	}
	
	/**
	 * 下载线程池
	 */
	private List<DownloadThread> mThreadPool = new ArrayList<DownloadThread>();
	
	/**
	 * 下载监听
	 */
	private DownloadTaskListener mListener;
	
	/**
	 * 分配任务ID
	 */
	private int mTaskId;
	
	/**
	 * </br><b>description : </b>	创建下载任务。必须设置一个回调接口。
	 * @param listener
	 */
	public HttpDownloader(DownloadTaskListener listener) {
		if( null == listener ) throw new RuntimeException("Download task callback listener cannot be null !");
		mListener = listener;
	}

	/**
	 * 添加下载任务。
	 * 执行此方法则创建一个下载线程。
	 * @param url 下载URL
	 * @param savePath 保存地址。请保证保存路径正确可读。
	 * @return 任务ID
	 */
	public int addTask(String url, String savePath) {
		TaskDescription task = new TaskDescription();
		task.id = mTaskId;
		task.savePath = savePath;
		task.url = url;
		//创建下载线程
		DownloadThread thread = new DownloadThread(task);
		new Thread(thread).start();
		mThreadPool.add(thread);
		return mTaskId++;
	}
	
	/**
	 * </br><b>title : </b>		取消下载任务
	 * </br><b>description :</b>TODO
	 * </br><b>time :</b>		2012-8-4 上午10:39:30
	 * @param id
	 */
	public void cancleTask(int id){
		mThreadPool.get(id).cancleThread();
	}

	/**
	 * </br><b>name : </b>		DownThread
	 * </br><b>description :</b>下载线程
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-8-4 上午10:35:58
	 *
	 */
	private class DownloadThread implements Runnable {
		
		/**
		 * 数据块大小
		 */
		private final static int BLOCK_SIZE = 1024;
		
		/**
		 * 下载任务描述
		 */
		private TaskDescription mTask;
		
		/**
		 * 是否取消下载
		 */
		private Boolean mIsCancle = false;
		
		/**
		 * </br><b>description : </b>	创建下载线程
		 * @param task
		 */
		public DownloadThread(TaskDescription task) {
			mTask = task;
		}
		
		/**
		 * 取消下载线程
		 */
		public void cancleThread(){
			synchronized(mIsCancle){
				mIsCancle = false;
			}
		}
		
		@Override
		public void run() {
			try {
				URL url = new URL(mTask.url);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
					RandomAccessFile accessFile = new RandomAccessFile(mTask.savePath, "rwd");
					byte[] buffer = new byte[BLOCK_SIZE];
					int bufferSize = 0;
					int resourceSize = conn.getContentLength() - 1;
					accessFile.setLength(resourceSize);
					long downloadedSize = 0L;
					BufferedInputStream cache = new BufferedInputStream(conn.getInputStream());
					while ((bufferSize = cache.read(buffer)) != -1) {
						if(mIsCancle) break;
						accessFile.write(buffer, 0, bufferSize);
						downloadedSize += bufferSize;
						int progress = Math.round((float) downloadedSize / resourceSize * 100);
						mListener.onProcess(mTask.id, progress);
					}
					cache.close();
					accessFile.close();
					if(mIsCancle){
						mListener.onCancle(mTask.id);
						//如果取消，把下载文件删除
						new File(mTask.savePath).delete();
					}else{
						mListener.onCompleted(mTask.id);
					}
				} else {
					mListener.onFailed(mTask.id, conn.getResponseMessage());
				}
				//断开连接
				conn.disconnect();
			} catch (IOException e) {
				mListener.onFailed(mTask.id, e.getMessage());
			}
		}

	}

}