package com.helloarron.touchmath.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.utils.Util;

/**
 * Created by arron on 2017/3/10.
 */

public class BaseActivity extends FragmentActivity {
    private ActivityTack tack = ActivityTack.getInstanse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tack.addActivity(this);

        // 设置通知栏颜色
        Util.setWindowStatusBarColor(this, getResources().getColor(R.color.text_teal_400));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void finish() {
        super.finish();
        tack.removeActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (Const.auto_inject) {
            InjectUtil.inject(this);
        }
    }
}
