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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * <ul>
 * <li><b>name : </b>		LongListView		</li>
 * <li><b>description :</b>	展开其高度的ListView				</li>
 * <li><b>author : </b>		桥下一粒砂			</li>
 * <li><b>e-mail : </b>		chenyoca@gmail.com	</li>
 * <li><b>weibo : </b>		@桥下一粒砂			</li>
 * <li><b>date : </b>		2012-8-16 下午10:16:11		</li>
 * </ul>
 */
public class LongListView extends ListView {

	public LongListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public LongListView(Context context) {
		super(context);
	}
	
	/**
	 * <b>description :</b>		将ListView的高度全部展开
	 * </br><b>time :</b>		2012-8-16 下午10:27:45
	 */
	public void expandHeight() {
		int totalHeight = 0;
		ListAdapter adapter = getAdapter();
		for (int i = 0, len = adapter.getCount(); i < len; i++) {
			View listItem = adapter.getView(i, null, this);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = totalHeight + (this.getDividerHeight() * (this.getCount() - 1));
		setLayoutParams(params);
	}
	
}
