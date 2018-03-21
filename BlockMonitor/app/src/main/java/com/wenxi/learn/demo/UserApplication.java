package com.wenxi.learn.demo;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by vivian on 2017/11/13.
 * User customized Application
 */
public class UserApplication extends Application {
    private UserApplication mInstance;

    /**
     * single instance
     *
     * @return instance
     */
    public UserApplication getInstance() {
        if (mInstance == null) {
            return new UserApplication();
        }
        return mInstance;
    }

    /**
     * 在创建应用程序时调用，可以在这里实例化应用程序的单例，
     * 创建和实例化任何应用程序状态变量或共享资源等
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 程序终止时调用，如果需要及时的释放资源可以在各个Activity中。
     * onTerminate只有在程序终止时才会调用，程序退到后台时有可能不会终止。
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 当系统处于资源匮乏时，具有良好行为的应用程序可以释放额外的内存。
     * 这个方法一般只会在后台进程已经终止，但是前台应用程序仍然缺少内存时调用。
     * 我们可以重写这个程序来清空缓存或者释放不必要的资源
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 作为onLowMemory的一个特定于应用程序的替代选择，在android4.0时引入，
     * 在程序运行时决定当前应用程序应该尝试减少其内存开销时（通常在它进入后台时）调用
     * 它包含一个level参数，用于提供请求的上下文
     * level 的值: ComponentCallbacks2.TRIM_MEMORY_...
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    /**
     * 在配置改变时，应用程序对象不会被终止和重启。
     * 如果应用程序使用的值依赖于特定的配置，则重写这个方法来重新加载这些值，或者在应用程序级别处理这些值的改变
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
