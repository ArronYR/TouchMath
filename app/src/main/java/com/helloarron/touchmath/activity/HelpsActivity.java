package com.helloarron.touchmath.activity;

import android.os.Bundle;
import android.view.View;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.TMBaseActivity;

public class HelpsActivity extends TMBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helps);

        setTitle(getString(R.string.helps_title));
        setLeftAction(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initView() {

    }
}
