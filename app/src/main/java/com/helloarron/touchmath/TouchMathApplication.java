package com.helloarron.touchmath;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.helloarron.touchmath.base.IocContainer;
import com.helloarron.touchmath.utils.TMPreference;

import java.util.Locale;

/**
 * Created by arron on 2017/3/11.
 */

public class TouchMathApplication extends Application implements
        Thread.UncaughtExceptionHandler {

    private static TouchMathApplication instance;

    public static TouchMathApplication getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        IocContainer.getShare().initApplication(this);
        TMPreference per = IocContainer.getShare().get(TMPreference.class);
        per.load();

        // 获得res资源对象
        Resources resources = this.getResources();
        // 获得设置对象
        Configuration config = resources.getConfiguration();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (per.langType == 2) {
            config.locale = Locale.ENGLISH;
        } else {
            // 简体中文
            config.locale = Locale.CHINA;
        }
        resources.updateConfiguration(config, dm);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
