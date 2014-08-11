package com.huilan.library_videoplay.sample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.huilan.library_videoplay.VideoViewPlay;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intId();
    }

    private void intId() {
        play = (Button) findViewById(R.id.btn_videoViewPlay);
        play = (Button) findViewById(R.id.btn_videoViewPlay);
        play = (Button) findViewById(R.id.btn_videoViewPlay);
        play = (Button) findViewById(R.id.btn_videoViewPlay);
        play = (Button) findViewById(R.id.btn_videoViewPlay);
        play.setOnClickListener(this);
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
       switch (v.getId()){
           case R.id.btn_videoViewPlay:
               Intent intent = new Intent(getApplication(), VideoViewPlay.class);
               startActivity(intent);
               break;
       }
    }
}
