package com.huilan.library_videoplay.view;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by liujigang on 2014/8/11 0011.
 */
public class SuperVideoView extends SurfaceView {

    private final Context mContext;
    private SurfaceHolder mSurfaceHolder;

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

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;
            openVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
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

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        openVideo();
        requestLayout();
        invalidate();
    }

    /**
     * release the media player in any state
     */
    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
//            mPendingSubtitleTracks.clear();
//            mCurrentState = STATE_IDLE;
//            if (cleartargetstate) {
//                mTargetState = STATE_IDLE;
//            }
        }
    }

}
