package com.recordsound;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                            implements MediaPlayer.OnPreparedListener,
                                        MediaPlayer.OnErrorListener,
                                        MediaPlayer.OnCompletionListener {

    private final int REQUEST_MULTI_PERMISSIONS = 100;
    private final String FILE_NAME = "my_recorded_audio.3gp";

    private Button mBtnAudioRecoOnOff,
                    mBtnPlayAudioOnOff;

    private boolean mBoolRecording = false,
                    mBoolPlaying = false;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAudioRecoOnOff = (Button)findViewById(R.id.btnAudioRecoOnOff);
        mBtnPlayAudioOnOff = (Button)findViewById(R.id.btnPlayAudioOnOff);

        mBtnAudioRecoOnOff.setOnClickListener(btnAudioRecoOnOffOnClick);
        mBtnPlayAudioOnOff.setOnClickListener(btnPlayAudioOnOffOnClick);

        askForPermissions();
    }

    private View.OnClickListener btnAudioRecoOnOffOnClick = new
        View.OnClickListener() {
            public void onClick(View v) {
                if (mBoolRecording) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;

                    mBoolRecording = false;
                    mBtnAudioRecoOnOff.setText("開始錄音");
                } else {
                    if (askForPermissions()) {
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecorder.setOutputFile(
                                Environment.getExternalStorageDirectory().getPath() +
                                        "/" + FILE_NAME);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                        try {
                            mRecorder.prepare();
                            mRecorder.start();
                            mBoolRecording = true;
                            mBtnAudioRecoOnOff.setText("停止錄音");
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "MediaRecorder 錯誤!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        };

    private View.OnClickListener btnPlayAudioOnOffOnClick = new
        View.OnClickListener() {
            public void onClick(View v) {
                if (mBoolRecording) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;

                    mBoolRecording = false;
                    mBtnAudioRecoOnOff.setText("開始錄音");
                }

                if (mBoolPlaying) {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;

                    mBoolPlaying = false;
                    mBtnPlayAudioOnOff.setText("開始播放");
                } else {
                    mPlayer = new MediaPlayer();

                    try {
                        mPlayer.setDataSource(
                                Environment.getExternalStorageDirectory().getAbsolutePath() +
                                        "/" + FILE_NAME);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "MediaPlayer 錯誤!",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                    mPlayer.setOnPreparedListener(MainActivity.this);
                    mPlayer.setOnErrorListener(MainActivity.this);
                    mPlayer.setOnCompletionListener(MainActivity.this);

                    mPlayer.prepareAsync();

                    mBoolPlaying = true;
                    mBtnPlayAudioOnOff.setText("停止播放");
                }
            }
        };

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mPlayer.release();
        mPlayer = null;

        mBoolPlaying = false;
        mBtnPlayAudioOnOff.setText("開始播放");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        mPlayer.release();
        mPlayer = null;

        Toast.makeText(MainActivity.this, "MediaPlayer錯誤!", Toast.LENGTH_LONG)
                .show();

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mPlayer.setVolume(1.0f, 1.0f);
        mPlayer.start();

        Toast.makeText(MainActivity.this, "開始播放...", Toast.LENGTH_LONG)
                .show();
    }

    private  boolean askForPermissions() {
        // App需要用的功能權限清單
        String[] permissions= new String[] {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // 檢查是否已經取得權限
        final List<String> listPermissionsNeeded = new ArrayList<>();
        boolean bShowPermissionRationale = false;

        for (String p: permissions) {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

                // 檢查是否需要顯示說明
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this, p))
                    bShowPermissionRationale = true;
            }
        }

        // 向使用者徵詢還沒有許可的權限
        if (!listPermissionsNeeded.isEmpty()) {
            if (bShowPermissionRationale) {
                AlertDialog.Builder altDlgBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("App需要您的許可才能執行。");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                                        REQUEST_MULTI_PERMISSIONS);
                            }
                        });
                altDlgBuilder.show();
            } else
                ActivityCompat.requestPermissions(MainActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_MULTI_PERMISSIONS);

            return false;
        }

        return true;
    }
}
