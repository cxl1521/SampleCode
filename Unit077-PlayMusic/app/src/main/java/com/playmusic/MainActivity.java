package com.playmusic;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity
                            implements MediaPlayer.OnPreparedListener,
                                    MediaPlayer.OnErrorListener,
                                    MediaPlayer.OnCompletionListener {

    private ImageButton mBtnMediaPlayPause,
                        mBtnMediaStop,
                        mBtnMediaGoto;

    private ToggleButton mBtnMediaRepeat;

    private EditText mEdtMediaGoto;

    // 程式使用的MediaPlayer物件
    private MediaPlayer mMediaPlayer = null;

    // 用來記錄是否MediaPlayer物件需要執行prepareAsync()
    private Boolean mbIsInitialised = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnMediaPlayPause = (ImageButton)findViewById(R.id.btnMediaPlayPause);
        mBtnMediaStop = (ImageButton)findViewById(R.id.btnMediaStop);
        mBtnMediaRepeat = (ToggleButton)findViewById(R.id.btnMediaRepeat);
        mBtnMediaGoto = (ImageButton)findViewById(R.id.btnMediaGoto);
        mEdtMediaGoto = (EditText)findViewById(R.id.edtMediaGoto);

        mBtnMediaPlayPause.setOnClickListener(btnMediaPlayPauseOnClick);
        mBtnMediaStop.setOnClickListener(btnMediaStopOnClick);
        mBtnMediaRepeat.setOnClickListener(btnMediaRepeatOnClick);
        mBtnMediaGoto.setOnClickListener(btnMediaGotoOnClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMediaPlayer = new MediaPlayer();

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.song);

        try {
            mMediaPlayer.setDataSource(this, uri);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "指定的音樂檔錯誤！", Toast.LENGTH_LONG)
                    .show();
        }

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mBtnMediaPlayPause.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        mediaPlayer.release();
        mediaPlayer = null;

        Toast.makeText(MainActivity.this, "發生錯誤，停止播放", Toast.LENGTH_LONG)
                .show();

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.seekTo(0);
        mediaPlayer.start();

        Toast.makeText(MainActivity.this, "開始播放", Toast.LENGTH_LONG)
                .show();
    }

    private View.OnClickListener btnMediaPlayPauseOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mMediaPlayer.isPlaying()) {
                mBtnMediaPlayPause.setImageResource(android.R.drawable.ic_media_play);
                mMediaPlayer.pause();
            } else {
                mBtnMediaPlayPause.setImageResource(android.R.drawable.ic_media_pause);

                if (mbIsInitialised) {
                    mMediaPlayer.prepareAsync();
                    mbIsInitialised = false;
                } else
                    mMediaPlayer.start();
            }
        }
    };

    private View.OnClickListener btnMediaStopOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMediaPlayer.stop();

            // 停止播放後必須再執行 prepareAsync()
            // 或 prepare() 才能重新播放。
            mbIsInitialised = true;
            mBtnMediaPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
    };

    private View.OnClickListener btnMediaRepeatOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (((ToggleButton)v).isChecked())
                mMediaPlayer.setLooping(true);
            else
                mMediaPlayer.setLooping(false);
        }
    };

    private View.OnClickListener btnMediaGotoOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mEdtMediaGoto.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this,
                        "請先輸入要播放的位置（以秒為單位）",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            int seconds = Integer.parseInt(mEdtMediaGoto.getText().toString());
            mMediaPlayer.seekTo(seconds * 1000); // 以毫秒（千分之一秒）為單位
        }
    };

}
