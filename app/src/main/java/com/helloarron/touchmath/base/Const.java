package com.helloarron.touchmath.base;

import android.os.Environment;

/**
 * 一些系统常量的定义
 * Created by arron on 2017/3/10.
 */

public class Const {

    public static boolean auto_inject = true;

    public static int APP_VERSION = 1;

    public final static int LOADING = 0x0;
    public final static int LOADED = 0x1;

    // 首先默认个文件保存路径
    // 保存到SD卡
    public static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
    // 保存的确切位置
    public static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/TouchMath";
}
