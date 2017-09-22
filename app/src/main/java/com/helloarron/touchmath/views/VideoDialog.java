package com.helloarron.touchmath.views;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.helloarron.touchmath.R;

/**
 * Created by arron on 2017/3/10.
 */

public class VideoDialog extends BaseAlertDialog {
    Context context;

    public VideoDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected VideoDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video);

        final VideoView videoView = (VideoView) findViewById(R.id.vv_video);

        videoView.setVideoPath("android.resource://" + getContext().getPackageName() + "/" + R.raw.math_video);
        videoView.requestFocus();
        videoView.setZOrderOnTop(true);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
    }
}
