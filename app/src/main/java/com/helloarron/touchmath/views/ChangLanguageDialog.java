package com.helloarron.touchmath.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.View;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.IocContainer;
import com.helloarron.touchmath.utils.TMPreference;

import java.util.Locale;

/**
 * Created by arron on 2017/3/11.
 */

public class ChangLanguageDialog extends BaseAlertDialog {
    Context context;
    OnClickResult onClickResult;

    public ChangLanguageDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected ChangLanguageDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_language);

        findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.english).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeLanguage(2);
                if (onClickResult != null) {
                    onClickResult.click();
                }
                dismiss();
            }
        });

        findViewById(R.id.chinese).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeLanguage(1);
                if (onClickResult != null) {
                    onClickResult.click();
                }
                dismiss();
            }
        });
    }

    public interface OnClickResult {
        void click();
    }

    public OnClickResult getOnClickResult() {
        return onClickResult;
    }

    public void setOnClickResult(OnClickResult onClickResult) {
        this.onClickResult = onClickResult;
    }

    private void changeLanguage(int type) {
        TMPreference preference = IocContainer.getShare().get(TMPreference.class);
        preference.load();

        // 获得res资源对象
        Resources resources = context.getResources();
        // 获得设置对象
        Configuration config = resources.getConfiguration();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (type == 1) {
            config.locale = Locale.CHINA; // 简体中文
        } else if (type == 2) {
            config.locale = Locale.ENGLISH; // 英语
        }
        resources.updateConfiguration(config, dm);
        preference.langType = type;
        preference.commit();
    }
}
