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
package com.apkits.android.system;

import android.content.Intent;

/**
 * <ul>
 * <li><b>name : </b>		IntentUtil		</li>
 * <li><b>description :</b>	TODO				</li>
 * <li></b>author : </b>			桥下一粒砂			</li>
 * <li><b>e-mail : </b>		chenyoca@gmail.com	</li>
 * <li><b>weibo : </b>		@桥下一粒砂			</li>
 * <li><b>date : </b>		2012-8-10 下午9:54:03		</li>
 * </ul>
 */
public class IntentUtil {

	/**
	 * </br><b>title : </b>		将值设置到Intent里
	 * </br><b>description :</b>将值设置到Intent里
	 * </br><b>time :</b>		2012-7-8 下午3:31:17
	 * @param intent			Inent对象
	 * @param key				Key
	 * @param val				Value
	 */
	public static void setValueToIntent(Intent intent, String key, Object val) {
		if (val instanceof Boolean)
			intent.putExtra(key, (Boolean) val);
		else if (val instanceof Boolean[])
			intent.putExtra(key, (Boolean[]) val);
		else if (val instanceof String)
			intent.putExtra(key, (String) val);
		else if (val instanceof String[])
			intent.putExtra(key, (String[]) val);
		else if (val instanceof Integer)
			intent.putExtra(key, (Integer) val);
		else if (val instanceof Integer[])
			intent.putExtra(key, (Integer[]) val);
		else if (val instanceof Long)
			intent.putExtra(key, (Long) val);
		else if (val instanceof Long[])
			intent.putExtra(key, (Long[]) val);
		else if (val instanceof Double)
			intent.putExtra(key, (Double) val);
		else if (val instanceof Double[])
			intent.putExtra(key, (Double[]) val);
		else if (val instanceof Float)
			intent.putExtra(key, (Float) val);
		else if (val instanceof Float[])
			intent.putExtra(key, (Float[]) val);
	}
	
	/**
	 * <b>description :</b>		请求获取图片的Intent
	 * </br><b>time :</b>		2012-8-10 下午10:03:02
	 * @return
	 */
	public static Intent getImageIntent(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT,null);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
		return intent;
	}
	
	/**
	 * <b>description :</b>		请求获取正矩形图片，并进行裁剪的Intent。
	 * </br><b>time :</b>		2012-8-10 下午10:04:36
	 * @return
	 */
	public static Intent getCropImageIntent(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT,null);
		intent.setType("image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("return-data", true);
		return intent;
	}
}
