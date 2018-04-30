package com.intentfilter;

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
            mBtnEditImg,
            mBtnViewImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnBrowseWWW = (Button)findViewById(R.id.btnBrowseWWW);
        mBtnEditImg = (Button)findViewById(R.id.btnEditImg);
        mBtnViewImg = (Button)findViewById(R.id.btnViewImg);

        mBtnBrowseWWW.setOnClickListener(btnBrowseWWWOnClick);
        mBtnEditImg.setOnClickListener(btnEditImgOnClick);
        mBtnViewImg.setOnClickListener(btnViewImgOnClick);
    }

    private View.OnClickListener btnBrowseWWWOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Uri uri = Uri.parse("http://developer.android.com/");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }
    };

    private View.OnClickListener btnEditImgOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent(Intent.ACTION_EDIT);
            File file = new File("/sdcard/image.png");
            it.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(it);
        }
    };

    private View.OnClickListener btnViewImgOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            File file = new File("/sdcard/image.png");
            it.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(it);
        }
    };
}
