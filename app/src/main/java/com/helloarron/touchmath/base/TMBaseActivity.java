package com.helloarron.touchmath.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.helloarron.touchmath.R;


/**
 * Created by arron on 2017/3/10.
 */

public abstract class TMBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
    }

    public abstract void initView();

    /**
     * 左边返回按钮为空
     */
    public void setLeftIconGone() {
        ImageView backV = (ImageView) findViewById(R.id.left_icon);
        if (backV != null) {
            backV.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String text) {
        TextView titleT = (TextView) findViewById(R.id.title);
        if (titleT != null) {
            titleT.setText(text);
        }
    }

    /**
     * 设置左面的点击事件
     */
    public void setLeftAction(int resId, View.OnClickListener listener) {
        ImageView leftI = (ImageView) findViewById(R.id.left_icon);
        if (leftI != null) {
            if (resId != -1) {
                leftI.setVisibility(View.VISIBLE);
                leftI.setImageResource(resId);
                leftI.setOnClickListener(listener);
            } else {
                leftI.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置左面的点击事件
     */
    public void setRightVideoAction(int resId, View.OnClickListener listener) {
        ImageView rightI = (ImageView) findViewById(R.id.right_icon_video);
        if (rightI != null) {
            if (resId != -1) {
                rightI.setVisibility(View.VISIBLE);
                rightI.setImageResource(resId);
                rightI.setOnClickListener(listener);
            } else {
                rightI.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置左面的点击事件
     */
    public void setRightRulesAction(int resId, View.OnClickListener listener) {
        ImageView rightI = (ImageView) findViewById(R.id.right_icon_rules);
        if (rightI != null) {
            if (resId != -1) {
                rightI.setVisibility(View.VISIBLE);
                rightI.setImageResource(resId);
                rightI.setOnClickListener(listener);
            } else {
                rightI.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // modalInAnim();
    }

    @Override
    public void finish() {
        super.finish();
//        popOutAnim();
    }

    public void finishWithoutAnim() {
        super.finish();
    }

    public void finishAnim() {
        super.finish();
        modalOutAnim();
    }

    @SuppressLint("NewApi")
    public void popInAnim() {
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    /**
     * 左右滑动切换页面的动画 后退
     */
    @SuppressLint("NewApi")
    public void popOutAnim() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    /**
     * 向上推出时切换的动画 进入
     */
    @SuppressLint("NewApi")
    public void modalInAnim() {
        overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out);
    }

    /**
     * 向上推出时切换的动画 后退
     */
    public void modalOutAnim() {
        overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
