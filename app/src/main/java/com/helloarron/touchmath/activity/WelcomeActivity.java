package com.helloarron.touchmath.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.Const;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView imgWelcome;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Const.LOADED) {
                // 清空动画
                imgWelcome.clearAnimation();
                // 跳转到 MainActivity
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            } else {
                imgWelcome.setAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.loading_img_scale));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏设置，隐藏窗口所有装饰
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        initView();
        handler.sendEmptyMessage(Const.LOADING);
        handler.sendEmptyMessageDelayed(Const.LOADED, 3000);
    }

    private void initView() {
        imgWelcome = (ImageView) findViewById(R.id.im_welcome);
    }
}
