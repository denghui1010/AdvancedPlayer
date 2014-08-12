package com.huilan.library_videoplay;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huilan.library_videoplay.view.SuperVideoView;

import java.util.Formatter;
import java.util.Locale;

/**
 * 基于VideoView的简单的播放器,仅有播放的进度条,快进按钮,后退按钮
 * path:播放的路径
 */
public class AdvancedPlayer extends Activity implements OnClickListener, SuperVideoView.PlayController, SeekBar.OnSeekBarChangeListener {
    private Button PlayOrPause;
    private SuperVideoView superVideoView;
    private SeekBar progress;
    private Button back;
    private TextView videoName;
    private TextView nowTime;
    private TextView totalTime;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private View videoTop;
    private View videoBottom;
    private Animation topAnimation;
    private Animation bottomAnimation;
    private int state;
    private boolean isShow = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int pos;
            switch (msg.what) {
                case 101:
                    autoShowOrHiden();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_advancedplayer);
        String path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path)) {
            path = "/mnt/sdcard2/sm.mp4";
        }
        initView();

        superVideoView.setOnProgressListener(this);

        superVideoView.setVideoPath(path);

        superVideoView.start();
        handler.sendEmptyMessageDelayed(101, 5000);
    }

    private void initView() {
        superVideoView = (SuperVideoView) findViewById(R.id.vv_video_view);
        back = (Button) findViewById(R.id.btn_back);
        PlayOrPause = (Button) findViewById(R.id.btn_PlayOrPause);
        progress = (SeekBar) findViewById(R.id.seekBar_progress);

        videoName = (TextView) findViewById(R.id.tv_name);
        nowTime = (TextView) findViewById(R.id.tv_nowTime);
        totalTime = (TextView) findViewById(R.id.tv_totalTime);

        PlayOrPause.setOnClickListener(this);
        back.setOnClickListener(this);
        progress.setOnSeekBarChangeListener(this);

        videoTop = findViewById(R.id.in_videoTop);
        videoBottom = findViewById(R.id.in_videoBottom);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//        if (i == R.id.btnplay) {
//            superVideoView.start();
//        } else if (i == R.id.btnpause) {
//            superVideoView.pause();
//        } else if (i == R.id.btnstop) {
//            superVideoView.stop();
//        }
        if (i == R.id.btn_back) {
            finish();
//            onDestroy();
//            onBackPressed();
        } else if (i == R.id.btn_PlayOrPause) {
            if (superVideoView.isPlaying()) {
                superVideoView.pause();
                PlayOrPause.setBackgroundResource(android.R.drawable.ic_media_pause);
            } else {
                superVideoView.start();
                PlayOrPause.setBackgroundResource(android.R.drawable.ic_media_play);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (superVideoView != null) {
            superVideoView.onDestroy();
            superVideoView = null;
        }
    }

    @Override
    public void currentProgress(int current, int total) {
        progress.setMax(total);
        progress.setProgress(current);
        nowTime.setText(stringForTime(current));
        totalTime.setText(stringForTime(total));
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(101);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                autoShowOrHiden();
//                if (isShow) {
//                    handler.sendEmptyMessageDelayed(101, 5000);
//                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 自动显示或者隐藏顶部或者底部
     */
    private void autoShowOrHiden() {
        if (state != 0) {
            return;
        }
        if (isShow) {//消失
            topAnimation = animationFactory(0, 0, 0, -1, 500);
            bottomAnimation = animationFactory(0, 0, 0, 1, 500);
        } else {//显示
            topAnimation = animationFactory(0, 0, -1, 0, 500);
            bottomAnimation = animationFactory(0, 0, 1, 0, 500);

            handler.sendEmptyMessageDelayed(101, 5000);
        }
        topAnimation.setFillAfter(true);
        bottomAnimation.setFillAfter(true);
        topAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                state += 1;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                state -= 1;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        bottomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                state += 1;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                state -= 1;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        videoTop.startAnimation(topAnimation);
        videoBottom.startAnimation(bottomAnimation);
        isShow = !isShow;
    }


    private Animation animationFactory(float fromX, float toX, float fromY, float toY, int duration) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF,
                toX, Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY);
        animation.setDuration(duration);
        return animation;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        System.out.println("onProgressChanged-->"+progress+","+fromUser);
        if(fromUser){
            superVideoView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        System.out.println("onStartTrackingTouch-->"+seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println("onStartTrackingTouch-->"+seekBar);
    }
}
