package com.helloarron.touchmath.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.Const;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by arron on 2017/3/12.
 */

public class Util {

    /**
     * 保存图片
     *
     * @param context
     * @param bmp
     * @param isPNG
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, Boolean isPNG) {
        // 判断sd卡是否存在
        String sdState = Environment.getExternalStorageState();
        // 检查SD卡是否可用
        if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, context.getString(R.string.no_sdcard), Toast.LENGTH_SHORT).show();
            return;
        }
        File appDir = new File(Const.SAVE_REAL_PATH);
        if (!appDir.exists()) {
            boolean mkdir = appDir.mkdir();
            if (!mkdir) {
                Toast.makeText(context, context.getString(R.string.failed_create_folder), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        if (isPNG) {
            fileName = System.currentTimeMillis() + ".png";
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (isPNG) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                bmp = Util.getRoundCornerImage(bmp, 0);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            fos.flush();
            fos.close();
            Toast.makeText(context, context.getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    /**
     * 画成圆角图片
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels) {
        // 创建一个和原始图片一样大小位图
        Bitmap roundCornerImage = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 创建带有位图roundConnerImage的画布
        Canvas canvas = new Canvas(roundCornerImage);
        // 创建画笔
        Paint paint = new Paint();
        // 创建一个和原始图片一样大小的矩形
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        // 去锯齿
        paint.setAntiAlias(true);
        // 画一个和原始图片一样大小的圆角矩形
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        // 设置相交模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 把图片画到矩形去
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        // 引时圆角区域为透明，给其填充白色
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawRect(rectF, paint);

        return roundCornerImage;
    }

    /**
     * 设置通知栏颜色
     * @param activity
     * @param colorResId
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        Window window = activity.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(colorResId);
        }

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }
}
