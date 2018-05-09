# 简介

AndroidLib 总结了项目中常用的方法和基类。主要包含两部分：appbase和utilslib

# 1. appbase

主要是app常用的一些基类

- activity
    - BaseActiviy：基类，包含initData,initView,loadData逻辑控制
    - BaseMVPActivity ： 加入Presenter持有和释放逻辑
    - BasePermissionActivity : 加入runtime Permission逻辑 
- fragment
    - BaseLazyFragment：支持懒加载
    - BaseMVPLazyFragment：加入Presenter的持有和释放
- helper
    - BaseUIRouter ：加入隐式action安全监测
- view
    - BaseView: 加入onLayout中获取size和初始化paint等
- presenter
    - BasePresenter：加入presenter的释放

---

使用方法：copy到项目中使用或者使用JCenter发布的库：[AppBase](https://bintray.com/vivianwayne1985/maven/AppBase)

---

# 2. utilslib

主要是常用的一些方法库

## base

- AppUtils: App常用方法，比如app version等
- ConvertUtils: bitmap, byte[], stream，时间格式 等转换
- SizeUtils： 尺寸转换，比如px, sp等
- MemoryConstants： KB,MB等常量

## cache

- DiskLruCacheUtils： DiskLruCache 方法封装，支持bitmap, drawable, json等


## device

- DeviceUtils：设备尺寸，设备截屏等
- CPUSample： cpu and process stat 采集库

## file

- CleanUtils： 文件夹clean等
- CloseUtils： safe close 等
- FileUtils： 文件操作
- PathUtils： 获取cache路径
- SDCardUtils： SD卡路径，空间，信息等
- ZipUtils： 压缩，解压文件

## network

- NetworkUtils： 网络状态，打开网络设定等

# security

- EncryptUtils： 加解密
- EncodeUtils： 编码

# time

- TimeUtils，TimeConstants：时间，日期，时间差等

# window

- ToastUtils： 不会重复的toast

---
使用方法 (Android Studio)：copy到项目中使用或者使用JCenter发布的库[utilslib](https://bintray.com/vivianwayne1985/maven/utilslib)

---
# java doc

download [here](https://github.com/vivianking6855/android-library/tree/master/AndroidLib/doc)

需要下载后，在本地点击index.html查看。暂时无法在网页端直接浏览。

# Contributor

vivian sun

# Reference

- [Lazy](https://github.com/l123456789jy/Lazy)
- [base-diskcache](https://github.com/hongyangAndroid/base-diskcache)
- [AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

