package com.helloarron.touchmath.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arron on 2017/3/11.
 */

public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;

    // 创建Cache
    private LruCache<String, Bitmap> mCaches;

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    private ImageLoader() {
        // 获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 在每次存入缓存的时候调用,返回Bitmap大小
                return value.getByteCount();
            }
        };
    }


    /**
     * 添加到缓存
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToCache(String key, Bitmap bitmap, Boolean force) {
        if (force || getBitmapFromCache(key) == null) {
            mCaches.put(key, bitmap);
        }
    }

    /**
     * 从缓存中获取Bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromCache(String key) {
        return mCaches.get(key);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    /**
     * 使用多线程加载图片
     *
     * @param imageView
     * @param url
     */
    public void showImgByThread(ImageView imageView, final String url) {
        mImageView = imageView;
        mUrl = url;
        // 从缓存中取出对应图片
        Bitmap bitmap = getBitmapFromCache(mUrl);
        // 如果缓存中没有，则从网络加载
        if (bitmap == null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Bitmap bitmap = getBitmapFromUrl(mUrl);
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    // 从网络获取图片并加入缓存
                    if (bitmap != null) {
                        addBitmapToCache(mUrl, bitmap, false);
                    }
                    mHandler.sendMessage(message);
                }
            }.start();
        } else {
            mImageView.setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用异步形式加载图片
     *
     * @param imageView
     * @param url
     */
    public void showImgByAsync(ImageView imageView, final String url) {
        // 从缓存中取出对应图片
        Bitmap bitmap = getBitmapFromCache(url);
        // 如果缓存中没有，则从网络加载
        if (bitmap == null) {
            new ImageAsyncTask(imageView, url).execute(url);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String mUrl;

        public ImageAsyncTask(ImageView imageView, String url) {
            this.mImageView = imageView;
            this.mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            // 从网络获取图片并加入缓存
            Bitmap bitmap = getBitmapFromUrl(url);
            if (bitmap != null) {
                addBitmapToCache(url, bitmap, false);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
