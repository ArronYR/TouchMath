package com.helloarron.touchmath.views;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.StyleRes;
import android.view.Window;

import com.helloarron.touchmath.R;

/**
 * Created by arron on 2017/3/11.
 */

public class BaseAlertDialog extends AlertDialog {

    public BaseAlertDialog(Context context) {
        super(context);
        Window window = getWindow();
        window.setWindowAnimations(R.style.CustomDialog);
    }

    protected BaseAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BaseAlertDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        Window window = getWindow();
        window.setWindowAnimations(R.style.CustomDialog);
    }
}
