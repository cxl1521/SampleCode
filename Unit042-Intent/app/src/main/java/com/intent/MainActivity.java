package com.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button mBtnBrowseWWW,
                    mBtnPlayMP3,
                    mBtnViewImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnBrowseWWW = (Button)findViewById(R.id.btnBrowseWWW);
        mBtnPlayMP3 = (Button)findViewById(R.id.btnPlayMP3);
        mBtnViewImg = (Button)findViewById(R.id.btnViewImg);

        mBtnBrowseWWW.setOnClickListener(btnBrowseWWWOnClick);
        mBtnPlayMP3.setOnClickListener(btnPlayMP3OnClick);
        mBtnViewImg.setOnClickListener(btnViewImgOnClick);
    }

    private View.OnClickListener btnBrowseWWWOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Uri uri = Uri.parse("http://developer.android.com/");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }
    };

    private View.OnClickListener btnPlayMP3OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            String sMp3File = Environment.getExternalStorageDirectory().getPath() +
                    File.separator + "song.mp3";
            File file = new File(sMp3File);
            boolean b = file.exists();
            it.setDataAndType(Uri.fromFile(file), "audio/*");
            startActivity(it);
        }
    };

    private View.OnClickListener btnViewImgOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            String sImgFile = Environment.getExternalStorageDirectory().getPath() +
                    File.separator + "image.png";
            File file = new File(sImgFile);
            it.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(it);
        }
    };

}
