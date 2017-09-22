package com.helloarron.touchmath.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.helloarron.touchmath.R;

/**
 * Created by arron on 2017/3/11.
 */

public class VersionDialog extends BaseAlertDialog {
    Context context;

    public VersionDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected VersionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public VersionDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version);
    }
}
