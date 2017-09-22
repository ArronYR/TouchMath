package com.helloarron.touchmath.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcel;

import com.google.gson.Gson;
import com.helloarron.touchmath.base.IocContainer;

/**
 * Created by arron on 2017/3/11.
 */

public class Preference implements android.os.Parcelable {

    private static final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Preference load() {
        Context context = IocContainer.getShare().getApplicationContext();
        SharedPreferences p = context.getSharedPreferences(getClass().getName(), Context.MODE_WORLD_READABLE);
        String infos = p.getString("TouchMath", "");
        Gson gson = new Gson();
        Preference preference = gson.fromJson(infos, getClass());
        if (preference != null) {
            BeanUtil.copyBeanWithOutNull(preference, this);
        }
        return this;
    }

    public Preference commit() {
        Context context = IocContainer.getShare().getApplicationContext();
        SharedPreferences p = context.getSharedPreferences(getClass().getName(), Context.MODE_WORLD_READABLE);
        String json = new Gson().toJson(this);
        p.edit().putString("TouchMath", json).commit();
        return this;
    }

    /**
     * 通知数据修改
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * 注册数据观察者
     *
     * @param observer
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    /**
     * 取消监听
     *
     * @param observer
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
}
