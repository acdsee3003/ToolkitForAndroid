android-common-tookit
=====================

 * A common tookit package for android.

 * 适用于Android项目的通用工具类的集合。

### 使用

	大部分是工具类，以静态方法方式调用。其中adapter包中的类是为各类Adapter应用场景设计。
	
## 使用范例

### Adapter

	adapter包设计了三类Adapter（继承自官方的Adapter）：CommonAdapter、CommonPagerAdapter、HolderAdapter。
	CommonAdapter适用于创建例如主界面功能菜单等不需要Item更新的布局。CommonPagerAdaptert为PagerView而设计，提供强制刷新功能。
	HolderAdapter适用于ListView等大量数据显示。其中HolderViewFiller是整合HolderAdapter的辅助工具类。
	ViewCreator是以上Adapter的核心接口，它提供一个统一的创建View、更新View、回收View等操作的接口。
	

### Common

	
### Encrypt


### Resource


### System
	

## 开源协议 Apache License 2.0

The code of this project is released under the Apache License 2.0, see [LICENSE](https://github.com/chenyoca/async-http-connection-core/blob/master/LICENSE)

