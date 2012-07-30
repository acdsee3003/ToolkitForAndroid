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
package chen.android.toolkit.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;


/**
 * </br><b>name : </b>		DownloadTask
 * </br><b>description :</b>TODO
 * </br>@author : 			桥下一粒砂
 * </br><b>e-mail : </b>	chenyoca@gmail.com
 * </br><b>weibo : </b>		@桥下一粒砂
 * </br><b>date : </b>		2012-7-29 下午4:52:55
 *
 */
public class DownloadTask{

	class TaskConfig{
		public int taskId;
		public int threadId;
		public String targetURL;
		public String savePath;
		public boolean isFailure;
		public InputStream result;
	}
	
	/**
	 * </br><b>name : </b>		DownloadCallback
	 * </br><b>description :</b>TODO
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-7-29 下午4:53:25
	 *
	 */
	public interface DownloadCallback {
		void onStart(int taskId);
		void onProcess(int taskId,int progress);
		void onCancle(int taskId);
		void onFinish(int taskId);
	};
	
	private static final int THREAD_PER_TASK = 10;
	
	private List<TaskConfig> mTaskConfigs = new ArrayList<DownloadTask.TaskConfig>();
	
	private List<DownloadHandler> mDownloadHandlers = new ArrayList<DownloadTask.DownloadHandler>();
	
	private DownloadCallback mCallback;
	
	public DownloadTask(DownloadCallback callback){
		mCallback = callback;
	}
	
	/**
	 * </br><b>title : </b>		TODO
	 * </br><b>description :</b>TODO
	 * </br><b>time :</b>		2012-7-29 下午5:04:45
	 * @param targetUrl
	 * @param savePath
	 * @return
	 */
	public int addTask(String targetUrl,String savePath){
		TaskConfig config = new TaskConfig();
		config.targetURL = targetUrl;
		config.savePath = savePath;
		config.isFailure = false;
		config.result = null;
		config.threadId = 0;
		config.taskId = mTaskConfigs.size() - 1;
		mTaskConfigs.add(config);
		return config.taskId;
	}
	
	public void start(){
		int threads = mTaskConfigs.size() / THREAD_PER_TASK;
		for(int i=0;i<threads;i++){
			DownloadHandler handle = new DownloadHandler();
			int startIndex = i * THREAD_PER_TASK;
			int endIndex = (i+1) * THREAD_PER_TASK;
			endIndex = (mTaskConfigs.size() < endIndex ? mTaskConfigs.size() : endIndex) - 1 ;
			
			//任务所在的线程ID
			for(TaskConfig c : mTaskConfigs) c.threadId = i;
			
			//执行任务
			handle.execute(mTaskConfigs.subList(startIndex, endIndex).toArray(new TaskConfig[mTaskConfigs.size()]));
			
			mDownloadHandlers.add(handle);
		}
	}
	
	public void cancle(int taskId){
		int threadId = 0;
		for(TaskConfig t : mTaskConfigs) threadId = t.taskId == taskId ? t.threadId : threadId;
		
//		mDownloadHandlers.get(threadId)
		
	}
	
	/**
	 * </br><b>name : </b>		DownloadHandler
	 * </br><b>description :</b>处理下载线程
	 * </br>@author : 			桥下一粒砂
	 * </br><b>e-mail : </b>	chenyoca@gmail.com
	 * </br><b>weibo : </b>		@桥下一粒砂
	 * </br><b>date : </b>		2012-7-29 下午5:06:53
	 *
	 */
	class DownloadHandler extends AsyncTask<TaskConfig, Integer, TaskConfig[]>{

		private static final String TAG = "DownloadHandler";
		
		private List<TaskConfig> mAvailableTask = new ArrayList<TaskConfig>();
		
		private TaskConfig[] mTotalTaskConfigs;
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected TaskConfig[] doInBackground(TaskConfig... params) {
			if( 0 == params.length ){
				throw new RuntimeException("Must set download task config!");
			}
			mTotalTaskConfigs = params;
			for(TaskConfig config : mTotalTaskConfigs){
				try {
					config.result = HttpConnection.get(config.targetURL);
					mAvailableTask.add(config);
				} catch (MalformedURLException e) {
					Log.e(TAG, String.format("Malformed Url (%s) !",config.targetURL));
					config.isFailure = true;
					continue;
				} catch (IOException e) {
					Log.e(TAG, String.format("Connecte to  Url (%s) failure ! Error message : %s .",
							config.targetURL,e.getCause()));
					config.isFailure = true;
					continue;
				}
			}
			return mAvailableTask.toArray(new TaskConfig[mAvailableTask.size()]);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(TaskConfig[] result) {
			for(TaskConfig task : result){
				
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			for(int i=0;i<mTotalTaskConfigs.length;i++){
				if( !mTotalTaskConfigs[i].isFailure ){
					mCallback.onProcess(mTotalTaskConfigs[i].taskId, values[i]);
				}else{
					mCallback.onProcess(mTotalTaskConfigs[i].taskId, -1);
				}
			}
		}
		
	}
}
