package com.huilan.library_videoplay.view;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by liujigang on 2014/8/11 0011.
 */
public class SuperVideoView extends SurfaceView {

    private final Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private boolean screenLock;
    private PlayController controller;

    public SuperVideoView(Context context) {
        super(context);
        initVideoView();
        mContext = context;
    }

    public SuperVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
        mContext = context;
    }

    public SuperVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initVideoView();
        mContext = context;
    }

    private void initVideoView() {
        requestFocus();
        getHolder().addCallback(mSHCallback);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int pos;
            switch (msg.what) {
                case 101:
                    break;
                case 102:
                    if (controller != null) {
//                        System.out.println("播放进度-->" + mMediaPlayer.getCurrentPosition() + "," + mMediaPlayer.getDuration());
                        controller.currentProgress(mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
                    }
                    handler.sendEmptyMessageDelayed(102, 500);
                    break;
            }
        }
    };
    private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;
            openVideo();
            handler.sendEmptyMessageDelayed(102, 500);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

    };
    /**
     * 当准备就绪时,调用该接口
     */
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
            handler.sendEmptyMessageDelayed(102, 1000);
        }
    };
    /**
     * 当完成时调用
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
        }
    };
    /**
     * 播放错误时调用
     */
    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
//            mCurrentBufferPercentage = percent;
        }

    };
    private MediaPlayer mMediaPlayer;
    private Uri mUri;

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            return;
        }
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);
        release();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMediaPlayer.setDataSource(mContext, mUri);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();// 为了不阻塞主线程而异步准备
        } catch (Exception ex) {
            mMediaPlayer = null;
            ex.printStackTrace();
        } finally {
        }
    }

    private int getCurrentProgress() {
        if (isPlaying()) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        mUri = uri;
        openVideo();
        requestLayout();
        invalidate();
    }

    /**
     * 视频是否在播放
     *
     * @return true:播放,false:暂停
     */
    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            return false;
        } else {
            return mMediaPlayer.isPlaying();
        }
    }

    /**
     * 开始
     */
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            handler.sendEmptyMessageDelayed(102, 500);
            System.out.println("开始播放了....");
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 释放
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 销毁当前的播放控件
     */
    public void onDestroy() {
        if (handler != null) {
            handler.removeMessages(102);
            handler = null;
        }
        release();
        controller = null;
        mSurfaceHolder = null;
        System.out.println("SuperVideoView 销毁");
    }

    /**
     * 视频跳转
     *
     * @param msec 跳转到第几秒
     */
    public void seekTo(int msec) {
//        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
        }
    }

    /**
     * 设置屏幕是否锁定
     *
     * @param screenLock true:锁定,false:未锁定
     */
    public void setScreenLock(boolean screenLock) {
        this.screenLock = screenLock;
    }

    /**
     * 获取屏幕锁定状态
     *
     * @return true:锁定,false:未锁定
     */
    public boolean isScreenLock() {
        return screenLock;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (screenLock) {
            return true;
        }


        return super.onTouchEvent(event);
    }

    public void setOnProgressListener(PlayController controller) {
        this.controller = controller;
    }

    public interface PlayController {
        void currentProgress(int current, int total);
    }

}
