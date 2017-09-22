package com.helloarron.touchmath.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.helloarron.touchmath.R;
import com.helloarron.touchmath.base.TMBaseActivity;
import com.helloarron.touchmath.utils.ImageLoader;
import com.helloarron.touchmath.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExportActivity extends TMBaseActivity implements View.OnClickListener {

    private String laTeX, mathML;
    private Bitmap bitmap;

    private TextView tvLaTeX, tvMathML;
    private ImageView imExport;

    private Button btnCopyLaTeX, btnCopyMathML, btnExportPNG, btnExportJPG;

    ClipboardManager clipboard;
    ClipData clipData;

    // 首先默认个文件保存路径
    // 保存到SD卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        setTitle(getString(R.string.export_title));
        setLeftAction(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorDialog();
            return;
        }
        laTeX = intent.getStringExtra("laTeX");
        mathML = intent.getStringExtra("mathML");
        btnCopyLaTeX = (Button) findViewById(R.id.btn_copy_latex);
        btnCopyMathML = (Button) findViewById(R.id.btn_copy_mathml);
        btnExportPNG = (Button) findViewById(R.id.btn_export_png);
        btnExportJPG = (Button) findViewById(R.id.btn_export_jpg);

        bitmap = ImageLoader.getInstance().getBitmapFromCache("bitmap");

        tvLaTeX = (TextView) findViewById(R.id.tv_laTeX);
        tvMathML = (TextView) findViewById(R.id.tv_mathML);
        imExport = (ImageView) findViewById(R.id.im_bitmap);

        tvLaTeX.setText(laTeX);
        tvMathML.setText(mathML);
        imExport.setImageBitmap(bitmap);

        btnCopyLaTeX.setOnClickListener(this);
        btnCopyMathML.setOnClickListener(this);
        btnExportPNG.setOnClickListener(this);
        btnExportJPG.setOnClickListener(this);
    }

    private void showErrorDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.error))
                .setContentText(getString(R.string.some_errors))
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy_latex:
                clipData = ClipData.newPlainText("laTeX", laTeX);
                clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(this, getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_copy_mathml:
                clipData = ClipData.newPlainText("mathML", mathML);
                clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(this, getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_export_png:
                Util.saveImageToGallery(this, bitmap, true);
                break;
            case R.id.btn_export_jpg:
                Util.saveImageToGallery(this, bitmap, false);
                break;
        }
    }

}
