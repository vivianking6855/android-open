# Android Block Monitor

Android版本：Android 4.1 API 16以上支持

# 原理

使用Choreographer.FrameCallback 

Choreographer类的FrameCallback是系统每隔16ms发出VSYNC信号，来通知界面进行重绘、渲染的回调。利用两次回调的时间差十分超出16ms来判断卡顿

当每一帧被渲染时会触发回调FrameCallback， FrameCallback回调void doFrame (long frameTimeNanos)函数。

检查两次doFrame的间隔，如果大于16.6ms说明发生了卡顿

# 使用