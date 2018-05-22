package com.backgroundmusic;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                        implements View.OnClickListener {

    private final int REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE = 100;

    private Button mBtnAddToMediaStore,
            mBtnStart, mBtnPause,
            mBtnStop, mBtnSetRepeat,
            mBtnCancelRepeat, mBtnGoto;

    private EditText mEdtGoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStart = (Button)findViewById(R.id.btnStart);
        mBtnPause = (Button)findViewById(R.id.btnPause);
        mBtnStop = (Button)findViewById(R.id.btnStop);
        mBtnSetRepeat = (Button)findViewById(R.id.btnSetRepeat);
        mBtnCancelRepeat = (Button)findViewById(R.id.btnCancelRepeat);
        mBtnGoto = (Button)findViewById(R.id.btnGoto);
        mBtnAddToMediaStore = (Button)findViewById(R.id.btnAddToMediaStore);
        mEdtGoto = (EditText)findViewById(R.id.edtGoto);

        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnSetRepeat.setOnClickListener(this);
        mBtnCancelRepeat.setOnClickListener(this);
        mBtnGoto.setOnClickListener(this);
        mBtnAddToMediaStore.setOnClickListener(btnAddToMediaStoreOnClick);

        askForWriteExternalStoragePermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 檢查收到的權限要求編號是否和我們送出的相同
        if (requestCode == REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "取得 WRITE_EXTERNAL_STORAGE 權限", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClick(View view) {
        Intent it;

        switch(view.getId()) {
            case R.id.btnStart:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_PLAY);
                startService(it);
                break;
            case R.id.btnPause:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_PAUSE);
                startService(it);
                break;
            case R.id.btnStop:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                stopService(it);
                break;
            case R.id.btnSetRepeat:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_SET_REPEAT);
                startService(it);
                break;
            case R.id.btnCancelRepeat:
                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_CANCEL_REPEAT);
                startService(it);
                break;
            case R.id.btnGoto:
                if (mEdtGoto.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,
                            "請先輸入要播放的位置（以秒為單位）",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                int seconds = Integer.parseInt(mEdtGoto.getText().toString());

                it = new Intent(MainActivity.this, MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_GOTO);
                it.putExtra("GOTO_POSITION_SECONDS", seconds);
                startService(it);
                break;
        }
    }

    private View.OnClickListener btnAddToMediaStoreOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            // 檢查是否已經取得權限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                ContentValues val = new ContentValues();
                val.put(MediaStore.MediaColumns.TITLE, "my mp3");
                val.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                val.put(MediaStore.MediaColumns.DATA, "/sdcard/song.mp3");
                ContentResolver contRes = getContentResolver();
                Uri newUri = contRes.insert(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        val);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
            } else
                askForWriteExternalStoragePermission();
        }
    };

    private void askForWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // 這項功能尚未取得使用者的同意
            // 開始執行徵詢使用者的流程
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder altDlgBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("App需要讀寫SD卡中的資料。");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 顯示詢問使用者是否同意功能權限的對話盒
                                // 使用者答覆後會執行onRequestPermissionsResult()
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE);
                            }
                        });
                altDlgBuilder.show();

                return;
            } else {
                // 顯示詢問使用者是否同意功能權限的對話盒
                // 使用者答覆後會執行onRequestPermissionsResult()
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE);

                return;
            }
        }
    }

}
