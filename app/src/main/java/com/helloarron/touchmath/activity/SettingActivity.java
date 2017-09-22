package com.helloarron.touchmath.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.IocContainer;
import com.helloarron.touchmath.base.TMBaseActivity;
import com.helloarron.touchmath.utils.TMPreference;
import com.helloarron.touchmath.views.ChangLanguageDialog;
import com.helloarron.touchmath.views.VersionDialog;

public class SettingActivity extends TMBaseActivity implements View.OnClickListener {

    private TMPreference preference;

    private LinearLayout llLang, llVersion, llSupport;
    private TextView tvLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle(getString(R.string.system_title));
        setLeftAction(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initView() {
        llLang = (LinearLayout) findViewById(R.id.ll_lang);
        llVersion = (LinearLayout) findViewById(R.id.ll_version);
        llSupport = (LinearLayout) findViewById(R.id.ll_support);
        tvLang = (TextView) findViewById(R.id.tv_lang);

        preference = IocContainer.getShare().get(TMPreference.class);
        preference.load();
        if (preference.getLangType() == 2) {
            tvLang.setText(getString(R.string.english));
        } else {
            tvLang.setText(getString(R.string.chinese));
        }

        llLang.setOnClickListener(this);
        llVersion.setOnClickListener(this);
        llSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_lang:
                ChangLanguageDialog dialog = new ChangLanguageDialog(this);
                dialog.setOnClickResult(new ChangLanguageDialog.OnClickResult() {
                    @Override
                    public void click() {
                        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                dialog.show();
                break;
            case R.id.ll_version:
                VersionDialog versionDialog = new VersionDialog(this);
                versionDialog.show();
                break;
            case R.id.ll_support:
                Intent intent = new Intent(SettingActivity.this, SupportActivity.class);
                startActivity(intent);
                break;
        }
    }
}
