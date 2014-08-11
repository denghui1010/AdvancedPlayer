package com.huilan.library_videoplay;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.huilan.library_videoplay.view.SuperVideoView;

/**
 * 基于VideoView的简单的播放器,仅有播放的进度条,快进按钮,后退按钮
 */
public class AdvancedPlayer extends Activity implements OnClickListener {
    private Button btnplay;
    private Button btnstop;
    private Button btnpause;
    private SuperVideoView superVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_videoview);
        String path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path)) {
            path = "/mnt/sdcard2/sm.mp4";
        }
        initView();
//
//        mVideoView.setVideoPath(path);
//
//        MediaController controller = new MediaController(this);
//
//        mVideoView.setMediaController(controller);
//
//        mVideoView.start();
    }

    private void initView() {
        btnplay = (Button) findViewById(R.id.btnplay);
        btnpause = (Button) findViewById(R.id.btnpause);
        btnstop = (Button) findViewById(R.id.btnstop);
        superVideoView = (SuperVideoView) findViewById(R.id.vv_video_view);

        btnplay.setOnClickListener(this);
        btnpause.setOnClickListener(this);
        btnstop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnplay) {
//            superVideoView.start();
        } else if (i == R.id.btnpause) {
//            mVideoView.pause();
        } else if (i == R.id.btnstop) {
//            superVideoView.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        superVideoView = null;
    }
}
