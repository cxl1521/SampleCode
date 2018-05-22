package com.backgroundmusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MediaPlayerService extends Service
                            implements MediaPlayer.OnPreparedListener,
                                        MediaPlayer.OnErrorListener,
                                        MediaPlayer.OnCompletionListener,
                                        AudioManager.OnAudioFocusChangeListener {

    public static final String
            ACTION_PLAY = "tw.android.mediaplayer.action.PLAY",
            ACTION_PAUSE = "tw.android.mediaplayer.action.PAUSE",
            ACTION_SET_REPEAT = "tw.android.mediaplayer.action.SET_REPEAT",
            ACTION_CANCEL_REPEAT = "tw.android.mediaplayer.action.CANCEL_REPEAT",
            ACTION_GOTO = "tw.android.mediaplayer.action.GOTO";

    // 程式使用的MediaPlayer物件
    private MediaPlayer mMediaPlayer = null;

    // 用來記錄是否MediaPlayer物件需要執行prepareAsync()
    private boolean mbIsInitialised = true,
            mbAudioFileFound = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 從 Android 系統的資料庫中取得播放檔
        // MediaStore 是用來指定影像、音訊或影片型態的資料
        // 這種方式要設定 stream type，
        // 這個播放檔必須利用程式畫面的"加入 mp3 檔"按鈕加入
        ContentResolver contRes = getContentResolver();
        String[] columns = {
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns._ID};
        Cursor c = contRes.query(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);

        Uri uri = null;
        if (c == null) {
            Toast.makeText(MediaPlayerService.this, "Content Resolver 錯誤！", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        else if (!c.moveToFirst()) {
            Toast.makeText(MediaPlayerService.this, "資料庫中沒有資料！", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        else {
            do {
                String title = c.getString(c.getColumnIndex(MediaStore.MediaColumns.TITLE));
                if (title.equals("my mp3")) {
                    mbAudioFileFound = true;
                    break;
                }
            } while(c.moveToNext());

            if (! mbAudioFileFound) {
                Toast.makeText(MediaPlayerService.this, "找不到指定的 mp3 檔！",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            int idColumn = c.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            long id = c.getLong(idColumn);
            uri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMediaPlayer.setDataSource(this, uri);
        } catch (Exception e) {
            Toast.makeText(MediaPlayerService.this, "指定的播放檔錯誤！", Toast.LENGTH_LONG)
                    .show();
        }

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        // 設定 Media Player 在背景執行時，讓 CPU 維持運轉
        // 如果播放的是來自網路的 streaming audio，
        // 還要設定網路維持運作
        // 只能在實體設備上使用，在模擬器執行會產生錯誤
//		mMediaPlayer.setWakeMode(getApplicationContext(),
//				PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mbAudioFileFound) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (! mbAudioFileFound) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        if (intent.getAction().equals(ACTION_PLAY))
            if (mbIsInitialised) {
                mMediaPlayer.prepareAsync();
                mbIsInitialised = false;
            }
            else
                mMediaPlayer.start();
        else if (intent.getAction().equals(ACTION_PAUSE))
            mMediaPlayer.pause();
        else if (intent.getAction().equals(ACTION_SET_REPEAT))
            mMediaPlayer.setLooping(true);
        else if (intent.getAction().equals(ACTION_CANCEL_REPEAT))
            mMediaPlayer.setLooping(false);
        else if (intent.getAction().equals(ACTION_GOTO)) {
            int seconds = intent.getIntExtra("GOTO_POSITION_SECONDS", 0);
            mMediaPlayer.seekTo(seconds * 1000); // 以毫秒（千分之一秒）為單位
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mMediaPlayer == null)
            return;

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // 程式取得聲音播放權
                mMediaPlayer.setVolume(0.8f, 0.8f);
                mMediaPlayer.start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // 程式尚失聲音播放權，而且時間可能很久
                stopSelf();		// 結束這個Service
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // 程式尚失聲音播放權，但預期很快就會再取得
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 程式尚失聲音播放權，但是可以用很小的音量繼續播放
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mMediaPlayer.release();
        mMediaPlayer = null;

        stopForeground(true);

        mbIsInitialised = true;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        mediaPlayer.release();
        mediaPlayer = null;

        Toast.makeText(MediaPlayerService.this, "發生錯誤，停止播放", Toast.LENGTH_LONG)
                .show();

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // 是否取得 audio focus
        AudioManager audioMgr =
                (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int r = audioMgr.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (r != AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            mediaPlayer.setVolume(0.1f, 0.1f);	// 降低音量

        mediaPlayer.start();

        Intent it = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent penIt = PendingIntent.getActivity(
                getApplicationContext(), 0, it,
                PendingIntent. FLAG_CANCEL_CURRENT);

        Notification noti = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker("播放背景音樂")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("背景音樂播放中...")
                .setContentIntent(penIt)
                .build();

        startForeground(1, noti);

        Toast.makeText(MediaPlayerService.this, "開始播放", Toast.LENGTH_LONG)
                .show();
    }
}
