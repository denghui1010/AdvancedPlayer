package com.huilan.library_videoplay.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.huilan.library_videoplay.AdvancedPlayer;
import com.huilan.library_videoplay.VideoViewPlay;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button videoPlay;
    private Button advancedplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intId();
    }

    private void intId() {
        videoPlay = (Button) findViewById(R.id.btn_videoViewPlay);
        advancedplayer = (Button) findViewById(R.id.btn_advancedplayer);
        videoPlay = (Button) findViewById(R.id.btn_videoViewPlay);
        videoPlay = (Button) findViewById(R.id.btn_videoViewPlay);
        videoPlay = (Button) findViewById(R.id.btn_videoViewPlay);
        videoPlay.setOnClickListener(this);
        advancedplayer.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_videoViewPlay:
                intent = new Intent(getApplication(), VideoViewPlay.class);
                startActivity(intent);
                break;
            case R.id.btn_advancedplayer:
                intent = new Intent(getApplication(), AdvancedPlayer.class);
                intent.putExtra("path","http://202.108.17.115/v.cctv.com/flash/mp4video32/TMS/2014/02/12/b32eaf65cc07489aabc748a37bfd4b4b_h264418000nero_aac32.mp4");
                startActivity(intent);
                break;
        }
    }
}
