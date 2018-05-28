package com.playvideo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity
                        implements MediaPlayer.OnErrorListener,
                                MediaPlayer.OnCompletionListener {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (VideoView)findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);

        Uri uri = Uri.parse("android.resource://" +
                getPackageName() + "/" + R.raw.video);
        mVideoView.setVideoURI(uri);
    }

    @Override
    protected void onResume() {
        mVideoView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoView.stopPlayback();
        super.onPause();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(MainActivity.this, "播放完畢！", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Toast.makeText(MainActivity.this, "發生錯誤！", Toast.LENGTH_LONG)
                .show();
        return true;
    }
}
