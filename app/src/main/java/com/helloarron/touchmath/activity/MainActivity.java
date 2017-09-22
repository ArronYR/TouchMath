package com.helloarron.touchmath.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.helloarron.touchmath.ATKCertificate;
import com.helloarron.touchmath.BuildConfig;
import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.ActivityTack;
import com.helloarron.touchmath.base.TMBaseActivity;
import com.helloarron.touchmath.utils.ImageLoader;
import com.helloarron.touchmath.views.VideoDialog;
import com.myscript.atk.math.widget.MathWidgetApi;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends TMBaseActivity implements MathWidgetApi.OnConfigureListener, MathWidgetApi.OnRecognitionListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private MathWidgetApi widget;
    private ImageView imRedo, imUndo, imDelete, imExport;
    private Boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityTack.getInstanse().addActivity(this);

        setTitle(getString(R.string.app_name));
        setLeftAction(R.drawable.icon_system, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        setRightVideoAction(R.drawable.icon_video, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo();
            }
        });
        setRightRulesAction(R.drawable.icon_important_tip, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpsActivity.class);
                startActivity(intent);
            }
        });

        initView();
    }


    public void initView() {

        imUndo = (ImageView) findViewById(R.id.im_undo);
        imRedo = (ImageView) findViewById(R.id.im_redo);
        imDelete = (ImageView) findViewById(R.id.im_delete);
        imExport = (ImageView) findViewById(R.id.im_export);

        imUndo.setOnClickListener(this);
        imRedo.setOnClickListener(this);
        imDelete.setOnClickListener(this);
        imExport.setOnClickListener(this);

        widget = (MathWidgetApi) findViewById(R.id.math_widget);
        if (!widget.registerCertificate(ATKCertificate.getBytes())) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.invalid_certificate_title))
                    .setContentText(getString(R.string.invalid_certificate_msg))
                    .setConfirmText(getString(R.string.sure))
                    .show();
            return;
        }

        widget.setOnConfigureListener(this);
        widget.setOnRecognitionListener(this);
        widget.addSearchDir("zip://" + getPackageCodePath() + "!/assets/conf");
        widget.configure("math", "standard");
    }

    @Override
    protected void onDestroy() {
        widget.setOnRecognitionListener(null);
        widget.setOnConfigureListener(null);

        // release widget's resources
        widget.release();
        super.onDestroy();
    }

    @Override
    public void onConfigureBegin(MathWidgetApi mathWidgetApi) {

    }

    @Override
    public void onConfigureEnd(MathWidgetApi mathWidgetApi, boolean success) {
        if (!success) {
            Log.e(TAG, "Unable to configure the Math Widget: " + widget.getErrorString());
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.unable_to_configure_title))
                    .setContentText(widget.getErrorString())
                    .setConfirmText(getString(R.string.sure))
                    .show();
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Math Widget configured!");
        }
    }

    @Override
    public void onRecognitionBegin(MathWidgetApi mathWidgetApi) {

    }

    @Override
    public void onRecognitionEnd(MathWidgetApi mathWidgetApi) {
        renderActionStatus();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Math Widget recognition: " + widget.getResultAsText());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_undo:
                undo();
                break;
            case R.id.im_redo:
                redo();
                break;
            case R.id.im_export:
                export();
                break;
            case R.id.im_delete:
                clear();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (widget.isEmpty()) {
                exitByDoubleClick();
            } else {
                giveUpTheEditor(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                        System.exit(0);
                    }
                });
            }
        }
        return false;
    }

    /**
     * 根据内容变化情况，更新操作条按钮
     */
    private void renderActionStatus() {
        if (widget.isEmpty()) {
            imDelete.setImageResource(R.drawable.icon_delete_disable);
            imExport.setImageResource(R.drawable.icon_export_disable);
            if (widget.canUndo()) {
                imUndo.setImageResource(R.drawable.icon_undo_active);
            } else {
                imUndo.setImageResource(R.drawable.icon_undo_disable);
            }
            if (widget.canRedo()) {
                imRedo.setImageResource(R.drawable.icon_redo_active);
            } else {
                imRedo.setImageResource(R.drawable.icon_redo_disable);
            }
        } else {
            imDelete.setImageResource(R.drawable.icon_delete_active);
            imExport.setImageResource(R.drawable.icon_export_active);
            if (widget.canUndo()) {
                imUndo.setImageResource(R.drawable.icon_undo_active);
            } else {
                imUndo.setImageResource(R.drawable.icon_undo_disable);
            }
            if (widget.canRedo()) {
                imRedo.setImageResource(R.drawable.icon_redo_active);
            } else {
                imRedo.setImageResource(R.drawable.icon_redo_disable);
            }
        }
    }

    /**
     * redo
     */
    private void redo() {
        if (widget.canRedo()) {
            widget.redo();
        }
    }

    /**
     * undo
     */
    private void undo() {
        if (widget.canUndo()) {
            widget.undo();
        }
    }

    /**
     * 清空
     */
    private void clear() {
        if (!widget.isEmpty()) {
            giveUpTheEditor(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                    widget.clear(true);
                }
            });
        }
    }

    /**
     * 导出
     */
    private void export() {
        if (!widget.isEmpty()) {
            Bitmap bitmap = widget.getResultAsImage();
            String laTeX = widget.getResultAsLaTeX();
            String mathML = widget.getResultAsMathML();

            ImageLoader.getInstance().addBitmapToCache("bitmap", bitmap, true);

            Intent intent = new Intent(MainActivity.this, ExportActivity.class);
            intent.putExtra("laTeX", laTeX);
            intent.putExtra("mathML", mathML);
            startActivity(intent);
        }
    }

    /**
     * 显示演示视频
     */
    private void showVideo() {
        VideoDialog dialog = new VideoDialog(this);
        dialog.show();
    }

    /**
     * 放弃编辑
     *
     * @param listener
     */
    private void giveUpTheEditor(SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning))
                .setContentText(getString(R.string.give_up_the_editor))
                .setConfirmText(getString(R.string.sure))
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true)
                .setConfirmClickListener(listener)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                });
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.show();
    }

    /**
     * 双击退出
     */
    private void exitByDoubleClick() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, getString(R.string.click_to_exit), Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void finish() {
        ActivityTack.getInstanse().removeActivity(this);
        super.finish();
    }
}
